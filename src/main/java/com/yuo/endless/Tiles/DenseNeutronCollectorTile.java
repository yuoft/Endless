package com.yuo.endless.Tiles;

import com.yuo.endless.Container.DenseNeutronCollectorContainer;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DenseNeutronCollectorTile extends AbsNeutronCollectorTile{

    public DenseNeutronCollectorTile(BlockPos pos, BlockState state){
        super(EndlessTileTypes.DENSE_NEUTRON_COLLECTOR_TILE.get(), pos, state);
    }

    public DenseNeutronCollectorTile(BlockEntityType<DenseNeutronCollectorTile> type) {
        super(type);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new DenseNeutronCollectorContainer(id, inventory, this);
    }

    @Override
    protected ItemStack getCraftOutputItem() {
        return new ItemStack(EndlessItems.neutroniumNugget.get());
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.endless.dense_neutronium_collector");
    }
}
