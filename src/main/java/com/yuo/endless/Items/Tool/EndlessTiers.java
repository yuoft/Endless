package com.yuo.endless.Items.Tool;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.RlUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags.Blocks;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class EndlessTiers {
    public static final TagKey<Block> NEEDS_CRYSTAL_TOOL = BlockTags.create(RlUtils.fa("needs_crystal_tool"));
    public static final TagKey<Block> NEEDS_NEUTRON_TOOL = BlockTags.create(RlUtils.fa("needs_neutron_tool"));
    public static final TagKey<Block> NEEDS_INFINITY_TOOL = BlockTags.create(RlUtils.fa("needs_infinity_tool"));

    public static Tier SKULL_FIRE = TierSortingRegistry.registerTier(new ForgeTier(3, 1561, 10, 10, 10, BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(EndlessBlocks.crystalMatrixBlock.get())), RlUtils.fa( "skull_fire"),
            List.of(Tiers.IRON), List.of(Tiers.NETHERITE));
    public static Tier CRYSTAL = TierSortingRegistry.registerTier(new ForgeTier(5, 2401, 120, 48, 17, NEEDS_CRYSTAL_TOOL,
                    () -> Ingredient.of(EndlessItems.crystalMatrixIngot.get())), RlUtils.fa("crystal"),
            List.of(Tiers.NETHERITE), List.of());
    public static Tier NEUTRON = TierSortingRegistry.registerTier(new ForgeTier(6, 3152, 300, 97, 21, NEEDS_NEUTRON_TOOL,
                    () -> Ingredient.of(EndlessItems.neutroniumIngot.get())), RlUtils.fa("neutron"),
            List.of(CRYSTAL), List.of());

    //数值无穷表示：Double或Float的POSITIVE_INFINITY（正）或NEGATIVE_INFINITY（负）
    public static Tier INFINITY_TOOL = TierSortingRegistry.registerTier(new ForgeTier(9999,9999, Float.MAX_VALUE,10, 99, NEEDS_INFINITY_TOOL,
            () -> Ingredient.of(EndlessItems.infinityIngot.get())), RlUtils.fa("infinity_tool"),
            List.of(NEUTRON), List.of());
    public static Tier INFINITY_SWORD = TierSortingRegistry.registerTier(new ForgeTier(9999, 9999, 999, Float.POSITIVE_INFINITY, 99,  NEEDS_INFINITY_TOOL,
            () -> Ingredient.of(EndlessItems.infinityIngot.get())), RlUtils.fa("infinity_sword"),
            List.of(NEUTRON), List.of());

}

