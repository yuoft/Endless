package com.yuo.endless.Compat.Curios;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CuriosCapProvider implements ICapabilityProvider {
    private final ICurio capInstance;

    public CuriosCapProvider(ItemStack sheath) {
        this.capInstance = new CuriosItemWrapper(sheath);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        if (capability == CuriosCapability.ITEM)
            return LazyOptional.of(() -> this.capInstance).cast();
        return LazyOptional.empty();
    }
}
