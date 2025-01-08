package com.yuo.endless.Client.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.Endless;
import com.yuo.endless.Entity.InfinityArrowSubEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

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
    public void render(InfinityArrowSubEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        /*
        matrixStackIn.push();
        float f = entityIn.getHeight() + 0.5F; //高度
        matrixStackIn.translate(0.0D, (double)f, 0.0D);
        Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
        float f1 = Minecraft.getInstance().gameSettings.getTextBackgroundOpacity(0.25F);
        int j = (int)(f1 * 255.0F) << 24;
        FontRenderer fontRenderer = this.getFontRendererFromRenderManager();
//        entityIn.setGlowing(true); //发光效果
        matrixStackIn.rotate(this.renderManager.getCameraOrientation());
        matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
        ITextComponent name = new StringTextComponent(entityIn.getName().getString() + "YUO"); //名称
        boolean flag = !entityIn.isDiscrete(); //实体是独立的
        int i = "deadmau5".equals(name.getString()) ? -10 : 0;
        float f2 = (float)(-fontRenderer.getStringPropertyWidth(name) / 2); //名称长度
        fontRenderer.func_243247_a(name, f2, (float)i, 553648127, false, matrix4f, bufferIn, flag, j, packedLightIn);
        matrixStackIn.pop();
        */
    }
}