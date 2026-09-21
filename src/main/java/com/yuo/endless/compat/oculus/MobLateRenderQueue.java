package com.yuo.endless.compat.oculus;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.yuo.endless.client.model.InfinityArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public final class MobLateRenderQueue {
    private static final List<Entry> QUEUE = new ArrayList<>();
    private static boolean deferThisFrame;

    private MobLateRenderQueue() {
    }

    public static void beginFrame(RenderFrameState.Snapshot snapshot) {
        QUEUE.clear();

        deferThisFrame = snapshot.worldRenderActive() && snapshot.shaderPackActive();
    }

    public static boolean shouldDefer() {
        return deferThisFrame;
    }

    public static void enqueue(EntityModel<?> model, PoseStack poseStack, LivingEntity entity, int packedLight, int packedOverlay) {
        if (!deferThisFrame || model == null || entity == null || RenderFrameState.isShadowPass()) {
            return;
        }

        ModelVertices geometry = new ModelVertices();
        poseStack.pushPose();
        try {
            poseStack.scale(1.02F, 1.02F, 1.02F);
            model.renderToBuffer(poseStack, geometry, packedLight, packedOverlay, 0.84F, 1.0F, 0.95F, 0.8F);
        } finally {
            poseStack.popPose();
        }
        PoseStack.Pose pose = poseStack.last();

        QUEUE.add(new Entry(geometry, entity, new Matrix4f(pose.pose()), new Matrix3f(pose.normal()), new Matrix4f(RenderSystem.getModelViewMatrix()), new Matrix4f(RenderSystem.getProjectionMatrix()), packedLight, packedOverlay));
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
                CosmicArmorLateRenderQueue.applyCosmicUniforms(1.0F);
                entry.geometry.draw(InfinityArmorModel.material(InfinityArmorModel.MASK_INV).buffer(buffers, ignored -> LateRenderTypes.COSMIC_ENTITY));
                buffers.endBatch(LateRenderTypes.COSMIC_ENTITY);
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

    public static void endFrame() {
        QUEUE.clear();
        deferThisFrame = false;
    }

    private record Entry(ModelVertices geometry, LivingEntity entity, Matrix4f pose, Matrix3f normal,
                         Matrix4f modelView, Matrix4f projection, int packedLight, int packedOverlay) {
    }
}