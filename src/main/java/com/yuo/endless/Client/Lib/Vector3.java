package com.yuo.endless.Client.Lib;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Vector3 implements Copyable<Vector3> {
    public static final Vector3 ZERO = new Vector3(0.0, 0.0, 0.0);
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
        this(fa[0], fa[1], fa[2]);
    }

    public Vector3(Vec3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
    }

    public Vec3 vec3() {
        return new Vec3(this.x, this.y, this.z);
    }

    public BlockPos pos() {
        return new BlockPos(MathHelper.floor(this.x), MathHelper.floor(this.y), MathHelper.floor(this.z));
    }

    public Vector3f vector3f() {
        return new Vector3f((float)this.x, (float)this.y, (float)this.z);
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
        return this.set(vec.getX(), vec.getY(), vec.getZ());
    }

    public Vector3 set(double[] da) {
        return this.set(da[0], da[1], da[2]);
    }

    public Vector3 set(float[] fa) {
        return this.set(fa[0], fa[1], fa[2]);
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

    public Vector3 subtract(Vector3 vec) {
        return this.subtract(vec.x, vec.y, vec.z);
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

    public Vector3 floor() {
        this.x = MathHelper.floor(this.x);
        this.y = MathHelper.floor(this.y);
        this.z = MathHelper.floor(this.z);
        return this;
    }

    public Vector3 ceil() {
        this.x = MathHelper.ceil(this.x);
        this.y = MathHelper.ceil(this.y);
        this.z = MathHelper.ceil(this.z);
        return this;
    }

    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector3 normalize() {
        double d = this.mag();
        if (d != 0.0) {
            this.multiply(1.0 / d);
        }

        return this;
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

    public double scalarProject(Vector3 b) {
        double l = b.mag();
        return l == 0.0 ? 0.0 : this.dotProduct(b) / l;
    }

    public void rotate(Quat rotator) {
        rotator.rotate(this);
    }


    public int hashCode() {
        int i = Double.hashCode(this.x);
        i = 31 * i + Double.hashCode(this.y);
        i = 31 * i + Double.hashCode(this.z);
        return i;
    }

    public boolean equals(Object o) {
        if (super.equals(o)) {
            return true;
        } else if (!(o instanceof Vector3 v)) {
            return false;
        } else {
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
}
