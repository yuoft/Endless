package com.yuo.endless.Container;

import com.yuo.endless.Tiles.ExtremeCraftTile;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

public class ExtremeCraftInventory extends CraftingContainer {
    private final ExtremeCraftTile craftTile;
    private final AbstractContainerMenu container;

    public ExtremeCraftInventory(AbstractContainerMenu containerIn, ExtremeCraftTile tile) {
        super(containerIn, 9, 9);
        this.craftTile = tile;
        this.container = containerIn;
    }

    @Override
    public ItemStack getItem(int index) {
        return index >= this.getContainerSize() ? ItemStack.EMPTY : craftTile.getItem(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = ContainerHelper.removeItem(craftTile.getItems(), index, count);
        if (!stack.isEmpty()) {
            container.slotsChanged(this);
        }
        return stack;
    }


    @Override
    public void setItem(int slot, ItemStack stack) {
        craftTile.setItem(slot, stack);
        container.slotsChanged(this);
    }

}
