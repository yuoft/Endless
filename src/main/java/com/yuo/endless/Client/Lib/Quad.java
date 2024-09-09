package com.yuo.endless.Client.Lib;

import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.IVertexProducer;
import net.minecraftforge.client.model.pipeline.LightUtil;

public class Quad implements IVertexProducer, ISmartVertexConsumer {
    public CachedFormat format;

    public int tintIndex = -1;

    public Direction orientation;

    public boolean diffuseLighting = true;

    public TextureAtlasSprite sprite;

    public Vertex[] vertices = new Vertex[4];

    public boolean full;

    int vertexIndex = 0;

    Vector3 v1 = new Vector3();

    Vector3 v2 = new Vector3();

    Vector3 t = new Vector3();

    public Quad() {}

    public Quad(CachedFormat format) {
        this.format = format;
    }

    public VertexFormat getVertexFormat() {
        return this.format.format;
    }

    public void setQuadTint(int tint) {
        this.tintIndex = tint;
    }

    public void setQuadOrientation(Direction orientation) {
        this.orientation = orientation;
    }

    public void setApplyDiffuseLighting(boolean diffuse) {
        this.diffuseLighting = diffuse;
    }

    public void setTexture(TextureAtlasSprite texture) {
        this.sprite = texture;
    }

    public void put(Quad quad) {
        copyFrom(quad);
    }

    public void put(int element, float... data) {
        if (this.full)
            throw new RuntimeException();
        Vertex v = this.vertices[this.vertexIndex];
        if (v == null) {
            v = new Vertex(this.format);
            this.vertices[this.vertexIndex] = v;
        }
        System.arraycopy(data, 0, v.raw[element], 0, data.length);
        if (element == this.format.elementCount - 1) {
            this.vertexIndex++;
            if (this.vertexIndex == 4) {
                this.vertexIndex = 0;
                this.full = true;
                if (this.orientation == null)
                    calculateOrientation(false);
            }
        }
    }

    public void pipe(IVertexConsumer consumer) {
        if (consumer instanceof ISmartVertexConsumer) {
            ((ISmartVertexConsumer)consumer).put(this);
        } else {
            consumer.setQuadTint(this.tintIndex);
            consumer.setQuadOrientation(this.orientation);
            consumer.setApplyDiffuseLighting(this.diffuseLighting);
            consumer.setTexture(this.sprite);
            for (Vertex v : this.vertices) {
                for (int e = 0; e < this.format.elementCount; e++)
                    consumer.put(e, v.raw[e]);
            }
        }
    }

    public void calculateOrientation(boolean setNormal) {
        this.v1.set((this.vertices[3]).vec).subtract(this.t.set((this.vertices[1]).vec));
        this.v2.set((this.vertices[2]).vec).subtract(this.t.set((this.vertices[0]).vec));
        Vector3 normal = this.v2.crossProduct(this.v1).normalize();
        if (this.format.hasNormal && setNormal)
            for (Vertex vertex : this.vertices) {
                vertex.normal[0] = (float)normal.x;
                vertex.normal[1] = (float)normal.y;
                vertex.normal[2] = (float)normal.z;
                vertex.normal[3] = 0.0F;
            }
        this.orientation = Direction.getFacingFromVector(normal.x, normal.y, normal.z);
    }

    public Quad copy() {
        if (!this.full)
            throw new RuntimeException();
        Quad quad = new Quad(this.format);
        quad.tintIndex = this.tintIndex;
        quad.orientation = this.orientation;
        quad.diffuseLighting = this.diffuseLighting;
        quad.sprite = this.sprite;
        quad.full = true;
        for (int i = 0; i < 4; i++)
            quad.vertices[i] = this.vertices[i].copy();
        return quad;
    }

    public Quad copyFrom(Quad quad) {
        this.tintIndex = quad.tintIndex;
        this.orientation = quad.orientation;
        this.diffuseLighting = quad.diffuseLighting;
        this.sprite = quad.sprite;
        this.full = quad.full;
        for (int v = 0; v < 4; v++) {
            for (int e = 0; e < this.format.elementCount; e++)
                System.arraycopy((quad.vertices[v]).raw[e], 0, (this.vertices[v]).raw[e], 0, 4);
        }
        return this;
    }

    public void reset(CachedFormat format) {
        this.format = format;
        this.tintIndex = -1;
        this.orientation = null;
        this.diffuseLighting = true;
        this.sprite = null;
        for (int i = 0; i < this.vertices.length; i++) {
            Vertex v = this.vertices[i];
            if (v == null)
                this.vertices[i] = v = new Vertex(format);
            v.reset(format);
        }
        this.vertexIndex = 0;
        this.full = false;
    }

    public BakedQuad bake() {
        int[] packedData = new int[this.format.format.getSize()];
        for (int v = 0; v < 4; v++) {
            for (int e = 0; e < this.format.elementCount; e++)
                LightUtil.pack((this.vertices[v]).raw[e], packedData, this.format.format, v, e);
        }
        return makeQuad(packedData);
    }

    BakedQuad makeQuad(int[] packedData) {
        if (this.format.format != DefaultVertexFormats.BLOCK)
            throw new IllegalStateException();
        return new BakedQuad(packedData, this.tintIndex, this.orientation, this.sprite, this.diffuseLighting);
    }

    public static class Vertex {
        public CachedFormat format;

        public float[][] raw;

        public float[] vec;

        public float[] normal;

        public float[] color;

        public float[] uv;

        public float[] overlay;

        public float[] lightmap;

        public Vertex(CachedFormat format) {
            this.format = format;
            this.raw = new float[format.elementCount][4];
            preProcess();
        }

        public Vertex(Vertex other) {
            this.format = other.format;
            this.raw = (float[][])other.raw.clone();
            for (int v = 0; v < this.format.elementCount; v++)
                this.raw[v] = (float[])other.raw[v].clone();
            preProcess();
        }

        public void preProcess() {
            if (this.format.hasPosition)
                this.vec = this.raw[this.format.positionIndex];
            if (this.format.hasNormal)
                this.normal = this.raw[this.format.normalIndex];
            if (this.format.hasColor)
                this.color = this.raw[this.format.colorIndex];
            if (this.format.hasUV)
                this.uv = this.raw[this.format.uvIndex];
            if (this.format.hasOverlay)
                this.overlay = this.raw[this.format.overlayIndex];
            if (this.format.hasLightMap)
                this.lightmap = this.raw[this.format.lightMapIndex];
        }

        public Vertex copy() {
            return new Vertex(this);
        }

        public void reset(CachedFormat format) {
            if (!this.format.equals(format) && format.elementCount > this.raw.length)
                this.raw = new float[format.elementCount][4];
            this.format = format;
            this.vec = null;
            this.normal = null;
            this.color = null;
            this.uv = null;
            this.lightmap = null;
            preProcess();
        }
    }
}
