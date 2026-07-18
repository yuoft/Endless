package com.yuo.endless.recipe;

import com.yuo.endless.EndlessUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public interface INeutroniumRecipe extends Recipe<Container> {
    ResourceLocation TYPE_ID = EndlessUtils.fa("neutronium");


    @Override
    default @NotNull RecipeType<?> getType(){
        return EndlessRecipes.NEUTRONIUM_RECIPE.get();
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }

}
