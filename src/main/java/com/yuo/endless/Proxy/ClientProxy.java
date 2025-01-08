package com.yuo.endless.Proxy;

import com.yuo.endless.Client.Gui.*;
import com.yuo.endless.Client.Render.EndlessChestTileRender;
import com.yuo.endless.Config;
import com.yuo.endless.Container.EndlessMenuTypes;
import com.yuo.endless.Endless;
import com.yuo.endless.Fluid.EndlessFluids;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.MatterCluster;
import com.yuo.endless.Items.Tool.InfinityCrossBow;
import com.yuo.endless.Tiles.EndlessTileTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * 客户端属性注册
 */
public class ClientProxy implements IProxy {

    @Override
    public void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
//        ModelLoaderRegistry.registerLoader(new ResourceLocation(Endless.MOD_ID, "cosmic"), new CosmicModelLoader());
//        ModelLoaderRegistry.registerLoader(new ResourceLocation(Endless.MOD_ID, "halo"), new HaloItemModelLoader());
//        AvaritiaShaders.init();
        modBus.addListener(this::clientSetup);
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() ->{
            setBowProperty(EndlessItems.infinityBow.get());
            setCrossBowProperty(EndlessItems.infinityCrossBow.get());
            setMatterClusterProperty(EndlessItems.matterCluster.get());
            setInfinityToolProperty(EndlessItems.infinityPickaxe.get(), "hammer");
            setInfinityToolProperty(EndlessItems.infinityShovel.get(), "destroyer");
        });
        //绑定Container和ContainerScreen
        event.enqueueWork(() -> {
            MenuScreens.register(EndlessMenuTypes.extremeCraftContainer.get(), ExtremeCraftScreen::new);
            MenuScreens.register(EndlessMenuTypes.neutronCollectorContainer.get(), NeutronCollectorScreen::new);
            MenuScreens.register(EndlessMenuTypes.denseNeutronCollectorContainer.get(), DenseNeutronCollectorScreen::new);
            MenuScreens.register(EndlessMenuTypes.doubleNeutronCollectorContainer.get(), DoubleNeutronCollectorScreen::new);
            MenuScreens.register(EndlessMenuTypes.tripleNeutronCollectorContainer.get(), TripleNeutronCollectorScreen::new);
            MenuScreens.register(EndlessMenuTypes.neutroniumCompressorContainer.get(), NeutroniumCompressorScreen::new);
            MenuScreens.register(EndlessMenuTypes.CompressorChestContainer.get(), CompressorChestScreen::new);
            MenuScreens.register(EndlessMenuTypes.infinityBoxContainer.get(), InfinityBoxScreen::new);

        });
        //TESR 方块实体渲染
        event.enqueueWork(() ->{
            BlockEntityRenderers.register(EndlessTileTypes.COMPRESS_CHEST_TILE.get(), EndlessChestTileRender::new);
            BlockEntityRenderers.register(EndlessTileTypes.INFINITY_CHEST_TILE.get(), EndlessChestTileRender::new);
        });
        //流体半透明渲染
        ItemBlockRenderTypes.setRenderLayer(EndlessFluids.infinityFluid.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(EndlessFluids.infinityFluidFlowing.get(), RenderType.translucent());
        event.enqueueWork(this::addLayer);
    }

    public void addLayer() {
//        EntityRenderersEvent.AddLayers
        EntityRenderDispatcher m = Minecraft.getInstance().getEntityRenderDispatcher();/*
        Stream.concat(m.getSkinMap().values().stream(), m.renderers.values().stream()).filter(LivingEntityRenderer.class::isInstance).map(r ->
                (LivingEntityRenderer)r).filter(render -> render.getModel() instanceof PlayerModel).unordered().distinct().forEach(render -> {
            EntityRenderer<? extends Player> rendererPlayer = render..getRenderManager().getSkinMap().get("default");
            EntityRenderer<? extends Player> rendererPlayerSlim = render.getRenderManager().getSkinMap().get("slim");
            if (rendererPlayer != null) {
                LivingEntityRenderer livingRenderer = (LivingEntityRenderer) rendererPlayer;
                livingRenderer.addLayer(new InfinityArmorModel.PlayerRender(livingRenderer));
            }
            if (rendererPlayerSlim != null) {
                LivingEntityRenderer livingRenderer = (LivingEntityRenderer)rendererPlayerSlim;
                livingRenderer.addLayer(new InfinityArmorModel.PlayerRender(livingRenderer));
            }
        });*/
    }

    //使用动态属性来切换无尽镐，铲形态
    private void setInfinityToolProperty(Item item, String prop) {
        ItemProperties.register(item, new ResourceLocation(Endless.MOD_ID,
                prop), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null){
                return 0.0f;
            }
            if (itemStack.getTag() != null && itemStack.getTag().getBoolean(prop)){
                return 1.0f;
            }
            return 0.0f;
        });
    }

    //设置弓物品的动态属性
    private void setBowProperty(Item item){
        ItemProperties.register(item, new ResourceLocation(Endless.MOD_ID,
                "pull"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getUseItem() != itemStack ? 0.0F : (float)(itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemProperties.register(item, new ResourceLocation(Endless.MOD_ID,
                "pulling"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);
    }

    private void setCrossBowProperty(Item item){
        ItemProperties.register(item, new ResourceLocation(Endless.MOD_ID,
                "pull"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return InfinityCrossBow.isCharged(itemStack) ? 0.0F : (float)(itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / (float) InfinityCrossBow.getChargeTime();
            }
        });
        ItemProperties.register(item, new ResourceLocation(Endless.MOD_ID,
                "pulling"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);

        ItemProperties.register(item, new ResourceLocation(Endless.MOD_ID,
                "charged"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && InfinityCrossBow.isCharged(itemStack) ? 1f : 0f);
        ItemProperties.register(item, new ResourceLocation(Endless.MOD_ID,
                "firework"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && InfinityCrossBow.isCharged(itemStack) && InfinityCrossBow.containsChargedProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1f : 0f);
    }

    //物资团颜色变化
    private void setMatterClusterProperty(Item item){
        ItemProperties.register(item, new ResourceLocation(Endless.MOD_ID,
                "num"), (itemStack, clientWorld, livingEntity, i) -> livingEntity != null && MatterCluster.getItemTag(itemStack).size() >= Config.SERVER.matterClusterMaxTerm.get() ? 1F : 0F);
    }
}
