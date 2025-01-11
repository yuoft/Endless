package com.yuo.endless.Client.Lib;

import com.mojang.math.Matrix4f;

public interface ICCUniform {
    default void glUniform1f(float f) {
        glUniformF(false, new float[] { f });
    }

    default void glUniform2f(float x, float y) {
        glUniformF(false, new float[] { x, y });
    }

    default void glUniform3f(float x, float y, float z) {
        glUniformF(false, new float[] { x, y, z });
    }

    default void glUniform4f(float x, float y, float z, float a) {
        glUniformF(false, new float[] { x, y, z, a });
    }

    default void glUniformMatrix4f(Matrix4f m) {
        glUniformF(false, toArrayF(m));
    }

    void glUniformI(int... paramVarArgs);

    void glUniformF(boolean paramBoolean, float... paramVarArgs);

    void glUniformD(boolean paramBoolean, double... paramVarArgs);

    static float[] toArrayF(Matrix4f m) {
        return new float[] {
                m.m00, m.m10, m.m20, m.m30, m.m01, m.m11, m.m21, m.m31, m.m02, m.m12,
                m.m22, m.m32, m.m03, m.m13, m.m23, m.m33 };
    }
}
