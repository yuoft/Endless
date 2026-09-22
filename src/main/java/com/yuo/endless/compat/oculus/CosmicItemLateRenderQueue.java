package com.yuo.endless.compat.oculus;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.yuo.endless.client.AvaritiaShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CosmicItemLateRenderQueue {
    private static final List<CosmicItemTask> QUEUE = new ArrayList<>();
    private static boolean deferThisFrame = false;

    private CosmicItemLateRenderQueue() {
    }

    public static void beginFrame(RenderFrameState.Snapshot snapshot) {
        deferThisFrame = snapshot.shaderPackActive();
        QUEUE.clear();
    }

    public static boolean shouldDefer() {
        return deferThisFrame;
    }

    public static void enqueue(PoseStack poseStack, List<BakedQuad> quads, ItemStack stack, boolean flag, int light, int overlay, CosmicUniforms uniforms) {
        if (!deferThisFrame || RenderFrameState.isShadowPass()) return;

        PoseStack.Pose pose = poseStack.last();
        QUEUE.add(new CosmicItemTask(new Matrix4f(pose.pose()), new Matrix3f(pose.normal()), new Matrix4f(RenderSystem.getModelViewMatrix()), new Matrix4f(RenderSystem.getProjectionMatrix()), new ArrayList<>(quads), stack.copy(), flag, light, overlay, uniforms));
    }

    public static void renderAfterLevel() {
        if (QUEUE.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            QUEUE.clear();
            return;
        }

        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        Matrix4f prevProj = new Matrix4f(RenderSystem.getProjectionMatrix());
        PoseStack mvStack = RenderSystem.getModelViewStack();
        mvStack.pushPose();

        try {
            LatePassState.prepare();
            Iterator<CosmicItemTask> it = QUEUE.iterator();

            while (it.hasNext()) {
                CosmicItemTask task = it.next();

                mvStack.last().pose().set(task.modelView);
                RenderSystem.applyModelViewMatrix();
                RenderSystem.setProjectionMatrix(new Matrix4f(task.projection), VertexSorting.DISTANCE_TO_ORIGIN);

                task.uniforms.apply();

                PoseStack poseStack = new PoseStack();
                poseStack.last().pose().set(task.pose);
                poseStack.last().normal().set(task.normal);
                VertexConsumer cons = buffers.getBuffer(LateRenderTypes.COSMIC_ITEM);
                mc.getItemRenderer().renderQuadList(poseStack, cons, task.quads, task.stack, task.light, task.overlay);
                buffers.endBatch(LateRenderTypes.COSMIC_ITEM);
                it.remove();
            }
        } finally {
            RenderSystem.setProjectionMatrix(prevProj, VertexSorting.DISTANCE_TO_ORIGIN);
            mvStack.popPose();
            RenderSystem.applyModelViewMatrix();
            LatePassState.finish();
            QUEUE.clear();
        }
    }

    public static void endFrame() {
        QUEUE.clear();
        deferThisFrame = false;
    }

    public record CosmicUniforms(float time, float yaw, float pitch, float externalScale, float opacity, float[] uvs) {
        public void apply() {
            AvaritiaShaders.cosmicTime.set(time);
            AvaritiaShaders.cosmicYaw.set(yaw);
            AvaritiaShaders.cosmicPitch.set(pitch);
            AvaritiaShaders.cosmicExternalScale.set(externalScale);
            AvaritiaShaders.cosmicOpacity.set(opacity);
            if (AvaritiaShaders.cosmicUVs != null) {
                AvaritiaShaders.cosmicUVs.set(uvs);
            }
        }
    }

    private record CosmicItemTask(Matrix4f pose, Matrix3f normal, Matrix4f modelView, Matrix4f projection,
                                  List<BakedQuad> quads, ItemStack stack, boolean flag, int light, int overlay,
                                  CosmicUniforms uniforms) {
    }
}
