package com.yuo.endless.Compat.Jei;

import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Recipe.ExtremeCraftRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class ExtremeCraftRecipeCategory implements IRecipeCategory<ExtremeCraftRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Endless.MOD_ID, "extreme_craft");
    //合成配方背景
    public static final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/gui/extreme_jei.png");

    private final IDrawable background;
    private final IDrawable icon;

    public ExtremeCraftRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 2,0,184,164); //绘制背景
        this.icon = helper.createDrawableItemStack(new ItemStack(EndlessItems.extremeCraftingTable.get())); //绘制合成方块
    }

    @Override
    @Deprecated
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    @Deprecated
    public Class<ExtremeCraftRecipe> getRecipeClass() {
        return ExtremeCraftRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TextComponent(I18n.get("jei.endless.extreme_craft"));
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
        int size = nullList.size();
        if (width != 9) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(9 * height, Ingredient.EMPTY); //防止物品因为配方宽度不足9格错位
            for (int i = 0; i < height; i++) { //i = 3
                for (int j = 0; j < 9; j++) {
                    int index = j + i * 9; //9*9中位置
                    int floor = (int) Math.floor((9 - width) / 2.0); //配方左边距离
                    int ceil = (int) Math.ceil((9 - width) / 2.0); //配方右边距离
                    int oldidx = Math.min(size - 1, Math.max(0, j - floor) + i * width); //旧配方中位置
                    if (j > floor - 1 && j < 9 - ceil) { //两边留空
                        inputs.set(index, nullList.get(oldidx));
                    }
                }
            }
            ingredients.setInputIngredients(inputs);
        } else ingredients.setInputIngredients(nullList);

        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    //绘制输入输出的物品图标
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ExtremeCraftRecipe recipe, IIngredients ingredients) {
        for (int m = 0; m < 9; m++){
            for (int n = 0; n < 9; n++){
                recipeLayout.getItemStacks().init(n + m * 9, true, -1 + n * 18, 1 + m * 18);
            }
        }
        recipeLayout.getItemStacks().init(81, false, 164, 72);
        recipeLayout.getItemStacks().set(ingredients);
    }

    /*
    //填充输入输出
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ExtremeCraftRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> nullList = recipe.getIngredients();

        int width = recipe.getWidth(); //获取配方实际尺寸
        int height = recipe.getHeight();
        int size = nullList.size();
        if (width != 9) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(9 * height, Ingredient.EMPTY); //防止物品因为配方宽度不足9格错位
            for (int i = 0; i < height; i++) { //i = 3
                for (int j = 0; j < 9; j++) {
                    int index = j + i * 9; //9*9中位置
                    int floor = (int) Math.floor((9 - width) / 2.0); //配方左边距离
                    int ceil = (int) Math.ceil((9 - width) / 2.0); //配方右边距离
                    int oldidx = Math.min(size - 1, Math.max(0, j - floor) + i * width); //旧配方中位置
                    if (j > floor - 1 && j < 9 - ceil) { //两边留空
                        inputs.set(index, nullList.get(oldidx));
                        builder.addSlot(RecipeIngredientRole.INPUT, -1 + j * 18, 1 + i * 18).addIngredients(inputs.get(index));
                    }
                }
            }

        } else {
            for (int m = 0; m < 9; m++){
                for (int n = 0; n < 9; n++){
                    builder.addSlot(RecipeIngredientRole.INPUT, -1 + n * 18, 1 + m * 18);
                }
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT,164, 72);
    }

    //绘制输入输出的物品图标
    @Override
    public void draw(ExtremeCraftRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
//        for (int m = 0; m < 9; m++){
//            for (int n = 0; n < 9; n++){
//                recipeLayout.getItemStacks().init(n + m * 9, true, -1 + n * 18, 1 + m * 18);
//            }
//        }
//        recipeLayout.getItemStacks().init(81, false, 164, 72);
//        recipeLayout.getItemStacks().set(ingredients);
    }*/
}
