package com.yuo.endless.Client.Lib;


public class NormalAttribute extends VertexAttribute<Vector3[]> {
    public static AttributeKey<Vector3[]> attributeKey;

    Vector3[] normalRef;

    static {
        attributeKey = (AttributeKey)new AttributeKey<>("normal", x$0 -> new Vector3[x$0]);
    }

    public NormalAttribute() {
        super((AttributeKey)attributeKey);
    }

    public boolean load(CCRenderState ccrs) {
        this.normalRef = ccrs.model.<Vector3[]>getAttributes((AttributeKey)attributeKey);
        if (ccrs.model.hasAttribute(attributeKey))
            return (this.normalRef != null);
        if (ccrs.model.hasAttribute(SideAttribute.attributeKey)) {
            ccrs.pipeline.addDependency(ccrs.sideAttrib);
            return true;
        }
        throw new IllegalStateException();
    }

    public static Vector3[] axes = new Vector3[] { new Vector3(0.0D, -1.0D, 0.0D), new Vector3(0.0D, 1.0D, 0.0D), new Vector3(0.0D, 0.0D, -1.0D), new Vector3(0.0D, 0.0D, 1.0D), new Vector3(-1.0D, 0.0D, 0.0D), new Vector3(1.0D, 0.0D, 0.0D) };

    public void operate(CCRenderState ccrs) {
        if (this.normalRef != null) {
            ccrs.normal.set(this.normalRef[ccrs.vertexIndex]);
        } else {
            ccrs.normal.set(axes[ccrs.side]);
        }
    }
}
