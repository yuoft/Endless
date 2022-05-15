package com.yuo.endless.Items.Tool;

import com.yuo.endless.tab.ModGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class InfinityAxe extends AxeItem {
    private ItemHander hander;

    public InfinityAxe() {
        super(MyItemTier.INFINITY_TOOL, 10, -3.0f, new Properties().group(ModGroup.myGroup).isImmuneToFire());
        this.hander = new ItemHander();
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
        if (player.isSneaking() && !player.world.isRemote){
            hander.aoeBlocks(player.world, pos, player, 64, itemstack, false, true);
        }
        return false;
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
