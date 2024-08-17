package com.yuo.endless.Blocks;

import com.yuo.endless.Tiles.CompressorChestTile;
import com.yuo.endless.Tiles.EndlessTileTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CompressorChest extends AbsEndlessChest{

    public CompressorChest() {
        super(EndlessTileTypes.COMPRESS_CHEST_TILE::get, EndlessChestType.COMPRESSOR,
                AbstractBlock.Properties.create(Material.WOOD).hardnessAndResistance(4f, 10f).sound(SoundType.WOOD));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CompressorChestTile();
    }

}
