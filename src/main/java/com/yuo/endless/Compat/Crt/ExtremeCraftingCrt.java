package com.yuo.endless.Compat.Crt;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Singularity;
import com.yuo.endless.Recipe.ExtremeCraftRecipe;
import com.yuo.endless.Recipe.ExtremeCraftingManager;
import net.minecraft.item.ItemStack;
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
                        Ingredient ingredient = iIngredient.asVanillaIngredient();
                        ItemStack singularity = new ItemStack(EndlessItems.singularity.get());
                        if (test(ingredient, singularity)){ //如果配方中有奇点，则补全奇点数据后替换
                            ItemStack[] matchingStacks = ingredient.getMatchingStacks();
                            for (int i = 0; i < matchingStacks.length; i++){
                                ItemStack stack = matchingStacks[i];
                                if (stack.getItem() instanceof Singularity){
                                    matchingStacks[i] = Singularity.getSingularity(stack.getOrCreateTag().getString(Singularity.NBT_TYPE));
                                }
                            }
                            ingredients.add(Ingredient.fromStacks(matchingStacks));
                        } else ingredients.add(ingredient);
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

    /**
     * 测试Ingredient是否含有相同物品
     * @param ingredient ig
     * @param stack 测试物品
     * @return 是 true
     */
    private static boolean test(Ingredient ingredient, ItemStack stack){
        if (stack == null) {
            return false;
        } else {
            ingredient.determineMatchingStacks();
            if (ingredient.getMatchingStacks().length == 0) {
                return stack.isEmpty();
            } else {
                for(ItemStack itemstack : ingredient.getMatchingStacks()) {
                    if (itemstack.getItem() == stack.getItem()) {
                        return true;
                    }
                }

                return false;
            }
        }
    }
}
