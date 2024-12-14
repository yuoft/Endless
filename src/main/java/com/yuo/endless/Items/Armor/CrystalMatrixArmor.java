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

public class CrystalMatrixArmor extends ArmorItem {
    private static final String TAG_PHANTOM_INK = "phantomInk";

    public CrystalMatrixArmor(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Properties().stacksTo(1).tab(EndlessTab.endless));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = stack.getItem().getAttributeModifiers(slot, stack);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        Item item = stack.getItem();
        if (slot == EquipmentSlot.HEAD && item == EndlessItems.crystalMatrixHead.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(0, 1.5d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0, 0.03d));
            return builder.build();
        }else if (slot == EquipmentSlot.CHEST && item == EndlessItems.crystalMatrixChest.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(1,4d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0, 0.03d));
            return builder.build();
        }else if (slot == EquipmentSlot.LEGS && item == EndlessItems.crystalMatrixLegs.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(2,3d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0, 0.03d));
            return builder.build();
        }else if (slot == EquipmentSlot.FEET && item == EndlessItems.crystalMatrixFeet.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(3,1.5d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0, 0.03d));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }
}
