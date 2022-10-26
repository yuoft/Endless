package com.yuo.endless.Recipe;

import appeng.core.Api;
import cofh.thermal.core.ThermalCore;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.refinedmods.refinedstorage.RSItems;
import com.refinedmods.refinedstorage.apiimpl.storage.FluidStorageType;
import com.refinedmods.refinedstorage.apiimpl.storage.ItemStorageType;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.enchants.Items.YEItems;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.morecoal.Items.MoreCoalItems;
import com.yuo.spacearms.Blocks.SABlocks;
import com.yuo.spacearms.Items.SAItems;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import mods.flammpfeil.slashblade.init.SBItems;
import moze_intel.projecte.gameObjs.registries.PEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.tslat.aoa3.common.registration.AoABlocks;
import net.tslat.aoa3.common.registration.AoAItems;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.tools.TinkerModifiers;
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
        if (Endless.isPE) rate += 3;
        if (Endless.isBOT) count += 100;
        if (Endless.isIAF) count += 100;
        if (Endless.isTorcherino) rate += 2;
        if (Endless.isCreate) rate += 1;
        if (Endless.isSophisticatedBackpacks) count += 100;

        if (Endless.isOreExcavation) rate += 2;
        if (Endless.isAOA3) count += 100;
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
        if (Endless.isAS) count += 100;
        if (Endless.isSlashBlade2) rate += 2;
        if (Endless.isThermal) count += 100;
        if (Endless.isTimeBottle) rate += 1;
        if (Endless.isDE) count += 150;
        if (Endless.isInfernalMobs) count -= 25;
        if (Endless.isChampions) count -= 25;
        if (Endless.isZombieAwareness) rate -= 2;
        if (Endless.isMysticalAgriculture) rate += 3;
        if (Endless.isRS) count += 150;

        //限制
        int countEnd = Math.min(Config.SERVER.modRatioCount.get(), count);
        int rateEnd = Math.min(Config.SERVER.modRatioRate.get(), rate);

        if (Endless.isSpaceArms) {
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityRuby.get()), (Config.SERVER.singularityRuby.get() + countEnd) * rateEnd,
                    getList(new ItemStack(SABlocks.rubyBlock.get())) );
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityDragon.get()), (Config.SERVER.singularityDragon.get() + countEnd) * rateEnd,
                    getList(new ItemStack(SABlocks.dragonBlock.get())));
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularitySpace.get()), (Config.SERVER.singularitySpace.get() + countEnd) * rateEnd,
                    getList(new ItemStack(SABlocks.spaceBlock.get())));
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityXray.get()), (Config.SERVER.singularityXray.get() + countEnd) * rateEnd,
                    getList(new ItemStack(SABlocks.xrayBlock.get()), new ItemStack(SABlocks.superXrayBlock.get(), 5)));
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityUltra.get()), (Config.SERVER.singularityUltra.get() + countEnd) * rateEnd,
                    getList(new ItemStack(SABlocks.superBlock.get()), new ItemStack(SABlocks.ultraBlock.get(), 5)));
        }
        if (Endless.isIAF){
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularitySilver.get()), (Config.SERVER.singularitySilver.get() + countEnd) * rateEnd,
                    getList(new ItemStack(IafBlockRegistry.SILVER_BLOCK)));
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityCopper.get()), (Config.SERVER.singularityCopper.get() + countEnd) * rateEnd,
                    getList(new ItemStack(IafBlockRegistry.COPPER_BLOCK)));
        }
        if (Endless.isCreate){
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityZinc.get()), (Config.SERVER.singularityZinc.get() + countEnd) * rateEnd,
                    getList(new ItemStack(AllBlocks.ZINC_BLOCK.get())));
        }
        if (Endless.isThermal){
//            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityPlatinum.get()), (Config.SERVER.singularityPlatinum.get() + countEnd) * rateEnd,
//                    getList(new ItemStack(ThermalCore.BLOCKS.get("platinum_block"))));
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityNickel.get()), (Config.SERVER.singularityNickel.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ThermalCore.BLOCKS.get("nickel_block"))));
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityLead.get()), (Config.SERVER.singularityLead.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ThermalCore.BLOCKS.get("lead_block"))));
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityTin.get()), (Config.SERVER.singularityTin.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ThermalCore.BLOCKS.get("tin_block"))));
        }
        if (Endless.isDE){
            String str0 = "draconicevolution:draconium_block"; //龙块
            String str1 = "draconicevolution:awakened_draconium_block"; //觉醒龙块
            Block block0 = Registry.BLOCK.getOrDefault(new ResourceLocation(str0));
            Block block1 = Registry.BLOCK.getOrDefault(new ResourceLocation(str1));
            if (block0 != Blocks.AIR){
                CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityDragonIum.get()), (Config.SERVER.singularityDragonIum.get() + countEnd) * rateEnd,
                        getList(new ItemStack(block0)));
            }
            if (block1 != Blocks.AIR){
                CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityAwakenDragon.get()), (Config.SERVER.singularityAwakenDragon.get() + countEnd) * rateEnd,
                        getList(new ItemStack(block1)));
            }
        }
        if (Endless.isBOT){
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityMana.get()), (Config.SERVER.singularityMana.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ModBlocks.manasteelBlock)));
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityTara.get()), (Config.SERVER.singularityTara.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ModBlocks.terrasteelBlock)));
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityElementIum.get()), (Config.SERVER.singularityElementIum.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ModBlocks.elementiumBlock)));
        }
        if (Endless.isPE){
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityDarkMatter.get()), (Config.SERVER.singularityDarkMatter.get() + countEnd) * rateEnd,
                    getList(new ItemStack(PEBlocks.DARK_MATTER)));
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityRedMatter.get()), (Config.SERVER.singularityRedMatter.get() + countEnd) * rateEnd,
                    getList(new ItemStack(PEBlocks.RED_MATTER)));
        }
        if (Endless.isTC3){
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityCobalt.get()), (Config.SERVER.singularityCobalt.get() + countEnd) * rateEnd,
                    getList(new ItemStack(TinkerMaterials.cobalt)));
            CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityManyullyn.get()), (Config.SERVER.singularityManyullyn.get() + countEnd) * rateEnd,
                    getList(new ItemStack(TinkerMaterials.manyullyn)));
        }

        //奇点合成配方
        CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityClay.get()), (Config.SERVER.singularityClay.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.CLAY)));
        CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityCoal.get()), (Config.SERVER.singularityCoal.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.COAL_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityDiamond.get()), (Config.SERVER.singularityDiamond.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.DIAMOND_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityEmerald.get()), (Config.SERVER.singularityEmerald.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.EMERALD_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityGold.get()), (Config.SERVER.singularityGold.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.GOLD_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityIron.get()), (Config.SERVER.singularityIron.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.IRON_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityLapis.get()), (Config.SERVER.singularityLapis.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.LAPIS_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityNetherite.get()), (Config.SERVER.singularityNetherite.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.NETHERITE_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityQuartz.get()), (Config.SERVER.singularityQuartz.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.QUARTZ_BLOCK)));
        CompressorManager.addRecipe(new ItemStack(EndlessItems.singularityRedstone.get()), (Config.SERVER.singularityRedstone.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.REDSTONE_BLOCK)));
    }

    //无尽工作台配方
    public static void addExtremeCrafts() {

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.neutroniumCollector.get()),
                "IIQQQQQII",
                "I QQQQQ I",
                "I  RRR  I",
                "X RRRRR X",
                "I RRXRR I",
                "X RRRRR X",
                "I  RRR  I",
                "I       I",
                "IIIXIXIII",
                'X', new ItemStack(EndlessItems.crystalMatrixIngot.get()),
                'I', new ItemStack(Blocks.IRON_BLOCK),
                'Q', new ItemStack(Blocks.QUARTZ_BLOCK),
                'R', new ItemStack(Blocks.REDSTONE_BLOCK));

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityIngot.get()),
                "NNNNNNNNN",
                "NCXXCXXCN",
                "NXCCXCCXN",
                "NCXXCXXCN",
                "NNNNNNNNN",
                'C', new ItemStack(EndlessItems.crystalMatrixIngot.get()),
                'N', new ItemStack(EndlessItems.neutroniumIngot.get()),
                'X', new ItemStack(EndlessItems.infinityCatalyst.get()));
        //无尽催化剂
        infinityCatalyst = ExtremeCraftingManager.getInstance().addShapelessRecipe(new ItemStack(EndlessItems.infinityCatalyst.get()),
                new ItemStack(EndlessItems.crystalMatrixIngot.get()), new ItemStack(EndlessItems.neutroniumIngot.get()),
                new ItemStack(EndlessItems.cosmicMeatBalls.get()), new ItemStack(EndlessItems.ultimateStew.get()),
                new ItemStack(EndlessItems.endestPearl.get()), new ItemStack(EndlessItems.recordFragment.get()),
                new ItemStack(EndlessItems.singularityClay.get()), new ItemStack(EndlessItems.singularityCoal.get()),
                new ItemStack(EndlessItems.singularityDiamond.get()), new ItemStack(EndlessItems.singularityEmerald.get()),
                new ItemStack(EndlessItems.singularityGold.get()), new ItemStack(EndlessItems.singularityIron.get()),
                new ItemStack(EndlessItems.singularityLapis.get()), new ItemStack(EndlessItems.singularityNetherite.get()),
                new ItemStack(EndlessItems.singularityQuartz.get()), new ItemStack(EndlessItems.singularityRedstone.get()));

        if (Endless.isPaimeng || Endless.isSpaceArms) {
            meatBalls = ExtremeCraftingManager.getInstance().addShapelessRecipe(new ItemStack(EndlessItems.cosmicMeatBalls.get()),
                    new ItemStack(EndlessItems.neutroniumNugget.get()), new ItemStack(Items.PORKCHOP), new ItemStack(Items.BEEF),
                    new ItemStack(Items.MUTTON), new ItemStack(Items.COD), new ItemStack(Items.SALMON), new ItemStack(Items.TROPICAL_FISH),
                    new ItemStack(Items.PUFFERFISH), new ItemStack(Items.RABBIT), new ItemStack(Items.CHICKEN),
                    new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.EGG));

            stew = ExtremeCraftingManager.getInstance().addShapelessRecipe(new ItemStack(EndlessItems.ultimateStew.get()),
                    new ItemStack(EndlessItems.neutroniumNugget.get()), new ItemStack(Items.APPLE), new ItemStack(Items.GOLDEN_APPLE),
                    new ItemStack(Items.BREAD), new ItemStack(Items.KELP), new ItemStack(Items.COCOA_BEANS), new ItemStack(Blocks.CAKE),
                    new ItemStack(Items.GLISTERING_MELON_SLICE), new ItemStack(Items.CARROT), new ItemStack(Items.POISONOUS_POTATO),
                    new ItemStack(Items.CHORUS_FRUIT), new ItemStack(Items.BEETROOT), new ItemStack(Items.MUSHROOM_STEW),
                    new ItemStack(Items.HONEY_BOTTLE), new ItemStack(Items.SWEET_BERRIES));
        } else {
            meatBalls = ExtremeCraftingManager.getInstance().addShapelessRecipe(new ItemStack(EndlessItems.cosmicMeatBalls.get()),
                    new ItemStack(EndlessItems.neutroniumNugget.get()), new ItemStack(Items.PORKCHOP), new ItemStack(Items.COOKED_PORKCHOP),
                    new ItemStack(Items.BEEF), new ItemStack(Items.COOKED_BEEF), new ItemStack(Items.MUTTON), new ItemStack(Items.COOKED_MUTTON),
                    new ItemStack(Items.COD), new ItemStack(Items.COOKED_COD), new ItemStack(Items.SALMON), new ItemStack(Items.COOKED_SALMON),
                    new ItemStack(Items.TROPICAL_FISH), new ItemStack(Items.PUFFERFISH), new ItemStack(Items.RABBIT), new ItemStack(Items.RABBIT_STEW),
                    new ItemStack(Items.COOKED_RABBIT), new ItemStack(Items.CHICKEN), new ItemStack(Items.COOKED_CHICKEN),
                    new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.EGG));

            stew = ExtremeCraftingManager.getInstance().addShapelessRecipe(new ItemStack(EndlessItems.ultimateStew.get()),
                    new ItemStack(EndlessItems.neutroniumNugget.get()), new ItemStack(Items.APPLE), new ItemStack(Items.GOLDEN_APPLE),
                    new ItemStack(Items.WHEAT), new ItemStack(Items.BREAD), new ItemStack(Items.KELP), new ItemStack(Items.DRIED_KELP),
                    new ItemStack(Items.COCOA_BEANS), new ItemStack(Items.COOKIE), new ItemStack(Items.MELON_SLICE),
                    new ItemStack(Items.GLISTERING_MELON_SLICE), new ItemStack(Items.CARROT), new ItemStack(Items.POTATO),
                    new ItemStack(Items.BAKED_POTATO), new ItemStack(Items.POISONOUS_POTATO), new ItemStack(Items.CHORUS_FRUIT),
                    new ItemStack(Blocks.CAKE), new ItemStack(Items.PUMPKIN_PIE), new ItemStack(Items.BEETROOT),
                    new ItemStack(Items.BEETROOT_SOUP), new ItemStack(Items.MUSHROOM_STEW), new ItemStack(Items.HONEY_BOTTLE),
                    new ItemStack(Items.SWEET_BERRIES));
        }

        if (Endless.isSpaceArms) {
            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityPickaxe.get()),
                    " IIIIIII ",
                    "IIIICIIII",
                    "II  Y  II",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'Y', new ItemStack(SAItems.opPickaxe.get()),
                    'C', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));
            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinitySword.get()),
                    "       II",
                    "      III",
                    "     III ",
                    "    III  ",
                    " C III   ",
                    "  CYI    ",
                    "  NC     ",
                    " N  C    ",
                    "X        ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'Y', new ItemStack(SAItems.opSword.get()),
                    'X', new ItemStack(EndlessItems.infinityCatalyst.get()),
                    'C', new ItemStack(EndlessItems.crystalMatrixIngot.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));
            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityShovel.get()),
                    "      III",
                    "     IIXI",
                    "      YII",
                    "     N I ",
                    "    N    ",
                    "   N     ",
                    "  N      ",
                    " N       ",
                    "N        ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'Y', new ItemStack(SAItems.spaceShovel.get()),
                    'X', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityAxe.get()),
                    "   I     ",
                    "  IIIII  ",
                    "  IYXI   ",
                    "   IN    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'Y', new ItemStack(SAItems.spaceAxe.get()),
                    'X', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityHoe.get()),
                    "     N   ",
                    "   IIII  ",
                    "  IIIYI  ",
                    "  I  XI  ",
                    "     N   ",
                    "     N   ",
                    "     N   ",
                    "     N   ",
                    "     N   ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'Y', new ItemStack(SAItems.spaceHoe.get()),
                    'X', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityBow.get()),
                    "     II  ",
                    "    I W  ",
                    "   I  W  ",
                    "  I   W  ",
                    "  X   Y  ",
                    "  I   W  ",
                    "   I  W  ",
                    "    I W  ",
                    "     II  ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'Y', new ItemStack(SAItems.amosiBow.get()),
                    'X', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'W', ItemTags.WOOL);

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityChest.get()),
                    " NN   NN ",
                    "NNN   NNN",
                    "NNN   NNN",
                    " NIIIIIN ",
                    " NIIXIIN ",
                    " NIIYIIN ",
                    " NIIIIIN ",
                    " NIIIIIN ",
                    "  NNNNN  ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'Y', new ItemStack(SAItems.opChest.get()),
                    'X', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityHead.get()),
                    "  NNNNN  ",
                    " NIIIIIN ",
                    " N XIX N ",
                    " NIIYIIN ",
                    " NIIIIIN ",
                    " NI I IN ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'Y', new ItemStack(SAItems.opHead.get()),
                    'X', new ItemStack(EndlessItems.infinityCatalyst.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityLegs.get()),
                    "NNNNNNNNN",
                    "NIIIYIIIN",
                    "NINNXNNIN",
                    "NIN   NIN",
                    "NCN   NCN",
                    "NIN   NIN",
                    "NIN   NIN",
                    "NIN   NIN",
                    "NNN   NNN",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'Y', new ItemStack(SAItems.opLegs.get()),
                    'X', new ItemStack(EndlessItems.infinityCatalyst.get()),
                    'C', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityFeet.get()),
                    " NNN NNN ",
                    " NIN NIN ",
                    " NIN NIN ",
                    "NNIN NYNN",
                    "NIIN NIIN",
                    "NNNN NNNN",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'Y', new ItemStack(SAItems.opFeet.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.skullfireSword.get()),
                    "       IX",
                    "      IXI",
                    "     IXI ",
                    "    IXI  ",
                    " B IXI   ",
                    "  BYI    ",
                    "  WB     ",
                    " W  B    ",
                    "D        ",
                    'I', new ItemStack(EndlessItems.infinityCatalyst.get()),
                    'Y', new ItemStack(SAItems.beheadSword.get()),
                    'X', new ItemStack(Items.BLAZE_POWDER),
                    'B', new ItemStack(Items.BONE),
                    'D', new ItemStack(Items.NETHER_STAR),
                    'W', ItemTags.LOGS);
        } else {
            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityPickaxe.get()),
                    " IIIIIII ",
                    "IIIICIIII",
                    "II  N  II",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'C', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));
            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinitySword.get()),
                    "       II",
                    "      III",
                    "     III ",
                    "    III  ",
                    " C III   ",
                    "  CII    ",
                    "  NC     ",
                    " N  C    ",
                    "X        ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'X', new ItemStack(EndlessItems.infinityCatalyst.get()),
                    'C', new ItemStack(EndlessItems.crystalMatrixIngot.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));
            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityShovel.get()),
                    "      III",
                    "     IIXI",
                    "      III",
                    "     N I ",
                    "    N    ",
                    "   N     ",
                    "  N      ",
                    " N       ",
                    "N        ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'X', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityAxe.get()),
                    "   I     ",
                    "  IIIII  ",
                    "  IIXI   ",
                    "   IN    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    "    N    ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'X', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityHoe.get()),
                    "     N   ",
                    "   IIII  ",
                    "  IIIII  ",
                    "  I  XI  ",
                    "     N   ",
                    "     N   ",
                    "     N   ",
                    "     N   ",
                    "     N   ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'X', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityBow.get()),
                    "     II  ",
                    "    I W  ",
                    "   I  W  ",
                    "  I   W  ",
                    "  X   W  ",
                    "  I   W  ",
                    "   I  W  ",
                    "    I W  ",
                    "     II  ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'X', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'W', ItemTags.WOOL);

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityChest.get()),
                    " NN   NN ",
                    "NNN   NNN",
                    "NNN   NNN",
                    " NIIIIIN ",
                    " NIIXIIN ",
                    " NIIIIIN ",
                    " NIIIIIN ",
                    " NIIIIIN ",
                    "  NNNNN  ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'X', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityHead.get()),
                    "  NNNNN  ",
                    " NIIIIIN ",
                    " N XIX N ",
                    " NIIIIIN ",
                    " NIIIIIN ",
                    " NI I IN ",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'X', new ItemStack(EndlessItems.infinityCatalyst.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityLegs.get()),
                    "NNNNNNNNN",
                    "NIIIXIIIN",
                    "NINNXNNIN",
                    "NIN   NIN",
                    "NCN   NCN",
                    "NIN   NIN",
                    "NIN   NIN",
                    "NIN   NIN",
                    "NNN   NNN",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'X', new ItemStack(EndlessItems.infinityCatalyst.get()),
                    'C', new ItemStack(EndlessItems.crystalMatrixBlock.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityFeet.get()),
                    " NNN NNN ",
                    " NIN NIN ",
                    " NIN NIN ",
                    "NNIN NINN",
                    "NIIN NIIN",
                    "NNNN NNNN",
                    'I', new ItemStack(EndlessItems.infinityIngot.get()),
                    'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

            ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.skullfireSword.get()),
                    "       IX",
                    "      IXI",
                    "     IXI ",
                    "    IXI  ",
                    " B IXI   ",
                    "  BXI    ",
                    "  WB     ",
                    " W  B    ",
                    "D        ",
                    'I', new ItemStack(EndlessItems.crystalMatrixIngot.get()),
                    'X', new ItemStack(Items.BLAZE_POWDER),
                    'B', new ItemStack(Items.BONE),
                    'D', new ItemStack(Items.NETHER_STAR),
                    'W', ItemTags.LOGS);
        }


        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityArrow.get()),
                "XY       ",
                "YYYY     ",
                " YZ      ",
                " Y Z     ",
                "    Z    ",
                "     Z   ",
                "      AA ",
                "      AAA",
                "       A ",
                'X', new ItemStack(EndlessItems.infinityNugget.get()),
                'Y', new ItemStack(EndlessItems.infinityIngot.get()),
                'Z', new ItemStack(EndlessItems.neutroniumIngot.get()),
                'A', new ItemStack(EndlessItems.crystalMatrixIngot.get()));

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityCrossBow.get()),
                "  YYYYY  ",
                " AZ  X   ",
                "YZZZX    ",
                "Y ZBZ    ",
                "Y XZZZ   ",
                "YX  ZZ   ",
                "Y     Z  ",
                "       Z ",
                "         ",
                'X', new ItemStack(EndlessItems.infinityNugget.get()),
                'Y', new ItemStack(EndlessItems.infinityIngot.get()),
                'Z', new ItemStack(EndlessItems.neutroniumIngot.get()),
                'B', new ItemStack(EndlessItems.neutroniumGear.get()),
                'A', new ItemStack(EndlessItems.crystalMatrixBlock.get()));

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.endestPearl.get()),
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
                'N', new ItemStack(EndlessItems.neutroniumIngot.get()));

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.neutronCompressor.get()),
                "IIIHHHIII",
                "X N   N X",
                "I N   N I",
                "X N   N X",
                "RNN O NNR",
                "X N   N X",
                "I N   N I",
                "X N   N X",
                "IIIXIXIII",
                'X', new ItemStack(EndlessItems.crystalMatrixIngot.get()),
                'N', new ItemStack(EndlessItems.neutroniumIngot.get()),
                'I', new ItemStack(Blocks.IRON_BLOCK),
                'H', new ItemStack(Blocks.HOPPER),
                'R', new ItemStack(Blocks.REDSTONE_BLOCK),
                'O', new ItemStack(EndlessItems.neutroniumBlock.get()));
    }


    /**
     * 根据其他模组修改配方
     */
    public static void lastMinuteChanges() {
        if (Endless.isSpaceArms) { // 16+7 13+1 15+3
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(EndlessItems.singularityRuby.get()), new ItemStack(EndlessItems.singularityDragon.get()),
                    new ItemStack(EndlessItems.singularitySpace.get()), new ItemStack(EndlessItems.singularityXray.get()),
                    new ItemStack(EndlessItems.singularityUltra.get()), new ItemStack(SAItems.jiejing.get()),
                    new ItemStack(Items.BEDROCK));
            ExtremeCraftingManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(SAItems.superRabbitStew.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    new ItemStack(SAItems.superCookie.get()),
                    new ItemStack(SAItems.superBeetrootSoup.get()),
                    new ItemStack(SAItems.superPumpkinPie.get()));
            CompressorManager.addInputs(EndlessItems.singularityEmerald.get(),
                    getList(new ItemStack(SABlocks.emeraldIngotBlock.get(), 5)));
        }
        if (Endless.isPaimeng) { //23+1 14+26 18+22
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(PMItems.relicsBoxOne.get()));
            
            ExtremeCraftingManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(PMItems.paimengFood.get()),
                    new ItemStack(PMItems.xiantiaoqiang.get()),
                    new ItemStack(PMItems.cishenPinpan.get()),
                    new ItemStack(PMItems.daHuangjinShunzhiji.get()),
                    new ItemStack(PMItems.huangjinxie.get()),
                    new ItemStack(PMItems.huangyouXiexie.get()),
                    new ItemStack(PMItems.larouWowotou.get()),
                    new ItemStack(PMItems.tianshurou.get()),
                    new ItemStack(PMItems.lailaicai.get()),
                    new ItemStack(PMItems.zhongyuanZasui.get()),
                    new ItemStack(PMItems.jinsiXiaqiu.get()),
                    new ItemStack(PMItems.duigaogao.get()),
                    new ItemStack(PMItems.honghuiShourou.get()),
                    new ItemStack(PMItems.mijiangHuluoboJianrou.get()),
                    new ItemStack(PMItems.yurenTusi.get()),
                    new ItemStack(PMItems.dulaiQinrou.get()),
                    new ItemStack(PMItems.chuanchuanSanwei.get()),
                    new ItemStack(PMItems.feiyingTianfuluo.get()),
                    new ItemStack(PMItems.feiyingXiaxianbei.get()),
                    new ItemStack(PMItems.yangangSanxian.get()),
                    new ItemStack(PMItems.tiantianhuaNiangji.get()),
                    new ItemStack(PMItems.chouziShucaiDunrou.get()),
                    new ItemStack(PMItems.molarou.get()),
                    new ItemStack(PMItems.mengdeKaoyu.get()),
                    new ItemStack(PMItems.tiwateJiandan.get()),
                    new ItemStack(PMItems.yeguJirouchuan.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    new ItemStack(PMItems.bugFood.get()),
                    new ItemStack(PMItems.xiantiaoqiang.get()),
                    new ItemStack(PMItems.feiyuShijindai.get()),
                    new ItemStack(PMItems.jidouhua.get()),
                    new ItemStack(PMItems.yueliangpai.get()),
                    new ItemStack(PMItems.riluoguo.get()),
                    new ItemStack(PMItems.danbaofan.get()),
                    new ItemStack(PMItems.mengdeTudoubing.get()),
                    new ItemStack(PMItems.sifangHeping.get()),
                    new ItemStack(PMItems.sancaiTuanzi.get()),
                    new ItemStack(PMItems.tuanziNiunai.get()),
                    new ItemStack(PMItems.kousansi.get()),
                    new ItemStack(PMItems.mingyuedan.get()),
                    new ItemStack(PMItems.huangyouSongrong.get()),
                    new ItemStack(PMItems.mifanBuding.get()),
                    new ItemStack(PMItems.xingrenDoufu.get()),
                    new ItemStack(PMItems.manzuShala.get()),
                    new ItemStack(PMItems.ruozhuzhu.get()),
                    new ItemStack(PMItems.shijinChaomian.get()),
                    new ItemStack(PMItems.miwowo.get()),
                    new ItemStack(PMItems.boheGuodong.get()),
                    new ItemStack(PMItems.luoboShishutang.get()));
        }
        if (Endless.isEnchants) { //24+1 40 40
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(YEItems.BrokenMagicPearlSuper.get()));
        }
        if (Endless.isMoreCoals) { //25+1 40 40
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(MoreCoalItems.lavaCoal.get()));
        }
        if (Endless.isIAF){ //26+4 40+1 40+1
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(EndlessItems.singularitySilver.get()), new ItemStack(EndlessItems.singularityCopper.get()));
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
        if (Endless.isBOT){ //30+4 41 41+1
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(ModItems.gaiaIngot), new ItemStack(EndlessItems.singularityMana.get()),
                    new ItemStack(EndlessItems.singularityTara.get()), new ItemStack(EndlessItems.singularityElementIum.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew, new ItemStack(ModItems.manaCookie));
        }
        if (Endless.isAE2){ //34+3 41 42
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(Api.instance().definitions().items().cell64k()),
                    new ItemStack(Api.instance().definitions().items().fluidCell64k()),
                    new ItemStack(Api.instance().definitions().materials().singularity()));
        }
        if (Endless.isDE){ //37+3 41 42
            Block block = Registry.BLOCK.getOrDefault(new ResourceLocation("draconicevolution:chaos_shard")); //混沌碎片
            if (block != Blocks.AIR){
                ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst, new ItemStack(block),
                        new ItemStack(EndlessItems.singularityDragonIum.get()), new ItemStack(EndlessItems.singularityAwakenDragon.get()));
            }
        }
        if (Endless.isPE){ //40+2 41 42
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(EndlessItems.singularityDarkMatter.get()),
                    new ItemStack(EndlessItems.singularityRedMatter.get()));
        }
        if (Endless.isTTF){ //42+1 41+1 42+1
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(TFBlocks.ironwood_block.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(TFItems.hydra_chop.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    new ItemStack(TFItems.maze_wafer.get()));
        }
        if (Endless.isCreate){ //43+1 42 43+1
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(EndlessItems.singularityZinc.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    new ItemStack(AllItems.BAR_OF_CHOCOLATE.get()));
        }
        if (Endless.isAOA3){ //44+1 42+3 44+3
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(AoABlocks.SHYREGEM_BLOCK.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(AoAItems.COOKED_CHIMERA_CHOP.get()), new ItemStack(AoAItems.RAW_RAINBOWFISH.get()),
                    new ItemStack(AoAItems.COOKED_CHARGER_SHANK.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    new ItemStack(AoAItems.GOLDICAP_PETALS.get()), new ItemStack(AoAItems.MAGIC_MARANG.get()),
                    new ItemStack(AoAItems.NATURAL_TEA.get()));
        }
        if (Endless.isAS){ //45+1 45 47
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(BlocksAS.STARMETAL));
        }
        if (Endless.isSlashBlade2){ //46+1 45 47
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(SBItems.proudsoul_trapezohedron));
        }
        if (Endless.isMysticalAgriculture){ //47+2 45 47
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(com.blakebr0.mysticalagriculture.init.ModBlocks.SUPREMIUM_GEMSTONE_BLOCK.get()),
                    new ItemStack(com.blakebr0.mysticalagriculture.init.ModBlocks.SUPREMIUM_BLOCK.get()));
        }
        if (Endless.isThermal){ //49+1 45+3 47+2
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst, new ItemStack(ThermalCore.BLOCKS.get("enderium_block")),
                    new ItemStack(EndlessItems.singularityNickel.get()),
                    new ItemStack(EndlessItems.singularityLead.get()), new ItemStack(EndlessItems.singularityTin.get()));
            ExtremeCraftingManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(ThermalCore.ITEMS.get("stuffed_pepper")), new ItemStack(ThermalCore.ITEMS.get("sushi_maki")),
                    new ItemStack(ThermalCore.ITEMS.get("stuffed_pumpkin")));
            ExtremeCraftingManager.getInstance().addRecipeInput(stew,
                    new ItemStack(ThermalCore.ITEMS.get("xp_stew")), new ItemStack(ThermalCore.ITEMS.get("spring_salad")));
            CompressorManager.addInputs(EndlessItems.singularityGold.get(), getList(new ItemStack(ThermalCore.BLOCKS.get("electrum_block"), 2)));
            CompressorManager.addInputs(EndlessItems.singularityIron.get(), getList(new ItemStack(ThermalCore.BLOCKS.get("invar_block"), 2)));
        }
        if (Endless.isIAF && Endless.isThermal){ //50 48 49
            CompressorManager.addInputs(EndlessItems.singularityCopper.get(), getList(new ItemStack(ThermalCore.BLOCKS.get("copper_block")),
                    new ItemStack(ThermalCore.BLOCKS.get("bronze_block"), 3), new ItemStack(ThermalCore.BLOCKS.get("constantan_block"), 2)));
            CompressorManager.addInputs(EndlessItems.singularitySilver.get(), getList(new ItemStack(ThermalCore.BLOCKS.get("silver_block"))));
        }
        if (Endless.isRS){ //50+2 48 49
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(RSItems.ITEM_STORAGE_DISKS.get(ItemStorageType.SIXTY_FOUR_K).get()),
                    new ItemStack(RSItems.FLUID_STORAGE_DISKS.get(FluidStorageType.FOUR_THOUSAND_NINETY_SIX_K).get()));
        }
        if (Endless.isTC3){//52 48 49
            ExtremeCraftingManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(TinkerModifiers.dragonScale),
                    new ItemStack(EndlessItems.singularityCobalt.get()), new ItemStack(EndlessItems.singularityManyullyn.get()));
        }
        if (Endless.isIAF && Endless.isTC3){ //52 48 49
            CompressorManager.addInputs(EndlessItems.singularityCopper.get(), getList(new ItemStack(TinkerMaterials.copper)));
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
