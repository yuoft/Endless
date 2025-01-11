package com.yuo.endless.Client.Lib;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class CCRenderState {
    static ThreadLocal<CCRenderState> instances = ThreadLocal.withInitial(CCRenderState::new);

    public VertexAttribute<Vector3[]> normalAttrib = new NormalAttribute();

    public VertexAttribute<int[]> colourAttrib = new ColourAttribute();

    public VertexAttribute<int[]> lightingAttrib = new LightingAttribute();

    public VertexAttribute<int[]> sideAttrib = new SideAttribute();

    public IVertexSource model;

    public int firstVertexIndex;

    public int lastVertexIndex;

    public int vertexIndex;

    public int baseColour;

    public int alphaOverride;

    public int colour;

    public int brightness;

    public int overlay;

    public int side;

    public CCRenderPipeline pipeline;

    public VertexConsumer r;

    public VertexFormat fmt;

    public CachedFormat cFmt;

    public boolean computeLighting;

    public final Vertex5 vert = new Vertex5();

    public final Vector3 normal = new Vector3();

    public TextureAtlasSprite sprite;

    CCRenderState() {
        this.pipeline = new CCRenderPipeline(this);
    }

    public static CCRenderState instance() {
        return instances.get();
    }

    public void bind(VertexConsumer consumer, VertexFormat format) {
        this.r = consumer;
        this.fmt = format;
        this.cFmt = CachedFormat.lookup(format);
    }

    public void bind(RenderType renderType, MultiBufferSource getter, PoseStack mStack) {
        bind(new TransformingVertexBuilder(getter.getBuffer(renderType), mStack), renderType.format());
    }

    public void reset() {
        this.model = null;
        this.pipeline.reset();
        this.computeLighting = true;
        this.colour = this.baseColour = this.alphaOverride = -1;
    }

    public void setPipeline(IVertexSource model, int start, int end, IVertexOperation... ops) {
        this.pipeline.reset();
        setModel(model, start, end);
        this.pipeline.setPipeline(ops);
    }

    public void bindModel(IVertexSource model) {
        if (this.model != model) {
            this.model = model;
            this.pipeline.rebuild();
        }
    }

    public void setModel(IVertexSource source) {
        setModel(source, 0, (source.getVertices()).length);
    }

    public void setModel(IVertexSource source, int start, int end) {
        bindModel(source);
        setVertexRange(start, end);
    }

    public void setVertexRange(int start, int end) {
        this.firstVertexIndex = start;
        this.lastVertexIndex = end;
    }

    public void render() {
        Vertex5[] verts = this.model.getVertices();
        for (this.vertexIndex = this.firstVertexIndex; this.vertexIndex < this.lastVertexIndex; this.vertexIndex++) {
            this.model.prepareVertex(this);
            this.vert.set(verts[this.vertexIndex]);
            runPipeline();
            writeVert();
        }
    }

    public void runPipeline() {
        this.pipeline.operate();
    }

    public void writeVert() {
        if (this.r instanceof ISpriteAwareVertexBuilder)
            ((ISpriteAwareVertexBuilder)this.r).sprite(this.sprite);
        ImmutableList<VertexFormatElement> elements = this.fmt.getElements();
        for (int e = 0; e < elements.size(); e++) {
            int idx;
            VertexFormatElement vertexFormatElement = elements.get(e);
            switch (vertexFormatElement.getUsage()) {
                case POSITION:
                    this.r.vertex(this.vert.vec.x, this.vert.vec.y, this.vert.vec.z);
                    break;
                case UV:
                    idx = vertexFormatElement.getIndex();
                    switch (idx) {
                        case 0:
                            this.r.uv((float)this.vert.uv.u, (float)this.vert.uv.v);
                            break;
                        case 1:
                            this.r.overlayCoords(this.overlay);
                            break;
                        case 2:
                            this.r.uv2(this.brightness);
                            break;
                    }
                    break;
                case COLOR:
                    if (this.r instanceof BufferBuilder && ((BufferBuilder)this.r).defaultColorSet) {
                        ((BufferBuilder)this.r).nextElement();
                        break;
                    }
                    this.r.color(this.colour >>> 24, this.colour >> 16 & 0xFF, this.colour >> 8 & 0xFF, (this.alphaOverride >= 0) ? this.alphaOverride : (this.colour & 0xFF));
                    break;
                case NORMAL:
                    this.r.normal((float)this.normal.x, (float)this.normal.y, (float)this.normal.z);
                    break;
            }
        }
        this.r.endVertex();
    }
}
