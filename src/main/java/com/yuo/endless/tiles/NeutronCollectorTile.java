package com.yuo.endless.tiles;

import com.yuo.endless.container.NeutronCollectorContainer;
import com.yuo.endless.items.EndlessItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class NeutronCollectorTile extends AbsNeutronCollectorTile{

    public NeutronCollectorTile(BlockPos pos, BlockState state){
        this(EndlessTileTypes.NEUTRON_COLLECTOR_TILE.get(), pos, state);
    }

    public NeutronCollectorTile(BlockEntityType<NeutronCollectorTile> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
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
        return Component.translatable("gui.endless.neutronium_collector");
    }
}
