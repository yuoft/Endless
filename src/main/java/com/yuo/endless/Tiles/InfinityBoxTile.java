package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Container.InfinityBoxContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;


public class InfinityBoxTile extends AbsEndlessChestTile {

    public InfinityBoxTile(){
        super(TileTypeRegistry.INFINITY_CHEST_TILE.get(), EndlessChestType.INFINITY, () -> EndlessBlocks.infinityBox.get());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.endless.infinity_chest");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new InfinityBoxContainer(id, player, this);
    }

}
