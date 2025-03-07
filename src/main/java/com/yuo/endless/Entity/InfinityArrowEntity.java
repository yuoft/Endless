package com.yuo.endless.Entity;

import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity;
import com.google.common.collect.Lists;
import com.yuo.endless.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.Tool.InfinityDamageSource;
import com.yuo.endless.Items.Tool.InfinitySword;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
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
        this.shooter = shooter;
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
    public Packet<?> getAddEntityPacket() {
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
        Entity entity = result.getEntity(); //被击中的实体
        float f = (float)this.getDeltaMovement().length();
        int i = Mth.ceil(Mth.clamp((double)f * this.baseDamage, 0.0D, 2.147483647E9D));
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

            this.piercingIgnoreEntityIds.add(entity.getId());
        }

        if (this.isCritArrow()) {
            long j = (long)this.random.nextInt(i / 2 + 2);
            i = (int)Math.min(j + (long)i, 2147483647L);
        }

        if (shooter != null)
            shooter.setLastHurtMob(entity); //设置最后攻击者

        if (shooter instanceof Player){
            InfinitySword.damageGuardian(entity, (Player) shooter);
        }

        if (this.isOnFire()) {
            entity.setSecondsOnFire(5);
        }

        if (entity instanceof LivingEntity livingentity) {
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

            if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
                this.piercedAndKilledEntities.add(livingentity);
            }

            if (!this.level.isClientSide && shooter instanceof ServerPlayer serverPlayer) {
                if (this.piercedAndKilledEntities != null && this.shotFromCrossbow()) {
                    CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverPlayer, this.piercedAndKilledEntities);
                } else if (!entity.isAlive() && this.shotFromCrossbow()) {
                    CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverPlayer, Collections.singletonList(entity));
                }
            }
        } else {
            entity.hurt(new InfinityDamageSource(this.shooter), (float)i);
        }
        this.playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        if (this.getPierceLevel() <= 0) {
            this.discard();
        }//反弹箭矢
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        if (living.level.isClientSide) return;
        if (living instanceof WitherBoss wither){
            wither.setInvulnerableTicks(0);
            wither.hurt(new InfinityDamageSource(this.shooter), Float.MAX_VALUE);
        } else if (living instanceof EnderDragon dragon && this.shooter instanceof Player){
            dragon.hurt(dragon.head, new InfinityDamageSource(this.shooter), Float.MAX_VALUE);
        } else if (living instanceof ArmorStand){
            living.hurt(DamageSource.GENERIC, 10);
        } else if (Endless.isDE && living instanceof DraconicGuardianEntity){
            DraconicGuardianEntity draconicGuardian = (DraconicGuardianEntity) living;
            draconicGuardian.attackEntityPartFrom(draconicGuardian.dragonPartHead, new InfinityDamageSource(this.shooter), Float.MAX_VALUE);
            draconicGuardian.setHealth(-1);
            draconicGuardian.die(new InfinityDamageSource(this.shooter));
        } else {
            if (living instanceof Player player){
                if (EventHandler.isInfinite(player)){ //被攻击玩家有全套无尽 减免至10点
                    if (EventHandler.isInfinityItem(player)) //玩家在持有无尽剑或弓时 减免至4点
                        living.hurt(new InfinityDamageSource(this.shooter), Config.SERVER.infinityBearDamage.get());
                    else living.hurt(new InfinityDamageSource(this.shooter), Config.SERVER.infinityArmorBearDamage.get());
                } else living.hurt(new InfinityDamageSource(this.shooter),  Float.MAX_VALUE);
            } else living.hurt(new InfinityDamageSource(this.shooter), Float.MAX_VALUE);
            if (living instanceof Player player){
                if (EventHandler.isInfinite(player)){
                    this.discard();
                    return;
                }
            }
            living.setHealth(-1);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!isSub) return;
        BlockPos pos = result.getBlockPos();
        Random random = level.random;
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
