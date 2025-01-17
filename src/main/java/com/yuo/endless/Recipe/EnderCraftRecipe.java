package com.yuo.endless.Recipe;

import com.google.gson.JsonObject;
import com.yuo.endless.Blocks.EndlessBlocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Map;

public class EnderCraftRecipe implements IExtremeCraftRecipe {

    static int MAX_WIDTH = 7;
    static int MAX_HEIGHT = 7;

    private final int Width;
    private final int Height;
    private final NonNullList<Ingredient> items;
    private final ItemStack result;
    private final ResourceLocation id;

    public EnderCraftRecipe(ResourceLocation id, int WidthIn, int HeightIn, NonNullList<Ingredient> itemsIn, ItemStack result){
        this.id = id;
        this.Width = WidthIn;
        this.Height = HeightIn;
        this.items = itemsIn;
        this.result = result;
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeTypeRegistry.ENDER_CRAFT_RECIPE;
    }

    public static class RecipeType implements IRecipeType<EnderCraftRecipe> {
        @Override
        public String toString() {
            return EnderCraftRecipe.ENDER_TYPE_ID.toString();
        }
    }

    //配方序列器
    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<EnderCraftRecipe>{
        @Override
        public EnderCraftRecipe read(ResourceLocation recipeId, JsonObject json) { //从json中获取信息
            Map<String, Ingredient> map = ExtremeCraftRecipe.deserializeKey(JSONUtils.getJsonObject(json, "key"));
            String[] astring = ExtremeCraftRecipe.shrink(ExtremeCraftRecipe.patternFromJson(JSONUtils.getJsonArray(json, "pattern"), MAX_HEIGHT, MAX_WIDTH));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = ExtremeCraftRecipe.deserializeIngredients(astring, map, i, j);
            ItemStack result = ExtremeCraftRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return new EnderCraftRecipe(recipeId, i, j, nonnulllist, result);
        }

        @Nullable
        @Override
        public EnderCraftRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

            nonnulllist.replaceAll(ignored -> Ingredient.read(buffer));

            ItemStack result = buffer.readItemStack();
            return new EnderCraftRecipe(recipeId, i, j, nonnulllist, result);
        }

        @Override
        public void write(PacketBuffer buffer, EnderCraftRecipe recipe) {
            buffer.writeVarInt(recipe.Width);
            buffer.writeVarInt(recipe.Height);

            for(Ingredient ingredient : recipe.items) {
                ingredient.write(buffer);
            }

            buffer.writeItemStack(recipe.result);
        }
    }

    public int getWidth() {
        return Width;
    }

    public int getHeight() {
        return Height;
    }

    //检查配方是否与合成台物品栏吻合
    @Override
    public boolean matches(IInventory inv, World worldIn) {
        for(int i = 0; i <= MAX_WIDTH - this.Width; ++i) {
            for(int j = 0; j <= MAX_HEIGHT - this.Height; ++j) {
                if (this.checkMatch(inv, i, j, true)) {
                    return true;
                }

                if (this.checkMatch(inv, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    //无尽配方检测
    public boolean checkRecipe(IInventory inv, World worldIn){
        for (int i = 0; i < items.size(); i++){
            if (!items.get(i).test(inv.getStackInSlot(i))) return false;
        }
        return true;
    }

    private boolean checkMatch(IInventory inv, int width, int height, boolean mirrored) {
        for(int i = 0; i < 7; ++i) {
            for(int j = 0; j < 7; ++j) {
                int k = i - width;
                int l = j - height;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.Width && l < this.Height) {
                    if (mirrored) {
                        ingredient = this.items.get(this.Width - k - 1 + l * this.Width);
                    } else {
                        ingredient = this.items.get(k + l * this.Width);
                    }
                }

                if (!ingredient.test(inv.getStackInSlot(i + j * MAX_WIDTH))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return items;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= this.Width && height >= this.Height;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public boolean hasOutput(ItemStack stack){
        return result.isItemEqual(stack);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.ENDER_CRAFT_SERIALIZER.get();
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(EndlessBlocks.enderCraftingTable.get());
    }
}
