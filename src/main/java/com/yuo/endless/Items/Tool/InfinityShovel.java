package com.yuo.endless.Items.Tool;

import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.EndlessItemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;

import java.util.function.Consumer;

public class InfinityShovel extends ShovelItem {
    private final ToolHelper handler;

    public InfinityShovel() {
        super(EndlessItemTiers.INFINITY_TOOL, -2, -2.8f, new Properties().tab(EndlessTab.endless).fireResistant());
        this.handler = new ToolHelper();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (stack.getOrCreateTag().getBoolean("destroyer")) {
            return 5.0F;
        }
        if (stack.isCorrectToolForDrops(state)){
            return this.getTier().getSpeed();
        }
        return Math.max(super.getDestroySpeed(stack, state), 6.0f);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching()) { //潜行右键切换形态
            CompoundTag tags = stack.getOrCreateTag();
            tags.putBoolean("destroyer", !tags.getBoolean("destroyer"));
            player.swing(hand); //摆臂
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        if (itemstack.getOrCreateTag().getBoolean("destroyer")){
            Level world = player.level;
            if (!world.isClientSide){
                BlockState state = world.getBlockState(pos);
                if (itemstack.isCorrectToolForDrops(state) && ToolHelper.MATERIAL_SHOVEL.contains(state.getMaterial())){
                    handler.onBlockStartBreak(itemstack, world, pos, player, 7);
                }
            }
        }
        return false; //范围挖掘成功与否与挖掘此方块无关
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getItemInHand().hasTag() && context.getItemInHand().getOrCreateTag().getBoolean("destroyer"))
            return InteractionResult.PASS; //正常形态才可以使用
        Level world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (context.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
        } else {
            Player player = context.getPlayer();
            BlockState modifiedState = blockstate.getToolModifiedState(context, ToolActions.SHOVEL_FLATTEN, false);
            BlockState blockstate2 = null;
            if (modifiedState != null && world.getBlockState(blockpos.above()).isAir()) {
                world.playSound(player, blockpos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
                blockstate2 = modifiedState;
            } else if (blockstate.getBlock() instanceof CampfireBlock && blockstate.getValue(CampfireBlock.LIT)) {
                if (!world.isClientSide()) {
                    world.levelEvent(null, 1009, blockpos, 0);
                }

                CampfireBlock.dowse(player, world, blockpos, blockstate);
                blockstate2 = blockstate.setValue(CampfireBlock.LIT, Boolean.FALSE);
            }

            if (blockstate2 != null) {
                if (!world.isClientSide) {
                    int rang = 5;
                    int height = 3;
                    BlockPos minPos = blockpos.offset(-rang, -height, -rang);
                    BlockPos maxPos = blockpos.offset(rang, height, rang);
                    if (player != null && player.isCrouching()){
                        for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
                            BlockState state = world.getBlockState(pos);
                            if (state.getBlock() instanceof GrassBlock && world.getBlockState(pos.above()).isAir()){ //当前方块时草方块，并且上方是空气
                                world.setBlock(pos, blockstate2, 11);
                            }
                        }

                    }else world.setBlock(blockpos, blockstate2, 11); //未潜行耕种一个方块
                }

                return InteractionResult.sidedSuccess(world.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 0;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        stack.getOrCreateTag().putInt("Damage", 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int pSlotId, boolean pIsSelected) {
        int damage = stack.getDamageValue();
        if (damage > 0){
            stack.getOrCreateTag().putInt("Damage", 0);
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(tab)){
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putBoolean("Unbreakable",true);
            stacks.add(stack);
        }
    }

    //创建一个能被快速捡起的物品实体
    @org.jetbrains.annotations.Nullable
    @Override
    public Entity createEntity(Level level, Entity location, ItemStack stack) {
        return new EndlessItemEntity(level, location, stack);
    }

    //允许创建自定义物品实体
    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }
}
