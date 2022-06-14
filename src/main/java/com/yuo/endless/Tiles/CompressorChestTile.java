package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.BlockRegistry;
import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Container.CompressorChestContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class CompressorChestTile extends AbsEndlessChestTile {

    public CompressorChestTile() {
        super(TileTypeRegistry.COMPRESS_CHEST_TILE.get(), EndlessChestType.COMPRESSOR, () -> BlockRegistry.compressorChest.get());
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new CompressorChestContainer(id, player, this);
    }

}
