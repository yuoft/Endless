package com.yuo.endless.Recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Iterator;

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
    public RecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOptional(TYPE_SHAPE_ID).get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.items;
    }

    public static class ModRecipeType implements RecipeType<ExtremeCraftShapeRecipe> {
        @Override
        public String toString() {
            return ExtremeCraftShapeRecipe.TYPE_SHAPE_ID.toString();
        }
    }
    //配方序列器
    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ExtremeCraftShapeRecipe>{

        @Override
        public ExtremeCraftShapeRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
            String s = GsonHelper.getAsString(json, "type", "");
            if (!"endless:extreme_craft_shape".equals(s))
                throw new JsonParseException("Type error!");
            NonNullList<Ingredient> nonnulllist = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (nonnulllist.isEmpty()) {
                throw new JsonParseException("No ingredients for shapeless recipe");
            } else if (nonnulllist.size() > 81) {
                throw new JsonParseException("Too many ingredients for shapeless recipe the max is 81");
            } else {
                ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
                return new ExtremeCraftShapeRecipe(resourceLocation, nonnulllist, result);
            }
        }

        @org.jetbrains.annotations.Nullable
        @Override
        public ExtremeCraftShapeRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buffer) {
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);

            nonnulllist.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            ItemStack result = buffer.readItem();
            return new ExtremeCraftShapeRecipe(resourceLocation, nonnulllist, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ExtremeCraftShapeRecipe recipe) {
            buffer.writeInt(recipe.items.size());
            for(Ingredient ingredient : recipe.items) {
                ingredient.toNetwork(buffer);
            }
            buffer.writeItemStack(recipe.result, true);
        }
    }

    @Override
    public boolean matches(Container iInventory, Level world) {
        ArrayList<ItemStack> stacks = new ArrayList<>(); //输入的物品
        for (int slot = 0; slot < iInventory.getContainerSize(); slot++) {
            ItemStack stack = iInventory.getItem(slot);
            if (!stack.isEmpty()) { stacks.add(stack); }
        }

        if (items.size() != stacks.size()) { return false; }

        int num  =  0; //判断物品是否相同
        NonNullList<Ingredient> list = NonNullList.create(); //配方物品
        list.addAll(items);

        Iterator<Ingredient> ingredientIterator = list.iterator();
        while(ingredientIterator.hasNext()) {
            Ingredient ingredient = ingredientIterator.next();
            Iterator<ItemStack> iterator = stacks.iterator();
            while (iterator.hasNext()){
                ItemStack stack = iterator.next();
                if (ingredient.test(stack)) {
                    num++;
                    iterator.remove();
                    break; //如果匹配成功 则退出当前循环
                }
            }
            ingredientIterator.remove();  //移除已匹配项
        }

        return num == items.size();
    }

    @Override
    public ItemStack getResultItem() {
        return this.result.copy();
    }

    @Override
    public ItemStack assemble(Container container) {
        return this.getResultItem().copy();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.EXTREME_CRAFT_SHAPE_SERIALIZER.get();
    }

    public boolean hasOutput(ItemStack stack){
        return result.equals(stack, false);
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
            if ("endless:singularity".equals(asString)){ //获取奇点
                ItemStack stack = CraftingHelper.getItemStack((JsonObject) element, true);
                nonnulllist.add(Ingredient.of(stack));
            }else {
                Ingredient ingredient = CraftingHelper.getIngredient(element);
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }
        }

        return nonnulllist;
    }
}
