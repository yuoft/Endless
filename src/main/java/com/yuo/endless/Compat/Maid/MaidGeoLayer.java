package com.yuo.endless.Compat.Maid;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.ILocationModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Client.Model.InfinityArmorModel;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.Event.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.InventoryMenu;

public class MaidGeoLayer<T extends Mob, R extends IGeoEntityRenderer<T>> extends GeoLayerRenderer<T, R> {
    private BedrockModel<Mob> maidModel;
    private final InfinityArmorModel cachedWingModel;
    private final Minecraft mc = Minecraft.getInstance();
    private final GeckoEntityMaidRenderer<T> geoEntityRenderer;
    public MaidGeoLayer(R renderer) {
        super(renderer);
        this.geoEntityRenderer = (GeckoEntityMaidRenderer<T>) renderer;
        this.cachedWingModel = new InfinityArmorModel(MaidLayer.rebuildWings().bakeRoot(), 0);
    }

    @Override
    public GeoLayerRenderer<T, R> copy(IGeoEntityRenderer iGeoEntityRenderer) {
        return new MaidGeoLayer(iGeoEntityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        if (mc.level == null) {
            return;
        }

        MaidLayer.updateShaderParams(entity);

        // 渲染女仆翅膀
        IMaid maid = IMaid.convert(entity);
        if (maid != null) {
            EntityMaid maidEntity = maid.asStrictMaid();
            if (maidEntity == null) return;

            IGeoEntity geoEntity = this.getGeoEntity(entity);
            if (geoEntity instanceof GeckoMaidEntity<?> geckoMaid){
                AnimatedGeoModel currentModel = geckoMaid.getCurrentModel();
                AnimatedGeoModel currentModel1 = this.geoEntityRenderer.getAnimatableEntity(entity).getCurrentModel();
                VertexConsumer consumer = InfinityArmorModel.material(InfinityArmorModel.MASK_INV).buffer(buffer, InfinityArmorModel::mask2);

                // 渲染身体星空效果
                poseStack.pushPose();
                poseStack.scale(1.01f, 1.01f, 1.01f);
                poseStack.translate(0,1.2f,0.3f);

                if (currentModel1 != null && EventHandler.isInfinite(maidEntity)){
                    this.geoEntityRenderer.render(currentModel1, entity, partialTick, AvaritiaShaders.COSMIC_RENDER_TYPE, poseStack, buffer, consumer, packedLight, 1, 0.84f, 1.0f, 0.95f, 0.8f);
                    MaidLayer.renderWing(poseStack, buffer, maidEntity, cachedWingModel, packedLight);
                }
                poseStack.popPose();
            }
        }
    }

}