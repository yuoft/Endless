package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.ExtremeCraft.CraftType;
import com.yuo.endless.Container.Craft.ExtremeCraftContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ExtremeCraftTile extends AbsCraftTile{
    public ExtremeCraftTile() {
        super(CraftType.EXTREME_CRAFT, EndlessTileTypes.EXTREME_CRAFT_TILE.get());
    }

    @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("gui.endless.extreme_crafting_table");
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory) {
        return new ExtremeCraftContainer(i, playerInventory, this);
    }
}
