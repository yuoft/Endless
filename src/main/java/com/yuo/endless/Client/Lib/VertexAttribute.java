package com.yuo.endless.Client.Lib;

public abstract class VertexAttribute<T> implements IVertexOperation {
    public boolean active = false;

    AttributeKey<T> key;

    public VertexAttribute(AttributeKey<T> key) {
        this.key = key;
    }

    public int operationID() {
        return this.key.operationIndex;
    }
}
