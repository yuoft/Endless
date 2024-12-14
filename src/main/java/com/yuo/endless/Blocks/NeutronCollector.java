package com.yuo.endless.Blocks;

import com.yuo.endless.Tiles.EndlessTileTypes;
import com.yuo.endless.Tiles.NeutronCollectorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class NeutronCollector extends AbsNeutronCollector{

    public NeutronCollector() {
        super();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NeutronCollectorTile(blockPos, blockState);
    }

    @Override
    protected void openContainer(Level worldIn, BlockPos pos, Player player) {
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (tile instanceof NeutronCollectorTile) {
            player.openMenu((MenuProvider)tile);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return createTicker(level, entityType, EndlessTileTypes.NEUTRON_COLLECTOR_TILE.get());
    }
}
