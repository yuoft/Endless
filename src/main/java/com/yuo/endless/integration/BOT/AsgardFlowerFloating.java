package com.yuo.endless.integration.BOT;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import vazkii.botania.common.block.BlockFloatingSpecialFlower;

public class AsgardFlowerFloating extends BlockFloatingSpecialFlower {
    public static final Properties FLOATING_PROPS = Properties.create(Material.EARTH).hardnessAndResistance(0.5F).sound(SoundType.GROUND).setLightLevel(s -> 15).harvestTool(ToolType.SHOVEL);
    public AsgardFlowerFloating() {
        super(FLOATING_PROPS, AsgardFlowerTile::new);
    }
}
