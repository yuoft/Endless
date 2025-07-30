package com.yuo.endless.Client.Lib;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public interface IVertexConsumer {
    VertexFormat getVertexFormat();

    void setQuadTint(int var1);

    void setQuadOrientation(Direction var1);

    void setApplyDiffuseLighting(boolean var1);

    void setTexture(TextureAtlasSprite var1);

    void put(int var1, float... var2);

    void put(Quad var1);

    default void put(BakedQuad quad) {
        VertexUtils.putQuad(this, quad);
    }
}
