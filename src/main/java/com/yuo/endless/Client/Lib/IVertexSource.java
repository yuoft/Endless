package com.yuo.endless.Client.Lib;

public interface IVertexSource {
    Vertex5[] getVertices();

    <T> T getAttributes(AttributeKey<T> paramAttributeKey);

    boolean hasAttribute(AttributeKey<?> paramAttributeKey);

    void prepareVertex(CCRenderState paramCCRenderState);
}
