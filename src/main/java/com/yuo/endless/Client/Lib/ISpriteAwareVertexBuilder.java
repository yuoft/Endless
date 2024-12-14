package com.yuo.endless.Client.Lib;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public interface ISpriteAwareVertexBuilder extends IVertexBuilder {
    void sprite(TextureAtlasSprite paramTextureAtlasSprite);
}
