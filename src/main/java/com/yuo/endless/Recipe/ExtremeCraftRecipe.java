package com.yuo.endless.Recipe;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Items.Singularity;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
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

    public static class ModRecipeType implements RecipeType<ExtremeCraftRecipe> {
        @Override
        public String toString() {
            return ExtremeCraftRecipe.TYPE_ID.toString();
        }
    }

    //配方序列器
    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ExtremeCraftRecipe>{

        @Override
        public ExtremeCraftRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
            Map<String, Ingredient> map = deserializeKey(GsonHelper.getAsJsonObject(json, "key"));
            String[] astring = shrink(patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = deserializeIngredients(astring, map, i, j);
            ItemStack result = deserializeItem(GsonHelper.getAsJsonObject(json, "result"));
            return new ExtremeCraftRecipe(resourceLocation, i, j, nonnulllist, result);
        }

        @org.jetbrains.annotations.Nullable
        @Override
        public ExtremeCraftRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            int j = buffer.readVarInt();
//            String s = buffer.readString(32767); 联机错误2
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);

            nonnulllist.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            ItemStack result = buffer.readItem();
            return new ExtremeCraftRecipe(resourceLocation, i, j, nonnulllist, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ExtremeCraftRecipe recipe) {
            buffer.writeVarInt(recipe.Width);
            buffer.writeVarInt(recipe.Height);

            for(Ingredient ingredient : recipe.items) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItemStack(recipe.result, true);
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
    public boolean matches(Container inv, Level worldIn) {
        for(int i = 0; i <= 9 - this.Width; ++i) {
            for(int j = 0; j <= 9 - this.Height; ++j) {
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

    @Override
    public ItemStack assemble(Container container) {
        return this.getResultItem().copy();
    }

    //无尽配方检测
    public boolean checkRecipe(Container inv, Level worldIn){
        for (int i = 0; i < items.size(); i++){
            if (!items.get(i).test(inv.getItem(i))) return false;
        }
        return true;
    }

    private boolean checkMatch(Container inv, int width, int height, boolean mirrored) {
        for(int i = 0; i < 9; ++i) {
            for(int j = 0; j < 9; ++j) {
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

                if (!ingredient.test(inv.getItem(i + j * 9))) {
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

    public boolean isInput(ItemStack stack){
        for (Ingredient item : this.items) {
            if (item.test(stack)) return true;
        }
        return false;
    }

    //获取合成结果

    @Override
    public ItemStack getResultItem() {
        return this.result.copy();
    }

    //检查给定配方是否适用给定宽高的网格
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.Width && height >= this.Height;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    //判断输出是否相同
    public boolean hasOutput(ItemStack stack){
        return result.equals(stack, false);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.EXTREME_CRAFT_SERIALIZER.get();
    }

    //合成方块图标
    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(EndlessBlocks.extremeCraftingTable.get());
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

        for(Entry<String, JsonElement> entry : json.entrySet()) {
            String key = entry.getKey();
            if (key != null) {
                if (key.length() != 1) {
                    throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
                }
                JsonElement element = ((JsonObject) entry.getValue()).get("item");
                String asString = "";
                if (element != null){
                    asString = element.getAsString();
                }
                if (" ".equals(key)) {
                    throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
                } else if ("endless:singularity".equals(asString)){ //获取奇点
                    ItemStack stack = CraftingHelper.getItemStack((JsonObject) entry.getValue(), true);
                    CompoundTag nbt = stack.getOrCreateTag();
                    map.put(key, Ingredient.of(Singularity.getSingularity(nbt.getString(Singularity.NBT_TYPE))));
                }else map.put(key, CraftingHelper.getIngredient(entry.getValue()));
            }
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
                String s = GsonHelper.convertToString(jsonArr.get(i), "pattern[" + i + "]");
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
//                if (ingredient.test(new ItemStack(EndlessItems.singularity.get()))){
//                    ArrayList<ItemStack> stacks = new ArrayList<>();
//                    for (ItemStack stack : ingredient.getMatchingStacks()) {
//                        if (stack.getItem() instanceof Singularity){
//                            CompoundNBT nbt = stack.getOrCreateTag();
//                            ItemStack singularity = Singularity.getSingularity(nbt.getString("type"));
//                            stacks.add(singularity);
//                        }
//                    }
//                    Ingredient.fromStacks(getStacks(stacks));
//                }else {
                    set.remove(s);
                    nonnulllist.set(j + patternWidth * i, ingredient);
//                }
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonnulllist;
        }
    }

    /**
     * 将列表转为数组
     * @param stacks 列表
     * @return 数组
     */
    private static ItemStack[] getStacks(ArrayList<ItemStack> stacks){
        ItemStack[] astack = new ItemStack[stacks.size()];
        for(int i = 0; i < astack.length; ++i) {
            astack[i] = stacks.get(i);
        }
        return astack;
    }

    public static ItemStack deserializeItem(JsonObject object) {
        return NeutroniumRecipe.deserializeItem(object);
    }
}
