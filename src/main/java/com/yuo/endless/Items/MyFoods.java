package com.yuo.endless.Items;

import com.yuo.endless.Config;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class MyFoods {
    //食物属性构建  补充饥饿值，饱腹度  药水效果 获取药水效果概率（1=100%） 总是可以食用 肉
    public static double ratio = Config.SERVER.foodTime.get();
    //寰宇肉丸
    public static final FoodProperties MEAT_BALLS = new FoodProperties.Builder().nutrition(50).saturationMod(30).effect(
            () -> new MobEffectInstance(MobEffects.HARM, (int) Math.ceil(5 * 60 * 20 * ratio), 4), 1).effect(
            () -> new MobEffectInstance(MobEffects.DIG_SPEED, (int) Math.ceil(3 * 60 * 20 * ratio), 2), 1).effect(
            () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, (int) Math.ceil(3 * 60 * 20 * ratio), 2), 1).effect(
            () -> new MobEffectInstance(MobEffects.JUMP, (int) Math.ceil(3 * 60 * 20 * ratio), 2), 1).alwaysEat().meat().build();
    //超级煲
    public static final FoodProperties STEW = new FoodProperties.Builder().nutrition(50).saturationMod(30).effect(
            () -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, (int) Math.ceil(5 * 60 * 20 * ratio), 0), 1).effect(
            () -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, (int) Math.ceil(1 * 60 * 20 * ratio), 1), 1).effect(
            () -> new MobEffectInstance(MobEffects.ABSORPTION, (int) Math.ceil(3 * 60 * 20 * ratio), 2), 1).effect(
            () -> new MobEffectInstance(MobEffects.NIGHT_VISION, (int) Math.ceil(3 * 60 * 20 * ratio), 0), 1).effect(
            () -> new MobEffectInstance(MobEffects.WATER_BREATHING, (int) Math.ceil(2 * 60 * 20 * ratio), 2), 1).effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION, (int) Math.ceil(5 * 60 * 20 * ratio), 4), 1).alwaysEat().build();
}
