package com.yuo.endless.integration.Jei;

import com.yuo.endless.Endless;
import com.yuo.endless.Items.ItemRegistry;
import com.yuo.endless.Recipe.ExtremeCraftRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ExtremeCraftRecipeCategory implements IRecipeCategory<ExtremeCraftRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Endless.MOD_ID, "extreme_craft");
    //合成配方背景
    public static final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/gui/extreme_jei.png");

    private final IDrawable background;
    private final IDrawable icon;
//    private final IDrawableStatic extreme;

    public ExtremeCraftRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0,0,190,163); //绘制背景
        this.icon = helper.createDrawableIngredient(new ItemStack(ItemRegistry.extremeCraftingTable.get())); //绘制合成方块
//        this.extreme = helper.createDrawable(TEXTURE);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class getRecipeClass() {
        return ExtremeCraftRecipe.class;
    }

    @Override
    public String getTitle() {
        return ItemRegistry.extremeCraftingTable.get().getName().getString();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    //填充输入输出
    @Override
    public void setIngredients(ExtremeCraftRecipe recipe, IIngredients ingredients) {
        NonNullList<Ingredient> nullList = recipe.getIngredients();

        int width = recipe.getWidth(); //获取配方实际尺寸
        int height = recipe.getHeight();
        if (width != 9) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(9 * height, Ingredient.EMPTY); //防止物品因为配方宽度不足9格错位
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < 9; j++) {
                    int index = j + i * 9; //9*9中位置
                    int oldidx = j + i * width; //配方中位置
                    if (j < width) {
                        inputs.set(index, nullList.get(oldidx));
                    }
                }
            }
            ingredients.setInputIngredients(inputs);
        }
        else ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    //绘制输入输出的物品图标
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ExtremeCraftRecipe recipe, IIngredients ingredients) {
        for (int m = 0; m < 9; m++){
            for (int n = 0; n < 9; n++){
                recipeLayout.getItemStacks().init(n + m * 9, true, 1 + n * 18, 1 + m * 18);
            }
        }
        recipeLayout.getItemStacks().init(81, false, 167, 72);
        recipeLayout.getItemStacks().set(ingredients);
    }

}
