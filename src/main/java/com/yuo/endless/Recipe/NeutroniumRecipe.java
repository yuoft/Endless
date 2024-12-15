package com.yuo.endless.Recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.yuo.endless.Items.Singularity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

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
        public NeutroniumRecipe read(ResourceLocation recipeId, JsonObject json) { //从json中获取信息
            NonNullList<ItemStack> list = NonNullList.create();
            ItemStack input = deserializeItem(JSONUtils.getJsonObject(json, "input"));
            list.add(input);
            int count = JSONUtils.getInt(json, "count");
            ItemStack output = deserializeItem(JSONUtils.getJsonObject(json, "output"));
            String type = output.getOrCreateTag().getString("type");
            ItemStack singularity = Singularity.getSingularity(type);
            return new NeutroniumRecipe(recipeId, list, count, singularity);
        }

        @Nullable
        @Override
        public NeutroniumRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<ItemStack> list = NonNullList.create();
            int i = buffer.readInt();
            for (int j = 0; j < i;j++)
                list.add(buffer.readItemStack());
            int count = buffer.readInt();
            ItemStack output = buffer.readItemStack();
            return new NeutroniumRecipe(recipeId, list, count, output);
        }

        @Override
        public void write(PacketBuffer buffer, NeutroniumRecipe recipe) {
            buffer.writeInt(recipe.inputs.size());
            for (ItemStack stack : recipe.inputs) {
                buffer.writeItemStack(stack);
            }

            buffer.writeInt(recipe.count);
            buffer.writeItemStack(recipe.output);
        }
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        ItemStack itemStack = inv.getStackInSlot(0);
        for (ItemStack stack : inputs) {
            if (stack.isItemEqual(itemStack)) return true;
        }

        return false;
    }

    //输入相同
    public boolean isInput(ItemStack stack){
        for (ItemStack itemStack : inputs) {
            if (itemStack.isItemEqual(stack)) return true;
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

        return NonNullList.from(Ingredient.EMPTY, Ingredient.fromStacks(list.stream()));
    }

    public NonNullList<ItemStack> getRecipeInput() {
        return inputs;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.output.copy();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.NEUTRONIUM_SERIALIZER.get();
    }

    //获取数量
    public int getRecipeCount(){
        return count;
    }

    //从json中获取物品
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
