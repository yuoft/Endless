package com.yuo.endless.Items.Tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.Items.ItemRegistry;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;

public class OrdinaryAxe extends AxeItem {

    public OrdinaryAxe(IItemTier tier) {
        super(tier, 2,-3.0f, new Properties().group(ModGroup.endless).isImmuneToFire());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = getItem().getAttributeModifiers(slot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND){
            if (stack.getItem() == ItemRegistry.crystalAxe.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(6,0.03d));
            }else if (stack.getItem() == ItemRegistry.neutronAxe.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(6,-0.05d));
            }
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }
}
