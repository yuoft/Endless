package com.yuo.endless.Compat.Jei;

import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Recipe.EnderCraftRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class EnderCraftRecipeCategory implements IRecipeCategory<EnderCraftRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Endless.MOD_ID, "ender_craft");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/gui/ender_crafting_jei.png");

    private final IDrawable background;
    private final IDrawable icon;

    public EnderCraftRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 3,13,181,134);
        this.icon = helper.createDrawableIngredient(new ItemStack(EndlessItems.enderCraftingTable.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class getRecipeClass() {
        return EnderCraftRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("jei.endless.ender_craft");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(EnderCraftRecipe recipe, IIngredients ingredients) {
        NonNullList<Ingredient> nullList = recipe.getIngredients();

        int width = recipe.getWidth();
        int height = recipe.getHeight();
        int size = nullList.size();
        int recipeWidth = 7;
        if (width != recipeWidth) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(recipeWidth * height, Ingredient.EMPTY);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < recipeWidth; j++) {
                    int index = j + i * recipeWidth;
                    int floor = (int) Math.floor((recipeWidth - width) / 2.0);
                    int ceil = (int) Math.ceil((recipeWidth - width) / 2.0);
                    int oldidx = Math.min(size - 1, Math.max(0, j - floor) + i * width);
                    if (j > floor - 1 && j < recipeWidth - ceil) {
                        inputs.set(index, nullList.get(oldidx));
                    }
                }
            }
            ingredients.setInputIngredients(inputs);
        } else ingredients.setInputIngredients(nullList);

        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, EnderCraftRecipe recipe, IIngredients ingredients) {
        for (int m = 0; m < 7; m++){
            for (int n = 0; n < 7; n++){
                recipeLayout.getItemStacks().init(n + m * 7, true, 4 + n * 18, 4 + m * 18);
            }
        }
        recipeLayout.getItemStacks().init(49, false, 158, 57);
        recipeLayout.getItemStacks().set(ingredients);
    }

}
