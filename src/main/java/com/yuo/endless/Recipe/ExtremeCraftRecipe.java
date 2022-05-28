package com.yuo.endless.Recipe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.yuo.endless.Blocks.BlockRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
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
import java.util.Map;
import java.util.Set;

public class ExtremeCraftRecipe implements IExtremeCraftRecipe {

    static int MAX_WIDTH = 9;
    static int MAX_HEIGHT = 9;

    private final int Width;
    private final int Height;
    private final NonNullList<Ingredient> items;
    private final ItemStack result;
    private final ResourceLocation id;

    public ExtremeCraftRecipe(ResourceLocation id, int WidthIn, int HeightIn, NonNullList<Ingredient> itemsIn, ItemStack result){
        this.id = id;
        this.Width = WidthIn;
        this.Height = HeightIn;
        this.items = itemsIn;
        this.result = result;
    }

    //追加输入
    public void addInputs(NonNullList<Ingredient> ingredients){
        this.items.addAll(ingredients);
    }

    public static class RecipeType implements IRecipeType<ExtremeCraftRecipe> {
        @Override
        public String toString() {
            return ExtremeCraftRecipe.TYPE_ID.toString();
        }
    }

    //配方序列器
    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ExtremeCraftRecipe>{
        @Override
        public ExtremeCraftRecipe read(ResourceLocation recipeId, JsonObject json) { //从json中获取信息
            Map<String, Ingredient> map = deserializeKey(JSONUtils.getJsonObject(json, "key"));
            String[] astring = shrink(patternFromJson(JSONUtils.getJsonArray(json, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = deserializeIngredients(astring, map, i, j);
            ItemStack result = deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return new ExtremeCraftRecipe(recipeId, i, j, nonnulllist, result);
        }

        @Nullable
        @Override
        public ExtremeCraftRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();
//            String s = buffer.readString(32767); 联机错误2
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

            for(int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.read(buffer));
            }

            ItemStack result = buffer.readItemStack();
            return new ExtremeCraftRecipe(recipeId, i, j, nonnulllist, result);
        }

        @Override
        public void write(PacketBuffer buffer, ExtremeCraftRecipe recipe) {
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
        int size = items.size();
        for (int i = 0; i < size; i++){
            if (!items.get(i).test(inv.getStackInSlot(i))) return false;
        }
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return items;
    }

    public boolean isInput(ItemStack stack){
        for (Ingredient item : this.items) {
            if (item.test(stack)) return true;
        }
        return false;
    }

    //获取合成结果
    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    //检查给定配方是否适用给定宽高的网格
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

    //判断输出是否相同
    public boolean hasOutput(ItemStack stack){
        return result.isItemEqual(stack);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.EXTREME_CRAFT_SERIALIZER.get();
    }

    //合成方块图标
    @Override
    public ItemStack getIcon() {
        return new ItemStack(BlockRegistry.extremeCraftingTable.get());
    }

    //解析json配方方法
    public static String[] shrink(String... toShrink) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for(int i1 = 0; i1 < toShrink.length; ++i1) {
            String s = toShrink[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (toShrink.length == l) {
            return new String[0];
        } else {
            String[] astring = new String[toShrink.length - l - k];

            for(int k1 = 0; k1 < astring.length; ++k1) {
                astring[k1] = toShrink[k1 + k].substring(i, j + 1);
            }

            return astring;
        }
    }

    private static int firstNonSpace(String str) {
        int i;
        for(i = 0; i < str.length() && str.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int lastNonSpace(String str) {
        int i;
        for(i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; --i) {
        }

        return i;
    }

    public static Map<String, Ingredient> deserializeKey(JsonObject json) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for(Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

//            JsonElement value = entry.getValue();
//            JsonObject tag = JSONUtils.getJsonObject(value, "tag");
//            if (tag != null && tag.isJsonNull()){
//                map.put(entry.getKey(), CraftingHelper.getIngredient(value));
//            }

            else map.put(entry.getKey(), CraftingHelper.getIngredient(entry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    public static String[] patternFromJson(JsonArray jsonArr) {
        String[] astring = new String[jsonArr.size()];
        if (astring.length > 9) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for(int i = 0; i < astring.length; ++i) {
                String s = JSONUtils.getString(jsonArr.get(i), "pattern[" + i + "]");
                if (s.length() > 9) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
                }

                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }

    public static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int patternWidth, int patternHeight) {
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(patternWidth * patternHeight, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(keys.keySet());
        set.remove(" ");

        for(int i = 0; i < pattern.length; ++i) {
            for(int j = 0; j < pattern[i].length(); ++j) {
                String s = pattern[i].substring(j, j + 1);
                Ingredient ingredient = keys.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonnulllist.set(j + patternWidth * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonnulllist;
        }
    }

    public static ItemStack deserializeItem(JsonObject object) {
        String s = JSONUtils.getString(object, "item");
        Item item = Registry.ITEM.getOptional(new ResourceLocation(s)).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown item '" + s + "'");
        });
        if (object.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        } else {
            int i = JSONUtils.getInt(object, "count", 1);
            return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(object, true);
        }
    }
}
