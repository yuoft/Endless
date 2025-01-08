package com.yuo.endless.Entity;

import com.yuo.endless.Items.EndlessItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;

//终末珍珠投掷物实体
public class EndestPearlEntity extends ThrowableItemProjectile {
    protected EndestPearlEntity(EntityType<? extends ThrowableItemProjectile> type, Level worldIn) {
        super(type, worldIn);
    }

    protected EndestPearlEntity(EntityType<? extends ThrowableItemProjectile> type, double x, double y, double z, Level worldIn) {
        super(type, x, y, z, worldIn);
    }

    private LivingEntity shooter;
    public EndestPearlEntity(EntityType<? extends ThrowableItemProjectile> type, LivingEntity livingEntityIn, Level worldIn) {
        super(type, livingEntityIn, worldIn);
        this.shooter = livingEntityIn;
    }

    @Override
    protected Item getDefaultItem() {
        return EndlessItems.endestPearl.get().asItem();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        entity.hurt( shooter == null ? DamageSource.CACTUS : DamageSource.thrown(this, shooter), 1.0f);
        if (level.isClientSide){
            for (int i = 0; i < 100; i++){
                level.addParticle(ParticleTypes.PORTAL, entity.getX(), entity.getY(), entity.getZ(), random.nextGaussian() * 3,
                        random.nextGaussian() * 3, random.nextGaussian() * 3);
            }
        }
        if (!level.isClientSide){
            GapingVoidEntity voidEntity;
            if (shooter != null){
                voidEntity = new GapingVoidEntity(EntityRegistry.GAPING_VOID.get(), shooter ,level);
            }else voidEntity = new GapingVoidEntity(EntityRegistry.GAPING_VOID.get(), level); //生成黑洞实体
            BlockPos offset = entity.getOnPos();
            voidEntity.setPos(offset.getX(), offset.getY(), offset.getZ());
            level.addFreshEntity(voidEntity);

            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        BlockPos pos = result.getBlockPos();
        for (int i = 0; i < 100; i++){
            level.addParticle(ParticleTypes.PORTAL, pos.getX(), pos.getY(), pos.getZ(), random.nextGaussian() * 3,
                    random.nextGaussian() * 3, random.nextGaussian() * 3);
        }
        if (!level.isClientSide){
            GapingVoidEntity voidEntity;
            if (shooter != null){
                voidEntity = new GapingVoidEntity(EntityRegistry.GAPING_VOID.get(), shooter ,level);
            }else voidEntity = new GapingVoidEntity(EntityRegistry.GAPING_VOID.get(), level);
            Direction facing = result.getDirection();
            BlockPos blockPos = pos.relative(facing);
            voidEntity.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            level.addFreshEntity(voidEntity);

            this.discard();
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }
}
