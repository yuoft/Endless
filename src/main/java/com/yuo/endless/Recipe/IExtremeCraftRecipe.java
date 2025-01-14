package com.yuo.endless.Recipe;

import com.yuo.endless.Endless;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IExtremeCraftRecipe extends Recipe<Container> {
    ResourceLocation TYPE_ID = new ResourceLocation(Endless.MOD_ID, "extreme_craft");
    ResourceLocation TYPE_SHAPE_ID = new ResourceLocation(Endless.MOD_ID, "extreme_craft_shape");

    @Override
    abstract RecipeType<?> getType();

    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    //不出现在配方手册中
    @Override
    default boolean isSpecial() {
        return true;
    }
}
