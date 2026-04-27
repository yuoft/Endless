package com.yuo.endless.Compat.Maid;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resource.models.MaidModels;
import com.github.tartaricacid.touhoulittlemaid.compat.patpat.PatPatCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.ysm.YsmCompat;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Client.Model.InfinityArmorModel;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.Event.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderType.CompositeState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class MaidLayer extends RenderLayer<Mob, BedrockModel<Mob>> {
    private static final ResourceLocation wingTex = EndlessUtils.fa("textures/models/infinity_armor_wing.png");
    private static final ResourceLocation wingGlowTex = EndlessUtils.fa("textures/models/infinity_armor_wingglow.png");
    private BedrockModel<Mob> maidModel;
    private final InfinityArmorModel cachedWingModel;
    private final Minecraft mc = Minecraft.getInstance();

    public MaidLayer(LivingEntityRenderer<Mob, BedrockModel<Mob>> renderer, Context manager) {
        super(renderer);
        cachedWingModel = new InfinityArmorModel(rebuildWings().bakeRoot(), 0);
    }

    public static LayerDefinition rebuildWings() {
        MeshDefinition m = new MeshDefinition();
        PartDefinition p = m.getRoot();
        p.addOrReplaceChild("bipedRightWing", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.0F, 2.0F, 0.0F, 1.2566371F, 0.0F));
        p.addOrReplaceChild("bipedLeftWing", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.0F, 2.0F, 0.0F, -1.2566371F, 0.0F));
        return LayerDefinition.create(m, 64, 64);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, Mob entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        if (mc.level == null) {
            return;
        }

        updateShaderParams(entity);

        // 渲染女仆翅膀
        IMaid maid = IMaid.convert(entity);
        if (maid != null) {
            EntityMaid maidEntity = maid.asStrictMaid();
            if (maidEntity == null) return;

            // 渲染身体星空效果
            poseStack.pushPose();
            poseStack.scale(1.0f, 1.0f, 1.0f);
            poseStack.translate(0,0.50f,-0.25f);
            CustomPackLoader.MAID_MODELS.getModel(maidEntity.getModelId()).ifPresent((model) -> {
                this.maidModel = model;
            });
            if (this.maidModel != null && EventHandler.isInfinite(maidEntity)) {
                this.getParentModel().renderToBuffer(poseStack, InfinityArmorModel.material(InfinityArmorModel.MASK_INV).buffer(buffer, InfinityArmorModel::mask2), packedLight, 1, 0.84f, 1.0f, 0.95f, 0.8f);
//                this.maidModel.renderToBuffer(poseStack, InfinityArmorModel.material(InfinityArmorModel.MASK_INV).buffer(buffer, InfinityArmorModel::mask2), packedLight, 1, 0.84f, 1.0f, 0.95f, 0.8f);
                renderWing(poseStack, buffer, maidEntity, cachedWingModel, packedLight);
            }
            poseStack.popPose();
        }
    }

    public static RenderType glow(ResourceLocation tex) {
        return RenderType.create("glow", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 0, CompositeState.builder().setShaderState(RenderType.POSITION_COLOR_TEX_LIGHTMAP_SHADER).setTextureState(new TextureStateShard(tex, false, false)).setTransparencyState(RenderType.LIGHTNING_TRANSPARENCY).setCullState(RenderType.NO_CULL).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
    }

    public static RenderType mask(ResourceLocation tex) {
        return RenderType.create("mask", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 0, CompositeState.builder().setShaderState(new ShaderStateShard(() -> AvaritiaShaders.cosmicShader)).setTextureState(new TextureStateShard(tex, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setLightmapState(RenderType.LIGHTMAP).setWriteMaskState(RenderStateShard.COLOR_WRITE).setCullState(RenderType.NO_CULL).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
    }


    public static void renderWing(PoseStack poseStack, MultiBufferSource buffer, EntityMaid entityMaid, InfinityArmorModel model, int packedLight){
        float f3 = entityMaid.getEyeHeight() * 0.5f;
        float f2 = entityMaid.getEyeHeight() * 0.5f;
        float r = 0.84f;
        float g = 1.0f;
        float b = 0.95f;
        float a = 0.5f;

        poseStack.pushPose();
        poseStack.scale(f2, f2, f2);
        poseStack.translate(0.0, f3, 0.0);
        model.renderToBufferWing(poseStack, buffer.getBuffer(RenderType.armorCutoutNoCull(wingTex)), packedLight, OverlayTexture.NO_OVERLAY, r, g, b, a);
        model.renderToBufferWing(poseStack, InfinityArmorModel.material(InfinityArmorModel.WING).buffer(buffer, MaidLayer::mask), packedLight, OverlayTexture.NO_OVERLAY,  r, g, b, a);
        model.renderToBufferWing(poseStack, buffer.getBuffer(MaidLayer.glow(wingGlowTex)), packedLight, OverlayTexture.NO_OVERLAY,  r, g, b, a);
        poseStack.popPose();
    }

    public static void updateShaderParams(Mob entity) {
        float yaw = (float) (entity.getYRot() * 2.0f * Math.PI / 360.0);
        float pitch = -(float) (entity.getXRot() * 2.0f * Math.PI / 360.0);

        AvaritiaShaders.cosmicTime.set((float) (System.currentTimeMillis() - AvaritiaShaders.renderTime) / 2000.0F);
        AvaritiaShaders.cosmicYaw.set(yaw);
        AvaritiaShaders.cosmicPitch.set(pitch);

        if (AvaritiaShaders.inventoryRender) {
            AvaritiaShaders.cosmicExternalScale.set(100.0f);
        } else {
            AvaritiaShaders.cosmicExternalScale.set(1.0f);
        }
        AvaritiaShaders.cosmicOpacity.set(1.0F);

        for (int i = 0; i < 10; ++i) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                    .apply(EndlessUtils.fa("shader/cosmic_" + i));
            if (sprite != null) {
                AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
                AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
                AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
                AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
            }
        }

        if (AvaritiaShaders.cosmicUVs != null) {
            AvaritiaShaders.cosmicUVs.set(AvaritiaShaders.COSMIC_UVS);
        }
    }
}