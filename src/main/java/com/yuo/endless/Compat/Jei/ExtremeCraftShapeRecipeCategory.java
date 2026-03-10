package com.yuo.endless.Compat.Jei;

import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Singularity;
import com.yuo.endless.Recipe.ExtremeCraftShapeRecipe;
import com.yuo.endless.EndlessUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("removal")
public class ExtremeCraftShapeRecipeCategory implements IRecipeCategory<ExtremeCraftShapeRecipe> {
    //合成配方背景
    public static final ResourceLocation TEXTURE = EndlessUtils.fa("textures/gui/extreme_jei.png");
    public static final RecipeType<ExtremeCraftShapeRecipe> RECIPE_TYPE = RecipeType.create(Endless.MOD_ID, "extreme_craft_shape", ExtremeCraftShapeRecipe.class);


    private final IDrawable background;
    private final IDrawable icon;

    public ExtremeCraftShapeRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 2,0,184,164); //绘制背景
        this.icon = helper.createDrawableItemStack(new ItemStack(EndlessItems.extremeCraftingTable.get())); //绘制合成方块
    }

    @Override
    public RecipeType<ExtremeCraftShapeRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.endless.extreme_craft_shape");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ExtremeCraftShapeRecipe recipe, IFocusGroup group) {
        NonNullList<Ingredient> inputs = recipe.getIngredients();
        for(int i = 0; i < 9; ++i) {
            for(int j = 0; j < 9; ++j) {
                int index = j + i * 9;
                if (index < inputs.size()) {
                    builder.addSlot(RecipeIngredientRole.INPUT, j * 18, i * 18 + 2).addIngredients(getSingularityIngredient(inputs.get(index)));
                }
            }
        }

        builder.setShapeless();
        builder.addSlot(RecipeIngredientRole.OUTPUT, 165, 73).addItemStack(recipe.getResultItem());
    }


    /**
     * 对奇点进行nbt数据重新设定
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

    public @NotNull List<Component> getTooltipStrings(ExtremeCraftShapeRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        int sX = 340 / 2;
        int sY = 100;
        return mouseX > (double)(sX + 10) && mouseX < (double)(sX + 20) && mouseY > (double)(sY - 1) && mouseY < (double)(sY + 8) ? Collections.singletonList(Component.translatable("jei.tooltip.shapeless.recipe")) : Collections.emptyList();
    }

}
