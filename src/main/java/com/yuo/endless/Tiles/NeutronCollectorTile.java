package com.yuo.endless.Tiles;

import com.yuo.endless.Container.NeutronCollectorContainer;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.level.block.state.BlockState;

public class NeutronCollectorTile extends AbsNeutronCollectorTile{

    public NeutronCollectorTile(BlockPos pos, BlockState state){
        super(EndlessTileTypes.NEUTRON_COLLECTOR_TILE.get(), pos, state);
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new NeutronCollectorContainer(id, player, this);
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
