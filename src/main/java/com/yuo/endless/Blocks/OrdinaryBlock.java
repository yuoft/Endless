package com.yuo.endless.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.ToolAction;

/**
 * 通用普通方块制作
 */
public class OrdinaryBlock extends Block {

	public OrdinaryBlock(MapColor material, int harvestLevel, ToolAction toolType, float hardness, float resistancelln) {
		super(Properties.of().mapColor(material).strength(hardness, resistancelln));
	}

}
