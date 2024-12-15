package com.yuo.endless.Recipe;

import com.yuo.endless.Endless;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IExtremeCraftRecipe extends Recipe<Container> {
    ResourceLocation TYPE_ID = new ResourceLocation(Endless.MOD_ID, "extreme_craft");
    ResourceLocation TYPE_SHAPE_ID = new ResourceLocation(Endless.MOD_ID, "extreme_craft_shape");

    @Override
    default RecipeType<?> getType(){
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

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
