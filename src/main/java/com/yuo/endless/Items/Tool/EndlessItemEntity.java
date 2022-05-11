package com.yuo.endless.Items.Tool;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EndlessItemEntity extends ItemEntity {
    public EndlessItemEntity(EntityType<? extends ItemEntity> p_i50217_1_, World world) {
        super(p_i50217_1_, world);
        this.setPickupDelay(20);
    }

    public EndlessItemEntity(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        this.setPickupDelay(20);
    }

    public EndlessItemEntity(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z, stack);
        this.setPickupDelay(20);
    }
}
