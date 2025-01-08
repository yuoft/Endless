package com.yuo.endless.World;

import com.mojang.serialization.Codec;
import com.yuo.endless.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Entity.EntityRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Method;

@Mod.EventBusSubscriber(modid = Endless.MOD_ID)
public class ModWorldEvent {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        ModStructureGen.genStructures(event);
        if (Config.SERVER.mobSpawn.get()) //默认不生成
            ModEntityGen.addEntityToAllBiomes(event, EntityRegistry.INFINITY_MOB.get(), Config.SERVER.mobWeigh.get(), 1,1);
    }

//    @SubscribeEvent
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerLevel serverLevel) {

            try {
                Method GETCODEC_METHOD =
                        ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey(
                        (Codec<? extends ChunkGenerator>)GETCODEC_METHOD.invoke(serverLevel.getChunkSource().getGenerator()));

                if (cgRL != null && cgRL.getNamespace().equals("terraforged")) {
                    return;
                }
            } catch (Exception e) {
                LogManager.getLogger().error("Was unable to check if " + serverLevel.dimension().location()
                        + " is using Terraforged's ChunkGenerator.");
            }

            if (serverLevel.getChunkSource().getGenerator() instanceof FlatLevelSource &&
                    serverLevel.dimension().equals(Level.OVERWORLD)) {
                return;
            }

//            Map<Structure<?>, StructureSeparationSettings> tempMap =
//                    new HashMap<>(serverLevel.getChunkSource().getGenerator().func_235957_b_().func_236195_a_());
//            tempMap.putIfAbsent(ModStructures.INFINITY_MOB.get(),
//                    DimensionStructuresSettings.field_236191_b_.get(ModStructures.INFINITY_MOB.get()));
//            serverLevel.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
        }
    }
}
