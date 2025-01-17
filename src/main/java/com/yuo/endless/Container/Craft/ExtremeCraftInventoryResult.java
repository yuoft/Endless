package com.yuo.endless.Container.Craft;

import com.yuo.endless.Tiles.AbsCraftTile;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.item.ItemStack;

public class ExtremeCraftInventoryResult extends CraftResultInventory {

    private final AbsCraftTile craftTile;

    public ExtremeCraftInventoryResult(AbsCraftTile tile){
        this.craftTile = tile;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return craftTile.getStackInSlot(craftTile.getCraftType().getCraftTotal());
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        ItemStack stack = craftTile.getStackInSlot(craftTile.getCraftType().getCraftTotal());
        if (!stack.isEmpty()) {
            craftTile.setInventorySlotContents(craftTile.getCraftType().getCraftTotal(), ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        craftTile.setInventorySlotContents(craftTile.getCraftType().getCraftTotal(), par2ItemStack);
    }
}
