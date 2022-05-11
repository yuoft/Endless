package com.yuo.endless.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yuo.endless.Container.AbsNeutronCollectorContainer;
import com.yuo.endless.Endless;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class AbsNeutronCollectorScreen extends ContainerScreen<AbsNeutronCollectorContainer>{
    private final ResourceLocation RESOURCE = new ResourceLocation(Endless.MOD_ID, "textures/gui/neutron_collector_gui.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;

    public AbsNeutronCollectorScreen(AbsNeutronCollectorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn, int titleWidth) {
        super(screenContainer, inv, titleIn);
        this.xSize = textureWidth;
        this.ySize = textureHeight;
        this.titleX = this.xSize / 2 - titleWidth;
    }

    //渲染背景
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        this.minecraft.getTextureManager().bindTexture(RESOURCE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        blit(matrixStack, i, j, 0, 0, xSize, ySize);
        int k = this.container.getTimer();
        this.blit(matrixStack, i + 105, j + 31 + 24 - k, 176, 24 - k, 4, k);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
}
