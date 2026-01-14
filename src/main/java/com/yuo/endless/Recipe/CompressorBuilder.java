package com.yuo.endless.Recipe;

import com.google.gson.JsonObject;
import com.yuo.endless.EndlessUtils;
import net.minecraft.advancements.Advancement.Builder;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CompressorBuilder extends CraftingRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final int resultCount;
    private final String type;
    private int inputCount;
    private final Ingredient ingredient;
    private final Builder advancement = Builder.recipeAdvancement();
    @javax.annotation.Nullable
    private String group;

    public CompressorBuilder(RecipeCategory category, ItemLike like, String type, Ingredient ingredient, int i, int inputCount) {
        this.category = category;
        this.result = like.asItem();
        this.resultCount = i;
        this.type = type;
        this.ingredient = ingredient;
        this.inputCount = inputCount;
    }

    public static CompressorBuilder shapeless(RecipeCategory category, Item itemLike, String type, Ingredient ingredient) {
        return new CompressorBuilder(category, itemLike, type, ingredient,1, 1);
    }

    /**
     * 压缩机压缩配方
     * @param category 配方类别
     * @param like 奇点
     * @param type 奇点type
     * @param ingredient 输入物品
     * @param inputCount 合成所需数量
     */
    public static CompressorBuilder shapeless(RecipeCategory category, Item like, String type, Ingredient ingredient, int inputCount) {
        return new CompressorBuilder(category, like, type, ingredient, 1, inputCount);
    }

    public static CompressorBuilder shapeless(RecipeCategory category, Item like, String type, Ingredient ingredient, int i, int inputCount) {
        return new CompressorBuilder(category, like, type, ingredient, i, inputCount);
    }

    public CompressorBuilder requires(int count) {
        this.inputCount = count;
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
        return this.result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation res) {
        this.ensureValid(res);
        String path = res.getPath() + "_" + type;
        ResourceLocation location = EndlessUtils.fa(path);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(AdvancementRewards.Builder.recipe(location)).requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(location, this.result, this.type, this.resultCount, this.inputCount, this.group == null ? "" : this.group, determineBookCategory(this.category), this.ingredient, this.advancement, location.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private void ensureValid(ResourceLocation res) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + res);
        }
    }

    public static class Result extends CraftingResult {
        private final ResourceLocation id;
        private final Item result;
        private final String type;
        private final int resultCount;
        private final int inputCount;
        private final String group;
        private final Ingredient ingredient;
        private final Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation location, Item item, String type, int i, int inputCount, String s, CraftingBookCategory bookCategory, Ingredient ingredient, Builder builder, ResourceLocation location1) {
            super(bookCategory);
            this.id = location;
            this.result = item;
            this.type = type;
            this.resultCount = i;
            this.inputCount = inputCount;
            this.group = s;
            this.ingredient = ingredient;
            this.advancement = builder;
            this.advancementId = location1;
        }

        public void serializeRecipeData(JsonObject jsonObject) {
            super.serializeRecipeData(jsonObject);
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }

            jsonObject.add("input", this.ingredient.toJson());
            jsonObject.addProperty("count", this.inputCount);
            JsonObject object = new JsonObject();
            object.addProperty("item", BuiltInRegistries.ITEM.getKey(this.result).toString());
            if (this.resultCount > 1) {
                object.addProperty("count", this.resultCount);
            }
            object.addProperty("nbt", "{type:"+ this.type + "}");
            jsonObject.add("output", object);
        }

        public RecipeSerializer<?> getType() {
            return EndlessRecipes.NEUTRONIUM_SERIALIZER.get();
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
}
