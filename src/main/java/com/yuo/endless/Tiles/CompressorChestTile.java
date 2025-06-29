package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Container.Chest.CompressorChestContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class CompressorChestTile extends AbsEndlessChestTile {

    public CompressorChestTile(BlockPos pos, BlockState state) {
        super(EndlessTileTypes.COMPRESS_CHEST_TILE.get(), EndlessChestType.COMPRESSOR, () -> EndlessBlocks.compressedChest.get(), pos, state);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new CompressorChestContainer(id, inventory, this);
    }
}
