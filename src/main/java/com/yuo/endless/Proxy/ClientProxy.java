package com.yuo.endless.Proxy;

import com.yuo.endless.Container.ContainerTypeRegistry;
import com.yuo.endless.Endless;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Gui.*;
import com.yuo.endless.Items.ItemRegistry;
import com.yuo.endless.Items.MatterCluster;
import com.yuo.endless.Render.GapingVoidRender;
import com.yuo.endless.Render.InfinityArrowRender;
import com.yuo.endless.Render.InfinityArrowSubRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
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
        registerEntityRender();

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


    @Override
    public void registerHandlers() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::clientSetup);
    }

}
