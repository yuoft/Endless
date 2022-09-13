package com.yuo.endless.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class InfinityWingModel extends EntityModel<PlayerEntity> {
    private final ModelRenderer wing;

    public InfinityWingModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.wing = new ModelRenderer(this);
        this.wing.addBox(-32.0F, -12.0F, 12.0F, 64.0F, 32.0F, 0.0F, 0.0F, false);
        this.wing.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.wing.setTextureOffset(0, 0);
    }

    @Override
    public void setRotationAngles(PlayerEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        Vector3d lookVec = entityIn.getLookVec();
        this.wing.rotateAngleX = (float) ((Math.PI / 180F) * lookVec.x);
        this.wing.rotateAngleY = (float) ((Math.PI / 180F) * lookVec.y);
        this.wing.rotateAngleZ = (float) ((Math.PI / 180F) * lookVec.z);
    }

    @Override
    public void setLivingAnimations(PlayerEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.wing.rotateAngleX = 0.0F;
        this.wing.rotateAngleY = ((float)Math.PI / 180F) * -MathHelper.interpolateAngle(partialTick, entityIn.prevRotationYaw, entityIn.rotationYaw);
        this.wing.rotateAngleZ = 0.0F;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.wing.render(matrixStackIn, bufferIn, packedLightIn, packedLightIn, red, green, blue, alpha);
    }
}
