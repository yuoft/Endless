package com.yuo.endless.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.client.AvaritiaShaders;
import com.yuo.endless.compat.oculus.CosmicBlockLateRenderQueue;
import com.yuo.endless.compat.oculus.OculusCompat;
import com.yuo.endless.compat.oculus.RenderFrameState;
import com.yuo.endless.items.EndlessItems;
import com.yuo.endless.tiles.CosmicTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CosmicBlockRender implements BlockEntityRenderer<CosmicTile> {
    private static final ItemStack STACK = new ItemStack(EndlessItems.cosmicBlock.get());
    public CosmicBlockRender(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CosmicTile cosmicTile, float v, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        BlockState blockState = cosmicTile.getBlockState();

        // ===== 一次性查询光影状态，避免帧内状态分裂 =====
        boolean oculusActive = OculusCompat.isShaderPackActive();
        boolean shadowPass = oculusActive && RenderFrameState.isShadowPass();
        // 阴影 pass 只画方块本体（由 chunk 渲染），这里不叠加 cosmic
        if (shadowPass) return;

        // shouldDefer 为 false 时不再直接 return，而是回退到立即渲染，
        // 避免光影切换那几帧方块星空完全消失
        boolean deferPath = oculusActive && CosmicBlockLateRenderQueue.shouldDefer();

        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.scale(1.0011123F, 1.0011123F, 1.0011123F);
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        if (deferPath)
            CosmicBlockLateRenderQueue.enqueue(blockState, poseStack, light, overlay);
        else
            renderBlockQuads(blockState, poseStack, bufferSource, light, overlay, STACK, EndlessRenderTypes.COSMIC_BLOCK_RENDER_TYPE);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull CosmicTile cosmicTile) {
        return true;
    }

    public static void renderBlockQuads(BlockState blockState, PoseStack poseStack, MultiBufferSource buffers, int packedLight, int packedOverlay, ItemStack stack, RenderType renderType) {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        float yaw = 0.0F;
        float pitch = 0.0F;
        float scale = AvaritiaShaders.inventoryRender ? 100.0F : 1.0F;
        if (!AvaritiaShaders.inventoryRender && mc.player != null) {
            yaw = (float) (mc.player.getYRot() * 2.0f * Math.PI / 360.0);
            pitch = -(float) (mc.player.getXRot() * 2.0f * Math.PI / 360.0);
        }

        AvaritiaShaders.cosmicTime.set((float) (System.currentTimeMillis() - (long) AvaritiaShaders.renderTime) / 2000.0F);
        AvaritiaShaders.cosmicYaw.set(yaw);
        AvaritiaShaders.cosmicPitch.set(pitch);
        AvaritiaShaders.cosmicExternalScale.set(scale);
        AvaritiaShaders.cosmicOpacity.set(2.0F);
        for (int i = 0; i < 10; i++) {
            TextureAtlasSprite sprite = mc.getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(
                    EndlessUtils.parse("shader/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }

        AvaritiaShaders.cosmicUVs.setMatrix2x2Array(AvaritiaShaders.COSMIC_UVS, 10);
        VertexConsumer consumer = buffers.getBuffer(renderType);
        BakedModel model = mc.getBlockRenderer().getBlockModel(blockState);
        List<BakedQuad> quads = new ArrayList<>();
        for (Direction direction : Direction.values())
            quads.addAll(model.getQuads(blockState, direction, mc.level.random));
        mc.getItemRenderer().renderQuadList(poseStack, consumer, quads, stack, packedLight, packedOverlay);
    }
}