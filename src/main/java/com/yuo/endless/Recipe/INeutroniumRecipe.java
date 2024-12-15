package com.yuo.endless.Recipe;

import com.yuo.endless.Endless;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface INeutroniumRecipe extends Recipe<Container> {
    ResourceLocation TYPE_ID = new ResourceLocation(Endless.MOD_ID, "neutronium");


    @Override
    default RecipeType<?> getType(){
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }

}
