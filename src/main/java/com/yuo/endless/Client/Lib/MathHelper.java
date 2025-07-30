package com.yuo.endless.Client.Lib;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

public class MathHelper {
    public static double[] SIN_TABLE = new double[65536];

    public MathHelper() {
    }

    public static double sin(double d) {
        return SIN_TABLE[(int)((float)d * 10430.378F) & '\uffff'];
    }

    public static double clip(double value, double min, double max) {
        if (value > max) {
            value = max;
        }

        if (value < min) {
            value = min;
        }

        return value;
    }

    public static double map(double valueIn, double inMin, double inMax, double outMin, double outMax) {
        return (valueIn - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static float map(float valueIn, float inMin, float inMax, float outMin, float outMax) {
        return (valueIn - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static boolean between(double min, double value, double max) {
        return min <= value && value <= max;
    }

    public static int floor(double d) {
        return MathUtils.floor(d);
    }

    public static int floor(float f) {
        return MathUtils.floor(f);
    }

    public static int ceil(double d) {
        return MathUtils.ceil(d);
    }

    public static int ceil(float f) {
        return MathUtils.ceil(f);
    }

    public static BlockPos min(Vec3i pos1, Vec3i pos2) {
        return new BlockPos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
    }

    public static BlockPos max(Vec3i pos1, Vec3i pos2) {
        return new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
    }

    static {
        for(int i = 0; i < 65536; ++i) {
            SIN_TABLE[i] = Math.sin((double)i / 65536.0 * 2.0 * Math.PI);
        }

    }
}
