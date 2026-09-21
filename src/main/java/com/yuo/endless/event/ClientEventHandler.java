package com.yuo.endless.event;

import com.yuo.endless.client.AvaritiaShaders;
import com.yuo.endless.client.model.CosmicModelLoader;
import com.yuo.endless.client.model.HaloItemModelLoader;
import com.yuo.endless.client.model.InfinityArmorModel;
import com.yuo.endless.client.render.*;
import com.yuo.endless.Endless;
import com.yuo.endless.entity.EndlessEntityTypes;
import com.yuo.endless.entity.InfinityMobEntity;
import com.yuo.endless.items.EndlessItems;
import com.yuo.endless.items.Singularity;
import com.yuo.endless.EndlessUtils;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

/**
 * 客户端事件
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Endless.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventHandler {
    public static final ModelLayerLocation COMPRESSOR_CHEST_TEXTURE = new ModelLayerLocation(EndlessUtils.fa("block/chest/compressor_chest"), "main");
    public static final ModelLayerLocation INFINITY_CHEST_TEXTURE = new ModelLayerLocation(EndlessUtils.fa("block/chest/infinity_chest"), "main");
    public static final ModelLayerLocation NORMAL_CHEST_LOCATION = new ModelLayerLocation(EndlessUtils.parse("entity/chest/normal"), "main");

    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event) {
        event.register(
                EndlessEntityTypes.INFINITY_MOB.get(), // 你的 EntityType
                SpawnPlacements.Type.ON_GROUND,        // 生成位置类型：地面
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, // 高度图类型
                InfinityMobEntity::checkInfinityMobSpawnRules, // 你的检查方法
                SpawnPlacementRegisterEvent.Operation.REPLACE // 操作类型
        );
    }

    //箱子贴图
    @SubscribeEvent
    public static void registerEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(COMPRESSOR_CHEST_TEXTURE, EndlessChestTileRender::createSingleBodyLayer);
        event.registerLayerDefinition(INFINITY_CHEST_TEXTURE, EndlessChestTileRender::createSingleBodyLayer);
    }

    //染色
    @SubscribeEvent
    public static void itemColors(RegisterColorHandlersEvent.Item event) {
        for (RegistryObject<Item> entry : EndlessItems.ITEMS.getEntries()) {
            Item item = entry.get();
            if (item instanceof Singularity){
                event.getItemColors().register(Singularity::getColor, item);
            }
        }
    }

    //实体渲染注册
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EndlessEntityTypes.ENDEST_PEARL.get(), ThrownItemRenderer::new); //投掷物渲染
        event.registerEntityRenderer(EndlessEntityTypes.INFINITY_ARROW.get(), InfinityArrowRender::new);
        event.registerEntityRenderer(EndlessEntityTypes.INFINITY_FIREWORK.get(), InfinityFireWorkRender::new);
        event.registerEntityRenderer(EndlessEntityTypes.INFINITY_ARROW_SUB.get(), InfinityArrowSubRender::new);

        event.registerEntityRenderer(EndlessEntityTypes.GAPING_VOID.get(), GapingVoidRender::new); //渲染实体
        event.registerEntityRenderer(EndlessEntityTypes.INFINITY_MOB.get(), InfinityMobEntityRender::new);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegisterShaders(RegisterShadersEvent event) {
        AvaritiaShaders.init(event);//注册着色器
        GapingVoidShaders.register(event);
    }
    @SubscribeEvent
    public static void registerLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("cosmic", CosmicModelLoader.INSTANCE);
        event.register("halo", HaloItemModelLoader.INSTANCE);
    }

    @SubscribeEvent
    public static void addPlayerLayer(EntityRenderersEvent.AddLayers event) {
        addLayer(event, "default");
        addLayer(event, "slim");

//        LivingEntityRenderer entityRenderer = event.getRenderer(EntityType.CREEPER);
//        entityRenderer.addLayer(new MobLayer(entityRenderer));
    }

    private static void addLayer(EntityRenderersEvent.AddLayers e, String s) {
        LivingEntityRenderer<Player, PlayerModel<Player>> entityRenderer = e.getSkin(s);
        if (entityRenderer != null) {
            entityRenderer.addLayer(new InfinityArmorModel.PlayerRender(entityRenderer));
        }
    }
}
