package com.yuo.endless.Compat.Jei;

import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Recipe.NeutroniumRecipe;
import com.yuo.endless.EndlessUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class NeutroniumCRecipeCategory implements IRecipeCategory<NeutroniumRecipe> {
    //合成配方背景
    public static final ResourceLocation TEXTURE = EndlessUtils.fa("textures/gui/compressor.png");
    public static final RecipeType<NeutroniumRecipe> RECIPE_TYPE = RecipeType.create(Endless.MOD_ID, "neutronium", NeutroniumRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic singularity;

    public NeutroniumCRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 38,26,102,40); //绘制背景
        this.icon = helper.createDrawableItemStack(new ItemStack(EndlessItems.neutronCompressor.get())); //绘制合成方块
        this.singularity = helper.createDrawable(TEXTURE, 176,16,16,16);
    }

    @Override
    public RecipeType<NeutroniumRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return EndlessItems.neutronCompressor.get().getDescription();
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
    public void setRecipe(IRecipeLayoutBuilder builder, NeutroniumRecipe recipe, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1,9).addIngredients(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 79, 9).addItemStack(recipe.getResultItem());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, NeutroniumRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        IRecipeCategory.super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);
        if (mouseX > 23 && mouseX < 45 && mouseY > 9 && mouseY < 24) {
            tooltip.add(Component.translatable("endless.text.recipe.count", recipe.getRecipeCount()));
        }

    }
}
