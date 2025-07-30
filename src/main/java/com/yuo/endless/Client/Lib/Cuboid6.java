package com.yuo.endless.Client.Lib;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Cuboid6 implements Copyable<Cuboid6> {
    public Vector3 min;
    public Vector3 max;

    public Cuboid6() {
        this(new Vector3(), new Vector3());
    }

    public Cuboid6(Vector3 min, Vector3 max) {
        this.min = min;
        this.max = max;
    }

    public Cuboid6(Cuboid6 cuboid) {
        min = cuboid.min.copy();
        max = cuboid.max.copy();
    }

    public Cuboid6 set(double minx, double miny, double minz, double maxx, double maxy, double maxz) {
        min.set(minx, miny, minz);
        max.set(maxx, maxy, maxz);
        return this;
    }

    public Cuboid6 set(Vector3 min, Vector3 max) {
        return set(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    public Cuboid6 set(Vec3i min, Vec3i max) {
        return set(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    public Cuboid6 set(Cuboid6 c) {
        return set(c.min.x, c.min.y, c.min.z, c.max.x, c.max.y, c.max.z);
    }

    public Cuboid6 set(AABB bb) {
        return set(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
    }

    public Cuboid6 add(double dx, double dy, double dz) {
        min.add(dx, dy, dz);
        max.add(dx, dy, dz);
        return this;
    }

    public Cuboid6 add(double d) {
        return add(d, d, d);
    }

    public Cuboid6 add(Vector3 vec) {
        return add(vec.x, vec.y, vec.z);
    }

    public Cuboid6 add(Vec3i vec) {
        return add(vec.getX(), vec.getY(), vec.getZ());
    }

    public Cuboid6 add(BlockPos pos) {
        return add(pos.getX(), pos.getY(), pos.getZ());
    }

    public Cuboid6 offset(Cuboid6 o) {
        min.add(o.min);
        max.add(o.max);
        return this;
    }

    public boolean contains(double x, double y, double z) {
        return min.x - 1E-5 <= x && min.y - 1E-5 <= y && min.z - 1E-5 <= z && max.x + 1E-5 >= x && max.y + 1E-5 >= y && max.z + 1E-5 >= z;
    }

    public boolean contains(Vector3 vec) {
        return contains(vec.x, vec.y, vec.z);
    }

    public int hashCode() {
        int j = Double.hashCode(min.x);
        j = 31 * j + Double.hashCode(min.y);
        j = 31 * j + Double.hashCode(min.z);
        j = 31 * j + Double.hashCode(max.x);
        j = 31 * j + Double.hashCode(max.y);
        j = 31 * j + Double.hashCode(max.z);
        return j;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (!(obj instanceof Cuboid6 c)) {
            return false;
        }
        return min.equals(c.min) && max.equals(c.max);
    }

    @Override
    public Cuboid6 copy() {
        return new Cuboid6(this);
    }

    @Override
    public String toString() {
        MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
        return "Cuboid: (" + new BigDecimal(min.x, cont) + ", " + new BigDecimal(min.y, cont) + ", " + new BigDecimal(min.z, cont) + ") -> (" + new BigDecimal(max.x, cont) + ", " + new BigDecimal(max.y, cont) + ", " + new BigDecimal(max.z, cont) + ")";
    }

    public Cuboid6 apply(Transformation t) {
        t.apply(min);
        t.apply(max);
        double temp;
        if (min.x > max.x) {
            temp = min.x;
            min.x = max.x;
            max.x = temp;
        }
        if (min.y > max.y) {
            temp = min.y;
            min.y = max.y;
            max.y = temp;
        }
        if (min.z > max.z) {
            temp = min.z;
            min.z = max.z;
            max.z = temp;
        }
        return this;
    }
}
