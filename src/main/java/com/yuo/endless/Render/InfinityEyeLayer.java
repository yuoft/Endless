package com.yuo.endless.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InfinityEyeLayer<T extends PlayerEntity> extends AbstractEyesLayer<T, BipedModel<T>> {
    private static final RenderType RENDER_TYPE = RenderType.getEyes(new ResourceLocation(Endless.MOD_ID, "textures/models/alex.png"));

    public InfinityEyeLayer(IEntityRenderer<T, BipedModel<T>> renderer) {
        super(renderer);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (isInfinityHead(player) && Config.CLIENT.isRenderEye.get()){
            matrixStackIn.push();
            IVertexBuilder builder = bufferIn.getBuffer(this.getRenderType());
            matrixStackIn.translate(0, -0.0245, 0);
            matrixStackIn.scale(1.25f, 1.25f, 1.25f);
            float xAngle = 0;
            float yAngle = 0;
            float zAngle = 0;
            float swimAnimation = player.getSwimAnimation(partialTicks);
            boolean flag = player.getTicksElytraFlying() > 4;
            boolean flag1 = player.isActualySwimming();
            yAngle = netHeadYaw * ((float)Math.PI / 180F);
            if (flag) {
                xAngle = (-(float)Math.PI / 4F);
            } else if (swimAnimation > 0.0F) {
                if (flag1) {
                    xAngle = this.rotLerpRad(swimAnimation, xAngle, (-(float)Math.PI / 4F));
                } else {
                    xAngle = this.rotLerpRad(swimAnimation, xAngle, headPitch * ((float)Math.PI / 180F));
                }
            } else {
                xAngle = headPitch * ((float)Math.PI / 180F);
            }
//            float v = ((float) Math.PI / 180F) * -MathHelper.interpolateAngle(partialTicks, netHeadYaw, headPitch);
//            matrixStackIn.rotate(new Quaternion(0, v, 0, true));
            matrixStackIn.rotate(new Quaternion(xAngle, yAngle, zAngle, true));
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

    private float rotLerpRad(float angleIn, float maxAngleIn, float mulIn) {
        float f = (mulIn - maxAngleIn) % ((float)Math.PI * 2F);
        if (f < -(float)Math.PI) {
            f += ((float)Math.PI * 2F);
        }

        if (f >= (float)Math.PI) {
            f -= ((float)Math.PI * 2F);
        }

        return maxAngleIn + angleIn * f;
    }
}
