package com.yuo.endless.Compat.Jei;

import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Recipe.ExtremeCraftRecipe;
import com.yuo.endless.Recipe.ExtremeCraftShapeRecipe;
import com.yuo.endless.Recipe.IExtremeCraftRecipe;
import committee.nova.mods.avaritia.api.utils.lang.Localizable;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ExtremeCraftRecipeCategory implements IRecipeCategory<ExtremeCraftRecipe> {
    //合成配方背景
    public static final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/gui/extreme_jei.png");
    public static final RecipeType<ExtremeCraftRecipe> RECIPE_TYPE = RecipeType.create(Endless.MOD_ID, "extreme_craft", ExtremeCraftRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ExtremeCraftRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 2, 0, 184, 164); //绘制背景
        this.icon = helper.createDrawableItemStack(new ItemStack(EndlessItems.extremeCraftingTable.get())); //绘制合成方块
    }

    @Override
    public RecipeType<ExtremeCraftRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(I18n.get("jei.endless.extreme_craft"));
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    //填充输入输出s
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ExtremeCraftRecipe recipe, IFocusGroup group) {
        NonNullList<Ingredient> nullList = recipe.getIngredients();

        int width = recipe.getWidth(); //获取配方实际尺寸
        int height = recipe.getHeight();
        int size = nullList.size();
        NonNullList<Ingredient> inputs = NonNullList.withSize(9 * height, Ingredient.EMPTY); //防止物品因为配方宽度不足9格错位
        if (width != 9) {
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

            if (recipe.getResultItem().getItem() == EndlessItems.infinityFeet.get()){
                ItemStack resultItem = recipe.getResultItem();
            }
            int index = 0;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (inputs.size() <= index) break;
                    Ingredient ingredient = inputs.get(index);
                    if (!ingredient.isEmpty()) {
                        builder.addSlot(RecipeIngredientRole.INPUT,j * 18, 2 + i * 18).addIngredients(ingredient);
                    }
                    index++;
                }
            }
        }else {
            int index = 0;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (inputs.size() <= index) break;
                    builder.addSlot(RecipeIngredientRole.INPUT,j * 18, 2 + i * 18).addIngredients(nullList.get(index));
                    index++;
                }
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 165, 73).addItemStack(recipe.getResultItem());
    }
}
