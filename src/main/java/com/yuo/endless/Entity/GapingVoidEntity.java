package com.yuo.endless.Entity;

import com.google.common.base.Predicate;
import com.mojang.authlib.GameProfile;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.Tool.EndlessItemEntity;
import com.yuo.endless.Items.Tool.InfinityDamageSource;
import com.yuo.endless.Sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;
import java.util.UUID;

//黑洞实体
public class GapingVoidEntity extends Entity {
    private final GameProfile FAKE = new GameProfile(UUID.fromString("32283731-bbef-487c-bb69-c7e32f84ed27"), "[Endless]");

    public static final DataParameter<Integer> AGE_PARAMETER = EntityDataManager.createKey(GapingVoidEntity.class, DataSerializers.VARINT);
    public static final int maxLifetime = 186; //存在时间
    public static double collapse = 0.95; //坍塌系数 膨胀速度
    public static double suckRange = Config.SERVER.endestPearlSuckRange.get(); //引力范围
    private static final int endDamage = Config.SERVER.endestPearlEndDamage.get(); //最终爆炸伤害
    private static final int oneDamage = Config.SERVER.endestPearlOneDamage.get(); //单次吸引伤害
    private FakePlayer fakePlayer; //模拟玩家
    private LivingEntity useEntity;
    public GapingVoidEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.isImmuneToFire();
        ignoreFrustumCheck = true;
        if (world instanceof ServerWorld) {
            fakePlayer = FakePlayerFactory.get((ServerWorld) world, FAKE);
        }
    }
    public GapingVoidEntity(EntityType<?> entityTypeIn,LivingEntity living ,World worldIn) {
        super(entityTypeIn, worldIn);
        this.isImmuneToFire();
        ignoreFrustumCheck = true;
        if (world instanceof ServerWorld) {
            fakePlayer = FakePlayerFactory.get((ServerWorld) world, FAKE);
        }
        this.useEntity = living;
    }

    //玩家是未飞行的创造模式
    public static final Predicate<Entity> SUCK_PREDICATE = input -> {
        if (input instanceof PlayerEntity) {
            PlayerEntity p = (PlayerEntity) input;
            return !p.abilities.isCreativeMode || !p.abilities.isFlying;
        }
        return true;
    };

    //是生命实体 或非创造模式玩家
    public static final Predicate<Entity> OMNOM_PREDICATE = input -> {
        if (!(input instanceof LivingEntity)) {
            return false;
        }

        if (input instanceof PlayerEntity) {
            PlayerEntity p = (PlayerEntity) input;
            return !p.abilities.isCreativeMode;
        }
        return true;
    };

    @Override
    protected void registerData() {
        dataManager.register(AGE_PARAMETER, 0); //注册实体年龄数据
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        setAge(compound.getInt("age"));
        if (world instanceof ServerWorld) {
            fakePlayer = FakePlayerFactory.get((ServerWorld) world, FAKE);
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("age", getAge());
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void setAge(int age) {
        dataManager.set(AGE_PARAMETER, age);
    }

    public int getAge() {
        return dataManager.get(AGE_PARAMETER);
    }

    @Override
    public void tick() {
        double posX = this.getPosX();
        double posY = this.getPosY();
        double posZ = this.getPosZ();
        BlockPos position = this.getPosition();
        int age = getAge();

        if (age >= maxLifetime && !world.isRemote) { //死亡时发生爆炸
            world.createExplosion(this, posX, posY, posZ, 6.0f, true, Explosion.Mode.BREAK);
            double range = 4;
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(position.add(-range, -range, -range), position.add(range,range,range));
            List<Entity> nommed = world.getEntitiesWithinAABB(LivingEntity.class, axisAlignedBB, OMNOM_PREDICATE);
            //最后给予生物高额伤害
            for (Entity nommee : nommed) {
                if (nommee != this) {
                    if (nommee instanceof EnderDragonEntity){
                        EnderDragonEntity dragon = (EnderDragonEntity) nommee;
                        dragon.attackEntityPartFrom(dragon.dragonPartHead, DamageSource.causeExplosionDamage(useEntity), endDamage);
                    }else if (nommee instanceof WitherEntity){
                        WitherEntity wither = (WitherEntity) nommee;
                        wither.setInvulTime(0);
                        wither.attackEntityFrom(DamageSource.causeExplosionDamage(useEntity), endDamage);
                    } else nommee.attackEntityFrom(DamageSource.causeExplosionDamage(useEntity), endDamage);
                }
            }
            setDead();
        } else {
            if (age == 0) { //生成实体时播放音效
                world.playSound( null, position, ModSounds.GAPING_VOID.get(), SoundCategory.HOSTILE, 8.0F, 1.0F);
            }
            setAge(age + 1); //年龄增加
        }

        if (age < maxLifetime - 20 && age % 5 == 0){
            //生成粒子
            for (int i = 0; i < 50; i++){
                world.addParticle(ParticleTypes.PORTAL, position.getX(), position.getY(), position.getZ(), rand.nextGaussian() * 3,
                        rand.nextGaussian() * 3, rand.nextGaussian() * 3);
            }
        }

        if (world.isRemote) {
            return;
        }
        if (fakePlayer == null) {
            setDead();
            return;
        }

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(position.add(-suckRange, -suckRange, -suckRange), position.add(suckRange,suckRange,suckRange));
        List<Entity> sucked = world.getEntitiesWithinAABB(Entity.class, axisAlignedBB, SUCK_PREDICATE); //获取引力范围内所以实体

        double radius = getVoidScale(age) * 0.5; //引力系数
        for (Entity suckee : sucked) { //将所以实体吸引到此实体处
            if (suckee != this) {
                double dist = getDist(suckee.getPosition(), position); //距离
                if (dist <= suckRange)
                    setEntityMotionFromVector(suckee, position, radius * 0.075d);
            }
        }

        double nomRange = radius * 0.95;
        if (age % 5 == 0){ //每5tick攻击一次 一秒4次
            AxisAlignedBB alignedBB = new AxisAlignedBB(position.add(-nomRange, -nomRange, -nomRange), position.add(nomRange,nomRange,nomRange));
            List<LivingEntity> livings = world.getEntitiesWithinAABB(LivingEntity.class, alignedBB, OMNOM_PREDICATE);
            //给所以被吸引到近处的实体掉出世界伤害
            for (LivingEntity living : livings) {
                double len = getDist(living.getPosition(), position); //与黑洞中心距离
                if (len <= nomRange) {
                    if (living instanceof EnderDragonEntity){
                        EnderDragonEntity dragon = (EnderDragonEntity) living;
                        dragon.attackEntityPartFrom(dragon.dragonPartHead, DamageSource.OUT_OF_WORLD, oneDamage);
                    }
                    else living.attackEntityFrom(DamageSource.OUT_OF_WORLD, oneDamage);
                }
            }
        }

        // 每半秒破坏一次方块
        if (age % 10 == 0) {
            int blockRange = Math.round(getVoidScale(age)); //破坏半径
            Iterable<BlockPos> boxMutable = BlockPos.getAllInBoxMutable(position.add(-blockRange, -blockRange, -blockRange), position.add(blockRange, blockRange, blockRange));
            for (BlockPos pos : boxMutable) {
                if (pos.getY() < 0 || pos.getY() > 255) { //0层以下或255以上，不破坏
                    continue;
                }
                double dist = getDist(pos, position);
                if (dist <= blockRange && !world.isAirBlock(pos)) {
                    BlockState state = world.getBlockState(pos);
//                    if (state.matchesBlock(Blocks.BEDROCK) && !Config.SERVER.endestPearBreakBedrock.get()) continue;
                    BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, fakePlayer);
                    MinecraftForge.EVENT_BUS.post(event);
                    if (!event.isCanceled()) {
                        float hardness = state.getBlockHardness(world, pos);
                        if (hardness <= 50.0 && hardness > 0) { //破坏硬度低于10点的方块
                            world.destroyBlock(pos, false,useEntity );
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
//        if (entity instanceof PlayerEntity){
//            PlayerEntity player = (PlayerEntity) entity;
//            if (player.isCreative() || EventHandler.isInfinite(player) || player.abilities.isFlying) return; //创造或全套无尽 不会被吸引
//            BlockPos position = player.getPosition();
//            int x = pos.getX() - position.getX();
//            int y = pos.getY() - position.getY();
//            int z = pos.getZ() - position.getZ();
//            Vector3d vector3d = new Vector3d(x, y, z).normalize();
//            player.setMotion(vector3d);
//        }
        Vector3d originalPosVector = new Vector3d(pos.getX(), pos.getY(), pos.getZ());
        Vector3d finalVector = originalPosVector.subtract(entity.getPositionVec());
        if (finalVector.length() > 1) { //向量长度超过1
            finalVector.normalize(); //化为标准1单位
        }
        double motionX = finalVector.x * modifier;
        double motionY = finalVector.y * modifier;
        double motionZ = finalVector.z * modifier;
        entity.setMotion(motionX, motionY, motionZ);
    }

    /**
     * 获取两个坐标间的距离
     * @param pos 坐标1
     * @param blockPos 坐标2
     * @return 距离
     */
    public static double getDist(BlockPos pos, BlockPos blockPos){
        return Math.sqrt(pos.distanceSq(blockPos.getX(), blockPos.getY(), blockPos.getZ(), true));
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
    public boolean isInRangeToRenderDist(double distance) { //在范围内才渲染
        return true; //一直渲染
    }
}
