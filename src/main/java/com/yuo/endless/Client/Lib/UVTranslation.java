package com.yuo.endless.Client.Lib;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class UVTranslation extends UVTransformation {
    public double du;

    public double dv;

    public UVTranslation(double u, double v) {
        this.du = u;
        this.dv = v;
    }

    public void apply(UV uv) {
        uv.u += this.du;
        uv.v += this.dv;
    }

    public UVTransformation at(UV point) {
        return this;
    }

    public UVTransformation inverse() {
        return new UVTranslation(-this.du, -this.dv);
    }

    public boolean isRedundant() {
        return (MathHelper.between(-1.0E-5D, this.du, 1.0E-5D) && MathHelper.between(-1.0E-5D, this.dv, 1.0E-5D));
    }

    public UVTransformation merge(UVTransformation next) {
        if (next instanceof UVTranslation) {
            UVTranslation t = (UVTranslation)next;
            return new UVTranslation(this.du + t.du, this.dv + t.dv);
        }
        return null;
    }

    public String toString() {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "UVTranslation(" + new BigDecimal(this.du, cont) + ", " + new BigDecimal(this.dv, cont) + ")";
    }
}
