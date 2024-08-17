package com.yuo.endless.Tiles;

import com.yuo.endless.Container.DoubleNeutronCollectorContainer;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class DoubleNeutronCollectorTile extends AbsNeutronCollectorTile{

    public DoubleNeutronCollectorTile(){
        super(EndlessTileTypes.DOUBLE_NEUTRON_COLLECTOR_TILE.get());
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new DoubleNeutronCollectorContainer(id, player, this);
    }

    @Override
    protected int getCraftTime() {
        return 3600;
    }

    @Override
    protected ItemStack getCraftOutputItem() {
        return new ItemStack(EndlessItems.neutroniumIngot.get());
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("gui.endless.denser_neutronium_collector");
    }
}
