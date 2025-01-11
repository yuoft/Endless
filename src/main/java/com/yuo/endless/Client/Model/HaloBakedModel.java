package com.yuo.endless.Client.Model;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.yuo.endless.Client.Lib.CachedFormat;
import com.yuo.endless.Client.Lib.ColourARGB;
import com.yuo.endless.Client.Lib.Quad;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;

public class HaloBakedModel extends WrappedItemModel implements IItemRenderer {
    public HaloBakedModel(BakedModel wrapped, TextureAtlasSprite sprite, int color, int size, boolean pulse) {
        super(wrapped);
        this.haloQuad = generateHaloQuad(sprite, size, color);
        this.pulse = pulse;
    }

    public void renderItem(ItemStack stack, TransformType transformType, PoseStack mStack, MultiBufferSource source, int light, int overlay) {
        if (transformType == TransformType.GUI) {
            Minecraft.getInstance().getItemRenderer().renderQuadList(mStack, source.getBuffer(RenderType.create("item", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true,
                    RenderType.CompositeState.builder().setShaderState(RenderType.RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER).setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
                            .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setOutputState(RenderType.ITEM_ENTITY_TARGET).setLightmapState(RenderType.LIGHTMAP).setOverlayState(RenderType.OVERLAY).setWriteMaskState(RenderType.COLOR_WRITE)
                            .createCompositeState(true))), Collections.singletonList(this.haloQuad), stack, light, overlay);
            if (this.pulse) {
                double scale = this.RANDOM.nextDouble() * 0.15D + 0.95D;
                double trans = (1.0D - scale) / 2.0D;
                mStack.translate(trans, trans, 0.0D);
                mStack.scale((float)scale, (float)scale, 1.0001F);
            }
        }
        renderWrapped(stack, mStack, source, light, overlay, true);
    }

    static BakedQuad generateHaloQuad(TextureAtlasSprite sprite, int size, int color) {
        float[] colors = (new ColourARGB(color)).getRGBA();
        double spread = size / 16.0D;
        double min = 0.0D - spread;
        double max = 1.0D + spread;
        float minU = sprite.getU0();
        float maxU = sprite.getU1();
        float minV = sprite.getV0();
        float maxV = sprite.getV1();
        Quad quad = new Quad();
        quad.reset(CachedFormat.BLOCK);
        quad.setTexture(sprite);
        putVertex(quad.vertices[0], max, max, 0.0D, maxU, minV);
        putVertex(quad.vertices[1], min, max, 0.0D, minU, minV);
        putVertex(quad.vertices[2], min, min, 0.0D, minU, maxV);
        putVertex(quad.vertices[3], max, min, 0.0D, maxU, maxV);
        for (int i = 0; i < 4; i++)
            System.arraycopy(colors, 0, (quad.vertices[i]).color, 0, 4);
        quad.calculateOrientation(true);
        return quad.bake();
    }

    static void putVertex(Quad.Vertex vertex, double x, double y, double z, double u, double v) {
        vertex.vec[0] = (float)x;
        vertex.vec[1] = (float)y;
        vertex.vec[2] = (float)z;
        vertex.uv[0] = (float)u;
        vertex.uv[1] = (float)v;
    }
}
