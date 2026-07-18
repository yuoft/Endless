package com.yuo.endless.client.lib;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.yuo.endless.client.lib.AttributeKey.AttributeKeyRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.yuo.endless.client.lib.SneakyUtils.unsafeCast;


public class CCModel implements IVertexSource, Copyable<CCModel> {

    public final VertexFormat.Mode vertexMode;
    public final int vp;
    public Vertex5[] verts;
    public ArrayList<Object> attributes = new ArrayList<>();

    protected CCModel(VertexFormat.Mode vertexMode) {
        if (vertexMode != VertexFormat.Mode.QUADS && vertexMode != VertexFormat.Mode.TRIANGLES) {
            throw new IllegalArgumentException("Models must be QUADS or TRIANGLES");
        }

        this.vertexMode = vertexMode;
        vp = vertexMode == VertexFormat.Mode.QUADS ? 4 : 3;
    }

    public Vector3[] normals() {
        return getAttribute(NormalAttribute.attributeKey);
    }

    @Nullable
    public ModelMaterial material() {
        return getAttribute(ModelMaterial.MATERIAL_KEY);
    }

    @Override
    public Vertex5[] getVertices() {
        return verts;
    }

    @Override
    public <T> T getAttribute(AttributeKey<T> attr) {
        if (attr.attributeKeyIndex < attributes.size()) {
            return unsafeCast(attributes.get(attr.attributeKeyIndex));
        }
        return null;
    }

    @Override
    public boolean hasAttribute(AttributeKey<?> attr) {
        return attr.attributeKeyIndex < attributes.size() && attributes.get(attr.attributeKeyIndex) != null;
    }

    @Override
    public void prepareVertex(CCRenderState ccrs) {
    }

    /**
     * Gets an attribute.
     * <p>
     * If the model doesn't have this attribute, a new storage will be created
     * and set.
     *
     * @param attr The attribute to get.
     * @return The value.
     */
    public <T> T getOrAllocate(AttributeKey<T> attr) {
        T value = getAttribute(attr);
        if (value == null) {
            allocateAttr(attr);
            attributes.set(attr.attributeKeyIndex, value = attr.createDefault(verts.length));
        }
        return value;
    }

    /**
     * Set an attribute.
     *
     * @param attr  The attribute to set.
     * @param value The value to set.
     */
    public <T> void setAttribute(AttributeKey<T> attr, @Nullable T value) {
        allocateAttr(attr);
        attributes.set(attr.attributeKeyIndex, value);

    }

    /**
     * Ensires the specified {@link AttributeKey}'s index
     * is available in the {@link #attributes} list.
     *
     * @param attr The attribute key.
     */
    private void allocateAttr(AttributeKey<?> attr) {
        if (attr.attributeKeyIndex >= attributes.size()) {
            while (attributes.size() <= attr.attributeKeyIndex) {
                attributes.add(null);
            }
        }
    }

    /**
     * Generates a box, uv mapped to be the same as a minecraft block with the same bounds
     *
     * @param i      The vertex index to start generating at
     * @param bounds The bounds of the block, 0 to 1
     * @return The generated model. When rendering an icon will need to be supplied for the UV transformation.
     */
    public CCModel generateBlock(int i, Cuboid6 bounds) {
        return generateBlock(i, bounds, 0);
    }

    public CCModel generateBlock(int i, Cuboid6 bounds, int mask) {
        return generateBlock(i, bounds.min.x, bounds.min.y, bounds.min.z, bounds.max.x, bounds.max.y, bounds.max.z, mask);
    }

    public CCModel generateBlock(int i, double x1, double y1, double z1, double x2, double y2, double z2) {
        return generateBlock(i, x1, y1, z1, x2, y2, z2, 0);
    }

    /**
     * Generates a box, uv mapped to be the same as a minecraft block with the same bounds
     *
     * @param i    The vertex index to start generating at
     * @param x1   minX
     * @param y1   minY
     * @param z1   minZ
     * @param x2   maxX
     * @param y2   maxY
     * @param z2   maxZ
     * @param mask A bitmask of sides NOT to generate. I high bit at index s means side s will not be generated
     * @return The generated model. When rendering an icon will need to be supplied for the UV transformation.
     */
    public CCModel generateBlock(int i, double x1, double y1, double z1, double x2, double y2, double z2, int mask) {
        double u1, v1, u2, v2;

        if ((mask & 1) == 0) {//bottom face
            u1 = x1;
            v1 = z1;
            u2 = x2;
            v2 = z2;
            verts[i++] = new Vertex5(x1, y1, z2, u1, v2, 0);
            verts[i++] = new Vertex5(x1, y1, z1, u1, v1, 0);
            verts[i++] = new Vertex5(x2, y1, z1, u2, v1, 0);
            verts[i++] = new Vertex5(x2, y1, z2, u2, v2, 0);
        }

        if ((mask & 2) == 0) {//top face
            u1 = x1;
            v1 = z1;
            u2 = x2;
            v2 = z2;
            verts[i++] = new Vertex5(x2, y2, z2, u2, v2, 1);
            verts[i++] = new Vertex5(x2, y2, z1, u2, v1, 1);
            verts[i++] = new Vertex5(x1, y2, z1, u1, v1, 1);
            verts[i++] = new Vertex5(x1, y2, z2, u1, v2, 1);
        }

        if ((mask & 4) == 0) {//north face
            u1 = 1 - x1;
            v1 = 1 - y2;
            u2 = 1 - x2;
            v2 = 1 - y1;
            verts[i++] = new Vertex5(x1, y1, z1, u1, v2, 2);
            verts[i++] = new Vertex5(x1, y2, z1, u1, v1, 2);
            verts[i++] = new Vertex5(x2, y2, z1, u2, v1, 2);
            verts[i++] = new Vertex5(x2, y1, z1, u2, v2, 2);
        }

        if ((mask & 8) == 0) {//south face
            u1 = x1;
            v1 = 1 - y2;
            u2 = x2;
            v2 = 1 - y1;
            verts[i++] = new Vertex5(x2, y1, z2, u2, v2, 3);
            verts[i++] = new Vertex5(x2, y2, z2, u2, v1, 3);
            verts[i++] = new Vertex5(x1, y2, z2, u1, v1, 3);
            verts[i++] = new Vertex5(x1, y1, z2, u1, v2, 3);
        }

        if ((mask & 0x10) == 0) {//west face
            u1 = z1;
            v1 = 1 - y2;
            u2 = z2;
            v2 = 1 - y1;
            verts[i++] = new Vertex5(x1, y1, z2, u2, v2, 4);
            verts[i++] = new Vertex5(x1, y2, z2, u2, v1, 4);
            verts[i++] = new Vertex5(x1, y2, z1, u1, v1, 4);
            verts[i++] = new Vertex5(x1, y1, z1, u1, v2, 4);
        }

        if ((mask & 0x20) == 0) {//east face
            u1 = 1 - z1;
            v1 = 1 - y2;
            u2 = 1 - z2;
            v2 = 1 - y1;
            verts[i++] = new Vertex5(x2, y1, z1, u1, v2, 5);
            verts[i++] = new Vertex5(x2, y2, z1, u1, v1, 5);
            verts[i++] = new Vertex5(x2, y2, z2, u2, v1, 5);
            verts[i++] = new Vertex5(x2, y1, z2, u2, v2, 5);
        }

        return this;
    }

    public CCModel setColour(int c) {
        int[] colours = getOrAllocate(ColourAttribute.attributeKey);
        Arrays.fill(colours, c);
        return this;
    }

    public CCModel apply(Transformation t) {
        for (Vertex5 vert : verts) {
            vert.apply(t);
        }

        Vector3[] normals = normals();
        if (normals != null) {
            for (Vector3 normal : normals) {
                t.applyN(normal);
            }
        }

        return this;
    }

    public CCModel apply(UVTransformation uvt) {
        for (Vertex5 vert : verts) {
            vert.apply(uvt);
        }

        return this;
    }

    public void render(CCRenderState state, double x, double y, double z, double u, double v) {
        render(state, new Vector3(x, y, z).translation(), new UVTranslation(u, v));
    }

    public void render(CCRenderState state, double x, double y, double z, UVTransformation u) {
        render(state, new Vector3(x, y, z).translation(), u);
    }

    public void render(CCRenderState state, Transformation t, double u, double v) {
        render(state, t, new UVTranslation(u, v));
    }

    public void render(CCRenderState state, IVertexOperation... ops) {
        render(state, 0, verts.length, ops);
    }

    /**
     * Renders vertices start through start+length-1 of the model
     *
     * @param start The first vertex index to render
     * @param end   The vertex index to render until
     * @param ops   Operations to apply
     */
    public void render(CCRenderState state, int start, int end, IVertexOperation... ops) {
        state.setPipeline(this, start, end, ops);
        state.render();
    }

    public static CCModel newModel(VertexFormat.Mode vertexMode, int numVerts) {
        CCModel model = newModel(vertexMode);
        model.verts = new Vertex5[numVerts];
        return model;
    }

    public static CCModel newModel(VertexFormat.Mode vertexMode) {
        return new CCModel(vertexMode);
    }

    public static CCModel createModel(List<Vector3> verts, List<Vector3> uvs, List<Vector3> normals, VertexFormat.Mode vertexMode, List<int[]> polys) {
        int vp = vertexMode == VertexFormat.Mode.QUADS ? 4 : 3;
        if (polys.size() < vp || polys.size() % vp != 0) {
            throw new IllegalArgumentException("Invalid number of vertices for model: " + polys.size());
        }

        boolean hasNormals = polys.get(0)[2] > 0;
        CCModel model = CCModel.newModel(vertexMode, polys.size());
        if (hasNormals) {
            model.getOrAllocate(NormalAttribute.attributeKey);
        }

        for (int i = 0; i < polys.size(); i++) {
            int[] ai = polys.get(i);
            Vector3 vert = verts.get(ai[0] - 1).copy();
            Vector3 uv = ai[1] <= 0 ? new Vector3() : uvs.get(ai[1] - 1).copy();
            if (ai[2] > 0 != hasNormals) {
                throw new IllegalArgumentException("Normals are an all or nothing deal here.");
            }

            model.verts[i] = new Vertex5(vert, uv.x, uv.y);
            if (hasNormals) {
                model.normals()[i] = normals.get(ai[2] - 1).copy();
            }
        }

        return model;
    }

    /**
     * Copies a range of vertices and attributes form one model to another.
     * <p>
     * This will deeply copy all vertex data and attributes.
     * {@link ModelMaterial} attributes will only be copied to the destination model
     * if the provided {@code srcpos} and {@code destpos} are {@code 0}.
     *
     * @param src     The source model.
     * @param srcpos  The index in the source model to copy from.
     * @param dst     The destination model.
     * @param destpos The index in the destination model to copy to.
     * @param length  The number of vertices to copy.
     */
    public static void copy(CCModel src, int srcpos, CCModel dst, int destpos, int length) {
        for (int k = 0; k < length; k++) {
            dst.verts[destpos + k] = src.verts[srcpos + k].copy();
        }

        for (int i = 0; i < src.attributes.size(); i++) {
            if (src.attributes.get(i) != null) {
                AttributeKey<?> key = AttributeKeyRegistry.getAttributeKey(i);
                dst.allocateAttr(key);
                dst.attributes.set(i, key.copyRange(
                        unsafeCast(src.attributes.get(i)),
                        srcpos,
                        unsafeCast(dst.getOrAllocate(key)),
                        destpos,
                        length
                ));
            }
        }
    }

    /**
     * Generates a rotated copy of verts into this model
     */
    public CCModel apply(Transformation t, int srcpos, int destpos, int length) {
        for (int k = 0; k < length; k++) {
            verts[destpos + k] = verts[srcpos + k].copy();
            verts[destpos + k].vec.apply(t);
        }

        Vector3[] normals = normals();
        if (normals != null) {
            for (int k = 0; k < length; k++) {
                normals[destpos + k] = normals[srcpos + k].copy();
                t.applyN(normals[destpos + k]);
            }
        }

        return this;
    }

    @Override
    public CCModel copy() {
        CCModel model = newModel(vertexMode, verts.length);
        copy(this, 0, model, 0, verts.length);
        return model;
    }

}
