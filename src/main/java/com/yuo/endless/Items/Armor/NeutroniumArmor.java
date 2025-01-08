package com.yuo.endless.Items.Armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Tool.Modifiers;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class NeutroniumArmor extends ArmorItem {
    private static final String TAG_PHANTOM_INK = "phantomInk";

    public NeutroniumArmor(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Properties().stacksTo(1).tab(EndlessTab.endless));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = getDefaultAttributeModifiers(slot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        Item item = stack.getItem();
        if (slot == EquipmentSlot.HEAD && item == EndlessItems.neutroniumHead.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(0,3d));
                builder.put(Attributes.ATTACK_SPEED, Modifiers.getModifierAtkSpeed(0,0.03d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0,0.04d));
            return builder.build();
        }else if (slot == EquipmentSlot.CHEST && item == EndlessItems.neutroniumChest.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(1,8d));
                builder.put(Attributes.ATTACK_SPEED, Modifiers.getModifierAtkSpeed(1,0.03d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(1,0.04d));
            return builder.build();
        }else if (slot == EquipmentSlot.LEGS && item == EndlessItems.neutroniumLegs.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(2,6d));
                builder.put(Attributes.ATTACK_SPEED, Modifiers.getModifierAtkSpeed(2,0.03d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(2,0.04d));
            return builder.build();
        }else if (slot == EquipmentSlot.FEET && item == EndlessItems.neutroniumFeet.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(3,3d));
                builder.put(Attributes.ATTACK_SPEED, Modifiers.getModifierAtkSpeed(3,0.03d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(3,0.04d));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }
}
