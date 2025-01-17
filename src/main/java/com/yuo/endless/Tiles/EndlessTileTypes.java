package com.yuo.endless.Tiles;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Endless;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//方块实体类型注册
public class EndlessTileTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Endless.MOD_ID);

    public static final RegistryObject<TileEntityType<ExtremeCraftTile>> EXTREME_CRAFT_TILE = TILE_ENTITIES.register("extreme_craft_tile",
            () -> TileEntityType.Builder.create(ExtremeCraftTile::new, EndlessBlocks.extremeCraftingTable.get()).build(null));
    public static final RegistryObject<TileEntityType<EnderCraftTile>> ENDER_CRAFT_TILE = TILE_ENTITIES.register("ender_craft_tile",
            () -> TileEntityType.Builder.create(EnderCraftTile::new, EndlessBlocks.enderCraftingTable.get()).build(null));
    public static final RegistryObject<TileEntityType<NetherCraftTile>> NETHER_CRAFT_TILE = TILE_ENTITIES.register("nether_craft_tile",
            () -> TileEntityType.Builder.create(NetherCraftTile::new, EndlessBlocks.netherCraftingTable.get()).build(null));
    public static final RegistryObject<TileEntityType<NeutronCollectorTile>> NEUTRON_COLLECTOR_TILE = TILE_ENTITIES.register("neutron_collector_tile",
            () -> TileEntityType.Builder.create(NeutronCollectorTile::new, EndlessBlocks.neutroniumCollector.get()).build(null));
    public static final RegistryObject<TileEntityType<DenseNeutronCollectorTile>> DENSE_NEUTRON_COLLECTOR_TILE = TILE_ENTITIES.register("dense_neutron_collector_tile",
            () -> TileEntityType.Builder.create(DenseNeutronCollectorTile::new, EndlessBlocks.denseNeutroniumCollector.get()).build(null));
    public static final RegistryObject<TileEntityType<DoubleNeutronCollectorTile>> DOUBLE_NEUTRON_COLLECTOR_TILE = TILE_ENTITIES.register("double_neutron_collector_tile",
            () -> TileEntityType.Builder.create(DoubleNeutronCollectorTile::new, EndlessBlocks.denserNeutroniumCollector.get()).build(null));
    public static final RegistryObject<TileEntityType<TripleNeutronCollectorTile>> TRIPLE_NEUTRON_COLLECTOR_TILE = TILE_ENTITIES.register("triple_neutron_collector_tile",
            () -> TileEntityType.Builder.create(TripleNeutronCollectorTile::new, EndlessBlocks.densestNeutroniumCollector.get()).build(null));
    public static final RegistryObject<TileEntityType<NeutroniumCompressorTile>> NEUTRONIUM_COMPRESSOR_TILE = TILE_ENTITIES.register("neutronium_compressor_tile",
            () -> TileEntityType.Builder.create(NeutroniumCompressorTile::new, EndlessBlocks.neutronCompressor.get()).build(null));
    public static final RegistryObject<TileEntityType<CompressorChestTile>> COMPRESS_CHEST_TILE = TILE_ENTITIES.register("compressor_chest_tile",
            () -> TileEntityType.Builder.create(CompressorChestTile::new, EndlessBlocks.compressedChest.get()).build(null));
    public static final RegistryObject<TileEntityType<InfinityBoxTile>> INFINITY_CHEST_TILE = TILE_ENTITIES.register("infinity_chest_tile",
            () -> TileEntityType.Builder.create(InfinityBoxTile::new, EndlessBlocks.infinityBox.get()).build(null));
}
