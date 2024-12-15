package com.yuo.endless.Container;

import com.yuo.endless.Tiles.DenseNeutronCollectorTile;
import com.yuo.endless.Tiles.EndlessTileTypes;
import net.minecraft.world.entity.player.Inventory;

public class DenseNeutronCollectorContainer extends AbsNeutronCollectorContainer{

    public DenseNeutronCollectorContainer(int id, Inventory playerInventory){
        this(id, playerInventory, new DenseNeutronCollectorTile(EndlessTileTypes.DENSE_NEUTRON_COLLECTOR_TILE.get()));
    }
    public DenseNeutronCollectorContainer(int id, Inventory playerInventory, DenseNeutronCollectorTile tile) {
        super(id, playerInventory, EndlessMenuTypes.denseNeutronCollectorContainer.get(), tile);
    }

}
