package com.yuo.endless.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.entity.GapingVoidEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Queues the black hole for the world composite pass, after translucent terrain
 * and particles have finished. The original GapingVoidRender remains available.
 */
public class GapingVoidShaderRender extends EntityRenderer<GapingVoidEntity> {
    private static final ResourceLocation TEXTURE = EndlessUtils.fa("textures/entity/void.png");

    public GapingVoidShaderRender(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(GapingVoidEntity entity, float yaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int packedLight) {
        // Client interpolation can run past the final tick before removal arrives.
        float age = Mth.clamp(entity.getAge() + partialTick, 0.0F, GapingVoidEntity.maxLifetime);
        // The Shadertoy r_bar is the visible shadow radius, rather than the
        // previous integrator's smaller Schwarzschild-radius unit.
        float radius = GapingVoidEntity.getVoidScale(age) * 0.50F;
        if (Float.isFinite(radius) && radius > 0.001F) {
            GapingVoidShaders.enqueue(poseStack.last().pose(), radius, age / 20.0F);
        }
        super.render(entity, yaw, partialTick, poseStack, buffers, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(GapingVoidEntity entity) {
        return TEXTURE;
    }
}
