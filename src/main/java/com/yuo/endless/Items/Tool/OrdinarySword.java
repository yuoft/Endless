package com.yuo.endless.Items.Tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class OrdinarySword extends SwordItem {

    public OrdinarySword(IItemTier tier) {
        super(tier, 0,-2.4f, new Properties().group(ModGroup.endless).isImmuneToFire());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = getItem().getAttributeModifiers(slot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND){
            if (stack.getItem() == EndlessItems.crystalMatrixSword.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(4,0.03d));
            }else if (stack.getItem() == EndlessItems.neutroniumSword.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(4,-0.05d));
            }
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }
}
