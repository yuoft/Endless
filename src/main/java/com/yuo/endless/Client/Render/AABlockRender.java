package com.yuo.endless.Client.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.Tiles.CosmicTile;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class AABlockRender implements BlockEntityRenderer<CosmicTile> {
    public AABlockRender(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(CosmicTile cosmicTile, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        BlockState blockState = cosmicTile.getBlockState();
        ItemStack stack = new ItemStack(EndlessItems.aa.get());
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.scale(1.0011123F, 1.0011123F, 1.0011123F);
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        CosmicBlockRenderHelper.renderBlockQuads(blockState, poseStack, multiBufferSource, i, i1, stack);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(CosmicTile p_112306_) {
        return true;
    }
}
