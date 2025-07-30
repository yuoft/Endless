package com.yuo.endless.Client.Model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.Client.Lib.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class HaloBakedModel extends WrappedItemModel implements IItemRenderer{
    private final Random random = new Random();
    private final BakedQuad haloQuad;
    private final boolean pulse;

    public HaloBakedModel(BakedModel wrapped, TextureAtlasSprite sprite, int color, int size, boolean pulse) {
        super(wrapped);
        this.haloQuad = generateHaloQuad(sprite, size, color);
        this.pulse = pulse;
    }

    static BakedQuad generateHaloQuad(TextureAtlasSprite sprite, int size, int color) {
        float[] colors = (new ColourARGB(color)).getRGBA();
        double spread = (double)size / 16.0;
        double min = 0.0 - spread;
        double max = 1.0 + spread;
        float minU = sprite.getU0();
        float maxU = sprite.getU1();
        float minV = sprite.getV0();
        float maxV = sprite.getV1();
        Quad quad = new Quad();
        quad.reset(CachedFormat.BLOCK);
        quad.setTexture(sprite);
        putVertex(quad.vertices[0], max, max, 0.0, maxU, minV);
        putVertex(quad.vertices[1], min, max, 0.0, minU, minV);
        putVertex(quad.vertices[2], min, min, 0.0, minU, maxV);
        putVertex(quad.vertices[3], max, min, 0.0, maxU, maxV);

        for(int i = 0; i < 4; ++i) {
            System.arraycopy(colors, 0, quad.vertices[i].color, 0, 4);
        }

        quad.calculateOrientation(true);
        return quad.bake();
    }

    static void putVertex(Quad.Vertex vx, double x, double y, double z, double u, double v) {
        vx.vec[0] = (float)x;
        vx.vec[1] = (float)y;
        vx.vec[2] = (float)z;
        vx.uv[0] = (float)u;
        vx.uv[1] = (float)v;
    }

    public void renderItem(ItemStack stack, ItemDisplayContext transformType, PoseStack pStack, MultiBufferSource source, int packedLight, int packedOverlay) {
        if (transformType == ItemDisplayContext.GUI) {
            Minecraft.getInstance().getItemRenderer().renderQuadList(pStack, source.getBuffer(ItemBlockRenderTypes.getRenderType(stack, true)), List.of(this.haloQuad), stack, packedLight, packedOverlay);
            if (this.pulse) {
                pStack.pushPose();
                double scale = this.random.nextDouble() * 0.15 + 0.95;
                double trans = (1.0 - scale) / 2.0;
                pStack.translate(trans, trans, 0.0);
                pStack.scale((float)scale, (float)scale, 1.0001F);
                this.renderWrapped(stack, pStack, source, packedLight, packedOverlay, true, (e) -> new AlphaOverrideVertexConsumer(e, 0.6000000238418579));
                pStack.popPose();
            }
        }

        this.renderWrapped(stack, pStack, source, packedLight, packedOverlay, true);
    }

    public PerspectiveModelState getModelState() {
        return TransformUtils.DEFAULT_ITEM;
    }
}
