package com.yuo.endless.Client.Gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.Container.Chest.CompressorChestContainer;
import com.yuo.endless.Endless;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CompressorChestScreen extends AbstractContainerScreen<CompressorChestContainer> {
    private static final ResourceLocation INFINITY_CHEST_GFUI_TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/gui/compressor_chest.png");
    public CompressorChestScreen(CompressorChestContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 256;
        this.imageHeight = 276;
        this.inventoryLabelY = this.imageHeight - 94;
        this.titleLabelX = this.imageWidth / 2 - 31;
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, INFINITY_CHEST_GFUI_TEXTURE);
//        if (this.minecraft != null) {
//            this.minecraft.getTextureManager().bindTexture(INFINITY_CHEST_GFUI_TEXTURE);
//        }
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        blit(matrixStack, i, j, 0, 0, this.imageWidth, imageHeight, 256, 276);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
}
