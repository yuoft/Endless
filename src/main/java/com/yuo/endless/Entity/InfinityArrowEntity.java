package com.yuo.endless.Entity;

import com.brandon3055.draconicevolution.blocks.ChaosCrystal;
import com.brandon3055.draconicevolution.entity.GuardianCrystalEntity;
import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.Tool.InfinityDamageSource;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.NetworkInstance;
import net.minecraftforge.fml.network.simple.IndexedMessageCodec;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

//箭实体
public class InfinityArrowEntity extends AbstractArrowEntity {
    private LivingEntity shooter; //使用玩家
    private boolean isSub; //是否召唤无尽光箭
    public InfinityArrowEntity(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
        this.setDamage(10000f);
    }

    public InfinityArrowEntity(EntityType<? extends AbstractArrowEntity> type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
        this.setDamage(10000f);
    }

    public InfinityArrowEntity(EntityType<? extends AbstractArrowEntity> type, LivingEntity shooter, World worldIn, boolean isSub) {
        super(type, shooter, worldIn);
        this.setDamage(10000f);
        this.shooter = shooter;
        this.isSub = isSub;
    }

    @Override
    protected ItemStack getArrowStack() {
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

        if (Endless.isDraconicEvolution){
            if (entity instanceof GuardianCrystalEntity && Config.SERVER.isBreakDECrystal.get()){
                GuardianCrystalEntity crystal = (GuardianCrystalEntity) entity;
                crystal.func_174812_G();
                this.remove();
                return;
            }
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
                    CriteriaTriggers.KILLED_BY_CROSSBOW.test(serverplayerentity, Arrays.asList(entity));
                }
            }
        }
        else {
            entity.attackEntityFrom(new InfinityDamageSource(this.shooter), (float)i);
        }
        this.playSound(this.hitSound, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        if (this.getPierceLevel() <= 0) {
            this.remove();
        }
//        } else { //反弹箭矢
//            entity.forceFireTicks(k);
//            this.setMotion(this.getMotion().scale(-0.1D));
//            this.rotationYaw += 180.0F;
//            this.prevRotationYaw += 180.0F;
//            if (!this.world.isRemote && this.getMotion().lengthSquared() < 1.0E-7D) {
//                if (this.pickupStatus == AbstractArrowEntity.PickupStatus.ALLOWED) {
//                    this.entityDropItem(this.getArrowStack(), 0.1F);
//                }
//
//                this.remove();
//            }
//        }

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
        } else if (Endless.isDraconicEvolution && living instanceof DraconicGuardianEntity){
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
