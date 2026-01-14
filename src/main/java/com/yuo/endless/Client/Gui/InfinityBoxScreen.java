package com.yuo.endless.Client.Gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.yuo.endless.Container.Chest.InfinityBoxContainer;
import com.yuo.endless.Items.Tool.ColorText;
import com.yuo.endless.EndlessUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

public class InfinityBoxScreen extends AbstractContainerScreen<InfinityBoxContainer> {
    private static final ResourceLocation INFINITY_CHEST_GFUI_TEXTURE = EndlessUtils.fa("textures/gui/infinity_chest.png");
    public InfinityBoxScreen(InfinityBoxContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageWidth = 500;
        this.imageHeight = 276;
        this.inventoryLabelY = this.imageHeight - 82;
        this.titleLabelX = this.imageWidth / 2 - 31;
    }

    @Override
    protected void renderBg(GuiGraphics matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, INFINITY_CHEST_GFUI_TEXTURE);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        matrixStack.blit(INFINITY_CHEST_GFUI_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight, 500, 500);
        if (this.menu.isBurning()) {
            int k = this.menu.getBurnProgress();
            matrixStack.blit(INFINITY_CHEST_GFUI_TEXTURE, i + 63, j + 213 + 14- k, 0, 276 + 14 - k, 14, k, 500, 500);
        }

        int l = this.menu.getCookProgress();
        matrixStack.blit(INFINITY_CHEST_GFUI_TEXTURE, i + 86, j + 211, 0, 290, l, 16, 500, 500);
    }


    @Override
    protected void renderLabels(GuiGraphics graphics, int i, int i1) {
        super.renderLabels(graphics, i, i1);
        graphics.drawString(this.font, ColorText.makeFabulous(this.menu.getDisplayName()), this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    @Override
    public Component getTitle() {
        if (ModList.get().isLoaded("jade"))
            return Component.literal(ColorText.makeFabulous(this.menu.getDisplayName()));
        return Component.empty();
    }

    @Override
    public void render(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderSlotCount(matrixStack);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    public void renderSlot(@NotNull GuiGraphics graphics, Slot slot) {
        int i = slot.x;
        int j = slot.y;
        ItemStack itemstack = slot.getItem();
        boolean flag = false;
        boolean flag1 = slot == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isSplittingStack;
        ItemStack itemstack1 = this.menu.getCarried();
        if (slot == this.clickedSlot && !this.draggingItem.isEmpty() && this.isSplittingStack && !itemstack.isEmpty()) {
            itemstack = itemstack.copyWithCount(itemstack.getCount() / 2);
        } else if (this.isQuickCrafting && this.quickCraftSlots.contains(slot) && !itemstack1.isEmpty()) {
            if (this.quickCraftSlots.size() == 1) {
                return;
            }

            if (AbstractContainerMenu.canItemQuickReplace(slot, itemstack1, true) && this.menu.canDragTo(slot)) {
                flag = true;
                int k = Math.min(itemstack1.getMaxStackSize(), slot.getMaxStackSize(itemstack1));
                int l = slot.getItem().isEmpty() ? 0 : slot.getItem().getCount();
                int i1 = AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, itemstack1) + l;
                if (i1 > k) {
                    i1 = k;
                }

                itemstack = itemstack1.copyWithCount(i1);
            } else {
                this.quickCraftSlots.remove(slot);
                this.recalculateQuickCraftRemaining();
            }
        }

        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 100.0F);
        if (itemstack.isEmpty() && slot.isActive()) {
            Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
            if (pair != null) {
                TextureAtlasSprite textureatlassprite;
                if (this.minecraft != null) {
                    textureatlassprite = (TextureAtlasSprite)this.minecraft.getTextureAtlas(pair.getFirst()).apply(pair.getSecond());
                    graphics.blit(i, j, 0, 16, 16, textureatlassprite);
                }
                flag1 = true;
            }
        }

        if (!flag1) {
            if (flag) {
                graphics.fill(i, j, i + 16, j + 16, -2130706433);
            }

            graphics.renderItem(itemstack, i, j, slot.x + slot.y * this.imageWidth);
//            graphics.renderItemDecorations(this.font, itemstack, i, j, s);  //移除原版数字渲染
        }

        graphics.pose().popPose();
    }

    /**
     * 容器物品数量小数字渲染 by：无尽：重生
     */
    public void renderSlotCount(GuiGraphics graphics) {
        PoseStack poseStack = graphics.pose();
        for (int i = 0; i < menu.slots.size(); i++) {
            int count = menu.slots.get(i).getItem().getCount();
            float fontSize = 0.5F;
            if (count != 0L) {
                String stringCount = String.valueOf(count);
                if (count >= 1_000 && count < 1_000_000)
                    stringCount = count / 1_000 + "K";
                else if (count >= 1_000_000 && count < 1_000_000_000)
                    stringCount = count / 1_000_000 + "M";
                else if (count >= 1_000_000_000)
                    stringCount = count / 1_000_000_000 + "B";
                RenderSystem.enableDepthTest();
                poseStack.pushPose();
                poseStack.translate(leftPos + menu.getSlot(i).x, topPos + menu.getSlot(i).y, 300.0D);
                poseStack.scale(fontSize, fontSize, 1.0F);
                graphics.drawString(this.font, stringCount,
                        (int) ((16 - this.font.width(stringCount) * fontSize) / fontSize),
                        (int) ((16 - this.font.lineHeight * fontSize) / fontSize),
                        16777215);
                poseStack.popPose();
            }
        }
    }
}
