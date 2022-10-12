package com.yuo.endless.Event;

import com.yuo.endless.Items.ItemRegistry;
import com.yuo.endless.Render.InfinityEyeLayer;
import com.yuo.endless.Render.InfinityWingLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.ArmorStandModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientHandler {
    /**
     * 生物渲染事件，可以为原本生物添加layer或取消渲染
     * @param event 生物渲染
     */
//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent
    public static void addLayer(RenderLivingEvent.Pre<PlayerEntity, PlayerModel<PlayerEntity>> event){
        LivingEntity entity = event.getEntity();
        if (entity instanceof PlayerEntity){
            addLayer(event.getRenderer());
        }
    }

    private static void addLayer(LivingRenderer<PlayerEntity, PlayerModel<PlayerEntity>> renderer) {
        EntityRendererManager renderManager = renderer.getRenderManager();
        Map<String, PlayerRenderer> skinMap = renderManager.getSkinMap();
        PlayerRenderer render;
        render = skinMap.get("default");
        render.addLayer(new InfinityEyeLayer<>(render));
        render.addLayer(new InfinityWingLayer<>(render));

        render = skinMap.get("slim");
        render.addLayer(new InfinityEyeLayer<>(render));
        render.addLayer(new InfinityWingLayer<>(render));
    }
}
