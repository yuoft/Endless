package com.yuo.endless.Blocks.Fluid;

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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class InfinityFluidBlock extends LiquidBlock {
    public static final Properties FLUID_PROP = Properties.of().mapColor(MapColor.FIRE).noCollission().randomTicks().strength(Float.MAX_VALUE).noLootTable();
    public InfinityFluidBlock(Supplier<? extends FlowingFluid> fluid) {
        super(fluid, FLUID_PROP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LiquidBlock.LEVEL);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide()) {
            if (entity instanceof LivingEntity living) {
                //给予debuff
                int fluid_level = 0;
                FluidState fluidState = level.getFluidState(pos);
                if (fluidState.isSource()) fluid_level = 10;
                else fluid_level = level.getFluidState(pos).getValue(FlowingFluid.LEVEL);
                if (living instanceof Player player) {
                    Boolean hasChest = player.getItemBySlot(EquipmentSlot.CHEST).getItem() == EndlessItems.infinityChest.get();
                    Boolean hasLeg = player.getItemBySlot(EquipmentSlot.LEGS).getItem() == EndlessItems.infinityLegs.get();
                    Boolean hasHead = player.getItemBySlot(EquipmentSlot.HEAD).getItem() == EndlessItems.infinityHead.get();
                    Boolean hasFeet = player.getItemBySlot(EquipmentSlot.FEET).getItem() == EndlessItems.infinityFeet.get();
                    if (hasChest || hasFeet || hasHead || hasLeg) { //有无尽装备时，debuff减半
                        living.hurt(new InfinityDamageSource(player), 1.0f);
                        fluid_level = Math.max(0, (int) Math.ceil(fluid_level / 2d));
                    } else if (EventHandler.isInfinite(player)) { //全套无尽buff无影响
                        return;
                    }else living.hurt(new InfinityDamageSource(living), 2.0f);
                } else living.hurt(new InfinityDamageSource(living), 2.0f);
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 0, fluid_level - 1));
                living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 0, fluid_level - 1));
            } else {
                entity.remove(RemovalReason.KILLED);
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
            }
        }
    }
}
