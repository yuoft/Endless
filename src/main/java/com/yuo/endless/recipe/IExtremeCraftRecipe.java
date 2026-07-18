package com.yuo.endless.recipe;

import com.yuo.endless.EndlessUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public interface IExtremeCraftRecipe extends Recipe<Container> {
    ResourceLocation TYPE_ID = EndlessUtils.fa("extreme_craft");
    ResourceLocation TYPE_SHAPE_ID = EndlessUtils.fa("extreme_craft_shape");

    @Override
    abstract @NotNull RecipeType<?> getType();

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
