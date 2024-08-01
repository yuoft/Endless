package com.yuo.endless.World;

import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.MobSpawnInfo.Spawners;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.Random;

public class ModEntityGen {
    //所有群系
    public static void addEntityToAllBiomes(BiomeLoadingEvent event, EntityType<?> type,
                                             int weight, int minCount, int maxCount) {
        Random rand = new Random();
        if (rand.nextDouble() < 0.1 * weight){ //降低生成概率
            List<Spawners> base = event.getSpawns().getSpawner(type.getClassification());
            base.add(new MobSpawnInfo.Spawners(type,weight, minCount, maxCount));
        }
    }
}
