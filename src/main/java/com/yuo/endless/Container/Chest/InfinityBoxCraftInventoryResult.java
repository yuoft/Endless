package com.yuo.endless.Container.Chest;

import com.yuo.endless.Tiles.InfinityBoxTile;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;

public class InfinityBoxCraftInventoryResult extends ResultContainer {

    private final InfinityBoxTile craftTile;

    public InfinityBoxCraftInventoryResult(InfinityBoxTile tile){
        this.craftTile = tile;
    }

    @Override
    public ItemStack getItem(int index) {
        return craftTile.getItem(252);
    }

    @Override
    public ItemStack removeItem(int par1, int par2) {
        ItemStack stack = craftTile.getItem(252);
        if (!stack.isEmpty()) {
            craftTile.setItem(252, ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int pIndex, ItemStack stack) {
        craftTile.setItem(252, stack);
    }
}
