package com.yuo.endless.Client.Lib;

import com.mojang.blaze3d.shaders.Shader;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL40;
import org.lwjgl.system.MemoryUtil;

import java.util.Arrays;

public abstract class CCUniform extends Uniform implements ICCUniform {
    UniformType type;

    protected CCUniform(String name, UniformType type, int count, Shader parent) {
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

    static CCUniform makeUniform(String name, UniformType type, int count, Shader parent) {
        if (count % type.getSize() != 0) {
            throw new IllegalArgumentException("Expected count to be a multiple of the uniform type size: " + type.getSize());
        } else {
            Object var10000 = switch (type.getCarrier()) {
                case INT, U_INT -> new IntUniform(name, type, count, parent);
                case FLOAT, MATRIX -> new FloatUniform(name, type, count, parent);
                case DOUBLE, D_MATRIX -> new DoubleUniform(name, type, count, parent);
                default -> throw new IncompatibleClassChangeError();
            };

            return (CCUniform) var10000;
        }
    }

    static class IntUniform extends UniformEntry<int[]> {
        public IntUniform(String name, UniformType type, int count, Shader parent) {
            super(name, type, count, parent);
            assert type.getCarrier() == UniformType.Carrier.INT || type.getCarrier() == UniformType.Carrier.U_INT;
        }

        public void flush() {
            assert this.cache != null;
            switch (this.type) {
                case INT:
                    GL20.glUniform1iv(getLocation(), this.cache);
                    break;
                case U_INT:
                    GL30.glUniform1uiv(getLocation(), this.cache);
                    break;
                case I_VEC2:
                case B_VEC2:
                    GL20.glUniform2iv(getLocation(), this.cache);
                    break;
                case U_VEC2:
                    GL30.glUniform2uiv(getLocation(), this.cache);
                    break;
                case I_VEC3:
                case B_VEC3:
                    GL20.glUniform3iv(getLocation(), this.cache);
                    break;
                case U_VEC3:
                    GL30.glUniform3uiv(getLocation(), this.cache);
                    break;
                case I_VEC4:
                case B_VEC4:
                    GL20.glUniform4iv(getLocation(), this.cache);
                    break;
                case U_VEC4:
                    GL30.glUniform4uiv(getLocation(), this.cache);
                    break;
            }
        }

        public int len(int[] cache) {
            return cache.length;
        }

        public boolean equals(int[] a, int[] b) {
            return Arrays.equals(a, b);
        }
    }

    static class FloatUniform extends UniformEntry<float[]> {
        public FloatUniform(String name, UniformType type, int count, Shader parent) {
            super(name, type, count, parent);
            assert type.getCarrier() == UniformType.Carrier.FLOAT || type.getCarrier() == UniformType.Carrier.MATRIX;
        }

        public void flush() {
            assert this.cache != null;
            switch (this.type) {
                case FLOAT:
                    GL20.glUniform1fv(getLocation(), this.cache);
                    break;
                case VEC2:
                    GL20.glUniform2fv(getLocation(), this.cache);
                    break;
                case VEC3:
                    GL20.glUniform3fv(getLocation(), this.cache);
                    break;
                case VEC4:
                    GL20.glUniform4fv(getLocation(), this.cache);
                    break;
                case MAT2:
                    GL20.glUniformMatrix2fv(getLocation(), this.transpose, this.cache);
                    break;
                case MAT2x3:
                    GL21.glUniformMatrix2x3fv(getLocation(), this.transpose, this.cache);
                    break;
                case MAT2x4:
                    GL21.glUniformMatrix2x4fv(getLocation(), this.transpose, this.cache);
                    break;
                case MAT3:
                    GL20.glUniformMatrix3fv(getLocation(), this.transpose, this.cache);
                    break;
                case MAT3x2:
                    GL21.glUniformMatrix3x2fv(getLocation(), this.transpose, this.cache);
                    break;
                case MAT3x4:
                    GL21.glUniformMatrix3x4fv(getLocation(), this.transpose, this.cache);
                    break;
                case MAT4:
                    GL20.glUniformMatrix4fv(getLocation(), this.transpose, this.cache);
                    break;
                case MAT4x2:
                    GL21.glUniformMatrix4x2fv(getLocation(), this.transpose, this.cache);
                    break;
                case MAT4x3:
                    GL21.glUniformMatrix4x3fv(getLocation(), this.transpose, this.cache);
                    break;
            }
        }

        public float[] make(int len) {
            return new float[len];
        }

        public int len(float[] cache) {
            return cache.length;
        }

        public boolean equals(float[] a, float[] b) {
            return Arrays.equals(a, b);
        }
    }

    static abstract class UniformEntry<T> extends CCUniform {
        protected T cache;

        protected boolean transpose;

        public UniformEntry(String name, UniformType type, int count, Shader parent) {
            super(name, type, count, parent);
        }

        public void setSafe(float f0, float f1, float f2, float f3) {
            assert this.type.getCarrier() == UniformType.Carrier.FLOAT;
            switch (this.type.getSize()) {
                case 1:
                    glUniform1f(f0);
                    break;
                case 2:
                    glUniform2f(f0, f1);
                    break;
                case 3:
                    glUniform3f(f0, f1, f2);
                    break;
                case 4:
                    glUniform4f(f0, f1, f2, f3);
                    break;
            }
        }

        public void set(float f0) {
            glUniformF(false, f0);
        }

        public void set(float f0, float f1) {
            glUniformF(false, f0, f1);
        }

        public void set(int i, float f) {
            throw new UnsupportedOperationException();
        }

        public void set(float f0, float f1, float f2) {
            glUniformF(false, f0, f1, f2);
        }

        public void set(Vector3f vec) {
            glUniformF(false, vec.x(), vec.y(), vec.z());
        }

        public void set(float f0, float f1, float f2, float f3) {
            glUniformF(false, f0, f1, f2, f3);
        }

        public void set(Vector4f vec) {
            glUniformF(false, vec.x(), vec.y(), vec.z(), vec.w());
        }

        public void set(@NotNull Matrix4f mat) {
            glUniformMatrix4f(mat);
        }

        public void set(int i0, int i1) {
            glUniformI(i0, i1);
        }

        public void set(int i0, int i1, int i2) {
            glUniformI(i0, i1, i2);
        }

        public void set(int i0, int i1, int i2, int i3) {
            glUniformI(i0, i1, i2, i3);
        }

        public void set(int i0) {
            glUniformI(i0);
        }

        public void set(float @NotNull [] p_85632_) {
            glUniformF(false, p_85632_);
        }

        public void setMat2x2(float m00, float m01, float m10, float m11) {
            glUniformF(true, m00, m01, m10, m11);
        }

        public void setMat2x3(float m00, float m01, float m02, float m10, float m11, float m12) {
            glUniformF(true, m00, m01, m02, m10, m11, m12);
        }

        public void setMat2x4(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13) {
            glUniformF(true, m00, m01, m02, m03, m10, m11, m12, m13);
        }

        public void setMat3x2(float m00, float m01, float m10, float m11, float m20, float m21) {
            glUniformF(true, m00, m01, m10, m11, m20, m21);
        }

        public void setMat3x3(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
            glUniformF(true, m00, m01, m02, m10, m11, m12, m20, m21, m22);
        }

        public void setMat3x4(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23) {
            glUniformF(true, m00, m01, m02, m03, m10, m11, m12, m13, m20, m21,
                    m22, m23);
        }

        public void setMat4x2(float m00, float m01, float m10, float m11, float m20, float m21, float m30, float m31) {
            glUniformF(true, m00, m01, m10, m11, m20, m21, m30, m31);
        }

        public void setMat4x3(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m23, float m30, float m31, float m32) {
            glUniformF(true, m00, m01, m02, m10, m11, m12, m20, m21, m23, m30,
                    m31, m32);
        }

        public void setMat4x4(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
            glUniformF(true, m00, m01, m02, m03, m10, m11, m12, m13, m20, m21,
                    m22, m23, m30, m31, m32, m33);
        }

        public void glUniformI(int... values) {
            if (this.type.getCarrier() == UniformType.Carrier.INT || this.type.getCarrier() != UniformType.Carrier.U_INT);
            set((T)values, false);
        }

        public void glUniformD(boolean transpose, double... values) {
            if (this.type.getCarrier() == UniformType.Carrier.DOUBLE || this.type.getCarrier() != UniformType.Carrier.D_MATRIX);
            set((T)values, transpose);
        }

        public void glUniformF(boolean transpose, float... values) {
            if (this.type.getCarrier() == UniformType.Carrier.FLOAT || this.type.getCarrier() != UniformType.Carrier.MATRIX);
            set((T)values, transpose);
        }

        public void set(T values, boolean transpose) {
            assert !transpose || this.type.getCarrier() == UniformType.Carrier.MATRIX || this.type.getCarrier() == UniformType.Carrier.D_MATRIX;
            if (len(values) != getCount());
            if (!equals(this.cache, values) || this.transpose != transpose) {
                this.cache = values;
                this.transpose = transpose;
                this.dirty = true;
            }
        }

        public void upload() {
            if (!this.dirty)
                return;
            flush();
            this.dirty = false;
        }

        public abstract void flush();

        public abstract int len(T param1T);

        public abstract boolean equals(T param1T1, T param1T2);
    }

    static class DoubleUniform extends UniformEntry<double[]> {
        public DoubleUniform(String name, UniformType type, int count, Shader parent) {
            super(name, type, count, parent);
            assert type.getCarrier() == UniformType.Carrier.DOUBLE || type.getCarrier() == UniformType.Carrier.D_MATRIX;
        }

        public void flush() {
            assert this.cache != null;
            switch (this.type) {
                case DOUBLE:
                    GL40.glUniform1dv(getLocation(), this.cache);
                    break;
                case D_VEC2:
                    GL40.glUniform2dv(getLocation(), this.cache);
                    break;
                case D_VEC3:
                    GL40.glUniform3dv(getLocation(), this.cache);
                    break;
                case D_VEC4:
                    GL40.glUniform4dv(getLocation(), this.cache);
                    break;
                case D_MAT2:
                    GL40.glUniformMatrix2dv(getLocation(), this.transpose, this.cache);
                    break;
                case D_MAT2x3:
                    GL40.glUniformMatrix2x3dv(getLocation(), this.transpose, this.cache);
                    break;
                case D_MAT2x4:
                    GL40.glUniformMatrix2x4dv(getLocation(), this.transpose, this.cache);
                    break;
                case D_MAT3:
                    GL40.glUniformMatrix3dv(getLocation(), this.transpose, this.cache);
                    break;
                case D_MAT3x2:
                    GL40.glUniformMatrix3x2dv(getLocation(), this.transpose, this.cache);
                    break;
                case D_MAT3x4:
                    GL40.glUniformMatrix3x4dv(getLocation(), this.transpose, this.cache);
                    break;
                case D_MAT4:
                    GL40.glUniformMatrix4dv(getLocation(), this.transpose, this.cache);
                    break;
                case D_MAT4x2:
                    GL40.glUniformMatrix4x2dv(getLocation(), this.transpose, this.cache);
                    break;
                case D_MAT4x3:
                    GL40.glUniformMatrix4x3dv(getLocation(), this.transpose, this.cache);
                    break;
            }
        }

        public int len(double[] cache) {
            return cache.length;
        }

        public boolean equals(double[] a, double[] b) {
            return Arrays.equals(a, b);
        }
    }
}
