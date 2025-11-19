package com.yuo.endless.Recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.yuo.endless.Items.Singularity;
import com.yuo.endless.RlUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;

public class NeutroniumRecipe implements INeutroniumRecipe {

    private final Ingredient input; //可压缩物品
    private int count; //数量 可能大于64
    private final ItemStack output;
    private final ResourceLocation id;

    public NeutroniumRecipe(ResourceLocation idIn, Ingredient inputIn, int countIn, ItemStack outputIn){
        this.id = idIn;
        this.input = inputIn;
        this.count = countIn;
        this.output = outputIn;
    }

    public static class Serializer implements RecipeSerializer<NeutroniumRecipe>{

        @Override
        public NeutroniumRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient list;
            JsonObject object = GsonHelper.getAsJsonObject(json, "input");
            if (object.has("tag")) {
                list = Ingredient.of(ItemTags.create(RlUtils.parse(GsonHelper.getAsString(object, "tag"))));
            }else {
                list = Ingredient.of(deserializeItem(object));
            }
            int count = GsonHelper.getAsInt(json, "count");
            ItemStack output = deserializeItem(GsonHelper.getAsJsonObject(json, "output"));
            String type = output.getOrCreateTag().getString("type");
            ItemStack singularity = Singularity.getSingularity(type);
            return new NeutroniumRecipe(recipeId, list, count, singularity);
        }

        @org.jetbrains.annotations.Nullable
        @Override
        public NeutroniumRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            int count = buffer.readInt();
            ItemStack output = buffer.readItem();
            return new NeutroniumRecipe(resourceLocation, input, count, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, NeutroniumRecipe recipe) {
            recipe.input.toNetwork(buffer);

            buffer.writeInt(recipe.count);
            buffer.writeItem(recipe.output);
        }
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        ItemStack itemStack = inv.getItem(0);
        return input.test(itemStack);
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return this.getResultItem(registryAccess).copy();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.output.copy();
    }

    public ItemStack getResultItem() {
        return this.output.copy();
    }

    public static ItemStack getOutput(Level level, ItemStack stack){
        for (NeutroniumRecipe recipe : level.getRecipeManager().getAllRecipesFor(EndlessRecipes.NEUTRONIUM_RECIPE.get())) {
            if (recipe.isInput(stack)) return recipe.getResultItem();
        }

        return ItemStack.EMPTY;
    }

    //输入相同
    public boolean isInput(ItemStack stack){
        return input.test(stack);
    }

    //输出是否相同
    public boolean hasOutput(ItemStack stack){
        return Singularity.isEqual(output, stack);
    }

    //设置数量
    public void setCount(int count){
        this.count = count;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(this.input);
    }

    public Ingredient getInput(){
        return this.input;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return EndlessRecipes.NEUTRONIUM_SERIALIZER.get();
    }

    //获取数量
    public int getRecipeCount(){
        return this.count;
    }

    //从json中获取物品
    public static ItemStack deserializeItem(JsonObject object) {
        String s = GsonHelper.getAsString(object, "item");
        Item item = BuiltInRegistries.ITEM.getOptional(RlUtils.parse(s)).orElseThrow(
                () -> new JsonSyntaxException("Unknown item '" + s + "'"));
        if (object.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        } else {
            int i = GsonHelper.getAsInt(object, "count", 1);
            return CraftingHelper.getItemStack(object, true);
        }
    }

    public static ItemStack itemStackFromJson(JsonObject object) {
        return CraftingHelper.getItemStack(object, true, true);
    }
}
