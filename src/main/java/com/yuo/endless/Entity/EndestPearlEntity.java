package com.yuo.endless.Entity;

import com.yuo.endless.Items.EndlessItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

//终末珍珠投掷物实体
public class EndestPearlEntity extends Projectile {
    protected EndestPearlEntity(EntityType<? extends Projectile> type, Level worldIn) {
        super(type, worldIn);
    }

    protected EndestPearlEntity(EntityType<? extends Projectile> type, double x, double y, double z, Level worldIn) {
        super(type, x, y, z, worldIn);
    }

    private LivingEntity shooter;
    public EndestPearlEntity(EntityType<? extends Projectile> type, LivingEntity livingEntityIn, Level worldIn) {
        super(type, livingEntityIn, worldIn);
        this.shooter = livingEntityIn;
    }

    @Override
    protected Item getDefaultItem() {
        return EndlessItems.endestPearl.get().asItem();
    }

    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
        entity.attackEntityFrom( shooter == null ? DamageSource.CACTUS:DamageSource.causeThrownDamage(this, shooter), 1.0f);
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
            BlockPos offset = entity.getPosition().offset(facing);
            voidEntity.setPositionAndUpdate(offset.getX(), offset.getY(), offset.getZ());
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
            BlockPos blockPos = pos.offset(facing);
            voidEntity.setPositionAndUpdate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            world.addEntity(voidEntity);

            this.setDead();
        }
    }

    @Override
    protected void defineSynchedData() {

    }
}
