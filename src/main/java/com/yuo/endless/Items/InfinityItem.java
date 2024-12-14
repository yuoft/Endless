package com.yuo.endless.Items;

import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.EndlessItemEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class InfinityItem extends Item {
    public InfinityItem() {
        super(new Properties().tab(EndlessTab.endless).fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> components, TooltipFlag pIsAdvanced) {
        Item item = stack.getItem();
        if (item == EndlessItems.infinityCatalyst.get()){
            components.add(new TranslatableComponent("endless.text.itemInfo.infinity_catalyst"));
        }
        if (item == EndlessItems.infinityIngot.get()){
            components.add(new TranslatableComponent("endless.text.itemInfo.infinity_ingot"));
        }
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        if (stack.getItem() == EndlessItems.eternalSingularity.get()) return true;
        return super.hasCustomEntity(stack);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public Entity createEntity(Level level, Entity location, ItemStack stack) {
        if (hasCustomEntity(stack)) return new EndlessItemEntity(level, location, stack);
        return super.createEntity(level, location, stack);
    }

}
