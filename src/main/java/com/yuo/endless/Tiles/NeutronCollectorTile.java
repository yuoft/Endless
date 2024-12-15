package com.yuo.endless.Tiles;

import com.yuo.endless.Container.NeutronCollectorContainer;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class NeutronCollectorTile extends AbsNeutronCollectorTile{

    public NeutronCollectorTile(BlockPos pos, BlockState state){
        super(EndlessTileTypes.NEUTRON_COLLECTOR_TILE.get(), pos, state);
    }

    public NeutronCollectorTile(BlockEntityType<NeutronCollectorTile> type) {
        super(type);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new NeutronCollectorContainer(id, inventory, this);
    }

    protected ItemStack getCraftOutputItem() {
        return new ItemStack(EndlessItems.neutroniumPile.get());
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("gui.endless.neutronium_collector");
    }
}
