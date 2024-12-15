package com.yuo.endless.Container;

import com.yuo.endless.Tiles.DoubleNeutronCollectorTile;
import com.yuo.endless.Tiles.EndlessTileTypes;
import net.minecraft.world.entity.player.Inventory;

public class DoubleNeutronCollectorContainer extends AbsNeutronCollectorContainer{

    public DoubleNeutronCollectorContainer(int id, Inventory playerInventory){
        this(id, playerInventory, new DoubleNeutronCollectorTile(EndlessTileTypes.DOUBLE_NEUTRON_COLLECTOR_TILE.get()));
    }
    public DoubleNeutronCollectorContainer(int id, Inventory playerInventory, DoubleNeutronCollectorTile tile) {
        super(id, playerInventory, EndlessMenuTypes.doubleNeutronCollectorContainer.get(), tile);
    }

}
