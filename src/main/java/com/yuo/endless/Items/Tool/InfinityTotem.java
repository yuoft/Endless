package com.yuo.endless.Items.Tool;

import com.yuo.endless.EndlessTab;
import net.minecraft.item.Item;

public class InfinityTotem extends Item {
    public InfinityTotem() {
        super(new Properties().maxStackSize(1).maxDamage(10).group(EndlessTab.endless).isImmuneToFire());
    }


}
