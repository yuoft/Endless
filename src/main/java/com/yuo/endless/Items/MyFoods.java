package com.yuo.endless.Items;

import com.yuo.endless.Config.Config;
import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class MyFoods {
    //食物属性构建  补充饥饿值，饱腹度  药水效果 获取药水效果概率（1=100%） 总是可以食用 肉
    public static double ratio = Config.SERVER.foodTime.get();
    //寰宇肉丸
    public static final Food MEAT_BALLS = (new Food.Builder()).hunger(50).saturation(30).effect(
            () -> new EffectInstance(Effects.STRENGTH, (int) Math.ceil(5 * 60 * 20 * ratio), 4), 1).effect(
            () -> new EffectInstance(Effects.HASTE, (int) Math.ceil(3 * 60 * 20 * ratio), 2), 1).effect(
            () -> new EffectInstance(Effects.SPEED, (int) Math.ceil(3 * 60 * 20 * ratio), 2), 1).effect(
            () -> new EffectInstance(Effects.JUMP_BOOST, (int) Math.ceil(3 * 60 * 20 * ratio), 2), 1).setAlwaysEdible().meat().build();
    //超级煲
    public static final Food STEW = (new Food.Builder()).hunger(50).saturation(30).effect(
            () -> new EffectInstance(Effects.FIRE_RESISTANCE, (int) Math.ceil(5 * 60 * 20 * ratio), 0), 1).effect(
            () -> new EffectInstance(Effects.RESISTANCE, (int) Math.ceil(1 * 60 * 20 * ratio), 1), 1).effect(
            () -> new EffectInstance(Effects.ABSORPTION, (int) Math.ceil(3 * 60 * 20 * ratio), 2), 1).effect(
            () -> new EffectInstance(Effects.NIGHT_VISION, (int) Math.ceil(3 * 60 * 20 * ratio), 0), 1).effect(
            () -> new EffectInstance(Effects.WATER_BREATHING, (int) Math.ceil(2 * 60 * 20 * ratio), 2), 1).effect(
            () -> new EffectInstance(Effects.REGENERATION, (int) Math.ceil(5 * 60 * 20 * ratio), 4), 1).setAlwaysEdible().build();
}
