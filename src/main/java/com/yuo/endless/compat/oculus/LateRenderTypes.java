package com.yuo.endless.compat.oculus;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.yuo.endless.client.AvaritiaShaders;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.function.Supplier;

public final class LateRenderTypes extends RenderStateShard {

    private static final LayeringStateShard OVERLAY_BIAS = new LayeringStateShard("late_overlay_bias", () -> {
        RenderSystem.polygonOffset(-1.0F, -16.0F);
        RenderSystem.enablePolygonOffset();
    }, () -> {
        RenderSystem.polygonOffset(0.0F, 0.0F);
        RenderSystem.disablePolygonOffset();
    });

    public static final RenderType COSMIC_ITEM = cosmic("late_cosmic_item", DefaultVertexFormat.BLOCK,
            InventoryMenu.BLOCK_ATLAS, () -> AvaritiaShaders.cosmicShader);
    public static final RenderType COSMIC_ENTITY = cosmic("late_cosmic_entity", DefaultVertexFormat.NEW_ENTITY,
            InventoryMenu.BLOCK_ATLAS, () -> AvaritiaShaders.cosmicShader);
    public static final RenderType COSMIC_BLOCK = cosmicBlock("late_cosmic_block", DefaultVertexFormat.BLOCK,
            InventoryMenu.BLOCK_ATLAS, () -> AvaritiaShaders.cosmicShader);

    private static RenderType cosmic(String name, VertexFormat format, ResourceLocation texture, Supplier<ShaderInstance> shader) {
        return RenderType.create(name, format, VertexFormat.Mode.QUADS, 2097152, true, false,
                RenderType.CompositeState.builder().setShaderState(new ShaderStateShard(shader))
                        .setDepthTestState(LEQUAL_DEPTH_TEST).setLayeringState(OVERLAY_BIAS)
                        .setLightmapState(LIGHTMAP).setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setTextureState(new TextureStateShard(texture, false, false)).setCullState(NO_CULL)
                        .setWriteMaskState(COLOR_WRITE).setOutputState(MAIN_TARGET).createCompositeState(false));
    }
    private static RenderType cosmicBlock(String name, VertexFormat format, ResourceLocation texture,
                                          Supplier<ShaderInstance> shader) {
        return RenderType.create(name, format, VertexFormat.Mode.QUADS, 2097152, true, false,
                RenderType.CompositeState.builder()
                        .setShaderState(new ShaderStateShard(shader))
                        .setDepthTestState(NO_DEPTH_TEST)          // ← 关键
                        .setLayeringState(OVERLAY_BIAS)
                        .setLightmapState(LIGHTMAP)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setCullState(NO_CULL)
                        .setWriteMaskState(COLOR_WRITE)
                        .setOutputState(MAIN_TARGET)
                        .createCompositeState(false));
    }

    private LateRenderTypes() { super("endless_late_types", () -> {}, () -> {}); }
}
