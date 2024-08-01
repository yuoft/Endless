package com.yuo.endless.Mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow public float zLevel;

    @Shadow protected abstract void draw(BufferBuilder p_181565_1_, int p_181565_2_, int p_181565_3_, int p_181565_4_, int p_181565_5_, int p_181565_6_, int p_181565_7_, int p_181565_8_, int p_181565_9_);

    /**
     * @author yuo
     * @reason 修改无尽箱子中的物品数量显示
     */
    @Overwrite
    public void renderItemOverlayIntoGUI(FontRenderer fontRenderer, ItemStack itemStack, int x, int y, @Nullable String name) {
        if (!itemStack.isEmpty()) {
            MatrixStack matrixstack = new MatrixStack();
            if (itemStack.getCount() != 1 || name != null) {
                String s = name == null ? String.valueOf(endless$getSimplifiedCount(itemStack.getCount())) : name;
                matrixstack.translate(0.0, 0.0, (double)(this.zLevel + 200.0F));
                IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
                fontRenderer.renderString(s, (float)(x + 19 - 2 - fontRenderer.getStringWidth(s)), (float)(y + 6 + 3), 16777215, true, matrixstack.getLast().getMatrix(), irendertypebuffer$impl, false, 0, 15728880);
                irendertypebuffer$impl.finish();
            }

            if (itemStack.getItem().showDurabilityBar(itemStack)) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.disableAlphaTest();
                RenderSystem.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                double health = itemStack.getItem().getDurabilityForDisplay(itemStack);
                int i = Math.round(13.0F - (float)health * 13.0F);
                int j = itemStack.getItem().getRGBDurabilityForDisplay(itemStack);
                this.draw(bufferbuilder, x + 2, y + 13, 13, 2, 0, 0, 0, 255);
                this.draw(bufferbuilder, x + 2, y + 13, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
                RenderSystem.enableBlend();
                RenderSystem.enableAlphaTest();
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }

            ClientPlayerEntity clientplayerentity = Minecraft.getInstance().player;
            float f3 = clientplayerentity == null ? 0.0F : clientplayerentity.getCooldownTracker().getCooldown(itemStack.getItem(), Minecraft.getInstance().getRenderPartialTicks());
            if (f3 > 0.0F) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                Tessellator tessellator1 = Tessellator.getInstance();
                BufferBuilder bufferbuilder1 = tessellator1.getBuffer();
                this.draw(bufferbuilder1, x, y + MathHelper.floor(16.0F * (1.0F - f3)), 16, MathHelper.ceil(16.0F * f3), 255, 255, 255, 127);
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }
        }
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
