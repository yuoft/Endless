package com.yuo.endless.client.lib;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/5/15 9:32
 * Version: 1.0
 */
public class MathUtils {
    public static double[] SIN_TABLE = new double[65536];

    static {
        for (int i = 0; i < 65536; ++i) {
            SIN_TABLE[i] = Math.sin(i / 65536D * 2 * Math.PI);
        }
    }

    public static double sin(double d) {
        return SIN_TABLE[(int) ((float) d * 10430.378F) & 65535];
    }

    /**
     * Maps a value range to another value range.
     *
     * @param valueIn The value to map.
     * @param inMin   The minimum of the input value range.
     * @param inMax   The maximum of the input value range
     * @param outMin  The minimum of the output value range.
     * @param outMax  The maximum of the output value range.
     * @return The mapped value.
     */
    public static double map(double valueIn, double inMin, double inMax, double outMin, double outMax) {
        return (valueIn - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    /**
     * Maps a value range to another value range.
     *
     * @param valueIn The value to map.
     * @param inMin   The minimum of the input value range.
     * @param inMax   The maximum of the input value range
     * @param outMin  The minimum of the output value range.
     * @param outMax  The maximum of the output value range.
     * @return The mapped value.
     */
    public static float map(float valueIn, float inMin, float inMax, float outMin, float outMax) {
        return (valueIn - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    /**
     * @return {@code min <= value <= max}
     */
    public static boolean between(double min, double value, double max) {
        return min <= value && value <= max;
    }

    public static int floor(double d) {
        int i = (int) d;
        return d < (double) i ? i - 1 : i;
    }

    public static int floor(float f) {
        int i = (int) f;
        return f < (float) i ? i - 1 : i;
    }

    public static int ceil(double d) {
        int i = (int) d;
        return d > (double) i ? i + 1 : i;
    }

    public static int ceil(float f) {
        int i = (int) f;
        return f > (float) i ? i + 1 : i;
    }

    public static BlockPos min(Vec3i pos1, Vec3i pos2) {
        return new BlockPos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
    }

    public static BlockPos max(Vec3i pos1, Vec3i pos2) {
        return new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
    }

}
