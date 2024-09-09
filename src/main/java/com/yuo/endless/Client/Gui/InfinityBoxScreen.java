package com.yuo.endless.Client.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yuo.endless.Container.Chest.InfinityBoxContainer;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.Tool.ColorText;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class InfinityBoxScreen extends ContainerScreen<InfinityBoxContainer> {
    private static final ResourceLocation INFINITY_CHEST_GFUI_TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/gui/infinity_chest.png");
    public InfinityBoxScreen(InfinityBoxContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 500;
        this.ySize = 276;
        this.playerInventoryTitleY = this.ySize - 82;
        this.titleX = this.xSize / 2 - 31;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(INFINITY_CHEST_GFUI_TEXTURE);
        }
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize, 500, 500);
        if (this.container.isBurning()) {
            int k = this.container.getBurnProgress();
            blit(matrixStack, i + 63, j + 213 + 14- k, 0, 276 + 14 - k, 14, k, 500, 500);
        }

        int l = this.container.getCookProgress();
        blit(matrixStack, i + 86, j + 211, 0, 290, l, 16, 500, 500);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.drawText(matrixStack, new StringTextComponent(ColorText.makeFabulous(this.title.getString())), (float)this.titleX, (float)this.titleY, 4210752);
        this.font.drawText(matrixStack, this.playerInventory.getDisplayName(), (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 4210752);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

}
