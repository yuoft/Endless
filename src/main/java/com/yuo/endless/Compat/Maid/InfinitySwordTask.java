package com.yuo.endless.Compat.Maid;

import com.github.tartaricacid.touhoulittlemaid.api.task.IAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.*;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Tool.ColorText;
import com.yuo.endless.Items.Tool.InfinitySword;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

//剑
public class InfinitySwordTask implements IAttackTask {

    //获取任务的 ID
    @Override
    public @NotNull ResourceLocation getUid() {
        return EndlessUtils.fa("infinity_sword_task");
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
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(this::hasAssaultWeapon, IAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create((target) -> !this.hasAssaultWeapon(maid) || this.farAway(target, maid));
        BehaviorControl<Mob> moveToTargetTask = SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(0.6F);
        BehaviorControl<EntityMaid> attackTargetTask = MaidMeleeAttack.create(20);
        MaidUseSwordTask maidUseSwordTask = new MaidUseSwordTask();
        return Lists.newArrayList(Pair.of(5, supplementedTask), Pair.of(5, findTargetTask), Pair.of(5, moveToTargetTask), Pair.of(5, maidUseSwordTask), Pair.of(5, attackTargetTask));
    }

    //工作模式是否可用
    @Override
    public boolean isEnable(EntityMaid maid) {
        Item hand = maid.getMainHandItem().getItem();
        return hand instanceof InfinitySword;
    }

    // 获取额外的条件提示文本。
    @Override
    public @NotNull List<Pair<String, Predicate<EntityMaid>>> getConditionDescription(@NotNull EntityMaid maid) {
        return Lists.newArrayList(Pair.of("has_infinity_sword", this::hasAssaultWeapon));
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

    private boolean hasAssaultWeapon(EntityMaid maid) {
        return maid.getMainHandItem().getItem() instanceof InfinitySword;
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        if (!target.isAlive()) {
            return true;
        } else {
            boolean enable = maid.isHomeModeEnable();
            float radius = maid.getRestrictRadius();
            if (!enable && maid.getOwner() != null) {
                return maid.getOwner().distanceTo(target) > radius;
            } else {
                return maid.distanceTo(target) > radius;
            }
        }
    }
}
