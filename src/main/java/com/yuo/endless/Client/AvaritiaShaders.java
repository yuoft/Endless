package com.yuo.endless.Client;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.yuo.endless.Client.Lib.CCShaderInstance;
import com.yuo.endless.Client.Lib.SpriteRegistryHelper;
import com.yuo.endless.Endless;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.IOException;
import java.util.Objects;

/**
 * 星空渲染 参考Avaritia Universal模组  和 龙之研究
 * Date: 2025/1/10
 */
@OnlyIn(Dist.CLIENT)
public class AvaritiaShaders {

    public static boolean inventoryRender = false;

    static int renderTime;

    static float renderFrame;

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

    public static float[] COSMIC_UVS = new float[40];

    public static CCShaderInstance cosmicShader;

    public static CCShaderInstance cosmicShader2;

    public static Uniform cosmicTime;

    public static Uniform cosmicYaw;

    public static Uniform cosmicPitch;

    public static Uniform cosmicExternalScale;

    public static Uniform cosmicOpacity;

    public static Uniform cosmicUVs;

    public static Uniform cosmicTime2;

    public static Uniform cosmicYaw2;

    public static Uniform cosmicPitch2;

    public static Uniform cosmicExternalScale2;

    public static Uniform cosmicOpacity2;

    public static Uniform cosmicUVs2;

    public static void init() {
        IEventBus eventbus = FMLJavaModLoadingContext.get().getModEventBus();
        eventbus.addListener(AvaritiaShaders::onRegisterShaders);
        MinecraftForge.EVENT_BUS.addListener(AvaritiaShaders::onRenderTick);
        MinecraftForge.EVENT_BUS.addListener(AvaritiaShaders::clientTick);
        MinecraftForge.EVENT_BUS.addListener(AvaritiaShaders::renderTick);
        TextureAtlasSprite[] s = COSMIC_SPRITES;
//        COSMIC_HELPER.addIIconRegister(registrar -> {
//            registrar.registerSprite(shader("cosmic_0"), () ->{});
//            registrar.registerSprite(shader("cosmic_1"), ());
//            registrar.registerSprite(shader("cosmic_2"), ());
//            registrar.registerSprite(shader("cosmic_3"), ());
//            registrar.registerSprite(shader("cosmic_4"), ());
//            registrar.registerSprite(shader("cosmic_5"), ());
//            registrar.registerSprite(shader("cosmic_6"), ());
//            registrar.registerSprite(shader("cosmic_7"), ());
//            registrar.registerSprite(shader("cosmic_8"), ());
//            registrar.registerSprite(shader("cosmic_9"), ());
//        });
//        MASK_HELPER.addIIconRegister(registrar -> registrar.registerSprite(mask("mask"), ()));
//        MASK_HELPER_INV.addIIconRegister(registrar -> registrar.registerSprite(mask("mask_inv"), ()));
//        WING_HELPER.addIIconRegister(registrar -> registrar.registerSprite(mask("mask_wings"), ()));
        //添加资源 龙之研究
        COSMIC_HELPER.addIIconRegister(registrar -> {
            for (int i =0; i< 10; i++) {
                int finalI = i;
                registrar.registerSprite(shader("cosmic_" + finalI), e -> COSMIC_SPRITES[finalI] = e);
            }
        });
        MASK_HELPER.addIIconRegister(registrar -> registrar.registerSprite(mask("mask"), e -> MASK_SPRITES[0] = e));
        MASK_HELPER_INV.addIIconRegister(registrar -> registrar.registerSprite(mask("mask_inv"), e -> MASK_SPRITES_INV[0] = e));
        WING_HELPER.addIIconRegister(registrar -> registrar.registerSprite(mask("mask_wings"), e -> WING_SPRITES[0] = e));
        MASK = MASK_SPRITES[0];
        MASK_INV = MASK_SPRITES_INV[0];
        WING = WING_SPRITES[0];
    }

    static ResourceLocation shader(String path) {
        return new ResourceLocation(Endless.MOD_ID, "shader/" + path);
    }

    static ResourceLocation mask(String path) {
        return new ResourceLocation(Endless.MOD_ID, "models/infinity_armor_" + path);
    }

    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (int i = 0; i < COSMIC_SPRITES.length; i++) {
                TextureAtlasSprite sprite = COSMIC_SPRITES[i];
                COSMIC_UVS[i * 4 + 0] = sprite.getU0();
                COSMIC_UVS[i * 4 + 1] = sprite.getV0();
                COSMIC_UVS[i * 4 + 2] = sprite.getU1();
                COSMIC_UVS[i * 4 + 3] = sprite.getV1();
            }
            if (cosmicUVs != null)
                cosmicUVs.set(COSMIC_UVS);
            if (cosmicUVs2 != null)
                cosmicUVs2.set(COSMIC_UVS);
        }
    }

    /**
     * 注册着色器
     */
    public static void onRegisterShaders(RegisterShadersEvent event) {
        ResourceManager resourceManager = event.getResourceManager();
        try {
            event.registerShader(new CCShaderInstance(resourceManager, new ResourceLocation(Endless.MOD_ID, "cosmic"), DefaultVertexFormat.BLOCK), e -> {
                cosmicShader = (CCShaderInstance)e;
                cosmicTime = Objects.requireNonNull(cosmicShader.getUniform("time"));
                cosmicYaw = Objects.requireNonNull(cosmicShader.getUniform("yaw"));
                cosmicPitch = Objects.requireNonNull(cosmicShader.getUniform("pitch"));
                cosmicExternalScale = Objects.requireNonNull(cosmicShader.getUniform("externalScale"));
                cosmicOpacity = Objects.requireNonNull(cosmicShader.getUniform("opacity"));
                cosmicUVs = Objects.requireNonNull(cosmicShader.getUniform("cosmicuvs"));
                cosmicShader.onApply(() -> {
                });
            });
        } catch (IOException iOException) {
            throw new RuntimeException("endless shader error", iOException);
        }
        try {
            event.registerShader(new CCShaderInstance(resourceManager, new ResourceLocation(Endless.MOD_ID, "cosmic"), DefaultVertexFormat.NEW_ENTITY), e -> {
                cosmicShader2 = (CCShaderInstance) e;
                cosmicTime2 = Objects.requireNonNull(cosmicShader2.getUniform("time"));
                cosmicYaw2 = Objects.requireNonNull(cosmicShader2.getUniform("yaw"));
                cosmicPitch2 = Objects.requireNonNull(cosmicShader2.getUniform("pitch"));
                cosmicExternalScale2 = Objects.requireNonNull(cosmicShader2.getUniform("externalScale"));
                cosmicOpacity2 = Objects.requireNonNull(cosmicShader2.getUniform("opacity"));
                cosmicUVs2 = Objects.requireNonNull(cosmicShader2.getUniform("cosmicuvs"));
                cosmicShader2.onApply(() -> {
                });
            });
        } catch (IOException iOException) {
            throw new RuntimeException("endless shader error", iOException);
        }
    }

    static void clientTick(TickEvent.ClientTickEvent event) {
        if (!Minecraft.getInstance().isPaused() && event.phase == TickEvent.Phase.END)
            renderTime++;
    }

    static void renderTick(TickEvent.RenderTickEvent event) {
        if (!Minecraft.getInstance().isPaused() && event.phase == TickEvent.Phase.START)
            renderFrame = event.renderTickTime;
    }
}
