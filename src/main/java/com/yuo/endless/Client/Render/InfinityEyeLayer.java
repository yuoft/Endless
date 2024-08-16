package com.yuo.endless.Client.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yuo.endless.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;

public class InfinityEyeLayer extends AbstractEyesLayer<LivingEntity, BipedModel<LivingEntity>> {
    private static final RenderType RENDER_TYPE = RenderType.getEyes(new ResourceLocation(Endless.MOD_ID, "textures/models/steve_eye.png"));

    public InfinityEyeLayer(IEntityRenderer<LivingEntity, BipedModel<LivingEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, LivingEntity living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (living instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) living;
            if (isInfinityHead(player) && Config.CLIENT.isRenderEye.get()){
                matrixStackIn.push();
                IVertexBuilder builder = bufferIn.getBuffer(this.getRenderType());
                matrixStackIn.translate(0, -0.025, -0.00001);
                matrixStackIn.scale(1.25f, 1.25f, 1.25f);
                float v = ((float) Math.PI / 180F) * -MathHelper.interpolateAngle(partialTicks, netHeadYaw, headPitch);
                matrixStackIn.rotate(new Quaternion(0, v,0, true));
                this.getEntityModel().render(matrixStackIn, builder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStackIn.pop();
            }
        }
    }

    @Override
    public RenderType getRenderType() {
        return RENDER_TYPE;
    }

    private boolean isInfinityHead(PlayerEntity player){
        return player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == EndlessItems.infinityHead.get();
    }
}
