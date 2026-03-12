package com.yuo.endless.Compat.Maid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.ImmutableMap;
import com.yuo.endless.Config;
import com.yuo.endless.Items.Tool.InfinityDamageTypes;
import com.yuo.endless.Items.Tool.InfinitySword;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;

public class MaidUseSwordTask extends Behavior<EntityMaid> {
    private static final int CHECK_RANGE = 8;

    public MaidUseSwordTask() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, EntityMaid maid) {
        if (!canUseItem(maid)) {
            return false;
        } else {
            LivingEntity target = maid.getTarget();
            if (maid.tickCount % 200 == 0 && maid.level().random.nextFloat() < 0.75f) return false;

            return target != null && target.isAlive() && target.distanceTo(maid) > 4.0F;
        }
    }


    @Override
    protected void start(ServerLevel serverLevel, EntityMaid maid, long gameTime) {
        ItemStack handItem = maid.getMainHandItem();
        if (handItem.getItem() instanceof InfinitySword){
            attackAOE(maid, Config.SERVER.swordAttackRange.get(), Config.SERVER.swordRangeDamage.get(), Config.SERVER.isSwordAttackAnimal.get());
            if (maid.level().isClientSide) {
            }
            maid.sendSystemMessage(Component.translatable("task.endless.infinity_sword_task.desc.use"));
            maid.playSound(SoundEvents.PLAYER_LEVELUP, 1.0f, 5.0f);
            maid.getCooldowns().addCooldown(handItem.getItem(), 20);
        }
    }

    public boolean canUseItem(EntityMaid maid) {
        ItemStack handItem = maid.getMainHandItem();
        return !maid.getCooldowns().isOnCooldown(handItem.getItem());
    }

    /**
     * 女仆使用无尽剑aoe伤害
     */
    public static void attackAOE(EntityMaid maid, float range, float damage, boolean type) {
        if (maid.level().isClientSide) return;
        AABB aabb = maid.getBoundingBox().deflate(range);//范围
        List<Entity> toAttack = maid.level().getEntities(maid, aabb);//生物列表
        DamageSource src = InfinityDamageTypes.infinity(maid);//伤害类型
        LivingEntity living = maid.getOwner();
        if (living instanceof Player owner) { //女仆有主人

            for (Entity entity : toAttack) {
                if (entity instanceof LivingEntity){
                    if (entity instanceof Player player){ //不攻击主人
                        UUID uuid = player.getUUID();
                        if (owner.getUUID().equals(uuid)){
                            continue;
                        }
                    }
                    if (entity instanceof TamableAnimal animal){ //宠物主人和女仆主人一致
                        UUID ownerUUID = animal.getOwnerUUID();
                        if (owner.getUUID().equals(ownerUUID)){
                            continue;
                        }
                    }
                    if(type) { //潜行攻击所有生物
                        InfinitySword.attackEntity(entity, src, damage);
                    } else {
                        if (entity instanceof Mob) {
                            InfinitySword.attackEntity(entity, src, damage);
                        }
                    }
                }
            }
        }

    }
}
