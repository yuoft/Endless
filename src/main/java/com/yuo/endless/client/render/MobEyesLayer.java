package com.yuo.endless.client.render;

import com.yuo.endless.entity.InfinityMobEntity;
import com.yuo.endless.EndlessUtils;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;

public class MobEyesLayer<T extends InfinityMobEntity> extends EyesLayer<T, ZombieModel<T>> {
    private static final RenderType RENDER_TYPE = RenderType.eyes(EndlessUtils.fa("textures/entity/steve_eye.png"));

    public MobEyesLayer(RenderLayerParent<T, ZombieModel<T>> rendererIn) {
        super(rendererIn);
    }

    @Override
    public RenderType renderType() {
        return RENDER_TYPE;
    }
}
