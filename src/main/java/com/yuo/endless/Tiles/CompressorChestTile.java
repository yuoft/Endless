package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Container.CompressorChestContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

public class CompressorChestTile extends AbsEndlessChestTile {

    public CompressorChestTile() {
        super(TileTypeRegistry.COMPRESS_CHEST_TILE.get(), EndlessChestType.COMPRESSOR, () -> EndlessBlocks.compressedChest.get());
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new CompressorChestContainer(id, player, this);
    }

}
