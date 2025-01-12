package com.yuo.endless.Proxy;

import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Client.Gui.*;
import com.yuo.endless.Client.Model.CosmicModelLoader;
import com.yuo.endless.Client.Model.HaloItemModelLoader;
import com.yuo.endless.Client.Model.InfinityArmorModel;
import com.yuo.endless.Client.Render.EndlessChestTileRender;
import com.yuo.endless.Config;
import com.yuo.endless.Container.EndlessMenuTypes;
import com.yuo.endless.Endless;
import com.yuo.endless.Fluid.EndlessFluids;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.MatterCluster;
import com.yuo.endless.Items.Tool.InfinityCrossBow;
import com.yuo.endless.Tiles.EndlessTileTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
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
        ModelLoaderRegistry.registerLoader(new ResourceLocation(Endless.MOD_ID, "cosmic"), new CosmicModelLoader());
        ModelLoaderRegistry.registerLoader(new ResourceLocation(Endless.MOD_ID, "halo"), new HaloItemModelLoader());
        AvaritiaShaders.init();
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::addLayers);
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
    }

    public void addLayers(EntityRenderersEvent.AddLayers e) {
        addLayer(e, "default");
        addLayer(e, "slim");
    }

    public void addLayer(EntityRenderersEvent.AddLayers e, String s) {
        LivingEntityRenderer r = e.getSkin(s);
        r.addLayer(new InfinityArmorModel.PlayerRender(r));
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
