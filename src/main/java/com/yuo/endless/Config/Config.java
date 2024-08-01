package com.yuo.endless.Config;

import com.yuo.endless.Items.Singularity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static Set<String> customSingularities = new HashSet<>();

    public static void loadConfig(){
        getToolBlocks(SERVER.pickaxeBlackList.get(), pickaxeBlocks);
        getToolBlocks(SERVER.axeBlackList.get(), axeBlocks);
        getToolBlocks(SERVER.shovelBlackList.get(), shovelBlocks);
        getToolBlocks(SERVER.hoeBlackList.get(), hoeBlocks);
        getCustomSingularities(SERVER.singularityCustomList.get(), customSingularities);
    }

    /**
     * 将方块添加到set
     * @param list 方块id
     * @param set 指定set集合
     */
    private static void getToolBlocks(List<? extends String> list, Set<Block> set){
        for (String s : list) {
            ResourceLocation resourceLocation = new ResourceLocation(s);
            Block block = Registry.BLOCK.getOrDefault(resourceLocation);
            if (block == Blocks.AIR){
                errorInfo.add("error block for ["+ s + "]");
            }else set.add(block);
        }
    }

    /**
     * 通过配置文件获取自定义奇点
     * @param list 字符串列表
     * @param set 奇点集合
     */
    private static void getCustomSingularities(List<? extends String> list, Set<String> set){
        int size = list.size();
        if (size == 0) return;
        if (size % 3 != 0){
            errorInfo.add("error singularity definition for [String size]");
        }else {
            for (int i = 0; i < list.size(); i += 3){
                String s = list.get(i);
                if (!isTypeFlag(s)) {
                    errorInfo.add("Error singularity definition for ["+ list.get(i) +"]");
                    continue;
                }
                String s1 = list.get(i + 1);
                if (!s1.matches("^color0:0x[a-z0-9]{6}$")) {
                    errorInfo.add("Error singularity definition for ["+ s1 +"]");
                    continue;
                }
                String s2 = list.get(i + 2);
                if (!s2.matches("^color1:0x[a-z0-9]{6}$")) {
                    errorInfo.add("Error singularity definition for ["+ list.get(i + 2) +"]");
                    continue;
                }
                String[] split = s.split(":");
                String[] split1 = s1.split(":");
                String[] split2 = s2.split(":");
                Singularity.addSingularity(new Singularity.SingularityData(split[1],
                        Integer.parseInt(split1[1].substring(2), 16), Integer.parseInt(split2[1].substring(2), 16)));
                set.add(split[1]);
            }
        }
    }

    /**
     * 判断字符串是否符合要求
     * @param s 字符串
     * @return 符合
     */
    private static boolean isTypeFlag(String s){
        //字符串结构是否正确
        if (s.matches("^name:[a-z_]+$") && !s.matches(":_")){
            String[] split = s.split(":");
            return !Singularity.linkageTypes.contains(split[1]); //名称是否冲突
        }
        return false;
    }

    public static class ServerConfig{

        public final ForgeConfigSpec.BooleanValue isKeepStone; //无尽工具的超级模式是否保留石头和泥土
        public final ForgeConfigSpec.BooleanValue isBreakBedrock; //无尽镐是否破坏不可破坏方块
        public final ForgeConfigSpec.BooleanValue isMergeMatterCluster; // 是否合并物质团
        public final ForgeConfigSpec.IntValue swordRangeDamage; //无尽剑右键的范围伤害值
        public final ForgeConfigSpec.IntValue swordAttackRange; //无尽剑右键攻击范围
        public final ForgeConfigSpec.BooleanValue isSwordAttackAnimal; // 无尽剑的右键范围攻击是否攻击中立生物
        public final ForgeConfigSpec.IntValue subArrowDamage; // 无尽光箭伤害
        public final ForgeConfigSpec.IntValue noArrowDamage; // 无尽弓,弩空射箭矢伤害
        public final ForgeConfigSpec.IntValue axeChainDistance; // 无尽斧砍树连锁最大距离
        public final ForgeConfigSpec.BooleanValue isAxeChangeGrassBlock; // 无尽斧的右键范围破坏是否转变草方块
        public final ForgeConfigSpec.DoubleValue foodTime; //食物效果时间缩放系数
        public final ForgeConfigSpec.BooleanValue isRemoveBlock; //无尽镐潜行左键是否删除方块
        public final ForgeConfigSpec.IntValue matterClusterMaxCount; //物质团单个物品存储上限
        public final ForgeConfigSpec.IntValue matterClusterMaxTerm; //物质团存储物品项上限
        public final ForgeConfigSpec.IntValue endestPearlEndDamage; //终望珍珠最终爆炸伤害
        public final ForgeConfigSpec.IntValue endestPearlOneDamage; //终望珍珠单次吸引伤害
        public final ForgeConfigSpec.IntValue endestPearlSuckRange; //终望珍珠引力范围
        public final ForgeConfigSpec.IntValue infinityArmorBearDamage; //无尽武器攻击全套无尽装备玩家时，造成伤害值
        public final ForgeConfigSpec.IntValue infinityBearDamage; //无尽武器攻击全套无尽装备和持有无尽武器玩家时，造成伤害值
        public final ForgeConfigSpec.IntValue infinityFireworkDamage; //无尽弩发射烟花伤害
        public final ForgeConfigSpec.BooleanValue isBreakDECrystal; //无尽剑和弓是否破坏龙研的混沌水晶
        public final ForgeConfigSpec.BooleanValue isCraftTable; //无尽工作台是否兼容原版工作台配方
        public final ForgeConfigSpec.BooleanValue isArrowLightning; //是否开启无尽矢的召雷功能
        public final ForgeConfigSpec.IntValue infinityBucketRange; //无尽桶的最大装取范围，0：一格，1：3*3*3，2:5*5*5
        public final ForgeConfigSpec.IntValue infinityChestFly; //无尽胸甲增加的飞行速度
        public final ForgeConfigSpec.IntValue infinityLegsWalk; //无尽护腿增加的移动速度
        public final ForgeConfigSpec.IntValue infinityFeetJump; //无尽鞋子增加的跳跃高度

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
        public final ForgeConfigSpec.IntValue singularityDarkMatter;
        public final ForgeConfigSpec.IntValue singularityRedMatter;
        public final ForgeConfigSpec.IntValue singularityCobalt;
        public final ForgeConfigSpec.IntValue singularityManyullyn;

        public final ForgeConfigSpec.IntValue mobWeigh; //无尽生物生成权重
        public final ForgeConfigSpec.BooleanValue mobHpInfo; //无尽生物血条

        public final ForgeConfigSpec.IntValue modRatioRate; //模组影响后的最大倍率
        public final ForgeConfigSpec.IntValue modRatioCount; //模组影响后的最大数量

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> pickaxeBlackList; //工具范围挖掘黑名单
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> axeBlackList;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> shovelBlackList;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> hoeBlackList;

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> singularityCustomList; //自定义奇点

        public ServerConfig(ForgeConfigSpec.Builder builder){
            builder.comment("Endless Base Config").push("general");
            this.isKeepStone = buildBoolean(builder, "Is Keep Stone", true, "Whether to allow the super mode of infinity tools keep stone and dirt.");
            this.isBreakBedrock = buildBoolean(builder, "Is Break Bedrock", true, "Whether to allow the hammer mode of the infinity pickaxe to destory unbreakable blocks such as bedrock.");
            this.isMergeMatterCluster = buildBoolean(builder, "Is Merge Matter Cluster", false, "Whether to merge matter cluster");
            this.swordRangeDamage = buildInt(builder, "Sword Range Damage", 10000, 10, 1000000, "Damage of right click attack of the infinity sword.");
            this.swordAttackRange = buildInt(builder, "Sword Attack Range", 32, 8, 128, "Infinity sword right click attack range.");
            this.isSwordAttackAnimal = buildBoolean(builder, "Is Sword Attack Animal", true, "Whether to allow infinity sword right click attack neutral creatures.");
            this.subArrowDamage = buildInt(builder, "Sub Arrow Damage", 10000, 10, 100000, "Damage of the sub arrow.");
            this.noArrowDamage = buildInt(builder, "No Arrow Damage", 10, 5, 1000, "Damage of the infinity bow or crossbow without projectile.");
            this.axeChainDistance = buildInt(builder, "Axe Chain Distance", 32, 16, 1024, "Maximum distance of a chain cutting of the infinity axe.");
            this.isAxeChangeGrassBlock = buildBoolean(builder, "Is Axe Change Grass Block", true,  "Whether to allow the infinity axe to turn grass block into dirt.");
            this.foodTime = buildDouble(builder, "Food Time", 1d, 0.1d, 5d, "Food effect time scaling factor");
            this.isRemoveBlock = buildBoolean(builder, "Is Remove Block", false,  "Whether to allow the infinity pickaxe to destory unbreakable blocks with shift + left click.");
            this.matterClusterMaxCount = buildInt(builder, "Matter Cluster Max Count", 2048, 256, 10240, "Maximum quantity of a single item in a matter cluster.");
            this.matterClusterMaxTerm = buildInt(builder, "Matter Cluster Max Term", 16, 8, 128, "Maximum category of items in a matter cluster.");
            this.endestPearlEndDamage = buildInt(builder, "Endest Pearl End Damage", 1000, 100, 10000, "Endest pearl explosion damage");
            this.endestPearlOneDamage = buildInt(builder, "Endest Pearl One Damage", 5, 1, 100, "Endest pearl attraction damage");
            this.endestPearlSuckRange = buildInt(builder, "Endest Pearl Attraction Range", 20, 10, 64, "Endest pearl attraction range");
            this.infinityArmorBearDamage = buildInt(builder, "Infinity Armor Bear Damage", 10, 4, 100, "Damage of infinity weapons to players wearing a full suit of infinity armor.");
            this.infinityBearDamage = buildInt(builder, "Infinity Bear Damage", 4, 1, 50, "Damage of infinity weapons to players wearing a full suit of infinity armor and holding infinity weapons.");
            this.infinityFireworkDamage = buildInt(builder, "Infinity Firework Damage", 100, 5, 10000, "Infinity crossbow fireworks damage.");
            this.isBreakDECrystal = buildBoolean(builder, "Is Break DE Crystal", false,  "Whether to allow the infinity sword and bow to destroy the guardian crystal in DE(Draconic Evolution).");
            this.isCraftTable = buildBoolean(builder, "Is Craft Table", true,  "Whether to allow the extreme crafting table to use vanilla crafting table recipes.");
            this.isArrowLightning = buildBoolean(builder, "Is Arrow Lightning", false,  "Whether to allow the infinity arrow to channel lightning.");
            this.infinityBucketRange = buildInt(builder, "Infinity Bucket Range", 7, 0, 32, "Maximum loading range of endless barrels. eg: 0-1*1*1, 1-3*3*3,2-5*5*5");
            this.infinityChestFly = buildInt(builder, "Infinity Chest Fly", 3, 1, 10, "Infinity Chest increased flight speed");
            this.infinityLegsWalk = buildInt(builder, "Infinity Legs Walk", 3, 1, 10, "Infinity Legs increased movement speed");
            this.infinityFeetJump = buildInt(builder, "Infinity Feet Jump", 3, 1, 10, "Infinity Feet increased jump height");
            builder.pop();

            builder.comment("Basic amount of singularities required by the compressor.").push("singularity");
            this.singularityCoal = buildInt(builder, "Coal", 450, 10, 100000, "base count");
            this.singularityClay = buildInt(builder, "Clay", 400, 10, 100000, "base count");
            this.singularityIron = buildInt(builder, "Iron", 300, 10, 100000, "base count");
            this.singularityGold = buildInt(builder, "Gold", 350, 10, 100000, "base count");
            this.singularityDiamond = buildInt(builder, "Diamond", 250, 10, 100000, "base count");
            this.singularityEmerald = buildInt(builder, "Emerald", 200, 10, 100000, "base count");
            this.singularityNetherite = buildInt(builder, "Netherite", 150, 10, 100000, "base count");
            this.singularityLapis = buildInt(builder, "Lapis", 400, 10, 100000, "base count");
            this.singularityQuartz = buildInt(builder, "Quartz", 500, 10, 100000, "base count");
            this.singularityRedstone = buildInt(builder, "Redstone", 400, 10, 100000, "base count");
            this.singularitySilver = buildInt(builder, "Silver", 200, 10, 100000, "base count");
            this.singularityCopper = buildInt(builder, "Copper", 300, 10, 100000, "base count");
            this.singularityRuby = buildInt(builder, "Ruby", 250, 10, 100000, "base count");
            this.singularityDragon = buildInt(builder, "Dragon", 100, 10, 100000, "base count");
            this.singularitySpace = buildInt(builder, "Space", 50, 10, 100000, "base count");
            this.singularityXray = buildInt(builder, "Xray", 150, 10, 100000, "base count");
            this.singularityUltra = buildInt(builder, "Ultra", 80, 10, 100000, "base count");
            this.singularityZinc = buildInt(builder, "Zinc", 300, 10, 100000, "base count");
//            this.singularityPlatinum = buildInt(builder, "Platinum", 200, 10, 2000, "base count");
            this.singularityNickel = buildInt(builder, "Nickel", 400, 10, 100000, "base count");
            this.singularityLead = buildInt(builder, "Lead", 300, 10, 100000, "base count");
            this.singularityTin = buildInt(builder, "Tin", 400, 10, 100000, "base count");
            this.singularityDragonIum = buildInt(builder, "DragonIum", 80, 10, 100000, "base count");
            this.singularityAwakenDragon = buildInt(builder, "AwakenDragon", 10, 10, 100000, "base count");
            this.singularityMana = buildInt(builder, "Mana", 200, 10, 100000, "base count");
            this.singularityTara = buildInt(builder, "Tara", 100, 10, 100000, "base count");
            this.singularityElementIum = buildInt(builder, "ElementIum", 50, 10, 100000, "base count");
            this.singularityDarkMatter = buildInt(builder, "Dark Matter", 150, 10, 100000, "base count");
            this.singularityRedMatter = buildInt(builder, "Red Matter", 100, 10, 100000, "base count");
            this.singularityCobalt = buildInt(builder, "Cobalt", 150, 10, 100000, "base count");
            this.singularityManyullyn = buildInt(builder, "Manyullyn", 100, 10, 100000, "base count");

            this.mobWeigh = buildInt(builder, "Mob Weigh", 1, 0, 5, "Infinity mob spawn weigh");
            this.mobHpInfo = buildBoolean(builder, "Mob HP Info", true, "Infinity mob hp info");
            builder.pop();

            builder.comment("Mod Impact").push("modRatio");
            this.modRatioRate = buildInt(builder, "Mod Ratio Rate", 20, 5, 100, "Maximum multiple of the amount of singularities affected by other mods.");
            this.modRatioCount = buildInt(builder, "Mod Ratio Count", 2000, 500, 5000, "Maximum addition of the amount of singularities affected by other mods.");
            builder.pop();

            builder.comment("Blacklist of the super mode of infinity tools.For example, if you want stone and block of diamond to be ignored from the super mode of the infinity pickaxe, use [\"minecraft:stone\", \"minecraft:diamond_block\"] in pickaxe blacklist;" +
                    "if you want dirt to be ignored from the super mode of the infinity shovel, use [\"minecraft:dirt\"] in shovel blacklist.").push("black list");
            this.pickaxeBlackList = buildConfig(builder, "Pickaxe Black List", "Blacklist of the super mode of the infinity pickaxe");
            this.axeBlackList = buildConfig(builder, "Axe Black List", "Blacklist of the super mode of the infinity axe");
            this.shovelBlackList = buildConfig(builder, "Shovel Black List", "Blacklist of the super mode of the infinity shovel");
            this.hoeBlackList = buildConfig(builder, "Hoe Black List", "Blacklist of the super mode of the infinity hoe");
            builder.pop();

            builder.comment("Custom Singularities,However, the existing name cannot be used.Provide a name (use lowercase letters and sliding line), and two hexadecimal color codes to define a new singularity." +
                    " eg:[{\"name:wood\",\"color0:0x112233\",\"color1:0x445566\"},{...}]").push("singularityCustom");
            this.singularityCustomList = buildConfig(builder, "Singularity Custom List", "Custom Singularity List");
            builder.pop();
        }
    }

    public static class ClientConfig{
        public final ForgeConfigSpec.BooleanValue isChangeWing; //是否切换翅膀纹理
        public final ForgeConfigSpec.BooleanValue isRenderEye; //是否开启发光眼睛

        public ClientConfig(ForgeConfigSpec.Builder builder){
            builder.comment("Endless Client Config").push("general");
            this.isChangeWing = buildBoolean(builder, "Is Change Wing", false,  "Whether to switch the wing texture");
            this.isRenderEye = buildBoolean(builder, "Is Render Eye", false,  "Whether to open the luminous eyes");
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
