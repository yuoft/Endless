package com.yuo.endless.Client.Render;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.ModelHelper;
import codechicken.lib.render.model.ModelMaterial;
import codechicken.lib.render.model.OBJParser;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import com.yuo.endless.Endless;
import com.yuo.endless.Entity.GapingVoidEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GapingVoidRender extends EntityRenderer<GapingVoidEntity> {
    private static final ResourceLocation VOID = new ResourceLocation(Endless.MOD_ID, "textures/entity/void.png");

    private final CCModel hemisphere;

//    private static final RenderType VOID_HALO = RenderType.makeType("endless:void_halo", DefaultVertexFormats.POSITION_COLOR_TEX, 7, 256,
//            RenderType.State.getBuilder().diffuseLighting(RenderState.DIFFUSE_LIGHTING_DISABLED).alpha(RenderState.ZERO_ALPHA).shadeModel(RenderType.SHADE_ENABLED)
//            .texture(new RenderState.TextureState(new ResourceLocation("endless", "textures/entity/void_halo.png"), false, false))
//                    .transparency(RenderType.TRANSLUCENT_TRANSPARENCY).writeMask(RenderType.COLOR_WRITE).build(false));
//
//    private static final RenderType VOID_HEMISPHERE = RenderType.makeType("endless:void_hemisphere", DefaultVertexFormats.ENTITY, 4,
//            256, RenderType.State.getBuilder().diffuseLighting(RenderState.DIFFUSE_LIGHTING_DISABLED).alpha(RenderState.ZERO_ALPHA)
//                    .shadeModel(RenderType.SHADE_DISABLED).texture(new RenderState.TextureState(VOID, false, false)).cull(RenderType.CULL_DISABLED).build(false));

    public GapingVoidRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.hemisphere = null;//OBJParser.parse(new ResourceLocation("endless", "models/hemisphere.obj")).get("model");
    }

    @Override
    public void render(GapingVoidEntity entityIn, float entityYaw, float partialTicks, PoseStack pStack, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, pStack, bufferIn, packedLightIn);
        double age = (entityIn.getAge() + partialTicks);
        ColourRGBA colour = getColour(age);
        double scale = GapingVoidEntity.getVoidScale(age);
        double halo_coord = 0.58D * scale;
        double halo_scale_dist = 2.2D * scale;
//        Vector3d camera = (getRenderManager()).info.getProjectedView();
//        double dx = entityIn.getPosX() - camera.x;
//        double dy = entityIn.getPosY() - camera.y;
//        double dz = entityIn.getPosZ() - camera.z;
//        double xzlen = Math.sqrt(dx * dx + dz * dz);
//        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
//        if (len <= halo_scale_dist) {
//            double close = (halo_scale_dist - len) / halo_scale_dist;
//            halo_coord *= 1.0D + close * close * close * close * 1.5D;
//        }
//        double yang = Math.atan2(xzlen, dy) * 57.29577951308232D;
//        double xang = Math.atan2(dx, dz) * 57.29577951308232D;
//        RenderType.endPortal();
//        pStack.pushPose();
//        pStack.mulPose(new Quaternion(Vector3f.YP, (float)xang, true));
//        pStack.mulPose(new Quaternion(Vector3f.XP, (float)(yang + 90.0D), true));
//        pStack.pushPose();
//        pStack.mulPose(new Quaternion(Vector3f.XP, 90.0F, true));
//        TransformingVertexBuilder transformingVertexBuilder = new TransformingVertexBuilder(bufferIn.getBuffer(VOID_HALO), pStack);
//        transformingVertexBuilder.pos(-halo_coord, 0.0D, -halo_coord).color(colour.r, colour.g, colour.b, colour.a).tex(0.0F, 0.0F).endVertex();
//        transformingVertexBuilder.pos(-halo_coord, 0.0D, halo_coord).color(colour.r, colour.g, colour.b, colour.a).tex(0.0F, 1.0F).endVertex();
//        transformingVertexBuilder.pos(halo_coord, 0.0D, halo_coord).color(colour.r, colour.g, colour.b, colour.a).tex(1.0F, 1.0F).endVertex();
//        transformingVertexBuilder.pos(halo_coord, 0.0D, -halo_coord).color(colour.r, colour.g, colour.b, colour.a).tex(1.0F, 0.0F).endVertex();
//        pStack.popPose();
//        pStack.scale((float)scale, (float)scale, (float)scale);
//        CCRenderState cc = CCRenderState.instance();
//        cc.reset();
//        cc.bind(VOID_HEMISPHERE, bufferIn, pStack);
//        cc.baseColour = colour.rgba();
//        this.hemisphere.render(cc);
        pStack.popPose();
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
