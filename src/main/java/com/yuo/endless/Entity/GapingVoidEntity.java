package com.yuo.endless.Entity;

import com.google.common.base.Predicate;
import com.mojang.authlib.GameProfile;
import com.yuo.endless.Items.Tool.EndlessItemEntity;
import com.yuo.endless.Items.Tool.InfinityDamageSource;
import com.yuo.endless.Sound.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
    private GameProfile FAKE = new GameProfile(UUID.fromString("32283731-bbef-487c-bb69-c7e32f84ed27"), "[Endless]");

    public static final DataParameter<Integer> AGE_PARAMETER = EntityDataManager.createKey(GapingVoidEntity.class, DataSerializers.VARINT);
    public static final int maxLifetime = 186; //存在时间
    public static double collapse = 0.95; //坍塌系数
    public static double suckRange = 20.0; //引力范围
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
            if (p.abilities.isCreativeMode && p.abilities.isFlying) {
                return false;
            }
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
            if (p.abilities.isCreativeMode) {
                return false;
            }
        } else if (input instanceof EndlessItemEntity) {
            return false;
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
                        dragon.attackEntityPartFrom(dragon.dragonPartHead, new InfinityDamageSource(useEntity), 1000.0f);
                    }else if (nommee instanceof WitherEntity){
                        WitherEntity wither = (WitherEntity) nommee;
                        wither.setInvulTime(0);
                        wither.attackEntityFrom(new InfinityDamageSource(useEntity), 1000.0f);
                    }
                    else nommee.attackEntityFrom(new InfinityDamageSource(useEntity), 1000.0f);
                }
            }
            setDead();
        } else {
            if (age == 0) { //生成实体时播放音效
                world.playSound( null, position, ModSounds.GAPING_VOID.get(), SoundCategory.HOSTILE, 8.0F, 1.0F);
            }
            setAge(age + 1); //年龄增加
        }

        //生成粒子
        for (int i = 0; i < 50; i++){
            world.addParticle(ParticleTypes.PORTAL, position.getX(), position.getY(), position.getZ(), rand.nextGaussian() * 3,
                    rand.nextGaussian() * 3, rand.nextGaussian() * 3);
        }

        if (world.isRemote) {
            return;
        }
        if (fakePlayer == null) {
            setDead();
            return;
        }

        //引力
        double radius = getVoidScale(age) * 0.5;
        double range = radius * suckRange;
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(position.add(-range, -range, -range), position.add(range,range,range));
        List<Entity> sucked = world.getEntitiesWithinAABB(Entity.class, axisAlignedBB, SUCK_PREDICATE); //获取引力范围内所以实体
        for (Entity suckee : sucked) { //将所以实体吸引到此实体处
            if (suckee != this) {
                double dx = posX - suckee.getPosX();
                double dy = posY - suckee.getPosY();
                double dz = posZ - suckee.getPosZ();

                double lensquared = dx * dx + dy * dy + dz * dz;
                double len = Math.sqrt(lensquared); //将被吸引实体距此实体距离
                double lenn = len / suckRange;

                if (len <= suckRange) {
                    double strength = (1 - lenn) * (1 - lenn);
                    double power = 0.075 * radius; //引力大小

                    Vector3d motion = suckee.getMotion();
                    double motionX = motion.x + (dx / len) * strength * power;
                    double motionY = motion.y + (dy / len) * strength * power;
                    double motionZ = motion.z + (dz / len) * strength * power;
                    suckee.setMotion(motionX, motionY, motionZ); //移动实体
                }
            }
        }

        //伤害
        double nomrange = radius * 0.95;
        AxisAlignedBB alignedBB = new AxisAlignedBB(position.add(-nomrange, -nomrange, -nomrange), position.add(nomrange,nomrange,nomrange));
        List<Entity> nommed = world.getEntitiesWithinAABB(LivingEntity.class, alignedBB, OMNOM_PREDICATE);
        //给所以被吸引到近处的实体掉出世界伤害
        for (Entity nommee : nommed) {
            if (nommee != this) {
                Vector3d nomedPos = nommee.getLookVec();
                Vector3d diff = this.getPositionVec().subtract(nomedPos);
                double len = diff.length();
                if (len <= nomrange) {
                    if (nommee instanceof EnderDragonEntity){
                        EnderDragonEntity dragon = (EnderDragonEntity) nommee;
                        dragon.attackEntityPartFrom(dragon.dragonPartHead, DamageSource.OUT_OF_WORLD, 5.0f);
                    }
                    else nommee.attackEntityFrom(DamageSource.OUT_OF_WORLD, 5.0f);
                }
            }
        }

        // 每半秒破坏一次方块
        if (age % 10 == 0) {
            Vector3d posFloor = this.getPositionVec();

            int blockrange = (int) Math.round(nomrange);

            for (int y = -blockrange; y <= blockrange; y++) {
                for (int z = -blockrange; z <= blockrange; z++) {
                    for (int x = -blockrange; x <= blockrange; x++) {
                        Vector3d pos2 = new Vector3d(x, y, z);
                        Vector3d rPos = posFloor.add(pos2);
                        BlockPos blockPos = new BlockPos(rPos);

                        if (blockPos.getY() < 0 || blockPos.getY() > 255) {
                            continue;
                        }

                        double dist = pos2.lengthSquared();
                        if (dist <= nomrange && !world.isAirBlock(blockPos)) {
                            BlockState state = world.getBlockState(blockPos);
                            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, blockPos, state, fakePlayer);
                            MinecraftForge.EVENT_BUS.post(event);
                            if (!event.isCanceled()) {
                                float resist = state.getBlock().getExplosionResistance();
                                if (resist <= 10.0) { //方块爆炸抗性小于10点
//                                    state.getBlock().dropBlockAsItemWithChance(world, blockPos, state, 0.9F, 0);
                                    state.getBlock().canDropFromExplosion(state, world, blockPos, new Explosion(world, null,blockPos.getX(),
                                                                                blockPos.getY(), blockPos.getZ(), 6.0f, false, Explosion.Mode.BREAK));
                                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //通过年龄获取缩放大小
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
