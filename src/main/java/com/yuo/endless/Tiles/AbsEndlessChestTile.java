package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.AbsEndlessChest;
import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Container.Chest.CompressorChestContainer;
import com.yuo.endless.Container.Chest.InfinityBoxContainer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

@OnlyIn(value = Dist.CLIENT, _interface = IChestLid.class)
public class AbsEndlessChestTile extends LockableLootTileEntity implements IChestLid, ITickableTileEntity {
    protected float lidAngle;
    protected float prevLidAngle;
    protected int numPlayersUsing;
    private int ticksSinceSync;
    private final Supplier<Block> blockSupplier;
    private final EndlessChestType type;
    protected final InfinityStackHandler stackHandler;
    private net.minecraftforge.common.util.LazyOptional<IItemHandlerModifiable> chestHandler;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String NBT_COUNT = "infinity_count";

    public AbsEndlessChestTile(TileEntityType<?> typeIn, EndlessChestType chestType, Supplier<Block> supplier) {
        super(typeIn);
        this.type = chestType;
        this.stackHandler = new InfinityStackHandler(chestType.size);
        this.blockSupplier = supplier;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.fillWithLoot(null);
        if (stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }

        this.stackHandler.getStacks().set(index, stack);
        this.markDirty();
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return super.decrStackSize(index, count);
    }

    @Override
    public int getInventoryStackLimit() {
        return this.type.stackLimit;
    }

    public Block getBlock() {
        return blockSupplier.get();
    }

    @Override
    public int getSizeInventory() {
        return this.getItems().size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.stackHandler.getStacks()) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        NbtRead(nbt);
    }

    public void NbtRead(CompoundNBT nbt) {
        this.stackHandler.setStacks(NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY));
        if (!this.checkLootAndRead(nbt)) {
            loadAllItems(nbt, this.stackHandler.getStacks());
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        NbtWrite(compound);
        return compound;
    }

    public CompoundNBT NbtWrite(CompoundNBT compound) {
        if (!this.checkLootAndWrite(compound)) {
            saveAllItems(compound, this.stackHandler.getStacks());
        }
        return compound;
    }

    /**
     * 将所有物品保存到nbt中
     * @param nbt nbt
     * @param stacks 物品列表
     * @return new nbt
     */
    public static CompoundNBT saveAllItems(CompoundNBT nbt, NonNullList<ItemStack> stacks) {
        ListNBT listNBT = new ListNBT();

        for(int i = 0; i < stacks.size(); ++i) {
            ItemStack stack = stacks.get(i);
            if (!stack.isEmpty()) {
                CompoundNBT tag = new CompoundNBT();
                tag.putByte("Slot", (byte)i);
                stack.write(tag);
                tag.putInt(NBT_COUNT, stack.getCount());
                listNBT.add(tag);
            }
        }

        if (!listNBT.isEmpty()) {
            nbt.put("Items", listNBT);
        }

        return nbt;
    }

    /**
     * 从nbt中读取物品列表
     * @param nbt nbt
     * @param stacks 物品列表
     */
    public static void loadAllItems(CompoundNBT nbt, NonNullList<ItemStack> stacks) {
        ListNBT listNBT = nbt.getList("Items", 10);

        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundNBT tag = listNBT.getCompound(i);
            int slot = tag.getByte("Slot") & 255;
            if (slot < stacks.size()) {
                ItemStack stack = ItemStack.read(tag);
                int count = tag.getInt(NBT_COUNT);
                stack.setCount(count);
                stacks.set(slot, stack);
            }
        }
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("gui.endless." + type.getName() + "_chest");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return ChestContainer.createGeneric9X3(id, player, this);
    }

    public void tick() {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;
        this.numPlayersUsing = calculatePlayersUsingSync(this.world, this, this.ticksSinceSync, i, j, k, this.numPlayersUsing);
        this.prevLidAngle = this.lidAngle;
        float f = 0.1F;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            this.playSound(SoundEvents.BLOCK_CHEST_OPEN);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f1 = this.lidAngle;
            if (this.numPlayersUsing > 0) {
                this.lidAngle += f;
            } else this.lidAngle -= f;

            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            float f2 = 0.5F;
            if (this.lidAngle < f2 && f1 >= f2) {
                this.playSound(SoundEvents.BLOCK_CHEST_CLOSE);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }

    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (world != null) {
            handleUpdateTag(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
        }
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT compound = super.getUpdateTag();
        NbtWrite(compound);
        return compound;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        NbtRead(tag);
    }

    public static int calculatePlayersUsingSync(World world, LockableTileEntity lockableTile, int ticksSinceSync, int x, int y, int z, int numPlayersUsing) {
        if (world != null && !world.isRemote && numPlayersUsing != 0 && (ticksSinceSync + x + y + z) % 200 == 0) {
            numPlayersUsing = calculatePlayersUsing(world, lockableTile, x, y, z);
        }

        return numPlayersUsing;
    }

    public static int calculatePlayersUsing(World world, LockableTileEntity lockableTile, int x, int y, int z) {
        int i = 0;
        float f = 5.0f;

        for (PlayerEntity playerentity : world.getEntitiesWithinAABB(PlayerEntity.class,
                new AxisAlignedBB((float) x - f, (float) y - f, (float) z - f, (float) (x + 1) + f, (float) (y + 1) + f, (float) (z + 1) + f))) {
            Container openContainer = playerentity.openContainer;
            if (openContainer instanceof CompressorChestContainer || openContainer instanceof InfinityBoxContainer) {
                ++i;
            }
        }

        return i;
    }

    private void playSound(SoundEvent soundIn) {
        double d0 = (double) this.pos.getX() + 0.5D;
        double d1 = (double) this.pos.getY() + 0.5D;
        double d2 = (double) this.pos.getZ() + 0.5D;
        if (world != null)
            this.world.playSound(null, d0, d1, d2, soundIn, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    @Override
    public void openInventory(PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.onOpenOrClose();
        }

    }

    @Override
    public void closeInventory(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.onOpenOrClose();
        }

    }

    protected void onOpenOrClose() {
        if (world != null) {
            Block block = this.getBlockState().getBlock();
            if (block instanceof AbsEndlessChest) {
                this.world.addBlockEvent(this.pos, block, 1, this.numPlayersUsing);
                this.world.notifyNeighborsOfStateChange(this.pos, block);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getLidAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }

    public static int getPlayersUsing(IBlockReader reader, BlockPos posIn) {
        BlockState blockstate = reader.getBlockState(posIn);
        if (blockstate.hasTileEntity()) {
            TileEntity tileentity = reader.getTileEntity(posIn);
            if (tileentity instanceof AbsEndlessChestTile) {
                return ((AbsEndlessChestTile) tileentity).numPlayersUsing;
            }
        }

        return 0;
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.stackHandler.getStacks();
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.stackHandler.setStacks(itemsIn);
    }

    //    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        if (this.chestHandler != null) {
            net.minecraftforge.common.util.LazyOptional<?> oldHandler = this.chestHandler;
            this.chestHandler = null;
            oldHandler.invalidate();
        }
    }

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> cap, Direction side) {
        if (!this.removed && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (this.chestHandler == null)
                this.chestHandler = net.minecraftforge.common.util.LazyOptional.of(this::createHandler);
            return this.chestHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Nonnull
    private IItemHandlerModifiable createHandler() {
        BlockState state = this.getBlockState();
        if (!(state.getBlock() instanceof AbsEndlessChest)) {
            return new InvWrapper(this);
        }
        IInventory inv = AbsEndlessChest.getChestInventory((AbsEndlessChest) state.getBlock(), state, getWorld(), getPos(), true);
        return new InvWrapper(inv == null ? this : inv);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        if (chestHandler != null)
            chestHandler.invalidate();
    }
}
