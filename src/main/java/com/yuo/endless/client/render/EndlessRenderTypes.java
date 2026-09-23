package com.yuo.endless.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.client.AvaritiaShaders;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class EndlessRenderTypes extends RenderType {
    public static final ResourceLocation RES_VOID = EndlessUtils.fa("textures/entity/void.png");
    public static final ResourceLocation RES_VOID_HALO = EndlessUtils.fa("textures/entity/void_halo.png");

    public static final ResourceLocation RES_MAGIC_CIRCLE0 = EndlessUtils.fa("textures/entity/magic_circle0.png");
    public static final ResourceLocation RES_MAGIC_CIRCLE1 = EndlessUtils.fa("textures/entity/magic_circle1.png");
    public static final ResourceLocation RES_MAGIC_CIRCLE2 = EndlessUtils.fa("textures/entity/magic_circle2.png");
    public static final ResourceLocation RES_MAGIC_CIRCLE3 = EndlessUtils.fa("textures/entity/magic_circle3.png");
    public static final ResourceLocation[] RES_MAGIC_CIRCLE = new ResourceLocation[] {
            RES_MAGIC_CIRCLE0, RES_MAGIC_CIRCLE1, RES_MAGIC_CIRCLE2, RES_MAGIC_CIRCLE3
    };

    public static RenderType COSMIC_RENDER_TYPE = create("endless:cosmic",
            DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false,
            CompositeState.builder().setShaderState(new ShaderStateShard(() -> AvaritiaShaders.cosmicShader))
                    .setDepthTestState(EQUAL_DEPTH_TEST).setLightmapState(LIGHTMAP).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setTextureState(BLOCK_SHEET_MIPPED).createCompositeState(true)
    );

    public static final RenderType COSMIC_BLOCK_RENDER_TYPE = create("endless:cosmic_block",
            DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false,
            CompositeState.builder().setShaderState(new ShaderStateShard(() -> AvaritiaShaders.cosmicShader))
                    .setDepthTestState(LEQUAL_DEPTH_TEST).setLightmapState(LIGHTMAP).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setTextureState(BLOCK_SHEET_MIPPED).createCompositeState(true));

    public static final RenderType VOID_HALO = create("endless:void_halo",
            DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256,
            CompositeState.builder().setShaderState(POSITION_COLOR_TEX_SHADER)
                    .setTextureState(new TextureStateShard(RES_VOID_HALO, false, false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
    public static final RenderType VOID_HEMISPHERE = create("endless:void_hemisphere",
            DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 256,
            CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_SHADOW_SHADER)
                    .setTextureState(new TextureStateShard(RES_VOID, false, false))
                    .setCullState(NO_CULL).createCompositeState(false));

    public EndlessRenderTypes(String s, VertexFormat vertexFormat, Mode mode, int i, boolean b, boolean b1, Runnable runnable, Runnable runnable1) {
        super(s, vertexFormat, mode, i, b, b1, runnable, runnable1);
    }

    public static RenderType mask2(ResourceLocation tex) {
        return create("",
                DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 0,
                CompositeState.builder().setShaderState(new ShaderStateShard(() -> AvaritiaShaders.cosmicShader))
                        .setTextureState(new TextureStateShard(tex, false, false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY).setLightmapState(LIGHTMAP).setWriteMaskState(COLOR_WRITE)
                        .setCullState(NO_CULL).createCompositeState(true));
    }

    public static RenderType getEyeMask(ResourceLocation tex) {
        return create("", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 0,
                CompositeState.builder().setShaderState(POSITION_COLOR_TEX_SHADER)
                        .setTextureState(new TextureStateShard(tex, false, false)).setCullState(NO_CULL)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(LEQUAL_DEPTH_TEST)  // 深度测试：小于等于
//                            .setWriteMaskState(new WriteMaskStateShard(false, true)) // 不写入深度
                        .setLayeringState(VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
    }

    public static RenderType getMagicCircle(ResourceLocation res) {
        return create("endless:magic_circle",
                DefaultVertexFormat.POSITION_COLOR_TEX, Mode.QUADS, 256, true, false,
                CompositeState.builder().setShaderState(POSITION_COLOR_TEX_SHADER)
                        .setTextureState(new TextureStateShard(res, false, false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).createCompositeState(true));
    }
}
