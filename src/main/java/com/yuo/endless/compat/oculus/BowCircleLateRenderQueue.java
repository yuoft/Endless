package com.yuo.endless.compat.oculus;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.DyeColor;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class BowCircleLateRenderQueue {

    private static final List<Entry> ENTRIES = new ArrayList<>();
    private static boolean deferThisFrame;

    private BowCircleLateRenderQueue() {}

    public static void beginFrame(RenderFrameState.Snapshot snapshot) {
        ENTRIES.clear();
        deferThisFrame = snapshot.worldRenderActive() && snapshot.shaderPackActive();
    }

    public static boolean shouldDefer() {
        return deferThisFrame;
    }

    public static void endFrame() {
        ENTRIES.clear();
        deferThisFrame = false;
    }

    public static void enqueue(PoseStack poseStack, RenderType type, DyeColor color) {
        if (!deferThisFrame || RenderFrameState.isShadowPass()) return;
        ENTRIES.add(new Entry(
                new Matrix4f(poseStack.last().pose()),
                new Matrix4f(RenderSystem.getModelViewMatrix()),
                new Matrix4f(RenderSystem.getProjectionMatrix()),
                type, color));
    }

    public static void renderAfterLevel() {
        if (ENTRIES.isEmpty()) return;

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

                mvStack.last().pose().set(entry.modelView);
                RenderSystem.applyModelViewMatrix();
                RenderSystem.setProjectionMatrix(
                        new Matrix4f(entry.projection), VertexSorting.DISTANCE_TO_ORIGIN);

                VertexConsumer consumer = buffers.getBuffer(entry.type);
                addVertex(consumer, entry.pose, entry.color);
                buffers.endBatch(entry.type);

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

    private static void addVertex(VertexConsumer b, Matrix4f m, DyeColor c) {
        int r = getR(c), g = getG(c), bl = getB(c);
        b.vertex(m, 0, 0, 0).color(r, g, bl, 255).uv(0, 0).endVertex();
        b.vertex(m, 0, 0, 1).color(r, g, bl, 255).uv(0, 1).endVertex();
        b.vertex(m, 1, 0, 1).color(r, g, bl, 255).uv(1, 1).endVertex();
        b.vertex(m, 1, 0, 0).color(r, g, bl, 255).uv(1, 0).endVertex();
    }

    private static int getR(DyeColor c) { return c.getTextColor() >> 16 & 255; }
    private static int getG(DyeColor c) { return c.getTextColor() >> 8  & 255; }
    private static int getB(DyeColor c) { return c.getTextColor()       & 255; }

    private record Entry(Matrix4f pose, Matrix4f modelView, Matrix4f projection,
                         RenderType type, DyeColor color) {}
}