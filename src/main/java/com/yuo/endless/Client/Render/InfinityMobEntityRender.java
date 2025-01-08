package com.yuo.endless.Client.Render;

import com.yuo.endless.Endless;
import com.yuo.endless.Entity.InfinityMobEntity;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class InfinityMobEntityRender extends AbstractZombieRenderer<InfinityMobEntity, ZombieModel<InfinityMobEntity>> {
    private final ResourceLocation TEXTURE = new ResourceLocation(Endless.MOD_ID, "textures/entity/steve.png");

    public InfinityMobEntityRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ZombieModel<>(renderManagerIn.bakeLayer(ModelLayers.ZOMBIE)),
                new ZombieModel<>(renderManagerIn.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)),
                new ZombieModel<>(renderManagerIn.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)));
        this.addLayer(new MobEyesLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(InfinityMobEntity entity) {
        return TEXTURE;
    }
}