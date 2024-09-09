package com.yuo.endless.Client.Lib;

public class ColourAttribute extends VertexAttribute<int[]> {
    public static AttributeKey<int[]> attributeKey;

    int[] colourRef;

    static {
        attributeKey = (AttributeKey)new AttributeKey<>("colour", x$0 -> new int[x$0]);
    }

    public ColourAttribute() {
        super((AttributeKey)attributeKey);
    }

    public boolean load(CCRenderState ccrs) {
        this.colourRef = ccrs.model.<int[]>getAttributes((AttributeKey)attributeKey);
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
