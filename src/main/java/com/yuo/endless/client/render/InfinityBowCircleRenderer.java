package com.yuo.endless.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yuo.endless.Endless;
import com.yuo.endless.compat.oculus.BowCircleLateRenderQueue;
import com.yuo.endless.compat.oculus.OculusCompat;
import com.yuo.endless.compat.oculus.RenderFrameState;
import com.yuo.endless.items.tool.InfinityBow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Mod.EventBusSubscriber(modid = Endless.MOD_ID, value = Dist.CLIENT)
public class InfinityBowCircleRenderer {

    private static final DyeColor[] CIRCLE_COLORS = {
            DyeColor.CYAN, DyeColor.GREEN, DyeColor.PURPLE, DyeColor.RED
    };

    // 复用，避免每帧分配
    private static final Vector3f UP = new Vector3f(0, 1, 0);
    private static final Vector3f LOOK = new Vector3f();

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        Player player = mc.player;
        ItemStack bow = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!(player.isUsingItem() && bow.getItem() instanceof InfinityBow)) return;

        int time = Mth.clamp(player.getUseItem().getUseDuration() - player.getUseItemRemainingTicks(), 0, 200);
        if (time < 10) return;
        int circleNum = Mth.clamp(InfinityBow.getCircleNumFormBowUseTime(time), 0, 4);
        if (circleNum <= 0) return;

        // 视线方向（世界）
        Vec3 lookVec = player.getLookAngle().normalize();
        LOOK.set((float) lookVec.x, (float) lookVec.y, (float) lookVec.z);
        if (LOOK.lengthSquared() < 1.0E-6f) LOOK.set(0, 0, 1);
        LOOK.normalize();

        // 把 (0,1,0) 旋转到视线方向
        Quaternionf rot = new Quaternionf().rotationTo(UP, LOOK);

        // 相机相对偏移：眼睛位置 - 相机位置
        Vec3 camPos = event.getCamera().getPosition();
        Vec3 eyePos = player.getEyePosition(event.getPartialTick());
        double ox = eyePos.x - camPos.x;
        double oy = eyePos.y - camPos.y;
        double oz = eyePos.z - camPos.z;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = mc.renderBuffers().bufferSource();

        poseStack.pushPose();
        // 移到眼睛位置（相对相机）
        poseStack.translate(ox, oy, oz);
        // 法阵法线朝 +Y → 旋转到视线方向
        poseStack.mulPose(rot);
        // 循环前拿到平滑时间，避免每 tick 跳一下
        float partialTick = event.getPartialTick();
        float baseAngle = (player.tickCount + partialTick) * 2.0f; // 每 tick 转 2°，可调

        for (int i = 1; i <= circleNum; i++) {
            // 圆环 i 在 time = (i-1)*50 出现，用 50 tick 从 0 涨到 1，之后冻结
            float appearAt = (i - 1) * 50f;
            float circleProgress = Mth.clamp((time - appearAt) / 50f, 0f, 1f);
            float radius = 0.5f * (float) Math.pow(2, i - 1) * (1.5f + circleProgress * 1.25f);
            double distance = 1.5 + i * 3 + radius;
            float scale = Mth.clamp(radius * 2, 0.01f, 20);

            // 每圈速度不同 + 方向交替，效果更好看
            float dir = (i % 2 == 0) ? -1f : 1f;
            float speed = 1.0f + i * 0.5f;
            float angle = baseAngle * speed * dir;

            poseStack.pushPose();
            poseStack.translate(0, distance, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(angle));   // ← 绕法线自转
            poseStack.scale(scale, scale, scale);
            poseStack.translate(-0.5, 0, -0.5);

            DyeColor color = CIRCLE_COLORS[(i - 1) % CIRCLE_COLORS.length];
            RenderType circleType = EndlessRenderTypes.getMagicCircle(
                    EndlessRenderTypes.RES_MAGIC_CIRCLE[i - 1]);

            // 光影激活时走延迟队列；否则走原路径
            if (OculusCompat.isShaderPackActive()
                    && !RenderFrameState.isShadowPass()
                    && BowCircleLateRenderQueue.shouldDefer()) {
                BowCircleLateRenderQueue.enqueue(poseStack, circleType, color);
            } else {
                VertexConsumer consumer = bufferSource.getBuffer(circleType);
                addVertex(poseStack, consumer, color);
            }
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    public static void addVertex(PoseStack matrixStack, VertexConsumer builder, DyeColor color) {
        Matrix4f matrix4f = matrixStack.last().pose();
        builder.vertex(matrix4f, 0, 0, 0).color(getR(color), getG(color), getB(color), 255).uv(0, 0).endVertex();
        builder.vertex(matrix4f, 0, 0, 1).color(getR(color), getG(color), getB(color), 255).uv(0, 1).endVertex();
        builder.vertex(matrix4f, 1, 0, 1).color(getR(color), getG(color), getB(color), 255).uv(1, 1).endVertex();
        builder.vertex(matrix4f, 1, 0, 0).color(getR(color), getG(color), getB(color), 255).uv(1, 0).endVertex();
    }

    private static int getR(DyeColor c) { return c.getTextColor() >> 16 & 255; }
    private static int getG(DyeColor c) { return c.getTextColor() >> 8  & 255; }
    private static int getB(DyeColor c) { return c.getTextColor()       & 255; }
}