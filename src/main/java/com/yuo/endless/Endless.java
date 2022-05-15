package com.yuo.endless;

import com.yuo.endless.Blocks.BlockRegistry;
import com.yuo.endless.Container.ContainerTypeRegistry;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Gui.*;
import com.yuo.endless.Items.ItemRegistry;
import com.yuo.endless.Items.MatterCluster;
import com.yuo.endless.Items.Singularity;
import com.yuo.endless.NetWork.NetWorkHandler;
import com.yuo.endless.Recipe.ModRecipeManager;
import com.yuo.endless.Recipe.RecipeTypeRegistry;
import com.yuo.endless.Render.GapingVoidRender;
import com.yuo.endless.Render.InfinityArrowRender;
import com.yuo.endless.Render.InfinityArrowSubRender;
import com.yuo.endless.Sound.ModSounds;
import com.yuo.endless.Tiles.TileTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Supplier;

@Mod("endless")
@Mod.EventBusSubscriber(modid = Endless.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Endless {
	public static final String MOD_ID = "endless";
    public static boolean IS_SPACE_ARMS = false; //强力装备
    public static boolean IS_ORE_CROP = false; //矿石作物
    public static boolean IS_ENCHANTS = false; //更多附魔
    public static boolean IS_MORE_COALS = false; //更多煤炭
    public static boolean IS_PAIMENG = false; //应急食品
    public static boolean IS_PROJECTE = false; //等价交换
    public static boolean IS_BOTANIA = false; //植物魔法
    public static boolean IS_ICE_AND_FIRE = false; //冰与火之歌
    public static boolean IS_TORCHERINO = false; //加速火把
    public static boolean IS_CREATE = false; //机械动力
    public static boolean IS_S_BACK_PACKS = false; //精妙背包
	public Endless() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        IS_SPACE_ARMS = checkMod("spacearms");
        IS_ORE_CROP = checkMod("orecrop");
        IS_ENCHANTS = checkMod("yuoenchants");
        IS_MORE_COALS = checkMod("morecoal");
        IS_PAIMENG = checkMod("paimeng");
        IS_PROJECTE = checkMod("projecte");
        IS_BOTANIA = checkMod("botania");
        IS_ICE_AND_FIRE = checkMod("iceandfire");
        IS_TORCHERINO = checkMod("torcherino");
        IS_CREATE = checkMod("create");
        IS_S_BACK_PACKS = checkMod("sophisticatedbackpacks");
        if (IS_SPACE_ARMS){
            ItemRegistry.registerSpaceArmsItem(); //在模组存在的前提下 注册物品
        }
        if (IS_ICE_AND_FIRE){
            ItemRegistry.registerIafItem();
        }

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        //注册物品至mod总线
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        TileTypeRegistry.TILE_ENTITIES.register(modEventBus);
        ContainerTypeRegistry.CONTAINERS.register(modEventBus);
        RecipeTypeRegistry.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
    }


//    @SubscribeEvent
    private void commonSetup(final FMLCommonSetupEvent event) {
        ModRecipeManager.addExtremeCrafts();
        ModRecipeManager.addCompressorCraft();
        ModRecipeManager.lastMinuteChanges();
        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
    }

    @SubscribeEvent
    public void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() ->{
            setBowProperty(ItemRegistry.infinityBow.get());
            setMatterClusterProperty(ItemRegistry.matterCluster.get());
            setInfinityToolProperty(ItemRegistry.infinityPickaxe.get(), "hammer");
            setInfinityToolProperty(ItemRegistry.infinityShovel.get(), "destroyer");
        });
        //绑定Container和ContainerScreen
        event.enqueueWork(() -> {
            ScreenManager.registerFactory(ContainerTypeRegistry.extremeCraftContainer.get(), ExtremeCraftScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.neutronCollectorContainer.get(), NeutronCollectorScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.denseNeutronCollectorContainer.get(), DenseNeutronCollectorScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.doubleNeutronCollectorContainer.get(), DoubleNeutronCollectorScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.tripleNeutronCollectorContainer.get(), TripleNeutronCollectorScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.neutroniumCompressorContainer.get(), NeutroniumCompressorScreen::new);
        });
        registerEntityRender(event.getMinecraftSupplier()); //注册客户端实体渲染
    }
    //    @SubscribeEvent
//    public static void onEnqueue(final InterModEnqueueEvent event) {
//        System.out.println("---------------+++++++++++++++++++");
//    }

    @SubscribeEvent
    public static void itemColors(ColorHandlerEvent.Item event) {
        // 第二个参数代表“所有需要使用此 IItemColor 的物品”，是一个 var-arg Item。
        // 有鉴于第一个参数是一个只有一个方法的接口，我们也可以直接在这里使用 lambda 表达式。
        for (RegistryObject<Item> entry : ItemRegistry.ITEMS.getEntries()) {
            Item item = entry.get();
            if (item instanceof Singularity){
                event.getItemColors().register(Singularity::getColor, item);
            }
        }
//        event.getItemColors().register(Singularity::getColor, ItemRegistry.singularityIron.get(), ItemRegistry.singularityGold.get(),
//                ItemRegistry.singularityDiamond.get(), ItemRegistry.singularityEmerald.get(), ItemRegistry.singularityNetherite.get(),
//                ItemRegistry.singularityCoal.get(), ItemRegistry.singularityLapis.get(), ItemRegistry.singularityRedstone.get(),
//                ItemRegistry.singularityQuartz.get(), ItemRegistry.singularityClay.get());

        // 出于某些原因，你还可以在这里拿到之前的 `BlockColors`。在某些时候这个玩意会很有用。
//        BlockColors blockColorHandler = event.getBlockColors();
    }

    //使用动态属性来切换无尽镐，铲形态
    private void setInfinityToolProperty(Item item, String prop) {
        ItemModelsProperties.registerProperty(item, new ResourceLocation(Endless.MOD_ID,
                prop), (itemStack, clientWorld, livingEntity) -> {
            if (livingEntity == null){
                return 0.0f;
            }
            if (itemStack.getTag() != null && itemStack.getTag().getBoolean(prop)){
                    return 1.0f;
            }
            return 0.0f;
        });
    }

    private void registerEntityRender(Supplier<Minecraft> minecraft){
        ItemRenderer renderer = minecraft.get().getItemRenderer();
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.ENDEST_PEARL.get(), manager -> new SpriteRenderer<>(manager, renderer)); //投掷物渲染
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.INFINITY_ARROW.get(), InfinityArrowRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.INFINITY_ARROW_SUB.get(), InfinityArrowSubRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.GAPING_VOID.get(), GapingVoidRender::new); //渲染实体
    }

    //注册物品染色
    //设置弓物品的动态属性
    private void setBowProperty(Item item){
        ItemModelsProperties.registerProperty(item, new ResourceLocation(Endless.MOD_ID,
                "pull"), (itemStack, clientWorld, livingEntity) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getActiveItemStack() != itemStack ? 0.0F : (float)(itemStack.getUseDuration() - livingEntity.getItemInUseCount()) / 20.0F;
            }
        });
        ItemModelsProperties.registerProperty(item, new ResourceLocation(Endless.MOD_ID,
                "pulling"), (itemStack, clientWorld, livingEntity) -> livingEntity != null && livingEntity.isHandActive() && livingEntity.getActiveItemStack() == itemStack ? 1.0F : 0.0F);
    }

    private void setMatterClusterProperty(Item item){
        ItemModelsProperties.registerProperty(item, new ResourceLocation(Endless.MOD_ID,
                "count"), (itemStack, clientWorld, livingEntity) -> MatterCluster.getItemTag(itemStack).size() > 0 ? 1f:0f);
    }

    /**
     * 检查模组是否存在
     * @param modId 模组id
     * @return 存在 true
     */
    private boolean checkMod(String modId){
        return ModList.get().isLoaded(modId);
    }

    /**
     * 比较版本
     * @param str 实际版本
     * @param minVer 最低版本
     * @param maxVer 最高版本
     * @return 符合 true
     */
    private boolean compareVersion(String str, String minVer, String maxVer){
        String s = str.replace(".", "");
        String s1 = minVer.replace(".", "");
        String s2 = maxVer.replace(".", "");
        int i = Integer.parseInt(s);
        int i1 = Integer.parseInt(s1);
        int i2 = Integer.parseInt(s2);
        return i >= i1 && i <= i2;
    }
}
