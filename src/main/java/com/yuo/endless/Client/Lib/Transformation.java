package com.yuo.endless.Client.Lib;

public abstract class Transformation extends ITransformation<Vector3, Transformation> implements IVertexOperation {
    public Transformation at(Vector3 m) {
        return new TransformationList(new Translation(-m.x, -m.y, -m.z), this, m.translation());
    }

    public TransformationList with(Transformation t) {
        return new TransformationList(this, t);
    }

    public boolean load(CCRenderState c) {
        c.pipeline.addRequirement(c.normalAttrib.operationID());
        return !isRedundant();
    }

    public void operate(CCRenderState c) {
        apply(c.vert.vec);
        if (c.normalAttrib.active)
            applyN(c.normal);
    }

    public int operationID() {
        return IVertexOperation.registerOperation();
    }

    public abstract void applyN(Vector3 paramVector3);

    public abstract void apply(Matrix4 paramMatrix4);
}

