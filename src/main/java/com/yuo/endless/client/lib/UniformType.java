package com.yuo.endless.client.lib;

import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum UniformType {
    INT(UniformType.Carrier.INT, 1),
    U_INT(UniformType.Carrier.U_INT, 1),
    FLOAT(UniformType.Carrier.FLOAT, 1),
    VEC2(UniformType.Carrier.FLOAT, 2),
    I_VEC2(UniformType.Carrier.INT, 2),
    U_VEC2(UniformType.Carrier.U_INT, 2),
    B_VEC2(UniformType.Carrier.INT, 2),
    VEC3(UniformType.Carrier.FLOAT, 3),
    I_VEC3(UniformType.Carrier.INT, 3),
    U_VEC3(UniformType.Carrier.U_INT, 3),
    B_VEC3(UniformType.Carrier.INT, 3),
    VEC4(UniformType.Carrier.FLOAT, 4),
    I_VEC4(UniformType.Carrier.INT, 4),
    U_VEC4(UniformType.Carrier.U_INT, 4),
    B_VEC4(UniformType.Carrier.INT, 4),
    MAT2(UniformType.Carrier.MATRIX, 4),
    MAT2x3(UniformType.Carrier.MATRIX, 6),
    MAT2x4(UniformType.Carrier.MATRIX, 8),
    MAT3(UniformType.Carrier.MATRIX, 9),
    MAT3x2(UniformType.Carrier.MATRIX, 6),
    MAT3x4(UniformType.Carrier.MATRIX, 12),
    MAT4(UniformType.Carrier.MATRIX, 16),
    MAT4x2(UniformType.Carrier.MATRIX, 8),
    MAT4x3(UniformType.Carrier.MATRIX, 12),
    DOUBLE(UniformType.Carrier.DOUBLE, 1),
    D_VEC2(UniformType.Carrier.DOUBLE, 2),
    D_VEC3(UniformType.Carrier.DOUBLE, 3),
    D_VEC4(UniformType.Carrier.DOUBLE, 4),
    D_MAT2(UniformType.Carrier.D_MATRIX, 4),
    D_MAT2x3(UniformType.Carrier.D_MATRIX, 6),
    D_MAT2x4(UniformType.Carrier.D_MATRIX, 8),
    D_MAT3(UniformType.Carrier.D_MATRIX, 9),
    D_MAT3x2(UniformType.Carrier.D_MATRIX, 6),
    D_MAT3x4(UniformType.Carrier.D_MATRIX, 12),
    D_MAT4(UniformType.Carrier.D_MATRIX, 16),
    D_MAT4x2(UniformType.Carrier.D_MATRIX, 8),
    D_MAT4x3(UniformType.Carrier.D_MATRIX, 12);

    public static final UniformType[] VALUES = values();
    private final UniformType.Carrier carrier;
    private final int size;

    UniformType(UniformType.Carrier carrier, int size) {
        this.carrier = carrier;
        this.size = size;
    }

    public UniformType.Carrier getCarrier() {
        return this.carrier;
    }

    public int getSize() {
        return this.size;
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
