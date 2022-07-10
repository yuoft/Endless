package com.yuo.endless.Entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class InfinityFireWorkEntity extends FireworkRocketEntity {
    private final float damage;

    public InfinityFireWorkEntity(EntityType<InfinityFireWorkEntity> type, World world) {
        super(EntityRegistry.INFINITY_FIREWORK.get(), world);
        this.damage = 10;
    }

    public InfinityFireWorkEntity(World world, ItemStack stack, LivingEntity living, float damageIn, double x, double y, double z, boolean flag) {
        super(world, stack, living, x, y, z, flag);
        this.damage = damageIn;
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)17);
            this.dealExplosionDamage();
            this.remove();
        }
    }

    @Override
    protected void func_230299_a_(BlockRayTraceResult result) {
        BlockPos blockpos = new BlockPos(result.getPos());
        this.world.getBlockState(blockpos).onEntityCollision(this.world, blockpos, this);
        if (!this.world.isRemote() && this.hasNbt()) {
            this.world.setEntityState(this, (byte)17);
            this.dealExplosionDamage();
            this.remove();
        }

        super.func_230299_a_(result);
    }

    //有无爆炸数据
    private boolean hasNbt() {
        ItemStack itemstack = this.dataManager.get(FIREWORK_ITEM);
        CompoundNBT compoundnbt = itemstack.isEmpty() ? null : itemstack.getChildTag("Fireworks");
        ListNBT listnbt = compoundnbt != null ? compoundnbt.getList("Explosions", 10) : null;
        return listnbt != null && !listnbt.isEmpty();
    }

    //爆炸后攻击范围内生物
    private void dealExplosionDamage() {
        float f = 0.0F;
        ItemStack itemstack = this.dataManager.get(FIREWORK_ITEM);
        CompoundNBT compoundnbt = itemstack.isEmpty() ? null : itemstack.getChildTag("Fireworks");
        ListNBT listnbt = compoundnbt != null ? compoundnbt.getList("Explosions", 10) : null;
        if (listnbt != null && !listnbt.isEmpty()) {
            f = 5.0F + (float)(listnbt.size() * 2);
        }

        if (f > 0.0F) {
            if (this.boostedEntity != null) {
                this.boostedEntity.attackEntityFrom(DamageSource.causeFireworkDamage(this, this.getShooter()), this.damage + (float)(listnbt.size() * 2));
            }

            double d0 = 5.0D; //对5格范围内生物造成伤害
            Vector3d vector3d = this.getPositionVec();

            for(LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(d0))) {
                if (livingentity != this.boostedEntity && !(this.getDistanceSq(livingentity) > 25.0D)) {
                    boolean flag = false;

                    for(int i = 0; i < 2; ++i) { //是否被方块阻挡 阻挡无伤害
                        Vector3d vector3d1 = new Vector3d(livingentity.getPosX(), livingentity.getPosYHeight(0.5D * (double)i), livingentity.getPosZ());
                        RayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
                        if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) { //伤害衰减
                        float f1 = f * (float)Math.sqrt((this.damage - (double)this.getDistance(livingentity)) / 2.5D);
                        livingentity.attackEntityFrom(DamageSource.causeFireworkDamage(this, this.getShooter()), f1);
                    }
                }
            }
        }

    }
}
