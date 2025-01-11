package com.yuo.endless.Client.Lib;

import java.util.ArrayList;
import java.util.Iterator;

public class UVTransformationList extends UVTransformation {
    ArrayList<UVTransformation> transformations = new ArrayList<>();

    public boolean isRedundant() {
        return (this.transformations.isEmpty());
    }

    public UVTransformationList(UVTransformation... transforms) {
        for (UVTransformation t : transforms) {
            if (t instanceof UVTransformationList) {
                this.transformations.addAll(((UVTransformationList)t).transformations);
            } else {
                this.transformations.add(t);
            }
        }
        compact();
    }

    public void apply(UV uv) {
        for (UVTransformation transformation : this.transformations)
            transformation.apply(uv);
    }

    public UVTransformationList with(UVTransformation t) {
        if (t.isRedundant())
            return this;
        if (t instanceof UVTransformationList) {
            this.transformations.addAll(((UVTransformationList)t).transformations);
        } else {
            this.transformations.add(t);
        }
        compact();
        return this;
    }

    private void compact() {
        ArrayList<UVTransformation> newList = new ArrayList<>(this.transformations.size());
        Iterator<UVTransformation> iterator = this.transformations.iterator();
        UVTransformation prev = null;
        while (iterator.hasNext()) {
            UVTransformation t = iterator.next();
            if (t.isRedundant())
                continue;
            if (prev != null) {
                UVTransformation m = prev.merge(t);
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
        if (newList.size() < this.transformations.size())
            this.transformations = newList;
    }

    public UVTransformation inverse() {
        UVTransformationList rev = new UVTransformationList(new UVTransformation[0]);
        for (int i = this.transformations.size() - 1; i >= 0; i--)
            rev.with(((UVTransformation)this.transformations.get(i)).inverse());
        return rev;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (UVTransformation t : this.transformations)
            s.append("\n").append(t.toString());
        return s.toString().trim();
    }
}
