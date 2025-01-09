package com.yuo.endless.Container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

public class NeutronCollectorContainer extends AbsNeutronCollectorContainer{

    public NeutronCollectorContainer(int id, Inventory playerInventory, FriendlyByteBuf buf){
        this(id, playerInventory, (Container) playerInventory.player.level.getBlockEntity(buf.readBlockPos()));
    }
    public NeutronCollectorContainer(int id, Inventory playerInventory, Container tile) {
        super(id, playerInventory, EndlessMenuTypes.neutronCollectorContainer.get(), tile);
    }

}
