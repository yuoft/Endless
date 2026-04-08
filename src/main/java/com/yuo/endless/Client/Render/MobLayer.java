package com.yuo.endless.Client.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Client.Model.InfinityArmorModel;
import com.yuo.endless.EndlessUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;

public class MobLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public MobLayer(LivingEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        // 计算着色器参数
        float yaw = (float) (mc.player.getYRot() * 2.0f * Math.PI / 360.0);
        float pitch = -(float) (mc.player.getXRot() * 2.0f * Math.PI / 360.0);
        float scale = 1.0f;

        AvaritiaShaders.cosmicTime.set((float)(System.currentTimeMillis() - (long) AvaritiaShaders.renderTime) / 2000.0F);
        AvaritiaShaders.cosmicYaw.set(yaw);
        AvaritiaShaders.cosmicPitch.set(pitch);
        AvaritiaShaders.cosmicExternalScale.set(scale);
        AvaritiaShaders.cosmicOpacity.set(1.0F);

        for(int i = 0; i < 10; ++i) {
            TextureAtlasSprite sprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                    .apply(EndlessUtils.fa( "shader/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }

        if (AvaritiaShaders.cosmicUVs != null) {
            AvaritiaShaders.cosmicUVs.set(AvaritiaShaders.COSMIC_UVS);
        }

        poseStack.pushPose();

        // 稍微放大一点避免深度冲突
        poseStack.scale(1.02f, 1.02f, 1.02f);

        // 渲染模型（使用星空着色器）
        this.getParentModel().renderToBuffer(poseStack, InfinityArmorModel.material(InfinityArmorModel.MASK_INV).buffer(buffer, InfinityArmorModel::mask2), packedLight,1, 0.84f, 1.0f, 0.95f, 0.8f);

        poseStack.popPose();
    }

}