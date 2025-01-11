package com.yuo.endless.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolAction;

/**
 * 通用普通方块制作
 */
public class OrdinaryToolBlock extends Block {

	public OrdinaryToolBlock(Material material, int harvestLevel, ToolAction toolType, float hardness, float resistancelln) {
		super(Properties.of(material).strength(hardness, resistancelln).requiresCorrectToolForDrops());
	}

}
