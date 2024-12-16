package com.yuo.endless.Client.Gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.Container.AbsNeutronCollectorContainer;
import com.yuo.endless.Endless;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class AbsNeutronCollectorScreen extends AbstractContainerScreen<AbsNeutronCollectorContainer> {
    private final ResourceLocation RESOURCE = new ResourceLocation(Endless.MOD_ID, "textures/gui/neutron_collector_gui.png");
    protected final int textureWidth = 176;
    protected final int textureHeight = 166;

    public AbsNeutronCollectorScreen(AbsNeutronCollectorContainer screenContainer, Inventory inv, Component titleIn, int titleWidth) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;
        this.titleLabelX = this.imageWidth / 2 - titleWidth;
    }

    //渲染背景
    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        RenderSystem.setShaderTexture(0, RESOURCE);
//        if (this.minecraft != null) {
//            this.minecraft.getTextureManager().bindForSetup(RESOURCE);
//        }
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        blit(matrixStack, i, j, 0, 0, imageWidth, imageHeight);
        int k = this.menu.getTimer();
        this.blit(matrixStack, i + 105, j + 31 + 24 - k, 176, 24 - k, 4, k);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
}
