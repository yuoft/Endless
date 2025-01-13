package com.yuo.endless.Entity;

import com.yuo.endless.Items.Tool.InfinityDamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;

//箭实体
public class InfinitySuperArrowEntity extends AbstractArrow {
    private LivingEntity shooter; //使用玩家
    private int range;
    public InfinitySuperArrowEntity(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
    }

    public InfinitySuperArrowEntity(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level worldIn) {
        super(type, x, y, z, worldIn);
    }

    public InfinitySuperArrowEntity(EntityType<? extends AbstractArrow> type, LivingEntity shooter, Level worldIn, int range) {
        super(type, shooter, worldIn);
        this.shooter = shooter;
        this.range = range;
    }

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
        return 1.0f;
    }

    //无下坠
    @Override
    public boolean isNoPhysics() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        boolean flag = this.isNoPhysics();
        Vec3 vec3 = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 57.2957763671875));
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * 57.2957763671875));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level.getBlockState(blockpos);
        Vec3 vec33;
        if (!blockstate.isAir() && !flag) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level, blockpos);
            if (!voxelshape.isEmpty()) {
                vec33 = this.position();

                for (AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(vec33)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }

        if (this.shakeTime > 0) {
            --this.shakeTime;
        }

        if (this.isInWaterOrRain() || blockstate.is(Blocks.POWDER_SNOW)) {
            this.clearFire();
        }

        if (this.inGround && !flag) {
            if (this.lastState != blockstate && this.shouldFall()) {
                this.startFalling();
            } else if (!this.level.isClientSide) {
                this.tickDespawn();
            }

            ++this.inGroundTime;
        } else
        {
            this.inGroundTime = 0;
            Vec3 vec32 = this.position();
            vec33 = vec32.add(vec3);
            HitResult hitresult = this.level.clip(new ClipContext(vec32, vec33, Block.COLLIDER, Fluid.NONE, this));
            if (((HitResult)hitresult).getType() != Type.MISS) {
                vec33 = ((HitResult)hitresult).getLocation();
            }

            while(!this.isRemoved()) {
                EntityHitResult entityhitresult = this.findHitEntity(vec32, vec33);
                if (entityhitresult != null) {
                    hitresult = entityhitresult;
                }

                if (hitresult != null && ((HitResult)hitresult).getType() == Type.ENTITY) {
                    Entity entity = ((EntityHitResult)hitresult).getEntity();
                    Entity entity1 = this.getOwner();
                    if (entity instanceof Player && entity1 instanceof Player && !((Player)entity1).canHarmPlayer((Player)entity)) {
                        hitresult = null;
                        entityhitresult = null;
                    }
                }

                if (hitresult != null && ((HitResult)hitresult).getType() != Type.MISS && !flag && !ForgeEventFactory.onProjectileImpact(this, (HitResult)hitresult)) {
                    this.onHit((HitResult)hitresult);
                    this.hasImpulse = true;
                }

                if (entityhitresult == null || this.getPierceLevel() <= 0) {
                    break;
                }

                hitresult = null;
            }

            vec3 = this.getDeltaMovement();
            double d5 = vec3.x;
            double d6 = vec3.y;
            double d1 = vec3.z;
            if (this.isCritArrow()) {
                for(int i = 0; i < 4; ++i) {
                    this.level.addParticle(ParticleTypes.CRIT, this.getX() + d5 * (double)i / 4.0, this.getY() + d6 * (double)i / 4.0, this.getZ() + d1 * (double)i / 4.0, -d5, -d6 + 0.2, -d1);
                }
            }

            double d7 = this.getX() + d5;
            double d2 = this.getY() + d6;
            double d3 = this.getZ() + d1;
            double d4 = vec3.horizontalDistance();
            if (flag) {
                this.setYRot((float)(Mth.atan2(-d5, -d1) * 57.2957763671875));
            } else {
                this.setYRot((float)(Mth.atan2(d5, d1) * 57.2957763671875));
            }

            this.setXRot((float)(Mth.atan2(d6, d4) * 57.2957763671875));
            this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
            this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
            float f = 0.99F;
            float f1 = 0.05F;
            if (this.isInWater()) {
                for(int j = 0; j < 4; ++j) {
                    float f2 = 0.25F;
                    this.level.addParticle(ParticleTypes.BUBBLE, d7 - d5 * 0.25, d2 - d6 * 0.25, d3 - d1 * 0.25, d5, d6, d1);
                }

                f = this.getWaterInertia();
            }

            this.setDeltaMovement(vec3.scale((double)f));
            if (!this.isNoGravity() && !flag) {
                Vec3 vec34 = this.getDeltaMovement();
                this.setDeltaMovement(vec34.x, vec34.y - 0.05000000074505806, vec34.z);
            }

            this.setPos(d7, d2, d3);
            this.checkInsideBlocks();
        }

        if (tickCount > 200) discard(); //10秒后死亡

        if(level.isClientSide) return;
        if (level.getGameTime() % 2 != 0) return;
        BlockPos pos = this.blockPosition();

        if (level.getGameTime() % 10 == 0) {
            int length = 8;
            float explosionLv = 5.0f;
            BlockPos offset = pos.relative(Direction.EAST, length);
            level.explode(this.shooter, offset.getX(), offset.getY(), offset.getZ(), explosionLv, true, BlockInteraction.DESTROY);
            BlockPos offsetW = pos.relative(Direction.WEST, length);
            level.explode(this.shooter, offsetW.getX(), offsetW.getY(), offsetW.getZ(), explosionLv, true, BlockInteraction.DESTROY);
            BlockPos offsetS = pos.relative(Direction.SOUTH, length);
            level.explode(this.shooter, offsetS.getX(), offsetS.getY(), offsetS.getZ(), explosionLv, true, BlockInteraction.DESTROY);
            BlockPos offsetN = pos.relative(Direction.NORTH, length);
            level.explode(this.shooter, offsetN.getX(), offsetN.getY(), offsetN.getZ(), explosionLv, true, BlockInteraction.DESTROY);
            BlockPos offsetU = pos.relative(Direction.UP, length);
            level.explode(this.shooter, offsetU.getX(), offsetU.getY(), offsetU.getZ(), explosionLv, true, BlockInteraction.DESTROY);
            BlockPos offsetD = pos.relative(Direction.DOWN, length);
            level.explode(this.shooter, offsetD.getX(), offsetD.getY(), offsetD.getZ(), explosionLv, true, BlockInteraction.DESTROY);
        }

        int range = 8 + getRange(this.range); //破坏范围
        int range0 = range + 1; //检索范围
        AABB alignedBB = new AABB(pos.offset(-range0, -range0, -range0), pos.offset(range0, range0, range0));

        //破坏方块
        Iterable<BlockPos> allInBoxMutable = BlockPos.betweenClosed(pos.offset(-range0, -range0, -range0), pos.offset(range0, range0, range0));
        for (BlockPos blockPos : allInBoxMutable) {
            if (level.getBlockState(blockPos).isAir()) continue;
            double distanceSq = Math.sqrt(pos.distToCenterSqr(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            if (distanceSq < range) {
                BlockState state = level.getBlockState(blockPos);
                if (state.getMaterial().isLiquid()) //流体
                    level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                else{
                    if (level.random.nextDouble() > 0.5d)
                        level.destroyBlock(blockPos, false);
                    else level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                }

            }
            boolean b = distanceSq >= range / 2.0d + 0.5d && distanceSq < range / 2.0d - 0.5d;
            if (b) {
                level.explode(this.shooter, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 5.0f, true, BlockInteraction.BREAK);
            }

        }

        //攻击实体
        List<LivingEntity> entityList = level.getEntitiesOfClass(LivingEntity.class, alignedBB);
        for (LivingEntity living : entityList) {
            if (living.isAlive() && living != this.getOwner()){
                BlockPos position = living.blockPosition();
                double dis = Math.sqrt(pos.distToCenterSqr(position.getX(), position.getY(), position.getZ()));
                if (dis <= range) {
                    living.hurt(new InfinityDamageSource((LivingEntity) this.getOwner()), Float.MAX_VALUE);
                    living.setHealth(-1);
                    living.kill();
                }
            }
        }

    }

    public int getRange(int i){
        return (int) Math.floor(i / 25.0d);
    }
}
