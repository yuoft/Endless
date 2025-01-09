package com.yuo.endless.Recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.yuo.endless.Items.Singularity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class NeutroniumRecipe implements INeutroniumRecipe {

    private final NonNullList<ItemStack> inputs; //可压缩物品 压缩效率 推荐为5的倍数
    private int count; //数量 可能大于64
    private final ItemStack output;
    private final ResourceLocation id;

    public NeutroniumRecipe(ResourceLocation idIn, NonNullList<ItemStack> inputIn, int countIn, ItemStack outputIn){
        this.id = idIn;
        this.inputs = inputIn;
        this.count = countIn;
        this.output = outputIn;
    }
    public static class ModRecipeType implements RecipeType<NeutroniumRecipe> {
        @Override
        public String toString() {
            return NeutroniumRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<NeutroniumRecipe>{

        @Override
        public NeutroniumRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            NonNullList<ItemStack> list = NonNullList.create();
            ItemStack input = deserializeItem(GsonHelper.getAsJsonObject(json, "input"));
            list.add(input);
            int count = GsonHelper.getAsInt(json, "count");
            ItemStack output = deserializeItem(GsonHelper.getAsJsonObject(json, "output"));
            String type = output.getOrCreateTag().getString("type");
            ItemStack singularity = Singularity.getSingularity(type);
            return new NeutroniumRecipe(recipeId, list, count, singularity);
        }

        @org.jetbrains.annotations.Nullable
        @Override
        public NeutroniumRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buffer) {
            NonNullList<ItemStack> list = NonNullList.create();
            int i = buffer.readInt();
            for (int j = 0; j < i;j++)
                list.add(buffer.readItem());
            int count = buffer.readInt();
            ItemStack output = buffer.readItem();
            return new NeutroniumRecipe(resourceLocation, list, count, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, NeutroniumRecipe recipe) {
            buffer.writeInt(recipe.inputs.size());
            for (ItemStack stack : recipe.inputs) {
                buffer.writeItem(stack);
            }

            buffer.writeInt(recipe.count);
            buffer.writeItem(recipe.output);
        }
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        ItemStack itemStack = inv.getItem(0);
        for (ItemStack stack : inputs) {
            if (stack.equals(itemStack, false)) return true;
        }

        return false;
    }

    //输入相同
    public boolean isInput(ItemStack stack){
        for (ItemStack itemStack : inputs) {
            if (ItemStack.isSame(itemStack, stack)) return true;
        }

        return false;
    }

    //输出是否相同
    public boolean hasOutput(ItemStack stack){
        return Singularity.isEqual(output, stack);
    }

    //设置数量
    public void setCount(int count){
        this.count = count;
    }

    //添加输入
    public void addInput(NonNullList<ItemStack> map){
        inputs.addAll(map);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<ItemStack> list = NonNullList.create();
        for (ItemStack input : inputs) {
            list.add(new ItemStack(input.getItem(), count / input.getCount()));
        }

        return NonNullList.of(Ingredient.EMPTY, Ingredient.of(list.stream()));
    }

    public NonNullList<ItemStack> getRecipeInput() {
        return inputs;
    }

    @Override
    public ItemStack getResultItem() {
        return this.output.copy();
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
        return RecipeTypeRegistry.NEUTRONIUM_SERIALIZER.get();
    }

    //获取数量
    public int getRecipeCount(){
        return count;
    }

    //从json中获取物品
    public static ItemStack deserializeItem(JsonObject object) {
        String s = GsonHelper.getAsString(object, "item");
        Item item = Registry.ITEM.getOptional(new ResourceLocation(s)).orElseThrow(
                () -> new JsonSyntaxException("Unknown item '" + s + "'"));
        if (object.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        } else {
            int i = GsonHelper.getAsInt(object, "count", 1);
            return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(object, true);
        }
    }
}
