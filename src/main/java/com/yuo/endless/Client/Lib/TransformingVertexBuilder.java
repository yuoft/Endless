package com.yuo.endless.Client.Lib;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class TransformingVertexBuilder implements ISpriteAwareVertexBuilder {
    VertexConsumer delegate;

    Transformation transform;

    Vector3 storage = new Vector3();

    public TransformingVertexBuilder(VertexConsumer delegate, PoseStack stack) {
        this(delegate, new Matrix4(stack));
    }

    public void endVertex() {
        this.delegate.endVertex();
    }

    @Override
    public void defaultColor(int i, int i1, int i2, int i3) {
        this.delegate.defaultColor(i, i1, i2, i3);
    }

    @Override
    public void unsetDefaultColor() {
        this.delegate.unsetDefaultColor();
    }

    public TransformingVertexBuilder(VertexConsumer delegate, Transformation transform) {
        this.delegate = delegate;
        this.transform = transform;
    }

    public VertexConsumer vertex(double x, double y, double z) {
        this.storage.set(x, y, z);
        this.transform.apply(this.storage);
        this.delegate.vertex(this.storage.x, this.storage.y, this.storage.z);
        return this;
    }

    public VertexConsumer color(int red, int green, int blue, int alpha) {
        this.delegate.color(red, green, blue, alpha);
        return this;
    }

    public VertexConsumer uv(float u, float v) {
        this.delegate.uv(u, v);
        return this;
    }

    public VertexConsumer overlayCoords(int u, int v) {
        this.delegate.overlayCoords(u, v);
        return this;
    }

    public VertexConsumer uv2(int u, int v) {
        this.delegate.uv2(u, v);
        return this;
    }

    public VertexConsumer normal(float x, float y, float z) {
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