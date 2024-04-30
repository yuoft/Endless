package com.yuo.endless.Entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

public class MobTPGoal extends Goal {
    private final ZombieEntity zombie;
    private LivingEntity target;
    public MobTPGoal(ZombieEntity entity) {
        zombie = entity;
        target = entity.getAttackTarget();
    }

    @Override
    public boolean shouldExecute() {
        World world = zombie.world;
        target = zombie.getAttackTarget();
        return !world.isRemote && world.getDayTime() % 200 == 0 && target != null;
    }

    @Override
    public void tick() {
        super.tick();
        World world = zombie.world;
        if (!world.isRemote && world.getDayTime() % 200 == 0){
            BlockPos blockPos = target.getPosition();
            Difficulty difficulty = world.getDifficulty();
            if (world.rand.nextFloat() < 0.075f + (difficulty == Difficulty.HARD ? 0.075f : 0.05f)){
                BlockPos pos1 = new BlockPos(blockPos.getX() + MathHelper.nextFloat(world.rand, 0.1f, 0.3f), blockPos.getY() + 0.25f, blockPos.getZ() + MathHelper.nextFloat(world.rand, 0.1f, 0.3f));
                zombie.setPositionAndUpdate(pos1.getX(), pos1.getY(), pos1.getZ());
                for (int i = 0; i < 50; i++){
                    world.addParticle(ParticleTypes.PORTAL, pos1.getX(), pos1.getY(), pos1.getZ(), 0, 0.001f, 0);
                }
            }
        }
    }
}
