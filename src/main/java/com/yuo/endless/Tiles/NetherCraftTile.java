package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.ExtremeCraft.CraftType;
import com.yuo.endless.Container.Craft.NetherCraftContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class NetherCraftTile extends AbsCraftTile{
    public NetherCraftTile() {
        super(CraftType.NETHER_CRAFT, EndlessTileTypes.NETHER_CRAFT_TILE.get());
    }

    @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("gui.endless.nether_crafting_table");
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory) {
        return new NetherCraftContainer(i, playerInventory, this);
    }
}
