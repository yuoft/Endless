package com.yuo.endless.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yuo.endless.Endless;
import com.yuo.endless.Entity.GapingVoidEntity;
import com.yuo.endless.Entity.GapingVoidModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class GapingVoidRender extends EntityRenderer<GapingVoidEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/entity/void.png");

    private final EntityModel<Entity> gapingVoid;

    public GapingVoidRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.gapingVoid = new GapingVoidModel();
    }

    @Override
    public ResourceLocation getEntityTexture(GapingVoidEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(GapingVoidEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.push();
        IVertexBuilder builder = bufferIn.getBuffer(this.gapingVoid.getRenderType(this.getEntityTexture(entityIn)));
        float scale = GapingVoidEntity.getVoidScale(entityIn.getAge()); //缩放值
        matrixStackIn.scale(scale, scale, scale); //缩放模型
        matrixStackIn.translate(0, -scale * 0.11d,0);
        this.gapingVoid.render(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f,1.0f,1.0f,1.0f);
        matrixStackIn.pop();
    }
}
