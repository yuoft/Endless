package com.yuo.endless.Client.Lib;

public abstract class Colour implements Copyable<Colour> {
    public byte r;

    public byte g;

    public byte b;

    public byte a;

    public Colour(int r, int g, int b, int a) {
        this.r = (byte)r;
        this.g = (byte)g;
        this.b = (byte)b;
        this.a = (byte)a;
    }

    public Colour(Colour colour) {
        this.r = colour.r;
        this.g = colour.g;
        this.b = colour.b;
        this.a = colour.a;
    }

    public String toString() {
        return getClass().getSimpleName() + "[0x" + Integer.toHexString(pack()).toUpperCase() + "]";
    }

    public Colour add(Colour colour2) {
        this.a = (byte)(this.a + colour2.a);
        this.r = (byte)(this.r + colour2.r);
        this.g = (byte)(this.g + colour2.g);
        this.b = (byte)(this.b + colour2.b);
        return this;
    }

    public Colour multiply(Colour colour2) {
        this.a = (byte)(int)((this.a & 0xFF) * (colour2.a & 0xFF) / 255.0D);
        this.r = (byte)(int)((this.r & 0xFF) * (colour2.r & 0xFF) / 255.0D);
        this.g = (byte)(int)((this.g & 0xFF) * (colour2.g & 0xFF) / 255.0D);
        this.b = (byte)(int)((this.b & 0xFF) * (colour2.b & 0xFF) / 255.0D);
        return this;
    }

    public Colour scale(double d) {
        this.a = (byte)(int)((this.a & 0xFF) * d);
        this.r = (byte)(int)((this.r & 0xFF) * d);
        this.g = (byte)(int)((this.g & 0xFF) * d);
        this.b = (byte)(int)((this.b & 0xFF) * d);
        return this;
    }

    public int rgba() {
        return (this.r & 0xFF) << 24 | (this.g & 0xFF) << 16 | (this.b & 0xFF) << 8 | this.a & 0xFF;
    }

    public Colour set(Colour colour) {
        this.r = colour.r;
        this.g = colour.g;
        this.b = colour.b;
        this.a = colour.a;
        return this;
    }

    public Colour set(double r, double g, double b, double a) {
        return set((int)(255.0D * r), (int)(255.0D * g), (int)(255.0D * b), (int)(255.0D * a));
    }

    public Colour set(int r, int g, int b, int a) {
        this.r = (byte)r;
        this.g = (byte)g;
        this.b = (byte)b;
        this.a = (byte)a;
        return this;
    }

    public float[] getRGBA() {
        return new float[] { this.r / 255.0F, this.g / 255.0F, this.b / 255.0F, this.a / 255.0F };
    }

    public boolean equals(Colour colour) {
        return (colour != null && rgba() == colour.rgba());
    }

    public abstract int pack();

    public abstract Colour copy();

    public abstract Colour set(int paramInt);
}