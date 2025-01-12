package com.yuo.endless.Entity;

import com.google.common.collect.Sets;
import com.yuo.endless.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
    private Potion potion = Potions.EMPTY;
    private final Set<MobEffectInstance> effects;
    private boolean fixedColor;
    private boolean isLighting; //是否是光灵箭
    public InfinityArrowSubEntity(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
        this.setBaseDamage(Config.SERVER.subArrowDamage.get());
        this.effects = Sets.newHashSet();
    }

    public InfinityArrowSubEntity(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level worldIn) {
        super(type, x, y, z, worldIn);
        this.setBaseDamage(Config.SERVER.subArrowDamage.get());
        this.effects = Sets.newHashSet();
    }

    public InfinityArrowSubEntity(EntityType<? extends AbstractArrow> type, LivingEntity shooter, Level worldIn, ItemStack stack) {
        super(type, shooter, worldIn);
        this.setBaseDamage(Config.SERVER.subArrowDamage.get());
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
        compound.putDouble("damage", Config.SERVER.subArrowDamage.get());
        if (this.potion != Potions.EMPTY && this.potion != null) {
            compound.putString("Potion", Registry.POTION.getKey(this.potion).toString());
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
    }

    @Override
    public Packet<?> getAddEntityPacket() {
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
        if (inGround && inGroundTime >= 100){
            discard();
        }else if (tickCount > 100) discard();

        if (tickCount % 4 == 0 && !this.level.isClientSide){
            BlockPos pos = this.getOnPos();
            int distance = 32; //水平追踪距离
            int height = 16; //垂直
            AABB aabb = new AABB(pos.offset(-distance, -height, -distance), pos.offset(distance, height, distance));
            List<LivingEntity> entityList = this.level.getEntitiesOfClass(LivingEntity.class, aabb);
            double dis = 1000;
            LivingEntity living = null;
            for (LivingEntity livingentity : entityList) {
                if (getOwner() != null && livingentity == getOwner()) continue;
                double sq = livingentity.distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
                if (sq < dis) {
                    dis = sq;
                    living = livingentity; //选定最近目标
                }
            }
            if (living != null && living.isAlive()) {
                Vec3 originalPosVector = new Vec3(pos.getX(), pos.getY(), pos.getZ());
                Vec3 finalVector = originalPosVector.subtract(living.position());
                if (finalVector.length() > 1) {
                    finalVector.normalize();
                }
                double motionX = finalVector.x * -2;
                double motionY = finalVector.y * -2;
                double motionZ = finalVector.z * -2;
                this.setDeltaMovement(motionX, motionY, motionZ); //向目标飞行
            }
        }
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
        if (isLighting)
            living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 10, 0)); //10秒发光
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
