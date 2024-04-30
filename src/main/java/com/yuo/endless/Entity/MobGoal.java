package com.yuo.endless.Entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MobGoal extends Goal {
    private final ZombieEntity zombie;
    public MobGoal(ZombieEntity entity) {
        zombie = entity;
    }

    @Override
    public boolean shouldExecute() {
        World world = zombie.world;
        if (!world.isRemote && world.getDayTime() % 5 == 0){
            BlockPos position = zombie.getPosition();
            BlockState state = world.getBlockState(position.up());
            return state.isSolid();
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        World world = zombie.world;
        if (!world.isRemote && world.getDayTime() % 5 == 0){
            BlockPos pos = zombie.getPosition();
            BlockPos up = pos.up();
            BlockState state = world.getBlockState(up);
            if (state.isSolid()){
                for (int x = up.getX() - 1; x <= up.getX() + 1; x ++){
                    for (int y = up.getY() - 1; y <= up.getY() + 1; y ++){
                        for (int z = up.getZ() - 1; z <= up.getZ() + 1; z ++){
                            world.destroyBlock(new BlockPos(x,y,z), false);
                        }
                    }
                }
            }
        }
    }
}
