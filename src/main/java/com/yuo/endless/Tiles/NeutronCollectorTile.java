package com.yuo.endless.Tiles;

import com.yuo.endless.Container.NeutronCollectorContainer;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class NeutronCollectorTile extends AbsNeutronCollectorTile{

    public NeutronCollectorTile(){
        super(EndlessTileTypes.NEUTRON_COLLECTOR_TILE.get());
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new NeutronCollectorContainer(id, player, this);
    }

    @Override
    public int getCraftTime() {
        return 3600;
    }

    @Override
    protected ItemStack getCraftOutputItem() {
        return new ItemStack(EndlessItems.neutroniumPile.get());
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("gui.endless.neutronium_collector");
    }
}
