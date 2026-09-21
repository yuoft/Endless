package com.yuo.endless.client.render;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.yuo.endless.client.lib.*;
import com.yuo.endless.config.ModConfig;
import com.yuo.endless.entity.GapingVoidEntity;
import com.yuo.endless.EndlessUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

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
        boolean flag = ModConfig.SERVER.isVoidNewRender.get();
        stack.mulPose(Axis.XP.rotationDegrees(90.0F));
        if (!flag) renderVoidHalo(stack, bufferIn, haloCord, colour);
        stack.popPose();

        //shader黑洞
        if (flag) GapingVoidShaders.renderDirect(stack, (float) haloCord, age / 20.0F);

        //内部球体
        if (!flag) renderVoidHemisphere(stack, bufferIn, scale, colour);
        stack.popPose();
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
