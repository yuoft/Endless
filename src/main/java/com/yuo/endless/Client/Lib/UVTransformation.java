package com.yuo.endless.Client.Lib;


public abstract class UVTransformation extends ITransformation<UV, UVTransformation> implements IVertexOperation {
    public static final int operationIndex = IVertexOperation.registerOperation();

    public UVTransformation at(UV point) {
        return new UVTransformationList(new UVTranslation(-point.u, -point.v), this, new UVTranslation(point.u, point.v));
    }

    public UVTransformationList with(UVTransformation t) {
        return new UVTransformationList(this, t);
    }

    public boolean load(CCRenderState ccrs) {
        return !isRedundant();
    }

    public int operationID() {
        return operationIndex;
    }

    public void operate(CCRenderState ccrs) {
        apply(ccrs.vert.uv);
        ccrs.sprite = null;
    }
}