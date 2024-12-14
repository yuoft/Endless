package com.yuo.endless.Items.Tool;

import com.yuo.endless.Compat.Curios.CuriosCapProvider;
import com.yuo.endless.Endless;
import com.yuo.endless.EndlessTab;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class InfinityTotem extends Item {
    public InfinityTotem() {
        super(new Properties().maxStackSize(1).maxDamage(10).group(EndlessTab.endless).isImmuneToFire());
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (Endless.isCurios){
            return new CuriosCapProvider(stack);
        }
        return super.initCapabilities(stack, nbt);
    }
}
