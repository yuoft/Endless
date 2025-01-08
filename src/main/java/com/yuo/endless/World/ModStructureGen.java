package com.yuo.endless.World;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.Objects;
import java.util.Set;

public class ModStructureGen {
    public static void genStructures(final BiomeLoadingEvent event) {
        ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, Objects.requireNonNull(event.getName()));
        Set<Type> types = BiomeDictionary.getTypes(key);

        if(types.contains(BiomeDictionary.Type.PLAINS)) {
//            List<Supplier<StructureFeature<?, ?>>> structures = event.getGeneration().getStructures();
//
//            structures.add(() -> ModStructures.INFINITY_MOB.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        }
    }
}
