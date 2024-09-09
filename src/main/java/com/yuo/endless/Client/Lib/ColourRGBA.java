package com.yuo.endless.Client.Lib;

public class ColourRGBA extends Colour {
    public ColourRGBA(int colour) {
        super(colour >> 24 & 0xFF, colour >> 16 & 0xFF, colour >> 8 & 0xFF, colour & 0xFF);
    }

    public ColourRGBA(double r, double g, double b, double a) {
        super((int)(255.0D * r), (int)(255.0D * g), (int)(255.0D * b), (int)(255.0D * a));
    }

    public ColourRGBA(ColourRGBA colour) {
        super(colour);
    }

    public int pack() {
        return pack(this);
    }

    public Colour copy() {
        return new ColourRGBA(this);
    }

    public Colour set(int colour) {
        return set(new ColourRGBA(colour));
    }

    public static int pack(Colour colour) {
        return (colour.r & 0xFF) << 24 | (colour.g & 0xFF) << 16 | (colour.b & 0xFF) << 8 | colour.a & 0xFF;
    }

    public static int multiply(int c1, int c2) {
        if (c1 == -1)
            return c2;
        if (c2 == -1)
            return c1;
        int r = ((c1 >>> 24) * (c2 >>> 24) & 0xFF00) << 16;
        int g = ((c1 >> 16 & 0xFF) * (c2 >> 16 & 0xFF) & 0xFF00) << 8;
        int b = (c1 >> 8 & 0xFF) * (c2 >> 8 & 0xFF) & 0xFF00;
        int a = (c1 & 0xFF) * (c2 & 0xFF) >> 8;
        return r | g | b | a;
    }
}

