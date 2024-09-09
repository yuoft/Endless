package com.yuo.endless.Items;

import com.yuo.endless.EndlessTab;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class InfinityItem extends Item {
    public InfinityItem() {
        super(new Properties().group(EndlessTab.endless).isImmuneToFire());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Item item = stack.getItem();
        if (item == EndlessItems.infinityCatalyst.get()){
            tooltip.add(new TranslationTextComponent("endless.text.itemInfo.infinity_catalyst"));
        }
        if (item == EndlessItems.infinityIngot.get()){
            tooltip.add(new TranslationTextComponent("endless.text.itemInfo.infinity_ingot"));
        }
    }
}
