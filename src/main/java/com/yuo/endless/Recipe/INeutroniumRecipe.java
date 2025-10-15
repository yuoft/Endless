package com.yuo.endless.Recipe;

import com.yuo.endless.Endless;
import com.yuo.endless.RlUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface INeutroniumRecipe extends Recipe<Container> {
    ResourceLocation TYPE_ID = RlUtils.fa("neutronium");


    @Override
    default RecipeType<?> getType(){
        return RecipeTypeRegistry.NEUTRONIUM_RECIPE;
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }

}
