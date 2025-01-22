package com.yuo.endless.Client.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yuo.endless.Client.Model.SwordVoidModel;
import com.yuo.endless.Endless;
import com.yuo.endless.Entity.SwordVoidEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class SwordVoidRender extends EntityRenderer<SwordVoidEntity> {
    private final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/entity/sword.png");
    private final EntityModel<Entity> model;

    public SwordVoidRender(EntityRendererManager renderer) {
        super(renderer);
        this.model = new SwordVoidModel();
    }

    @Override
    public void render(SwordVoidEntity entityIn, float entityYaw, float partialTicks, MatrixStack pStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, pStack, bufferIn, packedLightIn);
        pStack.push();
        IVertexBuilder buffer = bufferIn.getBuffer(model.getRenderType(getEntityTexture(entityIn)));
        pStack.translate(0,0,0.5);
        model.render(pStack, buffer, packedLightIn, OverlayTexture.NO_OVERLAY, 1f,1f,1f,1f);
//        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
//        int time = 300;
//        pStack.translate(0,0,0);
//        float scale = 10; //time / 300f * 10;
//        pStack.scale(scale, scale, scale);
//        pStack.rotate(new Quaternion(Vector3f.ZN, (float) (Math.PI / 4 + Math.PI), false));
//        renderer.renderItem(new ItemStack(EndlessItems.infinitySword.get()), TransformType.FIXED, packedLightIn, packedLightIn, pStack, bufferIn);
        pStack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(SwordVoidEntity swordVoidEntity) {
        return TEXTURE;
    }
}
