package com.yuo.endless.Container.Craft;

import com.yuo.endless.Tiles.AbsCraftTile;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

public class ExtremeCraftInventory extends CraftingInventory {
    private final AbsCraftTile craftTile;
    private final Container container;

    public ExtremeCraftInventory(Container containerIn, AbsCraftTile tile) {
        super(containerIn, tile.getCraftType().getCraftNum(), tile.getCraftType().getCraftNum());
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
