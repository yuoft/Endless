package com.yuo.endless.client.gui;

import com.yuo.endless.container.AbsNeutronCollectorContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class NeutronCollectorScreen extends AbsNeutronCollectorScreen{

    public NeutronCollectorScreen(AbsNeutronCollectorContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn, 37);
    }
}
