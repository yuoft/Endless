package com.yuo.endless.Entity;

import com.yuo.endless.Endless;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Endless.MOD_ID)
public class EntityGen {
    @SubscribeEvent
    public static void onEntitySpawn(final BiomeLoadingEvent event) {
        addEntityToAllBiomes(event, EntityRegistry.INFINITY_MOB.get(), 10, 1,1);
    }
    //所有群系
    private static void addEntityToAllBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                             int weight, int minCount, int maxCount) {
        List<MobSpawnInfo.Spawners> base = event.getSpawns().getSpawner(type.getClassification());
        base.add(new MobSpawnInfo.Spawners(type,weight, minCount, maxCount));
    }
}
