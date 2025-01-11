package com.yuo.endless.Client.Lib;

import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum UniformType {
    INT(Carrier.INT, 1),
    U_INT(Carrier.U_INT, 1),
    FLOAT(Carrier.FLOAT, 1),
    VEC2(Carrier.FLOAT, 2),
    I_VEC2(Carrier.INT, 2),
    U_VEC2(Carrier.U_INT, 2),
    B_VEC2(Carrier.INT, 2),
    VEC3(Carrier.FLOAT, 3),
    I_VEC3(Carrier.INT, 3),
    U_VEC3(Carrier.U_INT, 3),
    B_VEC3(Carrier.INT, 3),
    VEC4(Carrier.FLOAT, 4),
    I_VEC4(Carrier.INT, 4),
    U_VEC4(Carrier.U_INT, 4),
    B_VEC4(Carrier.INT, 4),
    MAT2(Carrier.MATRIX, 4),
    MAT2x3(Carrier.MATRIX, 6),
    MAT2x4(Carrier.MATRIX, 8),
    MAT3(Carrier.MATRIX, 9),
    MAT3x2(Carrier.MATRIX, 6),
    MAT3x4(Carrier.MATRIX, 12),
    MAT4(Carrier.MATRIX, 16),
    MAT4x2(Carrier.MATRIX, 8),
    MAT4x3(Carrier.MATRIX, 12),
    DOUBLE(Carrier.DOUBLE, 1),
    D_VEC2(Carrier.DOUBLE, 2),
    D_VEC3(Carrier.DOUBLE, 3),
    D_VEC4(Carrier.DOUBLE, 4),
    D_MAT2(Carrier.D_MATRIX, 4),
    D_MAT2x3(Carrier.D_MATRIX, 6),
    D_MAT2x4(Carrier.D_MATRIX, 8),
    D_MAT3(Carrier.D_MATRIX, 9),
    D_MAT3x2(Carrier.D_MATRIX, 6),
    D_MAT3x4(Carrier.D_MATRIX, 12),
    D_MAT4(Carrier.D_MATRIX, 16),
    D_MAT4x2(Carrier.D_MATRIX, 8),
    D_MAT4x3(Carrier.D_MATRIX, 12);

    public static final UniformType[] VALUES = values();
    private final Carrier carrier;
    private final int size;

    UniformType(Carrier carrier, int size) {
        this.carrier = carrier;
        this.size = size;
    }

    public Carrier getCarrier() {
        return this.carrier;
    }

    public int getSize() {
        return this.size;
    }

    /** @deprecated */
    @Deprecated(
            since = "1.18.2",
            forRemoval = true
    )
    public boolean isSupported() {
        return true;
    }

    public int getVanillaType() {
        return switch (this) {
            case INT, U_INT -> 0;
            case FLOAT -> 4;
            case VEC2 -> 5;
            case I_VEC2, U_VEC2, B_VEC2 -> 1;
            case VEC3 -> 6;
            case I_VEC3, U_VEC3, B_VEC3 -> 2;
            case VEC4 -> 7;
            case I_VEC4, U_VEC4, B_VEC4 -> 3;
            case MAT2 -> 8;
            case MAT3 -> 9;
            case MAT4 -> 10;
            default -> -1;
        };
    }

    public static @Nullable UniformType parse(String s) {
        return switch (s.toLowerCase(Locale.ROOT)) {
            case "matrix2x2" -> MAT2;
            case "matrix3x3" -> MAT3;
            case "matrix4x4" -> MAT4;
            default -> {
                for (UniformType value : VALUES) {
                    String n = value.name().toLowerCase(Locale.ROOT);
                    if (n.equals(s)) {
                        yield value;
                    }
                }
                yield null;
            }
        };
    }

    public enum Carrier {
        INT,
        U_INT,
        FLOAT,
        DOUBLE,
        MATRIX,
        D_MATRIX;

        Carrier() {
        }
    }
}
