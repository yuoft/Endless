package com.yuo.endless.entity.spawn;

import com.mojang.serialization.Codec;
import com.yuo.endless.Endless;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EndlessBiomeModifiers {
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Endless.MOD_ID);

    public static final RegistryObject<Codec<? extends BiomeModifier>> INFINITY_MOB_SPAWN = BIOME_MODIFIER_SERIALIZERS.register("infinity_mob_spawn",
            () -> InfinityMobSpawnModifier.CODEC);
}
