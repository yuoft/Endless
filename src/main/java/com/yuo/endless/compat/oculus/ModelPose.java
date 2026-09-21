package com.yuo.endless.compat.oculus;

import net.minecraft.client.model.geom.ModelPart;

import java.util.List;
import java.util.stream.Stream;

final class ModelPose {
    private final List<PartState> parts;

    private ModelPose(List<PartState> parts) {
        this.parts = parts;
    }

    static ModelPose capture(ModelPart... roots) {
        return new ModelPose(Stream.of(roots).flatMap(ModelPart::getAllParts).distinct().map(PartState::capture).toList());
    }

    void render(Runnable draw) {
        List<PartState> previous = parts.stream().map(p -> PartState.capture(p.part())).toList();
        try { parts.forEach(PartState::apply); draw.run();
        }
        finally { previous.forEach(PartState::apply);
        }
    }

    private record PartState(ModelPart part, float x, float y, float z, float xRot, float yRot, float zRot, float xScale, float yScale, float zScale, boolean visible, boolean skipDraw) {
        static PartState capture(ModelPart p) {
            return new PartState(p,p.x,p.y,p.z,p.xRot,p.yRot,p.zRot,p.xScale,p.yScale,p.zScale,p.visible,p.skipDraw);
        }

        void apply() {
            part.x=x; part.y=y; part.z=z; part.xRot=xRot; part.yRot=yRot; part.zRot=zRot;
            part.xScale=xScale; part.yScale=yScale; part.zScale=zScale; part.visible=visible; part.skipDraw=skipDraw;
        }
    }
}
