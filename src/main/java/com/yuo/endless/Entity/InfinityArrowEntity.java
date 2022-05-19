package com.yuo.endless.Entity;

import com.yuo.endless.Items.Tool.InfinityDamageSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.NetworkInstance;
import net.minecraftforge.fml.network.simple.IndexedMessageCodec;

import java.util.Random;

//箭实体
public class InfinityArrowEntity extends AbstractArrowEntity {
    private LivingEntity shooter;
    public InfinityArrowEntity(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
        this.setDamage(10000f);
    }

    public InfinityArrowEntity(EntityType<? extends AbstractArrowEntity> type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
        this.setDamage(10000f);
    }

    public InfinityArrowEntity(EntityType<? extends AbstractArrowEntity> type, LivingEntity shooter, World worldIn) {
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
            LivingEntity living = (LivingEntity) entity;
            living.attackEntityFrom(new InfinityDamageSource(this.shooter), Float.POSITIVE_INFINITY);
            living.setHealth(0);
            if (!(living instanceof PlayerEntity))
                living.onDeath(new InfinityDamageSource(this.shooter));
            this.setDead();
        }
    }

    protected void func_230299_a_(BlockRayTraceResult result) {
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
