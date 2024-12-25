package com.yuo.endless.Compat.Kubejs;

import com.google.gson.JsonArray;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;

public class ExtremeShapeCraftJs extends RecipeJS {

    @Override
    public void create(ListJS listJS) {
        this.outputItems.add(this.parseResultItem(listJS.get(0)));
        this.inputItems.addAll(this.parseIngredientItemList(listJS.get(1)));
    }

    @Override
    public void deserialize() {
        this.outputItems.add(this.parseResultItem(this.json.get("result")));
        this.inputItems.addAll(this.parseIngredientItemList(this.json.get("ingredients")));
    }

    @Override
    public void serialize() {
        if (this.serializeInputs) {
            JsonArray ingredientsJson = new JsonArray();

            for (IngredientJS in : this.inputItems) {

                for (IngredientJS in1 : in.unwrapStackIngredient()) {
                    ingredientsJson.add(in1.toJson());
                }
            }

            this.json.add("ingredients", ingredientsJson);
        }

        if (this.serializeOutputs) {
            this.json.add("result", this.outputItems.get(0).toResultJson());
        }
    }
}
