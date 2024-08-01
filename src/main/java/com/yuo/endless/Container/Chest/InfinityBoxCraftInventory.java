package com.yuo.endless.Container.Chest;

import com.yuo.endless.Tiles.InfinityBoxTile;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;

public class InfinityBoxCraftInventory extends CraftingInventory {
    private final InfinityBoxTile craftTile;
    private final InfinityBoxContainer container;

    public InfinityBoxCraftInventory(InfinityBoxContainer containerIn, InfinityBoxTile tile) {
        super(containerIn, 3, 3);
        this.craftTile = tile;
        this.container = containerIn;
    }

    public InfinityBoxContainer getContainer() {
        return container;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index < 243 || index > 251 ? ItemStack.EMPTY : craftTile.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = ItemStackHelper.getAndSplit(craftTile.getItems(), index, count);
        if (!stack.isEmpty()) {
            container.onCraftMatrixChanged(this);
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        craftTile.setInventorySlotContents(slot, itemstack);
        container.onCraftMatrixChanged(this);
    }
}
