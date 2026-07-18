package com.yuo.endless.compat.pe;

import com.yuo.endless.Endless;
import com.yuo.endless.recipe.NeutroniumRecipe;
import com.yuo.endless.recipe.EndlessRecipes;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.mapper.recipe.INSSFakeGroupManager;
import moze_intel.projecte.api.mapper.recipe.IRecipeTypeMapper;
import moze_intel.projecte.api.mapper.recipe.RecipeTypeMapper;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

@RecipeTypeMapper(requiredMods = Endless.MOD_ID, priority = 1)
public class CompressCraftEmc implements IRecipeTypeMapper {

    @Override
    public String getName() {
        return EndlessEmc.name("ExtremeCrafting");
    }

    public String getDescription() {
        return "Maps endless neutronium recipes.";
    }

    public boolean canHandle(RecipeType<?> recipeType) {
        return recipeType == EndlessRecipes.NEUTRONIUM_RECIPE.get();
    }

    @Override
    public boolean handleRecipe(IMappingCollector<NormalizedSimpleStack, Long> mapper, Recipe<?> recipe, RegistryAccess registryAccess, INSSFakeGroupManager fakeGroupManager) {
        if (!(recipe instanceof NeutroniumRecipe neutroniumRecipe)) {
            return false;
        } else {
            boolean handled = false;
            Ingredient ingredient = neutroniumRecipe.getInput();
            int recipeCount = neutroniumRecipe.getRecipeCount();

            ItemStack[] var11 = ingredient.getItems();

            for (ItemStack input : var11) {
                NormalizedSimpleStack inputStack = NSSItem.createItem(input);
                ItemStack output = neutroniumRecipe.getResultItem();
                if (!output.isEmpty()) {
                    EmcUtils ingredientHelper = new EmcUtils(mapper);
                    ingredientHelper.put(inputStack, recipeCount);
                    if (ingredientHelper.addAsConversion(output)) {
                        handled = true;
                    }
                }
            }
            return handled;
        }

    }
}
