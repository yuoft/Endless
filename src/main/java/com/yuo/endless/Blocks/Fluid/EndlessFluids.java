package com.yuo.endless.Blocks.Fluid;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EndlessFluids {
    public static final ResourceLocation STILL_OIL_TEXTURE = new ResourceLocation(Endless.MOD_ID, "block/fluids/fluid");
    public static final ResourceLocation FLOWING_OIL_TEXTURE = new ResourceLocation(Endless.MOD_ID, "block/fluids/fluid_flow");
    public static final ResourceLocation OVERLAY_OIL_TEXTURE = new ResourceLocation(Endless.MOD_ID, "block/fluids/fluid_overlay");

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Endless.MOD_ID);

    public static RegistryObject<FlowingFluid> infinityFluid = FLUIDS.register("infinity_fluid", () -> new InfinityFluid.Source(EndlessFluids.INFINITY_PRO));
    public static RegistryObject<FlowingFluid> infinityFluidFlowing = FLUIDS.register("infinity_fluid_flowing", () -> new InfinityFluid.Flowing(EndlessFluids.INFINITY_PRO));

    public static ForgeFlowingFluid.Properties INFINITY_PRO = new ForgeFlowingFluid.Properties(infinityFluid, infinityFluidFlowing,
            //材质，颜色，亮度，流体密度,温度,稠度,稀有度
            InfinityFluid.setAttr(STILL_OIL_TEXTURE, FLOWING_OIL_TEXTURE, 0xff333333).overlay(OVERLAY_OIL_TEXTURE))
            //流体桶,流体方块,流体消失速度，防爆性
            .bucket(EndlessItems.infinityFluidBucket).block(EndlessBlocks.infinityFluid).slopeFindDistance(1)
            .tickRate(40).explosionResistance(Float.MAX_VALUE);
}