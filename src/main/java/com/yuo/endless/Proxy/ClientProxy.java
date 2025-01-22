package com.yuo.endless.Proxy;

import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Client.Gui.*;
import com.yuo.endless.Client.Model.CosmicModelLoader;
import com.yuo.endless.Client.Model.HaloItemModelLoader;
import com.yuo.endless.Client.Model.InfinityArmorModel;
import com.yuo.endless.Client.Render.*;
import com.yuo.endless.Config;
import com.yuo.endless.Container.ContainerTypeRegistry;
import com.yuo.endless.Endless;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Fluid.EndlessFluids;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.MatterCluster;
import com.yuo.endless.Items.Tool.InfinityCrossBow;
import com.yuo.endless.Tiles.EndlessTileTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.stream.Stream;

/**
 * 客户端属性注册
 * Description:
 * Author: cnlimiter
 * Date: 2022/5/21 23:37
 * Version: 1.0
 */
public class ClientProxy implements IProxy {

    @Override
    public void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModelLoaderRegistry.registerLoader(new ResourceLocation(Endless.MOD_ID, "cosmic"), new CosmicModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(Endless.MOD_ID, "halo"), new HaloItemModelLoader());
        AvaritiaShaders.init();
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
            ScreenManager.registerFactory(ContainerTypeRegistry.extremeCraftContainer.get(), ExtremeCraftScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.enderCraftContainer.get(), EnderCraftScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.netherCraftContainer.get(), NetherCraftScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.neutronCollectorContainer.get(), NeutronCollectorScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.denseNeutronCollectorContainer.get(), DenseNeutronCollectorScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.doubleNeutronCollectorContainer.get(), DoubleNeutronCollectorScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.tripleNeutronCollectorContainer.get(), TripleNeutronCollectorScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.neutroniumCompressorContainer.get(), NeutroniumCompressorScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.CompressorChestContainer.get(), CompressorChestScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.infinityBoxContainer.get(), InfinityBoxScreen::new);

        });
        registerEntityRender();
        //TESR 方块实体渲染
        event.enqueueWork(() ->{
            ClientRegistry.bindTileEntityRenderer(EndlessTileTypes.COMPRESS_CHEST_TILE.get(), EndlessChestTileRender::new);
            ClientRegistry.bindTileEntityRenderer(EndlessTileTypes.INFINITY_CHEST_TILE.get(), EndlessChestTileRender::new);
        });
        //流体半透明渲染
        RenderTypeLookup.setRenderLayer(EndlessFluids.infinityFluid.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(EndlessFluids.infinityFluidFlowing.get(), RenderType.getTranslucent());
        event.enqueueWork(this::addLayer);
    }

    public void addLayer() {
        EntityRendererManager m = Minecraft.getInstance().getRenderManager();
        Stream.concat(m.getSkinMap().values().stream(), m.renderers.values().stream()).filter(LivingRenderer.class::isInstance).map(r ->
                (LivingRenderer)r).filter(render -> render.getEntityModel() instanceof PlayerModel).unordered().distinct().forEach(render -> {
            EntityRenderer<? extends PlayerEntity> rendererPlayer = render.getRenderManager().getSkinMap().get("default");
            EntityRenderer<? extends PlayerEntity> rendererPlayerSlim = render.getRenderManager().getSkinMap().get("slim");
            if (rendererPlayer != null) {
                LivingRenderer livingRenderer = (LivingRenderer) rendererPlayer;
                livingRenderer.addLayer(new InfinityArmorModel.PlayerRender(livingRenderer));
            }
            if (rendererPlayerSlim != null) {
                LivingRenderer livingRenderer = (LivingRenderer)rendererPlayerSlim;
                livingRenderer.addLayer(new InfinityArmorModel.PlayerRender(livingRenderer));
            }
        });
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

    private void registerEntityRender(){
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.ENDEST_PEARL.get(), manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer())); //投掷物渲染
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.INFINITY_ARROW.get(), InfinityArrowRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.INFINITY_FIREWORK.get(), InfinityFireWorkRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.INFINITY_ARROW_SUB.get(), InfinityArrowSubRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.GAPING_VOID.get(), GapingVoidRender::new); //渲染实体
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.INFINITY_MOB.get(), InfinityMobEntityRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRegistry.INFINITY_SWORD.get(), SwordVoidRender::new);
    }

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

    private void setCrossBowProperty(Item item){
        ItemModelsProperties.registerProperty(item, new ResourceLocation(Endless.MOD_ID,
                "pull"), (itemStack, clientWorld, livingEntity) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return InfinityCrossBow.isCharged(itemStack) ? 0.0F : (float)(itemStack.getUseDuration() - livingEntity.getItemInUseCount()) / (float) InfinityCrossBow.getChargeTime();
            }
        });
        ItemModelsProperties.registerProperty(item, new ResourceLocation(Endless.MOD_ID,
                "pulling"), (itemStack, clientWorld, livingEntity) -> livingEntity != null && livingEntity.isHandActive() && livingEntity.getActiveItemStack() == itemStack ? 1.0F : 0.0F);

        ItemModelsProperties.registerProperty(item, new ResourceLocation(Endless.MOD_ID,
                "charged"), (itemStack, clientWorld, livingEntity) -> livingEntity != null && InfinityCrossBow.isCharged(itemStack) ? 1f : 0f);
        ItemModelsProperties.registerProperty(item, new ResourceLocation(Endless.MOD_ID,
                "firework"), (itemStack, clientWorld, livingEntity) -> livingEntity != null && InfinityCrossBow.isCharged(itemStack) && InfinityCrossBow.hasChargedProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1f : 0f);
    }

    //物资团颜色变化
    private void setMatterClusterProperty(Item item){
        ItemModelsProperties.registerProperty(item, new ResourceLocation(Endless.MOD_ID,
                "num"), (itemStack, clientWorld, livingEntity) -> livingEntity != null && MatterCluster.getItemTag(itemStack).size() >= Config.SERVER.matterClusterMaxTerm.get() ? 1F : 0F);
    }
}
