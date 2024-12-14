package com.yuo.endless.Items.Armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Tool.Modifiers;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CrystalMatrixArmor extends ArmorItem {
    private static final String TAG_PHANTOM_INK = "phantomInk";

    public CrystalMatrixArmor(IArmorMaterial material, EquipmentSlotType slot) {
        super(material, slot, new Properties().maxStackSize(1).group(EndlessTab.endless));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = getItem().getAttributeModifiers(slot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        Item item = stack.getItem();
        if (slot == EquipmentSlotType.HEAD && item == EndlessItems.crystalMatrixHead.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(0, 1.5d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0, 0.03d));
            return builder.build();
        }else if (slot == EquipmentSlotType.CHEST && item == EndlessItems.crystalMatrixChest.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(1,4d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0, 0.03d));
            return builder.build();
        }else if (slot == EquipmentSlotType.LEGS && item == EndlessItems.crystalMatrixLegs.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(2,3d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0, 0.03d));
            return builder.build();
        }else if (slot == EquipmentSlotType.FEET && item == EndlessItems.crystalMatrixFeet.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(3,1.5d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0, 0.03d));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }
}
