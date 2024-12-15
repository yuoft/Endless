package com.yuo.endless.Container;

import com.yuo.endless.Tiles.EndlessTileTypes;
import com.yuo.endless.Tiles.NeutronCollectorTile;
import net.minecraft.world.entity.player.Inventory;

public class NeutronCollectorContainer extends AbsNeutronCollectorContainer{

    public NeutronCollectorContainer(int id, Inventory playerInventory){
        this(id, playerInventory, new NeutronCollectorTile(EndlessTileTypes.NEUTRON_COLLECTOR_TILE.get()));
    }
    public NeutronCollectorContainer(int id, Inventory playerInventory, NeutronCollectorTile tile) {
        super(id, playerInventory, EndlessMenuTypes.neutronCollectorContainer.get(), tile);
    }

}
