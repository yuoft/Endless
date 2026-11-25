package com.yuo.endless.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.client.AvaritiaShaders;
import com.yuo.endless.client.model.InfinityArmorModel;
import com.yuo.endless.compat.oculus.MobLateRenderQueue;
import com.yuo.endless.compat.oculus.OculusCompat;
import com.yuo.endless.compat.oculus.RenderFrameState;
import com.yuo.endless.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

public class MobLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public MobLayer(LivingEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean oculusActive = OculusCompat.isShaderPackActive();
        boolean shadowPass   = oculusActive && RenderFrameState.isShadowPass();
        boolean deferPath    = oculusActive && !shadowPass && RenderFrameState.shouldDeferWorldEffect();

        if (shadowPass) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        // 计算着色器参数
        float yaw = (float) (mc.player.getYRot() * 2.0f * Math.PI / 360.0);
        float pitch = -(float) (mc.player.getXRot() * 2.0f * Math.PI / 360.0);
        float scale = 1.0f;

        // 星空 UV + uniform 统一走 AvaritiaShaders，未就绪/图集异常时放弃本次叠加
        if (!AvaritiaShaders.applyCosmicUniforms(yaw, pitch, scale,  ModConfig.SERVER.isOldCosmicRender.get() ? 1.0F : 2.0F)) {
            return;
        }

        poseStack.pushPose();

        // 稍微放大一点避免深度冲突
        poseStack.scale(1.02f, 1.02f, 1.02f);

        if (deferPath)
            MobLateRenderQueue.enqueue(this.getParentModel(), poseStack, entity, packedLight, 1);
        // 渲染模型（使用星空着色器）
        else this.getParentModel().renderToBuffer(poseStack, InfinityArmorModel.material(InfinityArmorModel.MASK_INV).buffer(buffer, EndlessRenderTypes::mask2), packedLight,1, 0.84f, 1.0f, 0.95f, 0.8f);

        poseStack.popPose();
    }

}