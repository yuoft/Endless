package com.yuo.endless.Tiles;

import com.yuo.endless.Container.DenseNeutronCollectorContainer;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DenseNeutronCollectorTile extends AbsNeutronCollectorTile{

    public DenseNeutronCollectorTile(){
        super(EndlessTileTypes.DENSE_NEUTRON_COLLECTOR_TILE.get());
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new DenseNeutronCollectorContainer(id, player, this);
    }

    @Override
    protected ItemStack getCraftOutputItem() {
        return new ItemStack(EndlessItems.neutroniumNugget.get());
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("gui.endless.dense_neutronium_collector");
    }
}
