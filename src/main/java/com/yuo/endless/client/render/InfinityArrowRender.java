package com.yuo.endless.client.render;

import com.yuo.endless.entity.InfinityArrowEntity;
import com.yuo.endless.EndlessUtils;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class InfinityArrowRender extends ArrowRenderer<InfinityArrowEntity> {
    private static final ResourceLocation TEXTURE = EndlessUtils.fa("textures/entity/infinity_arrow.png");

    public InfinityArrowRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getTextureLocation(InfinityArrowEntity entity) {
        return TEXTURE;
    }
}
