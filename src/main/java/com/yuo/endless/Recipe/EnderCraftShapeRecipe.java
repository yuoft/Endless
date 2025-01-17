package com.yuo.endless.Recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class EnderCraftShapeRecipe implements IExtremeCraftRecipe{

    private final NonNullList<Ingredient> items;
    private final ItemStack result;
    private final ResourceLocation id;

    public EnderCraftShapeRecipe(ResourceLocation id, NonNullList<Ingredient> itemsIn, ItemStack result){
        this.id = id;
        this.items = itemsIn;
        this.result = result;
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeTypeRegistry.ENDER_CRAFT_SHAPE_RECIPE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.items;
    }

    public static class RecipeType implements IRecipeType<EnderCraftShapeRecipe> {
        @Override
        public String toString() {
            return EnderCraftShapeRecipe.ENDER_TYPE_SHAPE_ID.toString();
        }
    }
    //配方序列器
    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<EnderCraftShapeRecipe>{
        @Override
        public EnderCraftShapeRecipe read(ResourceLocation recipeId, JsonObject json) { //从json中获取信息
            NonNullList<Ingredient> nonnulllist = ExtremeCraftShapeRecipe.readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 49) {
                throw new JsonParseException("Too many ingredients for shapeless recipe the max is 49");
            } else {
                ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
                return new EnderCraftShapeRecipe(recipeId, nonnulllist, result);
            }
        }

        @Nullable
        @Override
        public EnderCraftShapeRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.read(buffer));
            }

            ItemStack result = buffer.readItemStack();
            return new EnderCraftShapeRecipe(recipeId, nonnulllist, result);
        }

        @Override
        public void write(PacketBuffer buffer, EnderCraftShapeRecipe recipe) {
            buffer.writeInt(recipe.items.size());
            for(Ingredient ingredient : recipe.items) {
                ingredient.write(buffer);
            }
            buffer.writeItemStack(recipe.result);
        }
    }

    @Override
    public boolean matches(IInventory iInventory, World world) {
        return ExtremeCraftShapeRecipe.recipeMatcher(iInventory, world, this.items);
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
}
