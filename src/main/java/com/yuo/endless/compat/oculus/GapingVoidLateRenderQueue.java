package com.yuo.endless.compat.oculus;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.math.Axis;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.client.lib.CCModel;
import com.yuo.endless.client.lib.CCRenderState;
import com.yuo.endless.client.lib.TransformingVertexConsumer;
import com.yuo.endless.client.render.GapingVoidShaders;
import com.yuo.endless.config.ModConfig;
import com.yuo.endless.entity.GapingVoidEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public final class GapingVoidLateRenderQueue {
    private static final ResourceLocation VOID_TEXTURE = EndlessUtils.fa("textures/entity/void.png");
    private static final ResourceLocation VOID_HALO_TEXTURE = EndlessUtils.fa("textures/entity/void_halo.png");
    private static final RenderType VOID_HALO_RENDER_TYPE = createVoidHaloRenderType();
    private static final RenderType VOID_HEMISPHERE_RENDER_TYPE = createVoidHemisphereRenderType();
    private static final List<Entry> QUEUE = new ArrayList<>();
    private static boolean deferThisFrame;

    private GapingVoidLateRenderQueue() {
    }

    public static void beginFrame(RenderFrameState.Snapshot snapshot) {
        QUEUE.clear();
        deferThisFrame = snapshot.worldRenderActive() && snapshot.shaderPackActive();
    }

    public static boolean shouldDefer() {
        return deferThisFrame;
    }

    public static void endFrame() {
        QUEUE.clear();
        deferThisFrame = false;
    }

    public static void enqueue(GapingVoidEntity entity, CCModel hemisphere, PoseStack poseStack, float partialTicks, int packedLight) {
        if (!deferThisFrame || entity == null || hemisphere == null || RenderFrameState.isShadowPass()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) {
            return;
        }

        float age = (float) entity.getAge() + partialTicks;

        float scale = (float) GapingVoidEntity.getVoidScale(age);

        Vec3 camera = minecraft.gameRenderer.getMainCamera().getPosition();

        double dx = entity.getX() - camera.x();
        double dy = entity.getY() - camera.y();
        double dz = entity.getZ() - camera.z();
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double haloCoord = 0.58D * scale;
        double haloScaleDistance = 2.2D * scale;
        if (length <= haloScaleDistance && haloScaleDistance > 0.0D) {
            double close = (haloScaleDistance - length) / haloScaleDistance;
            haloCoord *= 1.0D + close * close * close * close * 1.5D;
        }

        Colour colour = calculateColour(age);
        // The halo and hemisphere share the same camera-facing orientation.
        PoseStack orientedPose = new PoseStack();
        orientedPose.last().pose().set(poseStack.last().pose());
        orientedPose.last().normal().set(poseStack.last().normal());
        orientedPose.mulPose(Axis.YP.rotationDegrees((float) (Math.atan2(dx, dz) * 57.29578D)));
        orientedPose.mulPose(Axis.XP.rotationDegrees((float) (Math.atan2(Math.sqrt(dx * dx + dz * dz), dy) * (180.0D / Math.PI) + 90.0D)));
        PoseStack.Pose pose = orientedPose.last();
        QUEUE.add(new Entry(entity, hemisphere, new Matrix4f(pose.pose()), new Matrix3f(pose.normal()), new Matrix4f(RenderSystem.getModelViewMatrix()), new Matrix4f(RenderSystem.getProjectionMatrix()), age, (float) haloCoord, scale, colour.red, colour.green, colour.blue, colour.alpha, packedLight));
    }

    public static void renderAfterLevel() {
        if (!deferThisFrame || QUEUE.isEmpty()) {
            QUEUE.clear();
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level == null) {
            QUEUE.clear();
            return;
        }

        MultiBufferSource.BufferSource buffers = minecraft.renderBuffers().bufferSource();
        Matrix4f previousProjection = new Matrix4f(RenderSystem.getProjectionMatrix());
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();

        try {
            LatePassState.prepare();

            for (Entry entry : QUEUE) {
                if (entry.entity.isRemoved()) {
                    continue;
                }

                modelViewStack.last().pose().set(entry.modelView);
                RenderSystem.applyModelViewMatrix();
                RenderSystem.setProjectionMatrix(new Matrix4f(entry.projection), VertexSorting.DISTANCE_TO_ORIGIN);
                PoseStack entityPoseStack = new PoseStack();
                entityPoseStack.last().pose().set(entry.pose);
                entityPoseStack.last().normal().set(entry.normal);

                boolean flag = ModConfig.SERVER.isVoidNewRender.get();
                if (!flag) renderHalo(entityPoseStack, buffers, entry);
                entityPoseStack.scale(entry.scale, entry.scale, entry.scale);
                if (flag) {
                    entityPoseStack.translate(0,1,0);
                    GapingVoidShaders.renderDirect(entityPoseStack, entry.haloCord, entry.age / 20.0F);
                }
                if (!flag) renderHemisphere(entityPoseStack, buffers, entry);
                buffers.endBatch();
            }

            buffers.endBatch();
        } finally {
            RenderSystem.setProjectionMatrix(previousProjection, VertexSorting.DISTANCE_TO_ORIGIN);
            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();

            LatePassState.finish();
            QUEUE.clear();
        }
    }

    private static void renderHalo(PoseStack stack, MultiBufferSource.BufferSource buffers, Entry entry) {
        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(90.0F));

        float size = entry.haloCord;
        TransformingVertexConsumer consumer = new TransformingVertexConsumer(buffers.getBuffer(VOID_HALO_RENDER_TYPE), stack);
        consumer.vertex(-size, 0.0D, -size).color(entry.red, entry.green, entry.blue, entry.alpha).uv(0.0F, 0.0F).endVertex();
        consumer.vertex(-size, 0.0D, size).color(entry.red, entry.green, entry.blue, entry.alpha).uv(0.0F, 1.0F).endVertex();
        consumer.vertex(size, 0.0D, size).color(entry.red, entry.green, entry.blue, entry.alpha).uv(1.0F, 1.0F).endVertex();
        consumer.vertex(size, 0.0D, -size).color(entry.red, entry.green, entry.blue, entry.alpha).uv(1.0F, 0.0F).endVertex();
        stack.popPose();
    }

    private static void renderHemisphere(PoseStack stack, MultiBufferSource.BufferSource buffers, Entry entry) {
        CCRenderState renderState = CCRenderState.instance();
        renderState.reset();
        renderState.bind(VOID_HEMISPHERE_RENDER_TYPE, buffers, stack);
        renderState.baseColour = ((int) (entry.red * 255.0F) << 24) | ((int) (entry.green * 255.0F) << 16) | ((int) (entry.blue * 255.0F) << 8) | (int) (entry.alpha * 255.0F);
        entry.hemisphere.render(renderState);
    }

    private static Colour calculateColour(double age) {
        double life = age / 186.0D;
        double fadeIn = Math.max(0.0D, (life - 0.95D) / 0.05D);
        double fadeOut = Math.max(0.0D, 1.0D - life * 30.0D);
        double value = Math.max(fadeIn, fadeOut);
        return new Colour((float) value, (float) value, (float) value, 1.0F);
    }

    private static RenderType createVoidHaloRenderType() {
        return RenderType.create("endless_void_halo", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(VOID_HALO_TEXTURE, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setWriteMaskState(RenderStateShard.COLOR_WRITE).createCompositeState(false));
    }

    private static RenderType createVoidHemisphereRenderType() {
        return RenderType.create("endless_void_hemisphere", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 256, RenderType.CompositeState.builder().setShaderState(RenderType.RENDERTYPE_ENTITY_ALPHA_SHADER).setTextureState(new RenderStateShard.TextureStateShard(VOID_TEXTURE, false, false)).setCullState(RenderStateShard.NO_CULL).createCompositeState(false));
    }

    private record Entry(GapingVoidEntity entity, CCModel hemisphere, Matrix4f pose, Matrix3f normal,
                         Matrix4f modelView, Matrix4f projection, float age, float haloCord, float scale, float red, float green,
                         float blue, float alpha, int packedLight) {
    }

    private record Colour(float red, float green, float blue, float alpha) {
    }
}
