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
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.function.Consumer;

public class OrdinaryPickaxe extends PickaxeItem implements IManaUsingItem {

    public OrdinaryPickaxe(IItemTier tier) {
        super(tier, -2,-2.8f, new Properties().group(EndlessTab.endless).isImmuneToFire());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = getItem().getAttributeModifiers(slot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND){
            if (stack.getItem() == EndlessItems.crystalMatrixPickaxe.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(5,0.03d));
            }else if (stack.getItem() == EndlessItems.neutroniumPickaxe.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(5,-0.04d));
            }
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    //是否使用魔力
    @Override
    public boolean usesMana(ItemStack itemStack) {
        return true;
    }

    //使用魔力修复耐久
    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return ToolCommons.damageItemIfPossible(stack, amount, entity, stack.getItem() == EndlessItems.crystalMatrixPickaxe.get() ? 10 : 20);
    }
}
