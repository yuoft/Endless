package com.yuo.endless.World.Structure;

import com.yuo.endless.Endless;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModStructures {

    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Endless.MOD_ID);


    public static final RegistryObject<StructureFeature<?>> INFINITY_MOB = STRUCTURES.register("infinity_mob", InfinityMobStructure::new);


}
