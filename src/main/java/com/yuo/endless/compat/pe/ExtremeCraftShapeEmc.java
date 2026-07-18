package com.yuo.endless.compat.pe;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.mojang.datafixers.util.Pair;
import com.yuo.endless.Endless;
import com.yuo.endless.recipe.EndlessRecipes;
import com.yuo.endless.recipe.ExtremeCraftShapeRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.mapper.recipe.INSSFakeGroupManager;
import moze_intel.projecte.api.mapper.recipe.RecipeTypeMapper;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import moze_intel.projecte.emc.mappers.recipe.BaseRecipeTypeMapper;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.*;

@RecipeTypeMapper(requiredMods = Endless.MOD_ID, priority = 2)
public class ExtremeCraftShapeEmc extends BaseRecipeTypeMapper {

    @Override
    public String getName() {
        return EndlessEmc.name("ExtremeCraftingShape");
    }

    public String getDescription() {
        return "Maps endless extreme craft Shape recipes.";
    }

    public boolean canHandle(RecipeType<?> recipeType) {
        return recipeType == EndlessRecipes.EXTREME_CRAFT_SHAPE_RECIPE.get();
    }

    @Override
    public boolean handleRecipe(IMappingCollector<NormalizedSimpleStack, Long> collector, Recipe<?> recipe, RegistryAccess registryManager, INSSFakeGroupManager inssFakeGroupManager) {
        if (recipe instanceof ExtremeCraftShapeRecipe forgeRecipe) {
            ItemStack output = forgeRecipe.getResultItem();
            if (output.isEmpty()) return false;
            boolean successful = true;
            List<Pair<NormalizedSimpleStack, List<Object2IntMap<NormalizedSimpleStack>>>> fakeGroupMap = new ArrayList<>();
            Object2IntMap<NormalizedSimpleStack> ingredientMap = new Object2IntLinkedOpenHashMap<>();
            for (Ingredient ingredient : forgeRecipe.getIngredients())
                if (!convertIngredient(ingredient, ingredientMap, fakeGroupMap, inssFakeGroupManager, forgeRecipe.getId().toString())) {
                    successful = false;
                    break;
                }
            if (successful) collector.addConversion(output.getCount(), NSSItem.createItem(output), ingredientMap);
            for (Pair<NormalizedSimpleStack, List<Object2IntMap<NormalizedSimpleStack>>> dummyGroupInfo : fakeGroupMap)
                for (Object2IntMap<NormalizedSimpleStack> groupObject2IntMap : dummyGroupInfo.getSecond())
                    collector.addConversion(1, dummyGroupInfo.getFirst(), groupObject2IntMap);
            return true;
        }
        return false;
    }

    public static boolean convertIngredient(Ingredient ingredient, Object2IntMap<NormalizedSimpleStack> ingredientMap, List<Pair<NormalizedSimpleStack, List<Object2IntMap<NormalizedSimpleStack>>>> fakeGroupMap, INSSFakeGroupManager fakeGroupManager, String recipeID) {
        ItemStack[] matches = ingredient.getItems();
        if (matches.length == 1) return !addIngredient(ingredientMap, matches[0].copy());
        else if (matches.length > 0) {
            Set<NormalizedSimpleStack> rawNSSMatches = new HashSet<>();
            List<ItemStack> stacks = new ArrayList<>();

            for (ItemStack match : matches)
                if (!match.isEmpty()) {
                    rawNSSMatches.add(NSSItem.createItem(match));
                    stacks.add(match);
                }

            int count = stacks.size();
            if (count == 1) {
                ItemStack item = stacks.get(0);
                return !addIngredient(ingredientMap, item.copy());
            } else if (count > 1) {
                Tuple<NormalizedSimpleStack, Boolean> group = fakeGroupManager.getOrCreateFakeGroup(rawNSSMatches);
                NormalizedSimpleStack dummy = group.getA();
                ingredientMap.put(dummy, Math.max(-1, 1));
                if (group.getB()) {
                    List<Object2IntMap<NormalizedSimpleStack>> groupIngredientMaps = new ArrayList<>();
                    for (ItemStack stack : stacks) {
                        Object2IntMap<NormalizedSimpleStack> groupIngredientMap = new Object2IntLinkedOpenHashMap<>();
                        if (addIngredient(groupIngredientMap, stack.copy())) return false;
                        groupIngredientMaps.add(groupIngredientMap);
                    }
                    fakeGroupMap.add(new Pair<>(dummy, groupIngredientMaps));
                }
            }
        }
        return true;
    }

    public static boolean addIngredient(Object2IntMap<NormalizedSimpleStack> ingredientMap, ItemStack stack) {
        Item item = stack.getItem();
        try {
            if (item.hasCraftingRemainingItem(stack))
                ingredientMap.put(NSSItem.createItem(item.getCraftingRemainingItem(stack)), -1);
        } catch (Exception e) {
            IceAndFire.LOGGER.error("Failed to mapping recipe", e);
            return true;
        }
        ingredientMap.put(NSSItem.createItem(stack), stack.getCount());
        return false;
    }
}
