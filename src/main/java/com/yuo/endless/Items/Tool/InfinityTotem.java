package com.yuo.endless.Items.Tool;

import com.yuo.endless.tab.ModGroup;
import net.minecraft.item.Item;

public class InfinityTotem extends Item {
    public InfinityTotem() {
        super(new Properties().maxStackSize(1).maxDamage(10).group(ModGroup.endless).isImmuneToFire());
    }


}
