package com.yuo.endless.Tiles;

import com.yuo.endless.Container.ExtremeCraftContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

//存储工作台数据
public class ExtremeCraftTile extends BaseContainerBlockEntity {

    private NonNullList<ItemStack> items = NonNullList.withSize(81, ItemStack.EMPTY); //存储物品
    private final NonNullList<ItemStack> reslut = NonNullList.withSize(1, ItemStack.EMPTY); //存储合成物品

    public ExtremeCraftTile(BlockPos pos, BlockState state) {
        super(EndlessTileTypes.EXTREME_CRAFT_TILE.get(), pos, state);
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int getContainerSize() {
        return 82;
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return this.reslut.get(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        if (i == 81) return reslut.get(0);
        else return this.items.get(i);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (index == 81) return ContainerHelper.removeItem(reslut, index, count);
        else return ContainerHelper.removeItem(this.items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (index == 81) return ContainerHelper.takeItem(reslut, index);
        return ContainerHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index == 81) reslut.set(0, stack);
        else {
            ItemStack itemstack = this.items.get(index);
            boolean flag = !stack.isEmpty() && stack.equals(itemstack, false) && ItemStack.isSame(stack, itemstack); //相同物品
            this.items.set(index, stack);
            if (stack.getCount() > this.getMaxStackSize()) {
                stack.setCount(this.getMaxStackSize());
            }

            if (!flag) {
                this.setChanged();
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level != null && this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        NbtRead(nbt);
    }

    private void NbtRead(CompoundTag nbt){
        this.items = NonNullList.withSize(this.getContainerSize() - 1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.items);
        CompoundTag resultNbt = (CompoundTag) nbt.get("Result");
        if (resultNbt != null) {
            this.reslut.set(0, ItemStack.of(resultNbt));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        NbtWrite(compound);
        super.saveAdditional(compound);
    }

    private void NbtWrite(CompoundTag compound){
        ContainerHelper.saveAllItems(compound, this.items);
        CompoundTag nbt = new CompoundTag();
        this.reslut.get(0).setTag(nbt);
        compound.put("Result", nbt);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
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


    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compound = super.getUpdateTag();
        NbtWrite(compound);
        return compound;
    }

    @Override
    public Component getName() {
        return new TranslatableComponent("gui.endless.extreme_crafting_table");
    }

    @Override
    protected Component getDefaultName() {
        return null;
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new ExtremeCraftContainer(i, inventory, this);
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.reslut.clear();
    }
}