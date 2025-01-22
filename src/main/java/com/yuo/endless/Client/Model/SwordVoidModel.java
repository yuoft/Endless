package com.yuo.endless.Client.Model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class SwordVoidModel extends EntityModel<Entity> {
	private final ModelRenderer sword;
	private final ModelRenderer r1;

	public SwordVoidModel() {
		textureWidth = 32;
		textureHeight = 32;

		sword = new ModelRenderer(this);
		sword.setRotationPoint(0.0F, 16.0F, 0.0F);


		r1 = new ModelRenderer(this);
		r1.setRotationPoint(0.0F, 8.0F, -8.0F);
		sword.addChild(r1);
		setRotationAngle(r1, 0.0F, 0.0F, 0.7854F);
		r1.setTextureOffset(12, 12).addBox(-2.0F, -3.0F, 0.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(0, 18).addBox(-10.0F, -4.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(16, 3).addBox(-3.0F, -4.0F, 0.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(16, 16).addBox(-9.0F, -5.0F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(12, 16).addBox(-4.0F, -5.0F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(8, 13).addBox(-6.0F, -8.0F, 0.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(4, 17).addBox(-4.0F, -8.0F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(8, 0).addBox(-7.0F, -9.0F, 0.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(4, 4).addBox(-5.0F, -9.0F, 0.0F, 1.0F, 6.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(0, 4).addBox(-8.0F, -10.0F, 0.0F, 1.0F, 7.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(16, 0).addBox(-4.0F, -10.0F, 0.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(12, 6).addBox(-9.0F, -11.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(16, 10).addBox(-14.0F, -12.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(12, 0).addBox(-10.0F, -12.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(16, 8).addBox(-15.0F, -13.0F, 0.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(0, 12).addBox(-11.0F, -13.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(4, 11).addBox(-12.0F, -14.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(8, 7).addBox(-13.0F, -15.0F, 0.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
		r1.setTextureOffset(0, 0).addBox(-16.0F, -16.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		sword.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}