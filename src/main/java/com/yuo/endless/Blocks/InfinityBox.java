package com.yuo.endless.Blocks;

import com.yuo.endless.Tiles.EndlessTileTypes;
import com.yuo.endless.Tiles.InfinityBoxTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class InfinityBox extends AbsEndlessChest {
    public InfinityBox() {
        super(EndlessTileTypes.INFINITY_CHEST_TILE::get, EndlessChestType.INFINITY,
                AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(10f, 1000f).harvestTool(ToolType.PICKAXE).harvestLevel(3).setRequiresTool());
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new InfinityBoxTile();
    }

}
