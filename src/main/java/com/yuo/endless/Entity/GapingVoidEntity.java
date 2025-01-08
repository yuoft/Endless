package com.yuo.endless.Entity;

import com.mojang.authlib.GameProfile;
import com.yuo.endless.Client.Sound.ModSounds;
import com.yuo.endless.Config;
import com.yuo.endless.Event.EventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

//黑洞实体
public class GapingVoidEntity extends Entity {
    private final GameProfile FAKE = new GameProfile(UUID.fromString("32283731-bbef-487c-bb69-c7e32f84ed27"), "[Endless]");

    public static final EntityDataAccessor<Integer> AGE_PARAMETER = SynchedEntityData.defineId(GapingVoidEntity.class, EntityDataSerializers.INT);
    public static final int maxLifetime = 186; //存在时间
    public static double collapse = 0.95; //坍塌系数 膨胀速度
    public static double suckRange = Config.SERVER.endestPearlSuckRange.get(); //引力范围
    private static final int endDamage = Config.SERVER.endestPearlEndDamage.get(); //最终爆炸伤害
    private static final int oneDamage = Config.SERVER.endestPearlOneDamage.get(); //单次吸引伤害
    private FakePlayer fakePlayer; //模拟玩家
    private LivingEntity useEntity;
    public GapingVoidEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.fireImmune();
//        ignoreFrustumCheck = true;
        noCulling = true;
        if (worldIn instanceof ServerLevel) {
            fakePlayer = FakePlayerFactory.get((ServerLevel) worldIn, FAKE);
        }
    }
    public GapingVoidEntity(EntityType<?> entityTypeIn,LivingEntity living ,Level worldIn) {
        super(entityTypeIn, worldIn);
        this.fireImmune();
//        ignoreFrustumCheck = true;
        noCulling = true;
        if (worldIn instanceof ServerLevel) {
            fakePlayer = FakePlayerFactory.get((ServerLevel) worldIn, FAKE);
        }
        this.useEntity = living;
    }

    //玩家是未飞行的创造模式
    public static final Predicate<Entity> SUCK_PREDICATE = input -> {
        if (input instanceof Player p) {
            return !p.isCreative() || !p.getAbilities().flying;
        }
        return true;
    };

    //是生命实体 或非创造模式玩家
    public static final Predicate<Entity> OMNOM_PREDICATE = input -> {
        if (!(input instanceof LivingEntity)) {
            return false;
        }

        if (input instanceof Player p) {
            return !p.isCreative();
        }
        return true;
    };

    protected void defineSynchedData() {
        this.entityData.define(AGE_PARAMETER, 0);//注册实体年龄数据
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        setAge(compound.getInt("age"));
        if (level instanceof ServerLevel) {
            fakePlayer = FakePlayerFactory.get((ServerLevel) level, FAKE);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("age", getAge());
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void setAge(int age) {
        entityData.set(AGE_PARAMETER, age);
    }

    public int getAge() {
        return entityData.get(AGE_PARAMETER);
    }

    @Override
    public void tick() {
        double posX = this.getX();
        double posY = this.getY();
        double posZ = this.getZ();
        BlockPos position = this.getOnPos();
        int age = getAge();

        if (age >= maxLifetime && !level.isClientSide) { //死亡时发生爆炸
            level.explode(this, posX, posY, posZ, 6.0f, true, BlockInteraction.BREAK);
            double range = 4;
            AABB axisAlignedBB = new AABB(position.offset(-range, -range, -range), position.offset(range,range,range));
            List<LivingEntity> nommed = level.getEntitiesOfClass(LivingEntity.class, axisAlignedBB, OMNOM_PREDICATE);
            //最后给予生物高额伤害
            for (Entity nommee : nommed) {
                if (nommee != this) {
                    if (nommee instanceof EnderDragon dragon){
                        dragon.hurt(dragon.head, DamageSource.explosion(useEntity), endDamage);
                    }else if (nommee instanceof WitherBoss wither){
                        wither.setInvulnerableTicks(0);
                        wither.hurt(DamageSource.explosion(useEntity), endDamage);
                    } else nommee.hurt(DamageSource.explosion(useEntity), endDamage);
                }
            }
            discard();
        } else {
            if (age == 0) { //生成实体时播放音效
                level.playSound( null, position, ModSounds.GAPING_VOID.get(), SoundSource.HOSTILE, 8.0F, 1.0F);
            }
            setAge(age + 1); //年龄增加
        }

        if (age < maxLifetime - 20 && age % 5 == 0){
            //生成粒子
            for (int i = 0; i < 50; i++){
                level.addParticle(ParticleTypes.PORTAL, position.getX(), position.getY(), position.getZ(), random.nextGaussian() * 3,
                        random.nextGaussian() * 3, random.nextGaussian() * 3);
            }
        }

        if (level.isClientSide) {
            return;
        }
        if (fakePlayer == null) {
            discard();
            return;
        }

        AABB axisAlignedBB = new AABB(position.offset(-suckRange, -suckRange, -suckRange), position.offset(suckRange,suckRange,suckRange));
        List<Entity> sucked = level.getEntitiesOfClass(Entity.class, axisAlignedBB, SUCK_PREDICATE); //获取引力范围内所以实体

        double radius = getVoidScale(age) * 0.5; //引力系数
        for (Entity suckee : sucked) { //将所以实体吸引到此实体处
            if (suckee != this) {
                double dist = getDist(suckee.getOnPos(), position); //距离
                if (dist <= suckRange)
                    setEntityMotionFromVector(suckee, position, radius * 0.075d);
            }
        }

        double nomRange = radius * 0.95;
        if (age % 5 == 0){ //每5tick攻击一次 一秒4次
            AABB alignedBB = new AABB(position.offset(-nomRange, -nomRange, -nomRange), position.offset(nomRange,nomRange,nomRange));
            List<LivingEntity> livings = level.getEntitiesOfClass(LivingEntity.class, alignedBB, OMNOM_PREDICATE);
            //给所以被吸引到近处的实体掉出世界伤害
            for (LivingEntity living : livings) {
                double len = getDist(living.getOnPos(), position); //与黑洞中心距离
                if (len <= nomRange) {
                    if (living instanceof EnderDragon dragon){
                        dragon.hurt(dragon.head, DamageSource.OUT_OF_WORLD, oneDamage);
                    }
                    else living.hurt(DamageSource.OUT_OF_WORLD, oneDamage);

                    if (age % 40 == 0 && living instanceof Player player){
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 9));
                    }
                }
            }
        }

        //给予内部玩家失明buff
        if (age % 20 == 0){
            AABB alignedBB = new AABB(position.offset(-nomRange, -nomRange, -nomRange), position.offset(nomRange,nomRange,nomRange));
            List<LivingEntity> livings = level.getEntitiesOfClass(LivingEntity.class, alignedBB, OMNOM_PREDICATE);

        }

        // 每半秒破坏一次方块
        if (age % 10 == 0) {
            int blockRange = Math.round(getVoidScale(age)); //破坏半径
            Iterable<BlockPos> boxMutable = BlockPos.betweenClosed(position.offset(-blockRange, -blockRange, -blockRange), position.offset(blockRange, blockRange, blockRange));
            for (BlockPos pos : boxMutable) {
                if (pos.getY() < 0 || pos.getY() > 255) { //0层以下或255以上，不破坏
                    continue;
                }
                double dist = getDist(pos, position);
                if (dist <= blockRange && !level.getBlockState(pos).isAir()) {
                    BlockState state = level.getBlockState(pos);
                    BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state, fakePlayer);
                    MinecraftForge.EVENT_BUS.post(event);
                    if (!event.isCanceled()) {
                        float hardness = state.getDestroySpeed(level, pos);
                        if (state.getMaterial().isLiquid()){ //流体删除
                            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                            continue;
                        }
                        if (hardness <= 50.0 && hardness > 0) { //破坏硬度低于10点的方块
                            level.destroyBlock(pos, false, useEntity );
                        }
                    }
                }
            }
        }
    }

    /**
     * 设置实体移动
     * @param entity 要移动的实体
     * @param pos 目标坐标
     * @param modifier 移动距离 负数为排斥
     */
    public static void setEntityMotionFromVector(Entity entity, BlockPos pos, double modifier) {
        Vec3 originalPosVector = new Vec3(pos.getX(), pos.getY(), pos.getZ());
        Vec3 finalVector = originalPosVector.subtract(entity.position());
        if (finalVector.length() > 1) { //向量长度超过1
            finalVector.normalize(); //化为标准1单位
        }
        double motionX = finalVector.x * modifier;
        double motionY = finalVector.y * modifier;
        double motionZ = finalVector.z * modifier;
        if (entity instanceof Player player){
            if (player.isCreative() || EventHandler.isInfinite(player) || player.getAbilities().flying) return; //创造或全套无尽 不会被吸引
            Vec3 vector3d = new Vec3(motionX, motionY, motionZ).normalize();
            player.push(vector3d.x, vector3d.y, vector3d.z);
        }
        entity.setDeltaMovement(motionX, motionY, motionZ);
    }

    /**
     * 获取两个坐标间的距离
     * @param pos 坐标1
     * @param blockPos 坐标2
     * @return 距离
     */
    public static double getDist(BlockPos pos, BlockPos blockPos){
        return Math.sqrt(pos.distToCenterSqr(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
    }

    //通过年龄获取缩放大小（膨胀程度）
    public static float getVoidScale(double age) {
        double life = age / (double) maxLifetime;

        double curve;
        if (life < collapse) {
            curve = 0.005 + ease(1 - ((collapse - life) / collapse)) * 0.995;
        } else {
            curve = ease(1 - ((life - collapse) / (1 - collapse)));
        }
        return (float) (10.0 * curve);
    }

    private static double ease(double in) {
        double t = in - 1;
        return Math.sqrt(1 - t * t);
    }

    @Override
    public boolean canBeCollidedWith() { 
        return false;//不能通过此实体
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double pDistance) {//在范围内才渲染
        return true;//一直渲染
    }
}
