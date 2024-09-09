package com.yuo.endless.Client.Lib;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class TransformingVertexBuilder implements ISpriteAwareVertexBuilder {
    IVertexBuilder delegate;

    Transformation transform;

    Vector3 storage = new Vector3();

    public TransformingVertexBuilder(IVertexBuilder delegate, MatrixStack stack) {
        this(delegate, new Matrix4(stack));
    }

    public void endVertex() {
        this.delegate.endVertex();
    }

    public TransformingVertexBuilder(IVertexBuilder delegate, Transformation transform) {
        this.delegate = delegate;
        this.transform = transform;
    }

    public IVertexBuilder pos(double x, double y, double z) {
        this.storage.set(x, y, z);
        this.transform.apply(this.storage);
        this.delegate.pos(this.storage.x, this.storage.y, this.storage.z);
        return this;
    }

    public IVertexBuilder color(int red, int green, int blue, int alpha) {
        this.delegate.color(red, green, blue, alpha);
        return this;
    }

    public IVertexBuilder tex(float u, float v) {
        this.delegate.tex(u, v);
        return this;
    }

    public IVertexBuilder overlay(int u, int v) {
        this.delegate.overlay(u, v);
        return this;
    }

    public IVertexBuilder lightmap(int u, int v) {
        this.delegate.lightmap(u, v);
        return this;
    }

    public IVertexBuilder normal(float x, float y, float z) {
        this.storage.set(x, y, z);
        this.transform.applyN(this.storage);
        this.delegate.normal((float)this.storage.x, (float)this.storage.y, (float)this.storage.z);
        return this;
    }

    public void sprite(TextureAtlasSprite sprite) {
        if (this.delegate instanceof ISpriteAwareVertexBuilder)
            ((ISpriteAwareVertexBuilder)this.delegate).sprite(sprite);
    }
}