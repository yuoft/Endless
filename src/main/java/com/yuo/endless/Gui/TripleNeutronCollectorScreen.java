package com.yuo.endless.Gui;

import com.yuo.endless.Container.AbsNeutronCollectorContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class TripleNeutronCollectorScreen extends AbsNeutronCollectorScreen{

    public TripleNeutronCollectorScreen(AbsNeutronCollectorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn, 44);
    }
}
