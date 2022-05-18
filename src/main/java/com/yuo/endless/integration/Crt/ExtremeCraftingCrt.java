package com.yuo.endless.integration.Crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.yuo.endless.Recipe.ExtremeCraftRecipe;
import com.yuo.endless.Recipe.ExtremeCraftingManager;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenCodeType.Name("mods.endless.ExtremeCraftRecipe")
@ZenRegister
public class ExtremeCraftingCrt {

    /**
     * 添加一个无尽工作台配方
     * @param id    配方名
     * @param output 输出物品
     * @param inputs  输入物品项 二维数组9*9
     */
    @ZenCodeType.Method
    public static void addShaped(String id, IItemStack output, IIngredient[][] inputs) {
        CraftTweakerAPI.apply(new IRuntimeAction() {
            @Override
            public void apply() {
                ResourceLocation res = new ResourceLocation("crafttweaker", id);
                //转化为列表
                NonNullList<Ingredient> ingredients = NonNullList.create();
                for (IIngredient[] input : inputs) {
                    for (IIngredient iIngredient : input) {
                        ingredients.add(iIngredient.asVanillaIngredient());
                    }
                }
                //添加到模组配方管理
                ExtremeCraftRecipe recipe = new ExtremeCraftRecipe(res, 9, 9, ingredients, output.getInternal());
                ExtremeCraftingManager.getInstance().addRecipe(recipe);
            }

            @Override
            public String describe() {
                return "Adding ExtremeCraft recipe for " + output.getCommandString();
            }
        });
    }

    @ZenCodeType.Method
    public static void remove(IItemStack stack) {
        CraftTweakerAPI.apply(new IRuntimeAction() {
            @Override
            public void apply() {
                ExtremeCraftingManager.getInstance().removeRecipe(stack.getInternal());
            }

            @Override
            public String describe() {
                return "Removing ExtremeCraft recipes for " + stack.getCommandString();
            }
        });
    }

}
