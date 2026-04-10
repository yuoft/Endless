package com.yuo.endless;

import com.brandon3055.draconicevolution.entity.GuardianCrystalEntity;
import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity;
import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianPartEntity;
import com.yuo.endless.Config.ModConfig;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Tool.InfinityDamageTypes;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

/**
 * 工具类
 */
@SuppressWarnings("removal")
public class EndlessUtils {
    public static boolean isEnchants = false; //更多附魔
    public static boolean isSpaceArms = false; //强力装备
    public static boolean isPE = false; //等价交换
    public static boolean isBOT = false; //植物魔法
    public static boolean isIAF = false; //冰与火之歌
    public static boolean isTorcherino = false; //加速火把
    public static boolean isCreate = false; //机械动力
    public static boolean isSophisticatedBackpacks = false; //精妙背包
    public static boolean isOreExcavation = false; //矿石挖掘
    public static boolean isTC3 = false; //匠魂3
    public static boolean isCrT = false; //CRT
    public static boolean isStorageDrawers = false; //储物抽屉
    public static boolean isEnchantingInfuser = false; //附魔灌注台
    public static boolean isTouhouLittleMaid = false; //车万女仆
    public static boolean isTravelersBackpack = false; //旅行者背包
    public static boolean isAE2 = false; //应用能源2
    public static boolean isWaystones = false; //传送石碑
    public static boolean isAlexsMobs = false; //Alex 的生物
    public static boolean isTTF = false; //暮色森林
    public static boolean isSlashBlade2 = false; //拔刀剑2
    public static boolean isThermal = false; //热力基本
    public static boolean isTimeBottle = false; //时间之瓶
    public static boolean isDE = false; //龙之进化
    public static boolean isInfernalMobs = false; //稀有精英怪
    public static boolean isChampions = false; //冠军/强敌
    public static boolean isZombieAwareness = false; //僵尸意识
    public static boolean isMysticalAgriculture = false; //神秘农业
    public static boolean isRS = false; //精致存储
    public static boolean isDS = false; //龙之生存
    public static boolean isIPN = false; //一键背包整理
    public static boolean isWDA = false; //地牢复现之时
    public static boolean isFarmersDelight = false; //农夫乐事
    public static boolean isGoblinTraders = false; //哥布林商人
    public static boolean isFTBUltimine = false; //FTB连锁破坏
    public static boolean isVampirism = false; //吸血鬼
    public static boolean isCroparia = false; //魔种之咏
    public static boolean isMinecolonies = false; //模拟殖民地
    public static boolean isDivineRPG = false; //神圣RPG
    public static boolean isDEAdd = false; //龙之进化拓展
    public static boolean isDoggyTalents = false; //小狗天才
    public static boolean isIE = false; //沉浸工程
    public static boolean isEnigmaticLegacy = false; //神秘遗物
    public static boolean isApotheosis = false; //神化
    public static boolean isQuark = false; //夸克
    public static boolean isArsNouveau = false; //新生魔艺
    public static boolean isEXBOT = false; //额外植物学
    public static boolean isIronChests = false; //更多箱子
    public static boolean isMobGrindingUtils = false; //刷怪塔实用设备
    public static boolean isAR = false; //高级火箭
    public static boolean isEverlastingAbilities = false; //永恒能力
    public static boolean isBM3 = false; //血魔法3
    public static boolean isPEI = false; //等价交换兼容
    public static boolean isER = false; //末地创世
    public static boolean isLB = false; //幸运方块
    public static boolean isPEX = false; //等价交换升级
    public static boolean isMorph = false; //变身
    public static boolean isLootr = false; //多人宝箱
    public static boolean isExtremeReactors = false; //极限反应堆
    public static boolean isPlayerRevive = false; //玩家救援
    public static boolean isXPTmoe = false; //经验之书
    public static boolean isIronFurnaces = false; //更多熔炉
    public static boolean isCA = false; //混沌觉醒
    public static boolean isCurios = false; //饰品栏
    public static boolean isDummmmmmy = false; //假人
    public static boolean isccApi = false; //cloth config api

    public static ResourceLocation fa(String path){
        return new ResourceLocation(Endless.MOD_ID, path);
    }

    public static ResourceLocation fa(String namespace, String path){
        return new ResourceLocation(namespace, path);
    }

    public static ResourceLocation tryParse(String s){
        try {
            return new ResourceLocation(s);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }

    public static ResourceLocation parse(String s){
        return new ResourceLocation(s);
    }

    /**
     * 无尽攻击
     * @param target 目标
     * @param attacker 攻击者
     */
    public static void atkInfinity(LivingEntity target, LivingEntity attacker){
        if (target instanceof EnderDragon dragon && attacker instanceof Player){
            dragon.hurt(dragon.head, InfinityDamageTypes.infinity(attacker), Float.MAX_VALUE);
        } else if (target instanceof ArmorStand){
            target.hurt(attacker.damageSources().generic(), 10);
        } else {
            if (target instanceof Player player){
                if (EventHandler.isInfinite(player)){ //被攻击玩家有全套无尽 减免至10点
                    if (EventHandler.isInfinityItem(player)) //玩家在持有无尽剑或弓时 减免至4点
                        target.hurt(InfinityDamageTypes.infinity(attacker), ModConfig.SERVER.infinityBearDamage.get());
                    else target.hurt(InfinityDamageTypes.infinity(attacker), ModConfig.SERVER.infinityArmorBearDamage.get());
                } else target.hurt(InfinityDamageTypes.infinity(attacker),  Float.MAX_VALUE);
            } else target.hurt(InfinityDamageTypes.infinity(attacker), Float.MAX_VALUE);
        }
    }

    /**
     * 攻击龙研中的实体 混沌水晶
     * @param entity 实体
     * @param player 玩家
     */
    public static void damageGuardian(Entity entity, Player player){
        if (entity instanceof DraconicGuardianEntity draconicGuardian){
            draconicGuardian.attackEntityPartFrom(draconicGuardian.dragonPartHead, InfinityDamageTypes.infinity(player), Float.MAX_VALUE);
            draconicGuardian.setHealth(-1);
            draconicGuardian.die(InfinityDamageTypes.infinity(player));
        }else if (entity instanceof GuardianCrystalEntity crystal){
            crystal.kill();
        }else if (entity instanceof DraconicGuardianPartEntity draconicGuardian) {
            DraconicGuardianEntity dragon = draconicGuardian.dragon;
            dragon.hurt(player.damageSources().thorns(player), Float.MAX_VALUE);
            dragon.attackEntityPartFrom(dragon.dragonPartHead, InfinityDamageTypes.infinity(player), Float.MAX_VALUE);
            GuardianCrystalEntity crystal = dragon.closestGuardianCrystal;
            if (crystal != null) {
                crystal.kill();
            }
            if (dragon.isAlive() || dragon.getHealth() > 0) {
                dragon.setHealth(-1);
                if (!player.level().isClientSide) {
                    dragon.die(InfinityDamageTypes.infinity(player));
                }
            }
            dragon.kill();
        }
    }

    /**
     * 注册联动奇点
     */
    public static void registerModCompat() {
        if (isIAF){
            EndlessItems.registerIafItem();
        }
        if (isCreate){
            EndlessItems.registerCreate();
        }
        if (isThermal){
            EndlessItems.registerThermal();
        }
        if (isDE){
            EndlessItems.registerDE();
        }
        if (isBOT){
            EndlessItems.registerBOT();
        }
        if (isPE){
            EndlessItems.registerPE();
        }
        if (isTC3){
            EndlessItems.registerTC3();
        }
        if (isSpaceArms){
            EndlessItems.registerSArms();
        }
    }

    /**
     * 联动模组检查
     */
    public static void checkMods(){
        isSpaceArms = checkMod("spacearms");
        isEnchants = checkMod("yuoenchants");
        isPE = checkMod("projecte");
        isBOT = checkMod("botania");
        isIAF = checkMod("iceandfire");
        isTorcherino = checkMod("torcherino");
        isCreate = checkMod("create");
        isSophisticatedBackpacks = checkMod("sophisticatedbackpacks");
        isOreExcavation = checkMod("oreexcavation");
        isTC3 = checkMod("tconstruct");
        isCrT = checkMod("crafttweaker");
        isStorageDrawers = checkMod("storagedrawers");
        isEnchantingInfuser = checkMod("enchantinginfuser");
        isTouhouLittleMaid = checkMod("touhou_little_maid");
        isTravelersBackpack = checkMod("travellersbackpack");
        isAE2 = checkMod("ae2");
        isWaystones = checkMod("waystones");
        isAlexsMobs = checkMod("alexsmobs");
        isTTF = checkMod("twilightforest");
        isSlashBlade2 = checkMod("slashblade");
        isThermal = checkMod("thermal");
        isTimeBottle = checkMod("tiab");
        isDE = checkMod("draconicevolution");
        isInfernalMobs = checkMod("infernalmobs");
        isChampions = checkMod("champions");
        isZombieAwareness = checkMod("zombieawareness");
        isMysticalAgriculture = checkMod("mysticalagriculture");
        isRS = checkMod("refinedstorage");
        isDS = checkMod("dragonsurvival");
        isIPN = checkMod("inventoryprofilesnext");
        isWDA = checkMod("dungeons_arise");
        isFarmersDelight = checkMod("farmersdelight");
        isGoblinTraders = checkMod("goblintraders");
        isFTBUltimine = checkMod("ftbultimine");
        isVampirism = checkMod("vampirism");
        isCroparia = checkMod("croparia");
        isMinecolonies = checkMod("minecolonies");
        isDivineRPG = checkMod("divinerpg");
        isDEAdd = checkMod("draconicadditions");
        isDoggyTalents = checkMod("doggytalents");
        isIE = checkMod("immersiveengineering");
        isEnigmaticLegacy = checkMod("enigmaticlegacy");
        isApotheosis = checkMod("apotheosis");
        isQuark = checkMod("quark");
        isArsNouveau = checkMod("ars_nouveau");
        isEXBOT = checkMod("extrabotany");
        isIronChests = checkMod("ironchest");
        isMobGrindingUtils = checkMod("mob_grinding_utils");
        isAR = checkMod("advancedrocketry");
        isEverlastingAbilities = checkMod("everlastingabilities");
        isBM3 = checkMod("bloodmagic");
        isPEI = checkMod("projecteintegration");
        isER = checkMod("endrem");
        isLB = checkMod("lucky");
        isPEX = checkMod("projectex");
        isMorph = checkMod("morph");
        isLootr = checkMod("lootr");
        isExtremeReactors = checkMod("bigreactors");
        isPlayerRevive = checkMod("playerrevive");
        isXPTmoe = checkMod("xpbook");
        isIronFurnaces = checkMod("ironfurnaces");
        isCA = checkMod("chaosawakens");
        isCurios = checkMod("curios");
        isDummmmmmy = checkMod("dummmmmmy");
        isccApi = checkMod("cloth_config");
    }

    /**
     * 检查模组是否存在
     * @param modId 模组id
     * @return 存在 true
     */
    private static boolean checkMod(String modId){
        return ModList.get().isLoaded(modId);
    }
}
