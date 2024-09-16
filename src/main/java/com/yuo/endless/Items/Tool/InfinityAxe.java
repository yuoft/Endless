package com.yuo.endless.Items.Tool;

import com.yuo.endless.Config;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.EndlessItemEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class InfinityAxe extends AxeItem {
    private final ToolHelper hander;

    public InfinityAxe() {
        super(MyItemTier.INFINITY_TOOL, 10, -3.0f, new Properties().group(EndlessTab.endless).isImmuneToFire());
        this.hander = new ToolHelper();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking() && !world.isRemote) { //潜行右键切换形态
            player.swingArm(Hand.MAIN_HAND);
            hander.onBlockStartBreak(new ItemStack(this), world, player.getPosition(), player, 7, ToolType.AXE);
            return ActionResult.resultSuccess(stack);
        }
        return ActionResult.resultPass(stack);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        World world = player.world;
        if (player.isSneaking() && !world.isRemote){
            BlockState state = world.getBlockState(pos);
            //垂直方向的树木
            if (state.isIn(BlockTags.LOGS) && state.get(RotatedPillarBlock.AXIS) == Direction.Axis.Y){
                hander.aoeBlocks(world, pos, player, Config.SERVER.axeChainDistance.get(), itemstack);
                return true;
            }
        }
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.getHarvestTool() == ToolType.AXE){
            return this.getTier().getEfficiency();
        }
        return Math.max(super.getDestroySpeed(stack, state), 6.0f);
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
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)){
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putBoolean("Unbreakable",true);
            items.add(stack);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        int damage = stack.getDamage();
        if (damage > 0){
            stack.getOrCreateTag().putInt("Damage", 0);
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
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
