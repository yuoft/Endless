package com.yuo.endless.Entity;

import com.yuo.endless.Items.Tool.InfinityDamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Iterator;
import java.util.List;

//箭实体
public class InfinitySuperArrowEntity extends AbstractArrowEntity {
    private LivingEntity shooter; //使用玩家
    private int range;
    public InfinitySuperArrowEntity(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public InfinitySuperArrowEntity(EntityType<? extends AbstractArrowEntity> type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
    }

    public InfinitySuperArrowEntity(EntityType<? extends AbstractArrowEntity> type, LivingEntity shooter, World worldIn, int range) {
        super(type, shooter, worldIn);
        this.shooter = shooter;
        this.range = range;
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
        return 1.0f;
    }

    @Override
    public void tick() {
        super.tick();
        boolean flag = this.getNoClip();
        Vector3d vector3d = this.getMotion();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(horizontalMag(vector3d));
            this.rotationYaw = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * 57.2957763671875);
            this.rotationPitch = (float)(MathHelper.atan2(vector3d.y, (double)f) * 57.2957763671875);
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }

        BlockPos blockpos = this.getPosition();
        BlockState blockstate = this.world.getBlockState(blockpos);
        Vector3d vector3d3;
        if (!blockstate.isAir(this.world, blockpos) && !flag) {
            VoxelShape voxelshape = blockstate.getCollisionShapeUncached(this.world, blockpos);
            if (!voxelshape.isEmpty()) {
                vector3d3 = this.getPositionVec();
                Iterator var7 = voxelshape.toBoundingBoxList().iterator();

                while(var7.hasNext()) {
                    AxisAlignedBB axisalignedbb = (AxisAlignedBB)var7.next();
                    if (axisalignedbb.offset(blockpos).contains(vector3d3)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.isWet()) {
            this.extinguish();
        }

        if (this.inGround && !flag) {
            if (this.inBlockState != blockstate && this.func_234593_u_()) {
                this.func_234594_z_();
            } else if (!this.world.isRemote) {
                this.func_225516_i_();
            }

            ++this.timeInGround;
        }
        else {
            this.timeInGround = 0;
            Vector3d vector3d2 = this.getPositionVec();
            vector3d3 = vector3d2.add(vector3d);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(vector3d2, vector3d3, BlockMode.COLLIDER, FluidMode.NONE, this));
            if (raytraceresult.getType() != Type.MISS) {
                vector3d3 = raytraceresult.getHitVec();
            }

            while(!this.removed) {
                EntityRayTraceResult entityraytraceresult = this.rayTraceEntities(vector3d2, vector3d3);
                if (entityraytraceresult != null) {
                    raytraceresult = entityraytraceresult;
                }

                if (raytraceresult != null && raytraceresult.getType() == Type.ENTITY) {
                    Entity entity = ((EntityRayTraceResult)raytraceresult).getEntity();
                    Entity entity1 = this.getShooter();
                    if (entity instanceof PlayerEntity && entity1 instanceof PlayerEntity && !((PlayerEntity)entity1).canAttackPlayer((PlayerEntity)entity)) {
                        raytraceresult = null;
                        entityraytraceresult = null;
                    }
                }

                if (raytraceresult != null && ((RayTraceResult)raytraceresult).getType() != Type.MISS && !flag && !ForgeEventFactory.onProjectileImpact(this, (RayTraceResult)raytraceresult)) {
                    this.onImpact((RayTraceResult)raytraceresult);
                    this.isAirBorne = true;
                }

                if (entityraytraceresult == null || this.getPierceLevel() <= 0) {
                    break;
                }

                raytraceresult = null;
            }

            vector3d = this.getMotion();
            double d3 = vector3d.x;
            double d4 = vector3d.y;
            double d0 = vector3d.z;
            if (this.getIsCritical()) {
                for(int i = 0; i < 4; ++i) {
                    this.world.addParticle(ParticleTypes.CRIT, this.getPosX() + d3 * (double)i / 4.0, this.getPosY() + d4 * (double)i / 4.0, this.getPosZ() + d0 * (double)i / 4.0, -d3, -d4 + 0.2, -d0);
                }
            }

            double d5 = this.getPosX() + d3;
            double d1 = this.getPosY() + d4;
            double d2 = this.getPosZ() + d0;
            float f1 = MathHelper.sqrt(horizontalMag(vector3d));
            if (flag) {
                this.rotationYaw = (float)(MathHelper.atan2(-d3, -d0) * 57.2957763671875);
            } else {
                this.rotationYaw = (float)(MathHelper.atan2(d3, d0) * 57.2957763671875);
            }

            this.rotationPitch = (float)(MathHelper.atan2(d4, (double)f1) * 57.2957763671875);
            this.rotationPitch = func_234614_e_(this.prevRotationPitch, this.rotationPitch);
            this.rotationYaw = func_234614_e_(this.prevRotationYaw, this.rotationYaw);
            float f2 = 0.99F;
            if (this.isInWater()) {
                for(int j = 0; j < 4; ++j) {
                    this.world.addParticle(ParticleTypes.BUBBLE, d5 - d3 * 0.25, d1 - d4 * 0.25, d2 - d0 * 0.25, d3, d4, d0);
                }

                f2 = this.getWaterDrag();
            }

            this.setMotion(vector3d.scale((double)f2));
            if (!this.hasNoGravity() && !flag) {
                Vector3d vector3d4 = this.getMotion();
                this.setMotion(vector3d4.x, vector3d4.y, vector3d4.z);
            }

            this.setPosition(d5, d1, d2);
            this.doBlockCollisions();
        }
        if (ticksExisted > 200) setDead(); //10秒后死亡

        if(world.isRemote) return;
        if (world.getGameTime() % 2 != 0) return;
        BlockPos pos = this.getPosition();

        if (world.getGameTime() % 10 == 0) {
            int length = 8;
            float explosionLv = 5.0f;
            BlockPos offset = pos.offset(Direction.EAST, length);
            world.createExplosion(this.shooter, offset.getX(), offset.getY(), offset.getZ(), explosionLv, true, Mode.DESTROY);
            BlockPos offsetW = pos.offset(Direction.WEST, length);
            world.createExplosion(this.shooter, offsetW.getX(), offsetW.getY(), offsetW.getZ(), explosionLv, true, Mode.DESTROY);
            BlockPos offsetS = pos.offset(Direction.SOUTH, length);
            world.createExplosion(this.shooter, offsetS.getX(), offsetS.getY(), offsetS.getZ(), explosionLv, true, Mode.DESTROY);
            BlockPos offsetN = pos.offset(Direction.NORTH, length);
            world.createExplosion(this.shooter, offsetN.getX(), offsetN.getY(), offsetN.getZ(), explosionLv, true, Mode.DESTROY);
            BlockPos offsetU = pos.offset(Direction.UP, length);
            world.createExplosion(this.shooter, offsetU.getX(), offsetU.getY(), offsetU.getZ(), explosionLv, true, Mode.DESTROY);
            BlockPos offsetD = pos.offset(Direction.DOWN, length);
            world.createExplosion(this.shooter, offsetD.getX(), offsetD.getY(), offsetD.getZ(), explosionLv, true, Mode.DESTROY);
        }

        int range = 8 + getRange(this.range); //破坏范围
        int range0 = range + 1; //检索范围
        AxisAlignedBB alignedBB = new AxisAlignedBB(pos.add(-range0, -range0, -range0), pos.add(range0, range0, range0));

        //破坏方块
        Iterable<BlockPos> allInBoxMutable = BlockPos.getAllInBoxMutable(pos.add(-range0, -range0, -range0), pos.add(range0, range0, range0));
        for (BlockPos blockPos : allInBoxMutable) {
            if (world.isAirBlock(blockPos)) continue;
            double distanceSq = Math.sqrt(pos.distanceSq(blockPos.getX(), blockPos.getY(), blockPos.getZ(), true));
            if (distanceSq < range) {
                BlockState state = world.getBlockState(blockPos);
                if (state.getMaterial().isLiquid()) //流体
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                else{
                    if (world.rand.nextDouble() > 0.5d)
                        world.destroyBlock(blockPos, false);
                    else world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                }

            }
            boolean b = distanceSq >= range / 2.0d + 0.5d && distanceSq < range / 2.0d - 0.5d;
            if (b) {
                world.createExplosion(this.shooter, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 5.0f, true, Mode.DESTROY);
            }

        }

        //攻击实体
        List<LivingEntity> entityList = world.getEntitiesWithinAABB(LivingEntity.class, alignedBB);
        for (LivingEntity living : entityList) {
            if (living.isAlive() && living != this.getShooter()){
                BlockPos position = living.getPosition();
                double dis = Math.sqrt(pos.distanceSq(position.getX(), position.getY(), position.getZ(), true));
                if (dis <= range) {
                    living.attackEntityFrom(new InfinityDamageSource((LivingEntity) this.getShooter()), Float.POSITIVE_INFINITY);
                    living.setHealth(-1);
                    living.onKillCommand();
                }
            }
        }

    }

    public int getRange(int i){
        return (int) Math.floor(i / 25.0d);
    }
}
