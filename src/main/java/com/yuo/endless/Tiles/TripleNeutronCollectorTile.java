package com.yuo.endless.Tiles;

import com.yuo.endless.Container.TripleNeutronCollectorContainer;
import com.yuo.endless.Items.ItemRegistry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TripleNeutronCollectorTile extends AbsNeutronCollectorTile{

    public TripleNeutronCollectorTile(){
        super(TileTypeRegistry.TRIPLE_NEUTRON_COLLECTOR_TILE.get());
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new TripleNeutronCollectorContainer(id, player, this);
    }

    @Override
    protected int getCraftTime() {
        return 400;
    }

    @Override
    protected ItemStack getCraftOutputItem() {
        return new ItemStack(ItemRegistry.neutroniumIngot.get());
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("gui.endless.triple_neutron_collector");
    }
}
