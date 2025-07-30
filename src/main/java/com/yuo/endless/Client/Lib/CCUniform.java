package com.yuo.endless.Client.Lib;

import com.mojang.blaze3d.shaders.Shader;
import com.mojang.blaze3d.shaders.Uniform;
import com.yuo.endless.Client.Lib.UniformType.Carrier;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

public abstract class CCUniform extends Uniform implements ICCUniform {
    protected final UniformType type;

    protected CCUniform(String name, UniformType type, int count, @Nullable Shader parent) {
        super(name, type.getVanillaType(), count, parent);
        this.type = type;
        if (this.intValues != null) {
            MemoryUtil.memFree(this.intValues);
            this.intValues = null;
        }

        if (this.floatValues != null) {
            MemoryUtil.memFree(this.floatValues);
            this.floatValues = null;
        }

    }

    public void setMatrix2x2Array(float[] values, int count) {
        if (this.type != UniformType.MAT2)
            throw new IllegalStateException("Uniform '%s' is not of type MAT2.".formatted(getName()));
        if (values.length != count * 4)
            throw new IllegalArgumentException("Invalid size for mat2 array. Expected %d floats, got %d.".formatted(Integer.valueOf(count * 4), Integer.valueOf(values.length)));
        glUniformF(false, values);
    }

    static CCUniform makeUniform(String name, UniformType type, int count, @Nullable Shader parent) {
        if (count % type.getSize() != 0) {
            throw new IllegalArgumentException("Expected count to be a multiple of the uniform type size: " + type.getSize());
        } else {
            Object var10000;
            switch (type.getCarrier()) {
                case INT:
                case U_INT:
                    var10000 = new CCUniform.IntUniform(name, type, count, parent);
                    break;
                case FLOAT:
                case MATRIX:
                    var10000 = new CCUniform.FloatUniform(name, type, count, parent);
                    break;
                case DOUBLE:
                case D_MATRIX:
                    var10000 = new CCUniform.DoubleUniform(name, type, count, parent);
                    break;
                default:
                    throw new IncompatibleClassChangeError();
            }

            return (CCUniform)var10000;
        }
    }

    public IntBuffer getIntBuffer() {
        throw new NotImplementedException("TODO");
    }

    public FloatBuffer getFloatBuffer() {
        throw new NotImplementedException("TODO");
    }

    private static class IntUniform extends CCUniform.UniformEntry<int[]> {
        public IntUniform(String name, UniformType type, int count, @Nullable Shader parent) {
            super(name, type, count, parent);

            assert type.getCarrier() == Carrier.INT || type.getCarrier() == Carrier.U_INT;

        }

        public void flush() {
            assert this.cache != null;

            switch (this.type) {
                case INT:
                    GL20.glUniform1iv(this.getLocation(), (int[])this.cache);
                    break;
                case U_INT:
                    GL30.glUniform1uiv(this.getLocation(), (int[])this.cache);
                    break;
                case I_VEC2:
                case B_VEC2:
                    GL20.glUniform2iv(this.getLocation(), (int[])this.cache);
                    break;
                case U_VEC2:
                    GL30.glUniform2uiv(this.getLocation(), (int[])this.cache);
                    break;
                case I_VEC3:
                case B_VEC3:
                    GL20.glUniform3iv(this.getLocation(), (int[])this.cache);
                    break;
                case U_VEC3:
                    GL30.glUniform3uiv(this.getLocation(), (int[])this.cache);
                    break;
                case I_VEC4:
                case B_VEC4:
                    GL20.glUniform4iv(this.getLocation(), (int[])this.cache);
                    break;
                case U_VEC4:
                    GL30.glUniform4uiv(this.getLocation(), (int[])this.cache);
                    break;
                default:
                    throw new IllegalStateException("Unhandled uniform type for IntUniform: " + this.type);
            }

        }

        public int[] make(int len) {
            return new int[len];
        }

        public int len(int[] cache) {
            return cache.length;
        }

        public boolean equals(int @Nullable [] a, int[] b) {
            return Arrays.equals(a, b);
        }
    }

    private static class FloatUniform extends CCUniform.UniformEntry<float[]> {
        public FloatUniform(String name, UniformType type, int count, @Nullable Shader parent) {
            super(name, type, count, parent);

            assert type.getCarrier() == Carrier.FLOAT || type.getCarrier() == Carrier.MATRIX;

        }

        public void flush() {
            assert this.cache != null;

            switch (this.type) {
                case FLOAT -> GL20.glUniform1fv(this.getLocation(), (float[])this.cache);
                case VEC2 -> GL20.glUniform2fv(this.getLocation(), (float[])this.cache);
                case VEC3 -> GL20.glUniform3fv(this.getLocation(), (float[])this.cache);
                case VEC4 -> GL20.glUniform4fv(this.getLocation(), (float[])this.cache);
                case MAT2 -> GL20.glUniformMatrix2fv(this.getLocation(), this.transpose, (float[])this.cache);
                case MAT2x3 -> GL21.glUniformMatrix2x3fv(this.getLocation(), this.transpose, (float[])this.cache);
                case MAT2x4 -> GL21.glUniformMatrix2x4fv(this.getLocation(), this.transpose, (float[])this.cache);
                case MAT3 -> GL20.glUniformMatrix3fv(this.getLocation(), this.transpose, (float[])this.cache);
                case MAT3x2 -> GL21.glUniformMatrix3x2fv(this.getLocation(), this.transpose, (float[])this.cache);
                case MAT3x4 -> GL21.glUniformMatrix3x4fv(this.getLocation(), this.transpose, (float[])this.cache);
                case MAT4 -> GL20.glUniformMatrix4fv(this.getLocation(), this.transpose, (float[])this.cache);
                case MAT4x2 -> GL21.glUniformMatrix4x2fv(this.getLocation(), this.transpose, (float[])this.cache);
                case MAT4x3 -> GL21.glUniformMatrix4x3fv(this.getLocation(), this.transpose, (float[])this.cache);
                default -> throw new IllegalStateException("Unhandled uniform type for FloatUniform: " + this.type);
            }

        }

        public float[] make(int len) {
            return new float[len];
        }

        public int len(float[] cache) {
            return cache.length;
        }

        public boolean equals(float @Nullable [] a, float[] b) {
            return Arrays.equals(a, b);
        }
    }

    private static class DoubleUniform extends CCUniform.UniformEntry<double[]> {
        public DoubleUniform(String name, UniformType type, int count, @Nullable Shader parent) {
            super(name, type, count, parent);

            assert type.getCarrier() == Carrier.DOUBLE || type.getCarrier() == Carrier.D_MATRIX;

        }

        public void flush() {
            assert this.cache != null;

            switch (this.type) {
                case DOUBLE -> GL40.glUniform1dv(this.getLocation(), (double[])this.cache);
                case D_VEC2 -> GL40.glUniform2dv(this.getLocation(), (double[])this.cache);
                case D_VEC3 -> GL40.glUniform3dv(this.getLocation(), (double[])this.cache);
                case D_VEC4 -> GL40.glUniform4dv(this.getLocation(), (double[])this.cache);
                case D_MAT2 -> GL40.glUniformMatrix2dv(this.getLocation(), this.transpose, (double[])this.cache);
                case D_MAT2x3 -> GL40.glUniformMatrix2x3dv(this.getLocation(), this.transpose, (double[])this.cache);
                case D_MAT2x4 -> GL40.glUniformMatrix2x4dv(this.getLocation(), this.transpose, (double[])this.cache);
                case D_MAT3 -> GL40.glUniformMatrix3dv(this.getLocation(), this.transpose, (double[])this.cache);
                case D_MAT3x2 -> GL40.glUniformMatrix3x2dv(this.getLocation(), this.transpose, (double[])this.cache);
                case D_MAT3x4 -> GL40.glUniformMatrix3x4dv(this.getLocation(), this.transpose, (double[])this.cache);
                case D_MAT4 -> GL40.glUniformMatrix4dv(this.getLocation(), this.transpose, (double[])this.cache);
                case D_MAT4x2 -> GL40.glUniformMatrix4x2dv(this.getLocation(), this.transpose, (double[])this.cache);
                case D_MAT4x3 -> GL40.glUniformMatrix4x3dv(this.getLocation(), this.transpose, (double[])this.cache);
                default -> throw new IllegalStateException("Unhandled uniform type for DoubleUniform: " + this.type);
            }

        }

        public double[] make(int len) {
            return new double[len];
        }

        public int len(double[] cache) {
            return cache.length;
        }

        public boolean equals(double @Nullable [] a, double[] b) {
            return Arrays.equals(a, b);
        }
    }

    private abstract static class UniformEntry<T> extends CCUniform {
        protected @Nullable T cache;
        protected boolean transpose;

        public UniformEntry(String name, UniformType type, int count, @Nullable Shader parent) {
            super(name, type, count, parent);
        }

        public void set(float f0) {
            this.glUniformF(false, f0);
        }

        public void set(float f0, float f1) {
            this.glUniformF(false, f0, f1);
        }

        public void set(int i, float f) {
            throw new UnsupportedOperationException("Unable to set specific index.");
        }

        public void set(float f0, float f1, float f2) {
            this.glUniformF(false, f0, f1, f2);
        }

        public void set(Vector3f vec) {
            this.glUniformF(false, vec.x(), vec.y(), vec.z());
        }

        public void set(float f0, float f1, float f2, float f3) {
            this.glUniformF(false, f0, f1, f2, f3);
        }

        public void set(Vector4f vec) {
            this.glUniformF(false, vec.x(), vec.y(), vec.z(), vec.w());
        }

        public void set(int i0) {
            this.glUniformI(i0);
        }

        public void set(int i0, int i1) {
            this.glUniformI(i0, i1);
        }

        public void set(int i0, int i1, int i2) {
            this.glUniformI(i0, i1, i2);
        }

        public void set(int i0, int i1, int i2, int i3) {
            this.glUniformI(i0, i1, i2, i3);
        }

        public void set(float[] p_85632_) {
            this.glUniformF(false, p_85632_);
        }

        public void setMat2x2(float m00, float m01, float m10, float m11) {
            this.glUniformF(true, m00, m01, m10, m11);
        }

        public void setMat2x3(float m00, float m01, float m02, float m10, float m11, float m12) {
            this.glUniformF(true, m00, m01, m02, m10, m11, m12);
        }

        public void setMat2x4(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13) {
            this.glUniformF(true, m00, m01, m02, m03, m10, m11, m12, m13);
        }

        public void setMat3x2(float m00, float m01, float m10, float m11, float m20, float m21) {
            this.glUniformF(true, m00, m01, m10, m11, m20, m21);
        }

        public void setMat3x3(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
            this.glUniformF(true, m00, m01, m02, m10, m11, m12, m20, m21, m22);
        }

        public void setMat3x4(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23) {
            this.glUniformF(true, m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23);
        }

        public void setMat4x2(float m00, float m01, float m10, float m11, float m20, float m21, float m30, float m31) {
            this.glUniformF(true, m00, m01, m10, m11, m20, m21, m30, m31);
        }

        public void setMat4x3(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m23, float m30, float m31, float m32) {
            this.glUniformF(true, m00, m01, m02, m10, m11, m12, m20, m21, m23, m30, m31, m32);
        }

        public void setMat4x4(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
            this.glUniformF(true, m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
        }

        public void set(Matrix4f mat) {
            this.glUniformMatrix4f(mat);
        }

        public void set(Matrix3f mat) {
            this.glUniformMatrix3f(mat);
        }

        public void setSafe(float f0, float f1, float f2, float f3) {
            assert this.type.getCarrier() == Carrier.FLOAT;

            switch (this.type.getSize()) {
                case 1 -> this.glUniform1f(f0);
                case 2 -> this.glUniform2f(f0, f1);
                case 3 -> this.glUniform3f(f0, f1, f2);
                case 4 -> this.glUniform4f(f0, f1, f2, f3);
            }

            throw new IllegalStateException("Unexpected type size: " + this.type);
        }

        public void setSafe(int i0, int i1, int i2, int i3) {
            assert this.type.getCarrier() == Carrier.INT || this.type.getCarrier() == Carrier.U_INT;

            switch (this.type.getSize()) {
                case 1 -> this.glUniform1i(i0);
                case 2 -> this.glUniform2i(i0, i1);
                case 3 -> this.glUniform3i(i0, i1, i2);
                case 4 -> this.glUniform4i(i0, i1, i2, i3);
            }

            throw new IllegalStateException("Unexpected type size: " + this.type);
        }

        public void glUniformI(int... values) {
            if (this.type.getCarrier() != Carrier.INT && this.type.getCarrier() != Carrier.U_INT) {
                throw new IllegalArgumentException("Uniform '%s' isn't registered with the carrier of INT or U_INT, Got type '%s' with carrier '%s'.".formatted(this.getName(), this.type, this.type.getCarrier()));
            } else {
                this.set((T) values, false);
            }
        }

        public void glUniformF(boolean transpose, float... values) {
            if (this.type.getCarrier() != Carrier.FLOAT && this.type.getCarrier() != Carrier.MATRIX) {
                throw new IllegalArgumentException("Uniform '%s' isn't registered with the carrier of FLOAT or MATRIX, Got type '%s' with carrier '%s'.".formatted(this.getName(), this.type, this.type.getCarrier()));
            } else {
                this.set((T) values, transpose);
            }
        }

        public void glUniformD(boolean transpose, double... values) {
            if (this.type.getCarrier() != Carrier.DOUBLE && this.type.getCarrier() != Carrier.D_MATRIX) {
                throw new IllegalArgumentException("Uniform '%s' isn't registered with the carrier of DOUBLE or D_MATRIX, Got type '%s' with carrier '%s'.".formatted(this.getName(), this.type, this.type.getCarrier()));
            } else {
                this.set((T) values, transpose);
            }
        }

        public void set(T values, boolean transpose) {
            assert !transpose || this.type.getCarrier() == Carrier.MATRIX || this.type.getCarrier() == Carrier.D_MATRIX;

            if (this.len(values) != this.getCount()) {
                throw new IllegalArgumentException("Invalid size for uniform '%s', Expected: '%s', Got: '%s'.".formatted(this.getName(), this.getCount(), this.len(values)));
            } else {
                if (!this.equals(this.cache, values) || this.transpose != transpose) {
                    this.cache = values;
                    this.transpose = transpose;
                    this.dirty = true;
                }

            }
        }

        public void upload() {
            if (this.dirty) {
                this.flush();
                this.dirty = false;
            }
        }

        public abstract void flush();

        public abstract T make(int var1);

        public abstract int len(T var1);

        public abstract boolean equals(@Nullable T var1, T var2);
    }
}
