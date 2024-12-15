package com.yuo.endless.Container.Chest;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class InfinityBoxBurnFuelSlot extends Slot {
    private final InfinityBoxContainer container;

    public InfinityBoxBurnFuelSlot(InfinityBoxContainer container, Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.container = container;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.container.isFuel(stack) || isBucket(stack);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
    }

    public static boolean isBucket(ItemStack stack) {
        return stack.getItem() == Items.BUCKET;
    }
}
