package com.yuo.endless.Container;

import com.yuo.endless.Tiles.TripleNeutronCollectorTile;
import net.minecraft.entity.player.PlayerInventory;

public class TripleNeutronCollectorContainer extends AbsNeutronCollectorContainer{

    public TripleNeutronCollectorContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory, new TripleNeutronCollectorTile());
    }
    public TripleNeutronCollectorContainer(int id, PlayerInventory playerInventory, TripleNeutronCollectorTile tile) {
        super(id, playerInventory, ContainerTypeRegistry.tripleNeutronCollectorContainer.get(), tile);
    }

}
