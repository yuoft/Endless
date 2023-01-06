package com.yuo.endless.Container;

import com.yuo.endless.Tiles.InfinityBoxTile;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.item.ItemStack;

public class InfinityBoxCraftInventoryResult extends CraftResultInventory {

    private final InfinityBoxTile craftTile;

    public InfinityBoxCraftInventoryResult(InfinityBoxTile tile){
        this.craftTile = tile;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return craftTile.getStackInSlot(252);
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        ItemStack stack = craftTile.getStackInSlot(252);
        if (!stack.isEmpty()) {
            craftTile.setInventorySlotContents(252, ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        craftTile.setInventorySlotContents(252, par2ItemStack);
    }
}
