package com.yuo.endless.Container;

import com.yuo.endless.Tiles.NeutronCollectorTile;
import net.minecraft.entity.player.PlayerInventory;

public class NeutronCollectorContainer extends AbsNeutronCollectorContainer{

    public NeutronCollectorContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory, new NeutronCollectorTile());
    }
    public NeutronCollectorContainer(int id, PlayerInventory playerInventory, NeutronCollectorTile tile) {
        super(id, playerInventory, ContainerTypeRegistry.neutronCollectorContainer.get(), tile);
    }

}
