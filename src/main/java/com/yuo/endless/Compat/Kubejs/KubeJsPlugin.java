package com.yuo.endless.Compat.Kubejs;

import com.yuo.endless.Recipe.ExtremeCraftRecipe;
import com.yuo.endless.Recipe.ExtremeCraftShapeRecipe;
import com.yuo.endless.Recipe.NeutroniumRecipe;
import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;

public class KubeJsPlugin extends KubeJSPlugin {
    @Override
    public void init() {

    }

    @Override
    public void addRecipes(RegisterRecipeHandlersEvent event) {
        // 用于基于成形配方的自定义配方类型，如非镜像或复制NBT
//        event.registerShaped(new ResourceLocation(Endless.MOD_ID, "endless:extreme_craft"));

        // 这是您通常希望用于自定义机器配方类型等的方法
        event.register(ExtremeCraftRecipe.TYPE_ID, ExtremeCraftJs::new);
        event.register(ExtremeCraftShapeRecipe.TYPE_ID, ExtremeShapeCraftJs::new);
        event.register(NeutroniumRecipe.TYPE_ID, CompressorJs::new);
    }

}
