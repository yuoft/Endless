package com.yuo.endless.Client.Lib;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Rotation extends Transformation {
    public static Transformation[] quarterRotations;
    public static Transformation[] sideRotations;
    public static Vector3[] axes;
    public static int[] sideRotMap;
    public static int[] rotSideMap;
    public static int[] sideRotOffsets;
    public double angle;
    public Vector3 axis;
    private @Nullable Quat quat;

    public static int rotateSide(int s, int r) {
        return sideRotMap[s << 2 | r];
    }

    public static int rotationTo(int s1, int s2) {
        if ((s1 & 6) == (s2 & 6)) {
            throw new IllegalArgumentException("Faces " + s1 + " and " + s2 + " are opposites");
        } else {
            return rotSideMap[s1 * 6 + s2];
        }
    }

    public static int getSidedRotation(Player player, int side) {
        Vector3 look = new Vector3(player.getViewVector(1.0F));
        double max = 0.0;
        int maxr = 0;

        for (int r = 0; r < 4; ++r) {
            Vector3 axis = axes[rotateSide(side ^ 1, r)];
            double d = look.scalarProject(axis);
            if (d > max) {
                max = d;
                maxr = r;
            }
        }

        return maxr;
    }

    public static Transformation sideOrientation(int s, int r) {
        return quarterRotations[(r + sideRotOffsets[s]) % 4].with(sideRotations[s]);
    }

    public static int getSideFromLookAngle(LivingEntity entity) {
        Vector3 look = new Vector3(entity.getViewVector(1.0F));
        double max = 0.0;
        int maxs = 0;

        for (int s = 0; s < 6; ++s) {
            double d = look.scalarProject(axes[s]);
            if (d > max) {
                max = d;
                maxs = s;
            }
        }

        return maxs;
    }

    public Rotation(double angle, Vector3 axis) {
        this.angle = angle;
        this.axis = axis;
    }

    public Rotation(double angle, double x, double y, double z) {
        this(angle, new Vector3(x, y, z));
    }

    public Rotation(Quat quat) {
        this.quat = quat;
        this.angle = Math.acos(quat.s) * 2.0;
        if (this.angle == 0.0) {
            this.axis = new Vector3(0.0, 1.0, 0.0);
        } else {
            double sa = Math.sin(this.angle * 0.5);
            this.axis = new Vector3(quat.x / sa, quat.y / sa, quat.z / sa);
        }

    }

    public Rotation(Rotation rot) {
        if (rot.quat != null) {
            this.quat = rot.quat.copy();
        }

        this.angle = rot.angle;
        this.axis = rot.axis.copy();
    }

    public void apply(Vector3 vec) {
        if (this.quat == null) {
            this.quat = Quat.aroundAxis(this.axis, this.angle);
        }

        vec.rotate(this.quat);
    }

    public void applyN(Vector3 normal) {
        this.apply(normal);
    }

    public void apply(Matrix4 mat) {
        mat.rotate(this.angle, this.axis);
    }

    public Quat toQuat() {
        if (this.quat == null) {
            this.quat = Quat.aroundAxis(this.axis, this.angle);
        }

        return this.quat;
    }

    public Transformation inverse() {
        return new Rotation(-this.angle, this.axis);
    }

    public Transformation merge(Transformation next) {
        if (next instanceof Rotation r) {
            return r.axis.equalsT(this.axis) ? new Rotation(this.angle + r.angle, this.axis) : new Rotation(this.toQuat().copy().multiply(r.toQuat()));
        } else {
            return null;
        }
    }

    public boolean isRedundant() {
        return MathHelper.between(-1.0E-5, this.angle, 1.0E-5);
    }

    public String toString() {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        BigDecimal var10000 = new BigDecimal(this.angle, cont);
        return "Rotation(" + var10000 + ", " + new BigDecimal(this.axis.x, cont) + ", " + new BigDecimal(this.axis.y, cont) + ", " + new BigDecimal(this.axis.z, cont) + ")";
    }

    public Rotation copy() {
        return new Rotation(this);
    }

    static {
        quarterRotations = new Transformation[]{RedundantTransformation.INSTANCE, new VariableTransformation(new Matrix4(0.0, 0.0, -1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0)) {
            public void apply(Vector3 vec) {
                double d1 = vec.x;
                double d2 = vec.z;
                vec.x = -d2;
                vec.z = d1;
            }

            public Transformation inverse() {
                return Rotation.quarterRotations[3];
            }
        }, new VariableTransformation(new Matrix4(-1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0)) {
            public void apply(Vector3 vec) {
                vec.x = -vec.x;
                vec.z = -vec.z;
            }

            public Transformation inverse() {
                return this;
            }
        }, new VariableTransformation(new Matrix4(0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0)) {
            public void apply(Vector3 vec) {
                double d1 = vec.x;
                double d2 = vec.z;
                vec.x = d2;
                vec.z = -d1;
            }

            public Transformation inverse() {
                return Rotation.quarterRotations[1];
            }
        }};
        sideRotations = new Transformation[]{RedundantTransformation.INSTANCE, new VariableTransformation(new Matrix4(1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 1.0)) {
            public void apply(Vector3 vec) {
                vec.y = -vec.y;
                vec.z = -vec.z;
            }

            public Transformation inverse() {
                return this;
            }
        }, new VariableTransformation(new Matrix4(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0)) {
            public void apply(Vector3 vec) {
                double d1 = vec.y;
                double d2 = vec.z;
                vec.y = -d2;
                vec.z = d1;
            }

            public Transformation inverse() {
                return Rotation.sideRotations[3];
            }
        }, new VariableTransformation(new Matrix4(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0)) {
            public void apply(Vector3 vec) {
                double d1 = vec.y;
                double d2 = vec.z;
                vec.y = d2;
                vec.z = -d1;
            }

            public Transformation inverse() {
                return Rotation.sideRotations[2];
            }
        }, new VariableTransformation(new Matrix4(0.0, 1.0, 0.0, 0.0, -1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)) {
            public void apply(Vector3 vec) {
                double d0 = vec.x;
                double d1 = vec.y;
                vec.x = d1;
                vec.y = -d0;
            }

            public Transformation inverse() {
                return Rotation.sideRotations[5];
            }
        }, new VariableTransformation(new Matrix4(0.0, -1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0)) {
            public void apply(Vector3 vec) {
                double d0 = vec.x;
                double d1 = vec.y;
                vec.x = -d1;
                vec.y = d0;
            }

            public Transformation inverse() {
                return Rotation.sideRotations[4];
            }
        }};
        axes = new Vector3[]{new Vector3(0.0, -1.0, 0.0), new Vector3(0.0, 1.0, 0.0), new Vector3(0.0, 0.0, -1.0), new Vector3(0.0, 0.0, 1.0), new Vector3(-1.0, 0.0, 0.0), new Vector3(1.0, 0.0, 0.0)};
        sideRotMap = new int[]{3, 4, 2, 5, 3, 5, 2, 4, 1, 5, 0, 4, 1, 4, 0, 5, 1, 2, 0, 3, 1, 3, 0, 2};
        rotSideMap = new int[]{-1, -1, 2, 0, 1, 3, -1, -1, 2, 0, 3, 1, 2, 0, -1, -1, 3, 1, 2, 0, -1, -1, 1, 3, 2, 0, 1, 3, -1, -1, 2, 0, 3, 1, -1, -1};
        sideRotOffsets = new int[]{0, 2, 2, 0, 1, 3};
    }
}