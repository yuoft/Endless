package com.yuo.endless.Client.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yuo.endless.Config;
import com.yuo.endless.Container.ExtremeCraftContainer;
import com.yuo.endless.Endless;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ExtremeCraftScreen extends ContainerScreen<ExtremeCraftContainer> {
    private final ResourceLocation RESOURCE = new ResourceLocation(Endless.MOD_ID, "textures/gui/dire_crafting_gui.png");
    private final ResourceLocation RESOURCE_OPEN = new ResourceLocation(Endless.MOD_ID, "textures/gui/dire_crafting_gui_open.png");
    protected final int textureWidth = 238;
    protected final int textureHeight = 256;

    public ExtremeCraftScreen(ExtremeCraftContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = textureWidth;
        this.ySize = textureHeight;
        this.playerInventoryTitleY = this.ySize - 82;
        this.titleX = this.xSize - 50;
    }

    //渲染背景
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        if (Config.SERVER.isCraftTable.get() && this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(RESOURCE_OPEN);
        } else if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(RESOURCE);
        }
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        blit(matrixStack, i, j, 0, 0, xSize, ySize);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
}
