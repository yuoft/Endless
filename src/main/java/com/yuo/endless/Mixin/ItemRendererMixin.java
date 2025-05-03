package com.yuo.endless.Mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.Client.Model.IItemRenderer;
import com.yuo.endless.Event.SoundEvent;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {


    @Shadow @Final private BlockEntityWithoutLevelRenderer blockEntityRenderer;

    /**
     * @author yuo
     * @reason 修改无尽箱子中的物品数量显示
     */
    @ModifyVariable(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("STORE"), ordinal = 1)
    private String injected(String x) {
        if (SoundEvent.IS_INFINITY_CHEST){
            String s = x.replaceAll("\\D", ""); //去除非数字
            if (s.isEmpty()) {
                s = "1";
            }
            return endless$getSimplifiedCount(Integer.parseInt(s));
        }
        return x;
    }

    @Unique
    @OnlyIn(Dist.CLIENT)
    public String endless$getSimplifiedCount(int count) {
        if (count > 0 && count < 1_000)
            return Integer.toString(count);
        else if (count >= 1_000 && count < 1_000_000)
            return count / 1_000 + "K";
        else if (count >= 1_000_000L && count < 1_000_000_000)
            return count / 1_000_000 + "M";
        else if (count >= 1_000_000_000)
            return count / 1_000_000_000 + "B";
        else return Integer.toString(count);
    }

    @Inject(method = "render"
            , at = {@At("HEAD")}, cancellable = true)
    public void onRenderItem(ItemStack pItemStack, TransformType pTransformType, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int f1, int f2, BakedModel model, CallbackInfo ci) {
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

