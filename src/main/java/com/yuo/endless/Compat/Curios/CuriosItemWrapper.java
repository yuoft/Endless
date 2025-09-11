package com.yuo.endless.Compat.Curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
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
    public ItemStack getStack() {
        return this.itemStack;
    }

    @Override
    public void curioTick(SlotContext slotContext) {
        this.curioTick(slotContext.identifier(), slotContext.index(), slotContext.entity());
    }
}
