package com.yuo.endless.Client.Lib;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public abstract class DelegatingVertexConsumer implements ISpriteAwareVertexConsumer {
    protected final VertexConsumer delegate;

    public DelegatingVertexConsumer(VertexConsumer delegate) {
        this.delegate = delegate;
    }

    public void sprite(TextureAtlasSprite sprite) {
        VertexConsumer var3 = this.delegate;
        if (var3 instanceof ISpriteAwareVertexConsumer spriteCons) {
            spriteCons.sprite(sprite);
        }

    }

    public VertexConsumer vertex(double x, double y, double z) {
        this.delegate.vertex(x, y, z);
        return this;
    }

    public VertexConsumer color(int r, int g, int b, int a) {
        this.delegate.color(r, g, b, a);
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
        this.delegate.normal(x, y, z);
        return this;
    }

    public void endVertex() {
        this.delegate.endVertex();
    }

    public void defaultColor(int r, int g, int b, int a) {
        this.delegate.defaultColor(r, g, b, a);
    }

    public void unsetDefaultColor() {
        this.delegate.unsetDefaultColor();
    }
}
