package com.yuo.endless.Mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.Event.SoundEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ItemDecoratorHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {


    @Shadow @Final private PoseStack pose;
    @Shadow @Final private Minecraft minecraft;

    @Shadow public abstract int drawString(Font p_283343_, @Nullable String p_281896_, int p_283569_, int p_283418_, int p_281560_, boolean p_282130_);
    @Shadow public abstract void fill(RenderType p_286602_, int p_286738_, int p_286614_, int p_286741_, int p_286610_, int p_286560_);

    @Inject(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = {@At("HEAD")}, cancellable = true)
    private void renderItemDecorations(Font p_282005_, ItemStack p_283349_, int p_282641_, int p_282146_, String p_282803_, CallbackInfo ci){
        if (SoundEvent.IS_INFINITY_CHEST){
            ci.cancel();
            /*
            if (!stack.isEmpty()) {
                this.pose.pushPose();
                if (stack.getCount() != 1) {
                    String s = getItemCount(stack);
                    RenderSystem.enableDepthTest();
                    this.pose.pushPose();
                    float fontSize = 0.5F;
                    this.pose.translate(0.0f, 0.0f, 300.0D);
                    this.pose.scale(fontSize, fontSize, 1.0F);
                    this.drawString(font, s,
                            (int) ((16 -font.width(s) * fontSize) / fontSize),
                            (int) ((16 - font.lineHeight * fontSize) / fontSize),
                            16777215, true);
                    this.pose.popPose();
                }

                int i1;
                int j1;
                if (stack.isBarVisible()) {
                    int l = stack.getBarWidth();
                    int i = stack.getBarColor();
                    i1 = i + 2;
                    j1 = j + 13;
                    this.fill(RenderType.guiOverlay(), i1, j1, i1 + 13, j1 + 2, -16777216);
                    this.fill(RenderType.guiOverlay(), i1, j1, i1 + l, j1 + 1, i | -16777216);
                }

                LocalPlayer localplayer = this.minecraft.player;
                float f = localplayer == null ? 0.0F : localplayer.getCooldowns().getCooldownPercent(stack.getItem(), this.minecraft.getFrameTime());
                if (f > 0.0F) {
                    i1 = j + Mth.floor(16.0F * (1.0F - f));
                    j1 = i1 + Mth.ceil(16.0F * f);
                    this.fill(RenderType.guiOverlay(), i2, i1, i2 + 16, j1, Integer.MAX_VALUE);
                }

                this.pose.popPose();
//                ItemDecoratorHandler.of(stack).render(this, font, stack, i2, j);
            }*/
        }
    }

    /**
     * @author yuo
     * @reason 修改无尽箱子中的物品数量显示
     */
//    @ModifyVariable(method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("STORE"), ordinal = 1)
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

    private String getItemCount(ItemStack stack) {
        String s = String.valueOf(stack.getCount()).replaceAll("\\D", ""); //去除非数字
        if (s.isEmpty()) {
            s = "1";
        }
        int count = Integer.parseInt(s);
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

