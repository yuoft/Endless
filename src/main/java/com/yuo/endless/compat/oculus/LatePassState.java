package com.yuo.endless.compat.oculus;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;

public final class LatePassState {

    public static void prepare() {
        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        RenderSystem.disableScissor();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.colorMask(true, true, true, true);RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.defaultBlendFunc();
    }

    public static void finish() {
        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        RenderSystem.disableScissor();
        RenderSystem.depthMask(true);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.defaultBlendFunc();
    }

    public static void repairAfterPipelineSwitch() {
        RenderSystem.assertOnRenderThread();
        RenderSystem.disableScissor();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        var main = Minecraft.getInstance().getMainRenderTarget();
        main.bindWrite(false);
        RenderSystem.viewport(0, 0, main.width, main.height);
    }

    private LatePassState() {}
}