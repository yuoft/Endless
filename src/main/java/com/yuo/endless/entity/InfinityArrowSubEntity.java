package com.yuo.endless.entity;

import com.google.common.collect.Sets;
import com.yuo.endless.config.ModConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

//箭实体
public class InfinityArrowSubEntity extends AbstractArrow {
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(InfinityArrowSubEntity.class, EntityDataSerializers.INT);
    private static final String TARGET_ID_TAG = "InfinityArrowTargetId";
    private static final String TARGET_TIME_TAG = "InfinityArrowTargetTime";
    private static final long TARGET_LOCK_DURATION = 60; // 目标锁定时间（tick）

    private Potion potion = Potions.EMPTY;
    private final Set<MobEffectInstance> effects;
    private boolean fixedColor;
    private boolean isLighting; //是否是光灵箭
    private int currentTargetId = -1; // 当前追踪的目标ID（使用int类型的实体ID）
    private int noTargetCooldown = 0; // 无目标冷却

    public InfinityArrowSubEntity(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
        this.setBaseDamage(ModConfig.SERVER.subArrowDamage.get());
        this.effects = Sets.newHashSet();
    }

    public InfinityArrowSubEntity(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level worldIn) {
        super(type, x, y, z, worldIn);
        this.setBaseDamage(ModConfig.SERVER.subArrowDamage.get());
        this.effects = Sets.newHashSet();
    }

    public InfinityArrowSubEntity(EntityType<? extends AbstractArrow> type, LivingEntity shooter, Level worldIn, ItemStack stack) {
        super(type, shooter, worldIn);
        this.setBaseDamage(ModConfig.SERVER.subArrowDamage.get());
        this.isLighting = stack.getItem() == Items.SPECTRAL_ARROW;
        this.setPotionEffect(stack); //添加药水效果
        this.effects = Sets.newHashSet();
    }

    @Override
    protected ItemStack getPickupItem() {
        if (this.effects.isEmpty() && this.potion == Potions.EMPTY) {
            return new ItemStack(Items.ARROW);
        } else {
            ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
            PotionUtils.setPotion(itemstack, this.potion);
            PotionUtils.setCustomEffects(itemstack, this.effects);
            if (this.fixedColor) {
                itemstack.getOrCreateTag().putInt("CustomPotionColor", this.getColor());
            }
            return itemstack;
        }
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putDouble("damage", ModConfig.SERVER.subArrowDamage.get());
        if (this.potion != Potions.EMPTY && this.potion != null) {
            compound.putString("Potion", BuiltInRegistries.POTION.getKey(this.potion).toString());
        }
        if (this.fixedColor) {
            compound.putInt("Color", this.getColor());
        }
        if (!this.effects.isEmpty()) {
            ListTag listnbt = new ListTag();
            for(MobEffectInstance instance : this.effects) {
                listnbt.add(instance.save(new CompoundTag()));
            }
            compound.put("CustomPotionEffects", listnbt);
        }
        if (currentTargetId != -1) {
            compound.putInt(TARGET_ID_TAG, currentTargetId);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBaseDamage(compound.getDouble("damage"));
        if (compound.contains("Potion", 8)) {
            this.potion = PotionUtils.getPotion(compound);
        }
        for(MobEffectInstance instance : PotionUtils.getCustomEffects(compound)) {
            this.addEffect(instance);
        }
        if (compound.contains("Color", 99)) {
            this.setFixedColor(compound.getInt("Color"));
        } else {
            this.refreshColor();
        }
        if (compound.contains(TARGET_ID_TAG)) {
            this.currentTargetId = compound.getInt(TARGET_ID_TAG);
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level.isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.spawnPotionParticles(1);
                }
            } else {
                this.spawnPotionParticles(2);
            }
        } else if (this.inGround && this.inGroundTime != 0 && !this.effects.isEmpty() && this.inGroundTime >= 600) {
            this.level.broadcastEntityEvent(this, (byte)0);
            this.potion = Potions.EMPTY;
            this.effects.clear();
            this.entityData.set(COLOR, -1);
        }

        if (inGround && inGroundTime >= 100) {
            // 清除目标标记
            clearTargetMark();
            discard();
        } else if (tickCount > 200) {
            clearTargetMark();
            discard();
        }

        // 追踪逻辑 - 只在未落地时执行，每2 tick执行一次
        if (!this.inGround && !this.level.isClientSide && (tickCount % 2 == 0)) {
            updateTargetTracking();
        }

        // 冷却递减
        if (noTargetCooldown > 0) {
            noTargetCooldown--;
        }
    }

    private void updateTargetTracking() {
        Vec3 currentPos = this.position();
        LivingEntity target = null;

        // 优先追踪已锁定的目标（使用int类型的实体ID）
        if (currentTargetId != -1) {
            Entity entity = this.level.getEntity(currentTargetId);
            if (entity instanceof LivingEntity && entity.isAlive()) {
                target = (LivingEntity) entity;
                // 检查目标是否还在范围内
                double distance = target.distanceToSqr(currentPos);
                if (distance > 64 * 64) { // 超出64格，放弃追踪
                    target = null;
                    clearTargetMark();
                }
            } else {
                clearTargetMark();
            }
        }

        // 如果没有锁定目标，搜索新目标
        if (target == null && noTargetCooldown == 0) {
            int distance = 32;
            int height = 16;

            AABB aabb = new AABB(
                    currentPos.x - distance, currentPos.y - height, currentPos.z - distance,
                    currentPos.x + distance, currentPos.y + height, currentPos.z + distance
            );

            List<LivingEntity> entityList = this.level.getEntitiesOfClass(LivingEntity.class, aabb, e -> {
                if (getOwner() != null && e == getOwner()) return false;
                return e.isAlive();
            });

            double minDistance = 1000;
            LivingEntity potentialTarget = null;

            for (LivingEntity living : entityList) {
                // 检查是否已被其他箭矢追踪
                CompoundTag data = living.getPersistentData();
                long lastTargetTime = data.getLong(TARGET_TIME_TAG);
                long currentTime = this.level.getGameTime();

                // 如果目标在锁定时间内，跳过
                if (currentTime - lastTargetTime < TARGET_LOCK_DURATION) {
                    continue;
                }

                double sqDistance = living.distanceToSqr(currentPos);
                if (sqDistance < minDistance) {
                    minDistance = sqDistance;
                    potentialTarget = living;
                }
            }

            if (potentialTarget != null) {
                target = potentialTarget;
                // 标记目标
                CompoundTag data = target.getPersistentData();
                data.putInt(TARGET_ID_TAG, this.getId()); // 存储箭矢的实体ID
                data.putLong(TARGET_TIME_TAG, this.level.getGameTime());
                this.currentTargetId = target.getId(); // 存储目标的实体ID
            } else {
                // 没有找到目标，设置冷却避免每帧都搜索
                noTargetCooldown = 10;
            }
        }

        // 追踪目标
        if (target != null && target.isAlive()) {
            // 计算方向向量（从箭矢指向目标）
            Vec3 direction = target.position().subtract(currentPos);
            double length = direction.length();

            if (length > 0.5) {
                // 不限制速度，只根据距离调整（距离越远越快）
                double speed = Math.max(1.5, length * 0.2);
                // 最大速度限制为 5.0，避免飞出边界
                speed = Math.min(5.0, speed);

                // 归一化后乘以速度
                Vec3 velocity = direction.normalize().scale(speed);

                // 不限制垂直速度，让箭矢自由追踪
                this.setDeltaMovement(velocity);

                // 更新箭矢的旋转角度
                this.setYRot((float) (Math.atan2(velocity.x, velocity.z) * 180 / Math.PI));
                this.setXRot((float) (Math.atan2(velocity.y, Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z)) * 180 / Math.PI));
            }
        }
    }

    private void clearTargetMark() {
        if (currentTargetId != -1) {
            Entity entity = this.level.getEntity(currentTargetId);
            if (entity instanceof LivingEntity) {
                CompoundTag data = entity.getPersistentData();
                // 只有当标记的是当前箭矢时才清除
                if (this.getId() == data.getInt(TARGET_ID_TAG)) {
                    data.remove(TARGET_ID_TAG);
                    data.remove(TARGET_TIME_TAG);
                }
            }
            currentTargetId = -1;
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        // 命中后清除目标标记
        clearTargetMark();
    }

    @Override
    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        for(MobEffectInstance instance : this.potion.getEffects()) {
            living.addEffect(new MobEffectInstance(instance.getEffect(), Math.max(instance.getDuration() / 8, 1), instance.getAmplifier(), instance.isAmbient(), instance.isVisible()));
        }
        if (!this.effects.isEmpty()) {
            for(MobEffectInstance effectInstance : this.effects) {
                living.addEffect(effectInstance);
            }
        }
        if (isLighting) {
            living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 10, 0)); //10秒发光
        }
        // 命中后清除目标标记
        clearTargetMark();
    }

    @Override
    protected float getWaterInertia() {
        return 0.99f;
    }

    public void setPotionEffect(ItemStack stack) {
        if (stack.is(Items.TIPPED_ARROW)) {
            this.potion = PotionUtils.getPotion(stack);
            List<MobEffectInstance> collection = PotionUtils.getCustomEffects(stack);
            if (!collection.isEmpty()) {
                for(MobEffectInstance instance : collection) {
                    this.effects.add(new MobEffectInstance(instance));
                }
            }
            int i = getCustomColor(stack);
            if (i == -1) {
                this.refreshColor();
            } else {
                this.setFixedColor(i);
            }
        } else if (stack.is(Items.ARROW)) {
            this.potion = Potions.EMPTY;
            if (this.effects != null)
                this.effects.clear();
            this.entityData.set(COLOR, -1);
        }
    }

    public static int getCustomColor(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("CustomPotionColor", 99) ? tag.getInt("CustomPotionColor") : -1;
    }

    private void refreshColor() {
        this.fixedColor = false;
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.entityData.set(COLOR, -1);
        } else {
            this.entityData.set(COLOR, PotionUtils.getColor(this.effects));
        }
    }

    public void addEffect(MobEffectInstance effect) {
        this.effects.add(effect);
        this.entityData.set(COLOR, PotionUtils.getColor(this.effects));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COLOR, -1);
    }

    private void spawnPotionParticles(int particleCount) {
        int i = this.getColor();
        if (i != -1 && particleCount > 0) {
            double d0 = (double)(i >> 16 & 255) / 255.0D;
            double d1 = (double)(i >> 8 & 255) / 255.0D;
            double d2 = (double)(i & 255) / 255.0D;
            for(int j = 0; j < particleCount; ++j) {
                this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
            }
        }
    }

    public int getColor() {
        return this.entityData.get(COLOR);
    }

    private void setFixedColor(int i) {
        this.fixedColor = true;
        this.entityData.set(COLOR, i);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 0) {
            int i = this.getColor();
            if (i != -1) {
                double d0 = (double)(i >> 16 & 255) / 255.0D;
                double d1 = (double)(i >> 8 & 255) / 255.0D;
                double d2 = (double)(i & 255) / 255.0D;
                for(int j = 0; j < 20; ++j) {
                    this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), d0, d1, d2);
                }
            }
        } else {
            super.handleEntityEvent(id);
        }
    }
}