package com.yuo.endless.Client.Lib;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

public class MathHelper {
    public static <T> T unsafeCast(Object b) {
        return (T)b;
    }

    public static double[] SIN_TABLE = new double[65536];

    static {
        for (int i = 0; i < 65536; i++)
            SIN_TABLE[i] = Math.sin(i / 65536.0D * 2.0D * Math.PI);
        SIN_TABLE[0] = 0.0D;
        SIN_TABLE[16384] = 1.0D;
        SIN_TABLE[32768] = 0.0D;
        SIN_TABLE[49152] = 1.0D;
    }

    public static int floor(double d) {
        int i = (int)d;
        return (d < i) ? (i - 1) : i;
    }

    public static double map(double valueIn, double inMin, double inMax, double outMin, double outMax) {
        return (valueIn - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static float map(float valueIn, float inMin, float inMax, float outMin, float outMax) {
        return (valueIn - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static boolean between(double min, double value, double max) {
        return (min <= value && value <= max);
    }

    public static BlockPos max(Vec3i pos1, Vec3i pos2) {
        return new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
    }
}