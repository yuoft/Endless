package com.yuo.endless.items.tool;

import com.yuo.endless.compat.curios.CuriosCapProvider;
import com.yuo.endless.EndlessUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class InfinityTotem extends Item {
    public InfinityTotem() {
        super(new Properties().stacksTo(1).durability(10).fireResistant());
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (EndlessUtils.isCurios){
            return new CuriosCapProvider(stack);
        }
        return super.initCapabilities(stack, nbt);
    }
}
