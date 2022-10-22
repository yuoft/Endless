package com.yuo.endless.Proxy;

import com.yuo.endless.Config.Config;
import com.yuo.endless.Container.ContainerTypeRegistry;
import com.yuo.endless.Endless;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Gui.*;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.MatterCluster;
import com.yuo.endless.Items.Tool.InfinityCrossBow;
import com.yuo.endless.Render.*;
import com.yuo.endless.Tiles.TileTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
            ClientRegistry.bindTileEntityRenderer(TileTypeRegistry.COMPRESS_CHEST_TILE.get(), EndlessChestTileRender::new);
            ClientRegistry.bindTileEntityRenderer(TileTypeRegistry.INFINITY_CHEST_TILE.get(), EndlessChestTileRender::new);
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
                return InfinityCrossBow.isCharged(itemStack) ? 0.0F : (float)(itemStack.getUseDuration() - livingEntity.getItemInUseCount()) / (float) InfinityCrossBow.getChargeTime(itemStack);
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
                "count"), (itemStack, clientWorld, livingEntity) -> MatterCluster.getItemTag(itemStack).size() > 0 ?
                (MatterCluster.getItemTag(itemStack).size() == Config.SERVER.matterClusterMaxTerm.get() ? 1f : 0.5f) :0f);
    }
}
