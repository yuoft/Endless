package com.yuo.endless.Recipe;

import com.yuo.endless.Endless;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Difficulty;

import java.util.ArrayList;
import java.util.Iterator;

//配方管理
public class CompressorManager {

    private static final ArrayList<NeutroniumRecipe> recipes = new ArrayList<>();

    /**
     * 添加压缩机配方 如果已有配方则删除旧配方
     * @param output 配方输出
     * @param amount 数量
     * @param input 输入
     */
    static void addRecipe(ItemStack output, int amount, NonNullList<ItemStack> input) {
        boolean flag = true; //直接添加
        if (recipes.size() > 0){
            Iterator<NeutroniumRecipe> iterator = recipes.iterator();
            while (iterator.hasNext()){
                NeutroniumRecipe next = iterator.next();
                if (next.getRecipeOutput().isItemEqual(output)){
                    iterator.remove();
                    recipes.add(new NeutroniumRecipe(new ResourceLocation(Endless.MOD_ID, output.getItem().toString()), input, amount, output));
                    flag = false;
                    break;
                }
            }
        }
        if (flag)
            recipes.add(new NeutroniumRecipe(new ResourceLocation(Endless.MOD_ID, output.getItem().toString()), input, amount, output));
    }

    //获取输出
    public static ItemStack getOutput(ItemStack input) {
        for (NeutroniumRecipe recipe : recipes) {
            if (recipe.isInput(input)) //输入相同
                return recipe.getRecipeOutput();
        }
        return ItemStack.EMPTY;
    }

    //通过输入获取消耗数量
    public static int getCost(ItemStack input) {
        if (input == null || input.isEmpty())
            return 0;

        for (NeutroniumRecipe recipe : recipes) {
            if (recipe.isInput(input))
                return recipe.getRecipeCount();
        }
        return 0;
    }

    //通过输出获取消耗数量
    public static int getPrice(ItemStack output) {
        if (output == null || output.isEmpty())
            return 0;

        for (NeutroniumRecipe recipe : recipes) {
            if (recipe.getRecipeOutput().isItemEqual(output))
                return recipe.getRecipeCount();
        }
        return 0;
    }

    /**
     * 根据当前游戏难度修改奇点合成所需数量
     * @param difficulty 难度
     * @deprecated
     */
    public static void changeAllCount(Difficulty difficulty){
        ModRecipeManager.addCompressorCraft(); //重置配方
        if (difficulty == Difficulty.EASY || difficulty == Difficulty.PEACEFUL){
            return;
        }
        int count = difficulty == Difficulty.HARD ? 100 : 50;
        for (NeutroniumRecipe recipe : recipes) {
            recipe.setCount(recipe.getRecipeCount() + count);
        }
    }

    /**
     * 根据输出追加输入
     * @param output  输出
     * @param map 输入
     */
    public static void addInputs(Item output, NonNullList<ItemStack> map) {
        if (map.isEmpty()) return;
        for (NeutroniumRecipe recipe : recipes) {
            if (recipe.getRecipeOutput().isItemEqual(new ItemStack(output))){
                recipe.addInput(map);
            }
        }
    }

    /**
     * 根据输入获取当前输入的物品压缩效率
     * @param input 输入
     * @return 效率
     */
    public static int getInputCost(ItemStack input) {
        if (input.isEmpty()) return 1;
        for (NeutroniumRecipe recipe : recipes) {
            if (recipe.isInput(input)) {
                NonNullList<ItemStack> map = recipe.getRecipeInput();
                for (ItemStack stack : map) {
                    if (stack.isItemEqual(input)) return stack.getCount();
                }
            }
        }

        return 1;
    }

    /**
     * 判断两个物品是否可替换
     * @param stack 物品1
     * @param itemStack 物品2
     * @return 可替换 true
     */
    public static boolean isInput(ItemStack stack, ItemStack itemStack){
        for (NeutroniumRecipe recipe : recipes) {
            if (recipe.isInput(stack) && recipe.isInput(itemStack)) return true;
        }
        return false;
    }

    //获取所有配方
    public static ArrayList<NeutroniumRecipe> getRecipes() {
        return recipes;
    }

}
