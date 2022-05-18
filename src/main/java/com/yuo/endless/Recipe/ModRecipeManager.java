package com.yuo.endless.Recipe;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.ItemRegistry;
import com.yuo.spacearms.Blocks.BlockRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 模组动态配方
 */
public class ModRecipeManager {

    public static ExtremeCraftRecipe infinityCatalyst; //催化剂
    public static ExtremeCraftRecipe meatBalls; //寰宇肉丸
    public static ExtremeCraftRecipe stew; //超级煲

    public static void addCompressorCraft() {
        int count = 0; //模组影响的额外数量 +25 +50 +100 +150 -25
        int rate = 1; //模组影响的额外倍率  *1 *2 *3 *4

        if (Endless.IS_SPACE_ARMS) count += 100;
        if (Endless.IS_ENCHANTS) count += 50;
        if (Endless.IS_ORE_CROP) rate += 2;
        if (Endless.IS_PAIMENG) count += 150;
        if (Endless.IS_MORE_COALS) count += 25;
        if (Endless.IS_PROJECTE) rate += 3;
        if (Endless.IS_BOTANIA) count += 100;
        if (Endless.IS_ICE_AND_FIRE) count += 100;
        if (Endless.IS_TORCHERINO) rate += 2;
        if (Endless.IS_CREATE) rate += 1;
        if (Endless.IS_S_BACK_PACKS) count += 100;

        //限制
        int countEnd = Math.min(1000, count);
        int rateEnd = Math.min(10, rate);

        if (Endless.IS_SPACE_ARMS) {
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityRuby.get()), (250 + countEnd) * rateEnd,
                    getList(new ItemStack(BlockRegistry.rubyBlock.get())) );
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityDragon.get()), (100 + countEnd) * rateEnd,
                    getList(new ItemStack(BlockRegistry.dragonBlock.get())));
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularitySpace.get()), (50 + countEnd) * rateEnd,
                    getList(new ItemStack(BlockRegistry.spaceBlock.get())));
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityXray.get()), (150 + countEnd) * rateEnd,
                    getList(new ItemStack(BlockRegistry.xrayBlock.get()), new ItemStack(BlockRegistry.superXrayBlock.get(), 5)));
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityUltra.get()), (80 + countEnd) * rateEnd,
                    getList(new ItemStack(BlockRegistry.superBlock.get()), new ItemStack(BlockRegistry.ultraBlock.get(), 5)));
        }
        if (Endless.IS_ICE_AND_FIRE){
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularitySilver.get()), (200 + countEnd) * rateEnd,
                    getList(new ItemStack(IafBlockRegistry.SILVER_BLOCK)));
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityCopper.get()), (300 + countEnd) * rateEnd,
                    getList(new ItemStack(IafBlockRegistry.COPPER_BLOCK)));
        }
        //奇点合成配方
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityClay.get()), (400 + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.CLAY)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityCoal.get()), (450 + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.COAL_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityDiamond.get()), (250 + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.DIAMOND_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityEmerald.get()), (200 + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.EMERALD_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityGold.get()), (350 + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.GOLD_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityIron.get()), (300 + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.IRON_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityLapis.get()), (400 + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.LAPIS_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityNetherite.get()), (150 + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.NETHERITE_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityQuartz.get()), (500 + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.QUARTZ_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityRedstone.get()), (400 + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.REDSTONE_BLOCK)));
    }

    //无尽工作台配方
    public static void addExtremeCrafts() {

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.neutronCollector.get()),
                "IIQQQQQII",
                "I QQQQQ I",
                "I  RRR  I",
                "X RRRRR X",
                "I RRXRR I",
                "X RRRRR X",
                "I  RRR  I",
                "I       I",
                "IIIXIXIII",
                'X', new ItemStack(ItemRegistry.crystalMatrixIngot.get()),
                'I', new ItemStack(Blocks.IRON_BLOCK),
                'Q', new ItemStack(Blocks.QUARTZ_BLOCK),
                'R', new ItemStack(Blocks.REDSTONE_BLOCK));

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityIngot.get()),
                "NNNNNNNNN",
                "NCXXCXXCN",
                "NXCCXCCXN",
                "NCXXCXXCN",
                "NNNNNNNNN",
                'C', new ItemStack(ItemRegistry.crystalMatrixIngot.get()),
                'N', new ItemStack(ItemRegistry.neutroniumIngot.get()),
                'X', new ItemStack(ItemRegistry.infinityCatalyst.get()));
        //无尽催化剂
        infinityCatalyst = ExtremeCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ItemRegistry.infinityCatalyst.get()),
                new ItemStack(Blocks.EMERALD_BLOCK), new ItemStack(ItemRegistry.crystalMatrixIngot.get()),
                new ItemStack(ItemRegistry.neutroniumIngot.get()), new ItemStack(ItemRegistry.meatballs.get()),
                new ItemStack(ItemRegistry.stew.get()), new ItemStack(ItemRegistry.endestPearl.get()),
                new ItemStack(ItemRegistry.recordFragment.get()),
                new ItemStack(ItemRegistry.singularityClay.get()), new ItemStack(ItemRegistry.singularityCoal.get()),
                new ItemStack(ItemRegistry.singularityDiamond.get()), new ItemStack(ItemRegistry.singularityEmerald.get()),
                new ItemStack(ItemRegistry.singularityGold.get()), new ItemStack(ItemRegistry.singularityIron.get()),
                new ItemStack(ItemRegistry.singularityLapis.get()), new ItemStack(ItemRegistry.singularityNetherite.get()),
                new ItemStack(ItemRegistry.singularityQuartz.get()), new ItemStack(ItemRegistry.singularityRedstone.get()));

        if (Endless.IS_PAIMENG || Endless.IS_SPACE_ARMS) {
            meatBalls = ExtremeCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ItemRegistry.meatballs.get()),
                    new ItemStack(ItemRegistry.neutronNugget.get()), new ItemStack(Items.PORKCHOP), new ItemStack(Items.BEEF),
                    new ItemStack(Items.MUTTON), new ItemStack(Items.COD), new ItemStack(Items.SALMON), new ItemStack(Items.TROPICAL_FISH),
                    new ItemStack(Items.PUFFERFISH), new ItemStack(Items.RABBIT), new ItemStack(Items.CHICKEN),
                    new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.EGG));

            stew = ExtremeCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ItemRegistry.stew.get()),
                    new ItemStack(ItemRegistry.neutronNugget.get()), new ItemStack(Items.APPLE), new ItemStack(Items.GOLDEN_APPLE),
                    new ItemStack(Items.BREAD), new ItemStack(Items.KELP), new ItemStack(Items.COCOA_BEANS), new ItemStack(Blocks.CAKE),
                    new ItemStack(Items.GLISTERING_MELON_SLICE), new ItemStack(Items.CARROT), new ItemStack(Items.POISONOUS_POTATO),
                    new ItemStack(Items.CHORUS_FRUIT), new ItemStack(Items.BEETROOT), new ItemStack(Items.MUSHROOM_STEW),
                    new ItemStack(Items.HONEY_BOTTLE), new ItemStack(Items.SWEET_BERRIES));
        } else {
            meatBalls = ExtremeCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ItemRegistry.meatballs.get()),
                    new ItemStack(ItemRegistry.neutronNugget.get()), new ItemStack(Items.PORKCHOP), new ItemStack(Items.COOKED_PORKCHOP),
                    new ItemStack(Items.BEEF), new ItemStack(Items.COOKED_BEEF), new ItemStack(Items.MUTTON), new ItemStack(Items.COOKED_MUTTON),
                    new ItemStack(Items.COD), new ItemStack(Items.COOKED_COD), new ItemStack(Items.SALMON), new ItemStack(Items.COOKED_SALMON),
                    new ItemStack(Items.TROPICAL_FISH), new ItemStack(Items.PUFFERFISH), new ItemStack(Items.RABBIT), new ItemStack(Items.RABBIT_STEW),
                    new ItemStack(Items.COOKED_RABBIT), new ItemStack(Items.CHICKEN), new ItemStack(Items.COOKED_CHICKEN),
                    new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.EGG));

            stew = ExtremeCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ItemRegistry.stew.get()),
                    new ItemStack(ItemRegistry.neutronNugget.get()), new ItemStack(Items.APPLE), new ItemStack(Items.GOLDEN_APPLE),
                    new ItemStack(Items.WHEAT), new ItemStack(Items.BREAD), new ItemStack(Items.KELP), new ItemStack(Items.DRIED_KELP),
                    new ItemStack(Items.COCOA_BEANS), new ItemStack(Items.COOKIE), new ItemStack(Items.MELON_SLICE),
                    new ItemStack(Items.GLISTERING_MELON_SLICE), new ItemStack(Items.CARROT), new ItemStack(Items.POTATO),
                    new ItemStack(Items.BAKED_POTATO), new ItemStack(Items.POISONOUS_POTATO), new ItemStack(Items.CHORUS_FRUIT),
                    new ItemStack(Blocks.CAKE), new ItemStack(Items.PUMPKIN_PIE), new ItemStack(Items.BEETROOT),
                    new ItemStack(Items.BEETROOT_SOUP), new ItemStack(Items.MUSHROOM_STEW), new ItemStack(Items.HONEY_BOTTLE),
                    new ItemStack(Items.SWEET_BERRIES));
        }

        if (Endless.IS_SPACE_ARMS) {
            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityPickaxe.get()),
                    " IIIIIII ",
                    "IIIICIIII",
                    "II  Y  II",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'Y', new ItemStack(com.yuo.spacearms.Items.ItemRegistry.opPickaxe.get()),
                    'C', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));
            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinitySword.get()),
                    "       II",
                    "      III",
                    "     III ",
                    "    III  ",
                    " C III   ",
                    "  CYI    ",
                    "  NC     ",
                    " N  C    ",
                    "X        ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'Y', new ItemStack(com.yuo.spacearms.Items.ItemRegistry.opSword.get()),
                    'X', new ItemStack(ItemRegistry.infinityCatalyst.get()),
                    'C', new ItemStack(ItemRegistry.crystalMatrixIngot.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));
            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityShovel.get()),
                    "      III",
                    "     IIXI",
                    "      YII",
                    "     N I ",
                    "    N    ",
                    "   N     ",
                    "  N      ",
                    " N       ",
                    "N        ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'Y', new ItemStack(com.yuo.spacearms.Items.ItemRegistry.spaceShovel.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityAxe.get()),
                    "   I   ",
                    "  IIIII",
                    "  IYXI ",
                    "   IN  ",
                    "    N  ",
                    "    N  ",
                    "    N  ",
                    "    N  ",
                    "    N  ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'Y', new ItemStack(com.yuo.spacearms.Items.ItemRegistry.spaceAxe.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityHoe.get()),
                    "     N ",
                    "   IIII",
                    "  IIIYI",
                    "  I  XI",
                    "     N ",
                    "     N ",
                    "     N ",
                    "     N ",
                    "     N ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'Y', new ItemStack(com.yuo.spacearms.Items.ItemRegistry.spaceHoe.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityBow.get()),
                    "   II",
                    "  I W",
                    " I  W",
                    "I   W",
                    "X   Y",
                    "I   W",
                    " I  W",
                    "  I W",
                    "   II",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'Y', new ItemStack(com.yuo.spacearms.Items.ItemRegistry.amosiBow.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'W', ItemTags.WOOL);

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityChest.get()),
                    " NN   NN ",
                    "NNN   NNN",
                    "NNN   NNN",
                    " NIIIIIN ",
                    " NIIXIIN ",
                    " NIIYIIN ",
                    " NIIIIIN ",
                    " NIIIIIN ",
                    "  NNNNN  ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'Y', new ItemStack(com.yuo.spacearms.Items.ItemRegistry.opChest.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityHead.get()),
                    "  NNNNN  ",
                    " NIIIIIN ",
                    " N XIX N ",
                    " NIIYIIN ",
                    " NIIIIIN ",
                    " NI I IN ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'Y', new ItemStack(com.yuo.spacearms.Items.ItemRegistry.opHead.get()),
                    'X', new ItemStack(ItemRegistry.infinityCatalyst.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityLegs.get()),
                    "NNNNNNNNN",
                    "NIIIYIIIN",
                    "NINNXNNIN",
                    "NIN   NIN",
                    "NCN   NCN",
                    "NIN   NIN",
                    "NIN   NIN",
                    "NIN   NIN",
                    "NNN   NNN",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'Y', new ItemStack(com.yuo.spacearms.Items.ItemRegistry.opLegs.get()),
                    'X', new ItemStack(ItemRegistry.infinityCatalyst.get()),
                    'C', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityFeet.get()),
                    " NNN NNN ",
                    " NIN NIN ",
                    " NIN NIN ",
                    "NNIN NIYN",
                    "NIIN NIIN",
                    "NNNN NNNN",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'Y', new ItemStack(com.yuo.spacearms.Items.ItemRegistry.opFeet.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.skullfireSword.get()),
                    "       IX",
                    "      IXI",
                    "     IXI ",
                    "    IXI  ",
                    " B IXI   ",
                    "  BYI    ",
                    "  WB     ",
                    " W  B    ",
                    "D        ",
                    'I', new ItemStack(ItemRegistry.infinityCatalyst.get()),
                    'Y', new ItemStack(com.yuo.spacearms.Items.ItemRegistry.beheadSword.get()),
                    'X', new ItemStack(Items.BLAZE_POWDER),
                    'B', new ItemStack(Items.BONE),
                    'D', new ItemStack(Items.NETHER_STAR),
                    'W', ItemTags.LOGS);
        } else {
            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityPickaxe.get()),
                    " IIIIIII ",
                    "IIIICIIII",
                    "II  N  II",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'C', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));
            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinitySword.get()),
                    "       II",
                    "      III",
                    "     III ",
                    "    III  ",
                    " C III   ",
                    "  CII    ",
                    "  NC     ",
                    " N  C    ",
                    "X        ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'X', new ItemStack(ItemRegistry.infinityCatalyst.get()),
                    'C', new ItemStack(ItemRegistry.crystalMatrixIngot.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));
            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityShovel.get()),
                    "      III",
                    "     IIXI",
                    "      III",
                    "     N I ",
                    "    N    ",
                    "   N     ",
                    "  N      ",
                    " N       ",
                    "N        ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityAxe.get()),
                    "   I   ",
                    "  IIIII",
                    "  IIXI ",
                    "   IN  ",
                    "    N  ",
                    "    N  ",
                    "    N  ",
                    "    N  ",
                    "    N  ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityHoe.get()),
                    "     N ",
                    "   IIII",
                    "  IIIII",
                    "  I  XI",
                    "     N ",
                    "     N ",
                    "     N ",
                    "     N ",
                    "     N ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityBow.get()),
                    "   II",
                    "  I W",
                    " I  W",
                    "I   W",
                    "X   W",
                    "I   W",
                    " I  W",
                    "  I W",
                    "   II",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'W', ItemTags.WOOL);

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityChest.get()),
                    " NN   NN ",
                    "NNN   NNN",
                    "NNN   NNN",
                    " NIIIIIN ",
                    " NIIXIIN ",
                    " NIIIIIN ",
                    " NIIIIIN ",
                    " NIIIIIN ",
                    "  NNNNN  ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityHead.get()),
                    "  NNNNN  ",
                    " NIIIIIN ",
                    " N XIX N ",
                    " NIIIIIN ",
                    " NIIIIIN ",
                    " NI I IN ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'X', new ItemStack(ItemRegistry.infinityCatalyst.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityLegs.get()),
                    "NNNNNNNNN",
                    "NIIIXIIIN",
                    "NINNXNNIN",
                    "NIN   NIN",
                    "NCN   NCN",
                    "NIN   NIN",
                    "NIN   NIN",
                    "NIN   NIN",
                    "NNN   NNN",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'X', new ItemStack(ItemRegistry.infinityCatalyst.get()),
                    'C', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityFeet.get()),
                    " NNN NNN ",
                    " NIN NIN ",
                    " NIN NIN ",
                    "NNIN NINN",
                    "NIIN NIIN",
                    "NNNN NNNN",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.skullfireSword.get()),
                    "       IX",
                    "      IXI",
                    "     IXI ",
                    "    IXI  ",
                    " B IXI   ",
                    "  BXI    ",
                    "  WB     ",
                    " W  B    ",
                    "D        ",
                    'I', new ItemStack(ItemRegistry.infinityCatalyst.get()),
                    'X', new ItemStack(Items.BLAZE_POWDER),
                    'B', new ItemStack(Items.BONE),
                    'D', new ItemStack(Items.NETHER_STAR),
                    'W', ItemTags.LOGS);
        }


        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.endestPearl.get()),
                "   EEE   ",
                " EEPPPEE ",
                " EPPPPPE ",
                "EPPPNPPPE",
                "EPPNSNPPE",
                "EPPPNPPPE",
                " EPPPPPE ",
                " EEPPPEE ",
                "   EEE   ",
                'E', new ItemStack(Blocks.END_STONE),
                'P', new ItemStack(Items.ENDER_PEARL),
                'S', new ItemStack(Items.NETHER_STAR),
                'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.neutroniumCompressor.get()),
                "IIIHHHIII",
                "X N   N X",
                "I N   N I",
                "X N   N X",
                "RNN O NNR",
                "X N   N X",
                "I N   N I",
                "X N   N X",
                "IIIXIXIII",
                'X', new ItemStack(ItemRegistry.crystalMatrixIngot.get()),
                'N', new ItemStack(ItemRegistry.neutroniumIngot.get()),
                'I', new ItemStack(Blocks.IRON_BLOCK),
                'H', new ItemStack(Blocks.HOPPER),
                'R', new ItemStack(Blocks.REDSTONE_BLOCK),
                'O', new ItemStack(ItemRegistry.neutroniumBlock.get()));
    }


    /**
     * 根据其他模组修改配方
     */
    public static void lastMinuteChanges() {
        if (Endless.IS_SPACE_ARMS) {
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(ItemRegistry.singularityRuby.get()), new ItemStack(ItemRegistry.singularityDragon.get()),
                    new ItemStack(ItemRegistry.singularitySpace.get()), new ItemStack(ItemRegistry.singularityXray.get()),
                    new ItemStack(ItemRegistry.singularityUltra.get()), new ItemStack(com.yuo.spacearms.Items.ItemRegistry.jiejing.get()),
                    new ItemStack(Items.BEDROCK));
            ExtremeCraftingManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(com.yuo.spacearms.Items.ItemRegistry.superRabbitStew.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    new ItemStack(com.yuo.spacearms.Items.ItemRegistry.superCookie.get()),
                    new ItemStack(com.yuo.spacearms.Items.ItemRegistry.superBeetrootSoup.get()),
                    new ItemStack(com.yuo.spacearms.Items.ItemRegistry.superPumpkinPie.get()));
            CompressorManager.addInputs(ItemRegistry.singularityEmerald.get(),
                    getList(new ItemStack(BlockRegistry.emeraldIngotBlock.get(), 5)));
        }
        if (Endless.IS_PAIMENG) {
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.relicsBoxOne.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.paimengFood.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.xiantiaoqiang.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.cishenPinpan.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.daHuangjinShunzhiji.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.huangjinxie.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.huangyouXiexie.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.larouWowotou.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.tianshurou.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.lailaicai.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.zhongyuanZasui.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.jinsiXiaqiu.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.duigaogao.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.honghuiShourou.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.mijiangHuluoboJianrou.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.yurenTusi.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.dulaiQinrou.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.chuanchuanSanwei.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.feiyingTianfuluo.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.feiyingXiaxianbei.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.yangangSanxian.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.tiantianhuaNiangji.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.chouziShucaiDunrou.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.molarou.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.mengdeKaoyu.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.tiwateJiandan.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.yeguJirouchuan.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.bugFood.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.xiantiaoqiang.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.feiyuShijindai.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.jidouhua.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.yueliangpai.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.riluoguo.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.danbaofan.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.mengdeTudoubing.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.sifangHeping.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.sancaiTuanzi.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.tuanziNiunai.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.kousansi.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.mingyuedan.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.huangyouSongrong.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.mifanBuding.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.xingrenDoufu.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.manzuShala.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.ruozhuzhu.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.shijinChaomian.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.miwowo.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.boheGuodong.get()),
                    new ItemStack(com.yuo.PaiMeng.Items.ItemRegistry.luoboShishutang.get()));
        }
        if (Endless.IS_ENCHANTS) {
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(com.yuo.yuoenchants.Items.ItemRegistry.BrokenMagicPearlSuper.get()));
        }
        if (Endless.IS_MORE_COALS) {
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(com.yuo.morecoal.Items.ItemRegistry.lavaCoal.get()));
        }
        if (Endless.IS_ICE_AND_FIRE){
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(ItemRegistry.singularitySilver.get()), new ItemStack(ItemRegistry.singularityCopper.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    Ingredient.fromStacks(new ItemStack(IafItemRegistry.AMBROSIA)), Ingredient.fromStacks(
                            new ItemStack(IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK), new ItemStack(IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK),
                            new ItemStack(IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK)));
            ExtremeCraftingManager.getInstance().addRecipeInput(meatBalls,
                    Ingredient.fromStacks(new ItemStack(IafItemRegistry.FIRE_DRAGON_FLESH), new ItemStack(IafItemRegistry.ICE_DRAGON_FLESH),
                            new ItemStack(IafItemRegistry.LIGHTNING_DRAGON_FLESH)));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    Ingredient.fromStacks(new ItemStack(IafItemRegistry.FIRE_STEW), new ItemStack(IafItemRegistry.FROST_STEW),
                            new ItemStack(IafItemRegistry.LIGHTNING_STEW)));
        }
        if (Endless.IS_BOTANIA){
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(ModBlocks.terrasteelBlock), new ItemStack(ModItems.gaiaIngot));
        }
    }

    /**
     * 获取list
     * @param stacks 物品
     * @return list
     */
    private static NonNullList<ItemStack> getList(ItemStack... stacks){
        NonNullList<ItemStack> list = NonNullList.create();
        list.addAll(Arrays.asList(stacks));
        return list;
    }
}
