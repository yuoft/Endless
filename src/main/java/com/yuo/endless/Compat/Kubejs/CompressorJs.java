package com.yuo.endless.Compat.Kubejs;

import com.google.gson.JsonArray;
import com.yuo.endless.Items.Singularity;
import com.yuo.endless.Recipe.CompressorManager;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.kubejs.recipe.ingredientaction.IngredientActionFilter;
import dev.latvian.kubejs.util.ListJS;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;

public class CompressorJs extends RecipeJS {
    private int count;

    @Override
    public void create(ListJS listJS) {
        ItemStackJS resultItem = this.parseResultItem(listJS.get(0));
        List<ItemStackJS> jsList = this.parseResultItemList(listJS.get(1));
        this.outputItems.add(resultItem);
        this.inputItems.addAll(jsList);
        this.count = (int) listJS.get(2);
        CompressorManager.addRecipe(resultItem.getItemStack(), this.count, getInputItems(jsList));
    }

    @Override
    public RecipeJS ingredientAction(IngredientActionFilter filter, IngredientAction action) {
        JsonArray a = (JsonArray)this.json.get("kubejs_actions");
        if (a == null) {
            a = new JsonArray();
            this.json.add("kubejs_actions", a);
        }

        action.copyFrom(filter);
        a.add(action.toJson());
        this.save();
        return this;
    }

    public CompressorJs removeCom(int i){
        if (i == 0){
            CompressorManager.removeRecipe(Singularity.getSingularity("diamond"));
        }
        return this;
    }

    /**
     * 转换列表
     */
    private static NonNullList<ItemStack> getInputItems(List<ItemStackJS> list) {
        NonNullList<ItemStack> inputItems = NonNullList.create();
        for (ItemStackJS js : list) {
            ItemStack stack = js.getItemStack();
            if (stack != null && !stack.isEmpty()) {
                inputItems.add(stack);
            }
        }

        return inputItems;
    }

    @Override
    public void deserialize() {
        this.outputItems.add(this.parseResultItem(this.json.get("output")));
        List<ItemStackJS> inputs = this.parseResultItemList(this.json.get("input"));
        this.inputItems.add(IngredientJS.of(inputs));
        this.count = this.json.get("count").getAsInt();
    }

    @Override
    public void serialize() {
        if (this.serializeOutputs) {
            this.json.add("output", ((ItemStackJS)this.outputItems.get(0)).toResultJson());
        }

        if (this.serializeInputs) {
            this.json.add("input", ((IngredientJS)this.inputItems.get(0)).toJson());
            this.json.addProperty("count", this.count);
        }
    }
}
