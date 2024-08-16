package com.yuo.endless.Event;

import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Singularity;
import com.yuo.endless.Client.Render.Pulse.PulseModel;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Map;

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

    //物品脉冲效果
    @SubscribeEvent
    public  static void registerModel(ModelBakeEvent event){
        Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();
        ArrayList<ModelResourceLocation> locations = new ArrayList<>();
        //获取物品模型资源
        ModelResourceLocation location = new ModelResourceLocation(EndlessItems.infinityIngot.get().getRegistryName(), "inventory");
        ModelResourceLocation location0 = new ModelResourceLocation(EndlessItems.infinityCatalyst.get().getRegistryName(), "inventory");
        ModelResourceLocation location1 = new ModelResourceLocation(EndlessItems.endestPearl.get().getRegistryName(), "inventory");
        locations.add(0, location);
        locations.add(1, location0);
        locations.add(2, location1);
        //获取模型
        IBakedModel model = registry.get(location);
        IBakedModel model0 = registry.get(location0);
        IBakedModel model1 = registry.get(location1);
        ArrayList<IBakedModel> models= new ArrayList<>();
        models.add(0,model);
        models.add(1,model0);
        models.add(2,model1);
        for (int i = 0; i < locations.size(); i++) {
            if (models.get(i) == null) throw new RuntimeException("error_null + " + i);
            else if (models.get(i) instanceof PulseModel) throw  new RuntimeException("error_model + " + i);
            else {
                //将脉冲模型添加到注册器
                event.getModelRegistry().put(locations.get(i), new PulseModel(models.get(i)));
            }
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
