package com.yuo.endless.Recipe;

import com.yuo.endless.Endless;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeTypeRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Endless.MOD_ID);

    public static final RegistryObject<SimpleRecipeSerializer> EXTREME_CRAFT_SERIALIZER = RECIPE_TYPES.register("extreme_craft", ExtremeCraftRecipe.Serializer::new);
    public static final RegistryObject<ExtremeCraftShapeRecipe.Serializer> EXTREME_CRAFT_SHAPE_SERIALIZER = RECIPE_TYPES.register("extreme_craft_shape", ExtremeCraftShapeRecipe.Serializer::new);
    public static final RegistryObject<NeutroniumRecipe.Serializer> NEUTRONIUM_SERIALIZER = RECIPE_TYPES.register("neutronium", NeutroniumRecipe.Serializer::new);

    public static RecipeType<ExtremeCraftRecipe> EXTREME_CRAFT_RECIPE = new ExtremeCraftRecipe.ModRecipeType();
    public static RecipeType<ExtremeCraftShapeRecipe> EXTREME_CRAFT_SHAPE_RECIPE = new ExtremeCraftShapeRecipe.ModRecipeType();
    public static RecipeType<NeutroniumRecipe> NEUTRONIUM_RECIPE = new NeutroniumRecipe.ModRecipeType();


    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, ExtremeCraftRecipe.TYPE_ID, EXTREME_CRAFT_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, ExtremeCraftShapeRecipe.TYPE_SHAPE_ID, EXTREME_CRAFT_SHAPE_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, NeutroniumRecipe.TYPE_ID, NEUTRONIUM_RECIPE);
    }
}
