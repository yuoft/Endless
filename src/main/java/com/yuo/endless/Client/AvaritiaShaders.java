package com.yuo.endless.Client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.yuo.endless.Client.Lib.SpriteRegistryHelper;
import com.yuo.endless.Endless;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.shader.IShaderManager;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.client.shader.ShaderLoader;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

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

    public static SpriteRegistryHelper MASK_HELPER = new SpriteRegistryHelper();

    public static TextureAtlasSprite[] MASK_SPRITES_INV = new TextureAtlasSprite[1];

    public static SpriteRegistryHelper MASK_HELPER_INV = new SpriteRegistryHelper();

    public static TextureAtlasSprite[] WING_SPRITES = new TextureAtlasSprite[1];

    public static SpriteRegistryHelper WING_HELPER = new SpriteRegistryHelper();

    public static SpriteRegistryHelper COSMIC_HELPER = new SpriteRegistryHelper();

    public static TextureAtlasSprite[] COSMIC_SPRITES = new TextureAtlasSprite[10];

    public static FloatBuffer cosmicUVs = BufferUtils.createFloatBuffer(40);

    private static final ShaderCallback shaderCallback = shader -> {
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "yaw"), AvaritiaShaders.yaw);
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "pitch"), AvaritiaShaders.pitch);
        ARBShaderObjects.glUniform3fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "lightlevel"), AvaritiaShaders.lightlevel[0], AvaritiaShaders.lightlevel[1], AvaritiaShaders.lightlevel[2]);
        ARBShaderObjects.glUniformMatrix2fvARB(ARBShaderObjects.glGetUniformLocationARB(shader, "cosmicuvs"), false, AvaritiaShaders.cosmicUVs);
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "externalScale"), AvaritiaShaders.scale);
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "opacity"), AvaritiaShaders.cosmicOpacity);
    };

    private static final ShaderCallback shaderCallback2 = shader -> {
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "yaw"), AvaritiaShaders.yaw);
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "pitch"), AvaritiaShaders.pitch);
        ARBShaderObjects.glUniform3fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "lightlevel"), AvaritiaShaders.lightlevel[0], AvaritiaShaders.lightlevel[1], AvaritiaShaders.lightlevel[2]);
        ARBShaderObjects.glUniformMatrix2fvARB(ARBShaderObjects.glGetUniformLocationARB(shader, "cosmicuvs"), false, AvaritiaShaders.cosmicUVs);
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "externalScale"), AvaritiaShaders.scale);
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shader, "opacity"), AvaritiaShaders.cosmicOpacity2);
    };

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(AvaritiaShaders::onRenderTick);
        MinecraftForge.EVENT_BUS.addListener(AvaritiaShaders::clientTick);
        initShaders();
        //添加资源
        COSMIC_HELPER.addIIconRegister(registrar -> {
            for (int i =0; i< 10; i++) {
                int finalI = i;
                registrar.registerSprite(shader("cosmic_" + finalI), e -> COSMIC_SPRITES[finalI] = e);
            }
        });
        MASK_HELPER.addIIconRegister(registrar -> registrar.registerSprite(mask("mask"), e -> MASK_SPRITES[0] = e));
        MASK_HELPER_INV.addIIconRegister(registrar -> registrar.registerSprite(mask("mask_inv"), e -> MASK_SPRITES_INV[0] = e));
        WING_HELPER.addIIconRegister(registrar -> registrar.registerSprite(mask("mask_wings"), e -> WING_SPRITES[0] = e));
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

    /**
     * 注册着色器
     */
    public static void initShaders() {
        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        ISelectiveResourceReloadListener shaderReloadListener; //防止报错  ？？？
        if (resourceManager instanceof IReloadableResourceManager)
            ((IReloadableResourceManager) resourceManager).addReloadListener(shaderReloadListener = (manager, predicate) ->{
                PROGRAMS.values().forEach(ShaderLinkHelper::deleteShader);
                PROGRAMS.clear();
                for (AvaritiaShader shader : AvaritiaShader.values()) {
                    try {
                        ShaderProgram prog = new ShaderProgram(ARBShaderObjects.glCreateProgramObjectARB(),
                                createShader(manager, shader.v, ShaderLoader.ShaderType.VERTEX),
                                createShader(manager, shader.f, ShaderLoader.ShaderType.FRAGMENT));
                        ShaderLinkHelper.linkProgram(prog);
                        PROGRAMS.put(shader, prog);
                    } catch (IOException ignored) {
                    }
                }
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
