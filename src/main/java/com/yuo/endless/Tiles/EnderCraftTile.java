package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.ExtremeCraft.CraftType;
import com.yuo.endless.Container.Craft.EnderCraftContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class EnderCraftTile extends AbsCraftTile{
    public EnderCraftTile() {
        super(CraftType.ENDER_CRAFT, EndlessTileTypes.ENDER_CRAFT_TILE.get());
    }

    @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("gui.endless.ender_crafting_table");
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory) {
        return new EnderCraftContainer(i, playerInventory, this);
    }
}
