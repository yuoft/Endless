package com.yuo.endless.Client.Render;

import com.yuo.endless.Endless;
import com.yuo.endless.Entity.InfinityMobEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.util.ResourceLocation;

public class MobEyesLayer<T extends InfinityMobEntity> extends AbstractEyesLayer<T, ZombieModel<T>> {
    private static final RenderType RENDER_TYPE = RenderType.getEyes(new ResourceLocation(Endless.MOD_ID, "textures/entity/steve_eye.png"));

    public MobEyesLayer(IEntityRenderer<T, ZombieModel<T>> rendererIn) {
        super(rendererIn);
    }

    public RenderType getRenderType() {
        return RENDER_TYPE;
    }
}
