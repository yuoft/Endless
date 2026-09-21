package com.yuo.endless.compat.oculus;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.yuo.endless.client.render.CosmicBlockRender;
import com.yuo.endless.items.EndlessItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CosmicBlockLateRenderQueue {
    private static final List<Entry> ENTRIES = new ArrayList<>();
    private static boolean deferThisFrame;

    private CosmicBlockLateRenderQueue() {
    }

    public static void beginFrame(RenderFrameState.Snapshot snapshot) {
        ENTRIES.clear();
        deferThisFrame = snapshot.shaderPackActive();
    }

    public static void endFrame() {
        ENTRIES.clear();
        deferThisFrame = false;
    }

    public static boolean shouldDefer() {
        return deferThisFrame;
    }

    public static void enqueue(BlockState blockState, PoseStack poseStack, int packedLight, int packedOverlay) {
        if (!deferThisFrame || RenderFrameState.isShadowPass()) return;

        PoseStack.Pose pose = poseStack.last();
        ENTRIES.add(new Entry(blockState, new Matrix4f(pose.pose()), new Matrix3f(pose.normal()), new Matrix4f(RenderSystem.getModelViewMatrix()), new Matrix4f(RenderSystem.getProjectionMatrix()), packedLight, packedOverlay));
    }

    public static void renderAfterLevel() {
        if (!deferThisFrame || ENTRIES.isEmpty()) {
            ENTRIES.clear();
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            ENTRIES.clear();
            return;
        }

        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        Matrix4f prevProj = new Matrix4f(RenderSystem.getProjectionMatrix());
        PoseStack mvStack = RenderSystem.getModelViewStack();
        mvStack.pushPose();

        try {
            LatePassState.prepare();
            Iterator<Entry> it = ENTRIES.iterator();

            while (it.hasNext()) {
                Entry entry = it.next();

                mvStack.last().pose().set(entry.modelView());
                RenderSystem.applyModelViewMatrix();
                RenderSystem.setProjectionMatrix(new Matrix4f(entry.projection()), VertexSorting.DISTANCE_TO_ORIGIN);

                PoseStack blockPoseStack = new PoseStack();
                blockPoseStack.last().pose().set(entry.pose());
                blockPoseStack.last().normal().set(entry.normal());

                ItemStack stack = new ItemStack(EndlessItems.cosmicBlock.get());
                CosmicBlockRender.renderBlockQuads(entry.blockState(), blockPoseStack, buffers, entry.packedLight(), entry.packedOverlay(), stack, LateRenderTypes.COSMIC_BLOCK);

                buffers.endBatch(LateRenderTypes.COSMIC_BLOCK);
                it.remove();
            }
        } finally {
            RenderSystem.setProjectionMatrix(prevProj, VertexSorting.DISTANCE_TO_ORIGIN);
            mvStack.popPose();
            RenderSystem.applyModelViewMatrix();
            LatePassState.finish();
            ENTRIES.clear();
        }
    }

    private record Entry(BlockState blockState, Matrix4f pose, Matrix3f normal, Matrix4f modelView, Matrix4f projection, int packedLight, int packedOverlay) {
    }
}