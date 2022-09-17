package com.yuo.endless.Armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.Items.ItemRegistry;
import com.yuo.endless.Items.Tool.Modifiers;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeMod;

public class OrdinaryArmor extends ArmorItem {

    public OrdinaryArmor(IArmorMaterial material, EquipmentSlotType slot) {
        super(material, slot, new Properties().maxStackSize(1).group(ModGroup.endless));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = getItem().getAttributeModifiers(slot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        if (slot == EquipmentSlotType.HEAD){
            if (stack.getItem() == ItemRegistry.crystalMatrixHead.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(0, 1.5d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0, 0.03d));
            }else if (stack.getItem() == ItemRegistry.neutroniumHead.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(0,3d));
                builder.put(ForgeMod.SWIM_SPEED.get(), Modifiers.getModifierSwim(0,-0.05d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0,-0.05d));
            }
            return builder.build();
        }else if (slot == EquipmentSlotType.CHEST){
            if (stack.getItem() == ItemRegistry.crystalMatrixChest.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(1,4d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0, 0.03d));
            }else if (stack.getItem() == ItemRegistry.neutroniumChest.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(1,8d));
                builder.put(ForgeMod.SWIM_SPEED.get(), Modifiers.getModifierSwim(1,-0.05d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(1,-0.05d));
            }
            return builder.build();
        }else if (slot == EquipmentSlotType.LEGS){
            if (stack.getItem() == ItemRegistry.crystalMatrixLeggings.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(2,3d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0, 0.03d));
            }else if (stack.getItem() == ItemRegistry.neutroniumLegs.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(2,6d));
                builder.put(ForgeMod.SWIM_SPEED.get(), Modifiers.getModifierSwim(2,-0.05d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(2,-0.05d));
            }
            return builder.build();
        }else if (slot == EquipmentSlotType.FEET){
            if (stack.getItem() == ItemRegistry.crystalMatrixFeet.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(3,1.5d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0, 0.03d));
            }else if (stack.getItem() == ItemRegistry.neutroniumFeet.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(3,3d));
                builder.put(ForgeMod.SWIM_SPEED.get(), Modifiers.getModifierSwim(3,-0.05d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(3,-0.05d));
            }
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }
}
