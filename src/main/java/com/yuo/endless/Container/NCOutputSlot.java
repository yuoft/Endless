package com.yuo.endless.Container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class NCOutputSlot extends Slot {
    public NCOutputSlot(Container container, int index, int xPosition, int yPosition) {
        super(container, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return false;
    }

    @Override
    public int getMaxStackSize(ItemStack pStack) {
        return 64;
    }
}
