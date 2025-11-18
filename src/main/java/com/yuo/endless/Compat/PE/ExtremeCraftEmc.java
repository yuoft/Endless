package com.yuo.endless.Compat.PE;

import com.yuo.endless.Endless;
import com.yuo.endless.Recipe.EndlessRecipes;
import moze_intel.projecte.api.mapper.recipe.RecipeTypeMapper;
import moze_intel.projecte.emc.mappers.recipe.BaseRecipeTypeMapper;
import net.minecraft.world.item.crafting.RecipeType;

@RecipeTypeMapper(requiredMods = Endless.MOD_ID, priority = 1)
public class ExtremeCraftEmc extends BaseRecipeTypeMapper {

    @Override
    public String getName() {
        return EndlessEmc.name("ExtremeCrafting");
    }

    public String getDescription() {
        return "Maps endless extreme craft recipes.";
    }

    public boolean canHandle(RecipeType<?> recipeType) {
        return recipeType == EndlessRecipes.EXTREME_CRAFT_RECIPE.get();
    }
}
