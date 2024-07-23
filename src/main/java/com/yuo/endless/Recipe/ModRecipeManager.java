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
import com.yuo.endless.Items.Singularity;
import com.yuo.morecoal.Items.MoreCoalItems;
import com.yuo.spacearms.Blocks.SABlocks;
import com.yuo.spacearms.Items.SAItems;
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

    public static ExtremeCraftShapeRecipe infinityCatalyst; //催化剂
    public static ExtremeCraftShapeRecipe meatBalls; //寰宇肉丸
    public static ExtremeCraftShapeRecipe stew; //超级煲

    //压缩机所需矿物块数量
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
        int countEnd = Math.min(Config.SERVER.modRatioCount.get(), count);
        int rateEnd = Math.min(Config.SERVER.modRatioRate.get(), rate);

        if (Endless.isSpaceArms) {
            CompressorManager.addRecipe(Singularity.getSingularity("ruby"), (Config.SERVER.singularityRuby.get() + countEnd) * rateEnd,
                    getList(new ItemStack(SABlocks.rubyBlock.get())) );
            CompressorManager.addRecipe(Singularity.getSingularity("dragon"), (Config.SERVER.singularityDragon.get() + countEnd) * rateEnd,
                    getList(new ItemStack(SABlocks.dragonBlock.get())));
            CompressorManager.addRecipe(Singularity.getSingularity("space"), (Config.SERVER.singularitySpace.get() + countEnd) * rateEnd,
                    getList(new ItemStack(SABlocks.spaceBlock.get())));
            CompressorManager.addRecipe(Singularity.getSingularity("xray"), (Config.SERVER.singularityXray.get() + countEnd) * rateEnd,
                    getList(new ItemStack(SABlocks.xrayBlock.get()), new ItemStack(SABlocks.superXrayBlock.get(), 5)));
            CompressorManager.addRecipe(Singularity.getSingularity("ultra"), (Config.SERVER.singularityUltra.get() + countEnd) * rateEnd,
                    getList(new ItemStack(SABlocks.superBlock.get()), new ItemStack(SABlocks.ultraBlock.get(), 5)));
        }
        if (Endless.isIAF){
            CompressorManager.addRecipe(Singularity.getSingularity("silver"), (Config.SERVER.singularitySilver.get() + countEnd) * rateEnd,
                    getList(new ItemStack(IafBlockRegistry.SILVER_BLOCK)));
            CompressorManager.addRecipe(Singularity.getSingularity("copper"), (Config.SERVER.singularityCopper.get() + countEnd) * rateEnd,
                    getList(new ItemStack(IafBlockRegistry.COPPER_BLOCK)));
        }
        if (Endless.isCreate){
            CompressorManager.addRecipe(Singularity.getSingularity("zinc"), (Config.SERVER.singularityZinc.get() + countEnd) * rateEnd,
                    getList(new ItemStack(AllBlocks.ZINC_BLOCK.get())));
        }
        if (Endless.isThermal){
//            CompressorManager.addRecipe(new ItemStack(ItemRegistry.singularityPlatinum.get()), (Config.SERVER.singularityPlatinum.get() + countEnd) * rateEnd,
//                    getList(new ItemStack(ThermalCore.BLOCKS.get("platinum_block"))));
            CompressorManager.addRecipe(Singularity.getSingularity("nickel"), (Config.SERVER.singularityNickel.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ThermalCore.BLOCKS.get("nickel_block"))));
            CompressorManager.addRecipe(Singularity.getSingularity("lead"), (Config.SERVER.singularityLead.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ThermalCore.BLOCKS.get("lead_block"))));
            CompressorManager.addRecipe(Singularity.getSingularity("tin"), (Config.SERVER.singularityTin.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ThermalCore.BLOCKS.get("tin_block"))));
        }
        if (Endless.isDE){
            String str0 = "draconicevolution:draconium_block"; //龙块
            String str1 = "draconicevolution:awakened_draconium_block"; //觉醒龙块
            Block block0 = Registry.BLOCK.getOrDefault(new ResourceLocation(str0));
            Block block1 = Registry.BLOCK.getOrDefault(new ResourceLocation(str1));
            if (block0 != Blocks.AIR){
                CompressorManager.addRecipe(Singularity.getSingularity("draconium"), (Config.SERVER.singularityDragonIum.get() + countEnd) * rateEnd,
                        getList(new ItemStack(block0)));
            }
            if (block1 != Blocks.AIR){
                CompressorManager.addRecipe(Singularity.getSingularity("awakened_draconium"), (Config.SERVER.singularityAwakenDragon.get() + countEnd) * rateEnd,
                        getList(new ItemStack(block1)));
            }
        }
        if (Endless.isBOT){
            CompressorManager.addRecipe(Singularity.getSingularity("manasteel"), (Config.SERVER.singularityMana.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ModBlocks.manasteelBlock)));
            CompressorManager.addRecipe(Singularity.getSingularity("terrasteel"), (Config.SERVER.singularityTara.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ModBlocks.terrasteelBlock)));
            CompressorManager.addRecipe(Singularity.getSingularity("elementium"), (Config.SERVER.singularityElementIum.get() + countEnd) * rateEnd,
                    getList(new ItemStack(ModBlocks.elementiumBlock)));
        }
        if (Endless.isPE){
            CompressorManager.addRecipe(Singularity.getSingularity("dark_matter"), (Config.SERVER.singularityDarkMatter.get() + countEnd) * rateEnd,
                    getList(new ItemStack(PEBlocks.DARK_MATTER)));
            CompressorManager.addRecipe(Singularity.getSingularity("red_matter"), (Config.SERVER.singularityRedMatter.get() + countEnd) * rateEnd,
                    getList(new ItemStack(PEBlocks.RED_MATTER)));
        }
        if (Endless.isTC3){
            CompressorManager.addRecipe(Singularity.getSingularity("cobalt"), (Config.SERVER.singularityCobalt.get() + countEnd) * rateEnd,
                    getList(new ItemStack(TinkerMaterials.cobalt)));
            CompressorManager.addRecipe(Singularity.getSingularity("manyullyn"), (Config.SERVER.singularityManyullyn.get() + countEnd) * rateEnd,
                    getList(new ItemStack(TinkerMaterials.manyullyn)));
        }

        //奇点合成配方
        CompressorManager.addRecipe(Singularity.getSingularity("coal"), (Config.SERVER.singularityCoal.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.COAL_BLOCK)));
        CompressorManager.addRecipe(Singularity.getSingularity("iron"), (Config.SERVER.singularityIron.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.IRON_BLOCK)));
        CompressorManager.addRecipe(Singularity.getSingularity("gold"), (Config.SERVER.singularityGold.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.GOLD_BLOCK)));
        CompressorManager.addRecipe(Singularity.getSingularity("diamond"), (Config.SERVER.singularityDiamond.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.DIAMOND_BLOCK)));
        CompressorManager.addRecipe(Singularity.getSingularity("netherite"), (Config.SERVER.singularityNetherite.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.NETHERITE_BLOCK)));
        CompressorManager.addRecipe(Singularity.getSingularity("emerald"), (Config.SERVER.singularityEmerald.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.EMERALD_BLOCK)));
        CompressorManager.addRecipe(Singularity.getSingularity("lapis"), (Config.SERVER.singularityLapis.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.LAPIS_BLOCK)));
        CompressorManager.addRecipe(Singularity.getSingularity("redstone"), (Config.SERVER.singularityRedstone.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.REDSTONE_BLOCK)));
        CompressorManager.addRecipe(Singularity.getSingularity("quartz"), (Config.SERVER.singularityQuartz.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.QUARTZ_BLOCK)));
        CompressorManager.addRecipe(Singularity.getSingularity("clay"), (Config.SERVER.singularityClay.get() + countEnd) * rateEnd,
                getList(new ItemStack(Blocks.CLAY)));
    }

    //无尽工作台有序配方
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

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityBucket.get()),
                "II     II",
                "IIO   OII",
                "IAAO OAAI",
                "IAAX XAAI",
                "IAAX XAAI",
                "IAAXXXAAI",
                "IIAAAAAII",
                " IIAAAII ",
                "  IIIII  ",
                'X', new ItemStack(EndlessItems.infinityIngot.get()),
                'I', new ItemStack(EndlessItems.neutroniumIngot.get()),
                'A', new ItemStack(Items.BUCKET),
                'O', new ItemStack(EndlessItems.infinityCatalyst.get()));

        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.infinityTotem.get()),
                "   AAA   ",
                "  ABBBA  ",
                "  ACBCA  ",
                "DDDBBBDDD",
                " DDBBBDD ",
                "  ABBBA  ",
                "  AAAAA  ",
                "   DDD   ",
                "    D    ",
                'A', new ItemStack(EndlessItems.neutroniumIngot.get()),
                'B', new ItemStack(EndlessItems.infinityNugget.get()),
                'C', new ItemStack(Items.TOTEM_OF_UNDYING),
                'D', new ItemStack(EndlessItems.crystalMatrixIngot.get()));
        ExtremeCraftingManager.getInstance().addRecipe(new ItemStack(EndlessItems.densestNeutroniumCollector.get()),
                "CC     CC",
                "C  BBB  C",
                "  AAAAA  ",
                " BAXXXAB ",
                " BAXYXAB ",
                " BAXXXAB ",
                "  AAAAA  ",
                "C  BBB  C",
                "CC     CC",
                'A', new ItemStack(Items.REDSTONE_BLOCK),
                'B', new ItemStack(EndlessItems.neutroniumIngot.get()),
                'C', new ItemStack(EndlessItems.neutroniumGear.get()),
                'X', new ItemStack(EndlessItems.denserNeutroniumCollector.get()),
                'Y', Singularity.getSingularity("redstone"));
    }

    //无尽工作台无序配方
    public static void addExtremeCraftShape(){
        //无尽催化剂
        infinityCatalyst = ExtremeCraftShpaelessManager.getInstance().addShapelessRecipe(new ItemStack(EndlessItems.infinityCatalyst.get()),
                new ItemStack(EndlessItems.crystalMatrixIngot.get()), new ItemStack(EndlessItems.neutroniumIngot.get()),
                new ItemStack(EndlessItems.cosmicMeatBalls.get()), new ItemStack(EndlessItems.ultimateStew.get()),
                new ItemStack(EndlessItems.endestPearl.get()), new ItemStack(EndlessItems.recordFragment.get()),
                Singularity.getSingularity("coal"), Singularity.getSingularity("iron"),
                Singularity.getSingularity("gold"), Singularity.getSingularity("diamond"),
                Singularity.getSingularity("netherite"), Singularity.getSingularity("emerald"),
                Singularity.getSingularity("lapis"), Singularity.getSingularity("redstone"),
                Singularity.getSingularity("quartz"), Singularity.getSingularity("clay"));

        if (Endless.isPaimeng || Endless.isSpaceArms) {
            meatBalls = ExtremeCraftShpaelessManager.getInstance().addShapelessRecipe(new ItemStack(EndlessItems.cosmicMeatBalls.get()),
                    new ItemStack(EndlessItems.neutroniumNugget.get()), new ItemStack(Items.PORKCHOP), new ItemStack(Items.BEEF),
                    new ItemStack(Items.MUTTON), new ItemStack(Items.COD), new ItemStack(Items.SALMON), new ItemStack(Items.TROPICAL_FISH),
                    new ItemStack(Items.PUFFERFISH), new ItemStack(Items.RABBIT), new ItemStack(Items.CHICKEN),
                    new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.EGG));

            stew = ExtremeCraftShpaelessManager.getInstance().addShapelessRecipe(new ItemStack(EndlessItems.ultimateStew.get()),
                    new ItemStack(EndlessItems.neutroniumNugget.get()), new ItemStack(Items.APPLE), new ItemStack(Items.GOLDEN_APPLE),
                    new ItemStack(Items.BREAD), new ItemStack(Items.KELP), new ItemStack(Items.COCOA_BEANS), new ItemStack(Blocks.CAKE),
                    new ItemStack(Items.GLISTERING_MELON_SLICE), new ItemStack(Items.CARROT), new ItemStack(Items.POISONOUS_POTATO),
                    new ItemStack(Items.CHORUS_FRUIT), new ItemStack(Items.BEETROOT), new ItemStack(Items.MUSHROOM_STEW),
                    new ItemStack(Items.HONEY_BOTTLE), new ItemStack(Items.SWEET_BERRIES));
        } else {
            meatBalls = ExtremeCraftShpaelessManager.getInstance().addShapelessRecipe(new ItemStack(EndlessItems.cosmicMeatBalls.get()),
                    new ItemStack(EndlessItems.neutroniumNugget.get()), new ItemStack(Items.PORKCHOP), new ItemStack(Items.COOKED_PORKCHOP),
                    new ItemStack(Items.BEEF), new ItemStack(Items.COOKED_BEEF), new ItemStack(Items.MUTTON), new ItemStack(Items.COOKED_MUTTON),
                    new ItemStack(Items.COD), new ItemStack(Items.COOKED_COD), new ItemStack(Items.SALMON), new ItemStack(Items.COOKED_SALMON),
                    new ItemStack(Items.TROPICAL_FISH), new ItemStack(Items.PUFFERFISH), new ItemStack(Items.RABBIT), new ItemStack(Items.RABBIT_STEW),
                    new ItemStack(Items.COOKED_RABBIT), new ItemStack(Items.CHICKEN), new ItemStack(Items.COOKED_CHICKEN),
                    new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.EGG));

            stew = ExtremeCraftShpaelessManager.getInstance().addShapelessRecipe(new ItemStack(EndlessItems.ultimateStew.get()),
                    new ItemStack(EndlessItems.neutroniumNugget.get()), new ItemStack(Items.APPLE), new ItemStack(Items.GOLDEN_APPLE),
                    new ItemStack(Items.WHEAT), new ItemStack(Items.BREAD), new ItemStack(Items.KELP), new ItemStack(Items.DRIED_KELP),
                    new ItemStack(Items.COCOA_BEANS), new ItemStack(Items.COOKIE), new ItemStack(Items.MELON_SLICE),
                    new ItemStack(Items.GLISTERING_MELON_SLICE), new ItemStack(Items.CARROT), new ItemStack(Items.POTATO),
                    new ItemStack(Items.BAKED_POTATO), new ItemStack(Items.POISONOUS_POTATO), new ItemStack(Items.CHORUS_FRUIT),
                    new ItemStack(Blocks.CAKE), new ItemStack(Items.PUMPKIN_PIE), new ItemStack(Items.BEETROOT),
                    new ItemStack(Items.BEETROOT_SOUP), new ItemStack(Items.MUSHROOM_STEW), new ItemStack(Items.HONEY_BOTTLE),
                    new ItemStack(Items.SWEET_BERRIES));
        }
    }

    /**
     * 根据其他模组修改配方
     */
    public static void lastMinuteChanges() {
        if (Endless.isSpaceArms) { // 16+7 13+1 15+3
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    Singularity.getSingularity("ruby"), Singularity.getSingularity("dragon"), Singularity.getSingularity("space"),
                    Singularity.getSingularity("xray"), Singularity.getSingularity("ultra"), new ItemStack(SAItems.jiejing.get()),
                    new ItemStack(Items.BEDROCK));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(SAItems.superRabbitStew.get()));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(stew,
                    new ItemStack(SAItems.superCookie.get()), new ItemStack(SAItems.superBeetrootSoup.get()), new ItemStack(SAItems.superPumpkinPie.get()));
            CompressorManager.addInputs(Singularity.getSingularity("emerald"),
                    getList(new ItemStack(SABlocks.emeraldIngotBlock.get(), 5)));
        }
        if (Endless.isPaimeng) { //23+1 14+26 18+22
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(PMItems.relicsBoxOne.get()));

            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(PMItems.paimengFood.get()), new ItemStack(PMItems.xiantiaoqiang.get()),
                    new ItemStack(PMItems.cishenPinpan.get()), new ItemStack(PMItems.daHuangjinShunzhiji.get()),
                    new ItemStack(PMItems.huangjinxie.get()), new ItemStack(PMItems.huangyouXiexie.get()),
                    new ItemStack(PMItems.larouWowotou.get()), new ItemStack(PMItems.tianshurou.get()),
                    new ItemStack(PMItems.lailaicai.get()), new ItemStack(PMItems.zhongyuanZasui.get()),
                    new ItemStack(PMItems.jinsiXiaqiu.get()), new ItemStack(PMItems.duigaogao.get()),
                    new ItemStack(PMItems.honghuiShourou.get()), new ItemStack(PMItems.mijiangHuluoboJianrou.get()),
                    new ItemStack(PMItems.yurenTusi.get()), new ItemStack(PMItems.dulaiQinrou.get()),
                    new ItemStack(PMItems.chuanchuanSanwei.get()), new ItemStack(PMItems.feiyingTianfuluo.get()),
                    new ItemStack(PMItems.feiyingXiaxianbei.get()), new ItemStack(PMItems.yangangSanxian.get()),
                    new ItemStack(PMItems.tiantianhuaNiangji.get()), new ItemStack(PMItems.chouziShucaiDunrou.get()),
                    new ItemStack(PMItems.molarou.get()), new ItemStack(PMItems.mengdeKaoyu.get()),
                    new ItemStack(PMItems.tiwateJiandan.get()), new ItemStack(PMItems.yeguJirouchuan.get()));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(stew,
                    new ItemStack(PMItems.bugFood.get()), new ItemStack(PMItems.xiantiaoqiang.get()),
                    new ItemStack(PMItems.feiyuShijindai.get()), new ItemStack(PMItems.jidouhua.get()),
                    new ItemStack(PMItems.yueliangpai.get()), new ItemStack(PMItems.riluoguo.get()),
                    new ItemStack(PMItems.danbaofan.get()), new ItemStack(PMItems.mengdeTudoubing.get()),
                    new ItemStack(PMItems.sifangHeping.get()), new ItemStack(PMItems.sancaiTuanzi.get()),
                    new ItemStack(PMItems.tuanziNiunai.get()), new ItemStack(PMItems.kousansi.get()),
                    new ItemStack(PMItems.mingyuedan.get()), new ItemStack(PMItems.huangyouSongrong.get()),
                    new ItemStack(PMItems.mifanBuding.get()), new ItemStack(PMItems.xingrenDoufu.get()),
                    new ItemStack(PMItems.manzuShala.get()), new ItemStack(PMItems.ruozhuzhu.get()),
                    new ItemStack(PMItems.shijinChaomian.get()), new ItemStack(PMItems.miwowo.get()),
                    new ItemStack(PMItems.boheGuodong.get()), new ItemStack(PMItems.luoboShishutang.get()));
        }
        if (Endless.isEnchants) { //24+1 40 40
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(YEItems.SuperBrokenMagicPearl.get()));
        }
        if (Endless.isMoreCoals) { //25+1 40 40
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(MoreCoalItems.lavaCoal.get()));
        }
        if (Endless.isIAF){ //26+4 40+1 40+1
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    Singularity.getSingularity("silver"), Singularity.getSingularity("copper"));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    Ingredient.fromStacks(new ItemStack(IafItemRegistry.AMBROSIA)), Ingredient.fromStacks(
                            new ItemStack(IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK), new ItemStack(IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK),
                            new ItemStack(IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK)));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(meatBalls,
                    Ingredient.fromStacks(new ItemStack(IafItemRegistry.FIRE_DRAGON_FLESH), new ItemStack(IafItemRegistry.ICE_DRAGON_FLESH),
                            new ItemStack(IafItemRegistry.LIGHTNING_DRAGON_FLESH)));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(stew,
                    Ingredient.fromStacks(new ItemStack(IafItemRegistry.FIRE_STEW), new ItemStack(IafItemRegistry.FROST_STEW),
                            new ItemStack(IafItemRegistry.LIGHTNING_STEW)));
        }
        if (Endless.isBOT){ //30+4 41 41+1
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(ModItems.gaiaIngot), Singularity.getSingularity("manasteel"),
                    Singularity.getSingularity("terrasteel"), Singularity.getSingularity("elementium"));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(stew, new ItemStack(ModItems.manaCookie));
        }
        if (Endless.isAE2){ //34+3 41 42
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(Api.instance().definitions().items().cell64k()), new ItemStack(Api.instance().definitions().items().fluidCell64k()),
                    new ItemStack(Api.instance().definitions().materials().singularity()));
        }
        if (Endless.isDE){ //37+3 41 42
            Item item = Registry.ITEM.getOrDefault(new ResourceLocation("draconicevolution:chaos_shard")); //混沌碎片
            if (item != Items.AIR){
                ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst, new ItemStack(item),
                        Singularity.getSingularity("draconium"), Singularity.getSingularity("awakened_draconium"));
            }
        }
        if (Endless.isPE){ //40+2 41 42
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    Singularity.getSingularity("dark_matter"), Singularity.getSingularity("red_matter"));
        }
        if (Endless.isTTF){ //42+1 41+1 42+1
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(TFBlocks.ironwood_block.get()));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(TFItems.hydra_chop.get()));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(stew,
                    new ItemStack(TFItems.maze_wafer.get()));
        }
        if (Endless.isCreate){ //43+1 42 43+1
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst, Singularity.getSingularity("zinc"));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(stew,
                    new ItemStack(AllItems.BAR_OF_CHOCOLATE.get()));
        }
        if (Endless.isAOA3){ //44+1 42+3 44+3
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(AoABlocks.SHYREGEM_BLOCK.get()));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(AoAItems.COOKED_CHIMERA_CHOP.get()), new ItemStack(AoAItems.RAW_RAINBOWFISH.get()),
                    new ItemStack(AoAItems.COOKED_CHARGER_SHANK.get()));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(stew,
                    new ItemStack(AoAItems.GOLDICAP_PETALS.get()), new ItemStack(AoAItems.MAGIC_MARANG.get()),
                    new ItemStack(AoAItems.NATURAL_TEA.get()));
        }
        if (Endless.isAS){ //45+1 45 47
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(BlocksAS.STARMETAL));
        }
        if (Endless.isSlashBlade2){ //46+1 45 47
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(SBItems.proudsoul_trapezohedron));
        }
        if (Endless.isMysticalAgriculture){ //47+2 45 47
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(com.blakebr0.mysticalagriculture.init.ModBlocks.SUPREMIUM_GEMSTONE_BLOCK.get()),
                    new ItemStack(com.blakebr0.mysticalagriculture.init.ModBlocks.SUPREMIUM_BLOCK.get()));
        }
        if (Endless.isThermal){ //49+1 45+3 47+2
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst, new ItemStack(ThermalCore.BLOCKS.get("enderium_block")),
                    Singularity.getSingularity("nickel"), Singularity.getSingularity("lead"), Singularity.getSingularity("tin"));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(ThermalCore.ITEMS.get("stuffed_pepper")), new ItemStack(ThermalCore.ITEMS.get("sushi_maki")),
                    new ItemStack(ThermalCore.ITEMS.get("stuffed_pumpkin")));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(stew,
                    new ItemStack(ThermalCore.ITEMS.get("xp_stew")), new ItemStack(ThermalCore.ITEMS.get("spring_salad")));
            CompressorManager.addInputs(Singularity.getSingularity("gold"),
                    getList(new ItemStack(ThermalCore.BLOCKS.get("electrum_block"), 2)));
            CompressorManager.addInputs(Singularity.getSingularity("iron"),
                    getList(new ItemStack(ThermalCore.BLOCKS.get("invar_block"), 2)));
        }
        if (Endless.isIAF && Endless.isThermal){ //50 48 49
            CompressorManager.addInputs(Singularity.getSingularity("copper"),
                    getList(new ItemStack(ThermalCore.BLOCKS.get("copper_block")),
                    new ItemStack(ThermalCore.BLOCKS.get("bronze_block"), 3), new ItemStack(ThermalCore.BLOCKS.get("constantan_block"), 2)));
            CompressorManager.addInputs(Singularity.getSingularity("silver"),
                    getList(new ItemStack(ThermalCore.BLOCKS.get("silver_block"))));
        }
        if (Endless.isRS){ //50+2 48 49
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(RSItems.ITEM_STORAGE_DISKS.get(ItemStorageType.SIXTY_FOUR_K).get()),
                    new ItemStack(RSItems.FLUID_STORAGE_DISKS.get(FluidStorageType.FOUR_THOUSAND_NINETY_SIX_K).get()));
        }
        if (Endless.isTC3){//52 48 49
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(TinkerModifiers.dragonScale), Singularity.getSingularity("cobalt"), Singularity.getSingularity("manyullyn"));
        }
        if (Endless.isIAF && Endless.isTC3){ //52 48 49
            CompressorManager.addInputs(Singularity.getSingularity("copper"),
                    getList(new ItemStack(TinkerMaterials.copper)));
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
