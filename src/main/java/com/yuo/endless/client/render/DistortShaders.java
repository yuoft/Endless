package com.yuo.endless.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.yuo.endless.Endless;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.client.lib.CCShaderInstance;
import com.yuo.endless.client.lib.CCUniform;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Endless.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DistortShaders {
    public static CCShaderInstance distortShader;
    public static CCUniform distortTime;
    public static CCUniform distortStrength;

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) {
        event.registerShader(
                CCShaderInstance.create(
                        event.getResourceProvider(),
                        EndlessUtils.fa("distort"), // 对应 assets/endless/shaders/core/distort.json
                        DefaultVertexFormat.POSITION_COLOR_TEX // 与 RenderType 格式一致
                ),
                shader -> {
                    distortShader = (CCShaderInstance) shader;
                    distortTime = Objects.requireNonNull(distortShader.getUniform("time"));
                    distortStrength = Objects.requireNonNull(distortShader.getUniform("distortStrength"));
                    // 可在此设置初始值
                    distortTime.set(10f);
                    distortStrength.set(2f);
                }
        );
    }
}