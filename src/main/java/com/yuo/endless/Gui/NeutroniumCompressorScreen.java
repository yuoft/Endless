package com.yuo.endless.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yuo.endless.Container.NeutroniumCompressorContainer;
import com.yuo.endless.Endless;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class NeutroniumCompressorScreen extends ContainerScreen<NeutroniumCompressorContainer>{
    private final ResourceLocation RESOURCE = new ResourceLocation(Endless.MOD_ID, "textures/gui/compressor.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;

    public NeutroniumCompressorScreen(NeutroniumCompressorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = textureWidth;
        this.ySize = textureHeight;
        this.titleX = this.xSize / 2 - 31;
    }

    //渲染背景
    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        this.minecraft.getTextureManager().bindTexture(RESOURCE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        blit(matrixStack, i, j, 0, 0, xSize, ySize);
        int k = this.container.getProgress();
        int l = this.container.getProgress1();
        this.blit(matrixStack, i + 62, j + 34, 176, 0, k , 16);
        this.blit(matrixStack, i + 90, j + 35 + 16 - l, 176, 32 - l, 16, l);
        ItemStack item = this.container.getItem();
        if (!item.isEmpty()){ //渲染当前参与合成的物品
            this.minecraft.getItemRenderer().renderItemIntoGUI(item, i + 15, j + 35);
            this.font.drawString(matrixStack, item.getItem().getName().getString(), i + 15, j + 25, 0x696969);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.drawString(matrixStack, this.container.getNumber() + "/" + this.container.getCount(), 39, 54, 0x696969 );
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
}
