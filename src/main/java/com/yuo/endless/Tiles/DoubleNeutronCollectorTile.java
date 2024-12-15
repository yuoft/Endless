package com.yuo.endless.Tiles;

import com.yuo.endless.Container.DoubleNeutronCollectorContainer;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DoubleNeutronCollectorTile extends AbsNeutronCollectorTile{

    public DoubleNeutronCollectorTile(BlockPos pos, BlockState state){
        super(EndlessTileTypes.DOUBLE_NEUTRON_COLLECTOR_TILE.get(), pos, state);
    }

    public DoubleNeutronCollectorTile(BlockEntityType<DoubleNeutronCollectorTile> type) {
        super(type);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new DoubleNeutronCollectorContainer(id, inventory, this);
    }

    @Override
    protected ItemStack getCraftOutputItem() {
        return new ItemStack(EndlessItems.neutroniumIngot.get());
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.endless.denser_neutronium_collector");
    }
}
