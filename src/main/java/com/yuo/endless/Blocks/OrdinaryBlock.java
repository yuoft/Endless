package com.yuo.endless.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

/**
 * 通用普通方块制作
 */
public class OrdinaryBlock extends Block{

	public OrdinaryBlock(Material material, int harvestLevel, ToolType toolType,float hardness, float resistancelln) {
		super(Properties.create(material).harvestLevel(harvestLevel).harvestTool(toolType)
				.hardnessAndResistance(hardness, resistancelln).setRequiresTool());
	}

}
