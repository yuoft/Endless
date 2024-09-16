package com.yuo.endless.Items;

import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.EndlessItemEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
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

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        if (stack.getItem() == EndlessItems.eternalSingularity.get()) return true;
        return super.hasCustomEntity(stack);
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        if (hasCustomEntity(itemstack)) return new EndlessItemEntity(world, location, itemstack);
        return super.createEntity(world, location, itemstack);
    }
}
