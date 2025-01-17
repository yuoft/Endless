package com.yuo.endless.Compat.Jei;

import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Recipe.NetherCraftShapeRecipe;
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

public class NetherCraftShapeRecipeCategory implements IRecipeCategory<NetherCraftShapeRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Endless.MOD_ID, "nether_craft_shape");
    public static final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/gui/nether_crafting_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public NetherCraftShapeRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 8,12,160,100);
        this.icon = helper.createDrawableIngredient(new ItemStack(EndlessItems.netherCraftingTable.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class getRecipeClass() {
        return NetherCraftShapeRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("jei.endless.nether_craft_shape");
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
    public void setIngredients(NetherCraftShapeRecipe recipe, IIngredients ingredients) {
        NonNullList<Ingredient> list = recipe.getIngredients();
        NonNullList<Ingredient> nullList = NonNullList.create();
        for (Ingredient ingredient : list) {
            if (ingredient != null){
                nullList.add(ExtremeCraftShapeRecipeCategory.getSingularityIngredient(ingredient));
            }
        }

        ingredients.setInputIngredients(nullList);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, NetherCraftShapeRecipe recipe, IIngredients ingredients) {
        for (int m = 0; m < 5; m++){
            for (int n = 0; n < 5; n++){
                recipeLayout.getItemStacks().init(n + m * 5, true, 5 + n * 18, 5 + m * 18);
            }
        }

        recipeLayout.getItemStacks().init(25, false, 133, 40);
        recipeLayout.getItemStacks().set(ingredients);
    }

}
