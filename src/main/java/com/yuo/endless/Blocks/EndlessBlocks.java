package com.yuo.endless.Blocks;

import com.yuo.endless.Endless;
import com.yuo.endless.Fluid.EndlessFluids;
import com.yuo.endless.Fluid.InfinityFluidBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

//方块注册
public class EndlessBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Endless.MOD_ID);

    public static RegistryObject<Block> infinityBlock = BLOCKS.register("infinity_block",
            () -> new OrdinaryBlock(Material.METAL, 6, ToolActions.PICKAXE_DIG, 1000, 1000));
    public static RegistryObject<Block> crystalMatrixBlock = BLOCKS.register("crystal_matrix_block",
            () -> new OrdinaryBlock(Material.METAL, 4, ToolActions.PICKAXE_DIG, 10, 50));
    public static RegistryObject<Block> neutroniumBlock = BLOCKS.register("neutronium_block",
            () -> new OrdinaryBlock(Material.METAL, 5, ToolActions.PICKAXE_DIG, 15, 100));
    public static RegistryObject<Block> compressedCraftingTable = BLOCKS.register("compressed_crafting_table",
            () -> new OrdinaryBlock(Material.WOOD, 0, ToolActions.AXE_DIG, 5, 5));
    public static RegistryObject<Block> doubleCompressedCraftingTable = BLOCKS.register("double_compressed_crafting_table",
            () -> new OrdinaryToolBlock(Material.WOOD, 1, ToolActions.AXE_DIG, 5, 5));
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
