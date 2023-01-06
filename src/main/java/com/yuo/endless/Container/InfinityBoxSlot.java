package com.yuo.endless.Container;

import com.yuo.endless.Blocks.AbsEndlessChest;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.Locale;

public class InfinityBoxSlot extends Slot {
    public InfinityBoxSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (Block.getBlockFromItem(stack.getItem()) instanceof AbsEndlessChest) return false;
        else if (!stack.isEmpty()) {
            String name = stack.getTranslationKey().toLowerCase(Locale.ENGLISH);
            return !name.contains("pouch") && !name.contains("bag") && !name.contains("strongbox") && !name.contains("shulker_box");
        }
        return true;
    }
}
