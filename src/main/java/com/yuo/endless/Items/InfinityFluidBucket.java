package com.yuo.endless.Items;

import com.yuo.endless.tab.ModGroup;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class InfinityFluidBucket extends BucketItem {
    public InfinityFluidBucket(Supplier<? extends Fluid> supplier) {
        super(supplier, new Item.Properties().group(ModGroup.endless).containerItem(Items.BUCKET).maxStackSize(1).isImmuneToFire());
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new FluidBucketWrapper(stack);
    }
}
