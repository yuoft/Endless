package com.yuo.endless.compat.oculus;

import com.mojang.blaze3d.vertex.VertexConsumer;

import java.util.ArrayList;
import java.util.List;

final class ModelVertices implements VertexConsumer {
    private final List<Vertex> vertices = new ArrayList<>();
    private double x, y, z;
    private float u, v, nx, ny, nz;
    private int r = 255, g = 255, b = 255, a = 255, overlay, light;

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        this.x = x; this.y = y; this.z = z; return this;
    }

    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        this.r = r; this.g = g; this.b = b; this.a = a; return this;
    }

    @Override
    public VertexConsumer uv(float u, float v) {
        this.u = u; this.v = v; return this;
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v) {
        overlay = u | v << 16; return this;
    }

    @Override
    public VertexConsumer uv2(int u, int v) {
        light = u | v << 16; return this;
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        nx = x; ny = y; nz = z; return this;
    }

    @Override
    public void endVertex() {
        vertices.add(new Vertex(x, y, z, r, g, b, a, u, v, overlay, light, nx, ny, nz));
    }

    @Override
    public void defaultColor(int r, int g, int b, int a) {
        color(r, g, b, a);
    }

    @Override
    public void unsetDefaultColor() {
        color(255, 255, 255, 255);
    }

    void draw(VertexConsumer consumer) {
        for (Vertex vertex : vertices) {
            consumer.vertex(vertex.x, vertex.y, vertex.z).color(vertex.r, vertex.g, vertex.b, vertex.a)
                    .uv(vertex.u, vertex.v).overlayCoords(vertex.overlay).uv2(vertex.light)
                    .normal(vertex.nx, vertex.ny, vertex.nz).endVertex();
        }
    }

    private record Vertex(double x, double y, double z, int r, int g, int b, int a, float u, float v, int overlay, int light, float nx, float ny, float nz) {}
}
