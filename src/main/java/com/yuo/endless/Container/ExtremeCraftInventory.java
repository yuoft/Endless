package com.yuo.endless.Container;

import com.yuo.endless.Tiles.ExtremeCraftTile;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ExtremeCraftInventory extends CraftingInventory {
    private final NonNullList<ItemStack> stackList;
    private ExtremeCraftTile craftTile;
    private Container container;

    public ExtremeCraftInventory(Container containerIn, ExtremeCraftTile tile) {
        super(containerIn, 9, 9);
        this.stackList = NonNullList.withSize(81, ItemStack.EMPTY);
        this.craftTile = tile;
        this.container = containerIn;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= this.getSizeInventory() ? ItemStack.EMPTY : craftTile.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = craftTile.getStackInSlot(index);
        if (!stack.isEmpty()) {
            ItemStack itemstack;
            if (stack.getCount() <= count) {
                itemstack = stack.copy();
                craftTile.setInventorySlotContents(index, ItemStack.EMPTY);
                container.onCraftMatrixChanged(this);
                return itemstack;
            } else {
                itemstack = stack.split(count); //消耗
                if (stack.getCount() == 0) {
                    craftTile.setInventorySlotContents(index, ItemStack.EMPTY);
                }
                container.onCraftMatrixChanged(this);
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        craftTile.setInventorySlotContents(slot, itemstack);
        container.onCraftMatrixChanged(this);
    }
}
