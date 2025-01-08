package com.yuo.endless.Recipe;

import com.yuo.endless.Container.ExtremeCraftInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ExtremeCraftShpaelessManager {
    //实例
    private static final ExtremeCraftShpaelessManager instance = new ExtremeCraftShpaelessManager();
    //配方列表
    private final List<ExtremeCraftShapeRecipe> recipes = new ArrayList<>();

    //获取实例
    public static ExtremeCraftShpaelessManager getInstance() {
        return instance;
    }

    /**
     * 获取所有配方
     * @return 配方列表
     */
    public List<ExtremeCraftShapeRecipe> getRecipeList() {
        return this.recipes;
    }

    /**
     * 添加单个配方 如已有相同配方则不添加
     * @param recipe 需要添加的配方
     */
    public void addRecipe(ExtremeCraftShapeRecipe recipe){
        Iterator<ExtremeCraftShapeRecipe> iterator = this.recipes.iterator();
        while (iterator.hasNext()){
            ExtremeCraftShapeRecipe next = iterator.next();
            if (next.hasOutput(recipe.getResultItem()) && next.getIngredients().containsAll(recipe.getIngredients())){
                iterator.remove(); //添加无序合成配方管理，crt修改，降部分配方该为无序配方
                return;
            }
        }
        this.recipes.add(recipe);
    }

    /**
     * 删除单个配方 根据输出删除
     * @param output 要删除配方的输出
     */
    public void removeRecipe(ItemStack output){
        Iterator<ExtremeCraftShapeRecipe> iterator = this.recipes.iterator();
        while (iterator.hasNext()){
            ExtremeCraftShapeRecipe recipe = iterator.next();
            if (recipe.hasOutput(output)){
                iterator.remove();
                return;
            }
        }

    }

    /**
     * 添加无序配方
     * @param result 合成输出
     * @param stacks 物品列表
     * @return 无尽配方
     */
    public ExtremeCraftShapeRecipe addShapelessRecipe(ItemStack result, ItemStack... stacks) {
        ExtremeCraftShapeRecipe recipe = new ExtremeCraftShapeRecipe(result.getItem().getRegistryName(), getIngredients(stacks), result);
        this.recipes.add(recipe);
        return recipe;
    }

    private NonNullList<Ingredient>  getIngredients(ItemStack... stacks){
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (ItemStack stack : stacks){
            Ingredient ingredient = Ingredient.of(stack);
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    /**
     * 根据输入获取输出 无则返回空
     * @param inventory 输入物品栏
     * @param world 世界
     * @return 输出
     */
    public ItemStack getRecipeOutPut(ExtremeCraftInventory inventory, Level world){
        for (ExtremeCraftShapeRecipe recipe : this.recipes) {
            if (recipe.matches(inventory, world)){
                return recipe.getResultItem();
            }
        }

        return ItemStack.EMPTY;
    }

    /**
     * 向配方追加物品 追加后不能超过81个物品
     * @param recipe 配方 部分
     * @param stacks 物品列表
     */
    public void addRecipeInput(ExtremeCraftShapeRecipe recipe, ItemStack... stacks){
        //只能向以下3个配方追加物品
        if (recipe != ModRecipeManager.infinityCatalyst && recipe != ModRecipeManager.meatBalls && recipe != ModRecipeManager.stew && recipe != ModRecipeManager.eternalSingularity) return;
        List<ItemStack> list = new ArrayList<>(Arrays.asList(stacks));
        if (list.isEmpty()) return;
        if (recipe.getIngredients().size() + list.size() > 81) throw new RuntimeException("recipe size not pass to 81!");
        for (ExtremeCraftShapeRecipe craftRecipe : recipes) {
            if (craftRecipe.hasOutput(recipe.getResultItem())){
                craftRecipe.addInputs(getIngredients(stacks));
            }
        }
    }

    /**
     * 可替换物品
     * @param recipe 配方
     * @param ingredients 物品
     */
    public void addRecipeInput(ExtremeCraftShapeRecipe recipe, Ingredient... ingredients){
        if (recipe != ModRecipeManager.infinityCatalyst && recipe != ModRecipeManager.meatBalls
                && recipe != ModRecipeManager.stew && recipe != ModRecipeManager.eternalSingularity) return;
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(Arrays.asList(ingredients));
        if (list.isEmpty()) return;
        if (recipe.getIngredients().size() + list.size() > 81) throw new RuntimeException("recipe size not pass to 81!");
        for (ExtremeCraftShapeRecipe craftRecipe : recipes) {
            if (craftRecipe.hasOutput(recipe.getResultItem())){
                craftRecipe.addInputs(list);
            }
        }
    }
}
