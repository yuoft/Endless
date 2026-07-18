package com.yuo.endless.client.lib;

public class LC implements Copyable<LC> {

    public int side;
    public float fa;
    public float fb;
    public float fc;
    public float fd;

    public LC() {
        this(0, 0, 0, 0, 0);
    }

    public LC(int s, float a, float b, float c, float d) {
        side = s;
        fa = a;
        fb = b;
        fc = c;
        fd = d;
    }

    public LC set(int s, float a, float b, float c, float d) {
        side = s;
        fa = a;
        fb = b;
        fc = c;
        fd = d;
        return this;
    }

    public LC set(LC lc) {
        return set(lc.side, lc.fa, lc.fb, lc.fc, lc.fd);
    }

    public void compute(Vector3 vec, int side) {
        boolean offset = switch (side) {
            case 0 -> vec.y <= 0;
            case 1 -> vec.y >= 1;
            case 2 -> vec.z <= 0;
            case 3 -> vec.z >= 1;
            case 4 -> vec.x <= 0;
            case 5 -> vec.x >= 1;
            default -> false;
        };
        if (!offset) {
            side += 6;
        }
        computeO(vec, side);
    }

    public void computeO(Vector3 vec, int side) {
        Vector3 v1 = Rotation.axes[((side & 0xE) + 3) % 6];
        Vector3 v2 = Rotation.axes[((side & 0xE) + 5) % 6];
        float d1 = (float) vec.scalarProject(v1);
        float d2 = 1 - d1;
        float d3 = (float) vec.scalarProject(v2);
        float d4 = 1 - d3;
        set(side, d2 * d4, d2 * d3, d1 * d4, d1 * d3);
    }

    @Override
    public LC copy() {
        return new LC(side, fa, fb, fc, fd);
    }
}
