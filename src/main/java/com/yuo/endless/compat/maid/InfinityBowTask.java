package com.yuo.endless.compat.maid;

import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.*;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.entity.EntityRegistry;
import com.yuo.endless.entity.InfinityArrowEntity;
import com.yuo.endless.entity.InfinityArrowSubEntity;
import com.yuo.endless.items.EndlessItems;
import com.yuo.endless.items.tool.ColorText;
import com.yuo.endless.items.tool.InfinityArrow;
import com.yuo.endless.items.tool.InfinityBow;
import com.yuo.endless.items.tool.InfinityCrossBow;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

//弓 弩
public class InfinityBowTask implements IRangedAttackTask {

    //获取任务的 ID
    @Override
    public @NotNull ResourceLocation getUid() {
        return EndlessUtils.fa("infinity_bow_task");
    }

    // 获取任务的图标
    @Override
    public @NotNull ItemStack getIcon() {
        return EndlessItems.infinityBow.get().getDefaultInstance();
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
        ItemStack handItem = maid.getMainHandItem();
        Item item = handItem.getItem();
        boolean flag = item instanceof InfinityCrossBow;

        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create((e) -> this.hasBow(e) && this.hasArrow(e), IRangedAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create((target) -> !this.hasBow(maid) || !this.hasArrow(maid) || this.farAway(target, maid));
        BehaviorControl<EntityMaid> moveToTargetTask = MaidRangedWalkToTarget.create(0.6F);

        BehaviorControl<EntityMaid> maidAttackStrafingTask = new MaidAttackStrafingTask();
        BehaviorControl<EntityMaid> attackTargetTask;
        if (!flag) {
            attackTargetTask = new MaidShootTargetTask();
        } else {
            attackTargetTask = new MaidCrossbowAttack();
        }
        return Lists.newArrayList(Pair.of(5, supplementedTask), Pair.of(5, findTargetTask), Pair.of(5, moveToTargetTask), Pair.of(5, maidAttackStrafingTask), Pair.of(5, attackTargetTask));
    }

    @Override
    public void performRangedAttack(EntityMaid shooter, @NotNull LivingEntity target, float distanceFactor) {
        Level level = shooter.level();
        int arrowNum = this.findArrow(shooter);
        AbstractArrow arrow;
        if (arrowNum == 1) {
            arrow = new InfinityArrowEntity(EntityRegistry.INFINITY_ARROW.get(), shooter, level, true);
        }else arrow = new InfinityArrowSubEntity(EntityRegistry.INFINITY_ARROW_SUB.get(), shooter, level, shooter.getOffhandItem());
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
        }else if (mainHandItem.getItem() instanceof InfinityCrossBow) {
            shooter.performCrossbowAttack(shooter, 1.6F);
        }

    }

    //工作模式是否可用
    @Override
    public boolean isEnable(EntityMaid maid) {
        Item hand = maid.getMainHandItem().getItem();
        return hand instanceof InfinityBow || hand instanceof InfinityCrossBow;
    }

    // 获取额外的条件提示文本。
    @Override
    public @NotNull List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(@NotNull EntityMaid maid) {
        return Lists.newArrayList(Pair.of("has_infinity_bow", this::hasBow), Pair.of("has_infinity_arrow", this::hasArrow));
    }

    @Override
    public @NotNull MutableComponent getName() {
        String key = String.format("task.%s.%s", this.getUid().getNamespace(), this.getUid().getPath());
        return Component.translatable(ColorText.makeFabulous(I18n.get(key)));
    }

    @Override
    public @NotNull List<String> getDescription(@NotNull EntityMaid maid) {
        String key = String.format("task.%s.%s.desc", this.getUid().getNamespace(), this.getUid().getPath());
        return Lists.newArrayList(ColorText.makeSANIC(I18n.get(key)));
    }

    private boolean hasBow(EntityMaid maid) {
        Item item = maid.getMainHandItem().getItem();
        return item instanceof InfinityBow || item instanceof InfinityCrossBow;
    }

    private boolean hasArrow(EntityMaid maid) {
        return this.findArrow(maid) >= 0;
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > this.searchRadius(maid);
    }

    private int findArrow(EntityMaid maid) {
        ItemStack mainHandItem = maid.getMainHandItem();
        Item item = mainHandItem.getItem();
        if (item instanceof InfinityBow || item instanceof InfinityCrossBow) {
            ItemStack offhandItem = maid.getOffhandItem();
            if (offhandItem.getItem() instanceof InfinityArrow) {
                return 1;
            }else {
                return 2;
            }
        }
        return -1;
    }
}
