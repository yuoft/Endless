package com.yuo.endless.integration.BOT;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.potion.Effects;
import vazkii.botania.common.block.BlockSpecialFlower;

public class AsgardFlower extends BlockSpecialFlower {
    public static final AbstractBlock.Properties FLOWER_PROPS = AbstractBlock.Properties.from(Blocks.POPPY);
    public AsgardFlower() {
        super(Effects.HEALTH_BOOST, 360, FLOWER_PROPS, AsgardFlowerTile::new);
    }
}
