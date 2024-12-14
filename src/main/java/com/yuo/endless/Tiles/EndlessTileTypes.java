package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Endless;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

//方块实体类型注册
public class EndlessTileTypes {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Endless.MOD_ID);

    public static final RegistryObject<BlockEntityType<ExtremeCraftTile>> EXTREME_CRAFT_TILE = TILE_ENTITIES.register("extreme_craft_tile",
            () -> BlockEntityType.Builder.of(ExtremeCraftTile::new, EndlessBlocks.extremeCraftingTable.get()).build(null));
    public static final RegistryObject<BlockEntityType<NeutronCollectorTile>> NEUTRON_COLLECTOR_TILE = TILE_ENTITIES.register("neutron_collector_tile",
            () -> BlockEntityType.Builder.of(NeutronCollectorTile::new, EndlessBlocks.neutroniumCollector.get()).build(null));
    public static final RegistryObject<BlockEntityType<DenseNeutronCollectorTile>> DENSE_NEUTRON_COLLECTOR_TILE = TILE_ENTITIES.register("dense_neutron_collector_tile",
            () -> BlockEntityType.Builder.of(DenseNeutronCollectorTile::new, EndlessBlocks.denseNeutroniumCollector.get()).build(null));
    public static final RegistryObject<BlockEntityType<DoubleNeutronCollectorTile>> DOUBLE_NEUTRON_COLLECTOR_TILE = TILE_ENTITIES.register("double_neutron_collector_tile",
            () -> BlockEntityType.Builder.of(DoubleNeutronCollectorTile::new, EndlessBlocks.denserNeutroniumCollector.get()).build(null));
    public static final RegistryObject<BlockEntityType<TripleNeutronCollectorTile>> TRIPLE_NEUTRON_COLLECTOR_TILE = TILE_ENTITIES.register("triple_neutron_collector_tile",
            () -> BlockEntityType.Builder.of(TripleNeutronCollectorTile::new, EndlessBlocks.densestNeutroniumCollector.get()).build(null));
    public static final RegistryObject<BlockEntityType<NeutroniumCompressorTile>> NEUTRONIUM_COMPRESSOR_TILE = TILE_ENTITIES.register("neutronium_compressor_tile",
            () -> BlockEntityType.Builder.of(NeutroniumCompressorTile::new, EndlessBlocks.neutronCompressor.get()).build(null));
    public static final RegistryObject<BlockEntityType<CompressorChestTile>> COMPRESS_CHEST_TILE = TILE_ENTITIES.register("compressor_chest_tile",
            () -> BlockEntityType.Builder.of(CompressorChestTile::new, EndlessBlocks.compressedChest.get()).build(null));
    public static final RegistryObject<BlockEntityType<InfinityBoxTile>> INFINITY_CHEST_TILE = TILE_ENTITIES.register("infinity_chest_tile",
            () -> BlockEntityType.Builder.of(InfinityBoxTile::new, EndlessBlocks.infinityBox.get()).build(null));
}
