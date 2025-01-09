package com.yuo.endless.Blocks;

import com.yuo.endless.Tiles.CompressorChestTile;
import com.yuo.endless.Tiles.EndlessTileTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;

public class CompressorChest extends AbsEndlessChest{

    public CompressorChest() {
        super(EndlessTileTypes.COMPRESS_CHEST_TILE::get, EndlessChestType.COMPRESSOR,
                Properties.of(Material.WOOD).strength(4f, 10f).sound(SoundType.WOOD));
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CompressorChestTile(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? createTickerHelper(pBlockEntityType, EndlessTileTypes.COMPRESS_CHEST_TILE.get(), CompressorChestTile::lidAnimateTick) : null;
    }
}
