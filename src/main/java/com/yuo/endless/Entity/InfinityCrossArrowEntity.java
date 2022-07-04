package com.yuo.endless.Entity;

import com.brandon3055.draconicevolution.entity.GuardianCrystalEntity;
import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.Tool.InfinityDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;

//箭实体
public class InfinityCrossArrowEntity extends AbstractArrowEntity {
    private LivingEntity shooter;
    public InfinityCrossArrowEntity(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
        this.setDamage(10000f);
    }

    public InfinityCrossArrowEntity(EntityType<? extends AbstractArrowEntity> type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
        this.setDamage(10000f);
    }

    public InfinityCrossArrowEntity(EntityType<? extends AbstractArrowEntity> type, LivingEntity shooter, World worldIn) {
        super(type, shooter, worldIn);
        this.setDamage(10000f);
        this.shooter = shooter;
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
        super.onEntityHit(result);
        Entity entity = result.getEntity();
        if (entity instanceof LivingEntity){
            if (Endless.isDraconicEvolution){
                if (entity instanceof GuardianEntity){
                    GuardianEntity guardian = (GuardianEntity) entity;
                    guardian.attackEntityFrom(new InfinityDamageSource(this.shooter), Float.POSITIVE_INFINITY);
                    guardian.setHealth(0);
                    guardian.remove();
                }else if (entity instanceof DraconicGuardianEntity){
                    DraconicGuardianEntity draconicGuardian = (DraconicGuardianEntity) entity;
                    draconicGuardian.attackEntityPartFrom(draconicGuardian.dragonPartHead, new InfinityDamageSource(this.shooter),Float.POSITIVE_INFINITY);
                    draconicGuardian.setHealth(0);
                }
            }else {
                LivingEntity living = (LivingEntity) entity;
                living.attackEntityFrom(new InfinityDamageSource(this.shooter), Float.POSITIVE_INFINITY);
                if (living instanceof PlayerEntity){
                    PlayerEntity player = (PlayerEntity) living;
                    if (EventHandler.isInfinite(player)){
                        return;
                    }
                }
                living.setHealth(0);
            }
            this.setDead();
        }else if (Endless.isDraconicEvolution){
            if (entity instanceof GuardianCrystalEntity && Config.SERVER.isBreakDECrystal.get()){
                GuardianCrystalEntity crystal = (GuardianCrystalEntity) entity;
                crystal.func_174812_G();
            }
        }
    }

}
