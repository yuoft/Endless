package com.yuo.endless.entity.spawn;

import com.mojang.serialization.Codec;
import com.yuo.endless.config.ModConfig;
import com.yuo.endless.entity.EndlessEntityTypes;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public class InfinityMobSpawnModifier implements BiomeModifier {

    public static final Codec<InfinityMobSpawnModifier> CODEC = Codec.unit(InfinityMobSpawnModifier::new);

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            if (ModConfig.SERVER.mobSpawn.get() && (biome.is(BiomeTags.IS_OVERWORLD) || biome.is(BiomeTags.IS_NETHER) || biome.is(BiomeTags.IS_END))) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER,
                        new MobSpawnSettings.SpawnerData(EndlessEntityTypes.INFINITY_MOB.get(), ModConfig.SERVER.mobWeigh.get(), 0, 1));
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}