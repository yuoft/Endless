package com.yuo.endless.Client.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yuo.endless.Container.Craft.EnderCraftContainer;
import com.yuo.endless.Endless;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class EnderCraftScreen extends ContainerScreen<EnderCraftContainer> {
    private final ResourceLocation RESOURCE = new ResourceLocation(Endless.MOD_ID, "textures/gui/ender_crafting_gui.png");
    protected final int textureWidth = 200;
    protected final int textureHeight = 242;

    public EnderCraftScreen(EnderCraftContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = textureWidth;
        this.ySize = textureHeight;
        this.playerInventoryTitleY = this.ySize - 82;
        this.playerInventoryTitleX = this.xSize - 180;
        this.titleX = this.xSize - 50;
    }

    //渲染背景
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
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
