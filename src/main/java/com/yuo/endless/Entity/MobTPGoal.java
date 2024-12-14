package com.yuo.endless.Entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;

public class MobTPGoal extends Goal {
    private final Zombie zombie;
    private LivingEntity target;
    public MobTPGoal(Zombie entity) {
        zombie = entity;
        target = entity.getTarget();
    }

    @Override
    public boolean canUse() {
        Level world = zombie.level;
        target = zombie.getTarget();
        return !world.isClientSide && world.getDayTime() % 200 == 0 && target != null;
    }

    @Override
    public void tick() {
        super.tick();
        Level world = zombie.level;
        if (!world.isClientSide && world.getDayTime() % 200 == 0){
            BlockPos blockPos = target.getOnPos();
            Difficulty difficulty = world.getDifficulty();
            if (world.random.nextFloat() < 0.075f + (difficulty == Difficulty.HARD ? 0.075f : 0.05f)){
                BlockPos pos1 = new BlockPos(blockPos.getX() + world.random.nextFloat(0.1f, 0.3f), blockPos.getY() + 0.25f, blockPos.getZ()
                        + world.random.nextFloat(0.1f, 0.3f));
                zombie.setPos(pos1.getX(), pos1.getY(), pos1.getZ());
                for (int i = 0; i < 50; i++){
                    world.addParticle(ParticleTypes.PORTAL, pos1.getX(), pos1.getY(), pos1.getZ(), 0, 0.001f, 0);
                }
            }
        }
    }
}
