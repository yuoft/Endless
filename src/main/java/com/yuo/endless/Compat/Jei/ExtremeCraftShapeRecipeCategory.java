package com.yuo.endless.Compat.Jei;

import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Singularity;
import com.yuo.endless.Recipe.ExtremeCraftShapeRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ExtremeCraftShapeRecipeCategory implements IRecipeCategory<ExtremeCraftShapeRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(Endless.MOD_ID, "extreme_craft_shape");
    //合成配方背景
    public static final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/gui/extreme_jei.png");

    private final IDrawable background;
    private final IDrawable icon;

    public ExtremeCraftShapeRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 2,0,184,164); //绘制背景
        this.icon = helper.createDrawableIngredient(new ItemStack(EndlessItems.extremeCraftingTable.get())); //绘制合成方块
    }

    @Override
    @Deprecated
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    @Deprecated
    public Class<ExtremeCraftShapeRecipe> getRecipeClass() {
        return ExtremeCraftShapeRecipe.class;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("jei.endless.extreme_craft_shape");
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
                nullList.add(getSingularityIngredient(ingredient));
            }
        }

        ingredients.setInputIngredients(nullList);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    /**
     * 对奇点ing进行nbt数据重新设定
     * @param ingredient 输入
     * @return 输出
     */
    private Ingredient getSingularityIngredient(Ingredient ingredient) {
        ItemStack[] matchingStacks = ingredient.getItems();
        List<ItemStack> singularityStacks = new ArrayList<>();
        for (ItemStack stack : matchingStacks) { //通过奇点nbt来获取stack后填入list
            if (!stack.isEmpty() && stack.getItem() instanceof Singularity){
                CompoundTag tag = stack.getTag();
                if (tag != null) {
                    CompoundTag nbt = (CompoundTag) tag.get(Singularity.NBT_MOD);
                    if (nbt == null) { //没有完整奇点nbt数据 则重新获取
                        ItemStack stack1 = Singularity.getSingularity(tag.getString(Singularity.NBT_TYPE));
                        singularityStacks.add(stack1); //将奇点添加进列表
                    }
                }
            }
        }
        if (!singularityStacks.isEmpty()) {
            return Ingredient.of(singularityStacks.stream());
        }
        return ingredient;
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
