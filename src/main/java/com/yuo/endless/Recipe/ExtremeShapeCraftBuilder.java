package com.yuo.endless.Recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Singularity;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class ExtremeShapeCraftBuilder  extends CraftingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final ItemStack result;
    private final int count;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancement = Builder.recipeAdvancement();
    @javax.annotation.Nullable
    private String group;

    public ExtremeShapeCraftBuilder(RecipeCategory category, ItemStack like, int i) {
        this.category = category;
        this.result = like;
        this.count = i;
    }

    public static ExtremeShapeCraftBuilder shapeless(RecipeCategory category, Item itemLike) {
        return new ExtremeShapeCraftBuilder(category, new ItemStack(itemLike), 1);
    }

    /**
     * 终极工作台无序配方
     * @param category 配方类别
     * @param like 合成物品
     * @param i 合成数量
     */
    public static ExtremeShapeCraftBuilder shapeless(RecipeCategory category, Item like, int i) {
        return new ExtremeShapeCraftBuilder(category, new ItemStack(like), i);
    }

    public ExtremeShapeCraftBuilder requires(TagKey<Item> tagKey) {
        return this.requires(Ingredient.of(tagKey));
    }

    public ExtremeShapeCraftBuilder requires(ItemStack itemLike) {
        if (itemLike == null) return this;
        return this.requires(itemLike, 1);
    }

    /**
     * 添加合成所需物品
     * @param itemLike 物品
     */
    public ExtremeShapeCraftBuilder requires(Item itemLike) {
        if (itemLike == null) return this;
        return this.requires(new ItemStack(itemLike), 1);
    }

    public ExtremeShapeCraftBuilder requires(ItemStack itemLike, int i) {
        for(int v = 0; v < i; ++v) {
            this.requires(Ingredient.of(itemLike));
        }

        return this;
    }

    /**
     * 添加合成所需物品
     * @param ingredient 物品
     */
    public ExtremeShapeCraftBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    public ExtremeShapeCraftBuilder requires(Ingredient ingredient, int i) {
        for(int $$2 = 0; $$2 < i; ++$$2) {
            this.ingredients.add(ingredient);
        }

        return this;
    }

    /**
     * 添加合成所需物品列表
     * @param ingredients 列表
     */
    public ExtremeShapeCraftBuilder requires(List<Ingredient> ingredients) {
        this.ingredients.addAll(ingredients);
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String s, CriterionTriggerInstance criterionTriggerInstance) {
        this.advancement.addCriterion(s, criterionTriggerInstance);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String s) {
        this.group = s;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation res) {
        this.ensureValid(res);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(res)).rewards(AdvancementRewards.Builder.recipe(res)).requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(res, this.result.getItem(), this.count, this.group == null ? "" : this.group, determineBookCategory(this.category), this.ingredients, this.advancement, res.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation res) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + res);
        }
    }

    public static class Result extends CraftingRecipeBuilder.CraftingResult {
        private final ResourceLocation id;
        private final Item result;
        private final int count;
        private final String group;
        private final List<Ingredient> ingredients;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation location, Item item, int i, String s, CraftingBookCategory bookCategory, List<Ingredient> ingredients, Advancement.Builder builder, ResourceLocation location1) {
            super(bookCategory);
            this.id = location;
            this.result = item;
            this.count = i;
            this.group = s;
            this.ingredients = ingredients;
            this.advancement = builder;
            this.advancementId = location1;
        }

        public void serializeRecipeData(@NotNull JsonObject jsonObject) {
            super.serializeRecipeData(jsonObject);
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }

            JsonArray jsonArray = new JsonArray();

            for (Ingredient ingredient : this.ingredients) {
                if (test(ingredient, new ItemStack(EndlessItems.singularity.get()))){
                    ItemStack stack = ingredient.getItems()[0];
                    if (!stack.isEmpty()) {
                        JsonObject obj = new JsonObject();
                        obj.addProperty("item", Endless.MOD_ID + ":" + stack.getItem());
                        obj.addProperty("nbt", "{type:"+ stack.getOrCreateTag().getCompound(Singularity.NBT_MOD).getString(Singularity.NBT_TYPE) + "}");
                        jsonArray.add(obj);
                    }
                }
                else jsonArray.add(ingredient.toJson());
            }

            jsonObject.add("ingredients", jsonArray);
            JsonObject object = new JsonObject();
            object.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            if (this.count > 1) {
                object.addProperty("count", this.count);
            }

            jsonObject.add("result", object);
        }

        public RecipeSerializer<?> getType() {
            return EndlessRecipes.EXTREME_CRAFT_SHAPE_SERIALIZER.get();
        }

        public ResourceLocation getId() {
            return this.id;
        }

        @javax.annotation.Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @javax.annotation.Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }

    /**
     * 测试Ingredient是否含有相同物品
     * @param ingredient ig
     * @param stack 测试物品
     * @return 是 true
     */
    public static boolean test(Ingredient ingredient, ItemStack stack){
        if (stack == null) {
            return false;
        } else {
            ingredient.checkInvalidation();
            if (ingredient.getItems().length == 0) {
                return stack.isEmpty();
            } else {
                for(ItemStack itemstack : ingredient.getItems()) {
                    if (itemstack.getItem() == stack.getItem()) {
                        return true;
                    }
                }

                return false;
            }
        }
    }
}
