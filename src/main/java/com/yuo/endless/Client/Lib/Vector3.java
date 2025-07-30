package com.yuo.endless.Client.Lib;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Vector3 implements Copyable<Vector3> {
    public static final Vector3 ZERO = new Vector3(0.0, 0.0, 0.0);
    public static final Vector3 CENTER = new Vector3(0.5, 0.5, 0.5);
    public static final Vector3 ONE = new Vector3(1.0, 1.0, 1.0);
    public static final Vector3 X_POS = new Vector3(1.0, 0.0, 0.0);
    public static final Vector3 X_NEG = new Vector3(-1.0, 0.0, 0.0);
    public static final Vector3 Y_POS = new Vector3(0.0, 1.0, 0.0);
    public static final Vector3 Y_NEG = new Vector3(0.0, -1.0, 0.0);
    public static final Vector3 Z_POS = new Vector3(0.0, 0.0, 1.0);
    public static final Vector3 Z_NEG = new Vector3(0.0, 0.0, -1.0);
    public double x;
    public double y;
    public double z;

    public Vector3() {
    }

    public Vector3(double d, double d1, double d2) {
        this.x = d;
        this.y = d1;
        this.z = d2;
    }

    public Vector3(Vector3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vector3(double[] da) {
        this(da[0], da[1], da[2]);
    }

    public Vector3(float[] fa) {
        this((double)fa[0], (double)fa[1], (double)fa[2]);
    }

    public Vector3(Vec3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public static Vector3 fromBlockPos(BlockPos pos) {
        return fromVec3i(pos);
    }

    public static Vector3 fromVec3i(Vec3i pos) {
        return new Vector3((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    }

    public static Vector3 fromBlockPosCenter(BlockPos pos) {
        return fromBlockPos(pos).add(0.5);
    }

    public static Vector3 fromEntity(Entity e) {
        return new Vector3(e.position());
    }

    public static Vector3 fromEntityCenter(Entity e) {
        return (new Vector3(e.position())).add(0.0, e.getMyRidingOffset() + (double)(e.getBbHeight() / 2.0F), 0.0);
    }

    public static Vector3 fromTile(BlockEntity tile) {
        return fromBlockPos(tile.getBlockPos());
    }

    public static Vector3 fromTileCenter(BlockEntity tile) {
        return fromTile(tile).add(0.5);
    }

    public static Vector3 fromAxes(double[] da) {
        return new Vector3(da[2], da[0], da[1]);
    }

    public static Vector3 fromAxes(float[] fa) {
        return new Vector3((double)fa[2], (double)fa[0], (double)fa[1]);
    }

    public static Vector3 fromArray(double[] da) {
        return new Vector3(da[0], da[1], da[2]);
    }

    public static Vector3 fromArray(float[] fa) {
        return new Vector3((double)fa[0], (double)fa[1], (double)fa[2]);
    }

    public static Vector3 fromNBT(CompoundTag tag) {
        return new Vector3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
    }

    public Vec3 vec3() {
        return new Vec3(this.x, this.y, this.z);
    }

    public BlockPos pos() {
        return new BlockPos(MathHelper.floor(this.x), MathHelper.floor(this.y), MathHelper.floor(this.z));
    }

    public CompoundTag writeToNBT(CompoundTag tag) {
        tag.putDouble("x", this.x);
        tag.putDouble("y", this.y);
        tag.putDouble("z", this.z);
        return tag;
    }

    public Vector3f vector3f() {
        return new Vector3f((float)this.x, (float)this.y, (float)this.z);
    }

    public Vector4f vector4f() {
        return new Vector4f((float)this.x, (float)this.y, (float)this.z, 1.0F);
    }

    public double[] toArrayD() {
        return new double[]{this.x, this.y, this.z};
    }

    public float[] toArrayF() {
        return new float[]{(float)this.x, (float)this.y, (float)this.z};
    }

    public Vector3 set(double x1, double y1, double z1) {
        this.x = x1;
        this.y = y1;
        this.z = z1;
        return this;
    }

    public Vector3 set(double d) {
        return this.set(d, d, d);
    }

    public Vector3 set(Vector3 vec) {
        return this.set(vec.x, vec.y, vec.z);
    }

    public Vector3 set(Vec3i vec) {
        return this.set((double)vec.getX(), (double)vec.getY(), (double)vec.getZ());
    }

    public Vector3 set(double[] da) {
        return this.set(da[0], da[1], da[2]);
    }

    public Vector3 set(float[] fa) {
        return this.set((double)fa[0], (double)fa[1], (double)fa[2]);
    }

    public Vector3 add(double dx, double dy, double dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
        return this;
    }

    public Vector3 add(double d) {
        return this.add(d, d, d);
    }

    public Vector3 add(Vector3 vec) {
        return this.add(vec.x, vec.y, vec.z);
    }

    public Vector3 add(Vec3 vec) {
        return this.add(vec.x, vec.y, vec.z);
    }

    public Vector3 add(BlockPos pos) {
        return this.add((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    }

    public Vector3 subtract(double dx, double dy, double dz) {
        this.x -= dx;
        this.y -= dy;
        this.z -= dz;
        return this;
    }

    public Vector3 subtract(double d) {
        return this.subtract(d, d, d);
    }

    public Vector3 subtract(Vector3 vec) {
        return this.subtract(vec.x, vec.y, vec.z);
    }

    public Vector3 subtract(Vec3 vec) {
        return this.subtract(vec.x, vec.y, vec.z);
    }

    public Vector3 subtract(BlockPos pos) {
        return this.subtract((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    }

    public Vector3 multiply(double fx, double fy, double fz) {
        this.x *= fx;
        this.y *= fy;
        this.z *= fz;
        return this;
    }

    public Vector3 multiply(double f) {
        return this.multiply(f, f, f);
    }

    public Vector3 multiply(Vector3 f) {
        return this.multiply(f.x, f.y, f.z);
    }

    public Vector3 divide(double fx, double fy, double fz) {
        this.x /= fx;
        this.y /= fy;
        this.z /= fz;
        return this;
    }

    public Vector3 divide(double f) {
        return this.divide(f, f, f);
    }

    public Vector3 divide(Vector3 vec) {
        return this.divide(vec.x, vec.y, vec.z);
    }

    public Vector3 divide(BlockPos pos) {
        return this.divide((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
    }

    public Vector3 floor() {
        this.x = (double)MathHelper.floor(this.x);
        this.y = (double)MathHelper.floor(this.y);
        this.z = (double)MathHelper.floor(this.z);
        return this;
    }

    public Vector3 ceil() {
        this.x = (double)MathHelper.ceil(this.x);
        this.y = (double)MathHelper.ceil(this.y);
        this.z = (double)MathHelper.ceil(this.z);
        return this;
    }

    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public double magSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public Vector3 negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public Vector3 normalize() {
        double d = this.mag();
        if (d != 0.0) {
            this.multiply(1.0 / d);
        }

        return this;
    }

    public double distance(Vector3 other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public double distanceSquared(Vector3 other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double dotProduct(double x1, double y1, double z1) {
        return x1 * this.x + y1 * this.y + z1 * this.z;
    }

    public double dotProduct(Vector3 vec) {
        double d = vec.x * this.x + vec.y * this.y + vec.z * this.z;
        if (d > 1.0 && d < 1.00001) {
            d = 1.0;
        } else if (d < -1.0 && d > -1.00001) {
            d = -1.0;
        }

        return d;
    }

    public Vector3 crossProduct(Vector3 vec) {
        double d = this.y * vec.z - this.z * vec.y;
        double d1 = this.z * vec.x - this.x * vec.z;
        double d2 = this.x * vec.y - this.y * vec.x;
        this.x = d;
        this.y = d1;
        this.z = d2;
        return this;
    }

    public Vector3 perpendicular() {
        return this.z == 0.0 ? this.zCrossProduct() : this.xCrossProduct();
    }

    public Vector3 xCrossProduct() {
        double d = this.z;
        double d1 = -this.y;
        this.x = 0.0;
        this.y = d;
        this.z = d1;
        return this;
    }

    public Vector3 zCrossProduct() {
        double d = this.y;
        double d1 = -this.x;
        this.x = d;
        this.y = d1;
        this.z = 0.0;
        return this;
    }

    public Vector3 yCrossProduct() {
        double d = -this.z;
        double d1 = this.x;
        this.x = d;
        this.y = 0.0;
        this.z = d1;
        return this;
    }

    public double scalarProject(Vector3 b) {
        double l = b.mag();
        return l == 0.0 ? 0.0 : this.dotProduct(b) / l;
    }

    public Vector3 project(Vector3 b) {
        double l = b.magSquared();
        if (l == 0.0) {
            this.set(0.0, 0.0, 0.0);
            return this;
        } else {
            double m = this.dotProduct(b) / l;
            this.set(b).multiply(m);
            return this;
        }
    }

    public Vector3 rotate(double angle, Vector3 axis) {
        Quat.aroundAxis(axis.copy().normalize(), angle).rotate(this);
        return this;
    }

    public Vector3 rotate(Quat rotator) {
        rotator.rotate(this);
        return this;
    }

    public double angle(Vector3 vec) {
        return Math.acos(this.copy().normalize().dotProduct(vec.copy().normalize()));
    }

    public @Nullable Vector3 YZintercept(Vector3 end, double px) {
        double dx = end.x - this.x;
        double dy = end.y - this.y;
        double dz = end.z - this.z;
        if (dx == 0.0) {
            return null;
        } else {
            double d = (px - this.x) / dx;
            if (MathHelper.between(-1.0E-5, d, 1.0E-5)) {
                return this;
            } else if (!MathHelper.between(0.0, d, 1.0)) {
                return null;
            } else {
                this.x = px;
                this.y += d * dy;
                this.z += d * dz;
                return this;
            }
        }
    }

    public @Nullable Vector3 XZintercept(Vector3 end, double py) {
        double dx = end.x - this.x;
        double dy = end.y - this.y;
        double dz = end.z - this.z;
        if (dy == 0.0) {
            return null;
        } else {
            double d = (py - this.y) / dy;
            if (MathHelper.between(-1.0E-5, d, 1.0E-5)) {
                return this;
            } else if (!MathHelper.between(0.0, d, 1.0)) {
                return null;
            } else {
                this.x += d * dx;
                this.y = py;
                this.z += d * dz;
                return this;
            }
        }
    }

    public @Nullable Vector3 XYintercept(Vector3 end, double pz) {
        double dx = end.x - this.x;
        double dy = end.y - this.y;
        double dz = end.z - this.z;
        if (dz == 0.0) {
            return null;
        } else {
            double d = (pz - this.z) / dz;
            if (MathHelper.between(-1.0E-5, d, 1.0E-5)) {
                return this;
            } else if (!MathHelper.between(0.0, d, 1.0)) {
                return null;
            } else {
                this.x += d * dx;
                this.y += d * dy;
                this.z = pz;
                return this;
            }
        }
    }

    public boolean isZero() {
        return this.x == 0.0 && this.y == 0.0 && this.z == 0.0;
    }

    public boolean isAxial() {
        return this.x == 0.0 ? this.y == 0.0 || this.z == 0.0 : this.y == 0.0 && this.z == 0.0;
    }

    public double getSide(int side) {
        switch (side) {
            case 0:
            case 1:
                return this.y;
            case 2:
            case 3:
                return this.z;
            case 4:
            case 5:
                return this.x;
            default:
                throw new IndexOutOfBoundsException("Switch Falloff");
        }
    }

    public Vector3 setSide(int s, double v) {
        switch (s) {
            case 0:
            case 1:
                this.y = v;
                break;
            case 2:
            case 3:
                this.z = v;
                break;
            case 4:
            case 5:
                this.x = v;
                break;
            default:
                throw new IndexOutOfBoundsException("Switch Falloff");
        }

        return this;
    }

    public int hashCode() {
        long j = Double.doubleToLongBits(this.x);
        int i = (int)(j ^ j >>> 32);
        j = Double.doubleToLongBits(this.y);
        i = 31 * i + (int)(j ^ j >>> 32);
        j = Double.doubleToLongBits(this.z);
        i = 31 * i + (int)(j ^ j >>> 32);
        return i;
    }

    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        } else if (!(o instanceof Vector3)) {
            return false;
        } else {
            Vector3 v = (Vector3)o;
            return this.x == v.x && this.y == v.y && this.z == v.z;
        }
    }

    public boolean equalsT(Vector3 v) {
        return MathHelper.between(this.x - 1.0E-5, v.x, this.x + 1.0E-5) && MathHelper.between(this.y - 1.0E-5, v.y, this.y + 1.0E-5) && MathHelper.between(this.z - 1.0E-5, v.z, this.z + 1.0E-5);
    }

    public Vector3 copy() {
        return new Vector3(this);
    }

    public String toString() {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        BigDecimal var10000 = new BigDecimal(this.x, cont);
        return "Vector3(" + var10000 + ", " + new BigDecimal(this.y, cont) + ", " + new BigDecimal(this.z, cont) + ")";
    }

    public Translation translation() {
        return new Translation(this);
    }

    public Vector3 apply(Transformation t) {
        t.apply(this);
        return this;
    }

    public Vector3 $tilde() {
        return this.normalize();
    }

    public Vector3 unary_$tilde() {
        return this.normalize();
    }

    public Vector3 $plus(Vector3 v) {
        return this.add(v);
    }

    public Vector3 $minus(Vector3 v) {
        return this.subtract(v);
    }

    public Vector3 $times(double d) {
        return this.multiply(d);
    }

    public Vector3 $div(double d) {
        return this.multiply(1.0 / d);
    }

    public Vector3 $times(Vector3 v) {
        return this.crossProduct(v);
    }

    public double $dot$times(Vector3 v) {
        return this.dotProduct(v);
    }
}
