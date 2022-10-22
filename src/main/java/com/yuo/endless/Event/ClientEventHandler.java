package com.yuo.endless.Event;

import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Singularity;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

/**
 * Description: 客户端事件
 * Author: cnlimiter yuo
 * Date: 2022/5/21 23:27
 * Version: 1.0
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Endless.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventHandler {
    public static final ResourceLocation COMPRESSOR_CHEST_TEXTURE = new ResourceLocation(Endless.MOD_ID, "entity/compressor_chest");
    public static final ResourceLocation INFINITY_CHEST_TEXTURE = new ResourceLocation(Endless.MOD_ID, "entity/infinity_chest");
    public static final ResourceLocation NORMAL_CHEST_LOCATION = new ResourceLocation("entity/chest/normal");

    public static ResourceLocation chooseChestTexture(EndlessChestType type) {
        switch (type) {
            case COMPRESSOR:
                return COMPRESSOR_CHEST_TEXTURE;
            case INFINITY:
                return INFINITY_CHEST_TEXTURE;
            default:
                return NORMAL_CHEST_LOCATION;
        }
    }

    //箱子贴图
    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
            event.addSprite(COMPRESSOR_CHEST_TEXTURE);
            event.addSprite(INFINITY_CHEST_TEXTURE);
        }
    }

    //染色
    @SubscribeEvent
    public static void itemColors(ColorHandlerEvent.Item event) {
        // 第二个参数代表“所有需要使用此 IItemColor 的物品”，是一个 var-arg Item。
        // 有鉴于第一个参数是一个只有一个方法的接口，我们也可以直接在这里使用 lambda 表达式。
        for (RegistryObject<Item> entry : EndlessItems.ITEMS.getEntries()) {
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

}
