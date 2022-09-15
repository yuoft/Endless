package com.yuo.endless.Config;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class Config {
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ServerConfig SERVER;
    public static ClientConfig CLIENT;

    static {
        {
            final Pair<ClientConfig, ForgeConfigSpec> specPair1 = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
            CLIENT = specPair1.getLeft();
            CLIENT_CONFIG = specPair1.getRight();
        }
        {
            final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
            SERVER_CONFIG = specPair.getRight();
            SERVER = specPair.getLeft();
        }
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
        public final ForgeConfigSpec.IntValue noArrowDamage; // 无尽弓,弩空射箭矢伤害
        public final ForgeConfigSpec.IntValue axeChainCount; // 无尽斧砍树连锁数量
        public final ForgeConfigSpec.DoubleValue foodTime; //食物效果时间缩放系数
        public final ForgeConfigSpec.BooleanValue isRemoveBlock; //无尽镐的锤形态潜行左键是否删除方块
        public final ForgeConfigSpec.IntValue matterClusterMaxCount; //物质团单个物品存储上限
        public final ForgeConfigSpec.IntValue matterClusterMaxTerm; //物质团存储物品项上限
        public final ForgeConfigSpec.IntValue endestPearlEndDamage; //终望珍珠最终爆炸伤害
        public final ForgeConfigSpec.IntValue endestPearlOneDamage; //终望珍珠单次吸引伤害
        public final ForgeConfigSpec.IntValue endestPearlSuckRange; //终望珍珠引力范围
        public final ForgeConfigSpec.IntValue infinityArmorBearDamage; //无尽武器攻击全套无尽装备玩家时，造成伤害值
        public final ForgeConfigSpec.IntValue infinityBearDamage; //无尽武器攻击全套无尽装备和持有无尽武器玩家时，造成伤害值
        public final ForgeConfigSpec.IntValue infinityFireworkDamage; //无尽弩发射烟花伤害
        public final ForgeConfigSpec.BooleanValue isBreakDECrystal; //无尽剑和弓是否破坏龙研的混沌水晶
        public final ForgeConfigSpec.BooleanValue endestPearBreakBedrock; //终望珍珠是否破坏基岩
        public final ForgeConfigSpec.BooleanValue isCraftTable; //无尽工作台是否兼容原版工作台配方
        public final ForgeConfigSpec.BooleanValue isLogoInfo; //登录游戏时是否提升反馈信息
        public final ForgeConfigSpec.BooleanValue isArrowLightning; //是否开启无尽矢的召雷功能

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
        public final ForgeConfigSpec.IntValue singularityZinc;
//        public final ForgeConfigSpec.IntValue singularityPlatinum;
        public final ForgeConfigSpec.IntValue singularityNickel;
        public final ForgeConfigSpec.IntValue singularityLead;
        public final ForgeConfigSpec.IntValue singularityTin;
        public final ForgeConfigSpec.IntValue singularityDragonIum;
        public final ForgeConfigSpec.IntValue singularityAwakenDragon;
        public final ForgeConfigSpec.IntValue singularityMana;
        public final ForgeConfigSpec.IntValue singularityTara;
        public final ForgeConfigSpec.IntValue singularityElementIum;
        public final ForgeConfigSpec.IntValue modRatioRate; //模组影响后的最大倍率
        public final ForgeConfigSpec.IntValue modRatioCount; //模组影响后的最大数量

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> pickaxeBlackList; //工具范围挖掘黑名单
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> axeBlackList;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> shovelBlackList;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> hoeBlackList;

        public ServerConfig(ForgeConfigSpec.Builder builder){
            builder.comment("Endless Base Config").push("general");
            this.isKeepStone = buildBoolean(builder, "Is Keep Stone", true, "Does the super mode of endless tools retain stone and soil");
            this.isBreakBedrock = buildBoolean(builder, "Is Break Bedrock", true, "Whether the rock is damaged by the pickaxe");
            this.isMergeMatterCluster = buildBoolean(builder, "Is Merge Matter Cluster", false, "Whether to merge matter cluster");
            this.swordRangeDamage = buildInt(builder, "Sword Range Damage", 10000, 10, 100000, "Range damage value of the right key of endless sword");
            this.swordAttackRange = buildInt(builder, "Sword Attack Range", 32, 8, 64, "Endless sword right click attack range");
            this.isSwordAttackAnimal = buildBoolean(builder, "Is Sword Damage", true, "Does the right key range attack of endless sword attack neutral creatures");
            this.subArrowDamage = buildInt(builder, "Sub Arrow Damage", 10000, 10, 100000, "Endless bow scattering light arrow damage");
            this.noArrowDamage = buildInt(builder, "No Arrow Damage", 10, 5, 100, "If no arrow,Endless bow(cross) shooting arrow damage");
            this.axeChainCount = buildInt(builder, "Axe Chain Count", 64, 16, 128, "Chain number of endless axe cutting trees");
            this.foodTime = buildDouble(builder, "Food Time", 1d, 0.1d, 5d, "Food effect time scaling factor");
            this.isRemoveBlock = buildBoolean(builder, "Is Remove Block", false,  "Hammer form of endless pickaxe left click to delete box");
            this.matterClusterMaxCount = buildInt(builder, "Matter Cluster Max Count", 2048, 256, 10240, "Upper limit of storage of a single item in a material group");
            this.matterClusterMaxTerm = buildInt(builder, "Matter Cluster Max Term", 16, 8, 64, "Upper limit of items stored in the mass");
            this.endestPearlEndDamage = buildInt(builder, "Endest Pearl End Damage", 1000, 100, 10000, "Endest pearl final explosion damage");
            this.endestPearlOneDamage = buildInt(builder, "Endest Pearl One Damage", 5, 1, 10, "Endest pearl single attraction damage");
            this.endestPearlSuckRange = buildInt(builder, "Endest Pearl Suck Range", 20, 10, 32, "Endest pearl gravitational range");
            this.infinityArmorBearDamage = buildInt(builder, "Infinity Armor Bear Damage", 10, 4, 100, "When endless weapons attack players with a full set of endless equipment, damage value will be caused");
            this.infinityBearDamage = buildInt(builder, "Infinity Bear Damage", 4, 1, 50, "When endless weapons attack a full set of endless equipment and players with endless weapons, they cause damage");
            this.infinityFireworkDamage = buildInt(builder, "Infinity Firework Damage", 100, 5, 10000, "Endless crossbow firing fireworks damage");
            this.isBreakDECrystal = buildBoolean(builder, "Is Break DE Crystal", false,  "Does the endless sword and bow destroy the chaotic crystal of DE");
            this.endestPearBreakBedrock = buildBoolean(builder, "Endest Pearl Is Break Bedrock", true,  "Endest pearl whether destroys the bedrock");
            this.isCraftTable = buildBoolean(builder, "Is Craft Table", true,  "Whether the endless workbench is compatible with the original workbench formula");
            this.isLogoInfo = buildBoolean(builder, "Is Logo Info", true,  "Whether to improve feedback when logging in to the game");
            this.isArrowLightning = buildBoolean(builder, "Is Arrow Lightning", false,  "Whether to enable the thunder summoning function of Endless Arrow");
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
            this.singularityZinc = buildInt(builder, "Zinc", 300, 10, 2000, "base count");
//            this.singularityPlatinum = buildInt(builder, "Platinum", 200, 10, 2000, "base count");
            this.singularityNickel = buildInt(builder, "Nickel", 400, 10, 2000, "base count");
            this.singularityLead = buildInt(builder, "Lead", 300, 10, 2000, "base count");
            this.singularityTin = buildInt(builder, "Tin", 400, 10, 2000, "base count");
            this.singularityDragonIum = buildInt(builder, "DragonIum", 80, 10, 2000, "base count");
            this.singularityAwakenDragon = buildInt(builder, "AwakenDragon", 10, 10, 2000, "base count");
            this.singularityMana = buildInt(builder, "Mana", 200, 10, 2000, "base count");
            this.singularityTara = buildInt(builder, "Tara", 100, 10, 2000, "base count");
            this.singularityElementIum = buildInt(builder, "ElementIum", 50, 10, 2000, "base count");
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

    public static class ClientConfig{
        public final ForgeConfigSpec.BooleanValue isRenderLayer; //是否开启渲染（会导致游戏帧率大幅下降）

        public ClientConfig(ForgeConfigSpec.Builder builder){
            builder.comment("Endless Client Config").push("general");
            this.isRenderLayer = buildBoolean(builder, "Is Render Layer", false,  "Whether to turn on rendering (it will lead to a significant decrease in the game frame rate)");
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
