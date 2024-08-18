package com.yuo.endless.integration.BOT;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import vazkii.botania.common.block.tile.TileSimpleInventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.state.properties.BlockStateProperties.WATERLOGGED;

public class InfinityPotato extends Block implements IWaterLoggable, ITileEntityProvider {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 12.0D, 12.0D);

    public InfinityPotato() {
        super(AbstractBlock.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.0f));
//        this.setDefaultState(getDefaultState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH).with(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public void onReplaced(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileSimpleInventory) {
                InventoryHelper.dropInventoryItems(world, pos, ((TileSimpleInventory) te).getItemHandler());
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, HORIZONTAL_FACING);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : Fluids.EMPTY.getDefaultState();
    }

    @Nonnull
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidState = context.getWorld().getFluidState(context.getPos());
        return this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER).with(BlockStateProperties.HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction side, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }

        return stateIn;
    }

    @Override
    public boolean eventReceived(BlockState state, World world, BlockPos pos, int id, int param) {
        super.eventReceived(state, world, pos, id, param);
        TileEntity tileentity = world.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof InfinityPotatoTile){
            InfinityPotatoTile tile = (InfinityPotatoTile) tileEntity;
            ItemStack heldItem = player.getHeldItem(hand);
            tile.interact(player, hand, heldItem, rayTraceResult.getFace());

            //粒子和音效
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();
            Random rand = world.rand;
            for (int i = 0; i < 10; i++)
                world.addParticle(ParticleTypes.HEART, x + rand.nextDouble(), y + rand.nextDouble(), z + rand.nextDouble(), 0.0D, 0.1D + rand.nextDouble(), 0.0D);
            if (heldItem.isEmpty() && player.isSneaking()) { //空手潜行右键
                world.playSound(player, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                for (int i = 0; i < 5; i++)
                    world.addParticle(ParticleTypes.EXPLOSION, x + rand.nextDouble(), y + rand.nextDouble(), z + rand.nextDouble(), 0.1D, 0.1D + rand.nextDouble(), 0.1D);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity living, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof InfinityPotatoTile){
                ((InfinityPotatoTile) tileEntity).name = stack.getDisplayName();
            }
        }
    }

    @Nonnull
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new InfinityPotatoTile();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader iBlockReader) {
        return new InfinityPotatoTile();
    }
}
