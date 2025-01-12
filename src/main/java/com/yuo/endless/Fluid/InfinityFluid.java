package com.yuo.endless.Fluid;

import com.yuo.endless.Endless;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class InfinityFluid extends ForgeFlowingFluid {
    public static final TagKey<Fluid> INFINITY = FluidTags.create(new ResourceLocation(Endless.MOD_ID, "infinity"));

    protected InfinityFluid(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluidIn, Direction direction) {
        return direction == Direction.DOWN && fluidIn.is(INFINITY);
    }

    //能否流动到此方块
    @Override
    protected boolean canSpreadTo(BlockGetter getter, BlockPos frompos, BlockState fromblockstate, Direction direction, BlockPos topos, BlockState toblockstate, FluidState tofluidstate, Fluid fluid) {
        float hardness = toblockstate.getBlock().defaultDestroyTime();
        if (hardness > 0 && hardness < 50) return true;
        return super.canSpreadTo(getter, frompos, fromblockstate, direction, topos, toblockstate, tofluidstate, fluid);
    }

    //替换方块
    @Override
    protected void beforeDestroyingBlock(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        float hardness = state.getBlock().defaultDestroyTime();
        if (hardness > 0 && hardness < 50){
            worldIn.destroyBlock(pos, false);
            if (state.isAir())
                worldIn.removeBlock(pos, true);
        }
    }

    @Override
    protected FluidAttributes createAttributes() {
        return setAttr(EndlessFluids.STILL_OIL_TEXTURE, EndlessFluids.FLOWING_OIL_TEXTURE, 0xff333333).build(getSource());
    }

    public static FluidAttributes.Builder setAttr(ResourceLocation still, ResourceLocation flowing, int color){
        //材质，颜色，亮度，流体密度,温度,稠度,稀有度
        return FluidAttributes.builder(still, flowing)
                .color(color)
                .luminosity(0)
                .density(100000)
                .temperature(Integer.MAX_VALUE)
                .viscosity(100000)
                .rarity(Rarity.EPIC);
    }

    public static class Flowing extends InfinityFluid {
        public Flowing(Properties properties) {
            super(properties);
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        @Override
        protected void createFluidStateDefinition(Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends InfinityFluid {
        public Source(Properties properties) {
            super(properties);
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
