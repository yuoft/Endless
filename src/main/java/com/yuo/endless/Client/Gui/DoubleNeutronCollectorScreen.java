package com.yuo.endless.Client.Gui;

import com.yuo.endless.Container.AbsNeutronCollectorContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class DoubleNeutronCollectorScreen extends AbsNeutronCollectorScreen{

    public DoubleNeutronCollectorScreen(AbsNeutronCollectorContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn, 37);
    }
}
