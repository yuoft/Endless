package com.yuo.endless.Recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static com.yuo.endless.Recipe.ExtremeCraftRecipe.*;

//9*9无序合成
public class ExtremeCraftShapeRecipe implements IExtremeCraftRecipe{

    private final NonNullList<Ingredient> items;
    private final ItemStack result;
    private final ResourceLocation id;

    public ExtremeCraftShapeRecipe(ResourceLocation id, NonNullList<Ingredient> itemsIn, ItemStack result){
        this.id = id;
        this.items = itemsIn;
        this.result = result;
    }

    @Override
    public IRecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOptional(TYPE_SHAPE_ID).get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.items;
    }

    public static class RecipeType implements IRecipeType<ExtremeCraftShapeRecipe> {
        @Override
        public String toString() {
            return ExtremeCraftShapeRecipe.TYPE_SHAPE_ID.toString();
        }
    }
    //配方序列器
    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ExtremeCraftShapeRecipe>{
        @Override
        public ExtremeCraftShapeRecipe read(ResourceLocation recipeId, JsonObject json) { //从json中获取信息
            String s = JSONUtils.getString(json, "type", "");
            if (!"endless:extreme_craft_shape".equals(s))
                throw new JsonParseException("Type error!");
            NonNullList<Ingredient> nonnulllist = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 81) {
                throw new JsonParseException("Too many ingredients for shapeless recipe the max is 81");
            } else {
                ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
                return new ExtremeCraftShapeRecipe(recipeId, nonnulllist, result);
            }
        }

        @Nullable
        @Override
        public ExtremeCraftShapeRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.read(buffer));
            }

            ItemStack result = buffer.readItemStack();
            return new ExtremeCraftShapeRecipe(recipeId, nonnulllist, result);
        }

        @Override
        public void write(PacketBuffer buffer, ExtremeCraftShapeRecipe recipe) {
            buffer.writeInt(recipe.items.size());
            for(Ingredient ingredient : recipe.items) {
                ingredient.write(buffer);
            }
            buffer.writeItemStack(recipe.result);
        }
    }

    @Override
    public boolean matches(IInventory iInventory, World world) {
        ArrayList<ItemStack> stacks = new ArrayList<>(); //输入的物品
        for (int slot = 0; slot < iInventory.getSizeInventory(); slot++) {
            ItemStack stack = iInventory.getStackInSlot(slot);
            if (!stack.isEmpty()) { stacks.add(stack); }
        }

        int num  =  0; //判断物品是否相同
        for (Ingredient item : items) {
            Iterator<ItemStack> iterator = stacks.iterator();
            while (iterator.hasNext()){
                ItemStack stack = iterator.next();
                if (item.test(stack)) {
                    num++;
                    iterator.remove();
                }
            }
        }

//        for (ItemStack stack : stacks) {
//            for (Ingredient item : items) {
//                if (item.test(stack)) num++;
//            }
//        }

        return num == items.size();
    }

    @Override
    public ItemStack getCraftingResult(IInventory iInventory) {
        return this.result.copy();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return this.getSerializer();
    }

    public boolean hasOutput(ItemStack stack){
        return result.isItemEqual(stack);
    }

    //追加输入
    public void addInputs(NonNullList<Ingredient> ingredients){
        this.items.addAll(ingredients);
    }

    private static NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();

        for(int i = 0; i < jsonArray.size(); ++i) {
            JsonElement element = jsonArray.get(i);
            String asString = ((JsonObject) element).get("item").getAsString();
            if ("endless:singularity".equals(asString)){
                ItemStack stack = CraftingHelper.getItemStack((JsonObject) element, true);
                nonnulllist.add(Ingredient.fromStacks(stack));
            }else {
                Ingredient ingredient = CraftingHelper.getIngredient(element);
                if (!ingredient.hasNoMatchingItems()) {
                    nonnulllist.add(ingredient);
                }
            }
        }

        return nonnulllist;
    }
}
