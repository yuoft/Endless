package com.yuo.endless.Blocks.Fluid;

import com.yuo.endless.Endless;
import com.yuo.endless.RlUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

public class EndlessFluidTypes {
    public static final ResourceLocation STILL_OIL_TEXTURE = RlUtils.fa("block/fluids/fluid");
    public static final ResourceLocation FLOWING_OIL_TEXTURE = RlUtils.fa("block/fluids/fluid_flow");
    public static final ResourceLocation OVERLAY_OIL_TEXTURE = RlUtils.fa("block/fluids/fluid_overlay");

    // 获得deferredRegister的流体type的注册对象
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Endless.MOD_ID);

    // 注册一个流体的type 分别是流体密度，粘度，温度，稀有度  不能游泳，音效
    public static final RegistryObject<FluidType> INFINITY_FLUID_TYPE = register("infinity_fluid",
            FluidType.Properties.create().density(100000).viscosity(100000).temperature(Integer.MAX_VALUE).rarity(Rarity.EPIC)
                    .canSwim(false).sound(SoundAction.get("drink"), SoundEvents.HONEY_DRINK));


    // 注册方法传入name和配置
    // 其中new的是我们对fluidtype的包装类。
    // 其中的orl是我们雾的颜色。
    private static RegistryObject<FluidType> register(String name, FluidType.Properties properties) {
        return FLUID_TYPES.register(name, () -> new ModFluidType(STILL_OIL_TEXTURE, FLOWING_OIL_TEXTURE, OVERLAY_OIL_TEXTURE,
                0xff333333, new Vector3f(224f / 255f, 56f / 255f, 208f / 255f), properties));
    }

}
