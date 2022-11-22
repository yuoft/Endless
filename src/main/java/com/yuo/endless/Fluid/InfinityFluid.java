package com.yuo.endless.Fluid;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.List;
import java.util.Random;

public class InfinityFluid extends ForgeFlowingFluid {
    public static final ITag.INamedTag<Fluid> INFINITY = FluidTags.makeWrapperTag("infinity");

    protected InfinityFluid(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    @Override
    public int getLevel(FluidState state) {
        return 0;
    }

    @Override
    protected boolean canDisplace(FluidState state, IBlockReader world, BlockPos pos, Fluid fluidIn, Direction direction) {
        return direction == Direction.DOWN && fluidIn.isIn(INFINITY);
    }

    //能否流动到此方块
    @Override
    protected boolean canFlow(IBlockReader worldIn, BlockPos fromPos, BlockState fromBlockState, Direction direction, BlockPos toPos, BlockState toBlockState, FluidState toFluidState, Fluid fluidIn) {
        float hardness = toBlockState.getBlockHardness(worldIn, toPos);
        if (hardness > 0 && hardness < 50) return true;
        return super.canFlow(worldIn, fromPos, fromBlockState, direction, toPos, toBlockState, toFluidState, fluidIn);
    }

    //流动到此，设置流体
    @Override
    protected void flowInto(IWorld worldIn, BlockPos pos, BlockState blockStateIn, Direction direction, FluidState fluidStateIn) {
        beforeReplacingBlock(worldIn, pos, blockStateIn);
        worldIn.setBlockState(pos, fluidStateIn.getBlockState(), 3);
    }

    //替换方块
    @Override
    protected void beforeReplacingBlock(IWorld worldIn, BlockPos pos, BlockState state) {
        float hardness = state.getBlockHardness(worldIn, pos);
        if (hardness > 0 && hardness < 50){
            worldIn.destroyBlock(pos, false);
            if (worldIn.isAirBlock(pos))
                worldIn.removeBlock(pos, true);
        }
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
            setDefaultState(getStateContainer().getBaseState().with(LEVEL_1_8, 7));
        }

        protected void fillStateContainer(StateContainer.Builder<Fluid, FluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        public int getLevel(FluidState state) {
            return state.get(LEVEL_1_8);
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
