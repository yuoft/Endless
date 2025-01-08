package com.yuo.endless.Recipe;

import appeng.core.definitions.AEItems;
import cofh.thermal.core.ThermalCore;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.refinedmods.refinedstorage.RSItems;
import com.refinedmods.refinedstorage.apiimpl.storage.FluidStorageType;
import com.refinedmods.refinedstorage.apiimpl.storage.ItemStorageType;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.yuo.Enchants.Items.YEItems;
import com.yuo.endless.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Singularity;
import mods.flammpfeil.slashblade.init.SBItems;
import moze_intel.projecte.gameObjs.registries.PEBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
    public static ExtremeCraftShapeRecipe eternalSingularity; //永恒奇点
    public static ExtremeCraftShapeRecipe meatBalls; //寰宇肉丸
    public static ExtremeCraftShapeRecipe stew; //超级煲

    /**
     * 匹配硬编码配方
     * @param input 输入
     * @param world 世界
     * @return 匹配上的配方
     */
    public static ExtremeCraftShapeRecipe matchesRecipe(Container input, Level world){
        ExtremeCraftShapeRecipe[] recipes = new ExtremeCraftShapeRecipe[]{
                infinityCatalyst, eternalSingularity, meatBalls, stew
        };
        for (ExtremeCraftShapeRecipe shapeRecipe : recipes) {
            boolean matches = shapeRecipe.matches(input, world);
            if (matches) return shapeRecipe;
        }

        return null;
    }

    //压缩机所需矿物块数量
    public static void addCompressorCraft() {
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
        int countEnd = Math.min(Config.SERVER.modRatioCount.get(), count);
        int rateEnd = Math.min(Config.SERVER.modRatioRate.get(), rate);

        if (Endless.isIAF){
            CompressorManager.addRecipe(Singularity.getSingularity("silver"), (Config.SERVER.singularitySilver.get() + countEnd) * rateEnd,
                    getList(new ItemStack(IafBlockRegistry.SILVER_BLOCK.get())));
            CompressorManager.addRecipe(Singularity.getSingularity("copper"), (Config.SERVER.singularityCopper.get() + countEnd) * rateEnd,
                    getList(new ItemStack(IafBlockRegistry.COPPER_BLOCK.get())));
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
            Block block0 = Registry.BLOCK.get(new ResourceLocation(str0));
            Block block1 = Registry.BLOCK.get(new ResourceLocation(str1));
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

    //无尽工作台无序配方
    public static void addExtremeCraftShape(){
        //无尽催化剂
        infinityCatalyst = ExtremeCraftShpaelessManager.getInstance().addShapelessRecipe(new ItemStack(EndlessItems.infinityCatalyst.get()),
                new ItemStack(EndlessItems.crystalMatrixIngot.get()), new ItemStack(EndlessItems.neutroniumIngot.get()),
                new ItemStack(EndlessItems.cosmicMeatBalls.get()), new ItemStack(EndlessItems.ultimateStew.get()),
                new ItemStack(EndlessItems.endestPearl.get()), new ItemStack(EndlessItems.recordFragment.get()),
                new ItemStack(EndlessItems.eternalSingularity.get()));

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

        eternalSingularity = ExtremeCraftShpaelessManager.getInstance().addShapelessRecipe(new ItemStack(EndlessItems.eternalSingularity.get()),
                Singularity.getSingularity("coal"), Singularity.getSingularity("iron"),
                Singularity.getSingularity("gold"), Singularity.getSingularity("diamond"),
                Singularity.getSingularity("netherite"), Singularity.getSingularity("emerald"),
                Singularity.getSingularity("lapis"), Singularity.getSingularity("redstone"),
                Singularity.getSingularity("quartz"), Singularity.getSingularity("clay"));
    }

    /**
     * 根据其他模组修改配方
     */
    public static void lastMinuteChanges() {
        if (Endless.isEnchants) {
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(YEItems.SuperBrokenMagicPearl.get()));
        }
        if (Endless.isIAF){
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(eternalSingularity,
                    Singularity.getSingularity("silver"), Singularity.getSingularity("copper"));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    Ingredient.of(new ItemStack(IafItemRegistry.AMBROSIA.get())), Ingredient.of(
                            new ItemStack(IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK.get()), new ItemStack(IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK.get()),
                            new ItemStack(IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK.get())));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(meatBalls,
                    Ingredient.of(new ItemStack(IafItemRegistry.FIRE_DRAGON_FLESH.get()), new ItemStack(IafItemRegistry.ICE_DRAGON_FLESH.get()),
                            new ItemStack(IafItemRegistry.LIGHTNING_DRAGON_FLESH.get())));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(stew,
                    Ingredient.of(new ItemStack(IafItemRegistry.FIRE_STEW.get()), new ItemStack(IafItemRegistry.FROST_STEW.get()),
                            new ItemStack(IafItemRegistry.LIGHTNING_STEW.get())));
        }
        if (Endless.isBOT){
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst, new ItemStack(ModItems.gaiaIngot));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(eternalSingularity, Singularity.getSingularity("manasteel"),
                    Singularity.getSingularity("terrasteel"), Singularity.getSingularity("elementium"));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(stew, new ItemStack(ModItems.manaCookie));
        }
        if (Endless.isAE2){
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(AEItems.ITEM_CELL_64K), new ItemStack(AEItems.FLUID_CELL_64K),
                    new ItemStack(AEItems.SINGULARITY));
        }
        if (Endless.isDE){
            Item item = Registry.ITEM.get(new ResourceLocation("draconicevolution:chaos_shard")); //混沌碎片
            if (item != Items.AIR){
                ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst, new ItemStack(item));
                ExtremeCraftShpaelessManager.getInstance().addRecipeInput(eternalSingularity, Singularity.getSingularity("draconium"),
                        Singularity.getSingularity("awakened_draconium"));
            }
        }
        if (Endless.isPE){
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(eternalSingularity,
                    Singularity.getSingularity("dark_matter"), Singularity.getSingularity("red_matter"));
        }
        if (Endless.isTTF){
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(TFBlocks.IRONWOOD_BLOCK.get()));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(meatBalls,
                    new ItemStack(TFItems.HYDRA_CHOP.get()));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(stew,
                    new ItemStack(TFItems.MAZE_MAP.get()));
        }
        if (Endless.isCreate){
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(eternalSingularity, Singularity.getSingularity("zinc"));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(stew,
                    new ItemStack(AllItems.BAR_OF_CHOCOLATE.get()));
        }
        if (Endless.isSlashBlade2){
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(SBItems.proudsoul_trapezohedron));
        }
        if (Endless.isMysticalAgriculture){
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(com.blakebr0.mysticalagriculture.init.ModBlocks.SUPREMIUM_GEMSTONE_BLOCK.get()),
                    new ItemStack(com.blakebr0.mysticalagriculture.init.ModBlocks.SUPREMIUM_BLOCK.get()));
        }
        if (Endless.isThermal){
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst, new ItemStack(ThermalCore.BLOCKS.get("enderium_block")));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(eternalSingularity, Singularity.getSingularity("nickel"),
                    Singularity.getSingularity("lead"), Singularity.getSingularity("tin"));
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
        if (Endless.isIAF && Endless.isThermal){
            CompressorManager.addInputs(Singularity.getSingularity("copper"),
                    getList(new ItemStack(ThermalCore.BLOCKS.get("copper_block")),
                    new ItemStack(ThermalCore.BLOCKS.get("bronze_block"), 3), new ItemStack(ThermalCore.BLOCKS.get("constantan_block"), 2)));
            CompressorManager.addInputs(Singularity.getSingularity("silver"),
                    getList(new ItemStack(ThermalCore.BLOCKS.get("silver_block"))));
        }
        if (Endless.isRS){
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst,
                    new ItemStack(RSItems.ITEM_STORAGE_DISKS.get(ItemStorageType.SIXTY_FOUR_K).get()),
                    new ItemStack(RSItems.FLUID_STORAGE_DISKS.get(FluidStorageType.FOUR_THOUSAND_NINETY_SIX_K).get()));
        }
        if (Endless.isTC3){//52 48 49
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(infinityCatalyst, new ItemStack(TinkerModifiers.dragonScale));
            ExtremeCraftShpaelessManager.getInstance().addRecipeInput(eternalSingularity, Singularity.getSingularity("cobalt"),
                    Singularity.getSingularity("manyullyn"));
        }
        if (Endless.isIAF && Endless.isTC3){ //52 48 49
            CompressorManager.addInputs(Singularity.getSingularity("copper"),
                    getList(new ItemStack(TinkerMaterials.copperNugget)));
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
