package com.yuo.endless.Client.Render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.yuo.endless.Client.Lib.*;
import com.yuo.endless.Entity.GapingVoidEntity;
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

    private final CCModel hemisphere;

    private static final RenderType VOID_HALO = RenderType.create("endless:void_halo", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256,
            RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(VOID1, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
            .setWriteMaskState(RenderType.COLOR_WRITE).createCompositeState(false));

    private static final RenderType VOID_HEMISPHERE = RenderType.create("endless:void_hemisphere", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES,
            256, RenderType.CompositeState.builder().setShaderState(RenderType.RENDERTYPE_ENTITY_ALPHA_SHADER).setTextureState(new RenderStateShard.TextureStateShard(VOID, false, false))
                    .setCullState(RenderType.NO_CULL).createCompositeState(false));
    public GapingVoidRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.hemisphere = new OBJParser(EndlessUtils.fa("models/hemisphere.obj")).parse().get("model");
    }

    @Override
    public void render(GapingVoidEntity entityIn, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        float age = entityIn.getAge() + partialTicks;
        Colour colour = getColour(age);
        double scale = GapingVoidEntity.getVoidScale(age);
        double halocoord = 0.58D * scale;
        double haloscaledist = 2.2D * scale;
        Vec3 cam = this.entityRenderDispatcher.camera.getPosition();
        double dx = entityIn.getX() - cam.x();
        double dy = entityIn.getY() - cam.y();
        double dz = entityIn.getZ() - cam.z();
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len <= haloscaledist) {
            double close = (haloscaledist - len) / haloscaledist;
            halocoord *= 1.0D + close * close * close * close * 1.5D;
        }
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees((float) (Math.atan2(dx, dz) * 57.29577951308232f)));
        stack.mulPose(Axis.XP.rotationDegrees((float)(Math.atan2(Math.sqrt(dx * dx + dz * dz), dy) * 57.29577951308232D + 90.0D)));
        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(90.0F));
        TransformingVertexConsumer cons = new TransformingVertexConsumer(bufferIn.getBuffer(VOID_HALO), stack);
        cons.vertex(-halocoord, 0.0D, -halocoord).color(colour.r, colour.g, colour.b, colour.a).uv(0.0F, 0.0F).endVertex();
        cons.vertex(-halocoord, 0.0D, halocoord).color(colour.r, colour.g, colour.b, colour.a).uv(0.0F, 1.0F).endVertex();
        cons.vertex(halocoord, 0.0D, halocoord).color(colour.r, colour.g, colour.b, colour.a).uv(1.0F, 1.0F).endVertex();
        cons.vertex(halocoord, 0.0D, -halocoord).color(colour.r, colour.g, colour.b, colour.a).uv(1.0F, 0.0F).endVertex();
        stack.popPose();
        stack.scale((float)scale, (float)scale, (float)scale);
        CCRenderState cc = CCRenderState.instance();
        cc.reset();
        cc.bind(VOID_HEMISPHERE, bufferIn, stack);
        cc.baseColour = colour.rgba();
        hemisphere.render(cc);
        stack.popPose();
    }

    private static ColourRGBA getColour(double age) {
        double life = age / 186.0D;
        double f = Math.max(0.0D, (life - 0.95D) / 0.050000000000000044D);
        f = Math.max(f, 1.0D - life * 30.0D);
        return new ColourRGBA(f, f, f, 1.0);
    }

    @Override
    public ResourceLocation getTextureLocation(GapingVoidEntity entity) {
        return VOID;
    }
}
