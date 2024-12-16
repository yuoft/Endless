package com.yuo.endless.Client.Gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.Config;
import com.yuo.endless.Container.ExtremeCraftContainer;
import com.yuo.endless.Endless;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ExtremeCraftScreen extends AbstractContainerScreen<ExtremeCraftContainer> {
    private final ResourceLocation RESOURCE = new ResourceLocation(Endless.MOD_ID, "textures/gui/dire_crafting_gui.png");
    private final ResourceLocation RESOURCE_OPEN = new ResourceLocation(Endless.MOD_ID, "textures/gui/dire_crafting_gui_open.png");
    protected final int textureWidth = 238;
    protected final int textureHeight = 256;

    public ExtremeCraftScreen(ExtremeCraftContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;
        this.inventoryLabelY = this.imageHeight - 82;
        this.titleLabelX = this.imageWidth - 50;
    }

    //渲染背景
    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        if (Config.SERVER.isCraftTable.get() && this.minecraft != null) {
            RenderSystem.setShaderTexture(0, RESOURCE_OPEN);
//            this.minecraft.getTextureManager().bindTexture(RESOURCE_OPEN);
        } else if (this.minecraft != null) {
            RenderSystem.setShaderTexture(0, RESOURCE);
//            this.minecraft.getTextureManager().bindTexture(RESOURCE);
        }
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        blit(matrixStack, i, j, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
}
