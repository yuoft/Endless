package com.yuo.endless.Entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MobGoal extends Goal {
    private final Zombie zombie;
    public MobGoal(Zombie entity) {
        zombie = entity;
    }

    @Override
    public boolean canUse() {
        Level world = zombie.level;
        if (!world.isClientSide && world.getDayTime() % 5 == 0){
            BlockPos position = zombie.getOnPos();
            BlockState state = world.getBlockState(position.above());
            return state.getMaterial().isSolid();
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        Level world = zombie.level;
        if (!world.isClientSide && world.getDayTime() % 5 == 0){
            BlockPos pos = zombie.getOnPos();
            BlockPos up = pos.above();
            BlockState state = world.getBlockState(up);
            if (state.getMaterial().isSolid()){
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
