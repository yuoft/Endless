package com.yuo.endless.Client.Lib;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public interface AtlasRegistrar {
    void registerSprite(ResourceLocation paramResourceLocation, Consumer<TextureAtlasSprite> paramConsumer);
}

