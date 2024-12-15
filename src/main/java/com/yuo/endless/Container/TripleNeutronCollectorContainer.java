package com.yuo.endless.Container;

import com.yuo.endless.Tiles.EndlessTileTypes;
import com.yuo.endless.Tiles.TripleNeutronCollectorTile;
import net.minecraft.world.entity.player.Inventory;

public class TripleNeutronCollectorContainer extends AbsNeutronCollectorContainer{

    public TripleNeutronCollectorContainer(int id, Inventory playerInventory){
        this(id, playerInventory, new TripleNeutronCollectorTile(EndlessTileTypes.TRIPLE_NEUTRON_COLLECTOR_TILE.get()));
    }
    public TripleNeutronCollectorContainer(int id, Inventory playerInventory, TripleNeutronCollectorTile tile) {
        super(id, playerInventory, EndlessMenuTypes.tripleNeutronCollectorContainer.get(), tile);
    }

}
