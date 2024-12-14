package com.yuo.endless.Container;

import com.yuo.endless.Tiles.DenseNeutronCollectorTile;
import net.minecraft.entity.player.PlayerInventory;

public class DenseNeutronCollectorContainer extends AbsNeutronCollectorContainer{

    public DenseNeutronCollectorContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory, new DenseNeutronCollectorTile());
    }
    public DenseNeutronCollectorContainer(int id, PlayerInventory playerInventory, DenseNeutronCollectorTile tile) {
        super(id, playerInventory, ContainerTypeRegistry.denseNeutronCollectorContainer.get(), tile);
    }

}
