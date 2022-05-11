package com.yuo.endless.Recipe;

import com.yuo.endless.Endless;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public interface IExtremeCraftRecipe extends IRecipe<IInventory> {
    ResourceLocation TYPE_ID = new ResourceLocation(Endless.MOD_ID, "extreme_craft");

    @Override
    default IRecipeType<?> getType(){
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

    @Override
    default boolean canFit(int width, int height) {
        return true;
    }

    //不出现在配方手册中
    @Override
    default boolean isDynamic() {
        return true;
    }
}
