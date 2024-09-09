package com.yuo.endless.Client.Model;

import com.mojang.blaze3d.platform.GlStateManager;
import com.yuo.endless.Client.Lib.SpriteRegistryHelper;
import com.yuo.endless.Endless;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.shader.IShaderManager;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.client.shader.ShaderLoader;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener.IStage;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class AvaritiaShaders {

    public static float yaw;

    public static float pitch;

    public static float scale = 0.0F;

    public static boolean inventoryRender = false;

    public static float[] lightlevel = new float[3];

    public static float cosmicOpacity;

    public static float cosmicOpacity2;

    static Map<AvaritiaShader, ShaderProgram> PROGRAMS = new EnumMap<>(AvaritiaShader.class);

    public static int renderTime = 0;

    public static TextureAtlasSprite[] MASK_SPRITES = new TextureAtlasSprite[1];

    public static TextureAtlasSprite MASK;

    public static SpriteRegistryHelper MASK_HELPER = new SpriteRegistryHelper();

    public static TextureAtlasSprite[] MASK_SPRITES_INV = new TextureAtlasSprite[1];

    public static TextureAtlasSprite MASK_INV;

    public static SpriteRegistryHelper MASK_HELPER_INV = new SpriteRegistryHelper();

    public static TextureAtlasSprite[] WING_SPRITES = new TextureAtlasSprite[1];

    public static TextureAtlasSprite WING;

    public static SpriteRegistryHelper WING_HELPER = new SpriteRegistryHelper();

    public static SpriteRegistryHelper COSMIC_HELPER = new SpriteRegistryHelper();

    public static TextureAtlasSprite[] COSMIC_SPRITES = new TextureAtlasSprite[10];

    public static TextureAtlasSprite COSMIC_0;

    public static TextureAtlasSprite COSMIC_1;

    public static TextureAtlasSprite COSMIC_2;

    public static TextureAtlasSprite COSMIC_3;

    public static TextureAtlasSprite COSMIC_4;

    public static TextureAtlasSprite COSMIC_5;

    public static TextureAtlasSprite COSMIC_6;

    public static TextureAtlasSprite COSMIC_7;

    public static TextureAtlasSprite COSMIC_8;

    public static TextureAtlasSprite COSMIC_9;

    public static FloatBuffer cosmicUVs = BufferUtils.createFloatBuffer(40);

    public static ShaderCallback shaderCallback = new ShaderCallback() {
        public void c(int shader) {
            ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "yaw"), AvaritiaShaders.yaw);
            ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "pitch"), AvaritiaShaders.pitch);
            ARBShaderObjects.glUniform3fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "lightlevel"), AvaritiaShaders.lightlevel[0], AvaritiaShaders.lightlevel[1], AvaritiaShaders.lightlevel[2]);
            ARBShaderObjects.glUniformMatrix2fvARB(ARBShaderObjects.glGetUniformLocationARB(shader, "cosmicuvs"), false, AvaritiaShaders.cosmicUVs);
            ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "externalScale"), AvaritiaShaders.scale);
            ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "opacity"), AvaritiaShaders.cosmicOpacity);
        }
    };

    public static ShaderCallback shaderCallback2 = new ShaderCallback() {
        public void c(int shader) {
            ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "yaw"), AvaritiaShaders.yaw);
            ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "pitch"), AvaritiaShaders.pitch);
            ARBShaderObjects.glUniform3fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "lightlevel"), AvaritiaShaders.lightlevel[0], AvaritiaShaders.lightlevel[1], AvaritiaShaders.lightlevel[2]);
            ARBShaderObjects.glUniformMatrix2fvARB(ARBShaderObjects.glGetUniformLocationARB(shader, "cosmicuvs"), false, AvaritiaShaders.cosmicUVs);
            ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "externalScale"), AvaritiaShaders.scale);
            ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "opacity"), AvaritiaShaders.cosmicOpacity2);
        }
    };

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(AvaritiaShaders::onRenderTick);
        MinecraftForge.EVENT_BUS.addListener(AvaritiaShaders::clientTick);
        initShaders();
        TextureAtlasSprite[] s = COSMIC_SPRITES;
        Consumer<TextureAtlasSprite> spriteConsumer = sprite -> {
            // 在这里处理你的精灵，比如打印其宽度和高度
            System.out.println("Registered sprite with width: " + sprite.getWidth() + ", height: " + sprite.getHeight());
        };
        //没有添加进去
        COSMIC_HELPER.addIIconRegister(registrar -> {
            registrar.registerSprite(shader("cosmic_0"), spriteConsumer);
            registrar.registerSprite(shader("cosmic_1"), spriteConsumer);
            registrar.registerSprite(shader("cosmic_2"), spriteConsumer);
            registrar.registerSprite(shader("cosmic_3"), spriteConsumer);
            registrar.registerSprite(shader("cosmic_4"), spriteConsumer);
            registrar.registerSprite(shader("cosmic_5"), spriteConsumer);
            registrar.registerSprite(shader("cosmic_6"), spriteConsumer);
            registrar.registerSprite(shader("cosmic_7"), spriteConsumer);
            registrar.registerSprite(shader("cosmic_8"), spriteConsumer);
            registrar.registerSprite(shader("cosmic_9"), spriteConsumer);
        });
        MASK_HELPER.addIIconRegister(registrar -> registrar.registerSprite(mask("mask"), e -> {}));
        MASK_HELPER_INV.addIIconRegister(registrar -> registrar.registerSprite(mask("mask_inv"), e -> {}));
        WING_HELPER.addIIconRegister(registrar -> registrar.registerSprite(mask("mask_wings"), e -> {}));
    }

    static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            cosmicUVs = BufferUtils.createFloatBuffer(4 * COSMIC_SPRITES.length);
            for (TextureAtlasSprite cosmicIcon : COSMIC_SPRITES) {
                cosmicUVs.put(cosmicIcon.getMinU());
                cosmicUVs.put(cosmicIcon.getMinV());
                cosmicUVs.put(cosmicIcon.getMaxU());
                cosmicUVs.put(cosmicIcon.getMaxV());
            }
            cosmicUVs.flip();
        }
    }

    static void clientTick(TickEvent.ClientTickEvent event) {
        if (!Minecraft.getInstance().isGamePaused() && event.phase == TickEvent.Phase.END)
            if (renderTime < 65535) {
                renderTime++;
            } else {
                renderTime /= 65535;
            }
    }

    static ResourceLocation shader(String path) {
        return new ResourceLocation(Endless.MOD_ID, "shader/" + path);
    }

    static ResourceLocation mask(String path) {
        return new ResourceLocation(Endless.MOD_ID, "models/infinity_armor_" + path);
    }

    public static void useShader() {
        useShader(AvaritiaShader.cosmicShader, shaderCallback);
    }

    public static void useShader2() {
        useShader(AvaritiaShader.cosmicShader2, shaderCallback2);
    }

    enum AvaritiaShader {
        cosmicShader("cosmic.vert", "cosmic.frag"),
        cosmicShader2("cosmic.vert", "cosmic.frag");

        final String v;

        final String f;

        AvaritiaShader(String v, String f) {
            this.v = v;
            this.f = f;
        }
    }

    public static void initShaders() {
        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager)
            ((IReloadableResourceManager) resourceManager).addReloadListener((IStage var1, IResourceManager var2, IProfiler var3, IProfiler var4, Executor var5, Executor var6) -> {
                PROGRAMS.values().forEach(ShaderLinkHelper::deleteShader);
                PROGRAMS.clear();
                for (AvaritiaShader shader : AvaritiaShader.values()) {
                    try {
                        ShaderProgram prog = new ShaderProgram(ARBShaderObjects.glCreateProgramObjectARB(),
                                createShader(var2, shader.v, ShaderLoader.ShaderType.VERTEX),
                                createShader(var2, shader.f, ShaderLoader.ShaderType.FRAGMENT));
                        ShaderLinkHelper.linkProgram(prog);
                        PROGRAMS.put(shader, prog);
                    } catch (IOException ignored) {
                    }
                }
                return new CompletableFuture<>();
            });
    }

    static ShaderLoader createShader(IResourceManager manager, String filename, ShaderLoader.ShaderType shaderType) throws IOException {
        ResourceLocation l = new ResourceLocation(Endless.MOD_ID, "shaders/" + filename);
        return ShaderLoader.func_216534_a(shaderType, l.toString(), new BufferedInputStream(manager.getResource(l).getInputStream()), shaderType.name().toLowerCase(Locale.ROOT));
    }

    static void useShader(AvaritiaShader shader, ShaderCallback c) {
        ShaderProgram prog = PROGRAMS.get(shader);
        if (prog == null)
            return;
        int id = prog.getProgram();
        ARBShaderObjects.glUseProgramObjectARB(id);
        GlStateManager.disableDepthTest();
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.world != null)
            ARBShaderObjects.glUniform1iARB(ARBShaderObjects.glGetUniformLocationARB(id, "time"), renderTime);
        if (c != null)
            c.c(id);
    }

    public static void releaseShader(IRenderTypeBuffer t) {
        shading(t);
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    public static void shading(IRenderTypeBuffer t) {
        if (t instanceof IRenderTypeBuffer.Impl)
            ((IRenderTypeBuffer.Impl)t).finish();
    }

    static class ShaderProgram implements IShaderManager {
        int i;

        ShaderLoader v;

        ShaderLoader f;

        ShaderProgram(int i, ShaderLoader v, ShaderLoader f) {
            this.i = i;
            this.v = v;
            this.f = f;
        }

        public int getProgram() {
            return this.i;
        }

        public void markDirty() {}

        public ShaderLoader getVertexShaderLoader() {
            return this.v;
        }

        public ShaderLoader getFragmentShaderLoader() {
            return this.f;
        }
    }

    interface ShaderCallback {
        void c(int param1Int);
    }
}
