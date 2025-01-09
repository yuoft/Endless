package com.yuo.endless.Blocks;

import com.yuo.endless.Tiles.DoubleNeutronCollectorTile;
import com.yuo.endless.Tiles.EndlessTileTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class DoubleNeutronCollector extends AbsNeutronCollector{

    public DoubleNeutronCollector() {
        super();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DoubleNeutronCollectorTile(blockPos, blockState);
    }

    @Override
    protected void openContainer(Level worldIn, BlockPos pos, Player player) {
        BlockEntity tile = worldIn.getBlockEntity(pos);
        if (tile instanceof DoubleNeutronCollectorTile) {
            NetworkHooks.openGui((ServerPlayer) player, (DoubleNeutronCollectorTile) tile, pos);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return createTicker(level, entityType, EndlessTileTypes.DOUBLE_NEUTRON_COLLECTOR_TILE.get());
    }
}
