package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.BlockRegistry;
import com.yuo.endless.Endless;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//方块实体类型注册
public class TileTypeRegistry {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Endless.MOD_ID);

    public static final RegistryObject<TileEntityType<ExtremeCraftTile>> EXTREME_CRAFT_TILE = TILE_ENTITIES.register("extreme_craft_tile",
            () -> TileEntityType.Builder.create(ExtremeCraftTile::new, BlockRegistry.extremeCraftingTable.get()).build(null));
    public static final RegistryObject<TileEntityType<NeutronCollectorTile>> NEUTRON_COLLECTOR_TILE = TILE_ENTITIES.register("neutron_collector_tile",
            () -> TileEntityType.Builder.create(NeutronCollectorTile::new, BlockRegistry.neutronCollector.get()).build(null));
    public static final RegistryObject<TileEntityType<DenseNeutronCollectorTile>> DENSE_NEUTRON_COLLECTOR_TILE = TILE_ENTITIES.register("dense_neutron_collector_tile",
            () -> TileEntityType.Builder.create(DenseNeutronCollectorTile::new, BlockRegistry.denseNeutronCollector.get()).build(null));
    public static final RegistryObject<TileEntityType<DoubleNeutronCollectorTile>> DOUBLE_NEUTRON_COLLECTOR_TILE = TILE_ENTITIES.register("double_neutron_collector_tile",
            () -> TileEntityType.Builder.create(DoubleNeutronCollectorTile::new, BlockRegistry.doubleNeutronCollector.get()).build(null));
    public static final RegistryObject<TileEntityType<TripleNeutronCollectorTile>> TRIPLE_NEUTRON_COLLECTOR_TILE = TILE_ENTITIES.register("triple_neutron_collector_tile",
            () -> TileEntityType.Builder.create(TripleNeutronCollectorTile::new, BlockRegistry.tripleNeutronCollector.get()).build(null));
    public static final RegistryObject<TileEntityType<NeutroniumCompressorTile>> NEUTRONIUM_COOMPRESSOR_TILE = TILE_ENTITIES.register("neutronium_compressor_tile",
            () -> TileEntityType.Builder.create(NeutroniumCompressorTile::new, BlockRegistry.neutroniumCompressor.get()).build(null));

}
