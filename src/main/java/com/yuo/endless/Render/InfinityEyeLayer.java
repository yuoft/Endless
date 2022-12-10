package com.yuo.endless.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;

public class InfinityEyeLayer extends AbstractEyesLayer<PlayerEntity, PlayerModel<PlayerEntity>> {
    private static final RenderType RENDER_TYPE = RenderType.getEyes(new ResourceLocation(Endless.MOD_ID, "textures/models/alex.png"));

    public InfinityEyeLayer(IEntityRenderer<PlayerEntity, PlayerModel<PlayerEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, PlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
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

    @Override
    public RenderType getRenderType() {
        return RENDER_TYPE;
    }

    private boolean isInfinityHead(PlayerEntity player){
        return player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == EndlessItems.infinityHead.get();
    }
}
