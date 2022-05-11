package com.yuo.endless.Render;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class InfinityArrowRender extends ArrowRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/projectiles/arrow.png");

    public InfinityArrowRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(Entity entity) {
        return TEXTURE;
    }
}
