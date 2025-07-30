package com.yuo.endless.Client.Lib;

import org.joml.Matrix3f;
import org.joml.Matrix4f;

public interface ICCUniform {
    default void glUniform1i(int i0) {
        this.glUniformI(i0);
    }

    default void glUniform2i(int i0, int i1) {
        this.glUniformI(i0, i1);
    }

    default void glUniform3i(int i0, int i1, int i2) {
        this.glUniformI(i0, i1, i2);
    }

    default void glUniform4i(int i0, int i1, int i2, int i3) {
        this.glUniformI(i0, i1, i2, i3);
    }

    default void glUniform1ui(int i0) {
        this.glUniformI(i0);
    }

    default void glUniform2ui(int i0, int i1) {
        this.glUniformI(i0, i1);
    }

    default void glUniform3ui(int i0, int i1, int i2) {
        this.glUniformI(i0, i1, i2);
    }

    default void glUniform4ui(int i0, int i1, int i2, int i3) {
        this.glUniformI(i0, i1, i2, i3);
    }

    default void glUniform1f(float f0) {
        this.glUniformF(false, f0);
    }

    default void glUniform2f(float f0, float f1) {
        this.glUniformF(false, f0, f1);
    }

    default void glUniform3f(float f0, float f1, float f2) {
        this.glUniformF(false, f0, f1, f2);
    }

    default void glUniform4f(float f0, float f1, float f2, float f3) {
        this.glUniformF(false, f0, f1, f2, f3);
    }

    default void glUniform1d(float d0) {
        this.glUniformD(false, (double)d0);
    }

    default void glUniform2d(float d0, float d1) {
        this.glUniformD(false, (double)d0, (double)d1);
    }

    default void glUniform3d(float d0, float d1, float d2) {
        this.glUniformD(false, (double)d0, (double)d1, (double)d2);
    }

    default void glUniform4d(float d0, float d1, float d2, float d3) {
        this.glUniformD(false, (double)d0, (double)d1, (double)d2, (double)d3);
    }

    default void glUniform1b(boolean b0) {
        this.glUniformI(b0 ? 1 : 0);
    }

    default void glUniform2b(boolean b0, boolean b1) {
        this.glUniformI(b0 ? 1 : 0, b1 ? 1 : 0);
    }

    default void glUniform3b(boolean b0, boolean b1, boolean b2) {
        this.glUniformI(b0 ? 1 : 0, b1 ? 1 : 0, b2 ? 1 : 0);
    }

    default void glUniform4b(boolean b0, boolean b1, boolean b2, boolean b3) {
        this.glUniformI(b0 ? 1 : 0, b1 ? 1 : 0, b2 ? 1 : 0, b3 ? 1 : 0);
    }

    default void glUniformMatrix2f(float[] matrix) {
        this.glUniformF(false, matrix);
    }

    default void glUniformMatrix2f(boolean transpose, float[] matrix) {
        this.glUniformF(transpose, matrix);
    }

    default void glUniformMatrix2x3f(float[] matrix) {
        this.glUniformF(false, matrix);
    }

    default void glUniformMatrix2x3f(boolean transpose, float[] matrix) {
        this.glUniformF(transpose, matrix);
    }

    default void glUniformMatrix2x4f(float[] matrix) {
        this.glUniformF(false, matrix);
    }

    default void glUniformMatrix2x4f(boolean transpose, float[] matrix) {
        this.glUniformF(transpose, matrix);
    }

    default void glUniformMatrix3f(float[] matrix) {
        this.glUniformF(false, matrix);
    }

    default void glUniformMatrix3f(boolean transpose, float[] matrix) {
        this.glUniformF(transpose, matrix);
    }

    default void glUniformMatrix3f(Matrix3f matrix) {
        this.glUniformF(false, toArrayF(matrix));
    }

    default void glUniformMatrix3f(boolean transpose, Matrix3f matrix) {
        this.glUniformF(transpose, toArrayF(matrix));
    }

    default void glUniformMatrix3x2f(float[] matrix) {
        this.glUniformF(false, matrix);
    }

    default void glUniformMatrix3x2f(boolean transpose, float[] matrix) {
        this.glUniformF(transpose, matrix);
    }

    default void glUniformMatrix3x4f(float[] matrix) {
        this.glUniformF(false, matrix);
    }

    default void glUniformMatrix3x4f(boolean transpose, float[] matrix) {
        this.glUniformF(transpose, matrix);
    }

    default void glUniformMatrix4f(float[] matrix) {
        this.glUniformF(false, matrix);
    }

    default void glUniformMatrix4f(boolean transpose, float[] matrix) {
        this.glUniformF(transpose, matrix);
    }

    default void glUniformMatrix4f(Matrix4 matrix) {
        this.glUniformF(false, matrix.toArrayF());
    }

    default void glUniformMatrix4f(boolean transpose, Matrix4 matrix) {
        this.glUniformF(transpose, matrix.toArrayF());
    }

    default void glUniformMatrix4f(Matrix4f matrix) {
        this.glUniformF(false, toArrayF(matrix));
    }

    default void glUniformMatrix4f(boolean transpose, Matrix4f matrix) {
        this.glUniformF(transpose, toArrayF(matrix));
    }

    default void glUniformMatrix4x2f(float[] matrix) {
        this.glUniformF(false, matrix);
    }

    default void glUniformMatrix4x2f(boolean transpose, float[] matrix) {
        this.glUniformF(transpose, matrix);
    }

    default void glUniformMatrix4x3f(float[] matrix) {
        this.glUniformF(false, matrix);
    }

    default void glUniformMatrix4x3f(boolean transpose, float[] matrix) {
        this.glUniformF(transpose, matrix);
    }

    default void glUniformMatrix2d(double[] matrix) {
        this.glUniformD(false, matrix);
    }

    default void glUniformMatrix2d(boolean transpose, double[] matrix) {
        this.glUniformD(transpose, matrix);
    }

    default void glUniformMatrix2x3d(double[] matrix) {
        this.glUniformD(false, matrix);
    }

    default void glUniformMatrix2x3d(boolean transpose, double[] matrix) {
        this.glUniformD(transpose, matrix);
    }

    default void glUniformMatrix2x4d(double[] matrix) {
        this.glUniformD(false, matrix);
    }

    default void glUniformMatrix2x4d(boolean transpose, double[] matrix) {
        this.glUniformD(transpose, matrix);
    }

    default void glUniformMatrix3d(double[] matrix) {
        this.glUniformD(false, matrix);
    }

    default void glUniformMatrix3d(boolean transpose, double[] matrix) {
        this.glUniformD(transpose, matrix);
    }

    default void glUniformMatrix3d(Matrix3f matrix) {
        this.glUniformD(false, toArrayD(matrix));
    }

    default void glUniformMatrix3d(boolean transpose, Matrix3f matrix) {
        this.glUniformD(transpose, toArrayD(matrix));
    }

    default void glUniformMatrix3x2d(double[] matrix) {
        this.glUniformD(false, matrix);
    }

    default void glUniformMatrix3x2d(boolean transpose, double[] matrix) {
        this.glUniformD(transpose, matrix);
    }

    default void glUniformMatrix3x4d(double[] matrix) {
        this.glUniformD(false, matrix);
    }

    default void glUniformMatrix3x4d(boolean transpose, double[] matrix) {
        this.glUniformD(transpose, matrix);
    }

    default void glUniformMatrix4d(double[] matrix) {
        this.glUniformD(false, matrix);
    }

    default void glUniformMatrix4d(boolean transpose, double[] matrix) {
        this.glUniformD(transpose, matrix);
    }

    default void glUniformMatrix4d(Matrix4 matrix) {
        this.glUniformD(false, matrix.toArrayD());
    }

    default void glUniformMatrix4d(boolean transpose, Matrix4 matrix) {
        this.glUniformD(transpose, matrix.toArrayD());
    }

    default void glUniformMatrix4d(Matrix4f matrix) {
        this.glUniformD(false, toArrayD(matrix));
    }

    default void glUniformMatrix4d(boolean transpose, Matrix4f matrix) {
        this.glUniformD(transpose, toArrayD(matrix));
    }

    default void glUniformMatrix4x2d(double[] matrix) {
        this.glUniformD(false, matrix);
    }

    default void glUniformMatrix4x2d(boolean transpose, double[] matrix) {
        this.glUniformD(transpose, matrix);
    }

    default void glUniformMatrix4x3d(double[] matrix) {
        this.glUniformD(false, matrix);
    }

    default void glUniformMatrix4x3d(boolean transpose, double[] matrix) {
        this.glUniformD(transpose, matrix);
    }

    void glUniformI(int... var1);

    void glUniformF(boolean var1, float... var2);

    void glUniformD(boolean var1, double... var2);

    private static float[] toArrayF(Matrix3f matrix) {
        return new float[]{matrix.m00, matrix.m01, matrix.m02, matrix.m10, matrix.m11, matrix.m12, matrix.m20, matrix.m21, matrix.m22};
    }

    private static float[] toArrayF(Matrix4f matrix) {
        return new float[]{matrix.m00(), matrix.m01(), matrix.m02(), matrix.m03(), matrix.m10(), matrix.m11(), matrix.m12(), matrix.m13(), matrix.m20(), matrix.m21(), matrix.m22(), matrix.m23(), matrix.m30(), matrix.m31(), matrix.m32(), matrix.m33()};
    }

    private static double[] toArrayD(Matrix3f matrix) {
        return new double[]{(double)matrix.m00, (double)matrix.m01, (double)matrix.m02, (double)matrix.m10, (double)matrix.m11, (double)matrix.m12, (double)matrix.m20, (double)matrix.m21, (double)matrix.m22};
    }

    private static double[] toArrayD(Matrix4f matrix) {
        return new double[]{(double)matrix.m00(), (double)matrix.m01(), (double)matrix.m02(), (double)matrix.m03(), (double)matrix.m10(), (double)matrix.m11(), (double)matrix.m12(), (double)matrix.m13(), (double)matrix.m20(), (double)matrix.m21(), (double)matrix.m22(), (double)matrix.m23(), (double)matrix.m30(), (double)matrix.m31(), (double)matrix.m32(), (double)matrix.m33()};
    }
}
