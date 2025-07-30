package com.yuo.endless.Client.Lib;

public abstract class Colour implements Copyable<Colour> {

    public byte r;
    public byte g;
    public byte b;
    public byte a;

    public Colour(int r, int g, int b, int a) {
        this.r = (byte) r;
        this.g = (byte) g;
        this.b = (byte) b;
        this.a = (byte) a;
    }

    public Colour(Colour colour) {
        r = colour.r;
        g = colour.g;
        b = colour.b;
        a = colour.a;
    }

    public abstract int pack();

    public Colour add(Colour colour2) {
        a += colour2.a;
        r += colour2.r;
        g += colour2.g;
        b += colour2.b;
        return this;
    }

    public Colour multiply(Colour colour2) {
        a = (byte) ((a & 0xFF) * ((colour2.a & 0xFF) / 255D));
        r = (byte) ((r & 0xFF) * ((colour2.r & 0xFF) / 255D));
        g = (byte) ((g & 0xFF) * ((colour2.g & 0xFF) / 255D));
        b = (byte) ((b & 0xFF) * ((colour2.b & 0xFF) / 255D));
        return this;
    }

    public Colour scale(double d) {
        a = (byte) ((a & 0xFF) * d);
        r = (byte) ((r & 0xFF) * d);
        g = (byte) ((g & 0xFF) * d);
        b = (byte) ((b & 0xFF) * d);
        return this;
    }

    public abstract Colour copy();

    public int rgba() {
        return (r & 0xFF) << 24 | (g & 0xFF) << 16 | (b & 0xFF) << 8 | (a & 0xFF);
    }

    public abstract Colour set(int colour);

    public Colour set(Colour colour) {
        r = colour.r;
        g = colour.g;
        b = colour.b;
        a = colour.a;
        return this;
    }

    public Colour set(double r, double g, double b, double a) {
        return set((int) (255 * r), (int) (255 * g), (int) (255 * b), (int) (255 * a));
    }

    public Colour set(float r, float g, float b, float a) {
        return set((int) (255F * r), (int) (255F * g), (int) (255F * b), (int) (255F * a));
    }

    public Colour set(int r, int g, int b, int a) {
        this.r = (byte) r;
        this.g = (byte) g;
        this.b = (byte) b;
        this.a = (byte) a;
        return this;
    }

    public Colour set(double[] doubles) {
        return set(doubles[0], doubles[1], doubles[2], doubles[3]);
    }

    public Colour set(float[] floats) {
        return set(floats[0], floats[1], floats[2], floats[3]);
    }

    public int r() {
        return r & 0xFF;
    }

    public int g() {
        return g & 0xFF;
    }

    public int b() {
        return b & 0xFF;
    }

    public int a() {
        return a & 0xFF;
    }

    public float[] getRGBA() {
        return new float[] { r / 255F, g / 255F, b / 255F, a / 255F };
    }

    public boolean equals(Colour colour) {
        return colour != null && rgba() == colour.rgba();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Colour colour)) return false;

        if (r != colour.r) return false;
        if (g != colour.g) return false;
        if (b != colour.b) return false;
        return a == colour.a;
    }

    @Override
    public int hashCode() {
        int result = r;
        result = 31 * result + (int) g;
        result = 31 * result + (int) b;
        result = 31 * result + (int) a;
        return result;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[0x" + Integer.toHexString(pack()).toUpperCase() + "]";
    }
}
