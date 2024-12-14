package com.yuo.endless.Client.Lib;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Quat implements Copyable<Quat> {
    public double x;

    public double y;

    public double z;

    public double s;

    public Quat() {
        this.s = 1.0D;
        this.x = 0.0D;
        this.y = 0.0D;
        this.z = 0.0D;
    }

    public Quat(Quat quat) {
        this.x = quat.x;
        this.y = quat.y;
        this.z = quat.z;
        this.s = quat.s;
    }

    public Quat set(Quat quat) {
        this.x = quat.x;
        this.y = quat.y;
        this.z = quat.z;
        this.s = quat.s;
        return this;
    }

    public Quat set(double d, double d1, double d2, double d3) {
        this.x = d1;
        this.y = d2;
        this.z = d3;
        this.s = d;
        return this;
    }

    public static Quat aroundAxis(double ax, double ay, double az, double angle) {
        return (new Quat()).setAroundAxis(ax, ay, az, angle);
    }

    public static Quat aroundAxis(Vector3 axis, double angle) {
        return aroundAxis(axis.x, axis.y, axis.z, angle);
    }

    public Quat copy() {
        return new Quat(this);
    }

    public Quat setAroundAxis(double ax, double ay, double az, double angle) {
        angle *= 0.5D;
        double d4 = Math.sin(angle);
        return set(Math.cos(angle), ax * d4, ay * d4, az * d4);
    }

    public Quat multiply(Quat quat) {
        double d = this.s * quat.s - this.x * quat.x - this.y * quat.y - this.z * quat.z;
        double d1 = this.s * quat.x + this.x * quat.s - this.y * quat.z + this.z * quat.y;
        double d2 = this.s * quat.y + this.x * quat.z + this.y * quat.s - this.z * quat.x;
        double d3 = this.s * quat.z - this.x * quat.y + this.y * quat.x + this.z * quat.s;
        this.s = d;
        this.x = d1;
        this.y = d2;
        this.z = d3;
        return this;
    }

    public void rotate(Vector3 vec) {
        double d = -this.x * vec.x - this.y * vec.y - this.z * vec.z;
        double d1 = this.s * vec.x + this.y * vec.z - this.z * vec.y;
        double d2 = this.s * vec.y - this.x * vec.z + this.z * vec.x;
        double d3 = this.s * vec.z + this.x * vec.y - this.y * vec.x;
        vec.x = d1 * this.s - d * this.x - d2 * this.z + d3 * this.y;
        vec.y = d2 * this.s - d * this.y + d1 * this.z - d3 * this.x;
        vec.z = d3 * this.s - d * this.z - d1 * this.y + d2 * this.x;
    }

    public String toString() {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Quat(" + new BigDecimal(this.s, cont) + ", " + new BigDecimal(this.x, cont) + ", " + new BigDecimal(this.y, cont) + ", " + new BigDecimal(this.z, cont) + ")";
    }
}
