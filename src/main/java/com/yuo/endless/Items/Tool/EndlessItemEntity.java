package com.yuo.endless.Items.Tool;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EndlessItemEntity extends ItemEntity {
    public EndlessItemEntity(World world, Entity entity, ItemStack stack){
        this(world, entity.getPosX(), entity.getPosY(), entity.getPosZ(), stack);
        this.setMotion(entity.getMotion()); // 设置方向
        this.setThrowerId(entity.getUniqueID()); // 丢弃者
    }

    public EndlessItemEntity(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z, stack);
        this.setDefaultPickupDelay();
        this.lifespan = 3600; //物品消失时间减少2分钟
    }

    //物品实体不受伤害：仙人掌，TNT，火等
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return source == DamageSource.OUT_OF_WORLD;
    }

    //免疫岩浆
    @Override
    public boolean isImmuneToFire() {
        return true;
    }

    //免疫爆炸
    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public void onKillCommand() {

    }
}
