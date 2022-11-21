package com.yuo.endless.Blocks;

import com.yuo.endless.Endless;
import com.yuo.endless.Fluid.EndlessFluids;
import com.yuo.endless.Fluid.InfinityFluidBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//方块注册
public class EndlessBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Endless.MOD_ID);

    public static RegistryObject<Block> infinityBlock = BLOCKS.register("infinity_block",
            () -> new OrdinaryBlock(Material.IRON, 6, ToolType.PICKAXE, 1000, 1000));
    public static RegistryObject<Block> crystalMatrixBlock = BLOCKS.register("crystal_matrix_block",
            () -> new OrdinaryBlock(Material.IRON, 4, ToolType.PICKAXE, 10, 50));
    public static RegistryObject<Block> neutroniumBlock = BLOCKS.register("neutronium_block",
            () -> new OrdinaryBlock(Material.IRON, 5, ToolType.PICKAXE, 15, 100));
    public static RegistryObject<Block> compressedCraftingTable = BLOCKS.register("compressed_crafting_table",
            () -> new OrdinaryBlock(Material.WOOD, 0, ToolType.AXE, 5, 5));
    public static RegistryObject<Block> doubleCompressedCraftingTable = BLOCKS.register("double_compressed_crafting_table",
            () -> new OrdinaryBlock(Material.WOOD, 1, ToolType.AXE, 5, 5));
    public static RegistryObject<Block> extremeCraftingTable = BLOCKS.register("extreme_crafting_table", ExtremeCraft::new);
    public static RegistryObject<Block> neutroniumCollector = BLOCKS.register("neutronium_collector", NeutronCollector::new);
    public static RegistryObject<Block> denseNeutroniumCollector = BLOCKS.register("dense_neutronium_collector", DenseNeutronCollector::new);
    public static RegistryObject<Block> denserNeutroniumCollector = BLOCKS.register("denser_neutronium_collector", DoubleNeutronCollector::new);
    public static RegistryObject<Block> densestNeutroniumCollector = BLOCKS.register("densest_neutronium_collector", TripleNeutronCollector::new);
    public static RegistryObject<Block> neutronCompressor = BLOCKS.register("neutron_compressor", NeutroniumCompressor::new);
    public static RegistryObject<Block> compressedChest = BLOCKS.register("compressed_chest", CompressorChest::new);
    public static RegistryObject<Block> infinityBox = BLOCKS.register("infinity_chest", InfinityBox::new);

    public static RegistryObject<InfinityFluidBlock> infinityFluid = BLOCKS.register("infinity_fluid",
            () -> new InfinityFluidBlock(EndlessFluids.infinityFluid));

}
