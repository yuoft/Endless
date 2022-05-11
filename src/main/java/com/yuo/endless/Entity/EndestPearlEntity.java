package com.yuo.endless.Entity;

import com.yuo.endless.Items.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

//终末珍珠投掷物实体
public class EndestPearlEntity extends ProjectileItemEntity {
    protected EndestPearlEntity(EntityType<? extends ProjectileItemEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected EndestPearlEntity(EntityType<? extends ProjectileItemEntity> type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
    }

    private LivingEntity shooter;
    public EndestPearlEntity(EntityType<? extends ProjectileItemEntity> type, LivingEntity livingEntityIn, World worldIn) {
        super(type, livingEntityIn, worldIn);
        this.shooter = livingEntityIn;
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.endestPearl.get().asItem();
    }

//    @Override
//    protected void registerData() {
//
//    }

    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
        if (entity != null){
            entity.attackEntityFrom( shooter == null ? DamageSource.CACTUS:DamageSource.causeThrownDamage(this, shooter), 0.0f);
        }
        if (world.isRemote){
            for (int i = 0; i < 100; i++){
                world.addParticle(ParticleTypes.PORTAL, entity.getPosX(), entity.getPosY(), entity.getPosZ(), rand.nextGaussian() * 3,
                        rand.nextGaussian() * 3, rand.nextGaussian() * 3);
            }
        }
        if (!world.isRemote){
            GapingVoidEntity voidEntity;
            if (shooter != null){
                voidEntity = new GapingVoidEntity(EntityRegistry.GAPING_VOID.get(), shooter ,world);
            }else voidEntity = new GapingVoidEntity(EntityRegistry.GAPING_VOID.get(), world); //生成黑洞实体
            Direction facing = entity.getHorizontalFacing();
            Vector3d offset = Vector3d.ZERO;
            if (facing != null){
                offset = new Vector3d(facing.getXOffset(), facing.getYOffset(), facing.getZOffset());
            }
            voidEntity.setLocationAndAngles(entity.getPosX() + offset.x * 0.25, entity.getPosY() + offset.y * 0.25, entity.getPosZ() + offset.z * 0.25, rotationYaw, 0.0F);
            world.addEntity(voidEntity);

            this.setDead();
        }
    }

    @Override
    protected void func_230299_a_(BlockRayTraceResult result) {
        BlockPos pos = result.getPos();
        for (int i = 0; i < 100; i++){
            world.addParticle(ParticleTypes.PORTAL, pos.getX(), pos.getY(), pos.getZ(), rand.nextGaussian() * 3,
                    rand.nextGaussian() * 3, rand.nextGaussian() * 3);
        }
        if (!world.isRemote){
            GapingVoidEntity voidEntity;
            if (shooter != null){
                voidEntity = new GapingVoidEntity(EntityRegistry.GAPING_VOID.get(), shooter ,world);
            }else voidEntity = new GapingVoidEntity(EntityRegistry.GAPING_VOID.get(), world);
            Direction facing = result.getFace();
            Vector3d offset = Vector3d.ZERO;
            if (facing != null){
                offset = new Vector3d(facing.getXOffset(), facing.getYOffset(), facing.getZOffset());
            }
            voidEntity.setLocationAndAngles(pos.getX() + offset.x * 0.25, pos.getY() + offset.y * 0.25, pos.getZ() + offset.z * 0.25, rotationYaw, 0.0F);
            world.addEntity(voidEntity);

            this.setDead();
        }
    }

}
