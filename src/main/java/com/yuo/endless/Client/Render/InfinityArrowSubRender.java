package com.yuo.endless.Client.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.yuo.endless.Endless;
import com.yuo.endless.Entity.InfinityArrowSubEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class InfinityArrowSubRender extends ArrowRenderer<InfinityArrowSubEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/entity/infinity_arrow_sub.png");

    private EntityModel<InfinityArrowSubEntity> arrowSub;

    public InfinityArrowSubRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getTextureLocation(InfinityArrowSubEntity infinityArrowSubEntity) {
        return TEXTURE;
    }

    @Override
    public void render(InfinityArrowSubEntity arrowEntity, float entityYaw, float partialTicks, PoseStack mStack, MultiBufferSource getter, int packedLightIn) {
        mStack.pushPose();
        mStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, arrowEntity.yRotO, arrowEntity.getYRot()) - 90.0F));
        mStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, arrowEntity.xRotO, arrowEntity.getXRot())));
        float f9 = (float)arrowEntity.shakeTime - partialTicks;
        if (f9 > 0.0F) {
            float f10 = -Mth.sin(f9 * 3.0F) * f9;
            mStack.mulPose(Vector3f.ZP.rotationDegrees(f10));
        }

        mStack.mulPose(Vector3f.XP.rotationDegrees(45.0F));
        mStack.scale(0.05625F, 0.05625F, 0.05625F);
        mStack.translate(-4.0, 0.0, 0.0);
        VertexConsumer ivertexbuilder = getter.getBuffer(RenderType.entityCutout(this.getTextureLocation(arrowEntity)));
        PoseStack.Pose matrixstack$entry = mStack.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLightIn);

        for(int j = 0; j < 4; ++j) {
            mStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLightIn);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLightIn);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLightIn);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLightIn);
        }

        mStack.popPose();
        super.render(arrowEntity, entityYaw, partialTicks, mStack, getter, packedLightIn);
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, int offsetX, int offsetY, int offsetZ, float textureX, float textureY, int p_229039_9_, int p_229039_10_, int p_229039_11_, int packedLightIn) {
        vertexBuilder.vertex(matrix, (float)offsetX, (float)offsetY, (float)offsetZ).color(255, 255, 255, 255).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, (float)p_229039_9_, (float)p_229039_11_, (float)p_229039_10_).endVertex();
    }
}