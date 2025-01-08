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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;

public class InfinityFluid extends Flowing {
    public static final TagKey<Fluid> INFINITY = FluidTags.create(new ResourceLocation(Endless.MOD_ID, "infinity"));

    protected InfinityFluid(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isSource(FluidState state) {
        return false;
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

    //流动到此，设置流体
    @Override
    protected void spreadTo(LevelAccessor worldIn, BlockPos pos, BlockState blockStateIn, Direction direction, FluidState fluidStateIn) {
        beforeDestroyingBlock(worldIn, pos, blockStateIn);
        worldIn.setBlock(pos, fluidStateIn.createLegacyBlock(), 3);
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
    public int getAmount(FluidState fluidState) {
        return 0;
    }

    @Override
    protected FluidAttributes createAttributes() {
        return setAttr(new ResourceLocation(""), new ResourceLocation(""), 0).build(getSource());
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

        protected void fillStateContainer(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getLevel(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends InfinityFluid {
        public Source(Properties properties) {
            super(properties);
        }

        public int getLevel(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
