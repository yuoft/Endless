package com.yuo.endless.Recipe;

import com.yuo.endless.Endless;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeTypeRegistry {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Endless.MOD_ID);

    public static final RegistryObject<ExtremeCraftRecipe.Serializer> EXTREME_CRAFT_SERIALIZER =
            RECIPE_TYPES.register("extreme_craft", ExtremeCraftRecipe.Serializer::new);
    public static final RegistryObject<EnderCraftRecipe.Serializer> ENDER_CRAFT_SERIALIZER =
            RECIPE_TYPES.register("ender_craft", EnderCraftRecipe.Serializer::new);
    public static final RegistryObject<NetherCraftRecipe.Serializer> NETHER_CRAFT_SERIALIZER =
            RECIPE_TYPES.register("nether_craft", NetherCraftRecipe.Serializer::new);
    public static final RegistryObject<ExtremeCraftShapeRecipe.Serializer> EXTREME_CRAFT_SHAPE_SERIALIZER =
            RECIPE_TYPES.register("extreme_craft_shape", ExtremeCraftShapeRecipe.Serializer::new);
    public static final RegistryObject<EnderCraftShapeRecipe.Serializer> ENDER_CRAFT_SHAPE_SERIALIZER =
            RECIPE_TYPES.register("ender_craft_shape", EnderCraftShapeRecipe.Serializer::new);
    public static final RegistryObject<NetherCraftShapeRecipe.Serializer> NETHER_CRAFT_SHAPE_SERIALIZER =
            RECIPE_TYPES.register("nether_craft_shape", NetherCraftShapeRecipe.Serializer::new);
    public static final RegistryObject<NeutroniumRecipe.Serializer> NEUTRONIUM_SERIALIZER =
            RECIPE_TYPES.register("neutronium", NeutroniumRecipe.Serializer::new);

    public static IRecipeType<ExtremeCraftRecipe> EXTREME_CRAFT_RECIPE = new ExtremeCraftRecipe.RecipeType();
    public static IRecipeType<EnderCraftRecipe> ENDER_CRAFT_RECIPE = new EnderCraftRecipe.RecipeType();
    public static IRecipeType<NetherCraftRecipe> NETHER_CRAFT_RECIPE = new NetherCraftRecipe.RecipeType();
    public static IRecipeType<ExtremeCraftShapeRecipe> EXTREME_CRAFT_SHAPE_RECIPE = new ExtremeCraftShapeRecipe.RecipeType();
    public static IRecipeType<EnderCraftShapeRecipe> ENDER_CRAFT_SHAPE_RECIPE = new EnderCraftShapeRecipe.RecipeType();
    public static IRecipeType<NetherCraftShapeRecipe> NETHER_CRAFT_SHAPE_RECIPE = new NetherCraftShapeRecipe.RecipeType();
    public static IRecipeType<NeutroniumRecipe> NEUTRONIUM_RECIPE = new NeutroniumRecipe.RecipeType();


    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, IExtremeCraftRecipe.TYPE_ID, EXTREME_CRAFT_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, IExtremeCraftRecipe.ENDER_TYPE_ID, ENDER_CRAFT_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, IExtremeCraftRecipe.NETHER_TYPE_ID, NETHER_CRAFT_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, IExtremeCraftRecipe.TYPE_SHAPE_ID, EXTREME_CRAFT_SHAPE_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, IExtremeCraftRecipe.ENDER_TYPE_SHAPE_ID, ENDER_CRAFT_SHAPE_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, IExtremeCraftRecipe.NETHER_TYPE_SHAPE_ID, NETHER_CRAFT_SHAPE_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, NeutroniumRecipe.TYPE_ID, NEUTRONIUM_RECIPE);
    }
}
