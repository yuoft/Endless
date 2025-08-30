package com.yuo.endless.Event;

import com.yuo.endless.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Entity.InfinityMobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Endless.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EndlessMobEvent {

//    //注册刷怪蛋
//    @SubscribeEvent
//    public static void onRegisterEntities(RegistryEvent.Register<EntityType<?>> event) {
////        ModSpawnEgg.initSpawnEggs();
//    }

    //实体属性
    @SubscribeEvent
    public static void onRegisterEntitiesAttr(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.INFINITY_MOB.get(), InfinityMobEntity.createAttributes().build());
    }
}
