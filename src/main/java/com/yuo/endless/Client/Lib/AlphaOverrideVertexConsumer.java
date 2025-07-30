package com.yuo.endless.Client.Lib;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class AlphaOverrideVertexConsumer extends DelegatingVertexConsumer {
    private final int alpha;

    public AlphaOverrideVertexConsumer(VertexConsumer delegate, double alpha) {
        this(delegate, (int)(255.0 * alpha));
    }

    public AlphaOverrideVertexConsumer(VertexConsumer delegate, int alpha) {
        super(delegate);
        this.alpha = alpha;
    }

    public VertexConsumer color(int r, int g, int b, int a) {
        return super.color(r, g, b, this.alpha);
    }
}
