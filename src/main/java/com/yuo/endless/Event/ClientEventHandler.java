package com.yuo.endless.Event;

import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Client.Render.*;
import com.yuo.endless.Endless;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Singularity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

/**
 * 客户端事件
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Endless.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventHandler {
    public static final ResourceLocation COMPRESSOR_CHEST_TEXTURE = new ResourceLocation(Endless.MOD_ID, "entity/compressor_chest");
    public static final ResourceLocation INFINITY_CHEST_TEXTURE = new ResourceLocation(Endless.MOD_ID, "entity/infinity_chest");
    public static final ResourceLocation NORMAL_CHEST_LOCATION = new ResourceLocation("entity/chest/normal");

    public static ResourceLocation chooseChestTexture(EndlessChestType type) {
        return switch (type) {
            case COMPRESSOR -> COMPRESSOR_CHEST_TEXTURE;
            case INFINITY -> INFINITY_CHEST_TEXTURE;
            default -> NORMAL_CHEST_LOCATION;
        };
    }

    //箱子贴图
    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(Sheets.CHEST_SHEET)) {
            event.addSprite(COMPRESSOR_CHEST_TEXTURE);
            event.addSprite(INFINITY_CHEST_TEXTURE);
        }
    }

    //染色
    @SubscribeEvent
    public static void itemColors(ColorHandlerEvent.Item event) {
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
        event.registerEntityRenderer(EntityRegistry.ENDEST_PEARL.get(), ThrownItemRenderer::new); //投掷物渲染
        event.registerEntityRenderer(EntityRegistry.INFINITY_ARROW.get(), InfinityArrowRender::new);
        event.registerEntityRenderer(EntityRegistry.INFINITY_FIREWORK.get(), InfinityFireWorkRender::new);
        event.registerEntityRenderer(EntityRegistry.INFINITY_ARROW_SUB.get(), InfinityArrowSubRender::new);

        event.registerEntityRenderer(EntityRegistry.GAPING_VOID.get(), (Context renderManagerIn) -> new GapingVoidRender(renderManagerIn)); //渲染实体
        event.registerEntityRenderer(EntityRegistry.INFINITY_MOB.get(), InfinityMobEntityRender::new);
    }
}
