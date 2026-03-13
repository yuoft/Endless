package com.yuo.endless.Client.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Entity.InfinityMobEntity;
import com.yuo.endless.EndlessUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public class InfinityMobEntityRender extends AbstractZombieRenderer<InfinityMobEntity, ZombieModel<InfinityMobEntity>> {
    private final ResourceLocation TEXTURE = EndlessUtils.fa("textures/entity/steve.png");

    public InfinityMobEntityRender(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ZombieModel<>(renderManagerIn.bakeLayer(ModelLayers.ZOMBIE)),
                new ZombieModel<>(renderManagerIn.bakeLayer(ModelLayers.ZOMBIE_INNER_ARMOR)),
                new ZombieModel<>(renderManagerIn.bakeLayer(ModelLayers.ZOMBIE_OUTER_ARMOR)));
        this.addLayer(new MobEyesLayer<>(this));
        // 添加星空渲染层
        this.addLayer(new MobLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(InfinityMobEntity entity) {
        return TEXTURE;
    }
}