package com.yuo.endless.Items.Tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.ItemRegistry;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

import java.util.UUID;

public class OrdinarySword extends SwordItem {
    public static AttributeModifier modifier0 = new AttributeModifier(UUID.fromString("d164b605-3715-49ca-bea3-1e67080d3f64"), Endless.MOD_ID + ":max_health",10, AttributeModifier.Operation.ADDITION);
    public static AttributeModifier modifier1 = new AttributeModifier(UUID.fromString("d164b605-3715-49ca-bea3-1e67080d3f65"), Endless.MOD_ID + ":armor",2, AttributeModifier.Operation.ADDITION);

    public OrdinarySword(IItemTier tier) {
        super(tier, 0,-2.4f, new Properties().group(ModGroup.endless).isImmuneToFire());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
//        if (slot == EquipmentSlotType.MAINHAND){
//            Multimap<Attribute, AttributeModifier> multimap = getItem().getAttributeModifiers(slot);
//            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
//            builder.putAll(multimap);
//            if (stack.getItem() == ItemRegistry.test0.get()){
//                builder.put(Attributes.MAX_HEALTH, modifier0);
//            }else if (stack.getItem() == ItemRegistry.test1.get()){
//                builder.put(Attributes.ARMOR, modifier1);
//            }
//            return builder.build();
//        }
        return super.getAttributeModifiers(slot, stack);
    }
}
