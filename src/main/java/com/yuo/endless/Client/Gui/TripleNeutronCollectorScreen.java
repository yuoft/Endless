package com.yuo.endless.Client.Gui;

import com.yuo.endless.Container.AbsNeutronCollectorContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TripleNeutronCollectorScreen extends AbsNeutronCollectorScreen{

    public TripleNeutronCollectorScreen(AbsNeutronCollectorContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn, 44);
    }
}
