package com.yuo.endless.Container;

import com.yuo.endless.Tiles.ExtremeCraftTile;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;

public class ExtremeCraftInventoryResult extends ResultContainer {

    private final ExtremeCraftTile craftTile;

    public ExtremeCraftInventoryResult(ExtremeCraftTile tile){
        this.craftTile = tile;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return craftTile.getItem(81);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        ItemStack stack = craftTile.getItem(81);
        if (!stack.isEmpty()) {
            craftTile.setItem(81, ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        craftTile.setItem(81, pStack);
    }

}
