package com.yuo.endless.Compat.Kubejs;

import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;

import java.util.List;

public class CompressorJs extends RecipeJS {
    private int count;

    @Override
    public void create(ListJS listJS) {
        this.outputItems.add(this.parseResultItem(listJS.get(0)));
        this.inputItems.addAll(this.parseResultItemList(listJS.get(1)));
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA+ "+listJS.get(1));
        this.count = (int) listJS.get(2);

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
