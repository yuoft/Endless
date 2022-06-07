package com.yuo.endless.Items.Tool;

import com.yuo.endless.Config.Config;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Entity.InfinityArrowEntity;
import com.yuo.endless.Entity.InfinityArrowSubEntity;
import com.yuo.endless.Items.ItemRegistry;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class InfinityBow extends BowItem {
    public InfinityBow() {
        super(new Properties().group(ModGroup.endless).maxStackSize(1));
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
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            int max = getUseDuration(stack); //总使用时间
            float velocity = BowItem.getArrowVelocity(max - timeLeft); //速度
            velocity = Math.max(velocity, 1.0f);
            ItemStack itemStack = findArrow(player);
            if (!worldIn.isRemote) {
                AbstractArrowEntity arrow;
                if (!itemStack.isEmpty()) {
                    if (itemStack.getItem() == ItemRegistry.infinityArrow.get()) { //无尽箭矢
                        arrow = new InfinityArrowEntity(EntityRegistry.INFINITY_ARROW.get(), player, worldIn);
                    } else {
                        arrow = new InfinityArrowSubEntity(EntityRegistry.INFINITY_ARROW_SUB.get(), player, worldIn); //普通箭矢
                    }
                } else { //无箭矢
                    arrow = new InfinityArrowSubEntity(EntityRegistry.INFINITY_ARROW_SUB.get(), player, worldIn);
                    arrow.setDamage(Config.SERVER.subArrowDamageBow.get());
                }
                arrow.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, velocity * 3.0F, 1.0F);
                arrow.setIsCritical(true);
                arrow.setShooter(player);

                arrow.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                worldIn.addEntity(arrow);
            }
            worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (worldIn.rand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
            if (!player.isCreative() && itemStack.getItem() == Items.ARROW){
                itemStack.shrink(1);
            }
            player.addStat(Stats.ITEM_USED.get(this));
        }
    }

    /**
     * 寻找弹药
     *
     * @param living 使用无尽弓的生物
     * @return 弹药
     */
    private ItemStack findArrow(LivingEntity living) {
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) living;
            PlayerInventory inventory = player.inventory;
            for (int i = 0; i < inventory.getSizeInventory(); i++) { //优先无尽箭矢
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack.getItem() == ItemRegistry.infinityArrow.get()) return stack;
            }
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack.getItem() == Items.ARROW) return stack;
            }
        }
        return ShootableItem.getHeldAmmo(living, ARROWS);
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
