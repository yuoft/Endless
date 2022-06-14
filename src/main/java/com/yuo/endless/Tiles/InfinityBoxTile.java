package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.BlockRegistry;
import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Container.InfinityBoxContainer;
import com.yuo.endless.Items.Tool.ColorText;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class InfinityBoxTile extends AbsEndlessChestTile {

    public InfinityBoxTile(){
        super(TileTypeRegistry.INFINITY_CHEST_TILE.get(), EndlessChestType.INFINITY, () -> BlockRegistry.infinityBox.get());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(ColorText.makeFabulous(I18n.format("gui.endless.infinity_box")));
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new InfinityBoxContainer(id, player, this);
    }

}
