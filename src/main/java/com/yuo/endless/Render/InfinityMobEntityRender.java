package com.yuo.endless.Render;

import com.yuo.endless.Endless;
import com.yuo.endless.Entity.InfinityMobEntity;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.util.ResourceLocation;

public class InfinityMobEntityRender extends AbstractZombieRenderer<InfinityMobEntity, ZombieModel<InfinityMobEntity>> {
    private final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/entity/steve.png");

    public InfinityMobEntityRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ZombieModel<>(0.0F, false), new ZombieModel<>(0.5F, true), new ZombieModel<>(1.0F, true));
        this.addLayer(new MobEyesLayer<>(this));
    }

    @Override
    public ResourceLocation getEntityTexture(InfinityMobEntity entity) {
        return TEXTURE;
    }
}