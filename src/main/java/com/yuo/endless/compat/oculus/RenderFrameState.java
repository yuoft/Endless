package com.yuo.endless.compat.oculus;

public final class RenderFrameState {
    private static final Snapshot OUTSIDE = new Snapshot(  false, false, false, false, false, null, Integer.MIN_VALUE);
    private static Snapshot current = OUTSIDE;

    public static Snapshot beginFrame() {
        boolean oculusLoaded = OculusCompat.isOculusLoaded();
        current = new Snapshot(
                true,
                oculusLoaded,
                OculusCompat.isOculusEmbeddiumActive(),
                OculusCompat.isShaderPackActive(),
                false,
                null,
                Integer.MIN_VALUE
        );
        return current;
    }

    public static boolean isShadowPass() {
        return current.worldRenderActive()
                && current.oculusLoaded()
                && OculusCompat.isShadowPass();
    }

    public static void endFrame() {
        current = OUTSIDE;
    }

    public static void clear() {
        current = OUTSIDE;
    }

    public static Snapshot current() {
        return current;
    }

    public static Snapshot capturePipelineAfterWorldRender() {
        if (!current.worldRenderActive() || !current.oculusLoaded()) {
            return current;
        }

        OculusCompat.PipelineSnapshot pipeline = OculusCompat.capturePipeline();
        current = new Snapshot(
                true,
                current.oculusLoaded(),
                current.oculusEmbeddiumActive(),
                current.shaderPackActive(),
                pipeline.ready(),
                pipeline.identity(),
                pipeline.version()
        );
        return current;
    }

    public static boolean isWorldRenderActive() {
        return current.worldRenderActive();
    }

    public static boolean shaderPackActiveForRender() {
        return current.worldRenderActive()
                ? current.shaderPackActive()
                : OculusCompat.isShaderPackActive();
    }

    public static boolean shouldDeferWorldEffect() {
        return current.worldRenderActive() && current.shaderPackActive() && !isShadowPass();
    }

    public record Snapshot(
            boolean worldRenderActive,
            boolean oculusLoaded,
            boolean oculusEmbeddiumActive,
            boolean shaderPackActive,
            boolean pipelineReady,
            Object pipelineIdentity,
            int pipelineVersion
    ) {}

    private RenderFrameState() {}
}