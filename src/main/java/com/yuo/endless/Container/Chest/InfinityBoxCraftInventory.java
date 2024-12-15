package com.yuo.endless.Container.Chest;

import com.yuo.endless.Tiles.InfinityBoxTile;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

public class InfinityBoxCraftInventory extends CraftingContainer {
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
    public ItemStack getItem(int index) {
        return index < 243 || index > 251 ? ItemStack.EMPTY : craftTile.getItem(index);
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
    public void setItem(int slot, ItemStack itemstack) {
        craftTile.setItem(slot, itemstack);
        container.slotsChanged(this);
    }
}
