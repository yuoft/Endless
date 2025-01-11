package com.yuo.endless.Client.Lib;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface ISpriteAwareVertexBuilder extends VertexConsumer {
    void sprite(TextureAtlasSprite paramTextureAtlasSprite);
}
