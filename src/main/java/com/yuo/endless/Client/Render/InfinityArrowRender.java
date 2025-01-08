package com.yuo.endless.Client.Render;

import com.yuo.endless.Endless;
import com.yuo.endless.Entity.InfinityArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class InfinityArrowRender extends ArrowRenderer<InfinityArrowEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/entity/infinity_arrow.png");

    public InfinityArrowRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getTextureLocation(InfinityArrowEntity entity) {
        return TEXTURE;
    }
}
