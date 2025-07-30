package com.yuo.endless.Client.Lib;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class Quad implements IVertexProducer, IVertexConsumer {
    public CachedFormat format;
    public int tintIndex = -1;
    public Direction orientation;
    public boolean diffuseLighting = true;
    public TextureAtlasSprite sprite;
    public Quad.Vertex[] vertices = new Quad.Vertex[4];
    public boolean full;
    private int vertexIndex = 0;
    private final Vector3 v1 = new Vector3();
    private final Vector3 v2 = new Vector3();
    private final Vector3 t = new Vector3();

    public Quad() {
    }

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

    public void put(int element, float... data) {
        if (this.full) {
            throw new RuntimeException("Unable to add data when full.");
        } else {
            Quad.Vertex v = this.vertices[this.vertexIndex];
            if (v == null) {
                v = new Quad.Vertex(this.format);
                this.vertices[this.vertexIndex] = v;
            }

            System.arraycopy(data, 0, v.raw[element], 0, data.length);
            if (element == this.format.elementCount - 1) {
                ++this.vertexIndex;
                if (this.vertexIndex == 4) {
                    this.vertexIndex = 0;
                    this.full = true;
                    if (this.orientation == null) {
                        this.calculateOrientation(false);
                    }
                }
            }

        }
    }

    public void put(Quad quad) {
        this.copyFrom(quad);
    }

    public void calculateOrientation(boolean setNormal) {
        this.v1.set(this.vertices[3].vec).subtract(this.t.set(this.vertices[1].vec));
        this.v2.set(this.vertices[2].vec).subtract(this.t.set(this.vertices[0].vec));
        Vector3 normal = this.v2.crossProduct(this.v1).normalize();
        if (this.format.hasNormal && setNormal) {
            Quad.Vertex[] var3 = this.vertices;

            for (Vertex vertex : var3) {
                vertex.normal[0] = (float) normal.x;
                vertex.normal[1] = (float) normal.y;
                vertex.normal[2] = (float) normal.z;
                vertex.normal[3] = 0.0F;
            }
        }

        this.orientation = Direction.getNearest(normal.x, normal.y, normal.z);
    }

    public Quad copy() {
        if (!this.full) {
            throw new RuntimeException("Only copying full quads is supported.");
        } else {
            Quad quad = new Quad(this.format);
            quad.tintIndex = this.tintIndex;
            quad.orientation = this.orientation;
            quad.diffuseLighting = this.diffuseLighting;
            quad.sprite = this.sprite;
            quad.full = true;

            for(int i = 0; i < 4; ++i) {
                quad.vertices[i] = this.vertices[i].copy();
            }

            return quad;
        }
    }

    public void copyFrom(Quad quad) {
        this.tintIndex = quad.tintIndex;
        this.orientation = quad.orientation;
        this.diffuseLighting = quad.diffuseLighting;
        this.sprite = quad.sprite;
        this.full = quad.full;

        for(int v = 0; v < 4; ++v) {
            for(int e = 0; e < this.format.elementCount; ++e) {
                System.arraycopy(quad.vertices[v].raw[e], 0, this.vertices[v].raw[e], 0, 4);
            }
        }

    }

    public void reset(CachedFormat format) {
        this.format = format;
        this.tintIndex = -1;
        this.orientation = null;
        this.diffuseLighting = true;
        this.sprite = null;

        for(int i = 0; i < this.vertices.length; ++i) {
            Quad.Vertex v = this.vertices[i];
            if (v == null) {
                this.vertices[i] = v = new Quad.Vertex(format);
            }

            v.reset(format);
        }

        this.vertexIndex = 0;
        this.full = false;
    }

    public BakedQuad bake() {
        int[] packedData = new int[this.format.format.getVertexSize()];

        for(int v = 0; v < 4; ++v) {
            for(int e = 0; e < this.format.elementCount; ++e) {
                VertexUtils.pack(this.vertices[v].raw[e], packedData, this.format.format, v, e);
            }
        }

        return this.makeQuad(packedData);
    }

    private BakedQuad makeQuad(int[] packedData) {
        if (this.format.format != DefaultVertexFormat.BLOCK) {
            throw new IllegalStateException("Unable to bake this quad to the specified format. " + this.format.format);
        } else {
            return new BakedQuad(packedData, this.tintIndex, this.orientation, this.sprite, this.diffuseLighting);
        }
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
            this.preProcess();
        }

        public Vertex(Quad.Vertex other) {
            this.format = other.format;
            this.raw = (float[][])other.raw.clone();

            for(int v = 0; v < this.format.elementCount; ++v) {
                this.raw[v] = (float[])other.raw[v].clone();
            }

            this.preProcess();
        }

        public void preProcess() {
            if (this.format.hasPosition) {
                this.vec = this.raw[this.format.positionIndex];
            }

            if (this.format.hasNormal) {
                this.normal = this.raw[this.format.normalIndex];
            }

            if (this.format.hasColor) {
                this.color = this.raw[this.format.colorIndex];
            }

            if (this.format.hasUV) {
                this.uv = this.raw[this.format.uvIndex];
            }

            if (this.format.hasOverlay) {
                this.overlay = this.raw[this.format.overlayIndex];
            }

            if (this.format.hasLightMap) {
                this.lightmap = this.raw[this.format.lightMapIndex];
            }

        }


        public Quad.Vertex copy() {
            return new Quad.Vertex(this);
        }

        public void reset(CachedFormat format) {
            if (!this.format.equals(format) && format.elementCount > this.raw.length) {
                this.raw = new float[format.elementCount][4];
            }

            this.format = format;
            this.vec = null;
            this.normal = null;
            this.color = null;
            this.uv = null;
            this.lightmap = null;
            this.preProcess();
        }
    }
}
