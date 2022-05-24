package com.yuo.endless.Items.Tool;

import com.yuo.endless.tab.ModGroup;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.GrassBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class InfinityShovel extends ShovelItem {
    private final ItemHander hander;

    public InfinityShovel() {
        super(MyItemTier.INFINITY_TOOL, -2, -2.8f, new Properties().group(ModGroup.endless).isImmuneToFire());
        this.hander = new ItemHander();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (stack.getOrCreateTag().getBoolean("destroyer")) {
            return 5.0F;
        }
        if (state.getHarvestTool() == ToolType.SHOVEL){
            return 999.0f;
        }
        return Math.max(super.getDestroySpeed(stack, state), 6.0f);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking()) { //潜行右键切换形态
            CompoundNBT tags = stack.getOrCreateTag();
            tags.putBoolean("destroyer", !tags.getBoolean("destroyer"));
            player.swingArm(hand); //摆臂
            return ActionResult.resultSuccess(stack);
        }
        return ActionResult.resultPass(stack);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        if (itemstack.getOrCreateTag().getBoolean("destroyer")){
            World world = player.world;
            if (!world.isRemote){
                BlockState state = world.getBlockState(pos);
                if (state.getHarvestLevel() <= itemstack.getHarvestLevel(ToolType.SHOVEL, player, state)
                        && ItemHander.MATERIAL_SHOVEL.contains(state.getMaterial())){
                    hander.onBlockStartBreak(itemstack, world, pos, player, 7, ToolType.SHOVEL);
                }
            }
        }
        return false; //范围挖掘成功与否与挖掘此方块无关
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getItem().hasTag() && context.getItem().getOrCreateTag().getBoolean("destroyer"))
            return ActionResultType.PASS; //正常形态才可以使用
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (context.getFace() == Direction.DOWN) {
            return ActionResultType.PASS;
        } else {
            PlayerEntity playerentity = context.getPlayer();
            BlockState blockstate1 = blockstate.getToolModifiedState(world, blockpos, playerentity, context.getItem(), net.minecraftforge.common.ToolType.SHOVEL);
            BlockState blockstate2 = null;
            if (blockstate1 != null && world.isAirBlock(blockpos.up())) {
                world.playSound(playerentity, blockpos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                blockstate2 = blockstate1;
            } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.get(CampfireBlock.LIT)) {
                if (!world.isRemote()) {
                    world.playEvent(null, 1009, blockpos, 0);
                }

                CampfireBlock.extinguish(world, blockpos, blockstate);
                blockstate2 = blockstate.with(CampfireBlock.LIT, Boolean.FALSE);
            }

            if (blockstate2 != null) {
                if (!world.isRemote) {
                    int rang = 5;
                    int height = 3;
                    BlockPos minPos = blockpos.add(-rang, -height, -rang);
                    BlockPos maxPos = blockpos.add(rang, height, rang);
                    if (playerentity.isSneaking()){
                        for (BlockPos pos : BlockPos.getAllInBoxMutable(minPos, maxPos)) {
                            BlockState state = world.getBlockState(pos);
                            if (state.getBlock() instanceof GrassBlock && world.isAirBlock(pos.up())){ //当前方块时草方块，并且上方是空气
                                world.setBlockState(pos, blockstate2, 11);
                            }
                        }

                    }else world.setBlockState(blockpos, blockstate2, 11); //未潜行耕种一个方块
                }

                return ActionResultType.func_233537_a_(world.isRemote);
            } else {
                return ActionResultType.PASS;
            }
        }
    }


    @Override
    public int getItemEnchantability() {
        return 0;
    }
    @Override
    public boolean isDamageable() {
        return false;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EndlessItemEntity(world, location, itemstack);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }
}
