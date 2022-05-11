package com.yuo.endless.Items.Tool;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class InfinityDamageSource extends EntityDamageSource {

    public InfinityDamageSource(LivingEntity living) {
        super("infinity", living);
        setDamageBypassesArmor(); //不受盔甲护甲影响
        setDamageAllowedInCreativeMode(); //对创造模式造成伤害
        setDamageIsAbsolute(); //不受附魔，药水效果影响
    }

    @Override
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
        ItemStack itemstack = damageSourceEntity instanceof LivingEntity ? ((LivingEntity)damageSourceEntity).getHeldItem(Hand.MAIN_HAND) : ItemStack.EMPTY;
        String s = "death.attack.infinity";
        int rando = entityLivingBaseIn.getEntityWorld().rand.nextInt(4) + 1;
        return !itemstack.isEmpty() && itemstack.hasDisplayName() ? new TranslationTextComponent(s + ".item", entityLivingBaseIn.getDisplayName(), damageSourceEntity.getDisplayName(),itemstack.getDisplayName())
                : new TranslationTextComponent(s + "." + rando, entityLivingBaseIn.getDisplayName());
    }

    //是否根据难度缩放伤害值
    @Override
    public boolean isDifficultyScaled() {
        return false;
    }

}
