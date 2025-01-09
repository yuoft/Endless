package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.AbsEndlessChest;
import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Container.Chest.InfinityChestContainer;
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
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public abstract class AbsEndlessChestTile extends RandomizableContainerBlockEntity implements LidBlockEntity {
    private final Supplier<Block> blockSupplier;
    private final EndlessChestType type;
    protected NonNullList<ItemStack> items;
    private final ContainerOpenersCounter openersCounter;
    protected final ChestLidController chestLidController;
    private LazyOptional<IItemHandlerModifiable> chestHandler;
    private static final String NBT_COUNT = "infinity_count";

    public AbsEndlessChestTile(BlockEntityType<?> typeIn, EndlessChestType chestType, Supplier<Block> supplier, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.type = chestType;
        this.items = NonNullList.withSize(chestType.getSize(), ItemStack.EMPTY);
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
                if (!(player.containerMenu instanceof InfinityChestContainer)) {
                    return false;
                } else {
                    Container container = ((InfinityChestContainer)player.containerMenu).getContainer();
                    return container == AbsEndlessChestTile.this || container instanceof CompoundContainer && ((CompoundContainer)container).contains(AbsEndlessChestTile.this);
                }
            }
        };
        this.chestLidController = new ChestLidController();
    }

    @Override
    public void clearContent() {
        super.clearContent();
        this.items.clear();
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.unpackLootTable(null);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }

        this.items.set(index, stack);
        this.setChanged();
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return this.items.get(pIndex);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ContainerHelper.takeItem(this.items, pIndex);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.items, index, count);
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
        for (ItemStack itemstack : this.items) {
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
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.items);
//            loadAllItems(nbt, this.items);
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        NbtWrite(tag);
        super.saveAdditional(tag);
    }

    public void NbtWrite(CompoundTag compound) {
        if (!this.trySaveLootTable(compound)) {
            ContainerHelper.saveAllItems(compound, this.items);
//            saveAllItems(compound, this.items);
        }
    }

    /**
     * 将所有物品保存到nbt中
     * @param nbt nbt
     * @param stacks 物品列表
     */
    public static void saveAllItems(CompoundTag nbt, NonNullList<ItemStack> stacks) {
        ListTag listNBT = new ListTag();

        for(int i = 0; i < stacks.size(); ++i) {
            ItemStack stack = stacks.get(i);
            if (!stack.isEmpty()) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte)i);
                tag.put("Item", stack.serializeNBT());
                tag.putInt(NBT_COUNT, stack.getCount());
                listNBT.add(tag);
            }
        }

        if (!listNBT.isEmpty()) {
            nbt.put("Items", listNBT);
        }
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
                ItemStack stack = ItemStack.of(tag.getCompound("Item"));
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

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

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
        super.handleUpdateTag(tag);
        NbtRead(tag);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level != null && this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    private static void playSound(Level level, BlockPos pos, BlockState pState, SoundEvent soundIn) {
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.5D;
        double d2 = (double) pos.getZ() + 0.5D;
        if (level != null)
            level.playSound(null, d0, d1, d2, soundIn, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public static void lidAnimateTick(Level pLevel, BlockPos pPos, BlockState pState, AbsEndlessChestTile pBlockEntity) {
        pBlockEntity.chestLidController.tickLid();
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
            triggerEvent(1, 1);
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    @Override
    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            triggerEvent(1, 0);
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
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.items = itemsIn;
    }

//    @Override
//    public void setRemoved() {
//        super.setRemoved();
//        for (LazyOptional<? extends IItemHandler> handler : chestHandler)
//            handler.invalidate();
//    }

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
            if (this.chestHandler == null) {
                this.chestHandler = LazyOptional.of(this::createHandler);
            }

            return this.chestHandler.cast();
        } else {
            return super.getCapability(cap, side);
        }
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

    public void invalidateCaps() {
        super.invalidateCaps();
        if (this.chestHandler != null) {
            this.chestHandler.invalidate();
            this.chestHandler = null;
        }

    }
}
