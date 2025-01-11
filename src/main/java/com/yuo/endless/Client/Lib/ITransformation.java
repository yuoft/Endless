package com.yuo.endless.Client.Lib;

public abstract class ITransformation<Vector, Transformation extends ITransformation> {
    public abstract void apply(Vector paramVector);

    public abstract Transformation at(Vector paramVector);

    public abstract Transformation with(Transformation paramTransformation);

    public Transformation merge(Transformation next) {
        return null;
    }

    public boolean isRedundant() {
        return false;
    }

    public abstract Transformation inverse();
}
