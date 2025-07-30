package com.yuo.endless.Client.Lib;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Vec3i;
import org.joml.Matrix4f;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.*;

public class Matrix4 extends Transformation {

    //m<row><column>
    public double m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33;

    public Matrix4() {
        setIdentity();
    }

    public Matrix4(double d00, double d01, double d02, double d03, double d10, double d11, double d12, double d13, double d20, double d21, double d22, double d23, double d30, double d31, double d32, double d33) {
        m00 = d00;
        m01 = d01;
        m02 = d02;
        m03 = d03;
        m10 = d10;
        m11 = d11;
        m12 = d12;
        m13 = d13;
        m20 = d20;
        m21 = d21;
        m22 = d22;
        m23 = d23;
        m30 = d30;
        m31 = d31;
        m32 = d32;
        m33 = d33;
    }

    public Matrix4(Matrix4 mat) {
        set(mat);
    }

    public Matrix4(PoseStack stack) {
        set(stack);
    }

    public void setIdentity() {
        m00 = m11 = m22 = m33 = 1;
        m01 = m02 = m03 = m10 = m12 = m13 = m20 = m21 = m23 = m30 = m31 = m32 = 0;

    }

    //region Translate, Scale, Transpose.
    public Matrix4 translate(Vec3i pos) {
        return translate(pos.getX(), pos.getY(), pos.getZ());
    }

    public Matrix4 translate(Vector3 vec) {
        return translate(vec.x, vec.y, vec.z);
    }

    public Matrix4 translate(double x, double y, double z) {
        m03 += m00 * x + m01 * y + m02 * z;
        m13 += m10 * x + m11 * y + m12 * z;
        m23 += m20 * x + m21 * y + m22 * z;
        m33 += m30 * x + m31 * y + m32 * z;

        return this;
    }

    public Matrix4 scale(Vector3 vec) {
        return scale(vec.x, vec.y, vec.z);
    }

    public Matrix4 scale(double scale) {
        return scale(scale, scale, scale);
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

    //endregion

    //region Rotate
    public void rotate(double angle, Vector3 axis) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double mc = 1.0f - c;
        double xy = axis.x * axis.y;
        double yz = axis.y * axis.z;
        double xz = axis.x * axis.z;
        double xs = axis.x * s;
        double ys = axis.y * s;
        double zs = axis.z * s;

        double f00 = axis.x * axis.x * mc + c;
        double f10 = xy * mc + zs;
        double f20 = xz * mc - ys;

        double f01 = xy * mc - zs;
        double f11 = axis.y * axis.y * mc + c;
        double f21 = yz * mc + xs;

        double f02 = xz * mc + ys;
        double f12 = yz * mc - xs;
        double f22 = axis.z * axis.z * mc + c;

        double t00 = m00 * f00 + m01 * f10 + m02 * f20;
        double t10 = m10 * f00 + m11 * f10 + m12 * f20;
        double t20 = m20 * f00 + m21 * f10 + m22 * f20;
        double t30 = m30 * f00 + m31 * f10 + m32 * f20;
        double t01 = m00 * f01 + m01 * f11 + m02 * f21;
        double t11 = m10 * f01 + m11 * f11 + m12 * f21;
        double t21 = m20 * f01 + m21 * f11 + m22 * f21;
        double t31 = m30 * f01 + m31 * f11 + m32 * f21;
        m02 = m00 * f02 + m01 * f12 + m02 * f22;
        m12 = m10 * f02 + m11 * f12 + m12 * f22;
        m22 = m20 * f02 + m21 * f12 + m22 * f22;
        m32 = m30 * f02 + m31 * f12 + m32 * f22;
        m00 = t00;
        m10 = t10;
        m20 = t20;
        m30 = t30;
        m01 = t01;
        m11 = t11;
        m21 = t21;
        m31 = t31;

    }

    //endregion

    public Matrix4 multiply(Matrix4 mat) {
        double n00 = m00 * mat.m00 + m01 * mat.m10 + m02 * mat.m20 + m03 * mat.m30;
        double n01 = m00 * mat.m01 + m01 * mat.m11 + m02 * mat.m21 + m03 * mat.m31;
        double n02 = m00 * mat.m02 + m01 * mat.m12 + m02 * mat.m22 + m03 * mat.m32;
        double n03 = m00 * mat.m03 + m01 * mat.m13 + m02 * mat.m23 + m03 * mat.m33;
        double n10 = m10 * mat.m00 + m11 * mat.m10 + m12 * mat.m20 + m13 * mat.m30;
        double n11 = m10 * mat.m01 + m11 * mat.m11 + m12 * mat.m21 + m13 * mat.m31;
        double n12 = m10 * mat.m02 + m11 * mat.m12 + m12 * mat.m22 + m13 * mat.m32;
        double n13 = m10 * mat.m03 + m11 * mat.m13 + m12 * mat.m23 + m13 * mat.m33;
        double n20 = m20 * mat.m00 + m21 * mat.m10 + m22 * mat.m20 + m23 * mat.m30;
        double n21 = m20 * mat.m01 + m21 * mat.m11 + m22 * mat.m21 + m23 * mat.m31;
        double n22 = m20 * mat.m02 + m21 * mat.m12 + m22 * mat.m22 + m23 * mat.m32;
        double n23 = m20 * mat.m03 + m21 * mat.m13 + m22 * mat.m23 + m23 * mat.m33;
        double n30 = m30 * mat.m00 + m31 * mat.m10 + m32 * mat.m20 + m33 * mat.m30;
        double n31 = m30 * mat.m01 + m31 * mat.m11 + m32 * mat.m21 + m33 * mat.m31;
        double n32 = m30 * mat.m02 + m31 * mat.m12 + m32 * mat.m22 + m33 * mat.m32;
        double n33 = m30 * mat.m03 + m31 * mat.m13 + m32 * mat.m23 + m33 * mat.m33;

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

    private void mult3x3(Vector3 vec) {
        double x = m00 * vec.x + m01 * vec.y + m02 * vec.z;
        double y = m10 * vec.x + m11 * vec.y + m12 * vec.z;
        double z = m20 * vec.x + m21 * vec.y + m22 * vec.z;

        vec.x = x;
        vec.y = y;
        vec.z = z;
    }

    //endregion

    //region Set
    public Matrix4 set(Matrix4 mat) {
        m00 = mat.m00;
        m01 = mat.m01;
        m02 = mat.m02;
        m03 = mat.m03;
        m10 = mat.m10;
        m11 = mat.m11;
        m12 = mat.m12;
        m13 = mat.m13;
        m20 = mat.m20;
        m21 = mat.m21;
        m22 = mat.m22;
        m23 = mat.m23;
        m30 = mat.m30;
        m31 = mat.m31;
        m32 = mat.m32;
        m33 = mat.m33;

        return this;
    }

    public Matrix4 set(float[] matrix) {
        m00 = matrix[0];
        m10 = matrix[1];
        m20 = matrix[2];
        m30 = matrix[3];
        m01 = matrix[4];
        m11 = matrix[5];
        m21 = matrix[6];
        m31 = matrix[7];
        m02 = matrix[8];
        m12 = matrix[9];
        m22 = matrix[10];
        m32 = matrix[11];
        m03 = matrix[12];
        m13 = matrix[13];
        m23 = matrix[14];
        m33 = matrix[15];

        return this;
    }

    public Matrix4 set(double[] matrix) {
        m00 = matrix[0];
        m10 = matrix[1];
        m20 = matrix[2];
        m30 = matrix[3];
        m01 = matrix[4];
        m11 = matrix[5];
        m21 = matrix[6];
        m31 = matrix[7];
        m02 = matrix[8];
        m12 = matrix[9];
        m22 = matrix[10];
        m32 = matrix[11];
        m03 = matrix[12];
        m13 = matrix[13];
        m23 = matrix[14];
        m33 = matrix[15];

        return this;
    }

    public Matrix4 set(FloatBuffer buffer) {
        m00 = buffer.get();
        m10 = buffer.get();
        m20 = buffer.get();
        m30 = buffer.get();
        m01 = buffer.get();
        m11 = buffer.get();
        m21 = buffer.get();
        m31 = buffer.get();
        m02 = buffer.get();
        m12 = buffer.get();
        m22 = buffer.get();
        m32 = buffer.get();
        m03 = buffer.get();
        m13 = buffer.get();
        m23 = buffer.get();
        m33 = buffer.get();

        return this;
    }

    public Matrix4 set(DoubleBuffer buffer) {
        m00 = buffer.get();
        m10 = buffer.get();
        m20 = buffer.get();
        m30 = buffer.get();
        m01 = buffer.get();
        m11 = buffer.get();
        m21 = buffer.get();
        m31 = buffer.get();
        m02 = buffer.get();
        m12 = buffer.get();
        m22 = buffer.get();
        m32 = buffer.get();
        m03 = buffer.get();
        m13 = buffer.get();
        m23 = buffer.get();
        m33 = buffer.get();

        return this;
    }

    public Matrix4 set(PoseStack stack) {
        return set(stack.last().pose());
    }

    public Matrix4 set(Matrix4f mat) {
        m00 = mat.m00();
        m01 = mat.m10();
        m02 = mat.m20();
        m03 = mat.m30();
        m10 = mat.m01();
        m11 = mat.m11();
        m12 = mat.m21();
        m13 = mat.m31();
        m20 = mat.m02();
        m21 = mat.m12();
        m22 = mat.m22();
        m23 = mat.m32();
        m30 = mat.m03();
        m31 = mat.m13();
        m32 = mat.m23();
        m33 = mat.m33();

        return this;
    }
    //endregion

    @Override
    public Matrix4 copy() {
        return new Matrix4(this);
    }

    @Override
    public void apply(Matrix4 mat) {
        mat.multiply(this);
    }

    @Override
    public void apply(Vector3 vec) {
        mult3x3(vec);
        vec.add(m03, m13, m23);
    }

    @Override
    public void applyN(Vector3 vec) {
        mult3x3(vec);
        vec.normalize();
    }

    public Matrix4 apply(Transformation t) {
        t.apply(this);
        return this;
    }

    @Override
    public Transformation inverse() {//TODO this should be done, even if it is a waste..
        throw new IrreversibleTransformationException(this);//Don't waste your cpu with matrix inverses
    }

    @Override
    public int hashCode() {
        long bits = 1L;
        bits = 31L * bits + Double.doubleToLongBits(m00);
        bits = 31L * bits + Double.doubleToLongBits(m01);
        bits = 31L * bits + Double.doubleToLongBits(m02);
        bits = 31L * bits + Double.doubleToLongBits(m03);
        bits = 31L * bits + Double.doubleToLongBits(m10);
        bits = 31L * bits + Double.doubleToLongBits(m11);
        bits = 31L * bits + Double.doubleToLongBits(m12);
        bits = 31L * bits + Double.doubleToLongBits(m13);
        bits = 31L * bits + Double.doubleToLongBits(m20);
        bits = 31L * bits + Double.doubleToLongBits(m21);
        bits = 31L * bits + Double.doubleToLongBits(m22);
        bits = 31L * bits + Double.doubleToLongBits(m23);
        bits = 31L * bits + Double.doubleToLongBits(m30);
        bits = 31L * bits + Double.doubleToLongBits(m31);
        bits = 31L * bits + Double.doubleToLongBits(m32);
        bits = 31L * bits + Double.doubleToLongBits(m33);
        return (int) (bits ^ (bits >> 32));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Matrix4 other) {
            //@formatter:off
			return     m00 == other.m00 && m01 == other.m01 && m02 == other.m02 && m03 == other.m03
					&& m10 == other.m10 && m11 == other.m11 && m12 == other.m12 && m13 == other.m13
					&& m20 == other.m20 && m21 == other.m21 && m22 == other.m22 && m23 == other.m23
					&& m30 == other.m30 && m31 == other.m31 && m32 == other.m32 && m33 == other.m33;
			//@formatter:on
        }
        return false;
    }

    @Override
    public String toString() {
        //@formatter:off
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "[" + new BigDecimal(m00, cont) + "," + new BigDecimal(m01, cont) + "," + new BigDecimal(m02, cont) + "," + new BigDecimal(m03, cont) + "]\n" +
               "[" + new BigDecimal(m10, cont) + "," + new BigDecimal(m11, cont) + "," + new BigDecimal(m12, cont) + "," + new BigDecimal(m13, cont) + "]\n" +
               "[" + new BigDecimal(m20, cont) + "," + new BigDecimal(m21, cont) + "," + new BigDecimal(m22, cont) + "," + new BigDecimal(m23, cont) + "]\n" +
               "[" + new BigDecimal(m30, cont) + "," + new BigDecimal(m31, cont) + "," + new BigDecimal(m32, cont) + "," + new BigDecimal(m33, cont) + "]";
        //@formatter:on
    }
}
