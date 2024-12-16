package com.yuo.endless.Client.Gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.Container.Chest.InfinityBoxContainer;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.Tool.ColorText;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class InfinityBoxScreen extends AbstractContainerScreen<InfinityBoxContainer> {
    private static final ResourceLocation INFINITY_CHEST_GFUI_TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/gui/infinity_chest.png");
    public InfinityBoxScreen(InfinityBoxContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 500;
        this.imageHeight = 276;
        this.inventoryLabelY = this.imageHeight - 82;
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
        blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight, 500, 500);
        if (this.menu.isBurning()) {
            int k = this.menu.getBurnProgress();
            blit(matrixStack, i + 63, j + 213 + 14- k, 0, 276 + 14 - k, 14, k, 500, 500);
        }

        int l = this.menu.getCookProgress();
        blit(matrixStack, i + 86, j + 211, 0, 290, l, 16, 500, 500);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, new TextComponent(ColorText.makeFabulous(this.title.getString())), (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
        this.font.draw(matrixStack, this.playerInventoryTitle, (float)this.inventoryLabelX, (float)this.inventoryLabelY, 4210752);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

}
