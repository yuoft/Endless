package com.yuo.endless.Items.Tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.EndlessTabs;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Iterator;
import java.util.stream.Stream;

public class OrdinaryPickaxe extends PickaxeItem {

    public OrdinaryPickaxe(Tier tier) {
        super(tier, -2,-2.8f, new Properties().fireResistant());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = getDefaultAttributeModifiers(slot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND){
            if (stack.getItem() == EndlessItems.crystalMatrixPickaxe.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(5,0.03d));
            }else if (stack.getItem() == EndlessItems.neutroniumPickaxe.get()){
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(5,0.04d));
            }
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        int blockTool = getBlockTool(state);
        return blockTool == 0 ? 150000 : blockTool == 1 ? 1000000 : super.getDestroySpeed(stack, state);
    }

    /**
     * 返回方块需求工具等级
     * @param state 方块
     * @return 等级
     */
    public static int getBlockTool(BlockState state){
        Stream<TagKey<Block>> tags = state.getTags();
        Iterator<TagKey<Block>> iterator = tags.iterator();
        while (iterator.hasNext()) {
            TagKey<Block> tag = iterator.next();
            ResourceLocation location = tag.location();
            if (location.toString().equals("endless:needs_crystal_tool")) {
                return 0;
            }else if (location.toString().equals("endless:needs_neutron_tool")) {
                return 1;
            }
        }

        return -1;
    }
}
