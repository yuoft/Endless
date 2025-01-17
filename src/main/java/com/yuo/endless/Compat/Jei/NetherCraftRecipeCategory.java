package com.yuo.endless.Compat.Jei;

import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Recipe.NetherCraftRecipe;
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

public class NetherCraftRecipeCategory implements IRecipeCategory<NetherCraftRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Endless.MOD_ID, "nether_craft");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/gui/nether_crafting_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public NetherCraftRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 8,12,160,100);
        this.icon = helper.createDrawableIngredient(new ItemStack(EndlessItems.netherCraftingTable.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class getRecipeClass() {
        return NetherCraftRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("jei.endless.nether_craft");
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
    public void setIngredients(NetherCraftRecipe recipe, IIngredients ingredients) {
        NonNullList<Ingredient> nullList = recipe.getIngredients();

        int width = recipe.getWidth();
        int height = recipe.getHeight();
        int size = nullList.size();
        int recipeWidth = 5;
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
    public void setRecipe(IRecipeLayout recipeLayout, NetherCraftRecipe recipe, IIngredients ingredients) {
        for (int m = 0; m < 5; m++){
            for (int n = 0; n < 5; n++){
                recipeLayout.getItemStacks().init(n + m * 5, true, 5 + n * 18, 5 + m * 18);
            }
        }
        recipeLayout.getItemStacks().init(25, false, 133, 40);
        recipeLayout.getItemStacks().set(ingredients);
    }

}
