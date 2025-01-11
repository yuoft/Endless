package com.yuo.endless.Client.Lib;

public class ColourAttribute extends VertexAttribute<int[]> {
    public static AttributeKey<int[]> attributeKey;

    int[] colourRef;

    static {
        attributeKey = new AttributeKey<>("colour", int[]::new);
    }

    public ColourAttribute() {
        super(attributeKey);
    }

    public boolean load(CCRenderState ccrs) {
        this.colourRef = ccrs.model.getAttributes(attributeKey);
        return (this.colourRef != null || !ccrs.model.hasAttribute(attributeKey));
    }

    public void operate(CCRenderState ccrs) {
        if (this.colourRef != null) {
            ccrs.colour = ColourRGBA.multiply(ccrs.baseColour, this.colourRef[ccrs.vertexIndex]);
        } else {
            ccrs.colour = ccrs.baseColour;
        }
    }
}
