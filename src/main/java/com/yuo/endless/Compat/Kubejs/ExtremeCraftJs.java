package com.yuo.endless.Compat.Kubejs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.item.ingredient.IngredientJS;
import dev.latvian.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;
import dev.latvian.kubejs.util.MapJS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class ExtremeCraftJs extends RecipeJS {

    private final List<String> pattern = new ArrayList<>();
    private final List<String> key = new ArrayList<>();

    @Override
    public void create(ListJS listJS) {
        ListJS pattern0;
        Iterator var8;
        outputItems.add(parseResultItem(listJS.get(0))); //输出
        pattern0 = ListJS.orSelf(listJS.get(1)); //配方组[]
        if(pattern0.isEmpty()) throw new RecipeExceptionJS("Pattern is empty");
        else {
            List<String> args = new ArrayList<>(1);
            MapJS mapJS = MapJS.of(listJS.get(2)); //键值key{}
            if (mapJS != null && !mapJS.isEmpty()) {
                Iterator iterator = mapJS.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    Object o = mapJS.get(key);
                    if (o != ItemStackJS.EMPTY/* && !o.equals("minecraft:air")*/) {
                        inputItems.add(parseIngredientItem(o, key));
                    } else args.add(key);
                }

                iterator = pattern0.iterator();
                while (iterator.hasNext()) {
                    String pattern = String.valueOf(iterator.next());
                    String s = "";
                    for (var8 = args.iterator(); var8.hasNext(); s = pattern.replace(s, " ")) {
                        s = (String) var8.next();
                    }

                    this.pattern.add(s);
                }

            }
        }
    }

    @Override
    public void deserialize() {
        this.outputItems.add(this.parseResultItem(this.json.get("result")));
        Iterator var1 = this.json.get("pattern").getAsJsonArray().iterator();

        while(var1.hasNext()) {
            JsonElement e = (JsonElement)var1.next();
            this.pattern.add(e.getAsString());
        }

        var1 = this.json.get("key").getAsJsonObject().entrySet().iterator();

        while(var1.hasNext()) {
            Entry entry = (Entry)var1.next();
            this.inputItems.add(this.parseIngredientItem(entry.getValue(), (String)entry.getKey()));
            this.key.add((String)entry.getKey());
        }
    }

    @Override
    public void serialize() {
        if (this.serializeOutputs) {
            this.json.add("result", ((ItemStackJS)this.outputItems.get(0)).toResultJson());
        }

        if (this.serializeInputs) {
            JsonArray patternJson = new JsonArray();

            for (String s : this.pattern) {
                patternJson.add(s);
            }

            this.json.add("pattern", patternJson);
            JsonObject keyJson = new JsonObject();

            for(int i = 0; i < this.key.size(); ++i) {
                keyJson.add((String)this.key.get(i), ((IngredientJS)this.inputItems.get(i)).toJson());
            }

            this.json.add("key", keyJson);
        }
    }
}
