package com.yuo.endless.Client.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class CosmicBlockRenderHelper {
    public static void renderBlockQuads(BlockState blockState, PoseStack poseStack, MultiBufferSource buffers, int packedLight, int packedOverlay, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        float yaw = 0.0F;
        float pitch = 0.0F;
        float scale = AvaritiaShaders.inventoryRender ? 100.0F : 1.0F;
        if (!AvaritiaShaders.inventoryRender && mc.player != null) {
            yaw = (float)(mc.player.getYRot() * 2.0f * Math.PI / 360.0);
            pitch = -(float)(mc.player.getXRot() * 2.0f * Math.PI / 360.0);
        }

        AvaritiaShaders.cosmicTime.set((float)(System.currentTimeMillis() - (long) AvaritiaShaders.renderTime) / 2000.0F);
        AvaritiaShaders.cosmicYaw.set(yaw);
        AvaritiaShaders.cosmicPitch.set(pitch);
        AvaritiaShaders.cosmicExternalScale.set(scale);
        AvaritiaShaders.cosmicOpacity.set(0.6F);
        for (int i = 0; i < 10; i++) {
            TextureAtlasSprite sprite = mc.getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS).getSprite(
                    ResourceLocation.parse("shader/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }
        AvaritiaShaders.cosmicUVs.setMatrix2x2Array(AvaritiaShaders.COSMIC_UVS, 10);
        VertexConsumer consumer = buffers.getBuffer(AvaritiaShaders.COSMIC_BLOCK_RENDER_TYPE);
        BakedModel model = mc.getBlockRenderer().getBlockModel(blockState);
        List<BakedQuad> quads = new ArrayList<>();
        for (Direction direction : Direction.values())
            quads.addAll(model.getQuads(blockState, direction, mc.level.random));
        mc.getItemRenderer().renderQuadList(poseStack, consumer, quads, stack, packedLight, packedOverlay);
    }
}
