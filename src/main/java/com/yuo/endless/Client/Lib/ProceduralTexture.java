package com.yuo.endless.Client.Lib;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MipmapGenerator;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.function.Consumer;

public class ProceduralTexture extends TextureAtlasSprite {
    Consumer<ProceduralTexture> cycleFunc;

    public ProceduralTexture(AtlasTexture atlas, TextureAtlasSprite other, Consumer<ProceduralTexture> cycleFunc) {
        this(atlas, other.spriteInfo, other.frames.length - 1, 1, 1, other.x, other.y, other.frames[0], cycleFunc);
        this.minU = other.minU;
        this.maxU = other.maxU;
        this.minV = other.minV;
        this.maxV = other.maxV;
    }

    ProceduralTexture(AtlasTexture atlas, TextureAtlasSprite.Info info, int mipmapLevels, int storageX, int storageY, int x, int y, NativeImage image, Consumer<ProceduralTexture> cycleFunc) {
        super(atlas, info, mipmapLevels, storageX, storageY, x, y, image);
        this.cycleFunc = cycleFunc;
    }

    public void updateAnimation() {
        this.cycleFunc.accept(this);
        int levels = this.frames.length - 1;
        if (levels > 0) {
            NativeImage[] mipped = MipmapGenerator.generateMipmaps(this.frames[0], levels);
            System.arraycopy(mipped, 0, this.frames, 0, mipped.length);
        }
        uploadMipmaps();
    }
}

