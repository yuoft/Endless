package com.yuo.endless.Client.Lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TransformationList extends Transformation {
    ArrayList<Transformation> transformations = new ArrayList<>();

    Matrix4 mat;

    public TransformationList(List<Transformation> transforms) {
        for (Transformation t : transforms) {
            if (t instanceof TransformationList) {
                this.transformations.addAll(((TransformationList)t).transformations);
                continue;
            }
            this.transformations.add(t);
        }
        compact();
    }

    public Matrix4 compile() {
        if (this.mat == null) {
            this.mat = new Matrix4();
            for (int i = this.transformations.size() - 1; i >= 0; i--)
                ((Transformation)this.transformations.get(i)).apply(this.mat);
        }
        return this.mat;
    }

    public void apply(Vector3 vec) {
        if (this.mat != null) {
            this.mat.apply(vec);
        } else {
            for (Transformation transformation : this.transformations)
                transformation.apply(vec);
        }
    }

    public void applyN(Vector3 normal) {
        if (this.mat != null) {
            this.mat.applyN(normal);
        } else {
            for (Transformation transformation : this.transformations)
                transformation.applyN(normal);
        }
    }

    public TransformationList(Transformation... transforms) {
        this(Arrays.asList(transforms));
    }

    public void apply(Matrix4 mat) {
        mat.multiply(compile());
    }

    public boolean isRedundant() {
        return (this.transformations.isEmpty());
    }

    public TransformationList with(Transformation t) {
        if (t.isRedundant())
            return this;
        this.mat = null;
        if (t instanceof TransformationList) {
            this.transformations.addAll(((TransformationList)t).transformations);
        } else {
            this.transformations.add(t);
        }
        compact();
        return this;
    }

    void compact() {
        ArrayList<Transformation> newList = new ArrayList<>(this.transformations.size());
        Iterator<Transformation> iterator = this.transformations.iterator();
        Transformation prev = null;
        while (iterator.hasNext()) {
            Transformation t = iterator.next();
            if (t.isRedundant())
                continue;
            if (prev != null) {
                Transformation m = prev.merge(t);
                if (m == null) {
                    newList.add(prev);
                } else if (m.isRedundant()) {
                    t = null;
                } else {
                    t = m;
                }
            }
            prev = t;
        }
        if (prev != null)
            newList.add(prev);
        if (newList.size() < this.transformations.size()) {
            this.transformations = newList;
            this.mat = null;
        }
        if (this.transformations.size() > 3 && this.mat == null)
            compile();
    }

    public Transformation inverse() {
        TransformationList rev = new TransformationList();
        for (int i = this.transformations.size() - 1; i >= 0; i--)
            rev.with(this.transformations.get(i).inverse());
        return rev;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Transformation t : this.transformations)
            s.append("\n").append(t.toString());
        return s.toString().trim();
    }
}