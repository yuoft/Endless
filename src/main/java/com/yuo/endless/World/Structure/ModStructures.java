package com.yuo.endless.World.Structure;

import com.yuo.endless.Endless;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructures {

    public static final DeferredRegister<Structure> STRUCTURES = DeferredRegister.create(Registries.STRUCTURE, Endless.MOD_ID);

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, Endless.MOD_ID);

    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, Endless.MOD_ID);

//    public static final RegistryObject<StructurePieceType> STRUCTURE_PIECE = STRUCTURE_PIECE_TYPES.register("infinity_mob_piece", InfinityMobPieces.MyStructurePiece::new);

    public static final RegistryObject<StructureType<InfinityMobStructure>> INFINITY_MOB_TYPE = STRUCTURE_TYPES.register("infinity_mob_type", () -> () -> InfinityMobStructure.CODEC);

//    public static final RegistryObject<Structure> INFINITY_MOB = STRUCTURES.register("infinity_mob", InfinityMobStructure::new);


}
