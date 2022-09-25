package com.yuo.endless.Entity;

import com.google.common.collect.Sets;
import com.yuo.endless.Config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Set;

//箭实体
public class InfinityArrowSubEntity extends AbstractArrowEntity {
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(InfinityArrowSubEntity.class, DataSerializers.VARINT);
    private Potion potion = Potions.EMPTY;
    private final Set<EffectInstance> customPotionEffects = Sets.newHashSet();
    private boolean fixedColor;
    private boolean isLighting; //是否是光灵箭
    public InfinityArrowSubEntity(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
        this.setDamage(Config.SERVER.subArrowDamage.get());
    }

    public InfinityArrowSubEntity(EntityType<? extends AbstractArrowEntity> type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
        this.setDamage(Config.SERVER.subArrowDamage.get());
    }

    public InfinityArrowSubEntity(EntityType<? extends AbstractArrowEntity> type, LivingEntity shooter, World worldIn, ItemStack stack) {
        super(type, shooter, worldIn);
        this.setDamage(Config.SERVER.subArrowDamage.get());
        this.isLighting = stack.getItem() == Items.SPECTRAL_ARROW;
        this.setPotionEffect(stack); //添加药水效果
    }

    @Nonnull
    @Override
    protected ItemStack getArrowStack() {
        if (this.customPotionEffects.isEmpty() && this.potion == Potions.EMPTY) {
            return new ItemStack(Items.ARROW);
        } else {
            ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
            PotionUtils.addPotionToItemStack(itemstack, this.potion);
            PotionUtils.appendEffects(itemstack, this.customPotionEffects);
            if (this.fixedColor) {
                itemstack.getOrCreateTag().putInt("CustomPotionColor", this.getColor());
            }

            return itemstack;
        }
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putDouble("damage", Config.SERVER.subArrowDamage.get());
        if (this.potion != Potions.EMPTY && this.potion != null) {
            compound.putString("Potion", Registry.POTION.getKey(this.potion).toString());
        }

        if (this.fixedColor) {
            compound.putInt("Color", this.getColor());
        }

        if (!this.customPotionEffects.isEmpty()) {
            ListNBT listnbt = new ListNBT();

            for(EffectInstance effectinstance : this.customPotionEffects) {
                listnbt.add(effectinstance.write(new CompoundNBT()));
            }

            compound.put("CustomPotionEffects", listnbt);
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setDamage(compound.getDouble("damage"));
        if (compound.contains("Potion", 8)) {
            this.potion = PotionUtils.getPotionTypeFromNBT(compound);
        }

        for(EffectInstance effectinstance : PotionUtils.getFullEffectsFromTag(compound)) {
            this.addEffect(effectinstance);
        }

        if (compound.contains("Color", 99)) {
            this.setFixedColor(compound.getInt("Color"));
        } else {
            this.refreshColor();
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isRemote) {
            if (this.inGround) {
                if (this.timeInGround % 5 == 0) {
                    this.spawnPotionParticles(1);
                }
            } else {
                this.spawnPotionParticles(2);
            }
        } else if (this.inGround && this.timeInGround != 0 && !this.customPotionEffects.isEmpty() && this.timeInGround >= 600) {
            this.world.setEntityState(this, (byte)0);
            this.potion = Potions.EMPTY;
            this.customPotionEffects.clear();
            this.dataManager.set(COLOR, -1);
        }
        if (inGround && timeInGround >= 100){
            setDead();
        }else if (ticksExisted > 100) setDead();
    }

    @Override
    protected void arrowHit(LivingEntity living) {
        super.arrowHit(living);
        for(EffectInstance effectinstance : this.potion.getEffects()) {
            living.addPotionEffect(new EffectInstance(effectinstance.getPotion(), Math.max(effectinstance.getDuration() / 8, 1), effectinstance.getAmplifier(), effectinstance.isAmbient(), effectinstance.doesShowParticles()));
        }

        if (!this.customPotionEffects.isEmpty()) {
            for(EffectInstance effectInstance : this.customPotionEffects) {
                living.addPotionEffect(effectInstance);
            }
        }
        if (isLighting)
            living.addPotionEffect(new EffectInstance(Effects.GLOWING, 20 * 10, 0)); //10秒发光
    }

    @Override
    protected float getWaterDrag() {
        return 0.99f;
    }

    public void setPotionEffect(ItemStack stack) {
        if (stack.getItem() == Items.TIPPED_ARROW) {
            this.potion = PotionUtils.getPotionFromItem(stack);
            Collection<EffectInstance> collection = PotionUtils.getFullEffectsFromItem(stack);
            if (!collection.isEmpty()) {
                for(EffectInstance effectinstance : collection) {
                    this.customPotionEffects.add(new EffectInstance(effectinstance));
                }
            }

            int i = getCustomColor(stack);
            if (i == -1) {
                this.refreshColor();
            } else {
                this.setFixedColor(i);
            }
        } else if (stack.getItem() == Items.ARROW) {
            this.potion = Potions.EMPTY;
            this.customPotionEffects.clear();
            this.dataManager.set(COLOR, -1);
        }

    }

    public static int getCustomColor(ItemStack p_191508_0_) {
        CompoundNBT compoundnbt = p_191508_0_.getTag();
        return compoundnbt != null && compoundnbt.contains("CustomPotionColor", 99) ? compoundnbt.getInt("CustomPotionColor") : -1;
    }

    private void refreshColor() {
        this.fixedColor = false;
        if (this.potion == Potions.EMPTY && this.customPotionEffects.isEmpty()) {
            this.dataManager.set(COLOR, -1);
        } else {
            this.dataManager.set(COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects)));
        }

    }

    public void addEffect(EffectInstance effect) {
        this.customPotionEffects.add(effect);
        this.getDataManager().set(COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects)));
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(COLOR, -1);
    }

    private void spawnPotionParticles(int particleCount) {
        int i = this.getColor();
        if (i != -1 && particleCount > 0) {
            double d0 = (double)(i >> 16 & 255) / 255.0D;
            double d1 = (double)(i >> 8 & 255) / 255.0D;
            double d2 = (double)(i & 255) / 255.0D;

            for(int j = 0; j < particleCount; ++j) {
                this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
            }

        }
    }

    public int getColor() {
        return this.dataManager.get(COLOR);
    }

    private void setFixedColor(int p_191507_1_) {
        this.fixedColor = true;
        this.dataManager.set(COLOR, p_191507_1_);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 0) {
            int i = this.getColor();
            if (i != -1) {
                double d0 = (double)(i >> 16 & 255) / 255.0D;
                double d1 = (double)(i >> 8 & 255) / 255.0D;
                double d2 = (double)(i & 255) / 255.0D;

                for(int j = 0; j < 20; ++j) {
                    this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), d0, d1, d2);
                }
            }
        } else {
            super.handleStatusUpdate(id);
        }

    }
}
