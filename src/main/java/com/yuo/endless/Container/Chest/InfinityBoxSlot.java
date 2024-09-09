package com.yuo.endless.Container.Chest;

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

    @Override
    public void putStack(ItemStack stack) {
        ItemStack itemStack = this.inventory.getStackInSlot(this.getSlotIndex());
        if (itemStack.getMaxStackSize() <= 1) super.putStack(stack);
        else {
            this.inventory.setInventorySlotContents(this.getSlotIndex(), stack);
            this.onSlotChanged();
        }
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        int size = stack.getMaxStackSize();
        return size <= 1 ? 1 : this.getSlotStackLimit();
    }

    @Override
    public void onSwapCraft(int p_190900_1_) {
        super.onSwapCraft(p_190900_1_);
    }
}
