package com.yuo.endless.Client.Lib;

import com.mojang.math.Vector3d;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class Vector3 implements Copyable<Vector3> {
    public static final Vector3 ZERO = new Vector3(0.0D, 0.0D, 0.0D);

    public double x;

    public double y;

    public double z;

    public Vector3() {}

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
        this(fa[0], fa[1], fa[2]);
    }

    public Vector3(Vec3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public static Vec3 fromEntity(Entity e) {
        return e.getViewVector(0.5f);
    }

    public Vector3d vec3() {
        return new Vector3d(this.x, this.y, this.z);
    }

    public BlockPos pos() {
        return new BlockPos(this.x, this.y, this.z);
    }

    public Vector3 set(double x1, double y1, double z1) {
        this.x = x1;
        this.y = y1;
        this.z = z1;
        return this;
    }

    public Vector3 set(double d) {
        return set(d, d, d);
    }

    public Vector3 set(Vector3 vec) {
        return set(vec.x, vec.y, vec.z);
    }

    public Vector3 set(Vec3i vec) {
        return set(vec.getX(), vec.getY(), vec.getZ());
    }

    public Vector3 set(double[] da) {
        return set(da[0], da[1], da[2]);
    }

    public Vector3 set(float[] fa) {
        return set(fa[0], fa[1], fa[2]);
    }

    public Vector3 add(double dx, double dy, double dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
        return this;
    }

    public Vector3 add(double d) {
        return add(d, d, d);
    }

    public Vector3 add(Vector3 vec) {
        return add(vec.x, vec.y, vec.z);
    }

    public Vector3 add(Vector3d vec) {
        return add(vec.x, vec.y, vec.z);
    }

    public Vector3 add(BlockPos pos) {
        return add(pos.getX(), pos.getY(), pos.getZ());
    }

    public Vector3 subtract(double dx, double dy, double dz) {
        this.x -= dx;
        this.y -= dy;
        this.z -= dz;
        return this;
    }

    public Vector3 subtract(Vector3 vec) {
        return subtract(vec.x, vec.y, vec.z);
    }

    public Vector3 multiply(double fx, double fy, double fz) {
        this.x *= fx;
        this.y *= fy;
        this.z *= fz;
        return this;
    }

    public Vector3 multiply(double f) {
        return multiply(f, f, f);
    }

    public Vector3 multiply(Vector3 f) {
        return multiply(f.x, f.y, f.z);
    }

    public Vector3 divide(double fx, double fy, double fz) {
        this.x /= fx;
        this.y /= fy;
        this.z /= fz;
        return this;
    }

    public Vector3 divide(double f) {
        return divide(f, f, f);
    }

    public Vector3 floor() {
        this.x = MathHelper.floor(this.x);
        this.y = MathHelper.floor(this.y);
        this.z = MathHelper.floor(this.z);
        return this;
    }

    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector3 normalize() {
        double d = mag();
        if (d != 0.0D)
            multiply(1.0D / d);
        return this;
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

    public Vector3 rotate(double angle, Vector3 axis) {
        Quat.aroundAxis(axis.copy().normalize(), angle).rotate(this);
        return this;
    }

    public int hashCode() {
        long j = Double.doubleToLongBits(this.x);
        int i = (int)(j ^ j >>> 32L);
        j = Double.doubleToLongBits(this.y);
        i = 31 * i + (int)(j ^ j >>> 32L);
        j = Double.doubleToLongBits(this.z);
        i = 31 * i + (int)(j ^ j >>> 32L);
        return i;
    }

    public boolean equals(Object o) {
        if (super.equals(o))
            return true;
        if (!(o instanceof Vector3))
            return false;
        Vector3 v = (Vector3)o;
        return (this.x == v.x && this.y == v.y && this.z == v.z);
    }

    public boolean equalsT(Vector3 v) {
        return (MathHelper.between(this.x - 1.0E-5D, v.x, this.x + 1.0E-5D) && MathHelper.between(this.y - 1.0E-5D, v.y, this.y + 1.0E-5D) && MathHelper.between(this.z - 1.0E-5D, v.z, this.z + 1.0E-5D));
    }

    public Vector3 copy() {
        return new Vector3(this);
    }

    public Translation translation() {
        return new Translation(this);
    }

    public Vector3 apply(Transformation t) {
        t.apply(this);
        return this;
    }
}