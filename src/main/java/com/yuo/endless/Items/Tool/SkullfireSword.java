package com.yuo.endless.Items.Tool;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class SkullfireSword extends SwordItem {
    public SkullfireSword() {
        super(EndlessItemTiers.SKULL_FIRE, 0, -2.4f, new Properties().fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> components, TooltipFlag pIsAdvanced) {
        components.add(Component.translatable("endless.text.itemInfo.skullfire_sword"));
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.setSecondsOnFire(2);
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
