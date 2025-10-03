package com.yuo.endless.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

/**
 * 通用挖掘等级 普通方块制作
 */
public class OrdinaryToolBlock extends Block {
	private final int harvestLevel;
	private final ToolAction toolAction;

	public OrdinaryToolBlock(MapColor material, int harvestLevel, ToolAction toolType, float hardness, float resistancelln) {
		super(Properties.of().mapColor(material).strength(hardness, resistancelln).requiresCorrectToolForDrops());
		this.harvestLevel = harvestLevel;
		this.toolAction = toolType;
	}

	@Override
	public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
		if (player.getMainHandItem().getItem() instanceof DiggerItem digger){
			int levelDig = digger.getTier().getLevel();
			if (toolAction == ToolActions.PICKAXE_DIG && digger instanceof PickaxeItem){
				return levelDig >= harvestLevel;
			}
			if (toolAction == ToolActions.AXE_DIG && digger instanceof AxeItem){
				return levelDig >= harvestLevel;
			}
		}
		return false;
	}
}
