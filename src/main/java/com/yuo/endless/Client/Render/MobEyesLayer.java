package com.yuo.endless.Client.Render;

import com.yuo.endless.Endless;
import com.yuo.endless.Entity.InfinityMobEntity;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class MobEyesLayer<T extends InfinityMobEntity> extends EyesLayer<T, ZombieModel<T>> {
    private static final RenderType RENDER_TYPE = RenderType.eyes(new ResourceLocation(Endless.MOD_ID, "textures/entity/steve_eye.png"));

    public MobEyesLayer(RenderLayerParent<T, ZombieModel<T>> rendererIn) {
        super(rendererIn);
    }

    @Override
    public RenderType renderType() {
        return RENDER_TYPE;
    }
}
