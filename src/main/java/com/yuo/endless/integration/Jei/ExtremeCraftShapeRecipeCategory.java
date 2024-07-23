package com.yuo.endless.integration.Jei;

import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Singularity;
import com.yuo.endless.Recipe.ExtremeCraftRecipe;
import com.yuo.endless.Recipe.ExtremeCraftShapeRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ExtremeCraftShapeRecipeCategory implements IRecipeCategory<ExtremeCraftShapeRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Endless.MOD_ID, "extreme_craft_shape");
    //合成配方背景
    public static final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/gui/extreme_jei.png");

    private final IDrawable background;
    private final IDrawable icon;
//    private final IDrawableStatic extreme;

    public ExtremeCraftShapeRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 2,0,184,164); //绘制背景
        this.icon = helper.createDrawableIngredient(new ItemStack(EndlessItems.extremeCraftingTable.get())); //绘制合成方块
//        this.extreme = helper.createDrawable(TEXTURE);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class getRecipeClass() {
        return ExtremeCraftShapeRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("jei.endless.extreme_craft_shape");
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
    public void setIngredients(ExtremeCraftShapeRecipe recipe, IIngredients ingredients) {
        NonNullList<Ingredient> list = recipe.getIngredients();
        NonNullList<Ingredient> nullList = NonNullList.create();
        for (Ingredient ingredient : list) {
            if (ingredient != null){
                for (ItemStack stack : ingredient.getMatchingStacks()) { //通过奇点nbt来获取stack后填入list
                    if (!stack.isEmpty() && stack.getItem() instanceof Singularity){
                        CompoundNBT tag = stack.getTag();
                        if (tag != null) {
                            CompoundNBT nbt = (CompoundNBT) tag.get(Singularity.NBT_MOD);
                            if (nbt == null) { //没有完整奇点nbt数据 则重新获取
                                ItemStack stack1 = Singularity.getSingularity(tag.getString(Singularity.NBT_TYPE));
                                Ingredient ingredient1 = Ingredient.fromStacks(stack1);
                                nullList.add(ingredient1);
                                continue;
                            }
                        }
                    }
                    nullList.add(ingredient);
                }
            }
        }

        ingredients.setInputIngredients(nullList);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    //绘制输入输出的物品图标
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ExtremeCraftShapeRecipe recipe, IIngredients ingredients) {
        for (int m = 0; m < 9; m++){
            for (int n = 0; n < 9; n++){
                recipeLayout.getItemStacks().init(n + m * 9, true, -1 + n * 18, 1 + m * 18);
            }
        }

        recipeLayout.getItemStacks().init(81, false, 164, 72);
        recipeLayout.getItemStacks().set(ingredients);
    }

}
