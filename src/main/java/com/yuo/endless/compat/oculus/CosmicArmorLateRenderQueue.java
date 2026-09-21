package com.yuo.endless.compat.oculus;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.client.AvaritiaShaders;
import com.yuo.endless.client.model.InfinityArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.*;

public final class CosmicArmorLateRenderQueue {
    private static final List<ArmorPartRender> QUEUE = new ArrayList<>();
    private static final List<WingRender> WING_QUEUE = new ArrayList<>();
    private static final List<EyeRender> EYE_QUEUE = new ArrayList<>();
    private static final List<PlayerLayerRender> PLAYER_LAYER_QUEUE = new ArrayList<>();
    private static final Set<LivingEntity> WING_WEARERS = Collections.newSetFromMap(new IdentityHashMap<>());
    private static final ResourceLocation WING_BASE_TEXTURE = EndlessUtils.fa("textures/models/infinity_armor_wing.png");
    private static final ResourceLocation WING_GLOW_TEXTURE = EndlessUtils.fa("textures/models/infinity_armor_wingglow.png");
    private static final RenderType WING_GLOW_RENDER_TYPE = exe$wingGlow(WING_GLOW_TEXTURE);
    private static final RenderType WING_BASE_RENDER_TYPE = RenderType.armorCutoutNoCull(WING_BASE_TEXTURE);
    private static final RenderType WING_COSMIC_RENDER_TYPE = exe$wingCosmic();
    private static final ResourceLocation ARMOR_EYE_TEXTURE = EndlessUtils.fa("textures/models/infinity_armor_eyes.png");
    private static final RenderType ARMOR_EYE_GLOW_RENDER_TYPE = exe$armorEyeGlow(ARMOR_EYE_TEXTURE);
    private static final RenderType ARMOR_HAT_EYE_RENDER_TYPE = exe$armorHatEye(ARMOR_EYE_TEXTURE);
    private static boolean renderingQueuedWing;
    private static boolean deferThisFrame;

    private CosmicArmorLateRenderQueue() {
    }

    private static RenderType exe$wingCosmic() {
        return RenderType.create("infinity_wing_cosmic", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicShader)).setTextureState(new RenderStateShard.TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setLightmapState(RenderType.LIGHTMAP).setWriteMaskState(RenderStateShard.COLOR_WRITE).setCullState(RenderType.NO_CULL).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
    }

    private static RenderType exe$armorEyeGlow(ResourceLocation texture) {
        return RenderType.create("infinity_armor_eye_glow", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(RenderType.POSITION_COLOR_TEX_LIGHTMAP_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(RenderType.LIGHTNING_TRANSPARENCY).setCullState(RenderType.NO_CULL).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
    }

    private static RenderType exe$armorHatEye(ResourceLocation texture) {
        return RenderType.create("infinity_armor_hat_eye", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(RenderType.POSITION_COLOR_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setCullState(RenderType.NO_CULL).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
    }

    private static RenderType exe$wingGlow(ResourceLocation texture) {
        return RenderType.create("infinity_wing_glow", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, RenderType.CompositeState.builder().setShaderState(RenderType.POSITION_COLOR_TEX_LIGHTMAP_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false)).setTransparencyState(RenderType.LIGHTNING_TRANSPARENCY).setCullState(RenderType.NO_CULL).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
    }

    public static boolean isRenderingQueuedWing() {
        return renderingQueuedWing;
    }

    public static void beginFrame(RenderFrameState.Snapshot snapshot) {
        endFrame();
        deferThisFrame = snapshot.worldRenderActive() && snapshot.shaderPackActive();
    }

    private static float exe$calculateGlowAlpha() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) {
            return 0.0F;
        }

        long time = mc.level.getGameTime();
        double pulse = Math.sin((double) time / 10.0D) * 0.5D + 0.5D;
        double pulseMagnitude = pulse * pulse * pulse * pulse * pulse * pulse;
        return (float) (pulseMagnitude * 0.5D);
    }

    public static void enqueuePart(PoseStack poseStack, ModelPart part, ResourceLocation texture, int light, int overlay, float r, float g, float b, float a) {
        if (!deferThisFrame || RenderFrameState.isShadowPass()) {
            return;
        }

        PoseStack.Pose pose = poseStack.last();

        QUEUE.add(new ArmorPartRender(new Matrix4f(pose.pose()), new Matrix3f(pose.normal()), part, texture, light, overlay, r, g, b, a));
    }

    public static void enqueueEyePart(PoseStack poseStack, ModelPart part, EyeType type, int light, int overlay) {
        if (!deferThisFrame || RenderFrameState.isShadowPass()) {
            return;
        }
        PoseStack.Pose pose = poseStack.last();
        EYE_QUEUE.add(new EyeRender(new Matrix4f(pose.pose()), new Matrix3f(pose.normal()), part, type, light, overlay));
    }

    public static void enqueueWing(LivingEntity wearer, PoseStack poseStack, InfinityArmorModel model, ResourceLocation texture, int light, int overlay, float r, float g, float b, float a) {
        if (!deferThisFrame || RenderFrameState.isShadowPass()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        if (wearer != null && wearer == mc.player && mc.options.getCameraType().isFirstPerson()) {
            return;
        }

        if (wearer != null && !WING_WEARERS.add(wearer)) {
            return;
        }

        PoseStack.Pose pose = poseStack.last();

        WING_QUEUE.add(new WingRender(new Matrix4f(pose.pose()), new Matrix3f(pose.normal()), new Matrix4f(RenderSystem.getModelViewMatrix()), new Matrix4f(RenderSystem.getProjectionMatrix()), model, texture, light, overlay, r, g, b, a));
    }

    public static void enqueuePlayerLayer(PoseStack poseStack, PlayerModel<Player> playerModel, int light) {
        if (!deferThisFrame || RenderFrameState.isShadowPass()) {
            return;
        }

        PoseStack.Pose pose = poseStack.last();

        PLAYER_LAYER_QUEUE.add(new PlayerLayerRender(new Matrix4f(pose.pose()), new Matrix3f(pose.normal()), List.of(playerModel.head, playerModel.hat, playerModel.body, playerModel.leftArm, playerModel.rightArm, playerModel.leftLeg, playerModel.rightLeg), light));
    }

    static void applyCosmicUniforms(float opacity) {
        Minecraft mc = Minecraft.getInstance();

        if (AvaritiaShaders.cosmicShader == null) {
            return;
        }

        float cosmicTime = (float) (System.currentTimeMillis() - (long) AvaritiaShaders.renderTime) / 2000.0F;
        AvaritiaShaders.cosmicTime.set(cosmicTime);
        AvaritiaShaders.cosmicOpacity.set(opacity);
        AvaritiaShaders.cosmicExternalScale.set(1.0F);

        if (mc.player != null) {
            AvaritiaShaders.cosmicYaw.set((float) (mc.player.getYRot() * 2.0F * Math.PI / 360.0F));
            AvaritiaShaders.cosmicPitch.set(-(float) (mc.player.getXRot() * 2.0F * Math.PI / 360.0F));
        }

        for (int i = 0; i < 10; ++i) {
            var sprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(EndlessUtils.fa("shader/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }

        if (AvaritiaShaders.cosmicUVs != null) {
            AvaritiaShaders.cosmicUVs.set(AvaritiaShaders.COSMIC_UVS);
        }
    }

    public static void renderAfterLevel() {
        if (QUEUE.isEmpty() && EYE_QUEUE.isEmpty() && WING_QUEUE.isEmpty() && PLAYER_LAYER_QUEUE.isEmpty()) {
            return;
        }

        // 改用全局复用的 BufferSource，不再每帧 new BufferBuilder
        MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
        try {
            LatePassState.prepare();

            applyCosmicUniforms(1.0F);
            for (ArmorPartRender render : QUEUE) {
                render.execute(buffers);
            }

            buffers.endBatch();

            for (EyeRender render : EYE_QUEUE) {
                render.savedPose.render(() -> render.execute(buffers));
            }

            buffers.endBatch(ARMOR_EYE_GLOW_RENDER_TYPE);
            buffers.endBatch(ARMOR_HAT_EYE_RENDER_TYPE);

            for (WingRender render : WING_QUEUE) {
                render.execute(buffers);
            }

            if (!PLAYER_LAYER_QUEUE.isEmpty()) {
                applyCosmicUniforms(2.0F);

                for (PlayerLayerRender render : PLAYER_LAYER_QUEUE) {
                    render.savedPose.render(() -> render.execute(buffers));
                }
            }

            buffers.endBatch();
        } finally {
            LatePassState.finish();
            endFrame();
        }
    }

    public static void endFrame() {
        QUEUE.clear();
        EYE_QUEUE.clear();
        WING_QUEUE.clear();
        PLAYER_LAYER_QUEUE.clear();
        WING_WEARERS.clear();
        renderingQueuedWing = false;
        deferThisFrame = false;
    }

    public enum EyeType {
        BODY_GLOW, HAT_RAINBOW
    }

    private static class ArmorPartRender {
        final Matrix4f poseMatrix;
        final Matrix3f normalMatrix;
        final ModelPart part;
        final ModelPose savedPose;
        final ResourceLocation texture;
        final int light, overlay;
        final float r, g, b, a;

        ArmorPartRender(Matrix4f p, Matrix3f n, ModelPart part, ResourceLocation texture, int light, int overlay, float r, float g, float b, float a) {
            this.poseMatrix = p;
            this.normalMatrix = n;
            this.part = part;
            this.savedPose = ModelPose.capture(part);
            this.texture = texture;
            this.light = light;
            this.overlay = overlay;
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        void execute(MultiBufferSource bufferSource) {
            PoseStack stack = new PoseStack();
            stack.last().pose().mul(this.poseMatrix);
            stack.last().normal().mul(this.normalMatrix);

            VertexConsumer buffer = InfinityArmorModel.material(texture).buffer(bufferSource, ignored -> LateRenderTypes.COSMIC_ENTITY);
            savedPose.render(() -> part.render(stack, buffer, light, overlay, r, g, b, a));
        }
    }

    private static class WingRender {
        final Matrix4f poseMatrix;
        final Matrix3f normalMatrix;
        final InfinityArmorModel model;
        final ResourceLocation texture;
        final int light, overlay;
        final float r, g, b, a;
        final Matrix4f modelViewMatrix;
        final Matrix4f projectionMatrix;

        WingRender(Matrix4f pose, Matrix3f normal, Matrix4f modelView, Matrix4f projection, InfinityArmorModel model, ResourceLocation texture, int light, int overlay, float r, float g, float b, float a) {
            this.poseMatrix = pose;
            this.normalMatrix = normal;
            this.modelViewMatrix = modelView;
            this.projectionMatrix = projection;
            this.model = model;
            this.texture = texture;
            this.light = light;
            this.overlay = overlay;
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }

        void execute(MultiBufferSource.BufferSource bufferSource) {
            Matrix4f previousProjection = new Matrix4f(RenderSystem.getProjectionMatrix());
            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();

            try {
                modelViewStack.last().pose().set(this.modelViewMatrix);
                RenderSystem.applyModelViewMatrix();
                RenderSystem.setProjectionMatrix(new Matrix4f(this.projectionMatrix), VertexSorting.DISTANCE_TO_ORIGIN);
                PoseStack wingStack = new PoseStack();
                wingStack.last().pose().set(this.poseMatrix);
                wingStack.last().normal().set(this.normalMatrix);

                renderingQueuedWing = true;

                try {
                    VertexConsumer baseBuffer = bufferSource.getBuffer(WING_BASE_RENDER_TYPE);

                    this.model.renderToBufferWing(wingStack, baseBuffer, this.light, this.overlay, this.r, this.g, this.b, this.a);

                    bufferSource.endBatch(WING_BASE_RENDER_TYPE);
                    applyCosmicUniforms(1.0F);
                    VertexConsumer cosmicBuffer = InfinityArmorModel.material(this.texture).buffer(bufferSource, ignored -> WING_COSMIC_RENDER_TYPE);

                    this.model.renderToBufferWing(wingStack, cosmicBuffer, this.light, this.overlay, this.r, this.g, this.b, this.a);

                    bufferSource.endBatch(WING_COSMIC_RENDER_TYPE);
                    VertexConsumer glowBuffer = bufferSource.getBuffer(WING_GLOW_RENDER_TYPE);
                    float glowAlpha = exe$calculateGlowAlpha();
                    this.model.renderToBufferWing(wingStack, glowBuffer, this.light, this.overlay, 0.84F, 1.0F, 0.95F, glowAlpha);

                    bufferSource.endBatch(WING_GLOW_RENDER_TYPE);
                } finally {
                    renderingQueuedWing = false;
                }
            } finally {
                RenderSystem.setProjectionMatrix(previousProjection, VertexSorting.DISTANCE_TO_ORIGIN);
                modelViewStack.popPose();
                RenderSystem.applyModelViewMatrix();
            }
        }
    }

    private static class EyeRender {
        final Matrix4f poseMatrix;
        final Matrix3f normalMatrix;
        final ModelPart part;
        final ModelPose savedPose;
        final EyeType type;
        final int light;
        final int overlay;

        EyeRender(Matrix4f poseMatrix, Matrix3f normalMatrix, ModelPart part, EyeType type, int light, int overlay) {
            this.poseMatrix = poseMatrix;
            this.normalMatrix = normalMatrix;
            this.part = part;
            this.savedPose = ModelPose.capture(part);
            this.type = type;
            this.light = light;
            this.overlay = overlay;
        }

        void execute(MultiBufferSource.BufferSource bufferSource) {
            PoseStack stack = new PoseStack();

            stack.last().pose().set(this.poseMatrix);
            stack.last().normal().set(this.normalMatrix);

            if (this.type == EyeType.BODY_GLOW) {
                float alpha = exe$calculateGlowAlpha();

                VertexConsumer buffer = bufferSource.getBuffer(ARMOR_EYE_GLOW_RENDER_TYPE);

                this.part.render(stack, buffer, this.light, this.overlay, 0.84F, 1.0F, 0.95F, alpha);

                return;
            }

            Minecraft mc = Minecraft.getInstance();

            long time = mc.level != null ? mc.level.getGameTime() : 0L;

            Random random = new Random();
            random.setSeed(time / 3L * 1723609L);

            float[] color = com.yuo.endless.client.lib.ColorUtils.HSVtoRGB(random.nextFloat() * 6.0F, 1.0F, 1.0F);

            VertexConsumer buffer = bufferSource.getBuffer(ARMOR_HAT_EYE_RENDER_TYPE);

            this.part.render(stack, buffer, this.light, this.overlay, color[0], color[1], color[2], 1.0F);
        }
    }

    private static class PlayerLayerRender {
        final Matrix4f poseMatrix;
        final Matrix3f normalMatrix;
        final List<ModelPart> parts;
        final ModelPose savedPose;
        final int light;

        PlayerLayerRender(Matrix4f p, Matrix3f n, List<ModelPart> parts, int light) {
            this.poseMatrix = p;
            this.normalMatrix = n;
            this.parts = parts;
            this.savedPose = ModelPose.capture(parts.toArray(ModelPart[]::new));
            this.light = light;
        }

        void execute(MultiBufferSource bufferSource) {
            PoseStack stack = new PoseStack();
            stack.last().pose().mul(this.poseMatrix);
            stack.last().normal().mul(this.normalMatrix);
            VertexConsumer buffer = InfinityArmorModel.material(InfinityArmorModel.MASK_INV).buffer(bufferSource, ignored -> LateRenderTypes.COSMIC_ENTITY);
            for (ModelPart part : parts) {
                part.render(stack, buffer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}