package com.yuo.endless.Config;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Config {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ServerConfig SERVER;
    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        COMMON_CONFIG = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static Set<Block> pickaxeBlocks = new HashSet<>();
    public static Set<Block> axeBlocks = new HashSet<>();
    public static Set<Block> shovelBlocks = new HashSet<>();
    public static Set<Block> hoeBlocks = new HashSet<>();
    public static Set<String> errorInfo = new HashSet<>();

    public static void loadConfig(){
        getToolBlocks(SERVER.pickaxeBlackList.get(), pickaxeBlocks);
        getToolBlocks(SERVER.axeBlackList.get(), axeBlocks);
        getToolBlocks(SERVER.shovelBlackList.get(), shovelBlocks);
        getToolBlocks(SERVER.hoeBlackList.get(), hoeBlocks);
    }

    /**
     * 将方块添加到set
     * @param list 方块id
     * @param set 指定set集合
     */
    public static void getToolBlocks(List<? extends String> list, Set<Block> set){
        for (String s : list) {
            ResourceLocation resourceLocation = new ResourceLocation(s);
            Block block = Registry.BLOCK.getOrDefault(resourceLocation);
            if (block == Blocks.AIR){
                errorInfo.add("error block for["+ s + "]");
            }else set.add(block);
        }
    }

    public static class ServerConfig{

        public final ForgeConfigSpec.BooleanValue isKeepStone; //无尽工具的超级模式是否保留石头和泥土
        public final ForgeConfigSpec.BooleanValue isBreakBedrock; //无尽镐是否破坏基岩
        public final ForgeConfigSpec.BooleanValue isMergeMatterCluster; // 是否合并物质团
        public final ForgeConfigSpec.IntValue swordRangeDamage; //无尽剑右键的范围伤害值
        public final ForgeConfigSpec.IntValue swordAttackRange; //无尽剑右键攻击范围
        public final ForgeConfigSpec.BooleanValue isSwordAttackAnimal; // 无尽剑右键范围攻击是否攻击中立生物
        public final ForgeConfigSpec.IntValue subArrowDamage; // 无尽弓散射光箭伤害
        public final ForgeConfigSpec.IntValue axeChainCount; // 无尽斧砍树连锁数量
        public final ForgeConfigSpec.DoubleValue foodTime; //食物效果时间缩放系数
        public final ForgeConfigSpec.BooleanValue isRemoveBlock; //无尽镐的锤形态潜行左键是否删除方块
        public final ForgeConfigSpec.IntValue matterClusterMaxCount; //物质团单个物品存储上限
        public final ForgeConfigSpec.IntValue singularityCoal; // 奇点基础数量
        public final ForgeConfigSpec.IntValue singularityClay;
        public final ForgeConfigSpec.IntValue singularityIron;
        public final ForgeConfigSpec.IntValue singularityGold;
        public final ForgeConfigSpec.IntValue singularityDiamond;
        public final ForgeConfigSpec.IntValue singularityEmerald;
        public final ForgeConfigSpec.IntValue singularityNetherite;
        public final ForgeConfigSpec.IntValue singularityLapis;
        public final ForgeConfigSpec.IntValue singularityQuartz;
        public final ForgeConfigSpec.IntValue singularityRedstone;
        public final ForgeConfigSpec.IntValue singularitySilver;
        public final ForgeConfigSpec.IntValue singularityCopper;
        public final ForgeConfigSpec.IntValue singularityRuby;
        public final ForgeConfigSpec.IntValue singularityDragon;
        public final ForgeConfigSpec.IntValue singularitySpace;
        public final ForgeConfigSpec.IntValue singularityXray;
        public final ForgeConfigSpec.IntValue singularityUltra;
        public final ForgeConfigSpec.IntValue modRatioRate; //模组影响后的最大倍率
        public final ForgeConfigSpec.IntValue modRatioCount; //模组影响后的最大数量

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> pickaxeBlackList; //工具范围挖掘黑名单
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> axeBlackList;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> shovelBlackList;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> hoeBlackList;

        public ServerConfig(ForgeConfigSpec.Builder builder){
            builder.comment("Endless Base Config").push("general");
            this.isKeepStone = buildBoolean(builder, "Is Stone", true, "Does the super mode of endless tools retain stone and soil");
            this.isBreakBedrock = buildBoolean(builder, "Is Break Bedrock", true, "Whether the rock is damaged by the pickaxe");
            this.isMergeMatterCluster = buildBoolean(builder, "Is Merge Matter Cluster", false, "Whether to merge matter cluster");
            this.swordRangeDamage = buildInt(builder, "Sword Range Damage", 10000, 10, 100000, "Range damage value of the right key of endless sword");
            this.swordAttackRange = buildInt(builder, "Sword Attack Range", 32, 8, 64, "Endless sword right click attack range");
            this.isSwordAttackAnimal = buildBoolean(builder, "Is Sword Damage", true, "Does the right key range attack of endless sword attack neutral creatures");
            this.subArrowDamage = buildInt(builder, "Sub Arrow Damage", 10000, 10, 100000, "Endless bow scattering light arrow damage");
            this.axeChainCount = buildInt(builder, "Axe Chain Count", 64, 16, 128, "Chain number of endless axe cutting trees");
            this.foodTime = buildDouble(builder, "Food Time", 1d, 0.1d, 5d, "Food effect time scaling factor");
            this.isRemoveBlock = buildBoolean(builder, "Is Remove Block", false,  "Hammer form of endless pickaxe left click to delete box");
            this.matterClusterMaxCount = buildInt(builder, "Matter Cluster Max Count", 1024, 256, 4096, "Upper limit of storage of a single item in a material group");
            builder.pop();

            builder.comment("Singularity Recipe Count").push("singularity");
            this.singularityCoal = buildInt(builder, "Coal", 450, 10, 2000, "base count");
            this.singularityClay = buildInt(builder, "Clay", 400, 10, 2000, "base count");
            this.singularityIron = buildInt(builder, "Iron", 300, 10, 2000, "base count");
            this.singularityGold = buildInt(builder, "Gold", 350, 10, 2000, "base count");
            this.singularityDiamond = buildInt(builder, "Diamond", 250, 10, 2000, "base count");
            this.singularityEmerald = buildInt(builder, "Emerald", 200, 10, 2000, "base count");
            this.singularityNetherite = buildInt(builder, "Netherite", 150, 10, 2000, "base count");
            this.singularityLapis = buildInt(builder, "Lapis", 400, 10, 2000, "base count");
            this.singularityQuartz = buildInt(builder, "Quartz", 500, 10, 2000, "base count");
            this.singularityRedstone = buildInt(builder, "Redstone", 400, 10, 2000, "base count");
            this.singularitySilver = buildInt(builder, "Silver", 200, 10, 2000, "base count");
            this.singularityCopper = buildInt(builder, "Copper", 300, 10, 2000, "base count");
            this.singularityRuby = buildInt(builder, "Ruby", 250, 10, 2000, "base count");
            this.singularityDragon = buildInt(builder, "Dragon", 100, 10, 2000, "base count");
            this.singularitySpace = buildInt(builder, "Space", 50, 10, 2000, "base count");
            this.singularityXray = buildInt(builder, "Xray", 150, 10, 2000, "base count");
            this.singularityUltra = buildInt(builder, "Ultra", 80, 10, 2000, "base count");
            builder.pop();

            builder.comment("Mod Impact").push("modRatio");
            this.modRatioRate = buildInt(builder, "Mod Ratio Rate", 20, 5, 50, "Maximum magnification after module influence");
            this.modRatioCount = buildInt(builder, "Mod Ratio Count", 2000, 500, 5000, "Maximum number of modules affected");
            builder.pop();

            builder.comment("Tool Range Mining Black List").push("black list");
            this.pickaxeBlackList = buildConfig(builder, "Pickaxe Black List", "Tool range mining blacklist as pickaxe");
            this.axeBlackList = buildConfig(builder, "Axe Black List", "Tool range mining blacklist as pickaxe as axe");
            this.shovelBlackList = buildConfig(builder, "Shovel Black List", "Tool range mining blacklist as pickaxe as shovel");
            this.hoeBlackList = buildConfig(builder, "Hoe Black List", "Tool range mining blacklist as pickaxe as hoe");
            builder.pop();
        }
    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, int defaultValue, int min, int max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.DoubleValue buildDouble(ForgeConfigSpec.Builder builder, String name, double defaultValue, double min, double max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> buildConfig(ForgeConfigSpec.Builder builder, String name, String comment){
        return builder.comment(comment).translation(name).defineList(name, Collections.emptyList(), s -> s instanceof String && ResourceLocation.tryCreate((String) s) != null);
    }
}
