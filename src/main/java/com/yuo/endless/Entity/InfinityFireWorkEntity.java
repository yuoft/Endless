package com.yuo.endless.Entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;

public class InfinityFireWorkEntity extends FireworkRocketEntity {
    private final float damage;

    public InfinityFireWorkEntity(EntityType<InfinityFireWorkEntity> type, Level world) {
        super(EntityRegistry.INFINITY_FIREWORK.get(), world);
        this.damage = 10;
    }

    public InfinityFireWorkEntity(Level world, ItemStack stack, LivingEntity living, float damageIn, double x, double y, double z, boolean flag) {
        super(world, stack, living, x, y, z, flag);
        this.damage = damageIn;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)17);
            this.dealExplosionDamage();
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        BlockPos blockpos = new BlockPos(result.getBlockPos());
        this.level.getBlockState(blockpos).entityInside(this.level, blockpos, this);
        if (!this.level.isClientSide() && this.hasNbt()) {
            this.level.broadcastEntityEvent(this, (byte)17);
            this.dealExplosionDamage();
            this.discard();
        }

        super.onHitBlock(result);
    }

    //有无爆炸数据
    private boolean hasNbt() {
        ItemStack itemstack = this.entityData.get(DATA_ID_FIREWORKS_ITEM);
        CompoundTag tag = itemstack.isEmpty() ? null : itemstack.getTagElement("Fireworks");
        ListTag tags = tag != null ? tag.getList("Explosions", 10) : null;
        return tags != null && !tags.isEmpty();
    }

    //爆炸后攻击范围内生物
    private void dealExplosionDamage() {
        float f = 0.0F;
        ItemStack itemstack = this.entityData.get(DATA_ID_FIREWORKS_ITEM);
        CompoundTag compoundnbt = itemstack.isEmpty() ? null : itemstack.getTagElement("Fireworks");
        ListTag listnbt = compoundnbt != null ? compoundnbt.getList("Explosions", 10) : null;
        if (listnbt != null && !listnbt.isEmpty()) {
            f = 5.0F + (float)(listnbt.size() * 2);
        }

        if (f > 0.0F) {
            if (this.attachedToEntity != null) {
                this.attachedToEntity.hurt(DamageSource.fireworks(this, this.getOwner()), this.damage + (float)(listnbt.size() * 2));
            }

            double d0 = 5.0D; //对5格范围内生物造成伤害
            Vec3 vec3 = this.position();

            for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().deflate(d0))) {
                if (livingentity != this.attachedToEntity && !(this.distanceToSqr(livingentity) > 25.0D)) {
                    boolean flag = false;

                    for(int i = 0; i < 2; ++i) { //是否被方块阻挡 阻挡无伤害
                        Vec3 vec31 = new Vec3(livingentity.getX(), livingentity.getY(0.5 * (double)i), livingentity.getZ());
                        HitResult hitresult = this.level.clip(new ClipContext(vec3, vec31, Block.COLLIDER, Fluid.NONE, this));
                        if (hitresult.getType() == Type.MISS) {
                            flag = true;
                            break;
                        }
                    }

                    if (flag) { //伤害衰减
                        float f1 = f * (float)Math.sqrt((this.damage - (double)this.distanceTo(livingentity)) / 2.5D);
                        livingentity.hurt(DamageSource.fireworks(this, this.getOwner()), f1);
                    }
                }
            }
        }

    }
}
