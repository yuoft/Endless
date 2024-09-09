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
import net.minecraftforge.common.ForgeMod;

public class NeutroniumArmor extends ArmorItem {
    private static final String TAG_PHANTOM_INK = "phantomInk";

    public NeutroniumArmor(IArmorMaterial material, EquipmentSlotType slot) {
        super(material, slot, new Properties().maxStackSize(1).group(EndlessTab.endless));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = getItem().getAttributeModifiers(slot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        Item item = stack.getItem();
        if (slot == EquipmentSlotType.HEAD && item == EndlessItems.neutroniumHead.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(0,3d));
                builder.put(ForgeMod.SWIM_SPEED.get(), Modifiers.getModifierSwim(0,-0.04d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0,-0.04d));
            return builder.build();
        }else if (slot == EquipmentSlotType.CHEST && item == EndlessItems.neutroniumChest.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(1,8d));
                builder.put(ForgeMod.SWIM_SPEED.get(), Modifiers.getModifierSwim(1,-0.04d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(1,-0.04d));
            return builder.build();
        }else if (slot == EquipmentSlotType.LEGS && item == EndlessItems.neutroniumLegs.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(2,6d));
                builder.put(ForgeMod.SWIM_SPEED.get(), Modifiers.getModifierSwim(2,-0.04d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(2,-0.04d));
            return builder.build();
        }else if (slot == EquipmentSlotType.FEET && item == EndlessItems.neutroniumFeet.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(3,3d));
                builder.put(ForgeMod.SWIM_SPEED.get(), Modifiers.getModifierSwim(3,-0.04d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(3,-0.04d));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }
}
