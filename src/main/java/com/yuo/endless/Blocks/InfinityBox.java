package com.yuo.endless.Blocks;

import com.yuo.endless.Tiles.EndlessTileTypes;
import com.yuo.endless.Tiles.InfinityBoxTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.BlockGetter;
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
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        if (player.getMainHandItem().getItem() instanceof PickaxeItem digger){
            int levelDig = digger.getTier().getLevel();
            return levelDig >= 3;
        }
        return false;
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new InfinityBoxTile(blockPos, blockState);
    }

    //创建服务端tick
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? createTickerHelper(pBlockEntityType, EndlessTileTypes.INFINITY_CHEST_TILE.get(), InfinityBoxTile::lidAnimateTick) :
                createTickerHelper(pBlockEntityType, EndlessTileTypes.INFINITY_CHEST_TILE.get(), InfinityBoxTile::serverTick);
    }
}
