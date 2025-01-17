package com.yuo.endless.Recipe;

import com.yuo.endless.Endless;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

public interface IExtremeCraftRecipe extends IRecipe<IInventory> {
    ResourceLocation TYPE_ID = new ResourceLocation(Endless.MOD_ID, "extreme_craft");
    ResourceLocation ENDER_TYPE_ID = new ResourceLocation(Endless.MOD_ID, "ender_craft");
    ResourceLocation NETHER_TYPE_ID = new ResourceLocation(Endless.MOD_ID, "nether_craft");
    ResourceLocation TYPE_SHAPE_ID = new ResourceLocation(Endless.MOD_ID, "extreme_craft_shape");
    ResourceLocation ENDER_TYPE_SHAPE_ID = new ResourceLocation(Endless.MOD_ID, "ender_craft_shape");
    ResourceLocation NETHER_TYPE_SHAPE_ID = new ResourceLocation(Endless.MOD_ID, "nether_craft_shape");

    @Override
    IRecipeType<?> getType();

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
