package com.yuo.endless.Event;

import com.yuo.endless.Endless;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Entity.InfinityMobEntity;
import com.yuo.endless.Items.Tool.InfinityDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Endless.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EndlessMobEvent {

    //实体属性
    @SubscribeEvent
    public static void onRegisterEntitiesAttr(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.INFINITY_MOB.get(), InfinityMobEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> future = event.getLookupProvider();
        generator.addProvider(true, new DamageTypeInit(generator.getPackOutput(), future));
    }

    public static class DamageTypeInit extends DatapackBuiltinEntriesProvider {

        public DamageTypeInit(PackOutput output, CompletableFuture<Provider> future) {
            super(output, future, InfinityDamageTypes.DAMAGE_BUILDER, Set.of("minecraft", Endless.MOD_ID));
        }
    }
}
