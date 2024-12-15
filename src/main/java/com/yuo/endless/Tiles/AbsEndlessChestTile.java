package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.AbsEndlessChest;
import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Container.Chest.CompressorChestContainer;
import com.yuo.endless.Container.Chest.InfinityBoxContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public abstract class AbsEndlessChestTile extends RandomizableContainerBlockEntity implements LidBlockEntity {
    protected float lidAngle;
    protected float prevLidAngle;
    private int ticksSinceSync;
    private final Supplier<Block> blockSupplier;
    private final EndlessChestType type;
    protected final InfinityStackHandler stackHandler;
    private final ContainerOpenersCounter openersCounter;
    private final ChestLidController chestLidController;
    private net.minecraftforge.common.util.LazyOptional<IItemHandlerModifiable> chestHandler;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String NBT_COUNT = "infinity_count";

    public AbsEndlessChestTile(BlockEntityType<?> typeIn, EndlessChestType chestType, Supplier<Block> supplier){
        this(typeIn, chestType, supplier, null, null);
    }

    public AbsEndlessChestTile(BlockEntityType<?> typeIn, EndlessChestType chestType, Supplier<Block> supplier, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.type = chestType;
        this.stackHandler = new InfinityStackHandler(chestType.size);
        this.blockSupplier = supplier;
        this.openersCounter = new ContainerOpenersCounter() {
            protected void onOpen(Level level, BlockPos pos1, BlockState state1) {
                AbsEndlessChestTile.playSound(level, pos1, state1, SoundEvents.CHEST_OPEN);
            }

            protected void onClose(Level level, BlockPos pos1, BlockState state1) {
                AbsEndlessChestTile.playSound(level, pos1, state1, SoundEvents.CHEST_CLOSE);
            }

            protected void openerCountChanged(Level level, BlockPos pos1, BlockState state1, int i, int i1) {
                AbsEndlessChestTile.this.signalOpenCount(level, pos1, state1, i, i1);
            }

            protected boolean isOwnContainer(Player player) {
                if (!(player.containerMenu instanceof AbstractContainerMenu)) {
                    return false;
                } else {
                    Container container = ((ChestMenu)player.containerMenu).getContainer();
                    return container == AbsEndlessChestTile.this || container instanceof CompoundContainer && ((CompoundContainer)container).contains(AbsEndlessChestTile.this);
                }
            }
        };
        this.chestLidController = new ChestLidController();
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.unpackLootTable(null);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }

        this.stackHandler.getStacks().set(index, stack);
        this.setChanged();
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return super.removeItem(index, count);
    }

    @Override
    public int getMaxStackSize() {
        return this.type.stackLimit;
    }

    public Block getBlock() {
        return blockSupplier.get();
    }

    @Override
    public int getContainerSize() {
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
    public void load(CompoundTag nbt) {
        super.load(nbt);
        NbtRead(nbt);
    }

    public void NbtRead(CompoundTag nbt) {
        this.stackHandler.setStacks(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
        if (!this.tryLoadLootTable(nbt)) {
            loadAllItems(nbt, this.stackHandler.getStacks());
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        NbtWrite(compound);
        super.saveAdditional(compound);
    }

    public CompoundTag NbtWrite(CompoundTag compound) {
        if (!this.trySaveLootTable(compound)) {
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
    public static CompoundTag saveAllItems(CompoundTag nbt, NonNullList<ItemStack> stacks) {
        ListTag listNBT = new ListTag();

        for(int i = 0; i < stacks.size(); ++i) {
            ItemStack stack = stacks.get(i);
            if (!stack.isEmpty()) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte)i);
                stack.deserializeNBT(tag);
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
    public static void loadAllItems(CompoundTag nbt, NonNullList<ItemStack> stacks) {
        ListTag listNBT = nbt.getList("Items", 10);

        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundTag tag = listNBT.getCompound(i);
            int slot = tag.getByte("Slot") & 255;
            if (slot < stacks.size()) {
                ItemStack stack = ItemStack.of(tag);
                int count = tag.getInt(NBT_COUNT);
                stack.setCount(count);
                stacks.set(slot, stack);
            }
        }
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("gui.endless." + type.getName() + "_chest");
    }

//    @Nullable
//    @Override
//    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player pPlayer) {
//        return ChestMenu.threeRows(id, inventory, this);
//    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }
/*
    public static void serverTick(Level level, BlockPos pos, BlockState state, AbsEndlessChestTile tile) {
        int i = tile.pos.getX();
        int j = tile.pos.getY();
        int k = tile.pos.getZ();
        ++tile.ticksSinceSync;
        tile.numPlayersUsing = calculatePlayersUsingSync(tile.world, tile, tile.ticksSinceSync, i, j, k, tile.numPlayersUsing);
        tile.prevLidAngle = tile.lidAngle;
        float f = 0.1F;
        if (tile.numPlayersUsing > 0 && tile.lidAngle == 0.0F) {
            tile.playSound(SoundEvents.BLOCK_CHEST_OPEN);
        }

        if (tile.numPlayersUsing == 0 && tile.lidAngle > 0.0F || tile.numPlayersUsing > 0 && tile.lidAngle < 1.0F) {
            float f1 = tile.lidAngle;
            if (tile.numPlayersUsing > 0) {
                tile.lidAngle += f;
            } else tile.lidAngle -= f;

            if (tile.lidAngle > 1.0F) {
                tile.lidAngle = 1.0F;
            }

            float f2 = 0.5F;
            if (tile.lidAngle < f2 && f1 >= f2) {
                tile.playSound(SoundEvents.CHEST_CLOSE);
            }

            if (tile.lidAngle < 0.0F) {
                tile.lidAngle = 0.0F;
            }
        }

    }
*/
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        NbtWrite(tag);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (level != null) {
            handleUpdateTag(pkt.getTag());
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        NbtRead(tag);
    }

    public static int calculatePlayersUsingSync(Level world, BaseEntityBlock lockableTile, int ticksSinceSync, int x, int y, int z, int numPlayersUsing) {
        if (world != null && !world.isClientSide && numPlayersUsing != 0 && (ticksSinceSync + x + y + z) % 200 == 0) {
            numPlayersUsing = calculatePlayersUsing(world, lockableTile, x, y, z);
        }

        return numPlayersUsing;
    }

    public static int calculatePlayersUsing(Level world, BaseEntityBlock lockableTile, int x, int y, int z) {
        int i = 0;
        float f = 5.0f;

        for (Player playerentity : world.getEntitiesOfClass(Player.class,
                new AABB((float) x - f, (float) y - f, (float) z - f, (float) (x + 1) + f, (float) (y + 1) + f, (float) (z + 1) + f))) {
            AbstractContainerMenu openContainer = playerentity.containerMenu;
            if (openContainer instanceof CompressorChestContainer || openContainer instanceof InfinityBoxContainer) {
                ++i;
            }
        }

        return i;
    }

    private static void playSound(Level level, BlockPos pos, BlockState pState, SoundEvent soundIn) {
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.5D;
        double d2 = (double) pos.getZ() + 0.5D;
        if (level != null)
            level.playSound(null, d0, d1, d2, soundIn, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
//            this.numPlayersUsing = type;
            this.chestLidController.shouldBeOpen(type > 0);
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    @Override
    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    @Override
    public float getOpenNess(float v) {
        return this.chestLidController.getOpenness(v);
    }

    protected void signalOpenCount(Level pLevel, BlockPos pPos, BlockState pState, int pEventId, int pEventParam) {
        Block block = pState.getBlock();
        pLevel.blockEvent(pPos, block, 1, pEventParam);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.stackHandler.getStacks();
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.stackHandler.setStacks(itemsIn);
    }

    @Override
    public void setBlockState(BlockState state) {
        super.setBlockState(state);
        if (this.chestHandler != null) {
            LazyOptional<?> oldHandler = this.chestHandler;
            this.chestHandler = null;
            oldHandler.invalidate();
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (this.chestHandler == null)
                this.chestHandler = LazyOptional.of(this::createHandler);
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
        Container inv = AbsEndlessChest.getContainer((AbsEndlessChest) state.getBlock(), state, level, worldPosition, true);
        return new InvWrapper(inv == null ? this : inv);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (chestHandler != null){
            chestHandler.invalidate();
            chestHandler = null;
        }
    }
}
