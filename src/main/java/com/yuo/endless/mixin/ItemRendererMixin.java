package com.yuo.endless.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.client.model.IItemRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Inject(method = "render"
            , at = {@At("HEAD")}, cancellable = true)
    public void onRenderItem(ItemStack pItemStack, ItemDisplayContext pTransformType, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int f1, int f2, BakedModel model, CallbackInfo ci) {
        if (model instanceof IItemRenderer) {
            ci.cancel();
            pPoseStack.pushPose();
            IItemRenderer renderer = (IItemRenderer) ForgeHooksClient.handleCameraTransforms(pPoseStack, model, pTransformType, pLeftHand);
            pPoseStack.translate(-0.5D, -0.5D, -0.5D);
            renderer.renderItem(pItemStack, pTransformType, pPoseStack, pBuffer, f1, f2);
            pPoseStack.popPose();
        }
    }
}

