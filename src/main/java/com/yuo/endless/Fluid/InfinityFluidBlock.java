package com.yuo.endless.Fluid;

import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Tool.InfinityDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class InfinityFluidBlock extends LiquidBlock {
    public static final Properties FLUID_PROP = Properties.of(Material.LAVA).noCollission().randomTicks().strength(Float.MAX_VALUE).noDrops();
    public InfinityFluidBlock(Supplier<? extends FlowingFluid> fluid) {
        super(fluid, FLUID_PROP);
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        if (collidingEntity instanceof LivingEntity living){
            //给予debuff
            int fluid_level = 0;
            FluidState fluidState = level.getFluidState(pos);
            if (fluidState.isSource()) fluid_level = 10;
            else fluid_level = level.getFluidState(pos).getValue(FlowingFluid.LEVEL);
            if (living instanceof Player player){
                Boolean hasChest = player.getItemBySlot(EquipmentSlot.CHEST).getItem() == EndlessItems.infinityChest.get();
                Boolean hasLeg = player.getItemBySlot(EquipmentSlot.LEGS).getItem() == EndlessItems.infinityLegs.get();
                Boolean hasHead = player.getItemBySlot(EquipmentSlot.HEAD).getItem() == EndlessItems.infinityHead.get();
                Boolean hasFeet = player.getItemBySlot(EquipmentSlot.FEET).getItem() == EndlessItems.infinityFeet.get();
                if (hasChest || hasFeet || hasHead || hasLeg){ //有无尽装备时，debuff减半
                    living.hurt(new InfinityDamageSource(null), 1.0f);
                    fluid_level = Math.max(0, (int) Math.ceil(fluid_level / 2d));
                }else if (EventHandler.isInfinite(player)){ //全套无尽buff无影响
                    return false;
                }
            } else living.hurt(new InfinityDamageSource(null), 2.0f);
            living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 0, fluid_level - 1));
            living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 0, fluid_level - 1));
        }else {
            collidingEntity.remove(RemovalReason.KILLED);
            if (level instanceof ServerLevel serverLevel){
                serverLevel.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
        }

        return true;
    }
}
