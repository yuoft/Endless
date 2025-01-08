package com.yuo.endless.Items.Tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class OrdinarySword extends SwordItem {

    public OrdinarySword(Tier tier) {
        super(tier, 0,-2.4f, new Properties().tab(EndlessTab.endless).fireResistant());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = getDefaultAttributeModifiers(slot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND){
            if (stack.getItem() == EndlessItems.crystalMatrixSword.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(4,0.03d));
            }else if (stack.getItem() == EndlessItems.neutroniumSword.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(4,0.04d));
            }
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

}
