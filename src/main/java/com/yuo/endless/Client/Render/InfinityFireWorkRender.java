package com.yuo.endless.Client.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.yuo.endless.Entity.InfinityFireWorkEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InfinityFireWorkRender extends EntityRenderer<InfinityFireWorkEntity> {
    private final ItemRenderer itemRenderer;

    public InfinityFireWorkRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public ResourceLocation getTextureLocation(InfinityFireWorkEntity infinityFireWorkEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public void render(InfinityFireWorkEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        if (entityIn.isShotAtAngle()) {
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90.0F));
        }

        this.itemRenderer.renderStatic(entityIn.getItem(), TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, entityIn.getId());
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }
}
