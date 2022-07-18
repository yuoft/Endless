package com.yuo.endless.Recipe;

import appeng.core.Api;
import cofh.thermal.core.ThermalCore;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.ItemRegistry;
import com.yuo.spacearms.Blocks.BlockRegistry;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import mods.flammpfeil.slashblade.init.SBItems;
import moze_intel.projecte.gameObjs.registries.PEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.tslat.aoa3.common.registration.AoABlocks;
import net.tslat.aoa3.common.registration.AoAItems;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import java.util.Arrays;

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

        if (Endless.isSpaceArms) count += 100;
        if (Endless.isEnchants) count += 50;
        if (Endless.isOreCrop) rate += 2;
        if (Endless.isPaimeng) count += 150;
        if (Endless.isMoreCoals) count += 25;
        if (Endless.isProjecte) rate += 3;
        if (Endless.isBotania) count += 100;
        if (Endless.isIceandfire) count += 100;
        if (Endless.isTorcherino) rate += 2;
        if (Endless.isCreate) rate += 1;
        if (Endless.isSophisticatedBackpacks) count += 100;

        if (Endless.isOreExcavation) rate += 2;
        if (Endless.isAdventOfAscension3) count += 100;
        if (Endless.isTinkersConstruct3) count += 100;
        if (Endless.isCraftTweaker) count += 25;
        if (Endless.isStorageDrawers) count += 50;
        if (Endless.isEnchantingInfuser) rate += 1;
        if (Endless.isTouhouLittleMaid) count += 50;
        if (Endless.isTravelersBackpack) count += 100;
        if (Endless.isAppliedEnergistics2) count += 150;
        if (Endless.isWaystones) count += 25;
        if (Endless.isAlexsMobs) count += 25;
        if (Endless.isTheTwilightForest) count += 50;
        if (Endless.isAstralSorcery) count += 100;
        if (Endless.isSlashBlade2) rate += 2;
        if (Endless.isThermal) count += 100;
        if (Endless.isTimeBottle) rate += 1;
        if (Endless.isDraconicEvolution) count += 150;
        if (Endless.isInfernalMobs) count -= 25;
        if (Endless.isChampions) count -= 25;
        if (Endless.isZombieAwareness) rate -= 2;
        if (Endless.isMysticalAgriculture) rate += 3;

        //限制
        int countEnd = Math.min(Config.SERVER.modRatioCount.get(), count);
        int rateEnd = Math.min(Config.SERVER.modRatioRate.get(), rate);

        if (Endless.isSpaceArms) {
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityRuby.get()), (Config.SERVER.singularityRuby.get() + countEnd) * rateEnd,
                    getList(new ItemStack(BlockRegistry.rubyBlock.get())) );
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityDragon.get()), (Config.SERVER.singularityDragon.get() + countEnd) * rateEnd,
                    getList(new ItemStack(BlockRegistry.dragonBlock.get())));
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularitySpace.get()), (Config.SERVER.singularitySpace.get() + countEnd) * rateEnd,
                    getList(new ItemStack(BlockRegistry.spaceBlock.get())));
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityXray.get()), (Config.SERVER.singularityXray.get() + countEnd) * rateEnd,
                    getList(new ItemStack(BlockRegistry.xrayBlock.get()), new ItemStack(BlockRegistry.superXrayBlock.get(), 5)));
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityUltra.get()), (Config.SERVER.singularityUltra.get() + countEnd) * rateEnd,
                    getList(new ItemStack(BlockRegistry.superBlock.get()), new ItemStack(BlockRegistry.ultraBlock.get(), 5)));
        }
        if (Endless.isIceandfire){
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularitySilver.get()), (Config.SERVER.singularitySilver.get() + countEnd) * rateEnd,
                    getList(new ItemStack(IafBlockRegistry.SILVER_BLOCK)));
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityCopper.get()), (Config.SERVER.singularityCopper.get() + countEnd) * rateEnd,
                    getList(new ItemStack(IafBlockRegistry.COPPER_BLOCK)));
        }
        if (Endless.isCreate){
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityZinc.get()), (Config.SERVER.singularityZinc.get() + countEnd) * rateEnd,
                    getList(new ItemStack(AllBlocks.ZINC_BLOCK.get())));
        }
        if (Endless.isThermal){
//            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityPlatinum.get()), (Config.SERVER.singularityPlatinum.get() + countEnd) * rateEnd,
//                    getList(new ItemStack(ThermalCore.BLOCKS.get("platinum_block"))));
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityNickel.get()), (Config.SERVER.singularityNickel.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ThermalCore.BLOCKS.get("nickel_block"))));
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityLead.get()), (Config.SERVER.singularityLead.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ThermalCore.BLOCKS.get("lead_block"))));
            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityTin.get()), (Config.SERVER.singularityTin.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ThermalCore.BLOCKS.get("tin_block"))));
        }
        //奇点合成配方
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityClay.get()), (Config.SERVER.singularityClay.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.CLAY)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityCoal.get()), (Config.SERVER.singularityCoal.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.COAL_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityDiamond.get()), (Config.SERVER.singularityDiamond.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.DIAMOND_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityEmerald.get()), (Config.SERVER.singularityEmerald.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.EMERALD_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityGold.get()), (Config.SERVER.singularityGold.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.GOLD_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityIron.get()), (Config.SERVER.singularityIron.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.IRON_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityLapis.get()), (Config.SERVER.singularityLapis.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.LAPIS_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityNetherite.get()), (Config.SERVER.singularityNetherite.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.NETHERITE_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityQuartz.get()), (Config.SERVER.singularityQuartz.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.QUARTZ_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityRedstone.get()), (Config.SERVER.singularityRedstone.get() + countEnd) * rateEnd,
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

        if (Endless.isPaimeng || Endless.isSpaceArms) {
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

        if (Endless.isSpaceArms) {
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
                    "   I     ",
                    "  IIIII  ",
                    "  IYXI   ",
                    "   IN    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'Y', new ItemStack(com.yuo.spacearms.Items.ItemRegistry.spaceAxe.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityHoe.get()),
                    "     N   ",
                    "   IIII  ",
                    "  IIIYI  ",
                    "  I  XI  ",
                    "     N   ",
                    "     N   ",
                    "     N   ",
                    "     N   ",
                    "     N   ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'Y', new ItemStack(com.yuo.spacearms.Items.ItemRegistry.spaceHoe.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityBow.get()),
                    "     II  ",
                    "    I W  ",
                    "   I  W  ",
                    "  I   W  ",
                    "  X   Y  ",
                    "  I   W  ",
                    "   I  W  ",
                    "    I W  ",
                    "     II  ",
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
                    "NNIN NYNN",
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
                    "   I     ",
                    "  IIIII  ",
                    "  IIXI   ",
                    "   IN    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityHoe.get()),
                    "     N   ",
                    "   IIII  ",
                    "  IIIII  ",
                    "  I  XI  ",
                    "     N   ",
                    "     N   ",
                    "     N   ",
                    "     N   ",
                    "     N   ",
                    'I', new ItemStack(ItemRegistry.infinityIngot.get()),
                    'X', new ItemStack(ItemRegistry.crystalMatrix.get()),
                    'N', new ItemStack(ItemRegistry.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityBow.get()),
                    "     II  ",
                    "    I W  ",
                    "   I  W  ",
                    "  I   W  ",
                    "  X   W  ",
                    "  I   W  ",
                    "   I  W  ",
                    "    I W  ",
                    "     II  ",
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
                    'I', new ItemStack(ItemRegistry.crystalMatrixIngot.get()),
                    'X', new ItemStack(Items.BLAZE_POWDER),
                    'B', new ItemStack(Items.BONE),
                    'D', new ItemStack(Items.NETHER_STAR),
                    'W', ItemTags.LOGS);
        }


        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityArrow.get()),
                "XY       ",
                "YYYY     ",
                " YZ      ",
                " Y Z     ",
                "    Z    ",
                "     Z   ",
                "      AA ",
                "      AAA",
                "       A ",
                'X', new ItemStack(ItemRegistry.infinityNugget.get()),
                'Y', new ItemStack(ItemRegistry.infinityIngot.get()),
                'Z', new ItemStack(ItemRegistry.neutroniumIngot.get()),
                'A', new ItemStack(ItemRegistry.crystalMatrixIngot.get()));

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.infinityCrossBow.get()),
                "  YYYYY  ",
                " AZ  X   ",
                "YZZZX    ",
                "Y ZBZ    ",
                "Y XZZZ   ",
                "YX  ZZ   ",
                "Y     Z  ",
                "       Z ",
                "         ",
                'X', new ItemStack(ItemRegistry.infinityNugget.get()),
                'Y', new ItemStack(ItemRegistry.infinityIngot.get()),
                'Z', new ItemStack(ItemRegistry.neutroniumIngot.get()),
                'B', new ItemStack(ItemRegistry.neutroniumGear.get()),
                'A', new ItemStack(ItemRegistry.crystalMatrix.get()));

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(ItemRegistry.endestPearl.get()),
                "   EEE   ",
                " EEPPPEE ",
                " EPPAPPE ",
                "EPPANAPPE",
                "EPANSNAPE",
                "EPPANAPPE",
                " EPPAPPE ",
                " EEPPPEE ",
                "   EEE   ",
                'E', new ItemStack(Blocks.END_STONE),
                'A', new ItemStack(Blocks.TNT),
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
        if (Endless.isSpaceArms) { // 17+7 13+1 15+3
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
        if (Endless.isPaimeng) { //24+1 14+26 18+22
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
        if (Endless.isEnchants) { //25+1 40 40
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(com.yuo.yuoenchants.Items.ItemRegistry.BrokenMagicPearlSuper.get()));
        }
        if (Endless.isMoreCoals) { //26+1 40 40
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(com.yuo.morecoal.Items.ItemRegistry.lavaCoal.get()));
        }
        if (Endless.isIceandfire){ //27+4 40+1 40+1
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
        if (Endless.isBotania){ //31+2 41 41+1
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(ModBlocks.terrasteelBlock), new ItemStack(ModItems.gaiaIngot));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    new ItemStack(ModItems.manaCookie));
        }
        if (Endless.isAppliedEnergistics2){ //33+2 41 42
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(Api.instance().definitions().items().fluidCell64k()), new ItemStack(Api.instance().definitions().materials().singularity()));
        }
        if (Endless.isDraconicEvolution){ //35+1 41 42
            String str = "draconicevolution:awakened_draconium_block"; //觉醒龙块
            Block block = Registry.BLOCK.getOrDefault(new ResourceLocation(str));
            if (block != Blocks.AIR){
                ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                        new ItemStack(block));
            }
        }
        if (Endless.isProjecte){ //36+1 41 42
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(PEBlocks.RED_MATTER));
        }
        if (Endless.isTheTwilightForest){ //37+1 41+1 42+1
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(TFBlocks.ironwood_block.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(TFItems.hydra_chop.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    new ItemStack(TFItems.maze_wafer.get()));
        }
        if (Endless.isCreate){ //38+1 42 43+1
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(ItemRegistry.singularityZinc.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    new ItemStack(AllItems.BAR_OF_CHOCOLATE.get()));
        }
        if (Endless.isAdventOfAscension3){ //39+1 42+3 44+3
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(AoABlocks.SHYREGEM_BLOCK.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(AoAItems.COOKED_CHIMERA_CHOP.get()), new ItemStack(AoAItems.RAW_RAINBOWFISH.get()),
                    new ItemStack(AoAItems.COOKED_CHARGER_SHANK.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    new ItemStack(AoAItems.GOLDICAP_PETALS.get()), new ItemStack(AoAItems.MAGIC_MARANG.get()),
                    new ItemStack(AoAItems.NATURAL_TEA.get()));
        }
        if (Endless.isAstralSorcery){ //40+1 45 47
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(BlocksAS.STARMETAL));
        }
        if (Endless.isSlashBlade2){ //41+1 45 47
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(SBItems.proudsoul_trapezohedron));
        }
        if (Endless.isMysticalAgriculture){ //42+2 45 47
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(com.blakebr0.mysticalagriculture.init.ModBlocks.SUPREMIUM_GEMSTONE_BLOCK.get()),
                    new ItemStack(com.blakebr0.mysticalagriculture.init.ModBlocks.SUPREMIUM_BLOCK.get()));
        }
        if (Endless.isThermal){ //44+1 45+3 47+2
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst, new ItemStack(ThermalCore.BLOCKS.get("enderium_block")),
                    new ItemStack(ItemRegistry.singularityNickel.get()),
                    new ItemStack(ItemRegistry.singularityLead.get()), new ItemStack(ItemRegistry.singularityTin.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(ThermalCore.ITEMS.get("stuffed_pepper")), new ItemStack(ThermalCore.ITEMS.get("sushi_maki")),
                    new ItemStack(ThermalCore.ITEMS.get("stuffed_pumpkin")));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    new ItemStack(ThermalCore.ITEMS.get("xp_stew")), new ItemStack(ThermalCore.ITEMS.get("spring_salad")));
            CompressorManager.addInputs(ItemRegistry.singularityGold.get(), getList(new ItemStack(ThermalCore.BLOCKS.get("electrum_block"), 2)));
            CompressorManager.addInputs(ItemRegistry.singularityIron.get(), getList(new ItemStack(ThermalCore.BLOCKS.get("invar_block"), 2)));
        }
        if (Endless.isIceandfire && Endless.isThermal){
            CompressorManager.addInputs(ItemRegistry.singularityCopper.get(), getList(new ItemStack(ThermalCore.BLOCKS.get("copper_block")),
                    new ItemStack(ThermalCore.BLOCKS.get("bronze_block"), 3), new ItemStack(ThermalCore.BLOCKS.get("constantan_block"), 2)));
            CompressorManager.addInputs(ItemRegistry.singularitySilver.get(), getList(new ItemStack(ThermalCore.BLOCKS.get("silver_block"))));
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
