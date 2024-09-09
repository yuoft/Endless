package com.yuo.endless.Client.Model;

import codechicken.lib.colour.ColourARGB;
import codechicken.lib.model.CachedFormat;
import codechicken.lib.model.Quad;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.yuo.endless.Client.Lib.WrappedItemModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;

import java.util.Collections;

public class HaloBakedModel extends WrappedItemModel implements IItemRenderer {
    public HaloBakedModel(IBakedModel wrapped, TextureAtlasSprite sprite, int color, int size, boolean pulse) {
        super(wrapped);
        this.haloQuad = generateHaloQuad(sprite, size, color);
        this.pulse = pulse;
    }

    public void renderItem(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack mStack, IRenderTypeBuffer source, int light, int overlay) {
        if (transformType == ItemCameraTransforms.TransformType.GUI) {
            Minecraft.getInstance().getItemRenderer().renderQuads(mStack, source.getBuffer(RenderType.makeType("item", DefaultVertexFormats.ENTITY, 7, 256, true, true,
                    RenderType.State.getBuilder().texture(new RenderState.TextureState(AtlasTexture.LOCATION_BLOCKS_TEXTURE, false, false)).transparency(RenderType.TRANSLUCENT_TRANSPARENCY).writeMask(RenderType.COLOR_WRITE).build(true))), Collections.singletonList(this.haloQuad), stack, light, overlay);
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
        float minU = sprite.getMinU();
        float maxU = sprite.getMaxU();
        float minV = sprite.getMinV();
        float maxV = sprite.getMaxV();
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
