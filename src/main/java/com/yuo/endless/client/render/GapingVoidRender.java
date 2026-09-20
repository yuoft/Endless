package com.yuo.endless.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.yuo.endless.client.lib.*;
import com.yuo.endless.entity.GapingVoidEntity;
import com.yuo.endless.EndlessUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

public class GapingVoidRender extends EntityRenderer<GapingVoidEntity> {
    private static final ResourceLocation VOID = EndlessUtils.fa("textures/entity/void.png");
    private static final ResourceLocation VOID1 = EndlessUtils.fa("textures/entity/void_halo.png");
    private static final RenderType VOID_HALO = RenderType.create("endless:void_halo", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256,
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(VOID1, false, false))
                    .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setWriteMaskState(RenderType.COLOR_WRITE).createCompositeState(false));
    private static final RenderType VOID_HEMISPHERE = RenderType.create("endless:void_hemisphere", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 256,
            RenderType.CompositeState.builder().setShaderState(RenderType.RENDERTYPE_ENTITY_SHADOW_SHADER).setTextureState(new RenderStateShard.TextureStateShard(VOID, false, false))
                    .setCullState(RenderType.NO_CULL).createCompositeState(false));
    private final CCModel hemisphere;

    public GapingVoidRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.hemisphere = new OBJParser(EndlessUtils.fa("models/hemisphere.obj")).parse().get("model");
    }

    @Override
    public void render(GapingVoidEntity entityIn, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        float age = entityIn.getAge() + partialTicks;
        Colour colour = getColour(age); // 光环颜色
        float scale = GapingVoidEntity.getVoidScale(age);
        double haloCord = 0.58D * scale;
        double haloScaleDist = 2.2D * scale;
        Vec3 cam = this.entityRenderDispatcher.camera.getPosition();
        double dx = entityIn.getX() - cam.x();
        double dy = entityIn.getY() - cam.y();
        double dz = entityIn.getZ() - cam.z();
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len <= haloScaleDist) {
            double close = (haloScaleDist - len) / haloScaleDist;
            haloCord *= 1.0D + close * close * close * close * 1.5D;
        }
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees((float) (Math.atan2(dx, dz) * 57.29577951308232f)));
        stack.mulPose(Axis.XP.rotationDegrees((float) (Math.atan2(Math.sqrt(dx * dx + dz * dz), dy) * 57.29577951308232D + 90.0D)));

        //外部光环
        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(90.0F));
//        if (age < 140) renderVoidHalo(stack, bufferIn, haloCord, colour);
        stack.popPose();

        //shader黑洞
//        if (age >= 140) {
//             renderGapingVoid(stack, bufferIn, (float) haloCord, age / 20.0F);
//        }

        //内部球体
//        if (age < 140)
//            renderVoidHemisphere(stack, bufferIn, scale, colour);
        renderGapingVoid(stack, bufferIn, (float) haloCord * 0.5f, age / 20.0F, colour);
        stack.popPose();
    }

    /**
     * 世界/视空间吸积盘渲染：
     * 一个面向相机的 billboard，fragment shader 根据视空间射线方向算吸积盘和视界阴影。
     * 不拷贝屏幕。
     */
    private void renderGapingVoid(PoseStack stack, MultiBufferSource bufferIn, float horizonRadiusValue, float timeValue, Colour colour) {
        if (DistortShaders.gapingVoidShader == null) return;

        // HoleCenter：实体 + 相机
        Matrix4f pose = stack.last().pose();
        Matrix4f modelView = RenderSystem.getModelViewMatrix();
        Matrix4f fullTransform = new Matrix4f(modelView).mul(pose);
        Vector4f c = new Vector4f(0.0F, 0.0F, 0.0F, 1.0F).mul(fullTransform);
        Vector3f centerView = new Vector3f(c.x, c.y, c.z);

// DiskNormal：世界空间水平面，只经过相机变换
        Vector4f n = new Vector4f(0.12F, 1.0F, 0.24F, 0.0F).mul(modelView);
        Vector3f normalView = new Vector3f(n.x, n.y, n.z).normalize();

// DiskAxis：世界空间参考轴，只经过相机变换
        Vector4f a = new Vector4f(1.0F, -0.12F, 0.0F, 0.0F).mul(modelView);
        Vector3f axisView = new Vector3f(a.x, a.y, a.z).normalize();

        DistortShaders.holeCenter.set(centerView.x, centerView.y, centerView.z);
        DistortShaders.diskNormal.set(normalView.x, normalView.y, normalView.z);
        DistortShaders.diskAxis.set(axisView.x, axisView.y, axisView.z);
        DistortShaders.horizonRadius.set(horizonRadiusValue);
        DistortShaders.time.set(timeValue);

        float size = horizonRadiusValue * 3.0F;

        VertexConsumer cons = new TransformingVertexConsumer(bufferIn.getBuffer(DistortShaders.GAPING_VOID_RENDER_TYPE), stack);

        cons.vertex(-size, -size, 0.0F).color(colour.r, colour.g, colour.b, 0.5F).uv(0F, 0F).uv2(235,120).normal(0F, 0F, 1F).endVertex();
        cons.vertex( size, -size, 0.0F).color(colour.r, colour.g, colour.b, 0.5F).uv(1F, 0F).uv2(235,120).normal(0F, 0F, 1F).endVertex();
        cons.vertex( size,  size, 0.0F).color(colour.r, colour.g, colour.b, 0.5F).uv(1F, 1F).uv2(235,120).normal(0F, 0F, 1F).endVertex();
        cons.vertex(-size,  size, 0.0F).color(colour.r, colour.g, colour.b, 0.5F).uv(0F, 1F).uv2(235,120).normal(0F, 0F, 1F).endVertex();
    }

    private void renderVoidHalo(PoseStack stack, MultiBufferSource bufferIn, double haloCord, Colour colour) {
        TransformingVertexConsumer consHalo = new TransformingVertexConsumer(bufferIn.getBuffer(VOID_HALO), stack);
        consHalo.vertex(-haloCord, 0.0D, -haloCord).color(colour.r, colour.g, colour.b, colour.a).uv(0.0F, 0.0F).endVertex();
        consHalo.vertex(-haloCord, 0.0D, haloCord).color(colour.r, colour.g, colour.b, colour.a).uv(0.0F, 1.0F).endVertex();
        consHalo.vertex(haloCord, 0.0D, haloCord).color(colour.r, colour.g, colour.b, colour.a).uv(1.0F, 1.0F).endVertex();
        consHalo.vertex(haloCord, 0.0D, -haloCord).color(colour.r, colour.g, colour.b, colour.a).uv(1.0F, 0.0F).endVertex();
    }

    private void renderVoidHemisphere(PoseStack stack, MultiBufferSource bufferIn, float scale, Colour colour) {
        stack.scale(scale, scale, scale);
        CCRenderState cc = CCRenderState.instance();
        cc.reset();
        cc.bind(VOID_HEMISPHERE, bufferIn, stack);
        cc.baseColour = colour.rgba();
        this.hemisphere.render(cc);
    }

    private static Colour getColour(double age) {
        double life = age / 186.0D;
        double f = Math.max(0.0, (life - 0.95) / 0.05);
        f = Math.max(f, 1.0 - life * 30.0);
        return new ColourRGBA(f, f, f, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(GapingVoidEntity entity) {
        return VOID;
    }
}
