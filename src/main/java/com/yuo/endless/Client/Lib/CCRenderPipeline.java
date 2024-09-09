package com.yuo.endless.Client.Lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CCRenderPipeline {
    CCRenderState renderState;

    PipelineBuilder builder;

    List<VertexAttribute<?>> attribs = new ArrayList<>();

    List<IVertexOperation> ops = new ArrayList<>();

    List<PipelineNode> nodes = new ArrayList<>();

    List<IVertexOperation> sorted = new ArrayList<>();

    PipelineNode loading;

    public CCRenderPipeline(CCRenderState renderState) {
        this.renderState = renderState;
        this.builder = new PipelineBuilder(renderState);
    }

    public void setPipeline(IVertexOperation... ops) {
        this.ops.clear();
        Collections.addAll(this.ops, ops);
        rebuild();
    }

    void reset() {
        this.ops.clear();
        unbuild();
    }

    void unbuild() {
        for (int i = 0; i < this.attribs.size(); i++) {
            VertexAttribute<?> attrib = this.attribs.get(i);
            attrib.active = false;
        }
        this.attribs.clear();
        this.sorted.clear();
    }

    void rebuild() {
        if (this.renderState.model == null || this.renderState.fmt == null)
            return;
        unbuild();
        if (this.renderState.cFmt.hasNormal)
            addAttribute(this.renderState.normalAttrib);
        if (this.renderState.cFmt.hasColor)
            addAttribute(this.renderState.colourAttrib);
        if (this.renderState.computeLighting)
            addAttribute(this.renderState.lightingAttrib);
        if (this.ops.isEmpty())
            return;
        while (this.nodes.size() < IVertexOperation.operationCount())
            this.nodes.add(new PipelineNode());
        int i;
        for (i = 0; i < this.ops.size(); i++) {
            IVertexOperation op = this.ops.get(i);
            this.loading = this.nodes.get(op.operationID());
            boolean loaded = op.load(this.renderState);
            if (loaded)
                this.loading.op = op;
            if (op instanceof VertexAttribute)
                if (loaded) {
                    this.attribs.add((VertexAttribute)op);
                } else {
                    ((VertexAttribute)op).active = false;
                }
        }
        for (i = 0; i < this.nodes.size(); i++) {
            PipelineNode node = this.nodes.get(i);
            node.add();
        }
    }

    public void addRequirement(int opRef) {
        this.loading.deps.add(this.nodes.get(opRef));
    }

    public void addDependency(VertexAttribute<?> attrib) {
        this.loading.deps.add(this.nodes.get(attrib.operationID()));
        addAttribute(attrib);
    }

    public void addAttribute(VertexAttribute<?> attrib) {
        if (!attrib.active) {
            this.ops.add(attrib);
            attrib.active = true;
        }
    }

    public void operate() {
        for (int i = 0; i < this.sorted.size(); i++) {
            IVertexOperation aSorted = this.sorted.get(i);
            aSorted.operate(this.renderState);
        }
    }

    public class PipelineBuilder {
        CCRenderState renderState;

        public PipelineBuilder(CCRenderState renderState) {
            this.renderState = renderState;
        }

        public PipelineBuilder add(IVertexOperation op) {
            CCRenderPipeline.this.ops.add(op);
            return this;
        }

        public PipelineBuilder add(IVertexOperation... ops) {
            Collections.addAll(CCRenderPipeline.this.ops, ops);
            return this;
        }
    }

    class PipelineNode {
        public ArrayList<PipelineNode> deps = new ArrayList<>();

        public IVertexOperation op;

        public void add() {
            if (this.op == null)
                return;
            for (int i = 0; i < this.deps.size(); i++) {
                PipelineNode dep = this.deps.get(i);
                dep.add();
            }
            this.deps.clear();
            CCRenderPipeline.this.sorted.add(this.op);
            this.op = null;
        }
    }
}
