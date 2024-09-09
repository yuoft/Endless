package com.yuo.endless.Recipe;

import com.yuo.endless.Container.ExtremeCraftInventory;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.*;

public class ExtremeCraftingManager {
    //实例
    private static final ExtremeCraftingManager instance = new ExtremeCraftingManager();
    //配方列表
    private final List<ExtremeCraftRecipe> recipes = new ArrayList<>();

    //获取实例
    public static ExtremeCraftingManager getInstance() {
        return instance;
    }

    /**
     * 添加单个配方 如已有相同配方则不添加
     * @param recipe 需要添加的配方
     */
    public void addRecipe(ExtremeCraftRecipe recipe){
        Iterator<ExtremeCraftRecipe> iterator = this.recipes.iterator();
        while (iterator.hasNext()){
            ExtremeCraftRecipe next = iterator.next();
            if (next.hasOutput(recipe.getRecipeOutput()) && next.getIngredients().containsAll(recipe.getIngredients())){
                iterator.remove();
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
        Iterator<ExtremeCraftRecipe> iterator = this.recipes.iterator();
        while (iterator.hasNext()){
            ExtremeCraftRecipe recipe = iterator.next();
            if (recipe.hasOutput(output)){
                iterator.remove();
                return;
            }
        }

    }

    //列表添加 同输出替换
    void addRecipes(List<ExtremeCraftRecipe> recipeList){
        Iterator<ExtremeCraftRecipe> iterator = this.recipes.iterator();
        while (iterator.hasNext()){
            ExtremeCraftRecipe recipe = iterator.next();
            for (ExtremeCraftRecipe craftRecipe : recipeList) {
                if (recipe.hasOutput(craftRecipe.getRecipeOutput())){
                    iterator.remove();
                }
            }
        }

        this.recipes.addAll(recipeList);
    }

    /**
     * 通过json配方添加
     * @param id 配方id
     * @param width 配方宽
     * @param height 高
     * @param result 输出
     * @param ingredients 输入列表
     */
    void addRecipe(ResourceLocation id, int width, int height, ItemStack result, NonNullList<Ingredient> ingredients){
        boolean flag = true;
        Iterator<ExtremeCraftRecipe> iterator = this.recipes.iterator();
        while (iterator.hasNext()){
            ExtremeCraftRecipe next = iterator.next();
            //输出与输入相同
            if (next.hasOutput(result) && next.getIngredients().containsAll(ingredients)){
                iterator.remove();
                recipes.add(new ExtremeCraftRecipe(id, width, height, ingredients, result));
                flag = false;
                break;
            }
        }
        if (flag)
            this.recipes.add(new ExtremeCraftRecipe(id, width, height, ingredients, result));
    }

    /**
     * 按无尽工作台配方格式解释添加的配方 并将其添加到集合
     * @param result 合成输出物品
     * @param recipe 输入
     */
    public void addRecipe(ItemStack result, Object... recipe) {
        StringBuilder str = new StringBuilder();
        int i = 0;
        int width = 0;
        int height = 0;

        if (recipe[i] instanceof String[]) {
            String[] astring = (String[]) recipe[i++];

            for (String s1 : astring) {
                ++height;
                width = s1.length();
                str.append(s1);
            }
        } else {
            while (recipe[i] instanceof String) {
                String s2 = (String) recipe[i++];
                ++height;
                width = s2.length();
                str.append(s2);
            }
        }

        Map<Character, Ingredient> hashmap = new HashMap<>();

        for (; i < recipe.length; i += 2) {
            Character character = (Character) recipe[i];
            Ingredient ingredient;

            if (recipe[i + 1] instanceof Item) {
                ingredient = Ingredient.fromStacks(new ItemStack((Item) recipe[i + 1]));
            } else if (recipe[i + 1] instanceof Block) {
                ingredient = Ingredient.fromStacks(new ItemStack((Block) recipe[i + 1]));
            } else if (recipe[i + 1] instanceof ItemStack) {
                ingredient = Ingredient.fromStacks((ItemStack) recipe[i + 1]);
            } else if (result.getItem() == EndlessItems.infinityBow.get()){
                ingredient = Ingredient.fromStacks(getStackList(0).stream());
            } else if (result.getItem() == EndlessItems.skullfireSword.get()){
                ingredient = Ingredient.fromStacks(getStackList(1).stream());
            } else ingredient = Ingredient.EMPTY;

            hashmap.put(character, ingredient);
        }

        NonNullList<Ingredient> ingredients = NonNullList.create();

        char[] s = str.toString().toCharArray();

        for (int i1 = 0; i1 < width * height; ++i1) {
            char c0 = s[i1];
            for (Character character : hashmap.keySet()) {
                if (c0 == character){
                    ingredients.add(hashmap.get(character));
                }
            }

            if (c0 == ' ')
                ingredients.add(Ingredient.EMPTY);
        }

        ExtremeCraftRecipe shapedrecipes = new ExtremeCraftRecipe(result.getItem().getRegistryName(), width, height, ingredients, result);
        this.recipes.add(shapedrecipes);
    }

    /**
     * 同上 无键值对应 只有物品
     * @param result 合成输出
     * @param ingredients 物品列表
     * @return 无尽配方
     */
    ExtremeCraftRecipe addShapelessRecipe(ItemStack result, Object ... ingredients) {
        List<ItemStack> arraylist = new ArrayList<>();

        for (Object object1 : ingredients) {
            if (object1 instanceof ItemStack) {
                arraylist.add(((ItemStack) object1).copy());
            } else if (object1 instanceof Item) {
                arraylist.add(new ItemStack((Item) object1));
            } else {
                if (!(object1 instanceof Block)) {
                    throw new RuntimeException("Invalid shapeless recipy!");
                }
                arraylist.add(new ItemStack((Block) object1));
            }
        }

        ExtremeCraftRecipe recipe = new ExtremeCraftRecipe(result.getItem().getRegistryName(), Math.min(arraylist.size(), 9), (int) Math.ceil(arraylist.size() / 9d), getList(arraylist), result);
        this.recipes.add(recipe);
        return recipe;
    }

    /**
     * 为部分合成添加 合成物品替换
     * @param i 配方标识
     * @return 物品列表
     */
    private List<ItemStack> getStackList(int i) {
        List<ItemStack> stacks = new ArrayList<>();
        if (i == 0){
            stacks.add(new ItemStack(Items.BLACK_WOOL));
            stacks.add(new ItemStack(Items.WHITE_WOOL));
            stacks.add(new ItemStack(Items.BLUE_WOOL));
            stacks.add(new ItemStack(Items.BROWN_WOOL));
            stacks.add(new ItemStack(Items.CYAN_WOOL));
            stacks.add(new ItemStack(Items.GRAY_WOOL));
            stacks.add(new ItemStack(Items.GREEN_WOOL));
            stacks.add(new ItemStack(Items.LIGHT_BLUE_WOOL));
            stacks.add(new ItemStack(Items.LIGHT_GRAY_WOOL));
            stacks.add(new ItemStack(Items.LIME_WOOL));
            stacks.add(new ItemStack(Items.MAGENTA_WOOL));
            stacks.add(new ItemStack(Items.ORANGE_WOOL));
            stacks.add(new ItemStack(Items.PINK_WOOL));
            stacks.add(new ItemStack(Items.PURPLE_WOOL));
            stacks.add(new ItemStack(Items.RED_WOOL));
            stacks.add(new ItemStack(Items.YELLOW_WOOL));
        }else if (i == 1){
            stacks.add(new ItemStack(Blocks.ACACIA_LOG));
            stacks.add(new ItemStack(Blocks.BIRCH_LOG));
            stacks.add(new ItemStack(Blocks.DARK_OAK_LOG));
            stacks.add(new ItemStack(Blocks.JUNGLE_LOG));
            stacks.add(new ItemStack(Blocks.OAK_LOG));
            stacks.add(new ItemStack(Blocks.SPRUCE_LOG));
            stacks.add(new ItemStack(Blocks.CRIMSON_STEM));
            stacks.add(new ItemStack(Blocks.WARPED_STEM));
        }
        return stacks;
    }

    /**
     * 将List<ItemStack>转为NonNullList<Ingredient>
     * @param arrayList 输入物品集合
     * @return 新集合
     */
    private NonNullList<Ingredient> getList(List<ItemStack> arrayList){
        NonNullList<Ingredient> ingredients = NonNullList.create();
        for (ItemStack stack : arrayList) {
            ingredients.add(Ingredient.fromStacks(stack));
        }
        return ingredients;
    }

    /**
     * 获取所有配方
     * @return 配方列表
     */
    public List<ExtremeCraftRecipe> getRecipeList() {
        return this.recipes;
    }

    /**
     * 根据输入获取输出 无则返回空
     * @param inventory 输入物品栏
     * @param world 世界
     * @return 输出
     */
    public ItemStack getRecipeOutPut(ExtremeCraftInventory inventory, World world){
        for (ExtremeCraftRecipe recipe : this.recipes) {
            if (recipe.checkRecipe(inventory, world)){
                return recipe.getRecipeOutput();
            }
        }

        return ItemStack.EMPTY;
    }

    /**
     * 获取合成后的剩余容器 eg：水桶 -> 桶
     * @param inventory 输入容器
     * @param world 世界
     * @return 物品列表
     */
    public NonNullList<ItemStack> getRecipeShirkItem(ExtremeCraftInventory inventory, World world){
        for (ExtremeCraftRecipe recipe : this.recipes) {
            if (recipe.checkRecipe(inventory, world)){
                return recipe.getRemainingItems(inventory);
            }
        }

        return NonNullList.withSize(inventory.getSizeInventory(), ItemStack.EMPTY);
    }

    /**
     * 判断物品是否在 配方输入中
     * @param stack 要判断的物品
     * @return 结果
     */
    public boolean isRecipeInput(ItemStack stack){
        for (ExtremeCraftRecipe recipe : this.recipes) {
            if (recipe.isInput(stack)) return true;
        }
        return false;
    }

}
