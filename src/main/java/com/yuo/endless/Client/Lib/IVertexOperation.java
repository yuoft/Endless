package com.yuo.endless.Client.Lib;

public interface IVertexOperation {
    static int registerOperation() {
        return VertexOperationRegistry.n++;
    }

    static int operationCount() {
        return VertexOperationRegistry.n;
    }

    boolean load(CCRenderState paramCCRenderState);

    void operate(CCRenderState paramCCRenderState);

    int operationID();

    class VertexOperationRegistry {
        static int n;
    }
}

