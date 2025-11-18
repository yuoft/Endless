package com.yuo.endless.Recipe;

import appeng.core.definitions.AEItems;
import cofh.thermal.core.ThermalCore;
import com.defacto34.croparia.init.BlockInit;
import com.defacto34.croparia.init.ItemInit;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.refinedmods.refinedstorage.RSItems;
import com.refinedmods.refinedstorage.apiimpl.storage.FluidStorageType;
import com.refinedmods.refinedstorage.apiimpl.storage.ItemStorageType;
import com.yuo.Enchants.Items.YEItems;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Singularity;
import com.yuo.endless.RlUtils;
import mods.flammpfeil.slashblade.init.SBItems;
import moze_intel.projecte.gameObjs.registries.PEBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerModifiers;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ModDataRecipes extends RecipeProvider {
    public ModDataRecipes(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.ENCHANTED_GOLDEN_APPLE).requires(Items.DIAMOND).requires(EndlessItems.infinityCatalyst.get())
                        .group("default").unlockedBy("has_item", has(Items.ENCHANTED_GOLDEN_APPLE)).save(consumer);


        //无序配方
        ExtremeShapeCraftBuilder infinityCatalyst = ExtremeShapeCraftBuilder.shapeless(RecipeCategory.MISC, EndlessItems.infinityCatalyst.get(), 1).requires(getBaseInfinityCatalyst());
        if (Endless.isEnchants) infinityCatalyst.requires(YEItems.SuperBrokenMagicPearl.get());
        if (Endless.isIAF) {
            infinityCatalyst.requires(Ingredient.of(new ItemStack(IafItemRegistry.AMBROSIA.get())));
            infinityCatalyst.requires(Ingredient.of(new ItemStack(IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK.get()), new ItemStack(IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK.get()), new ItemStack(IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK.get())));
        }
        if (Endless.isBOT) infinityCatalyst.requires(BotaniaItems.gaiaIngot);
        if (Endless.isAE2){
            infinityCatalyst.requires(new ItemStack(AEItems.ITEM_CELL_256K));
            infinityCatalyst.requires(new ItemStack(AEItems.FLUID_CELL_256K));
            infinityCatalyst.requires(new ItemStack(AEItems.SINGULARITY));
        }
        if (Endless.isDE){
            Item item = BuiltInRegistries.ITEM.get(RlUtils.parse("draconicevolution:chaos_shard")); //混沌碎片
            if (item != Items.AIR) infinityCatalyst.requires(item);
        }
        if (Endless.isTTF) infinityCatalyst.requires(new ItemStack(TFBlocks.IRONWOOD_BLOCK.get()));
        if (Endless.isSlashBlade2) infinityCatalyst.requires(SBItems.proudsoul_trapezohedron);
        if (Endless.isMysticalAgriculture){
            infinityCatalyst.requires(new ItemStack(BlockInit.ELEMENTAL_STONE.get()));
            infinityCatalyst.requires(new ItemStack(BlockInit.ELEMATILIUS_CAULDRON.get()));
            infinityCatalyst.requires(ItemInit.POTION_ELEMATILIUS.get());
        }
        if (Endless.isThermal) infinityCatalyst.requires(new ItemStack(ThermalCore.BLOCKS.get("enderium_block")));
        if (Endless.isRS){
            infinityCatalyst.requires(RSItems.ITEM_STORAGE_DISKS.get(ItemStorageType.SIXTY_FOUR_K).get());
            infinityCatalyst.requires(RSItems.FLUID_STORAGE_DISKS.get(FluidStorageType.FOUR_THOUSAND_NINETY_SIX_K).get());
        }
        if (Endless.isTC3) infinityCatalyst.requires(new ItemStack(TinkerModifiers.dragonScale));
        infinityCatalyst.unlockedBy("has_item", has(EndlessItems.infinityCatalyst.get())).save(consumer);

        ExtremeShapeCraftBuilder cosmicMeatBalls = ExtremeShapeCraftBuilder.shapeless(RecipeCategory.MISC, EndlessItems.cosmicMeatBalls.get(), 1).requires(getBaseCosmicMeatBalls());
        if (Endless.isIAF) cosmicMeatBalls.requires(Ingredient.of(new ItemStack(IafItemRegistry.FIRE_DRAGON_FLESH.get()), new ItemStack(IafItemRegistry.ICE_DRAGON_FLESH.get()), new ItemStack(IafItemRegistry.LIGHTNING_DRAGON_FLESH.get())));
        if (Endless.isTTF) cosmicMeatBalls.requires(TFItems.HYDRA_CHOP.get());
        if (Endless.isThermal){
            cosmicMeatBalls.requires(ThermalCore.ITEMS.get("stuffed_pepper"));
            cosmicMeatBalls.requires(ThermalCore.ITEMS.get("sushi_maki"));
            cosmicMeatBalls.requires(ThermalCore.ITEMS.get("stuffed_pumpkin"));
        }
        cosmicMeatBalls.unlockedBy("has_item", has(EndlessItems.cosmicMeatBalls.get())).save(consumer);

        ExtremeShapeCraftBuilder ultimateStew = ExtremeShapeCraftBuilder.shapeless(RecipeCategory.MISC, EndlessItems.ultimateStew.get(), 1).requires(getBaseUltimateStew());
        if (Endless.isIAF) ultimateStew.requires(Ingredient.of(new ItemStack(IafItemRegistry.FIRE_STEW.get()), new ItemStack(IafItemRegistry.FROST_STEW.get()), new ItemStack(IafItemRegistry.LIGHTNING_STEW.get())));
        if (Endless.isBOT) ultimateStew.requires(BotaniaItems.manaCookie);
        if (Endless.isTTF) ultimateStew.requires(TFItems.MAZE_MAP.get());
        if (Endless.isCreate){
            String str = "create:bar_of_chocolate";
            Item item = BuiltInRegistries.ITEM.get(RlUtils.parse(str));
            if (item != Items.AIR) ultimateStew.requires(item);
        }
        if (Endless.isThermal){
            ultimateStew.requires(ThermalCore.ITEMS.get("xp_stew"));
            ultimateStew.requires(ThermalCore.ITEMS.get("spring_salad"));
        }
        ultimateStew.unlockedBy("has_item", has(EndlessItems.ultimateStew.get())).save(consumer);

        ExtremeShapeCraftBuilder eternalSingularity = ExtremeShapeCraftBuilder.shapeless(RecipeCategory.MISC, EndlessItems.eternalSingularity.get(), 1).requires(getBaseEternalSingularity());
        if (Endless.isIAF) eternalSingularity.requires(Singularity.getSingularity("silver"));
        if (Endless.isBOT) {
            eternalSingularity.requires(Singularity.getSingularity("manasteel"));
            eternalSingularity.requires(Singularity.getSingularity("terrasteel"));
            eternalSingularity.requires(Singularity.getSingularity("elementium"));
        }
        if (Endless.isDE){
            eternalSingularity.requires(Singularity.getSingularity("draconium"));
            eternalSingularity.requires(Singularity.getSingularity("awakened_draconium"));
        }
        if (Endless.isPE){
            eternalSingularity.requires(Singularity.getSingularity("dark_matter"));
            eternalSingularity.requires(Singularity.getSingularity("red_matter"));
        }
        if (Endless.isCreate) eternalSingularity.requires(Singularity.getSingularity("zinc"));
        if (Endless.isThermal){
            eternalSingularity.requires(Singularity.getSingularity("nickel"));
            eternalSingularity.requires(Singularity.getSingularity("lead"));
            eternalSingularity.requires(Singularity.getSingularity("tin"));
        }
        if (Endless.isTC3){
            eternalSingularity.requires(Singularity.getSingularity("cobalt"));
            eternalSingularity.requires(Singularity.getSingularity("manyullyn"));
        }
        eternalSingularity.unlockedBy("has_item", has(EndlessItems.eternalSingularity.get())).save(consumer);

        //压缩机配方 原版
        CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("coal").getItem(), "coal",
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_COAL), getInputCount(450))
                .unlockedBy("has_item", has(Singularity.getSingularity("coal").getItem())).save(consumer);
        CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("clay").getItem(), "clay",
                        Ingredient.of(new ItemStack(Items.CLAY)), getInputCount(400))
                .unlockedBy("has_item", has(Singularity.getSingularity("clay").getItem())).save(consumer);
        CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("iron").getItem(), "iron",
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_IRON), getInputCount(300))
                .unlockedBy("has_item", has(Singularity.getSingularity("iron").getItem())).save(consumer);
        CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("gold").getItem(), "gold",
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD), getInputCount(350))
                .unlockedBy("has_item", has(Singularity.getSingularity("gold").getItem())).save(consumer);
        CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("diamond").getItem(), "diamond",
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_DIAMOND), getInputCount(250))
                .unlockedBy("has_item", has(Singularity.getSingularity("diamond").getItem())).save(consumer);
        CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("emerald").getItem(), "emerald",
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_EMERALD), getInputCount(200))
                .unlockedBy("has_item", has(Singularity.getSingularity("emerald").getItem())).save(consumer);
        CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("netherite").getItem(), "netherite",
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_NETHERITE), getInputCount(150))
                .unlockedBy("has_item", has(Singularity.getSingularity("netherite").getItem())).save(consumer);
        CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("lapis").getItem(), "lapis",
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_LAPIS), getInputCount(400))
                .unlockedBy("has_item", has(Singularity.getSingularity("lapis").getItem())).save(consumer);
        CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("redstone").getItem(), "redstone",
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_REDSTONE), getInputCount(400))
                .unlockedBy("has_item", has(Singularity.getSingularity("redstone").getItem())).save(consumer);
        CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("quartz").getItem(), "quartz",
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_QUARTZ), getInputCount(500))
                .unlockedBy("has_item", has(Singularity.getSingularity("quartz").getItem())).save(consumer);
        CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("copper").getItem(), "copper",
                        Ingredient.of(Tags.Items.STORAGE_BLOCKS_COPPER), getInputCount(375))
                .unlockedBy("has_item", has(Singularity.getSingularity("copper").getItem())).save(consumer);

        //模组
        if (Endless.isIAF){
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("silver").getItem(), "silver",
                            Ingredient.of(new ItemStack(IafBlockRegistry.SILVER_BLOCK.get())), getInputCount(275))
                    .unlockedBy("has_item", has(Singularity.getSingularity("silver").getItem())).save(consumer);
        }
        if (Endless.isCreate){
            String str = "create:zinc_block"; //锌块
            Block block = BuiltInRegistries.BLOCK.get(RlUtils.parse(str));
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("zinc").getItem(), "zinc",
                            Ingredient.of(new ItemStack(block)), getInputCount(300))
                    .unlockedBy("has_item", has(Singularity.getSingularity("zinc").getItem())).save(consumer);
        }
        if (Endless.isThermal){
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("nickel").getItem(), "nickel",
                            Ingredient.of(new ItemStack(ThermalCore.BLOCKS.get("nickel_block"))), getInputCount(400))
                    .unlockedBy("has_item", has(Singularity.getSingularity("nickel").getItem())).save(consumer);
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("lead").getItem(), "lead",
                            Ingredient.of(new ItemStack(ThermalCore.BLOCKS.get("lead_block"))), getInputCount(300))
                    .unlockedBy("has_item", has(Singularity.getSingularity("lead").getItem())).save(consumer);
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("tin").getItem(), "tin",
                            Ingredient.of(new ItemStack(ThermalCore.BLOCKS.get("tin_block"))), getInputCount(400))
                    .unlockedBy("has_item", has(Singularity.getSingularity("tin").getItem())).save(consumer);
        }
        if (Endless.isDE){
            String str0 = "draconicevolution:draconium_block"; //龙块
            String str1 = "draconicevolution:awakened_draconium_block"; //觉醒龙块
            Block block0 = BuiltInRegistries.BLOCK.get(RlUtils.parse(str0));
            Block block1 = BuiltInRegistries.BLOCK.get(RlUtils.parse(str1));
            if (block0 != Blocks.AIR){
                CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("draconium").getItem(), "draconium",
                                Ingredient.of(new ItemStack(block0)), getInputCount(80))
                        .unlockedBy("has_item", has(Singularity.getSingularity("draconium").getItem())).save(consumer);
            }
            if (block1 != Blocks.AIR){
                CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("awakened_draconium").getItem(), "awakened_draconium",
                                Ingredient.of(new ItemStack(block1)), getInputCount(10))
                        .unlockedBy("has_item", has(Singularity.getSingularity("awakened_draconium").getItem())).save(consumer);
            }
        }
        if (Endless.isBOT){
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("manasteel").getItem(), "manasteel",
                            Ingredient.of(new ItemStack(BotaniaBlocks.manasteelBlock)), getInputCount(200))
                    .unlockedBy("has_item", has(Singularity.getSingularity("manasteel").getItem())).save(consumer);
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("terrasteel").getItem(), "terrasteel",
                            Ingredient.of(new ItemStack(BotaniaBlocks.terrasteelBlock)), getInputCount(100))
                    .unlockedBy("has_item", has(Singularity.getSingularity("terrasteel").getItem())).save(consumer);
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("elementium").getItem(), "elementium",
                            Ingredient.of(new ItemStack(BotaniaBlocks.elementiumBlock)), getInputCount(50))
                    .unlockedBy("has_item", has(Singularity.getSingularity("elementium").getItem())).save(consumer);
        }
        if (Endless.isPE){
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("dark_matter").getItem(), "dark_matter",
                            Ingredient.of(new ItemStack(PEBlocks.DARK_MATTER)), getInputCount(150))
                    .unlockedBy("has_item", has(Singularity.getSingularity("dark_matter").getItem())).save(consumer);
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("red_matter").getItem(), "red_matter",
                            Ingredient.of(new ItemStack(PEBlocks.RED_MATTER)), getInputCount(100))
                    .unlockedBy("has_item", has(Singularity.getSingularity("red_matter").getItem())).save(consumer);
        }
        if (Endless.isTC3){
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("cobalt").getItem(), "cobalt",
                            Ingredient.of(new ItemStack(TinkerMaterials.cobalt)), getInputCount(100))
                    .unlockedBy("has_item", has(Singularity.getSingularity("cobalt").getItem())).save(consumer);
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("manyullyn").getItem(), "manyullyn",
                            Ingredient.of(new ItemStack(TinkerMaterials.manyullyn)), getInputCount(100))
                    .unlockedBy("has_item", has(Singularity.getSingularity("manyullyn").getItem())).save(consumer);
        }
    }

    /**
     * 转换List<ItemStack>到List<Ingredient>
     */
    private static List<Ingredient> getIngredients(List<ItemStack> list) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (ItemStack stack : list) {
            ingredients.add(Ingredient.of(stack));
        }

        return ingredients;
    }

    //配方中不变的部分
    private static List<Ingredient> getBaseInfinityCatalyst(){
        List<ItemStack> list = Arrays.asList(new ItemStack(EndlessItems.crystalMatrixIngot.get()), new ItemStack(EndlessItems.neutroniumIngot.get()),
                new ItemStack(EndlessItems.cosmicMeatBalls.get()), new ItemStack(EndlessItems.ultimateStew.get()),
                new ItemStack(EndlessItems.endestPearl.get()), new ItemStack(EndlessItems.recordFragment.get()),
                new ItemStack(EndlessItems.eternalSingularity.get()));
        return getIngredients(list);
    }

    private static List<Ingredient> getBaseCosmicMeatBalls(){
        List<ItemStack> list = Arrays.asList(new ItemStack(EndlessItems.neutroniumNugget.get()), new ItemStack(Items.PORKCHOP), new ItemStack(Items.COOKED_PORKCHOP),
                new ItemStack(Items.BEEF), new ItemStack(Items.COOKED_BEEF), new ItemStack(Items.MUTTON), new ItemStack(Items.COOKED_MUTTON),
                new ItemStack(Items.COD), new ItemStack(Items.COOKED_COD), new ItemStack(Items.SALMON), new ItemStack(Items.COOKED_SALMON),
                new ItemStack(Items.TROPICAL_FISH), new ItemStack(Items.PUFFERFISH), new ItemStack(Items.RABBIT), new ItemStack(Items.RABBIT_STEW),
                new ItemStack(Items.COOKED_RABBIT), new ItemStack(Items.CHICKEN), new ItemStack(Items.COOKED_CHICKEN),
                new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.EGG));
        return getIngredients(list);
    }

    private static List<Ingredient> getBaseUltimateStew(){
        List<ItemStack> list = Arrays.asList(new ItemStack(EndlessItems.neutroniumNugget.get()), new ItemStack(Items.APPLE), new ItemStack(Items.GOLDEN_APPLE),
                new ItemStack(Items.WHEAT), new ItemStack(Items.BREAD), new ItemStack(Items.KELP), new ItemStack(Items.DRIED_KELP),
                new ItemStack(Items.COCOA_BEANS), new ItemStack(Items.COOKIE), new ItemStack(Items.MELON_SLICE),
                new ItemStack(Items.GLISTERING_MELON_SLICE), new ItemStack(Items.CARROT), new ItemStack(Items.POTATO),
                new ItemStack(Items.BAKED_POTATO), new ItemStack(Items.POISONOUS_POTATO), new ItemStack(Items.CHORUS_FRUIT),
                new ItemStack(Blocks.CAKE), new ItemStack(Items.PUMPKIN_PIE), new ItemStack(Items.BEETROOT),
                new ItemStack(Items.BEETROOT_SOUP), new ItemStack(Items.MUSHROOM_STEW), new ItemStack(Items.HONEY_BOTTLE),
                new ItemStack(Items.SWEET_BERRIES));
        return getIngredients(list);
    }

    private static List<Ingredient> getBaseEternalSingularity(){
        List<ItemStack> list = Arrays.asList(Singularity.getSingularity("coal"), Singularity.getSingularity("copper"), Singularity.getSingularity("iron"),
                Singularity.getSingularity("gold"), Singularity.getSingularity("diamond"),
                Singularity.getSingularity("netherite"), Singularity.getSingularity("emerald"),
                Singularity.getSingularity("lapis"), Singularity.getSingularity("redstone"),
                Singularity.getSingularity("quartz"), Singularity.getSingularity("clay"));
        return getIngredients(list);
    }

    /**
     * 计算最终数量
     * @param base 基础
     */
    private static int getInputCount(int base){
        List<Integer> list = getSingularityCount();
        return (base + list.get(0)) * list.get(1);
    }

    /**
     * 压缩机所需矿物块数量
     */
    private static List<Integer> getSingularityCount(){
        int count = 0; //模组影响的额外数量 +25 +50 +100 +150 -25
        int rate = 1; //模组影响的额外倍率  *1 *2 *3 *4

        if (Endless.isEnchants) count += 50;
        if (Endless.isPE) rate += 3;
        if (Endless.isBOT) count += 100;
        if (Endless.isIAF) count += 100;
        if (Endless.isTorcherino) rate += 2;
        if (Endless.isCreate) rate += 1;
        if (Endless.isSophisticatedBackpacks) count += 100;

        if (Endless.isOreExcavation) rate += 2;
        if (Endless.isTC3) count += 100;
        if (Endless.isCrT) count += 25;
        if (Endless.isStorageDrawers) count += 50;
        if (Endless.isEnchantingInfuser) rate += 1;
        if (Endless.isTouhouLittleMaid) count += 50;
        if (Endless.isTravelersBackpack) count += 100;
        if (Endless.isAE2) count += 150;
        if (Endless.isWaystones) count += 25;
        if (Endless.isAlexsMobs) count += 25;
        if (Endless.isTTF) count += 50;
        if (Endless.isSlashBlade2) rate += 2;
        if (Endless.isThermal) count += 100;
        if (Endless.isTimeBottle) rate += 1;
        if (Endless.isDE) rate += 2;
        if (Endless.isInfernalMobs) count -= 25;
        if (Endless.isChampions) count -= 25;
        if (Endless.isZombieAwareness) rate -= 2;
        if (Endless.isMysticalAgriculture) rate += 3;
        if (Endless.isRS) count += 150;

        if (Endless.isDS) rate += 2;
        if (Endless.isIPN) count += 50;
        if (Endless.isWDA) count += 50;
        if (Endless.isFarmersDelight) count += 100;
        if (Endless.isGoblinTraders) rate += 1;
        if (Endless.isFTBUltimine) rate += 2;
        if (Endless.isVampirism) count += 100;
        if (Endless.isCroparia) rate += 3;
        if (Endless.isMinecolonies) count += 100;
        if (Endless.isDivineRPG) rate += 2;
        if (Endless.isDEAdd) rate += 2;
        if (Endless.isDoggyTalents) count += 100;
        if (Endless.isIE) rate += 1;
        if (Endless.isEnigmaticLegacy) rate += 2;
        if (Endless.isApotheosis) rate += 2;
        if (Endless.isQuark) count += 50;
        if (Endless.isArsNouveau) rate += 1;
        if (Endless.isEXBOT) count += 50;
        if (Endless.isIronChests) rate += 1;
        if (Endless.isMobGrindingUtils) count += 50;
        if (Endless.isAR) rate += 2;
        if (Endless.isEverlastingAbilities) rate += 3;
        if (Endless.isBM3) rate += 2;
        if (Endless.isPEI) rate += 1;
        if (Endless.isER) count += 50;
        if (Endless.isLB) rate += 5;
        if (Endless.isPEX) rate += 5;
        if (Endless.isMorph) rate += 2;
        if (Endless.isLootr) rate += 2;
        if (Endless.isExtremeReactors) rate += 3;
        if (Endless.isPlayerRevive) count += 50;
        if (Endless.isXPTmoe) count += 50;
        if (Endless.isIronFurnaces) rate += 1;
        if (Endless.isCA) rate += 3;

        //限制
        int countEnd = Math.min(2000, count);
        int rateEnd = Math.min(20, rate);
        List<Integer> list = new ArrayList<>(2);
        list.add(countEnd);
        list.add(rateEnd);
        return list;
    }
}
