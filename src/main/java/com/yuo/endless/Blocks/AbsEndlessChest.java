package com.yuo.endless.Blocks;

import com.yuo.endless.Tiles.AbsEndlessChestTile;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class AbsEndlessChest extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final ResourceLocation CONTENTS = new ResourceLocation("contents");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);
    private final Supplier<BlockEntityType<? extends AbsEndlessChestTile>> tileSupplier;
    private final EndlessChestType type;

    public AbsEndlessChest(Supplier<BlockEntityType<? extends AbsEndlessChestTile>> supplier, EndlessChestType chestType, Properties properties) {
        super(properties);
        this.type = chestType;
        this.tileSupplier = supplier;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.FALSE));
    }

    public static EndlessChestType getTypeFromBlock(Block block) {
        return block instanceof AbsEndlessChest ? ((AbsEndlessChest) block).getType() : EndlessChestType.NORMAL;
    }

    public EndlessChestType getType() {
        return type;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState blockState, LevelAccessor accessor, BlockPos currentPos, BlockPos pNeighborPos) {
        if (state.getValue(WATERLOGGED)) {
            accessor.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
        }
        return state;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());

        return this.defaultBlockState().setValue(FACING, direction).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof AbsEndlessChestTile chestTile) {
            if (!level.isClientSide && player.isCreative() && !chestTile.isEmpty()) {
                ItemStack itemstack = new ItemStack(this);
                CompoundTag tag = new CompoundTag();
                if (!tag.isEmpty()) {
                    itemstack.addTagElement("BlockEntityTag", tag);
                }

                if (chestTile.hasCustomName()) {
                    itemstack.setHoverName(chestTile.getCustomName());
                }

                ItemEntity itementity = new ItemEntity(level, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, itemstack);
                itementity.setDefaultPickUpDelay();
                level.addFreshEntity(itementity);
            } else {
                chestTile.unpackLootTable(player);
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }


    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof AbsEndlessChestTile chestTile) {
            builder = builder.withDynamicDrop(CONTENTS, (context, stackConsumer) -> {
                for(int i = 0; i < chestTile.getContainerSize(); ++i) {
                    stackConsumer.accept(chestTile.getItem(i));
                }

            });
        }

        return super.getDrops(state, builder);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState pState, @org.jetbrains.annotations.Nullable LivingEntity pPlacer, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof AbsEndlessChestTile) {
                ((AbsEndlessChestTile)blockEntity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof AbsEndlessChestTile) {
                Containers.dropContents(pLevel, pPos, (AbsEndlessChestTile)blockentity);
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable BlockGetter level, List<Component> components, TooltipFlag pFlag) {
        super.appendHoverText(stack, level, components, pFlag);
        CompoundTag compoundnbt = stack.getTagElement("BlockEntityTag");
        if (compoundnbt != null) {
            if (compoundnbt.contains("LootTable", 8)) {
                components.add(new TextComponent("???????"));
            }

            if (compoundnbt.contains("Items", 9)) {
                NonNullList<ItemStack> nonnulllist = NonNullList.withSize(type.getSize(), ItemStack.EMPTY);
                AbsEndlessChestTile.loadAllItems(compoundnbt, nonnulllist);
                int i = 0;
                int j = 0;

                for(ItemStack itemstack : nonnulllist) {
                    if (!itemstack.isEmpty()) {
                        ++j;
                        if (i <= 4) {
                            ++i;
                            MutableComponent iformattabletextcomponent = itemstack.getDisplayName().copy();
                            iformattabletextcomponent.append(" x").append(String.valueOf(itemstack.getCount()));
                            components.add(iformattabletextcomponent);
                        }
                    }
                }

                if (j - i > 0) {
                    components.add((new TranslatableComponent("container.shulkerBox.more", j - i)).withStyle(ChatFormatting.ITALIC));
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHit) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof AbsEndlessChestTile endlessChestTile) {
                NetworkHooks.openGui((ServerPlayer)player, endlessChestTile, pos);
                player.awardStat(Stats.OPEN_CHEST);
                PiglinAi.angerNearbyPiglins(player, true);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    private static final DoubleBlockCombiner.Combiner<AbsEndlessChestTile, Optional<Container>> INVENTORY_MERGER = new DoubleBlockCombiner.Combiner<AbsEndlessChestTile, Optional<Container>>() {
        public Optional<Container> acceptDouble(AbsEndlessChestTile tile, AbsEndlessChestTile chestTile) {
            return Optional.of(tile);
        }

        public Optional<Container> acceptSingle(AbsEndlessChestTile tile) {
            return Optional.of(tile);
        }

        public Optional<Container> acceptNone() {
            return Optional.empty();
        }
    };

    @Nullable
    public static Container getContainer(AbsEndlessChest chest, BlockState state, Level level, BlockPos pos, boolean override) {
        return chest.combine(state, level, pos, override).apply(INVENTORY_MERGER).orElse(null);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof AbsEndlessChestTile) {
            ((AbsEndlessChestTile)blockEntity).recheckOpen();
        }

    }

    public DoubleBlockCombiner.NeighborCombineResult<? extends AbsEndlessChestTile> combine(BlockState state, Level world, BlockPos pos, boolean override) {
        BiPredicate<LevelAccessor, BlockPos> bipredicate;
        if (override) {
            bipredicate = (worldIn, posIn) -> false;
        } else {
            bipredicate = AbsEndlessChest::isBlocked;
        }

        return DoubleBlockCombiner.combineWithNeigbour(this.tileSupplier.get(), AbsEndlessChest::getMergerType, AbsEndlessChest::getDirectionToAttached, FACING, state, world, pos, bipredicate);
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
        super.triggerEvent(state, level, pos, id, param);
        BlockEntity tile = level.getBlockEntity(pos);
        return tile != null && tile.triggerEvent(id, param);
    }

    private static boolean isBlocked(LevelAccessor iWorld, BlockPos blockPos) {
        return isBelowSolidBlock(iWorld, blockPos) || isCatSittingOn(iWorld, blockPos);
    }

    private static boolean isBelowSolidBlock(BlockGetter iBlockReader, BlockPos worldIn) {
        BlockPos blockpos = worldIn.above();
        return iBlockReader.getBlockState(blockpos).isRedstoneConductor(iBlockReader, blockpos);
    }

    private static boolean isCatSittingOn(LevelAccessor iWorld, BlockPos blockPos) {
        List<Cat> list = iWorld.getEntitiesOfClass(Cat.class, new AABB(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 2, blockPos.getZ() + 1));
        if (!list.isEmpty()) {
            for (Cat catentity : list) {
                if (catentity.isInSittingPose()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
        return AbstractContainerMenu.getRedstoneSignalFromContainer(getContainer(this, pBlockState, pLevel, pPos, false));
    }

    @Override
    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    public static DoubleBlockCombiner.BlockType getMergerType(BlockState blockState) {
        return DoubleBlockCombiner.BlockType.SINGLE;
    }

    public static Direction getDirectionToAttached(BlockState state) {
        Direction direction = state.getValue(FACING);
        return direction.getCounterClockWise();
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }

}
