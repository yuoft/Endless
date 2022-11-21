package com.yuo.endless.Fluid;

import com.yuo.endless.Items.Tool.InfinityDamageSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class InfinityFluidBlock extends FlowingFluidBlock {
    public InfinityFluidBlock(Supplier<? extends FlowingFluid> supplier) {
        super(supplier, Block.Properties.create(Material.LAVA).doesNotBlockMovement().tickRandomly().hardnessAndResistance(Float.MAX_VALUE).noDrops());
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity){
            LivingEntity living = (LivingEntity) entityIn;
            living.attackEntityFrom(new InfinityDamageSource(null), 2.0f);
            living.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 0, 9));
        }else {
            entityIn.remove();
            worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }
}
