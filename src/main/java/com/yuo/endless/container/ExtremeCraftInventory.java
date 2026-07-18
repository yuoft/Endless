package com.yuo.endless.container;

import com.yuo.endless.tiles.ExtremeCraftTile;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;

public class ExtremeCraftInventory extends TransientCraftingContainer {
    private final ExtremeCraftTile craftTile;
    private final AbstractContainerMenu container;

    public ExtremeCraftInventory(AbstractContainerMenu containerIn, Container tile) {
        super(containerIn, 9, 9);
        this.craftTile = (ExtremeCraftTile) tile;
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
