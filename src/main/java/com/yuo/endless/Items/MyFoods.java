package com.yuo.endless.Items;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class MyFoods {
    //食物属性构建  补充饥饿值，饱腹度  药水效果 获取药水效果概率（1=100%） 总是可以食用 肉
    //寰宇肉丸
    public static final Food MEAT_BALLS = (new Food.Builder()).hunger(50).saturation(30).effect(
            () -> new EffectInstance(Effects.STRENGTH, 5 * 60 * 20, 4), 1).effect(
            () -> new EffectInstance(Effects.HASTE, 3 * 60 * 20, 2), 1).effect(
            () -> new EffectInstance(Effects.SPEED, 3 * 60 * 20, 2), 1).effect(
            () -> new EffectInstance(Effects.JUMP_BOOST, 3 * 60 * 20, 2), 1).setAlwaysEdible().meat().build();
    //超级煲
    public static final Food STEW = (new Food.Builder()).hunger(50).saturation(30).effect(
            () -> new EffectInstance(Effects.FIRE_RESISTANCE, 5 * 60 * 20, 0), 1).effect(
            () -> new EffectInstance(Effects.RESISTANCE, 60 * 20, 1), 1).effect(
            () -> new EffectInstance(Effects.ABSORPTION, 3 * 60 * 20, 2), 1).effect(
            () -> new EffectInstance(Effects.NIGHT_VISION, 3 * 60 * 20, 0), 1).effect(
            () -> new EffectInstance(Effects.WATER_BREATHING, 2 * 60 * 20, 2), 1).effect(
            () -> new EffectInstance(Effects.REGENERATION, 5 * 60 * 20, 4), 1).setAlwaysEdible().build();
}
