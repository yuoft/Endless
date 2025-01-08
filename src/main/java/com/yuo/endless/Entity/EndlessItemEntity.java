package com.yuo.endless.Entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EndlessItemEntity extends ItemEntity {
    public EndlessItemEntity(Level world, Entity entity, ItemStack stack){
        this(world, entity.getX(), entity.getY(), entity.getZ(), stack);
        this.setDeltaMovement(entity.getDeltaMovement()); // 设置方向
        this.setOwner(entity.getUUID()); // 丢弃者
    }

    public EndlessItemEntity(Level worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z, stack);
//        this.setPickUpDelay(1);
        this.lifespan = 3600; //物品消失时间减少2分钟
    }

    //物品实体不受伤害：仙人掌，TNT，火等
    @Override
    public boolean hurt(DamageSource source, float pAmount) {
        return source == DamageSource.OUT_OF_WORLD;
    }

    //免疫岩浆

    @Override
    public boolean fireImmune() {
        return true;
    }

    //免疫爆炸
    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    public void kill() {
    }

    @Override
    public void killed(ServerLevel pLevel, LivingEntity pKilledEntity) {
    }

    @Override
    public void awardKillScore(Entity pKilled, int pScoreValue, DamageSource pSource) {
    }
}
