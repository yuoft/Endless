package com.yuo.endless.client.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.yuo.endless.Endless;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.client.lib.CCShaderInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public final class GapingVoidShaders {
    private static final ResourceLocation ACCRETION_TEXTURE = EndlessUtils.fa("textures/entity/gaping_void_accretion.png");
    public static ShaderInstance shader;
    private static RenderTarget sceneCopy;

    private GapingVoidShaders() {
    }

    public static void register(RegisterShadersEvent event) {
        releaseSceneCopy();
        event.registerShader(CCShaderInstance.create(event.getResourceProvider(), EndlessUtils.fa("gaping_void"),
                DefaultVertexFormat.POSITION_TEX), loaded -> shader = loaded);
    }

    /**
     * shader黑洞加引力透镜
     */
    public static void renderDirect(PoseStack stack, float horizonRadius, float time) {
        if (shader == null) return;

        Minecraft mc = Minecraft.getInstance();
        RenderTarget target = mc.getMainRenderTarget();

        ShaderInstance previousShader = RenderSystem.getShader();
        boolean depthTest  = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        boolean depthWrite = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        boolean cull       = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        boolean blend      = GL11.glIsEnabled(GL11.GL_BLEND);

        PoseStack mvStack = RenderSystem.getModelViewStack();
        try {
            Matrix4f projection = RenderSystem.getProjectionMatrix();
            Matrix4f pose = stack.last().pose();

            // 世界空间：中心、法线、轴
            Vector3f center = pose.transformPosition(new Vector3f());
            Vector3f normal = pose.transformDirection(new Vector3f(0.12F, 1.0F, 0.24F)).normalize();
            Vector3f axis   = pose.transformDirection(new Vector3f(1.0F, -0.12F, 0.0F)).normalize();

            // 影响半径（要和 fsh 里的 INFLUENCE_RADIUS 一致）
            float influence = horizonRadius * 6.0F;

            // 在 View 空间算包围盒，再投影到 NDC
            Matrix4f view = new Matrix4f(RenderSystem.getModelViewMatrix());
            int[] rect = projectedPixelRect(center, influence, view, projection, target.width, target.height);
            if (rect == null) return;

            int rx = rect[0], ry = rect[1], rw = rect[2], rh = rect[3];

            ensureSceneCopy(target);
            copySceneRegion(target, rx, ry, rw, rh);

            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();
            RenderSystem.disableBlend();
            RenderSystem.setShader(() -> shader);

            shader.setSampler("SceneColor", sceneCopy.getColorTextureId());
            shader.setSampler("SceneDepth", sceneCopy.getDepthTextureId());
            shader.setSampler("AccretionTexture",
                    mc.getTextureManager().getTexture(ACCRETION_TEXTURE));

            shader.safeGetUniform("InverseProjection").set(new Matrix4f(projection).invert());
            shader.safeGetUniform("SceneProjection").set(projection);
            shader.safeGetUniform("HoleCenter").set(center.x, center.y, center.z);
            shader.safeGetUniform("DiskNormal").set(normal.x, normal.y, normal.z);
            shader.safeGetUniform("DiskAxis").set(axis.x, axis.y, axis.z);
            shader.safeGetUniform("HorizonRadius").set(horizonRadius);
            shader.safeGetUniform("Time").set(time);

            // 传递区域矩形，shader 里把屏幕 UV 重映射到该区域
            float u0 = (float) rx / target.width;
            float v0 = (float) ry / target.height;
            float u1 = (float) (rx + rw) / target.width;
            float v1 = (float) (ry + rh) / target.height;
            shader.safeGetUniform("RegionUV0").set(u0, v0);
            shader.safeGetUniform("RegionUV1").set(u1, v1);

            // ModelView 重置，四边形只受投影影响
            mvStack.pushPose();
            mvStack.setIdentity();
            RenderSystem.applyModelViewMatrix();

            // NDC 范围：从 rect 反推
            float ndcL = u0 * 2.0F - 1.0F;
            float ndcB = v0 * 2.0F - 1.0F;
            float ndcR = u1 * 2.0F - 1.0F;
            float ndcT = v1 * 2.0F - 1.0F;
            drawQuad(new float[]{ndcL, ndcB, ndcR, ndcT});

            mvStack.popPose();
            RenderSystem.applyModelViewMatrix();
        } finally {
            target.bindWrite(true);
            RenderSystem.depthMask(depthWrite);
            if (depthTest) RenderSystem.enableDepthTest(); else RenderSystem.disableDepthTest();
            if (cull) RenderSystem.enableCull(); else RenderSystem.disableCull();
            if (blend) RenderSystem.enableBlend(); else RenderSystem.disableBlend();
            RenderSystem.setShader(() -> previousShader);
        }
    }

    private static int[] projectedPixelRect(Vector3f centerWorld, float worldRadius, Matrix4f view, Matrix4f proj, int width, int height) {
        float minX = 1, minY = 1, maxX = -1, maxY = -1;
        for (int c = 0; c < 8; c++) {
            Vector4f p = new Vector4f(
                    centerWorld.x + ((c & 1) == 0 ? -worldRadius : worldRadius),
                    centerWorld.y + ((c & 2) == 0 ? -worldRadius : worldRadius),
                    centerWorld.z + ((c & 4) == 0 ? -worldRadius : worldRadius),
                    1.0F);
            p.mul(view).mul(proj);
            if (p.w <= 0.0F) {
                // 跨越摄像机平面，退化为全屏
                return new int[]{0, 0, width, height};
            }
            float nx = p.x / p.w;
            float ny = p.y / p.w;
            minX = Math.min(minX, nx);
            minY = Math.min(minY, ny);
            maxX = Math.max(maxX, nx);
            maxY = Math.max(maxY, ny);
        }
        minX = Math.max(-1.0F, minX);
        minY = Math.max(-1.0F, minY);
        maxX = Math.min( 1.0F, maxX);
        maxY = Math.min( 1.0F, maxY);
        if (maxX <= minX || maxY <= minY) return null;

        int x0 = (int) Math.floor((minX * 0.5F + 0.5F) * width);
        int y0 = (int) Math.floor((minY * 0.5F + 0.5F) * height);
        int x1 = (int) Math.ceil ((maxX * 0.5F + 0.5F) * width);
        int y1 = (int) Math.ceil ((maxY * 0.5F + 0.5F) * height);
        x0 = Math.max(0, x0);
        y0 = Math.max(0, y0);
        x1 = Math.min(width,  x1);
        y1 = Math.min(height, y1);
        if (x1 <= x0 || y1 <= y0) return null;
        return new int[]{x0, y0, x1 - x0, y1 - y0};
    }

    private static void copySceneRegion(RenderTarget target, int x, int y, int w, int h) {
        GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, target.frameBufferId);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, sceneCopy.frameBufferId);
        GlStateManager._glBlitFrameBuffer(
                x, y, x + w, y + h,
                x, y, x + w, y + h,
                GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT,
                GL11.GL_NEAREST);
        target.bindWrite(true);
    }

    private static void ensureSceneCopy(RenderTarget target) {
        if (sceneCopy == null || sceneCopy.width != target.width || sceneCopy.height != target.height
                || sceneCopy.isStencilEnabled() != target.isStencilEnabled()) {
            releaseSceneCopy();
            sceneCopy = new TextureTarget(target.width, target.height, true, Minecraft.ON_OSX);
            if (target.isStencilEnabled()) {
                sceneCopy.enableStencil();
            }
            sceneCopy.setFilterMode(GL11.GL_LINEAR);
        }
    }

    private static void drawQuad(float[] bounds) {
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        vertex(buffer, bounds[0], bounds[1]);
        vertex(buffer, bounds[2], bounds[1]);
        vertex(buffer, bounds[2], bounds[3]);
        vertex(buffer, bounds[0], bounds[3]);
        BufferUploader.drawWithShader(buffer.end());
    }

    private static void vertex(BufferBuilder buffer, float x, float y) {
        buffer.vertex(x, y, 0.0F).uv(x * 0.5F + 0.5F, y * 0.5F + 0.5F).endVertex();
    }

    private static void releaseSceneCopy() {
        if (sceneCopy != null) {
            sceneCopy.destroyBuffers();
            sceneCopy = null;
        }
    }
}
