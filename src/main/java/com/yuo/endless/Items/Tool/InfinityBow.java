package com.yuo.endless.Items.Tool;

import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Entity.InfinityArrowEntity;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class InfinityBow extends BowItem {
    public InfinityBow() {
        super(new Properties().group(ModGroup.myGroup).maxStackSize(1));
    }

    //使用时间
    @Override
    public int getUseDuration(ItemStack stack) {
        return 1200;
    }

    //使用时动作
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, true);
        if (ret != null) return ret;

        playerIn.setActiveHand(handIn);
        return ActionResult.resultConsume(itemstack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        fire(stack, worldIn, entityLiving, timeLeft);
    }

    //开火
    public void fire(ItemStack stack, World world, LivingEntity player, int useCount) {
        int max = getUseDuration(stack); //总使用时间
        float velocity = BowItem.getArrowVelocity(max - useCount); //速度
        velocity = velocity < 1.0D ? 1.0f : velocity;
        InfinityArrowEntity arrow = new InfinityArrowEntity(EntityRegistry.INFINITY_ARROW.get(), player, world);
        arrow.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, velocity *  3.0F, 1.0F);
        arrow.setIsCritical(true);

        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (world.rand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
        arrow.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;

        if (!world.isRemote) {
            world.addEntity(arrow);
            if(player instanceof PlayerEntity) ((PlayerEntity) player).addStat(Stats.ITEM_USED.get(this));
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
