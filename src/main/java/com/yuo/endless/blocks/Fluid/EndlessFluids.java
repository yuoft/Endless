package com.yuo.endless.blocks.Fluid;

import com.yuo.endless.blocks.EndlessBlocks;
import com.yuo.endless.Endless;
import com.yuo.endless.items.EndlessItems;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EndlessFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Endless.MOD_ID);

    public static RegistryObject<FlowingFluid> infinityFluid = FLUIDS.register("infinity_fluid", () -> new InfinityFluid.Source(EndlessFluids.INFINITY_PRO));
    public static RegistryObject<FlowingFluid> infinityFluidFlowing = FLUIDS.register("infinity_fluid_flowing", () -> new InfinityFluid.Flowing(EndlessFluids.INFINITY_PRO));

    public static ForgeFlowingFluid.Properties INFINITY_PRO = new ForgeFlowingFluid.Properties(EndlessFluidTypes.INFINITY_FLUID_TYPE, infinityFluid, infinityFluidFlowing)
            //流体桶,流体方块,流体消失速度，防爆性
            .bucket(EndlessItems.infinityFluidBucket).block(EndlessBlocks.infinityFluid).slopeFindDistance(1).tickRate(40).explosionResistance(Float.MAX_VALUE);
}
