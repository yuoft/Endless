package com.yuo.endless.Container;

import com.yuo.endless.Blocks.AbsEndlessChest;
import com.yuo.endless.Blocks.InfinityBox;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class InfinityBoxSlot extends Slot {
    public InfinityBoxSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return !(Block.getBlockFromItem(stack.getItem()) instanceof AbsEndlessChest);
    }
}
