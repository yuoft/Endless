package com.yuo.endless.Container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

public class DenseNeutronCollectorContainer extends AbsNeutronCollectorContainer{

    public DenseNeutronCollectorContainer(int id, Inventory playerInventory, FriendlyByteBuf buf){
        this(id, playerInventory, (Container) playerInventory.player.level.getBlockEntity(buf.readBlockPos()));
    }

    public DenseNeutronCollectorContainer(int id, Inventory playerInventory, Container tile) {
        super(id, playerInventory, EndlessMenuTypes.denseNeutronCollectorContainer.get(), tile);
    }

}
