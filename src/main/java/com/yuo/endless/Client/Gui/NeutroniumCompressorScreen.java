package com.yuo.endless.Client.Gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.yuo.endless.Container.NeutroniumCompressorContainer;
import com.yuo.endless.EndlessUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class NeutroniumCompressorScreen extends AbstractContainerScreen<NeutroniumCompressorContainer> {
    private final ResourceLocation RESOURCE = EndlessUtils.fa("textures/gui/compressor.png");
    protected final int textureWidth = 176;
    protected final int textureHeight = 166;

    public NeutroniumCompressorScreen(NeutroniumCompressorContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;
        this.titleLabelX = this.imageWidth / 2 - 31;
    }

    //渲染背景
    @Override
    protected void renderBg(GuiGraphics matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        RenderSystem.setShaderTexture(0, RESOURCE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        matrixStack.blit(RESOURCE, i, j, 0, 0, imageWidth, imageHeight);
        int k = this.menu.getProgress();
        int l = this.menu.getProgress1();
        matrixStack.blit(RESOURCE, i + 62, j + 34, 176, 0, k , 16);
        matrixStack.blit(RESOURCE, i + 90, j + 35 + 16 - l, 176, 32 - l, 16, l);
    }

    @Override
    protected void renderLabels(GuiGraphics matrixStack, int x, int y) {
        super.renderLabels(matrixStack, x, y);
    }

    @Override
    public void render(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        if (isMouseOverArea(mouseX, mouseY, i + 62, j + 34, 22, 16))
            matrixStack.drawString(this.font, this.menu.getNumber() + "/" + this.menu.getCount(), mouseX + 10, mouseY, 4210752 , false);

        ItemStack resultItem = this.menu.getResultItem();
        if (!resultItem.isEmpty()) {
            matrixStack.renderFakeItem(resultItem, i + 150, j + 35);
            if (isMouseOverArea(mouseX, mouseY, i + 150, j + 35, 16, 16)){
                matrixStack.renderTooltip(this.font, resultItem, mouseX, mouseY);
            }
        }

        ItemStack inputItem = this.menu.getItem();
        if (!inputItem.isEmpty()) {
            matrixStack.renderItem(inputItem, i + 15, j + 35);
            if (isMouseOverArea(mouseX, mouseY, i + 15, j + 35, 16, 16)){
                matrixStack.renderTooltip(this.font, inputItem, mouseX, mouseY);
            }
        }
    }

    /**
     * 检查鼠标是否在指定区域内
     */
    private boolean isMouseOverArea(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
