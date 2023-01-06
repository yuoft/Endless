package com.yuo.endless.Tiles;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class InfinityStackHandler extends ItemStackHandler {
    public InfinityStackHandler() {
    }

    public InfinityStackHandler(int size) {
        super(size);
    }

    public InfinityStackHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return 128;
    }

    public NonNullList<ItemStack> getStacks(){
        return this.stacks;
    }

    public void setStacks(NonNullList<ItemStack> list){
        this.stacks = list;
    }
}
