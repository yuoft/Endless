package com.yuo.endless.Compat.Maid;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Client.Model.InfinityArmorModel;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.Event.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.InventoryMenu;

public class MaidGeoLayer<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerRenderer<T, R> {
    private BedrockModel<Mob> maidModel;
    private final InfinityArmorModel cachedWingModel;
    private final Minecraft mc = Minecraft.getInstance();
    public MaidGeoLayer(R renderer) {
        super(renderer);
        this.cachedWingModel = new InfinityArmorModel(rebuildWings().bakeRoot(), 0);
    }

    private LayerDefinition rebuildWings() {
        MeshDefinition m = new MeshDefinition();
        PartDefinition p = m.getRoot();
        p.addOrReplaceChild("bipedRightWing", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.0F, 2.0F, 0.0F, 1.2566371F, 0.0F));
        p.addOrReplaceChild("bipedLeftWing", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.0F, 2.0F, 0.0F, -1.2566371F, 0.0F));
        return LayerDefinition.create(m, 64, 64);
    }

    @Override
    public GeoLayerRenderer copy(IGeoEntityRenderer iGeoEntityRenderer) {
        return new MaidGeoLayer(iGeoEntityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        if (mc.level == null) {
            return;
        }

        // 计算着色器参数
        float yaw = (float) (entity.getYRot() * 2.0f * Math.PI / 360.0);
        float pitch = -(float) (entity.getXRot() * 2.0f * Math.PI / 360.0);

        AvaritiaShaders.cosmicTime.set((float) (System.currentTimeMillis() - AvaritiaShaders.renderTime) / 2000.0F);
        AvaritiaShaders.cosmicYaw.set(yaw);
        AvaritiaShaders.cosmicPitch.set(pitch);
        AvaritiaShaders.cosmicExternalScale.set(1.0f);
        AvaritiaShaders.cosmicOpacity.set(1.0F);

        for (int i = 0; i < 10; ++i) {
            TextureAtlasSprite sprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(EndlessUtils.fa("shader/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }

        if (AvaritiaShaders.cosmicUVs != null) {
            AvaritiaShaders.cosmicUVs.set(AvaritiaShaders.COSMIC_UVS);
        }
        // 渲染女仆翅膀
        IMaid maid = IMaid.convert(entity);
        if (maid != null) {
            EntityMaid maidEntity = maid.asStrictMaid();
            IGeoEntity geoEntity = this.getGeoEntity(entity);
            ILocationModel geoModel = geoEntity.getGeoModel();
            // 渲染身体星空效果
            poseStack.pushPose();
            poseStack.scale(1.01f, 1.01f, 1.01f);
            poseStack.translate(0,1.2f,0.3f);

            CustomPackLoader.MAID_MODELS.getModel(maidEntity.getModelId()).ifPresent((model) -> {
                this.maidModel = model;
            });
            if (this.maidModel != null && EventHandler.isInfinite(maidEntity)) {
                this.maidModel.renderToBuffer(poseStack, InfinityArmorModel.material(InfinityArmorModel.MASK_INV).buffer(buffer, InfinityArmorModel::mask2), packedLight, 1, 0.84f, 1.0f, 0.95f, 0.8f);
            }
            MaidLayer.renderWing(poseStack, buffer, maidEntity, cachedWingModel, packedLight);
            poseStack.popPose();
        }
    }

}