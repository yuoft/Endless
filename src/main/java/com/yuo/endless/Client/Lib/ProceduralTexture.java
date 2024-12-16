package com.yuo.endless.Client.Lib;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.MipmapGenerator;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.function.Consumer;

public class ProceduralTexture extends TextureAtlasSprite {
    Consumer<ProceduralTexture> cycleFunc;

    public ProceduralTexture(TextureAtlas atlas, TextureAtlasSprite other, Consumer<ProceduralTexture> cycleFunc) {
        this(atlas, new Info(), other.mainImage.length - 1, 1, 1, other.x, other.y, other.mainImage[0], cycleFunc);
        this.u0 = other.u0;
        this.u1 = other.u1;
        this.v0 = other.v0;
        this.v1 = other.v1;
    }

    ProceduralTexture(TextureAtlas atlas, TextureAtlasSprite.Info info, int mipmapLevels, int storageX, int storageY, int x, int y, NativeImage image, Consumer<ProceduralTexture> cycleFunc) {
        super(atlas, info, mipmapLevels, storageX, storageY, x, y, image);
        this.cycleFunc = cycleFunc;
    }

    public void updateAnimation() {
        this.cycleFunc.accept(this);
        int levels = this.mainImage.length - 1;
        if (levels > 0) {
            NativeImage[] mipped = MipmapGenerator.generateMipLevels(this.mainImage[0], levels);
            System.arraycopy(mipped, 0, this.mainImage, 0, mipped.length);
        }
        uploadFirstFrame();
    }
}

