package com.yuo.endless.Compat.Maid;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidAttackStrafingTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidRangedWalkToTarget;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidShootTargetTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Entity.InfinityArrowEntity;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Tool.InfinityArrow;
import com.yuo.endless.Items.Tool.InfinityBow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class InfinityTask implements IRangedAttackTask {

    //获取任务的 ID
    @Override
    public @NotNull ResourceLocation getUid() {
        return EndlessUtils.fa("infinity_task");
    }

    // 获取任务的图标
    @Override
    public @NotNull ItemStack getIcon() {
        return EndlessItems.infinitySword.get().getDefaultInstance();
    }

    // 获取女仆在该任务时的音效，可以为 null
    @Nullable
    @Override
    public SoundEvent getAmbientSound(@NotNull EntityMaid entityMaid) {
        return null;
    }

    // 创建女仆 AI，通过 Minecraft 原版的 BehaviorControl 实现
    @Override
    public @NotNull List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(@NotNull EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create((e) -> {
            return this.hasBow(e) && this.hasArrow(e);
        }, IRangedAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create((target) -> !this.hasBow(maid) || !this.hasArrow(maid) || this.farAway(target, maid));
        BehaviorControl<EntityMaid> moveToTargetTask = MaidRangedWalkToTarget.create(0.6F);
        BehaviorControl<EntityMaid> maidAttackStrafingTask = new MaidAttackStrafingTask();
        BehaviorControl<EntityMaid> shootTargetTask = new MaidShootTargetTask();
        return Lists.newArrayList(Pair.of(5, supplementedTask), Pair.of(5, findTargetTask), Pair.of(5, moveToTargetTask), Pair.of(5, maidAttackStrafingTask), Pair.of(5, shootTargetTask));
    }

    @Override
    public void performRangedAttack(EntityMaid shooter, @NotNull LivingEntity target, float distanceFactor) {
        Level level = shooter.level();
        AbstractArrow arrow = new InfinityArrowEntity(EntityRegistry.INFINITY_ARROW.get(), shooter, level, true);
        arrow.setPierceLevel((byte) 3);
        arrow.setCritArrow(true); //暴击粒子
        arrow.setOwner(shooter);

        arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        level.addFreshEntity(arrow);
        ItemStack mainHandItem = shooter.getMainHandItem();
        if (mainHandItem.getItem() instanceof InfinityBow) {
            double x = target.getX() - shooter.getX();
            double y = target.getEyeY() - shooter.getEyeY();
            double z = target.getZ() - shooter.getZ();
            float distance = shooter.distanceTo(target);
            float velocity = Mth.clamp(distance / 10.0F, 1.6F, 3.2F);
            float inaccuracy = 1.0F - Mth.clamp(distance / 100.0F, 0.0F, 0.9F);
            arrow.setNoGravity(true);
            arrow.shoot(x, y, z, velocity, inaccuracy);
            shooter.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));
            level.addFreshEntity(arrow);
        }

    }

    //工作模式是否可用
    @Override
    public boolean isEnable(EntityMaid maid) {
        ItemStack hand = maid.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offhandItem = maid.getOffhandItem();
        if (!EventHandler.isInfinityItem(hand.getItem())) return false;

        if (hand.getItem() != EndlessItems.infinityBow.get()) return false;
        if (offhandItem.getItem() != EndlessItems.infinityArrow.get()) return false;

        return true;
    }

    // 获取额外的条件提示文本。
    @Override
    public @NotNull List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(@NotNull EntityMaid maid) {
        return Lists.newArrayList(Pair.of("has_infinity_bow", this::hasBow), Pair.of("has_infinity_arrow", this::hasArrow));
    }

    private boolean hasBow(EntityMaid maid) {
        return maid.getMainHandItem().getItem() instanceof InfinityBow;
    }

    private boolean hasArrow(EntityMaid maid) {
        return this.findArrow(maid) >= 0;
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > this.searchRadius(maid);
    }

    private int findArrow(EntityMaid maid) {
        ItemStack mainHandItem = maid.getMainHandItem();
        if (mainHandItem.getItem() instanceof InfinityBow) {
            ItemStack offhandItem = maid.getOffhandItem();
            if (offhandItem.getItem() instanceof InfinityArrow) {
                return 1;
            }
        }
        return -1;
    }
}
