package com.yuo.endless.Items.Tool;

import com.yuo.endless.tab.ModGroup;
import net.minecraft.block.BlockState;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class InfinityBucket extends Item {
    private final java.util.function.Supplier<? extends Fluid> fluidSupplier; //桶内流体
    private final Fluid fluid;

    public InfinityBucket(Supplier<? extends Fluid> supplier) {
        super(new Properties().group(ModGroup.endless).maxStackSize(1).isImmuneToFire());
        this.fluidSupplier = supplier;
        this.fluid = Fluids.EMPTY;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public Fluid getFluid() {
        return fluidSupplier.get();
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundNBT nbt) {
        if (this.getClass() == InfinityBucket.class)
            return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
        else
            return super.initCapabilities(stack, nbt);
    }

    private boolean canBlockContainFluid(World worldIn, BlockPos posIn, BlockState blockstate) {
        return blockstate.getBlock() instanceof ILiquidContainer && ((ILiquidContainer) blockstate.getBlock()).canContainFluid(worldIn, posIn, blockstate, this.fluid);
    }
}
