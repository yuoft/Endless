package com.yuo.endless;

import com.yuo.endless.Blocks.BlockRegistry;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Container.ContainerTypeRegistry;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Items.ItemRegistry;
import com.yuo.endless.NetWork.NetWorkHandler;
import com.yuo.endless.Proxy.ClientProxy;
import com.yuo.endless.Proxy.CommonProxy;
import com.yuo.endless.Proxy.IProxy;
import com.yuo.endless.Recipe.ModRecipeManager;
import com.yuo.endless.Recipe.RecipeTypeRegistry;
import com.yuo.endless.Sound.ModSounds;
import com.yuo.endless.Tiles.TileTypeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("endless")
public class Endless {
	public static final String MOD_ID = "endless";
    public static boolean isSpaceArms = false; //强力装备
    public static boolean isOreCrop = false; //矿石作物
    public static boolean isEnchants = false; //更多附魔
    public static boolean isMoreCoals = false; //更多煤炭
    public static boolean isPaimeng = false; //应急食品
    public static boolean isProjecte = false; //等价交换
    public static boolean isBotania = false; //植物魔法
    public static boolean isIceandfire = false; //冰与火之歌
    public static boolean isTorcherino = false; //加速火把
    public static boolean isCreate = false; //机械动力
    public static boolean isSophisticatedBackpacks = false; //精妙背包
    public static boolean isOreExcavation = false; //矿石挖掘
    public static boolean isAdventOfAscension3 = false; //虚无世界3
    public static boolean isTinkersConstruct3 = false; //匠魂3
    public static boolean isCraftTweaker = false; //CRT
    public static boolean isStorageDrawers = false; //储物抽屉
    public static boolean isEnchantingInfuser = false; //附魔灌注台
    public static boolean isTouhouLittleMaid = false; //车万女仆
    public static boolean isTravelersBackpack = false; //旅行者背包
    public static boolean isAppliedEnergistics2 = false; //应用能源2
    public static boolean isWaystones = false; //传送石碑
    public static boolean isAlexsMobs = false; //Alex 的生物
    public static boolean isTheTwilightForest = false; //暮色森林
    public static boolean isAstralSorcery = false; //星辉魔法
    public static boolean isSlashBlade2 = false; //拔刀剑2
    public static boolean isThermal = false; //热力基本
    public static boolean isTimeBottle = false; //时间之瓶
    public static boolean isDraconicEvolution = false; //龙之进化
    public static boolean isInfernalMobs = false; //稀有精英怪
    public static boolean isChampions = false; //冠军/强敌
    public static boolean isZombieAwareness = false; //僵尸意识
    public static boolean isMysticalAgriculture = false; //神秘农业
    public static final IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	public Endless() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SERVER_CONFIG); //配置文件
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        checkMods();
        if (isSpaceArms){
            ItemRegistry.registerSpaceArmsItem(); //在模组存在的前提下 注册物品
        }
        if (isIceandfire){
            ItemRegistry.registerIafItem();
        }
        if (isCreate){
            ItemRegistry.registerCreate();
        }
        if (isThermal){
            ItemRegistry.registerThermal();
        }
        if (isDraconicEvolution){
            ItemRegistry.registerDE();
        }
        if (isBotania){
            ItemRegistry.registerBOT();
        }
        if (isProjecte){
            ItemRegistry.registerPE();
        }

        modEventBus.addListener(this::commonSetup);
        //注册物品至mod总线
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        TileTypeRegistry.TILE_ENTITIES.register(modEventBus);
        ContainerTypeRegistry.CONTAINERS.register(modEventBus);
        RecipeTypeRegistry.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        proxy.registerHandlers();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModRecipeManager.addExtremeCrafts();
        ModRecipeManager.addCompressorCraft();
        ModRecipeManager.lastMinuteChanges();
        Config.loadConfig(); //加载工具黑名单
        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
    }

    private void checkMods(){
        isSpaceArms = checkMod("spacearms");
        isOreCrop = checkMod("orecrop");
        isEnchants = checkMod("yuoenchants");
        isMoreCoals = checkMod("morecoal");
        isPaimeng = checkMod("paimeng");
        isProjecte = checkMod("projecte");
        isBotania = checkMod("botania");
        isIceandfire = checkMod("iceandfire");
        isTorcherino = checkMod("torcherino");
        isCreate = checkMod("create");
        isSophisticatedBackpacks = checkMod("sophisticatedbackpacks");
        isOreExcavation = checkMod("oreexcavation");
        isAdventOfAscension3 = checkMod("aoa3");
        isTinkersConstruct3 = checkMod("tconstruct");
        isCraftTweaker = checkMod("crafttweaker");
        isStorageDrawers = checkMod("storagedrawers");
        isEnchantingInfuser = checkMod("enchantinginfuser");
        isTouhouLittleMaid = checkMod("touhou_little_maid");
        isTravelersBackpack = checkMod("travellersbackpack");
        isAppliedEnergistics2 = checkMod("appliedenergistics2");
        isWaystones = checkMod("waystones");
        isAlexsMobs = checkMod("alexsmobs");
        isTheTwilightForest = checkMod("twilightforest");
        isAstralSorcery = checkMod("astralsorcery");
        isSlashBlade2 = checkMod("slashblade");
        isThermal = checkMod("thermal");
        isTimeBottle = checkMod("tiab");
        isDraconicEvolution = checkMod("draconicevolution");
        isInfernalMobs = checkMod("infernalmobs");
        isChampions = checkMod("champions");
        isZombieAwareness = checkMod("zombieawareness");
        isMysticalAgriculture = checkMod("mysticalagriculture");
    }

    /**
     * 检查模组是否存在
     * @param modId 模组id
     * @return 存在 true
     */
    private boolean checkMod(String modId){
        return ModList.get().isLoaded(modId);
    }

}
