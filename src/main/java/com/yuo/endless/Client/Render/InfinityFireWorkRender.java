package com.yuo.endless.Client.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yuo.endless.Entity.InfinityFireWorkEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InfinityFireWorkRender extends EntityRenderer<InfinityFireWorkEntity> {
    private final ItemRenderer itemRenderer;

    public InfinityFireWorkRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(InfinityFireWorkEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.rotate(this.renderManager.getCameraOrientation());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
        if (entityIn.func_213889_i()) {
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
        }

        this.itemRenderer.renderItem(entityIn.getItem(), ItemCameraTransforms.TransformType.GROUND, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getEntityTexture(InfinityFireWorkEntity entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}
