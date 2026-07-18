package com.yuo.endless.recipe;

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
import com.yuo.endless.items.EndlessItems;
import com.yuo.endless.items.Singularity;
import com.yuo.endless.EndlessUtils;
import com.yuo.spacearms.Items.SAItems;
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
        //无序配方
        ExtremeShapeCraftBuilder infinityCatalyst = ExtremeShapeCraftBuilder.shapeless(RecipeCategory.MISC, EndlessItems.infinityCatalyst.get(), 1).requires(getBaseInfinityCatalyst());
        if (EndlessUtils.isEnchants) infinityCatalyst.requires(YEItems.SuperBrokenMagicPearl.get());
        if (EndlessUtils.isSpaceArms) infinityCatalyst.requires(Items.BEDROCK);
        if (EndlessUtils.isIAF) {
            infinityCatalyst.requires(Ingredient.of(new ItemStack(IafItemRegistry.AMBROSIA.get())));
            infinityCatalyst.requires(Ingredient.of(new ItemStack(IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK.get()), new ItemStack(IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK.get()), new ItemStack(IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK.get())));
        }
        if (EndlessUtils.isBOT) infinityCatalyst.requires(BotaniaItems.gaiaIngot);
        if (EndlessUtils.isAE2){
            infinityCatalyst.requires(new ItemStack(AEItems.ITEM_CELL_256K));
            infinityCatalyst.requires(new ItemStack(AEItems.FLUID_CELL_256K));
            infinityCatalyst.requires(new ItemStack(AEItems.SINGULARITY));
        }
        if (EndlessUtils.isDE){
            Item item = BuiltInRegistries.ITEM.get(EndlessUtils.parse("draconicevolution:chaos_shard")); //混沌碎片
            if (item != Items.AIR) infinityCatalyst.requires(item);
        }
        if (EndlessUtils.isTTF) infinityCatalyst.requires(new ItemStack(TFBlocks.IRONWOOD_BLOCK.get()));
        if (EndlessUtils.isSlashBlade2) infinityCatalyst.requires(SBItems.proudsoul_trapezohedron);
        if (EndlessUtils.isMysticalAgriculture){
            infinityCatalyst.requires(new ItemStack(BlockInit.ELEMENTAL_STONE.get()));
            infinityCatalyst.requires(new ItemStack(BlockInit.ELEMATILIUS_CAULDRON.get()));
            infinityCatalyst.requires(ItemInit.POTION_ELEMATILIUS.get());
        }
        if (EndlessUtils.isThermal) infinityCatalyst.requires(new ItemStack(ThermalCore.BLOCKS.get("enderium_block")));
        if (EndlessUtils.isRS){
            infinityCatalyst.requires(RSItems.ITEM_STORAGE_DISKS.get(ItemStorageType.SIXTY_FOUR_K).get());
            infinityCatalyst.requires(RSItems.FLUID_STORAGE_DISKS.get(FluidStorageType.FOUR_THOUSAND_NINETY_SIX_K).get());
        }
        if (EndlessUtils.isTC3) infinityCatalyst.requires(new ItemStack(TinkerModifiers.dragonScale));
        infinityCatalyst.unlockedBy("has_item", has(EndlessItems.infinityCatalyst.get())).save(consumer);

        ExtremeShapeCraftBuilder cosmicMeatBalls = ExtremeShapeCraftBuilder.shapeless(RecipeCategory.MISC, EndlessItems.cosmicMeatBalls.get(), 1).requires(getBaseCosmicMeatBalls());
        if (EndlessUtils.isIAF) cosmicMeatBalls.requires(Ingredient.of(new ItemStack(IafItemRegistry.FIRE_DRAGON_FLESH.get()), new ItemStack(IafItemRegistry.ICE_DRAGON_FLESH.get()), new ItemStack(IafItemRegistry.LIGHTNING_DRAGON_FLESH.get())));
        if (EndlessUtils.isTTF) cosmicMeatBalls.requires(TFItems.HYDRA_CHOP.get());
        if (EndlessUtils.isThermal){
            cosmicMeatBalls.requires(ThermalCore.ITEMS.get("stuffed_pepper"));
            cosmicMeatBalls.requires(ThermalCore.ITEMS.get("sushi_maki"));
            cosmicMeatBalls.requires(ThermalCore.ITEMS.get("stuffed_pumpkin"));
        }
        cosmicMeatBalls.unlockedBy("has_item", has(EndlessItems.cosmicMeatBalls.get())).save(consumer);

        ExtremeShapeCraftBuilder ultimateStew = ExtremeShapeCraftBuilder.shapeless(RecipeCategory.MISC, EndlessItems.ultimateStew.get(), 1).requires(getBaseUltimateStew());
        if (EndlessUtils.isIAF) ultimateStew.requires(Ingredient.of(new ItemStack(IafItemRegistry.FIRE_STEW.get()), new ItemStack(IafItemRegistry.FROST_STEW.get()), new ItemStack(IafItemRegistry.LIGHTNING_STEW.get())));
        if (EndlessUtils.isBOT) ultimateStew.requires(BotaniaItems.manaCookie);
        if (EndlessUtils.isTTF) ultimateStew.requires(TFItems.MAZE_MAP.get());
        if (EndlessUtils.isCreate){
            String str = "create:bar_of_chocolate";
            Item item = BuiltInRegistries.ITEM.get(EndlessUtils.parse(str));
            if (item != Items.AIR) ultimateStew.requires(item);
        }
        if (EndlessUtils.isThermal){
            ultimateStew.requires(ThermalCore.ITEMS.get("xp_stew"));
            ultimateStew.requires(ThermalCore.ITEMS.get("spring_salad"));
        }
        ultimateStew.unlockedBy("has_item", has(EndlessItems.ultimateStew.get())).save(consumer);

        ExtremeShapeCraftBuilder eternalSingularity = ExtremeShapeCraftBuilder.shapeless(RecipeCategory.MISC, EndlessItems.eternalSingularity.get(), 1).requires(getBaseEternalSingularity());
        if (EndlessUtils.isIAF) eternalSingularity.requires(Singularity.getSingularity("silver"));
        if (EndlessUtils.isBOT) {
            eternalSingularity.requires(Singularity.getSingularity("manasteel"));
            eternalSingularity.requires(Singularity.getSingularity("terrasteel"));
            eternalSingularity.requires(Singularity.getSingularity("elementium"));
        }
        if (EndlessUtils.isDE){
            eternalSingularity.requires(Singularity.getSingularity("draconium"));
            eternalSingularity.requires(Singularity.getSingularity("awakened_draconium"));
        }
        if (EndlessUtils.isPE){
            eternalSingularity.requires(Singularity.getSingularity("dark_matter"));
            eternalSingularity.requires(Singularity.getSingularity("red_matter"));
        }
        if (EndlessUtils.isCreate) eternalSingularity.requires(Singularity.getSingularity("zinc"));
        if (EndlessUtils.isThermal){
            eternalSingularity.requires(Singularity.getSingularity("nickel"));
            eternalSingularity.requires(Singularity.getSingularity("lead"));
            eternalSingularity.requires(Singularity.getSingularity("tin"));
        }
        if (EndlessUtils.isTC3){
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
        if (EndlessUtils.isIAF){
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("silver").getItem(), "silver",
                            Ingredient.of(new ItemStack(IafBlockRegistry.SILVER_BLOCK.get())), getInputCount(275))
                    .unlockedBy("has_item", has(Singularity.getSingularity("silver").getItem())).save(consumer);
        }
        if (EndlessUtils.isSpaceArms){
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("ruby").getItem(), "ruby",
                            Ingredient.of(new ItemStack(SAItems.rubyBlock.get())), getInputCount(250))
                    .unlockedBy("has_item", has(Singularity.getSingularity("ruby").getItem())).save(consumer);
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("dragon").getItem(), "dragon",
                            Ingredient.of(new ItemStack(SAItems.dragonBlock.get())), getInputCount(100))
                    .unlockedBy("has_item", has(Singularity.getSingularity("dragon").getItem())).save(consumer);
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("space").getItem(), "space",
                            Ingredient.of(new ItemStack(SAItems.spaceBlock.get())), getInputCount(50))
                    .unlockedBy("has_item", has(Singularity.getSingularity("space").getItem())).save(consumer);
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("xray").getItem(), "xray",
                            Ingredient.of(new ItemStack(SAItems.xrayBlock.get())), getInputCount(150))
                    .unlockedBy("has_item", has(Singularity.getSingularity("xray").getItem())).save(consumer);
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("ultra").getItem(), "ultra",
                            Ingredient.of(new ItemStack(SAItems.superBlock.get())), getInputCount(80))
                    .unlockedBy("has_item", has(Singularity.getSingularity("ultra").getItem())).save(consumer);
        }
        if (EndlessUtils.isCreate){
            String str = "create:zinc_block"; //锌块
            Block block = BuiltInRegistries.BLOCK.get(EndlessUtils.parse(str));
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("zinc").getItem(), "zinc",
                            Ingredient.of(new ItemStack(block)), getInputCount(300))
                    .unlockedBy("has_item", has(Singularity.getSingularity("zinc").getItem())).save(consumer);
        }
        if (EndlessUtils.isThermal){
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
        if (EndlessUtils.isDE){
            String str0 = "draconicevolution:draconium_block"; //龙块
            String str1 = "draconicevolution:awakened_draconium_block"; //觉醒龙块
            Block block0 = BuiltInRegistries.BLOCK.get(EndlessUtils.parse(str0));
            Block block1 = BuiltInRegistries.BLOCK.get(EndlessUtils.parse(str1));
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
        if (EndlessUtils.isBOT){
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
        if (EndlessUtils.isPE){
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("dark_matter").getItem(), "dark_matter",
                            Ingredient.of(new ItemStack(PEBlocks.DARK_MATTER)), getInputCount(150))
                    .unlockedBy("has_item", has(Singularity.getSingularity("dark_matter").getItem())).save(consumer);
            CompressorBuilder.shapeless(RecipeCategory.MISC, Singularity.getSingularity("red_matter").getItem(), "red_matter",
                            Ingredient.of(new ItemStack(PEBlocks.RED_MATTER)), getInputCount(100))
                    .unlockedBy("has_item", has(Singularity.getSingularity("red_matter").getItem())).save(consumer);
        }
        if (EndlessUtils.isTC3){
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
    private int getInputCount(int base){
        List<Integer> list = getSingularityCount();
        return (base + list.get(0)) * list.get(1);
    }

    /**
     * 压缩机所需矿物块数量
     */
    private List<Integer> getSingularityCount(){
        int count = 0; //模组影响的额外数量 +25 +50 +100 +150 -25
        int rate = 1; //模组影响的额外倍率  *1 *2 *3 *4

        if (EndlessUtils.isEnchants) count += 50;
        if (EndlessUtils.isPE) rate += 3;
        if (EndlessUtils.isBOT) count += 100;
        if (EndlessUtils.isIAF) count += 100;
        if (EndlessUtils.isTorcherino) rate += 2;
        if (EndlessUtils.isCreate) rate += 1;
        if (EndlessUtils.isSophisticatedBackpacks) count += 100;

        if (EndlessUtils.isOreExcavation) rate += 2;
        if (EndlessUtils.isTC3) count += 100;
        if (EndlessUtils.isCrT) count += 25;
        if (EndlessUtils.isStorageDrawers) count += 50;
        if (EndlessUtils.isEnchantingInfuser) rate += 1;
        if (EndlessUtils.isTouhouLittleMaid) count += 50;
        if (EndlessUtils.isTravelersBackpack) count += 100;
        if (EndlessUtils.isAE2) count += 150;
        if (EndlessUtils.isWaystones) count += 25;
        if (EndlessUtils.isAlexsMobs) count += 25;
        if (EndlessUtils.isTTF) count += 50;
        if (EndlessUtils.isSlashBlade2) rate += 2;
        if (EndlessUtils.isThermal) count += 100;
        if (EndlessUtils.isTimeBottle) rate += 1;
        if (EndlessUtils.isDE) rate += 2;
        if (EndlessUtils.isInfernalMobs) count -= 25;
        if (EndlessUtils.isChampions) count -= 25;
        if (EndlessUtils.isZombieAwareness) rate -= 2;
        if (EndlessUtils.isMysticalAgriculture) rate += 3;
        if (EndlessUtils.isRS) count += 150;

        if (EndlessUtils.isDS) rate += 2;
        if (EndlessUtils.isIPN) count += 50;
        if (EndlessUtils.isWDA) count += 50;
        if (EndlessUtils.isFarmersDelight) count += 100;
        if (EndlessUtils.isGoblinTraders) rate += 1;
        if (EndlessUtils.isFTBUltimine) rate += 2;
        if (EndlessUtils.isVampirism) count += 100;
        if (EndlessUtils.isCroparia) rate += 3;
        if (EndlessUtils.isMinecolonies) count += 100;
        if (EndlessUtils.isDivineRPG) rate += 2;
        if (EndlessUtils.isDEAdd) rate += 2;
        if (EndlessUtils.isDoggyTalents) count += 100;
        if (EndlessUtils.isIE) rate += 1;
        if (EndlessUtils.isEnigmaticLegacy) rate += 2;
        if (EndlessUtils.isApotheosis) rate += 2;
        if (EndlessUtils.isQuark) count += 50;
        if (EndlessUtils.isArsNouveau) rate += 1;
        if (EndlessUtils.isEXBOT) count += 50;
        if (EndlessUtils.isIronChests) rate += 1;
        if (EndlessUtils.isMobGrindingUtils) count += 50;
        if (EndlessUtils.isAR) rate += 2;
        if (EndlessUtils.isEverlastingAbilities) rate += 3;
        if (EndlessUtils.isBM3) rate += 2;
        if (EndlessUtils.isPEI) rate += 1;
        if (EndlessUtils.isER) count += 50;
        if (EndlessUtils.isLB) rate += 5;
        if (EndlessUtils.isPEX) rate += 5;
        if (EndlessUtils.isMorph) rate += 2;
        if (EndlessUtils.isLootr) rate += 2;
        if (EndlessUtils.isExtremeReactors) rate += 3;
        if (EndlessUtils.isPlayerRevive) count += 50;
        if (EndlessUtils.isXPTmoe) count += 50;
        if (EndlessUtils.isIronFurnaces) rate += 1;
        if (EndlessUtils.isCA) rate += 3;

        //限制
        int countEnd = Math.min(2000, count);
        int rateEnd = Math.min(20, rate);
        List<Integer> list = new ArrayList<>(2);
        list.add(countEnd);
        list.add(rateEnd);
        return list;
    }

}
