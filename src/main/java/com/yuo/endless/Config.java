package com.yuo.endless;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class Config {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ServerConfig SERVER;
    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        COMMON_CONFIG = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static class ServerConfig{

        public final ForgeConfigSpec.BooleanValue isKeepStone; //无尽工具的超级模式是否保留石头和泥土
        public final ForgeConfigSpec.BooleanValue isMergeMatterCluster; // 是否合并物质团
        public final ForgeConfigSpec.IntValue swordRangeDamage; //无尽剑右键的范围伤害值
        public final ForgeConfigSpec.IntValue swordAttackRange; //无尽剑右键攻击范围
        public final ForgeConfigSpec.BooleanValue isSwordAttackAnimal; // 无尽剑右键范围攻击是否攻击中立生物
        public final ForgeConfigSpec.IntValue subArrowDamage; // 无尽弓散射光箭伤害
        public final ForgeConfigSpec.IntValue axeChainCount; // 无尽斧砍树连锁数量
        public final ForgeConfigSpec.DoubleValue foodTime; //食物效果时间缩放系数
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

        public ServerConfig(ForgeConfigSpec.Builder builder){
            builder.comment("Endless Base Config").push("general");
            this.isKeepStone = buildBoolean(builder, "Is Stone", false, "Does the super mode of endless tools retain stone and soil");
            this.isMergeMatterCluster = buildBoolean(builder, "Is Merge Matter Cluster", false, "Whether to merge matter cluster");
            this.swordRangeDamage = buildInt(builder, "Sword Range Damage", 10000, 100, 100000, "Range damage value of the right key of endless sword");
            this.swordAttackRange = buildInt(builder, "Sword Attack Range", 32, 8, 64, "Endless sword right click attack range");
            this.isSwordAttackAnimal = buildBoolean(builder, "Is Sword Damage", true, "Does the right key range attack of endless sword attack neutral creatures");
            this.subArrowDamage = buildInt(builder, "Sub Arrow Damage", 10000, 100, 100000, "Endless bow scattering light arrow damage");
            this.axeChainCount = buildInt(builder, "Axe Chain Count", 64, 16, 128, "Chain number of endless axe cutting trees");
            this.foodTime = buildDouble(builder, "Food Time", 1d, 0.1d, 5d, "Food effect time scaling factor");
            builder.pop();

            builder.comment("Singularity Recipe Count").push("singularity");
            this.singularityCoal = buildInt(builder, "Coal", 450, 10, 2000, "");
            this.singularityClay = buildInt(builder, "Clay", 400, 10, 2000, "");
            this.singularityIron = buildInt(builder, "Iron", 300, 10, 2000, "");
            this.singularityGold = buildInt(builder, "Gold", 350, 10, 2000, "");
            this.singularityDiamond = buildInt(builder, "Diamond", 250, 10, 2000, "");
            this.singularityEmerald = buildInt(builder, "Emerald", 200, 10, 2000, "");
            this.singularityNetherite = buildInt(builder, "Netherite", 150, 10, 2000, "");
            this.singularityLapis = buildInt(builder, "Lapis", 400, 10, 2000, "");
            this.singularityQuartz = buildInt(builder, "Quartz", 500, 10, 2000, "");
            this.singularityRedstone = buildInt(builder, "Redstone", 400, 10, 2000, "");
            this.singularitySilver = buildInt(builder, "Silver", 200, 10, 2000, "");
            this.singularityCopper = buildInt(builder, "Copper", 300, 10, 2000, "");
            this.singularityRuby = buildInt(builder, "Ruby", 250, 10, 2000, "");
            this.singularityDragon = buildInt(builder, "Dragon", 100, 10, 2000, "");
            this.singularitySpace = buildInt(builder, "Space", 50, 10, 2000, "");
            this.singularityXray = buildInt(builder, "Xray", 150, 10, 2000, "");
            this.singularityUltra = buildInt(builder, "Ultra", 80, 10, 2000, "");
            builder.pop();

            builder.comment("Mod Impact").push("modRatio");
            this.modRatioRate = buildInt(builder, "Mod Ratio Rate", 20, 5, 50, "Maximum magnification after module influence");
            this.modRatioCount = buildInt(builder, "Mod Ratio Count", 2000, 500, 5000, "Maximum number of modules affected");
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

}
