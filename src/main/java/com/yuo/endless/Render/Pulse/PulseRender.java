package com.yuo.endless.Render.Pulse;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class PulseRender extends ItemStackTileEntityRenderer {

    private long lastSystemTime = 0;
    private double pulse = 0;
    private final Random random = new Random();

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer typeBuffer, int i, int j) {
        if (transformType == ItemCameraTransforms.TransformType.FIXED)
            matrixStack.scale(-1.0f, 1.0f, 1.0f);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        IBakedModel bakedModel = itemRenderer.getItemModelWithOverrides(stack, null, null);
        matrixStack.push();
        matrixStack.translate(0.5f, 0.5f, 0.5f);

        updatePulse();
        float scale = 1.0f + 0.05f * (float) Math.sin(pulse) + getRandomPulse();
        matrixStack.scale(scale, scale, scale);

        itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.NONE, false, matrixStack,typeBuffer,i, j, bakedModel );
        matrixStack.pop();
    }

    /**
     * 更新脉冲
     */
    private void updatePulse(){
        long systemTime = System.currentTimeMillis(); //系统时间
        if (lastSystemTime == 0){
            lastSystemTime = systemTime;
        }
        long deltaTime = systemTime - lastSystemTime;
        pulse += deltaTime * 0.3;
        lastSystemTime = systemTime;
    }

    private float getRandomPulse(){
        return (random.nextFloat() - 0.5f) * 0.06f;
    }
}
