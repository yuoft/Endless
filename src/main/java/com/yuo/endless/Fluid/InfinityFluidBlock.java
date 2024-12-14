package com.yuo.endless.Fluid;

import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Tool.InfinityDamageSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class InfinityFluidBlock extends FlowingFluidBlock {
    public InfinityFluidBlock(Supplier<? extends FlowingFluid> supplier) {
        super(supplier, Block.Properties.create(Material.LAVA).doesNotBlockMovement().tickRandomly().hardnessAndResistance(Float.MAX_VALUE).noDrops());
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof LivingEntity){
            LivingEntity living = (LivingEntity) entityIn;
            //给予debuff
            int level = 0;
            FluidState fluidState = worldIn.getFluidState(pos);
            if (fluidState.isSource()) level = 10;
            else level = worldIn.getFluidState(pos).get(FlowingFluid.LEVEL_1_8);
            if (living instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) living;
                Boolean hasChest = player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == EndlessItems.infinityChest.get();
                Boolean hasLeg = player.getItemStackFromSlot(EquipmentSlotType.LEGS).getItem() == EndlessItems.infinityLegs.get();
                Boolean hasHead = player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == EndlessItems.infinityHead.get();
                Boolean hasFeet = player.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() == EndlessItems.infinityFeet.get();
                if (hasChest || hasFeet || hasHead || hasLeg){ //有无尽装备时，debuff减半
                    living.attackEntityFrom(new InfinityDamageSource(null), 1.0f);
                    level = Math.max(0, (int) Math.ceil(level / 2d));
                }else if (EventHandler.isInfinite(player)){ //全套无尽buff无影响
                    return;
                }
            } else living.attackEntityFrom(new InfinityDamageSource(null), 2.0f);
            living.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 0, level - 1));
            living.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 0, level - 1));
        }else {
            entityIn.remove();
            worldIn.playSound(null, pos, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
        }
    }
}
