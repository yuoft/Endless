package com.yuo.endless.integration.BOT;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import vazkii.botania.common.block.decor.BlockTinyPotato;

import javax.annotation.Nullable;
import java.util.Random;

public class InfinityPotato extends BlockTinyPotato {
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 12.0D, 12.0D);

    public InfinityPotato() {
        super(AbstractBlock.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(0).hardnessAndResistance(1.0f));
//        this.setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
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

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new InfinityPotatoTile();
    }

}
