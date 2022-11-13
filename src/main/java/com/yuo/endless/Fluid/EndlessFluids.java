package com.yuo.endless.Fluid;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EndlessFluids {
    public static final ResourceLocation STILL_OIL_TEXTURE = new ResourceLocation(Endless.MOD_ID, "block/fluids/fluid");
    public static final ResourceLocation FLOWING_OIL_TEXTURE = new ResourceLocation(Endless.MOD_ID, "block/fluids/fluid_flow");

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Endless.MOD_ID);

    public static RegistryObject<FlowingFluid> infinityFluid = FLUIDS.register("infinity_fluid", () -> new ForgeFlowingFluid.Source(EndlessFluids.PROPERTIES));
    public static RegistryObject<FlowingFluid> infinityFluidFlowing = FLUIDS.register("infinity_fluid_flowing", () -> new ForgeFlowingFluid.Flowing(EndlessFluids.PROPERTIES));

    public static ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(infinityFluid, infinityFluidFlowing,
            //材质， 颜色，流体的稠度，流体桶，流体方块，流动距离，防爆性
            FluidAttributes.builder(STILL_OIL_TEXTURE, FLOWING_OIL_TEXTURE).color(0xFF256256).density(1000)).bucket(EndlessItems.infinityFluidBucket)
                .block(EndlessBlocks.infinityFluid).slopeFindDistance(4).explosionResistance(Float.MAX_VALUE);
}
