package com.yuo.endless.Items.Tool;

import com.yuo.endless.EndlessTab;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SkullfireSword extends SwordItem {
    public SkullfireSword() {
        super(MyItemTier.SKULL_FIRE, 0, -2.4f, new Properties().group(EndlessTab.endless).isImmuneToFire());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("endless.text.itemInfo.skullfire_sword"));
    }
}
