package com.yuo.endless.Tiles;

import com.yuo.endless.Container.TripleNeutronCollectorContainer;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TripleNeutronCollectorTile extends AbsNeutronCollectorTile{

    public TripleNeutronCollectorTile(BlockPos pos, BlockState state){
        super(EndlessTileTypes.TRIPLE_NEUTRON_COLLECTOR_TILE.get(), pos, state);
    }

    public TripleNeutronCollectorTile(BlockEntityType<TripleNeutronCollectorTile> type) {
        super(type);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new TripleNeutronCollectorContainer(id, inventory, this);
    }

    @Override
    public int getCraftTime() {
        return 200;
    }

    @Override
    protected ItemStack getCraftOutputItem() {
        return new ItemStack(EndlessItems.neutroniumIngot.get());
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.endless.densest_neutronium_collector");
    }
}
