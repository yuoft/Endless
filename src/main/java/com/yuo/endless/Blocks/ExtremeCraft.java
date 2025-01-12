package com.yuo.endless.Blocks;

import com.yuo.endless.Tiles.ExtremeCraftTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class ExtremeCraft extends BaseEntityBlock {
    private static final TranslatableComponent CONTAINER_NAME = new TranslatableComponent("gui.endless.extreme_crafting_table");

    public ExtremeCraft() {
        super(Properties.of(Material.WOOD).strength(10, 50).requiresCorrectToolForDrops().sound(SoundType.GLASS));
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        return player.getMainHandItem().getItem() instanceof PickaxeItem pickaxeItem && pickaxeItem.getTier().getLevel() >= Tiers.DIAMOND.getLevel();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ExtremeCraftTile(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (level.getBlockEntity(pos) instanceof ExtremeCraftTile tile) {
                NetworkHooks.openGui((ServerPlayer) player, tile, pos);
                player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                return InteractionResult.CONSUME;
            }return InteractionResult.SUCCESS;
        }
    }

    //被破坏时 里面所有物品掉落
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ExtremeCraftTile) {
//                ((ExtremeCraftTile) blockEntity).dropItem(level, pos);
                Containers.dropContents(level, pos, (ExtremeCraftTile)blockEntity);
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

}
