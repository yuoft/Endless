package com.yuo.endless.Compat.Maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.*;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.yuo.endless.Config.ModConfig;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.Armor.InfinityArmor;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Tool.InfinityDamageTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class MaidEvents {
    public static List<String> maidWithChest = new ArrayList<>();
    public static List<String> maidWithLegs = new ArrayList<>();

    // 处理女仆与无尽食物交互的事件
    @SubscribeEvent
    public void onItemInteract(InteractMaidEvent event) {
        Player player = event.getPlayer();
        Level world = event.getWorld();
        EntityMaid maid = event.getMaid();
        // 获取主手物品
        ItemStack stack = event.getStack();
        // 不需要判断女仆归属，模组已自动处理
        if (stack.is(EndlessItems.cosmicMeatBalls.get())) {
            // 播放吃东西的音效
            world.playSound(null, maid.getX(), maid.getY(), maid.getZ(), maid.getEatingSound(stack), SoundSource.NEUTRAL, 1, 1 + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);
            // 添加食物效果
            maid.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, (int) Math.ceil(5 * 60 * 20 * ModConfig.SERVER.foodTime.get()), 4));
            maid.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, (int) Math.ceil(3 * 60 * 20 * ModConfig.SERVER.foodTime.get()), 2));
            maid.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, (int) Math.ceil(3 * 60 * 20 * ModConfig.SERVER.foodTime.get()), 2));
            maid.addEffect(new MobEffectInstance(MobEffects.JUMP, (int) Math.ceil(3 * 60 * 20 * ModConfig.SERVER.foodTime.get()), 2));
            // 消耗苹果
            stack.shrink(1);
            // 取消后续操作，避免打开女仆GUI
            event.setCanceled(true);
        }
        if (stack.is(EndlessItems.ultimateStew.get())) {
            world.playSound(null, maid.getX(), maid.getY(), maid.getZ(), maid.getEatingSound(stack), SoundSource.NEUTRAL, 1, 1 + (world.random.nextFloat() - world.random.nextFloat()) * 0.4F);

            maid.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, (int) Math.ceil(5 * 60 * 20 * ModConfig.SERVER.foodTime.get()), 1));
            maid.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, (int) Math.ceil(3 * 60 * 20 * ModConfig.SERVER.foodTime.get()), 2));
            maid.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, (int) Math.ceil(3 * 60 * 20 * ModConfig.SERVER.foodTime.get()), 0));
            maid.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, (int) Math.ceil(2 * 60 * 20 * ModConfig.SERVER.foodTime.get()), 2));
            maid.addEffect(new MobEffectInstance(MobEffects.REGENERATION, (int) Math.ceil(5 * 60 * 20 * ModConfig.SERVER.foodTime.get()), 4));

            stack.shrink(1);
            event.setCanceled(true);
        }
    }

    //女仆全套无尽时 不被攻击
    @SubscribeEvent
    public void onAttack(MaidAttackEvent event){
        EntityMaid maid = event.getMaid();
        if (EventHandler.isInfinite(maid) && !InfinityDamageTypes.isInfinity(event.getSource())) {
            event.setCanceled(true);
        }
        String key = maid.getStringUUID() + ":" + maid.level().isClientSide;
        if ((event.getSource().is(DamageTypes.IN_FIRE) || event.getSource().is(DamageTypes.ON_FIRE)) && maidWithLegs.contains(key)){
            event.setCanceled(true);
        }
        if (event.getSource().is(DamageTypes.MAGIC) && maidWithChest.contains(key)){
            event.setCanceled(true);
        }

        boolean isFeet = maid.getItemBySlot(EquipmentSlot.FEET).getItem() == EndlessItems.infinityFeet.get();
        if (isFeet && event.getSource().is(DamageTypes.FALL)){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onHurt(MaidHurtEvent event){
        EntityMaid maid = event.getMaid();
        if (EventHandler.isInfinite(maid) && !InfinityDamageTypes.isInfinity(event.getSource())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onDamage(MaidDamageEvent event){
        EntityMaid maid = event.getMaid();
        if (EventHandler.isInfinite(maid) && !InfinityDamageTypes.isInfinity(event.getSource())) {
            event.setAmount(0.0f);
            event.setCanceled(true);
        }
    }

    //女仆使用无尽装备效果
    @SubscribeEvent
    public void onTick(MaidTickEvent event){
        EntityMaid maid = event.getMaid();
        boolean isHead = maid.getItemBySlot(EquipmentSlot.HEAD).getItem() == EndlessItems.infinityHead.get();
        boolean isChest = maid.getItemBySlot(EquipmentSlot.CHEST).getItem() == EndlessItems.infinityChest.get();
        boolean isLegs = maid.getItemBySlot(EquipmentSlot.LEGS).getItem() == EndlessItems.infinityLegs.get();

        if (isHead){
            if (maid.isEyeInFluid(FluidTags.WATER)) { //玩家视线在水中
                maid.setAirSupply(300);
            }
            if (maid.tickCount % 100 == 0)
                maid.eat(maid.level(), new ItemStack(Items.COOKED_BEEF));
        }

        if (isChest){
            InfinityArmor.clearBadEffects(maid);
        }
        if (isLegs){
            if (maid.isOnFire()) maid.clearFire();
        }
    }

}
