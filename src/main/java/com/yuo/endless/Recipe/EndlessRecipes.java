package com.yuo.endless.Recipe;

import com.yuo.endless.Endless;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EndlessRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Endless.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Endless.MOD_ID);

    public static final RegistryObject<ExtremeCraftRecipe.Serializer> EXTREME_CRAFT_SERIALIZER = RECIPE_TYPES.register("extreme_craft",
            ExtremeCraftRecipe.Serializer::new);
    public static final RegistryObject<ExtremeCraftShapeRecipe.Serializer> EXTREME_CRAFT_SHAPE_SERIALIZER = RECIPE_TYPES.register("extreme_craft_shape",
            ExtremeCraftShapeRecipe.Serializer::new);
    public static final RegistryObject<NeutroniumRecipe.Serializer> NEUTRONIUM_SERIALIZER = RECIPE_TYPES.register("neutronium",
            NeutroniumRecipe.Serializer::new);

    public static final RegistryObject<RecipeType<ExtremeCraftRecipe>> EXTREME_CRAFT_RECIPE = RECIPES.register(IExtremeCraftRecipe.TYPE_ID.getPath(),
            () -> RecipeType.simple(IExtremeCraftRecipe.TYPE_ID));
    public static final RegistryObject<RecipeType<ExtremeCraftShapeRecipe>> EXTREME_CRAFT_SHAPE_RECIPE = RECIPES.register(IExtremeCraftRecipe.TYPE_SHAPE_ID.getPath(),
            () -> RecipeType.simple(IExtremeCraftRecipe.TYPE_SHAPE_ID));
    public static final RegistryObject<RecipeType<NeutroniumRecipe>> NEUTRONIUM_RECIPE = RECIPES.register(NeutroniumRecipe.TYPE_ID.getPath(),
            () -> RecipeType.simple(NeutroniumRecipe.TYPE_ID));

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPES.register(eventBus);
    }
}
