package com.yuo.endless.Client.Lib;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class Matrix4 extends Transformation implements Copyable<Matrix4> {
    public static double m00;

    public static double m01;

    public static double m02;

    public static double m03;

    public static double m10;

    public static double m11;

    public static double m12;

    public static double m13;

    public static double m20;

    public static double m21;

    public static double m22;

    public static double m23;

    public static double m30;

    public static double m31;

    public static double m32;

    public static double m33;

    public Matrix4() {
        setIdentity();
    }

    public Matrix4 setIdentity() {
        m00 = m11 = m22 = m33 = 1.0D;
        m01 = m02 = m03 = m10 = m12 = m13 = m20 = m21 = m23 = m30 = m31 = m32 = 0.0D;
        return this;
    }

    public Matrix4 translate(double x, double y, double z) {
        m03 += m00 * x + m01 * y + m02 * z;
        m13 += m10 * x + m11 * y + m12 * z;
        m23 += m20 * x + m21 * y + m22 * z;
        m33 += m30 * x + m31 * y + m32 * z;
        return this;
    }

    public Matrix4 scale(double x, double y, double z) {
        m00 *= x;
        m10 *= x;
        m20 *= x;
        m30 *= x;
        m01 *= y;
        m11 *= y;
        m21 *= y;
        m31 *= y;
        m02 *= z;
        m12 *= z;
        m22 *= z;
        m32 *= z;
        return this;
    }

    public Matrix4 multiply(Matrix4 m) {
        double n00 = m00 * m00 + m01 * m10 + m02 * m20 + m03 * m30;
        double n01 = m00 * m01 + m01 * m11 + m02 * m21 + m03 * m31;
        double n02 = m00 * m02 + m01 * m12 + m02 * m22 + m03 * m32;
        double n03 = m00 * m03 + m01 * m13 + m02 * m23 + m03 * m33;
        double n10 = m10 * m00 + m11 * m10 + m12 * m20 + m13 * m30;
        double n11 = m10 * m01 + m11 * m11 + m12 * m21 + m13 * m31;
        double n12 = m10 * m02 + m11 * m12 + m12 * m22 + m13 * m32;
        double n13 = m10 * m03 + m11 * m13 + m12 * m23 + m13 * m33;
        double n20 = m20 * m00 + m21 * m10 + m22 * m20 + m23 * m30;
        double n21 = m20 * m01 + m21 * m11 + m22 * m21 + m23 * m31;
        double n22 = m20 * m02 + m21 * m12 + m22 * m22 + m23 * m32;
        double n23 = m20 * m03 + m21 * m13 + m22 * m23 + m23 * m33;
        double n30 = m30 * m00 + m31 * m10 + m32 * m20 + m33 * m30;
        double n31 = m30 * m01 + m31 * m11 + m32 * m21 + m33 * m31;
        double n32 = m30 * m02 + m31 * m12 + m32 * m22 + m33 * m32;
        double n33 = m30 * m03 + m31 * m13 + m32 * m23 + m33 * m33;
        m00 = n00;
        m01 = n01;
        m02 = n02;
        m03 = n03;
        m10 = n10;
        m11 = n11;
        m12 = n12;
        m13 = n13;
        m20 = n20;
        m21 = n21;
        m22 = n22;
        m23 = n23;
        m30 = n30;
        m31 = n31;
        m32 = n32;
        m33 = n33;
        return this;
    }

    void mult3x3(Vector3 m) {
        double x = m00 * m.x + m01 * m.y + m02 * m.z;
        double y = m10 * m.x + m11 * m.y + m12 * m.z;
        double z = m20 * m.x + m21 * m.y + m22 * m.z;
        m.x = x;
        m.y = y;
        m.z = z;
    }

    public Matrix4 set(Matrix4 m) {
        return m;
    }

    public Matrix4 set(float[] m) {
        m00 = m[0];
        m01 = m[1];
        m02 = m[2];
        m03 = m[3];
        m10 = m[4];
        m11 = m[5];
        m12 = m[6];
        m13 = m[7];
        m20 = m[8];
        m21 = m[9];
        m22 = m[10];
        m23 = m[11];
        m30 = m[12];
        m31 = m[13];
        m32 = m[14];
        m33 = m[15];
        return this;
    }

    public Matrix4 set(double[] m) {
        m00 = m[0];
        m01 = m[1];
        m02 = m[2];
        m03 = m[3];
        m10 = m[4];
        m11 = m[5];
        m12 = m[6];
        m13 = m[7];
        m20 = m[8];
        m21 = m[9];
        m22 = m[10];
        m23 = m[11];
        m30 = m[12];
        m31 = m[13];
        m32 = m[14];
        m33 = m[15];
        return this;
    }

    public Matrix4 set(FloatBuffer m) {
        m00 = m.get();
        m01 = m.get();
        m02 = m.get();
        m03 = m.get();
        m10 = m.get();
        m11 = m.get();
        m12 = m.get();
        m13 = m.get();
        m20 = m.get();
        m21 = m.get();
        m22 = m.get();
        m23 = m.get();
        m30 = m.get();
        m31 = m.get();
        m32 = m.get();
        m33 = m.get();
        return this;
    }

    public Matrix4 set(DoubleBuffer m) {
        m00 = m.get();
        m01 = m.get();
        m02 = m.get();
        m03 = m.get();
        m10 = m.get();
        m11 = m.get();
        m12 = m.get();
        m13 = m.get();
        m20 = m.get();
        m21 = m.get();
        m22 = m.get();
        m23 = m.get();
        m30 = m.get();
        m31 = m.get();
        m32 = m.get();
        m33 = m.get();
        return this;
    }

    public Matrix4 set(PoseStack m) {
        return set(m.last().pose());
    }

    public Matrix4 set(Matrix4f m) {
        m00 = m.m00;
        m01 = m.m01;
        m02 = m.m02;
        m03 = m.m03;
        m10 = m.m10;
        m11 = m.m11;
        m12 = m.m12;
        m13 = m.m13;
        m20 = m.m20;
        m21 = m.m21;
        m22 = m.m22;
        m23 = m.m23;
        m30 = m.m30;
        m31 = m.m31;
        m32 = m.m32;
        m33 = m.m33;
        return this;
    }

    public Matrix4 copy() {
        return new Matrix4(this);
    }

    public Matrix4(Matrix4 m) {
        set(m);
    }

    public Matrix4 scale(Vector3 m) {
        return scale(m.x, m.y, m.z);
    }

    public Matrix4 scale(double m) {
        return scale(m, m, m);
    }

    public Matrix4(PoseStack m) {
        set(m);
    }

    public Matrix4 translate(Vector3 m) {
        return translate(m.x, m.y, m.z);
    }

    public Transformation inverse() {
        throw new RuntimeException("");
    }

    public void apply(Matrix4 m) {
        m.multiply(this);
    }

    public void apply(Vector3 m) {
        mult3x3(m);
        m.add(m03, m13, m23);
    }

    public void applyN(Vector3 m) {
        mult3x3(m);
        m.normalize();
    }

    public Matrix4 apply(Transformation t) {
        t.apply(this);
        return this;
    }

    public int hashCode() {
        long m = 1L;
        m = 31L * m + Double.doubleToLongBits(m00);
        m = 31L * m + Double.doubleToLongBits(m01);
        m = 31L * m + Double.doubleToLongBits(m02);
        m = 31L * m + Double.doubleToLongBits(m03);
        m = 31L * m + Double.doubleToLongBits(m10);
        m = 31L * m + Double.doubleToLongBits(m11);
        m = 31L * m + Double.doubleToLongBits(m12);
        m = 31L * m + Double.doubleToLongBits(m13);
        m = 31L * m + Double.doubleToLongBits(m20);
        m = 31L * m + Double.doubleToLongBits(m21);
        m = 31L * m + Double.doubleToLongBits(m22);
        m = 31L * m + Double.doubleToLongBits(m23);
        m = 31L * m + Double.doubleToLongBits(m30);
        m = 31L * m + Double.doubleToLongBits(m31);
        m = 31L * m + Double.doubleToLongBits(m32);
        m = 31L * m + Double.doubleToLongBits(m33);
        return (int)(m ^ m >> 32L);
    }

    public boolean equals(Object m) {
        if (m instanceof Matrix4) {
            Matrix4 n = (Matrix4)m;
            return (this.m00 == n.m00 && this.m01 == n.m01 && this.m02 == n.m02 && this.m03 == n.m03 && this.m10 == n.m10 && this.m11 == n.m11 &&
                    this.m12 == n.m12 && this.m13 == n.m13 && this.m20 == n.m20 && this.m21 == n.m21 && this.m22 == n.m22 && this.m23 == n.m23 &&
                    this.m30 == n.m30 && this.m31 == n.m31 && this.m32 == n.m32 && this.m33 == n.m33);
        }
        return false;
    }

    public String toString() {
        MathContext m = new MathContext(4, RoundingMode.HALF_UP);
        return "[" + new BigDecimal(m00, m) + "," + new BigDecimal(m01, m) + "," + new BigDecimal(m02, m) + "," + new BigDecimal(m03, m) + "]\n[" + new BigDecimal(m10, m) + "," + new BigDecimal(m11, m) + "," + new BigDecimal(m12, m) + "," + new BigDecimal(m13, m) + "]\n[" + new BigDecimal(m20, m) + "," + new BigDecimal(m21, m) + "," + new BigDecimal(m22, m) + "," + new BigDecimal(m23, m) + "]\n[" + new BigDecimal(m30, m) + "," + new BigDecimal(m31, m) + "," + new BigDecimal(m32, m) + "," + new BigDecimal(m33, m) + "]";
    }
}
