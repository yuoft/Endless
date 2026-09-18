package com.yuo.endless.client.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.yuo.endless.Endless;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.client.lib.CCShaderInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Mod.EventBusSubscriber(modid = Endless.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GapingVoidShaders {
    private static final float INFLUENCE_RADIUS = 6.0F; //引力半径系数
    private static final ResourceLocation ACCRETION_TEXTURE = EndlessUtils.fa("textures/entity/gaping_void_accretion.png");
    private static final List<Hole> PENDING = new ArrayList<>();
    private static ShaderInstance shader;
    private static RenderTarget sceneCopy;

    private GapingVoidShaders() {
    }

    public static void register(RegisterShadersEvent event) {
        releaseSceneCopy();
        PENDING.clear();
        event.registerShader(CCShaderInstance.create(event.getResourceProvider(), EndlessUtils.fa("gaping_void"),
                DefaultVertexFormat.POSITION_TEX), loaded -> shader = loaded);
    }

    public static void enqueue(Matrix4f pose, float radius, float time) {
        Vector3f center = pose.transformPosition(new Vector3f());
        Vector3f normal = pose.transformDirection(new Vector3f(0.12F, 1.0F, 0.24F)).normalize();
        Vector3f axis = pose.transformDirection(new Vector3f(1.0F, -0.12F, 0.0F)).normalize();
        PENDING.add(new Hole(center, normal, axis, radius, time));
    }

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            PENDING.clear();
            return;
        }
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            return;
        }
        if (PENDING.isEmpty() || shader == null) {
            releaseSceneCopy();
            PENDING.clear();
            return;
        }

        RenderTarget target = Minecraft.getInstance().getMainRenderTarget();
        ShaderInstance previousShader = RenderSystem.getShader();
        boolean depthTest = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        boolean depthWrite = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        boolean cull = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        try {
            ensureSceneCopy(target);
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();
            RenderSystem.disableBlend();
            RenderSystem.setShader(() -> shader);
            Matrix4f projection = event.getProjectionMatrix();
            shader.safeGetUniform("InverseProjection").set(new Matrix4f(projection).invert());
            shader.safeGetUniform("SceneProjection").set(projection);
            shader.setSampler("AccretionTexture", Minecraft.getInstance().getTextureManager().getTexture(ACCRETION_TEXTURE));

            PENDING.sort(Comparator.comparingDouble((Hole hole) -> hole.center.lengthSquared()).reversed());
            for (Hole hole : PENDING) {
                float[] bounds = projectedBounds(hole, projection);
                if (bounds == null) {
                    continue;
                }
                copyScene(target);
                shader.setSampler("SceneColor", sceneCopy.getColorTextureId());
                shader.setSampler("SceneDepth", sceneCopy.getDepthTextureId());
                shader.safeGetUniform("HoleCenter").set(hole.center.x, hole.center.y, hole.center.z);
                shader.safeGetUniform("DiskNormal").set(hole.normal.x, hole.normal.y, hole.normal.z);
                shader.safeGetUniform("DiskAxis").set(hole.axis.x, hole.axis.y, hole.axis.z);
                shader.safeGetUniform("HorizonRadius").set(hole.radius);
                shader.safeGetUniform("Time").set(hole.time);
                drawQuad(bounds);
            }
        } finally {
            PENDING.clear();
            target.bindWrite(true);
            RenderSystem.depthMask(depthWrite);
            if (depthTest) RenderSystem.enableDepthTest(); else RenderSystem.disableDepthTest();
            if (cull) RenderSystem.enableCull(); else RenderSystem.disableCull();
            if (blend) RenderSystem.enableBlend(); else RenderSystem.disableBlend();
            RenderSystem.setShader(() -> previousShader);
        }
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

    private static void copyScene(RenderTarget target) {
        GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, target.frameBufferId);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, sceneCopy.frameBufferId);
        GlStateManager._glBlitFrameBuffer(0, 0, target.width, target.height, 0, 0, sceneCopy.width, sceneCopy.height,
                GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
        target.bindWrite(true);
    }

    private static float[] projectedBounds(Hole hole, Matrix4f projection) {
        float extent = hole.radius * INFLUENCE_RADIUS;
        if (hole.center.z - extent >= 0.0F) {
            return null;
        }
        if (hole.center.z + extent >= -0.05F) {
            return new float[]{-1.0F, -1.0F, 1.0F, 1.0F};
        }
        float left = 1.0F, bottom = 1.0F, right = -1.0F, top = -1.0F;
        for (int corner = 0; corner < 8; corner++) {
            Vector4f point = new Vector4f(hole.center.x + ((corner & 1) == 0 ? -extent : extent),
                    hole.center.y + ((corner & 2) == 0 ? -extent : extent),
                    hole.center.z + ((corner & 4) == 0 ? -extent : extent), 1.0F).mul(projection);
            left = Math.min(left, point.x / point.w);
            bottom = Math.min(bottom, point.y / point.w);
            right = Math.max(right, point.x / point.w);
            top = Math.max(top, point.y / point.w);
        }
        left = Math.max(-1.0F, left);
        bottom = Math.max(-1.0F, bottom);
        right = Math.min(1.0F, right);
        top = Math.min(1.0F, top);
        return right <= left || top <= bottom ? null : new float[]{left, bottom, right, top};
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

    @SubscribeEvent
    public static void logout(ClientPlayerNetworkEvent.LoggingOut event) {
        if (RenderSystem.isOnRenderThread()) {
            clear();
        } else {
            RenderSystem.recordRenderCall(GapingVoidShaders::clear);
        }
    }

    private static void clear() {
        PENDING.clear();
        releaseSceneCopy();
    }

    private static void releaseSceneCopy() {
        if (sceneCopy != null) {
            sceneCopy.destroyBuffers();
            sceneCopy = null;
        }
    }

    private record Hole(Vector3f center, Vector3f normal, Vector3f axis, float radius, float time) {
    }
}
