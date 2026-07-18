package com.yuo.endless.blocks;

import com.yuo.endless.tiles.CosmicTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CosmicBlock extends Block implements EntityBlock {
    public CosmicBlock() {
        super(Properties.copy(Blocks.DIAMOND_BLOCK));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CosmicTile(blockPos, blockState);
    }
}
