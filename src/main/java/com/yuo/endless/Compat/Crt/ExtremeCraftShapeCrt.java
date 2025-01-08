package com.yuo.endless.Compat.Crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Singularity;
import com.yuo.endless.Recipe.ExtremeCraftShapeRecipe;
import com.yuo.endless.Recipe.ExtremeCraftShpaelessManager;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.openzen.zencode.java.ZenCodeType;

@ZenCodeType.Name("mods.endless.ExtremeCraftShapeRecipe")
@ZenRegister
public class ExtremeCraftShapeCrt {

    /**
     * 添加一个无尽工作台配方
     * @param id    配方名
     * @param output 输出物品
     * @param inputs  输入物品项 二维数组9*9
     */
    @ZenCodeType.Method
    public static void addShaped(String id, IItemStack output, IIngredient[] inputs) {
        CraftTweakerAPI.apply(new IRuntimeAction() {
            @Override
            public void apply() {
                ResourceLocation res = new ResourceLocation("crafttweaker", id);
                //转化为列表
                NonNullList<Ingredient> ingredients = NonNullList.create();
                for (IIngredient input : inputs) {
                    Ingredient ingredient = input.asVanillaIngredient();
                    ItemStack singularity = new ItemStack(EndlessItems.singularity.get());
                    if (ExtremeCraftingCrt.test(ingredient, singularity)) { //如果配方中有奇点，则补全奇点数据后替换
                        addStack(ingredients, ingredient);
                    } else ingredients.add(ingredient);
                }

                //添加到模组配方管理
                ExtremeCraftShapeRecipe recipe = new ExtremeCraftShapeRecipe(res, ingredients, output.getInternal());
                ExtremeCraftShpaelessManager.getInstance().addRecipe(recipe);
            }

            @Override
            public String describe() {
                return "Adding ExtremeCraft recipe for " + output.getCommandString();
            }
        });
    }

    public static void addStack(NonNullList<Ingredient> ingredients, Ingredient ingredient) {
        ItemStack[] matchingStacks = ingredient.getItems();
        for (int i = 0; i < matchingStacks.length; i++) {
            ItemStack stack = matchingStacks[i];
            if (stack.getItem() instanceof Singularity) {
                matchingStacks[i] = Singularity.getSingularity(stack.getOrCreateTag().getString(Singularity.NBT_TYPE));
            }
        }
        ingredients.add(Ingredient.of(matchingStacks));
    }

    @ZenCodeType.Method
    public static void remove(IItemStack stack) {
        CraftTweakerAPI.apply(new IRuntimeAction() {
            @Override
            public void apply() {
                ExtremeCraftShpaelessManager.getInstance().removeRecipe(stack.getInternal());
            }

            @Override
            public String describe() {
                return "Removing ExtremeCraft recipes for " + stack.getCommandString();
            }
        });
    }
}
