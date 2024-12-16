package com.yuo.endless.Client.Gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.Container.NeutroniumCompressorContainer;
import com.yuo.endless.Endless;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class NeutroniumCompressorScreen extends AbstractContainerScreen<NeutroniumCompressorContainer> {
    private final ResourceLocation RESOURCE = new ResourceLocation(Endless.MOD_ID, "textures/gui/compressor.png");
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
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        RenderSystem.setShaderTexture(0, RESOURCE);
//        if (this.minecraft != null) {
//            this.minecraft.getTextureManager().bindTexture(RESOURCE);
//        }
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        blit(matrixStack, i, j, 0, 0, imageWidth, imageHeight);
        int k = this.menu.getProgress();
        int l = this.menu.getProgress1();
        this.blit(matrixStack, i + 62, j + 34, 176, 0, k , 16);
        this.blit(matrixStack, i + 90, j + 35 + 16 - l, 176, 32 - l, 16, l);
        ItemStack item = this.menu.getItem();
        if (!item.isEmpty()){ //渲染当前参与合成的物品
            if (this.minecraft != null) {
                this.minecraft.getItemRenderer().renderGuiItem(item, i + 15, j + 35);
            }
            this.font.draw(matrixStack, item.getItem().getDescriptionId(), i + 15, j + 25, 0x696969);
        }
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        this.font.draw(matrixStack, this.menu.getNumber() + "/" + this.menu.getCount(), 39, 54, 0x696969 );
        super.renderLabels(matrixStack, x, y);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
}
