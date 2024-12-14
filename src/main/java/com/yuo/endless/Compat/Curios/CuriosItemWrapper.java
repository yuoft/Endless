package com.yuo.endless.Compat.Curios;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class CuriosItemWrapper implements ICurio {
    private final ItemStack itemStack;

    public CuriosItemWrapper(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public boolean canRightClickEquip() {
        return true;
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity) {
        ICurio.super.curioTick(identifier, index, livingEntity);
    }
}
