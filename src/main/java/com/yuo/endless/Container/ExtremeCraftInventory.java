package com.yuo.endless.Container;

import com.yuo.endless.Tiles.ExtremeCraftTile;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ExtremeCraftInventory extends CraftingInventory {
    private final ExtremeCraftTile craftTile;
    private final Container container;

    public ExtremeCraftInventory(Container containerIn, ExtremeCraftTile tile) {
        super(containerIn, 9, 9);
        this.craftTile = tile;
        this.container = containerIn;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= this.getSizeInventory() ? ItemStack.EMPTY : craftTile.getStackInSlot(index);
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
