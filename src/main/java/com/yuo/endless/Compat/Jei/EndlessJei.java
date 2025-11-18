package com.yuo.endless.Compat.Jei;

import com.yuo.endless.Client.Gui.ExtremeCraftScreen;
import com.yuo.endless.Client.Gui.NeutroniumCompressorScreen;
import com.yuo.endless.Container.EndlessMenuTypes;
import com.yuo.endless.Container.ExtremeCraftContainer;
import com.yuo.endless.Container.NeutroniumCompressorContainer;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Singularity;
import com.yuo.endless.Recipe.*;
import com.yuo.endless.RlUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class EndlessJei implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return RlUtils.fa("jei_plugin");
    }

    //插件告诉JEI定制菜谱类别
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new ExtremeCraftRecipeCategory(guiHelper),
                new ExtremeCraftShapeRecipeCategory(guiHelper),
                new NeutroniumCRecipeCategory(guiHelper));
    }

    //注册配方类别
    @Override
    @Deprecated
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        List<ExtremeCraftRecipe> recipes = recipeManager.getAllRecipesFor(EndlessRecipes.EXTREME_CRAFT_RECIPE.get()).stream().filter(Objects::nonNull).toList();
        registration.addRecipes(ExtremeCraftRecipeCategory.RECIPE_TYPE, recipes);
        List<ExtremeCraftShapeRecipe> recipes0 = recipeManager.getAllRecipesFor(EndlessRecipes.EXTREME_CRAFT_SHAPE_RECIPE.get()).stream().filter(Objects::nonNull).toList();
        registration.addRecipes(ExtremeCraftShapeRecipeCategory.RECIPE_TYPE, recipes0);
        registration.addRecipes(ExtremeCraftShapeRecipeCategory.RECIPE_TYPE, ExtremeCraftShpaelessManager.getInstance().getRecipeList());
        List<NeutroniumRecipe> recipes1 = recipeManager.getAllRecipesFor(EndlessRecipes.NEUTRONIUM_RECIPE.get()).stream().filter(Objects::nonNull).toList();
        registration.addRecipes(NeutroniumCRecipeCategory.RECIPE_TYPE, recipes1);
        registration.addRecipes(NeutroniumCRecipeCategory.RECIPE_TYPE, CompressorManager.getRecipes());
    }

    //注册+号添加
    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(ExtremeCraftContainer.class, EndlessMenuTypes.extremeCraftContainer.get(), ExtremeCraftRecipeCategory.RECIPE_TYPE, 0, 81, 82, 36);
        registration.addRecipeTransferHandler(ExtremeCraftContainer.class, EndlessMenuTypes.extremeCraftContainer.get(), ExtremeCraftShapeRecipeCategory.RECIPE_TYPE, 0, 81, 82, 36);
        registration.addRecipeTransferHandler(NeutroniumCompressorContainer.class, EndlessMenuTypes.neutroniumCompressorContainer.get(), NeutroniumCRecipeCategory.RECIPE_TYPE, 0, 1, 2, 36);
    }

    //注册机器合成
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(EndlessItems.extremeCraftingTable.get()), ExtremeCraftRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(EndlessItems.extremeCraftingTable.get()), ExtremeCraftShapeRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(EndlessItems.neutronCompressor.get()), NeutroniumCRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ExtremeCraftScreen.class, 0, 0, 0, 0, ExtremeCraftRecipeCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(ExtremeCraftScreen.class, 0, 0, 0, 0, ExtremeCraftShapeRecipeCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(NeutroniumCompressorScreen.class, 0, 0, 0, 0, NeutroniumCRecipeCategory.RECIPE_TYPE);
    }

    //注册物品不同nbt  使用nbt来在jei中显示
    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        IModPlugin.super.registerItemSubtypes(registration);
        registration.registerSubtypeInterpreter(EndlessItems.singularity.get(), (e, u) -> {
            CompoundTag nbt = (CompoundTag) e.getOrCreateTag().get(Singularity.NBT_MOD);
            if (nbt != null) return nbt.getString(Singularity.NBT_TYPE);
            return "";
        });
    }
}
