package com.yuo.endless.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.yuo.endless.Endless;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.client.lib.CCShaderInstance;
import com.yuo.endless.client.lib.CCUniform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Endless.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DistortShaders {

    public static final ResourceLocation ACCRETION_TEXTURE = EndlessUtils.fa("textures/entity/gaping_void_accretion.png");

    public static CCShaderInstance gapingVoidShader;
    // 和 Cosmic 一样的 BLOCK 顶点格式
    public static final RenderType GAPING_VOID_RENDER_TYPE = RenderType.create(
            "endless:gaping_void",
            DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> gapingVoidShader))
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setTextureState(RenderStateShard.NO_TEXTURE)
                    .createCompositeState(false));
    public static CCUniform holeCenter;
    public static CCUniform diskNormal;
    public static CCUniform diskAxis;
    public static CCUniform horizonRadius;
    public static CCUniform time;

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) {
        event.registerShader(
                CCShaderInstance.create(
                        event.getResourceProvider(),
                        EndlessUtils.fa("gaping_void"),
                        DefaultVertexFormat.BLOCK),
                shader -> {
                    gapingVoidShader = (CCShaderInstance) shader;
                    holeCenter = Objects.requireNonNull(gapingVoidShader.getUniform("HoleCenter"));
                    diskNormal = Objects.requireNonNull(gapingVoidShader.getUniform("DiskNormal"));
                    diskAxis = Objects.requireNonNull(gapingVoidShader.getUniform("DiskAxis"));
                    horizonRadius = Objects.requireNonNull(gapingVoidShader.getUniform("HorizonRadius"));
                    time = Objects.requireNonNull(gapingVoidShader.getUniform("Time"));

                    // 关键：在 shader 每次 apply 后重新绑定 sampler
                    gapingVoidShader.onApply(() -> {
                        Minecraft mc = Minecraft.getInstance();
                        gapingVoidShader.setSampler("AccretionTexture",
                                mc.getTextureManager().getTexture(ACCRETION_TEXTURE));
                    });
                });
    }
}