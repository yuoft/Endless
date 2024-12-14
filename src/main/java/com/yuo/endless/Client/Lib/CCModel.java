package com.yuo.endless.Client.Lib;

import java.util.ArrayList;
import java.util.List;

public class CCModel implements IVertexSource, Copyable<CCModel> {
    public int vertexMode;

    public int vp;

    public Vertex5[] verts;

    public ArrayList<Object> attributes = new ArrayList<>();

    CCModel(int vertexMode) {
        if (vertexMode != 7 && vertexMode != 4)
            throw new IllegalArgumentException();
        this.vertexMode = vertexMode;
        this.vp = (vertexMode == 7) ? 4 : 3;
    }

    public Vector3[] normals() {
        return (Vector3[]) getAttributes((AttributeKey) NormalAttribute.attributeKey);
    }

    public Vertex5[] getVertices() {
        return this.verts;
    }

    public <T> T getAttributes(AttributeKey<T> attr) {
        if (attr.attributeKeyIndex < this.attributes.size())
            return MathHelper.unsafeCast(this.attributes.get(attr.attributeKeyIndex));
        return null;
    }

    public boolean hasAttribute(AttributeKey<?> attr) {
        return (attr.attributeKeyIndex < this.attributes.size() && this.attributes.get(attr.attributeKeyIndex) != null);
    }

    public void prepareVertex(CCRenderState ccrs) {}

    <T> T getOrAllocate(AttributeKey<T> attr) {
        T array = getAttributes(attr);
        if (array == null) {
            while (this.attributes.size() <= attr.attributeKeyIndex)
                this.attributes.add(null);
            this.attributes.set(attr.attributeKeyIndex, array = attr.newArray(this.verts.length));
        }
        return array;
    }

    public CCModel apply(Transformation t) {
        for (Vertex5 vert : this.verts)
            vert.apply(t);
        Vector3[] normals = normals();
        if (normals != null)
            for (Vector3 normal : normals)
                t.applyN(normal);
        return this;
    }

    public CCModel apply(UVTransformation uvt) {
        for (Vertex5 vert : this.verts)
            vert.apply(uvt);
        return this;
    }

    public void render(CCRenderState state, IVertexOperation... ops) {
        render(state, 0, this.verts.length, ops);
    }

    void render(CCRenderState state, int start, int end, IVertexOperation... ops) {
        state.setPipeline(this, start, end, ops);
        state.render();
    }

    static CCModel newModel(int vertexMode, int numVerts) {
        CCModel model = newModel(vertexMode);
        model.verts = new Vertex5[numVerts];
        return model;
    }

    static CCModel newModel(int vertexMode) {
        return new CCModel(vertexMode);
    }

    public static CCModel createModel(List<Vector3> verts, List<Vector3> uvs, List<Vector3> normals, int vertexMode, List<int[]> polys) {
        int vp = (vertexMode == 7) ? 4 : 3;
        if (polys.size() < vp || polys.size() % vp != 0)
            throw new IllegalArgumentException();
        boolean hasNormals = (((int[])polys.get(0))[2] > 0);
        CCModel model = newModel(vertexMode, polys.size());
        if (hasNormals)
            model.getOrAllocate((AttributeKey)NormalAttribute.attributeKey);
        for (int i = 0; i < polys.size(); i++) {
            int[] ai = polys.get(i);
            Vector3 vert = verts.get(ai[0] - 1).copy();
            Vector3 uv = (ai[1] <= 0) ? new Vector3() : uvs.get(ai[1] - 1).copy();
            if (((ai[2] > 0)) != hasNormals)
                throw new IllegalArgumentException();
            model.verts[i] = new Vertex5(vert, uv.x, uv.y);
            if (hasNormals)
                model.normals()[i] = normals.get(ai[2] - 1).copy();
        }
        return model;
    }

    static void copy(CCModel src, int srcpos, CCModel dst, int destpos, int length) {
        for (int k = 0; k < length; k++)
            dst.verts[destpos + k] = src.verts[srcpos + k].copy();
        for (int i = 0; i < src.attributes.size(); i++) {
            if (src.attributes.get(i) != null)
                arrayCopy(src.attributes.get(i), srcpos, dst.getOrAllocate(AttributeKey.AttributeKeyRegistry.getAttributeKey(i)), destpos, length);
        }
    }

    static void arrayCopy(Object s, int p, Object o, int f, int v) {
        System.arraycopy(s, p, o, f, v);
        if (o instanceof Copyable[]) {
            Object[] oa = (Object[])o;
            Copyable[] arrayOfCopyable = (Copyable[])o;
            for (int i = f; i < f + v; i++) {
                if (arrayOfCopyable[i] != null)
                    oa[i] = arrayOfCopyable[i].copy();
            }
        }
    }

    public CCModel apply(Transformation t, int srcpos, int destpos, int length) {
        for (int k = 0; k < length; k++) {
            this.verts[destpos + k] = this.verts[srcpos + k].copy();
            (this.verts[destpos + k]).vec.apply(t);
        }
        Vector3[] normals = normals();
        if (normals != null)
            for (int i = 0; i < length; i++) {
                normals[destpos + i] = normals[srcpos + i].copy();
                t.applyN(normals[destpos + i]);
            }
        return this;
    }

    public CCModel copy() {
        CCModel model = newModel(this.vertexMode, this.verts.length);
        copy(this, 0, model, 0, this.verts.length);
        return model;
    }
}