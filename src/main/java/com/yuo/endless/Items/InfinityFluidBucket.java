package com.yuo.endless.Items;

import com.yuo.endless.EndlessTab;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class InfinityFluidBucket extends BucketItem {
    public InfinityFluidBucket(Supplier<? extends Fluid> supplier) {
        super(supplier, new Item.Properties().tab(EndlessTab.endless).craftRemainder(Items.BUCKET).stacksTo(1).fireResistant());
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidBucketWrapper(stack);
    }
}
