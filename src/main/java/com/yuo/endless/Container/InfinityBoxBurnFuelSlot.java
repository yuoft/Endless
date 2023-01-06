package com.yuo.endless.Container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class InfinityBoxBurnFuelSlot extends Slot {
    private final InfinityBoxContainer container;

    public InfinityBoxBurnFuelSlot(InfinityBoxContainer container, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.container = container;
    }

    public boolean isItemValid(ItemStack stack) {
        return this.container.isFuel(stack) || isBucket(stack);
    }

    public int getItemStackLimit(ItemStack stack) {
        return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
    }

    public static boolean isBucket(ItemStack stack) {
        return stack.getItem() == Items.BUCKET;
    }
}
