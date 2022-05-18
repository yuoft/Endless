package com.yuo.endless.integration.Jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.ItemRegistry;
import com.yuo.endless.Recipe.NeutroniumRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class NeutroniumCRecipeCategory implements IRecipeCategory<NeutroniumRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Endless.MOD_ID, "neutronium");
    //合成配方背景
    public static final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/gui/compressor.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic singularity;

    public NeutroniumCRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 37,25,102,40); //绘制背景
        this.icon = helper.createDrawableIngredient(new ItemStack(ItemRegistry.neutroniumCompressor.get())); //绘制合成方块
        this.singularity = helper.createDrawable(TEXTURE, 176,16,16,16);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class getRecipeClass() {
        return NeutroniumRecipe.class;
    }

    @Override
    public String getTitle() {
        return ItemRegistry.neutroniumCompressor.get().getName().getString();
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
    public void setIngredients(NeutroniumRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    //绘制输入输出的物品图标
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, NeutroniumRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 1, 9);
        recipeLayout.getItemStacks().init(1, false, 79, 9);
        recipeLayout.getItemStacks().set(ingredients);
    }

    @Override
    public void draw(NeutroniumRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
//        this.progress.draw(matrixStack, 62, 10);
        this.singularity.draw(matrixStack, 52, 10);
    }
}
