package com.yuo.endless.Container;

import com.yuo.endless.Tiles.DoubleNeutronCollectorTile;
import net.minecraft.entity.player.PlayerInventory;

public class DoubleNeutronCollectorContainer extends AbsNeutronCollectorContainer{

    public DoubleNeutronCollectorContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory, new DoubleNeutronCollectorTile());
    }
    public DoubleNeutronCollectorContainer(int id, PlayerInventory playerInventory, DoubleNeutronCollectorTile tile) {
        super(id, playerInventory, ContainerTypeRegistry.doubleNeutronCollectorContainer.get(), tile);
    }

}
