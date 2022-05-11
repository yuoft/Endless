package com.yuo.endless.Recipe;

import com.yuo.endless.Endless;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeTypeRegistry {
    public static final DeferredRegister RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Endless.MOD_ID);

    public static final RegistryObject<ExtremeCraftRecipe.Serializer> EXTREME_CRAFT_SERIALIZER = RECIPE_TYPES.register("extreme_craft", ExtremeCraftRecipe.Serializer::new);
    public static final RegistryObject<NeutroniumRecipe.Serializer> NEUTRONIUM_SERIALIZER = RECIPE_TYPES.register("neutronium", NeutroniumRecipe.Serializer::new);

    public static IRecipeType<ExtremeCraftRecipe> EXTREME_CRAFT_RECIPE = new ExtremeCraftRecipe.RecipeType();
    public static IRecipeType<NeutroniumRecipe> NEUTRONIUM_RECIPE = new NeutroniumRecipe.RecipeType();


    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, ExtremeCraftRecipe.TYPE_ID, EXTREME_CRAFT_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, NeutroniumRecipe.TYPE_ID, NEUTRONIUM_RECIPE);
    }
}
