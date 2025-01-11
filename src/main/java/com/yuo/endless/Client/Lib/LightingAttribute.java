package com.yuo.endless.Client.Lib;

public class LightingAttribute extends VertexAttribute<int[]> {
    public static AttributeKey<int[]> attributeKey;

    int[] colourRef;

    static {
        attributeKey = new AttributeKey<>("lighting", int[]::new);
    }

    public LightingAttribute() {
        super(attributeKey);
    }

    public boolean load(CCRenderState ccrs) {
        if (!ccrs.computeLighting || !ccrs.cFmt.hasColor || !ccrs.model.hasAttribute(attributeKey))
            return false;
        this.colourRef = ccrs.model.getAttributes(attributeKey);
        if (this.colourRef != null) {
            ccrs.pipeline.addDependency(ccrs.colourAttrib);
            return true;
        }
        return false;
    }

    public void operate(CCRenderState ccrs) {
        ccrs.colour = ColourRGBA.multiply(ccrs.colour, this.colourRef[ccrs.vertexIndex]);
    }
}

