package com.yuo.endless.Client.Lib;

public class SideAttribute extends VertexAttribute<int[]> {
    public static AttributeKey<int[]> attributeKey;

    int[] sideRef;

    static {
        attributeKey = new AttributeKey<>("side", int[]::new);
    }

    public SideAttribute() {
        super(attributeKey);
    }

    public boolean load(CCRenderState ccrs) {
        this.sideRef = ccrs.model.getAttributes(attributeKey);
        if (ccrs.model.hasAttribute(attributeKey))
            return (this.sideRef != null);
        ccrs.pipeline.addDependency(ccrs.normalAttrib);
        return true;
    }

    public static int findSide(Vector3 normal) {
        if (normal.y <= -0.99D)
            return 0;
        if (normal.y >= 0.99D)
            return 1;
        if (normal.z <= -0.99D)
            return 2;
        if (normal.z >= 0.99D)
            return 3;
        if (normal.x <= -0.99D)
            return 4;
        if (normal.x >= 0.99D)
            return 5;
        return -1;
    }

    public void operate(CCRenderState c) {
        if (this.sideRef != null) {
            c.side = this.sideRef[c.vertexIndex];
        } else {
            c.side = findSide(c.normal);
        }
    }
}
