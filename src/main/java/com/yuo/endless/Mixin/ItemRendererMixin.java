package com.yuo.endless.Mixin;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {


    /**
     * @author yuo
     * @reason 修改无尽箱子中的物品数量显示
     */
    @ModifyArgs(method = "renderItemOverlayIntoGUI", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZLnet/minecraft/util/math/vector/Matrix4f;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ZII)I"))
    public void renderItemOverlayIntoGUI(Args args, FontRenderer renderer, ItemStack stack, int x, int y, String name){
        String count = endless$getSimplifiedCount(stack.getCount());
        args.set(0, count);
        args.set(1, (float)(x + 19 - 2 - renderer.getStringWidth(count)));
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
}
