package com.yuo.endless.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

/**
 * 通用普通方块制作
 */
public class OrdinaryToolBlock extends Block {
	private final int harvestLevel;
	private final ToolAction toolAction;

	public OrdinaryToolBlock(Material material, int harvestLevel, ToolAction toolType, float hardness, float resistancelln) {
		super(Properties.of(material).strength(hardness, resistancelln).requiresCorrectToolForDrops());
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
