package com.yuo.endless.Entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class SwordVoidEntity extends Entity {
    private static final AxisAlignedBB BOX_AABB = new AxisAlignedBB(4, 0, 4, 8, 20, 8);

    public SwordVoidEntity(EntityType<?> type, World world) {
        super(type, world);
//        this.noClip = true;
        this.isImmuneToFire();
        this.setInvulnerable(true);
    }

    @Override
    public void tick() {
        super.tick();
        if (ticksExisted >= 1200) this.setDead();
        if (world.isRemote) return;
        BlockPos pos = this.getPosition();
        if (!world.isAirBlock(pos.down())){
            world.createExplosion(null, this.getPosX(), this.getPosY() - 1, this.getPosZ(), 10, Mode.BREAK);
            AxisAlignedBB alignedBB = new AxisAlignedBB(pos.add(-4,-1,-4), pos.add(4,4,4));
            world.getEntitiesWithinAABB(LivingEntity.class, alignedBB).forEach(entity -> {
                if (entity.isAlive()) {
                    entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
                }
            });
            this.setDead();
        }

        this.setPositionAndUpdate(this.getPosX(), this.getPosY() - 0.25, this.getPosZ());
    }

    @Override
    public boolean canCollide(Entity entity) {
        return super.canCollide(entity);
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        super.applyEntityCollision(entity);

        if (entity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
        }
    }

    @Override
    public void onKillCommand() {

    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float v) {
        return false;
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compoundNBT) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compoundNBT) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;//不能通过此实体
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRenderDist(double distance) { //在范围内才渲染
        return true; //一直渲染
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return super.getBoundingBox();
    }
}
