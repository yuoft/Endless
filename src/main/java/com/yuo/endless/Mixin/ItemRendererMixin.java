package com.yuo.endless.Mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yuo.endless.Client.Model.IItemRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {


    /**
     * @author yuo
     * @reason 修改无尽箱子中的物品数量显示
     */
    @ModifyVariable(method = "renderItemOverlayIntoGUI", at = @At("STORE"), ordinal = 1)
    private String injected(String x) {
        String s = x.replaceAll("\\D", ""); //去除非数字
        return endless$getSimplifiedCount(Integer.parseInt(s));
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

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/model/ItemCameraTransforms$TransformType;ZLcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;IILnet/minecraft/client/renderer/model/IBakedModel;)V"
            , at = {@At("HEAD")}, cancellable = true)
    public void onRenderItem(ItemStack stack, ItemCameraTransforms.TransformType type, boolean left, MatrixStack m, IRenderTypeBuffer get, int f1, int f2, IBakedModel model, CallbackInfo ci) {
        if (model instanceof IItemRenderer) {
            ci.cancel();
            m.push();
            IItemRenderer renderer = (IItemRenderer) ForgeHooksClient.handleCameraTransforms(m, model, type, left);
            m.translate(-0.5D, -0.5D, -0.5D);
            renderer.renderItem(stack, type, m, get, f1, f2);
            m.pop();
        }
    }
}

