package com.yuo.endless.Entity;

import com.google.common.collect.Lists;
import com.yuo.endless.Config.ModConfig;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.Tool.InfinityDamageTypes;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
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
        this.shooter = shooter;//(LivingEntity) getOwner();
        this.isSub = isSub;
    }

    //捡起来的物品
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putDouble("damage", Float.MAX_VALUE);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setBaseDamage(pCompound.getDouble("damage"));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    //减小水的阻力
    @Override
    protected float getWaterInertia() {
        return 0.99f;
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > 200) discard(); //10秒后死亡
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity(); //被击中的实体
        if (this.getPierceLevel() > 0) {  //穿透等级
            if (this.piercingIgnoreEntityIds == null) {
                this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }

            if (this.piercedAndKilledEntities == null) {
                this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1) {
                this.discard();
                return;
            }

            this.piercingIgnoreEntityIds.add(target.getId());
        }

        DamageSource damageSource;
        if (shooter == null){
            damageSource = this.damageSources().fellOutOfWorld();
        }else {
            damageSource = InfinityDamageTypes.infinity(this.shooter);
            shooter.setLastHurtMob(target); //设置最后攻击者

            if (shooter instanceof Player){
                if (target instanceof LivingEntity living)
                    EndlessUtils.atkInfinity(living, shooter);
                if (ModConfig.SERVER.isBreakDECrystal.get())
                    EndlessUtils.damageGuardian(target, (Player) shooter);
            }
        }


        if (this.isOnFire()) {
            target.setSecondsOnFire(5);
        }

        if (target.hurt(damageSource, Float.MAX_VALUE)){
            if (target instanceof LivingEntity livingentity) {
                if (!this.level.isClientSide && this.getPierceLevel() <= 0) {
                    livingentity.setArrowCount(livingentity.getArrowCount() + 1);
                }

                if (this.knockback > 0) { //击退
                    Vec3 vector3d = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double)this.knockback * 0.6D);
                    if (vector3d.lengthSqr() > 0.0D) {
                        livingentity.push(vector3d.x, 0.1D, vector3d.z);
                    }
                }

                if (!this.level.isClientSide) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, shooter);
                    EnchantmentHelper.doPostDamageEffects(shooter, livingentity);
                }

                this.doPostHurtEffects(livingentity);
                if (shooter != null && livingentity != shooter && livingentity instanceof Player && shooter instanceof ServerPlayer && !this.isSilent()) {
                    ((ServerPlayer)shooter).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
                }

                if (!target.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(livingentity);
                }

                if (!this.level.isClientSide && shooter instanceof ServerPlayer serverPlayer) {
                    if (this.piercedAndKilledEntities != null && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverPlayer, this.piercedAndKilledEntities);
                    } else if (!target.isAlive() && this.shotFromCrossbow()) {
                        CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverPlayer, Collections.singletonList(target));
                    }
                }
            }
            this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                this.discard();
            }
        }//反弹箭矢
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        if (living.level().isClientSide) return;

        if (living instanceof Player player){
            if (EventHandler.isInfinite(player)){
                this.discard();
                return;
            }
        }
        EndlessUtils.atkInfinity(living, this.shooter);
        if (living.isAlive()) living.setHealth(-1);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!isSub) return;
        BlockPos pos = result.getBlockPos();
        Random random = new Random();
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

            InfinityArrowSubEntity arrow = new InfinityArrowSubEntity(EntityRegistry.INFINITY_ARROW_SUB.get(), x, y, z, level);
            if (shooter != null) arrow.setOwner(shooter);
            arrow.push(dx, -(random.nextDouble() * 1.85 + 0.15), dz);
            arrow.setCritArrow(true);
            arrow.pickup = pickup;

            level.addFreshEntity(arrow);
        }
        this.discard();
    }
}
