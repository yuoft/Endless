package com.yuo.endless.compat.oculus;

import net.minecraftforge.fml.ModList;

import java.lang.reflect.Method;

/**
 * @author xiaoyang
 */
public final class OculusCompat {
    public static final boolean OCULUS_LOADED = ModList.get().isLoaded("oculus");
    public static final boolean EMBEDDIUM_LOADED = ModList.get().isLoaded("embeddium");
    private static boolean initialized;
    private static Method shadowStateMethod;
    private static volatile boolean shaderPackReflectionInitialized;
    private static Method isShaderPackInUse;
    private static Object irisApiInstance;

    private static boolean pipelineReflectionInitialized;
    private static Method getPipelineManager;
    private static Method getPipelineNullable;
    private static Method getPipelineVersion;

    public static boolean isOculusLoaded() {
        return OCULUS_LOADED;
    }

    public static boolean isOculusEmbeddiumActive() {
        return OCULUS_LOADED && EMBEDDIUM_LOADED;
    }

    /**
     * 是否加载光影
     */
    public static boolean isShaderPackActive() {
        if (!OCULUS_LOADED) return false;
        initShaderPackReflection();
        if (irisApiInstance == null || isShaderPackInUse == null) return false;
        try {
            return Boolean.TRUE.equals(isShaderPackInUse.invoke(irisApiInstance));
        } catch (ReflectiveOperationException | LinkageError e) {
            return false;
        }
    }

    private static void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        try {
            Class<?> clazz = Class.forName(
                    "net.irisshaders.iris.shadows.ShadowRenderingState",
                    false,
                    OculusCompat.class.getClassLoader()
            );
            shadowStateMethod = clazz.getDeclaredMethod(
                    "areShadowsCurrentlyBeingRendered"
            );
            shadowStateMethod.setAccessible(true);
        } catch (Throwable ignored) {
            shadowStateMethod = null;
        }
    }

    private static void initShaderPackReflection() {
        if (shaderPackReflectionInitialized) return;
        synchronized (OculusCompat.class) {
            if (shaderPackReflectionInitialized) return;
            try {
                Class<?> api = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
                Method getInstance = api.getMethod("getInstance");
                irisApiInstance = getInstance.invoke(null);
                isShaderPackInUse = api.getMethod("isShaderPackInUse");
            } catch (ReflectiveOperationException | LinkageError e) {
                irisApiInstance = null;
                isShaderPackInUse = null;
            } finally {
                shaderPackReflectionInitialized = true;
            }
        }
    }

    public static boolean isShadowPass() {
        initialize();
        if (shadowStateMethod == null) {
            return false;
        }
        try {
            return (Boolean) shadowStateMethod.invoke(null);
        } catch (Throwable ignored) {
            return false;
        }
    }

    public record PipelineSnapshot(boolean ready, Object identity, int version) {
        static final PipelineSnapshot NOT_READY =
                new PipelineSnapshot(false, null, Integer.MIN_VALUE);
    }

    public static PipelineSnapshot capturePipeline() {
        if (!OCULUS_LOADED) return PipelineSnapshot.NOT_READY;
        initPipelineReflection();
        if (getPipelineManager == null) return PipelineSnapshot.NOT_READY;
        try {
            Object manager = getPipelineManager.invoke(null);
            if (manager == null) return PipelineSnapshot.NOT_READY;
            Object pipeline = getPipelineNullable.invoke(manager);
            if (pipeline == null) return PipelineSnapshot.NOT_READY;
            Object version = getPipelineVersion.invoke(manager);
            return new PipelineSnapshot(true, pipeline, ((Number) version).intValue());
        } catch (ReflectiveOperationException | LinkageError | ClassCastException e) {
            return PipelineSnapshot.NOT_READY;
        }
    }

    private static synchronized void initPipelineReflection() {
        if (pipelineReflectionInitialized) return;
        pipelineReflectionInitialized = true;
        try {
            Class<?> iris = Class.forName("net.irisshaders.iris.Iris");
            Class<?> pm   = Class.forName("net.irisshaders.iris.pipeline.PipelineManager");
            getPipelineManager  = iris.getMethod("getPipelineManager");
            getPipelineNullable = pm.getMethod("getPipelineNullable");
            getPipelineVersion  = pm.getMethod("getVersionCounterForSodiumShaderReload");
        } catch (ReflectiveOperationException | LinkageError e) {
            getPipelineManager  = null;
            getPipelineNullable = null;
            getPipelineVersion  = null;
        }
    }

    private OculusCompat() {}
}