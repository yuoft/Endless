package com.yuo.endless.Blocks;

import com.yuo.endless.Tiles.EndlessTileTypes;
import com.yuo.endless.Tiles.InfinityBoxTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;

public class InfinityBox extends AbsEndlessChest {
    public InfinityBox() {
        super(EndlessTileTypes.INFINITY_CHEST_TILE::get, EndlessChestType.INFINITY,
                Properties.of(Material.METAL).strength(10f, 1000f).requiresCorrectToolForDrops());
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InfinityBoxTile(blockPos, blockState);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> entityType) {
        return createTicker(level, entityType, EndlessTileTypes.INFINITY_CHEST_TILE.get());
    }

    //创建服务端tick
    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createTicker(Level level, BlockEntityType<T> serverType, BlockEntityType<? extends InfinityBoxTile> entityType) {
        return level.isClientSide ? null : createTickerHelper(serverType, entityType, InfinityBoxTile::serverTick);
    }
}
