package com.yuo.endless.Items.Tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.EndlessTab;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.function.Consumer;

public class OrdinaryHoe extends HoeItem implements IManaUsingItem {

    public OrdinaryHoe(IItemTier tier) {
        super(tier, -8,0f, new Properties().group(EndlessTab.endless).isImmuneToFire());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = getItem().getAttributeModifiers(slot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND){
            if (stack.getItem() == EndlessItems.crystalMatrixHoe.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(8,0.03d));
            }else if (stack.getItem() == EndlessItems.neutroniumHoe.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(8,-0.04d));
            }
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean usesMana(ItemStack itemStack) {
        return true;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return ToolCommons.damageItemIfPossible(stack, amount, entity, stack.getItem() == EndlessItems.crystalMatrixHoe.get() ? 10 : 20);
    }
}
