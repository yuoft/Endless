package com.yuo.endless.Entity;

import com.google.common.collect.Lists;
import com.mojang.math.Vector3d;
import com.yuo.endless.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.Tool.InfinityDamageSource;
import com.yuo.endless.Items.Tool.InfinitySword;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import java.util.Collections;
import java.util.Random;

//箭实体
public class InfinityArrowEntity extends AbstractArrow {
    private LivingEntity shooter; //使用玩家
    private boolean isSub; //是否召唤无尽光箭
    public InfinityArrowEntity(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
        this.setBaseDamage(10000f);
    }

    public InfinityArrowEntity(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level worldIn) {
        super(type, x, y, z, worldIn);
        this.setBaseDamage(10000f);
    }

    public InfinityArrowEntity(EntityType<? extends AbstractArrow> type, LivingEntity shooter, Level worldIn, boolean isSub) {
        super(type, shooter, worldIn);
        this.setBaseDamage(10000f);
        this.shooter = shooter;
        this.isSub = isSub;
    }

    //捡起来的物品
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putDouble("damage", Float.POSITIVE_INFINITY);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setDamage(compound.getDouble("damage"));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    //减小水的阻力
    @Override
    protected float getWaterDrag() {
        return 0.99f;
    }

    @Override
    public void tick() {
        super.tick();
        if (ticksExisted > 200) setDead(); //10秒后死亡
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        Entity entity = result.getEntity(); //被击中的实体
        float f = (float)this.getMotion().length();
        int i = MathHelper.ceil(MathHelper.clamp((double)f * this.damage, 0.0D, 2.147483647E9D));
        if (this.getPierceLevel() > 0) {  //穿透等级
            if (this.piercedEntities == null) {
                this.piercedEntities = new IntOpenHashSet(5);
            }

            if (this.hitEntities == null) {
                this.hitEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercedEntities.size() >= this.getPierceLevel() + 1) {
                this.remove();
                return;
            }

            this.piercedEntities.add(entity.getEntityId());
        }

        if (this.getIsCritical()) {
            long j = (long)this.rand.nextInt(i / 2 + 2);
            i = (int)Math.min(j + (long)i, 2147483647L);
        }

        if (shooter != null)
            shooter.setLastAttackedEntity(entity); //设置最后攻击者

        if (shooter instanceof PlayerEntity){
            InfinitySword.damageGuardian(entity, (PlayerEntity) shooter);
        }

        if (this.isBurning()) {
            entity.setFire(5);
        }

        if (entity instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)entity;
            if (!this.world.isRemote && this.getPierceLevel() <= 0) {
                livingentity.setArrowCountInEntity(livingentity.getArrowCountInEntity() + 1);
            }

            if (this.knockbackStrength > 0) { //击退
                Vector3d vector3d = this.getMotion().mul(1.0D, 0.0D, 1.0D).normalize().scale((double)this.knockbackStrength * 0.6D);
                if (vector3d.lengthSquared() > 0.0D) {
                    livingentity.addVelocity(vector3d.x, 0.1D, vector3d.z);
                }
            }

            if (!this.world.isRemote) {
                EnchantmentHelper.applyThornEnchantments(livingentity, shooter);
                EnchantmentHelper.applyArthropodEnchantments(shooter, livingentity);
            }

            this.arrowHit(livingentity);
            if (shooter != null && livingentity != shooter && livingentity instanceof PlayerEntity && shooter instanceof ServerPlayerEntity && !this.isSilent()) {
                ((ServerPlayerEntity)shooter).connection.sendPacket(new SChangeGameStatePacket(SChangeGameStatePacket.HIT_PLAYER_ARROW, 0.0F));
            }

            if (!entity.isAlive() && this.hitEntities != null) {
                this.hitEntities.add(livingentity);
            }

            if (!this.world.isRemote && shooter instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)shooter;
                if (this.hitEntities != null && this.getShotFromCrossbow()) {
                    CriteriaTriggers.KILLED_BY_CROSSBOW.test(serverplayerentity, this.hitEntities);
                } else if (!entity.isAlive() && this.getShotFromCrossbow()) {
                    CriteriaTriggers.KILLED_BY_CROSSBOW.test(serverplayerentity, Collections.singletonList(entity));
                }
            }
        } else {
            entity.attackEntityFrom(new InfinityDamageSource(this.shooter), (float)i);
        }
        this.playSound(this.hitSound, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        if (this.getPierceLevel() <= 0) {
            this.remove();
        }//反弹箭矢
    }

    @Override
    protected void arrowHit(LivingEntity living) {
        if (living.world.isRemote) return;
        if (living instanceof WitherEntity){
            WitherEntity wither = (WitherEntity) living;
            wither.setInvulTime(0);
            wither.attackEntityFrom(new InfinityDamageSource(this.shooter), Float.POSITIVE_INFINITY);
        } else if (living instanceof EnderDragonEntity && this.shooter instanceof PlayerEntity){
            EnderDragonEntity dragon = (EnderDragonEntity) living; //攻击末影龙
            dragon.attackEntityPartFrom(dragon.dragonPartHead, new InfinityDamageSource(this.shooter), Float.POSITIVE_INFINITY);
        } else if (living instanceof ArmorStandEntity){
            living.attackEntityFrom(DamageSource.GENERIC, 10);
        } else if (Endless.isDE && living instanceof DraconicGuardianEntity){
            DraconicGuardianEntity draconicGuardian = (DraconicGuardianEntity) living;
            draconicGuardian.attackEntityPartFrom(draconicGuardian.dragonPartHead, new InfinityDamageSource(this.shooter), Float.POSITIVE_INFINITY);
            draconicGuardian.setHealth(-1);
            draconicGuardian.onDeath(new InfinityDamageSource(this.shooter));
        } else {
            if (living instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) living;
                if (EventHandler.isInfinite(player)){ //被攻击玩家有全套无尽 减免至10点
                    if (EventHandler.isInfinityItem(player)) //玩家在持有无尽剑或弓时 减免至4点
                        living.attackEntityFrom(new InfinityDamageSource(this.shooter), Config.SERVER.infinityBearDamage.get());
                    else living.attackEntityFrom(new InfinityDamageSource(this.shooter), Config.SERVER.infinityArmorBearDamage.get());
                } else living.attackEntityFrom(new InfinityDamageSource(this.shooter),  Float.POSITIVE_INFINITY);
            } else living.attackEntityFrom(new InfinityDamageSource(this.shooter), Float.POSITIVE_INFINITY);
            if (living instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) living;
                if (EventHandler.isInfinite(player)){
                    this.remove();
                    return;
                }
            }
            living.setHealth(-1);
        }
    }

    @Override
    protected void func_230299_a_(BlockRayTraceResult result) {
        super.func_230299_a_(result);
        if (!isSub) return;
        BlockPos pos = result.getPos();
        Random random = getEntityWorld().rand;
        for (int i = 0; i < 30; i++) { //生成一片光箭
            double angle = random.nextDouble() * 2 * Math.PI;
            double dist = random.nextGaussian() * 0.5;

            double x = Math.sin(angle) * dist + pos.getX();
            double z = Math.cos(angle) * dist + pos.getZ();
            double y = pos.getY() + 25.0;

            double dangle = random.nextDouble() * 2 * Math.PI;
            double ddist = random.nextDouble() * 0.35;
            double dx = Math.sin(dangle) * ddist;
            double dz = Math.cos(dangle) * ddist;

            InfinityArrowSubEntity arrow = new InfinityArrowSubEntity(EntityRegistry.INFINITY_ARROW_SUB.get(), x, y, z, world);
            if (shooter != null) arrow.setShooter(shooter);
            arrow.addVelocity(dx, -(random.nextDouble() * 1.85 + 0.15), dz);
            arrow.setIsCritical(true);
            arrow.pickupStatus = pickupStatus;

            world.addEntity(arrow);
        }
        this.setDead();
    }

}
