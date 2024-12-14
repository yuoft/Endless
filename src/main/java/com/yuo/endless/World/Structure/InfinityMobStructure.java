package com.yuo.endless.World.Structure;

import com.yuo.endless.Endless;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class InfinityMobStructure extends Structure<NoFeatureConfig> {

    public InfinityMobStructure() {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public Decoration getDecorationStage() {
        return Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return InfinityMobStructure.Start::new;
    }

    @Override
    protected boolean func_230363_a_(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider, long seed, SharedSeedRandom random, int x, int z, Biome biome, ChunkPos chunkPos, NoFeatureConfig config) {
        BlockPos blockPos = new BlockPos((x << 4) + 7, 0, (z << 4) + 8);
        int height = chunkGenerator.getHeight(blockPos.getX(), blockPos.getZ(), Type.WORLD_SURFACE_WG);
        IBlockReader reader = chunkGenerator.func_230348_a_(blockPos.getX(), blockPos.getZ());
        BlockState top = reader.getBlockState(blockPos.up(height));
        return top.getFluidState().isEmpty(); //不生成在流体位置
    }

    public static class Start extends StructureStart<NoFeatureConfig>{

        public Start(Structure<NoFeatureConfig> structure, int x, int z, MutableBoundingBox box, int reference, long seed) {
            super(structure, x, z, box, reference, seed);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistries, ChunkGenerator chunkGenerator, TemplateManager templateManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;
            BlockPos blockPos = new BlockPos(x, 0, z);

            JigsawManager.func_242837_a(dynamicRegistries, new VillageConfig(() -> dynamicRegistries.getRegistry(Registry.JIGSAW_POOL_KEY)
                    .getOrDefault(new ResourceLocation(Endless.MOD_ID, "infinity_mob/start_pool")), 10),
                    AbstractVillagePiece::new, chunkGenerator, templateManager, blockPos, this.components, this.rand, false, true);

            this.components.forEach(e -> e.offset(0, 1, 0));
            this.components.forEach(e -> e.getBoundingBox().maxY -= 1);

            this.recalculateStructureSize();


            LogManager.getLogger().log(Level.DEBUG, "Infinity Mob at " +
                    this.components.get(0).getBoundingBox().minX + " " +
                    this.components.get(0).getBoundingBox().minY + " " +
                    this.components.get(0).getBoundingBox().minZ);
        }
    }
}
