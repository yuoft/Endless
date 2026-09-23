package com.yuo.endless.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.client.AvaritiaShaders;
import com.yuo.endless.client.lib.ColorUtils;
import com.yuo.endless.client.render.EndlessRenderTypes;
import com.yuo.endless.compat.oculus.CosmicArmorLateRenderQueue;
import com.yuo.endless.compat.oculus.OculusCompat;
import com.yuo.endless.compat.oculus.RenderFrameState;
import com.yuo.endless.event.EventHandler;
import com.yuo.endless.items.EndlessItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderType.CompositeState;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class InfinityArmorModel extends HumanoidModel<Player> {
    public static ResourceLocation MASK = EndlessUtils.fa("models/infinity_armor_mask");
    public static ResourceLocation MASK_INV = EndlessUtils.fa("models/infinity_armor_mask_inv");
    public static ResourceLocation WING = EndlessUtils.fa("models/infinity_armor_mask_wings");
    private static boolean modelRender; //全套特效
    private static boolean playerFlying; //飞行
    private static boolean player; //
    private final ResourceLocation eyeTex = EndlessUtils.fa("textures/models/infinity_armor_eyes.png");
    private final ResourceLocation wingTex = EndlessUtils.fa("textures/models/infinity_armor_wing.png");
    private final ResourceLocation wingGlowTex = EndlessUtils.fa("textures/models/infinity_armor_wingglow.png");
    private final Minecraft mc = Minecraft.getInstance();
    private final MultiBufferSource bufferSource;
    private final Random random;
    private final HumanoidModel<Player> humanoidModel;

    public InfinityArmorModel(ModelPart pRoot, int x) {
        super(createMesh(new CubeDeformation(1.0F), 0.0F).getRoot().bake(64, 64));
        this.bufferSource = this.mc.renderBuffers().bufferSource();
        this.random = new Random();
        this.humanoidModel = new HumanoidModel<>(createMesh(new CubeDeformation(0.0F), 0.0F).getRoot().bake(64, 64));
    }

    public InfinityArmorModel(ModelPart pRoot) {
        super(pRoot);
        this.bufferSource = this.mc.renderBuffers().bufferSource();
        this.random = new Random();
        this.humanoidModel = new HumanoidModel<>(createMesh(new CubeDeformation(0.0F), 0.0F).getRoot().bake(64, 64));
    }

    public static MeshDefinition createMesh(CubeDeformation deformation, float f, boolean islegs) {
        int legoffset = islegs ? 32 : 0;
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition p = meshDefinition.getRoot();
        p.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation), PartPose.offset(0.0F, 0.0F + f, 0.0F));
        p.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        p.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation), PartPose.offset(0.0F, 0.0F + f, 0.0F));
        p.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(-5.0F, 2.0F + f, 0.0F));
        p.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(5.0F, 2.0F + f, 0.0F));
        p.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(-1.9F, 12.0F + f, 0.0F));
        p.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, deformation), PartPose.offset(1.9F, 12.0F + f, 0.0F));
        if (islegs) {
            p.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16 + legoffset).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
            p.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16 + legoffset).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
            p.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16 + legoffset).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(1.9F, 12.0F, 0.0F));
        }

        return meshDefinition;
    }

    public static Material material(ResourceLocation t) {
        return new Material(InventoryMenu.BLOCK_ATLAS, t);
    }

    private RenderType glow(ResourceLocation tex) {
        return RenderType.create("", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 0, CompositeState.builder().setShaderState(RenderType.POSITION_COLOR_TEX_LIGHTMAP_SHADER).setTextureState(new RenderStateShard.TextureStateShard(tex, false, false)).setTransparencyState(RenderType.LIGHTNING_TRANSPARENCY).setCullState(RenderType.NO_CULL).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
    }

    private RenderType mask(ResourceLocation tex) {
        return RenderType.create("", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 0, CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicShader)).setTextureState(new RenderStateShard.TextureStateShard(tex, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setLightmapState(RenderType.LIGHTMAP).setWriteMaskState(RenderStateShard.COLOR_WRITE).setCullState(RenderType.NO_CULL).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
    }

    private LayerDefinition rebuildWings() {
        MeshDefinition m = new MeshDefinition();
        PartDefinition p = m.getRoot();
        p.addOrReplaceChild("bipedRightWing", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.0F, 2.0F, 0.0F, 1.2566371F, 0.0F));
        p.addOrReplaceChild("bipedLeftWing", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.0F, 2.0F, 0.0F, -1.2566371F, 0.0F));
        return LayerDefinition.create(m, 64, 64);
    }

    public void renderToBufferWing(@NotNull PoseStack pPoseStack, @NotNull VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        ModelPart h = this.rebuildWings().bakeRoot();
        ModelPart bipedRightWing = h.getChild("bipedRightWing");
        ModelPart bipedLeftWing = h.getChild("bipedLeftWing");
        bipedRightWing.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        bipedLeftWing.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }

    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
        if (OculusCompat.isShaderPackActive()){
            if (RenderFrameState.isShadowPass()) {
                super.renderToBuffer(poseStack, consumer, light, overlay, red, green, blue, alpha);
                return;
            }
            if (!RenderFrameState.shouldDeferWorldEffect()) {
                return;
            }
        }

        InfinityArmorModel model = new InfinityArmorModel(this.rebuildWings().bakeRoot(), 0);
        this.copyBipedAngles(this, this.humanoidModel);
        super.renderToBuffer(poseStack, consumer, light, overlay, red, green, blue, alpha);
        long time = this.mc.player.level().getGameTime();
        double pulse = Math.sin((double)time / 10.0) * 0.5 + 0.5;
        double pulse_mag_sqr = pulse * pulse * pulse * pulse * pulse * pulse;
        float f;
        float f2;
        float f3;
        if (this.young) {
            f = 1.5F / this.babyHeadScale;
            f2 = 1.0F / this.babyBodyScale;
            f3 = 1.0F;
        } else {
            f = 1.0F;
            f2 = 0.9F;
            f3 = 0.0F;
        }

        AvaritiaShaders.cosmicOpacity.set(4.0F);
        if (AvaritiaShaders.inventoryRender) {
            AvaritiaShaders.cosmicExternalScale.set(100.0F);
        } else {
            AvaritiaShaders.cosmicExternalScale.set(1.0F);
            AvaritiaShaders.cosmicYaw.set((float)((double)(this.mc.player.getYRot() * 2.0F) * Math.PI / 360.0));
            AvaritiaShaders.cosmicPitch.set(-((float)((double)(this.mc.player.getXRot() * 2.0F) * Math.PI / 360.0)));
        }

        poseStack.pushPose();
        poseStack.scale(f, f, f);
        poseStack.translate(0.0, this.babyYHeadOffset / 16.0F * f3, -0.029999999329447746);
        if (OculusCompat.isShaderPackActive())
            CosmicArmorLateRenderQueue.enqueuePart(poseStack, this.head, MASK, light, overlay, red, green, blue, alpha);
        else this.head.render(poseStack, material(MASK).buffer(this.bufferSource, this::mask), light, overlay, red, green, blue, alpha);
        if (modelRender && !player) {
            this.hatsOver().forEach((modelPart) -> {
                if (OculusCompat.isShaderPackActive())
                    CosmicArmorLateRenderQueue.enqueuePart(poseStack, modelPart, MASK, light, overlay, red, green, blue, alpha);
                else modelPart.render(poseStack, material(MASK_INV).buffer(this.bufferSource, EndlessRenderTypes::mask2), light, overlay, red, green, blue, alpha);
            });
        }

        poseStack.popPose();
        poseStack.pushPose();
        poseStack.scale(f2, f2, f2);
        poseStack.translate(0.0, this.bodyYOffset / 16.0F * f3, 0.0);
        this.bodyParts().forEach((modelPart) -> {
            if (OculusCompat.isShaderPackActive()) {
                CosmicArmorLateRenderQueue.enqueuePart(poseStack, modelPart, MASK, light, overlay, red, green, blue, alpha);
                CosmicArmorLateRenderQueue.enqueueEyePart(poseStack, modelPart,CosmicArmorLateRenderQueue.EyeType.BODY_GLOW, light, overlay);
            }
            else {
                modelPart.render(poseStack, material(MASK).buffer(this.bufferSource, this::mask), light, overlay, red, green, blue, alpha);
                modelPart.render(poseStack, this.vertex(this.glow(this.eyeTex)), light, overlay, 0.84F, 1.0F, 0.95F, (float) (pulse_mag_sqr * 0.5));
            }
        });
        if (modelRender && !player) {
            this.bodyPartsOver().forEach((modelPart) -> {
                if (OculusCompat.isShaderPackActive())
                    CosmicArmorLateRenderQueue.enqueuePart(poseStack, modelPart, MASK_INV, light, overlay, red, green, blue, alpha);
                else modelPart.render(poseStack, material(MASK_INV).buffer(this.bufferSource, EndlessRenderTypes::mask2), light, overlay, red, green, blue, alpha);
            });
        }

        poseStack.popPose();
        poseStack.pushPose();
        this.random.setSeed(time / 3L * 1723609L);
        float[] col = ColorUtils.HSVtoRGB(this.random.nextFloat() * 6.0F, 1.0F, 1.0F);
        poseStack.scale(f, f, f);
        poseStack.translate(0.0, this.babyYHeadOffset / 16.0F * f3, -0.029999999329447746);
        if (OculusCompat.isShaderPackActive())
            CosmicArmorLateRenderQueue.enqueuePart(poseStack, this.hat, MASK, light, overlay, red, green, blue, alpha);
        else this.hat.render(poseStack, material(MASK).buffer(this.bufferSource, this::mask), light, overlay, red, green, blue, alpha);
        if (modelRender) {
            if (OculusCompat.isShaderPackActive())
                CosmicArmorLateRenderQueue.enqueueEyePart(poseStack, this.hat, CosmicArmorLateRenderQueue.EyeType.HAT_RAINBOW, light, overlay);
            else this.hat.render(poseStack, this.vertex(EndlessRenderTypes.getEyeMask(this.eyeTex)), light, overlay, col[0], col[1], col[2], 1.0F);
        }

        poseStack.popPose();
        if (playerFlying && !AvaritiaShaders.inventoryRender) {
            poseStack.pushPose();
            this.rebuildWings();
            poseStack.scale(f2, f2, f2);
            poseStack.translate(0.0, this.bodyYOffset / 16.0F * f3, 0.0);
            model.renderToBufferWing(poseStack, this.mc.renderBuffers().bufferSource().getBuffer(RenderType.armorCutoutNoCull(this.wingTex)), light, overlay, red, green, blue, alpha);
            if (OculusCompat.isShaderPackActive()){
                CosmicArmorLateRenderQueue.enqueueWing(this.mc.player, poseStack, this, WING, light, overlay, red, green, blue, alpha);
            }else {
                model.renderToBufferWing(poseStack, material(WING).buffer(this.bufferSource, this::mask), light, overlay, red, green, blue, alpha);
            }
            model.renderToBufferWing(poseStack, this.mc.renderBuffers().bufferSource().getBuffer(this.glow(this.wingGlowTex)), light, overlay, 0.84F, 1.0F, 0.95F, (float)(pulse_mag_sqr * 0.5));
            poseStack.popPose();
        }

    }

    public void update(LivingEntity e) {
        modelRender = false;
        playerFlying = false;
        player = false;
        ItemStack hats = e.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = e.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack leg = e.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack foot = e.getItemBySlot(EquipmentSlot.FEET);
        boolean hasHat = hats.getItem() == EndlessItems.infinityHead.get();
        boolean hasChest = chest.getItem() == EndlessItems.infinityChest.get();
        boolean hasLeg = leg.getItem() == EndlessItems.infinityLegs.get();
        boolean hasFoot = foot.getItem() == EndlessItems.infinityFeet.get();
        if (hasHat && hasChest && hasLeg && hasFoot) {
            modelRender = true;
        }

        if (e instanceof Player) {
            player = true;
            if (hasChest && ((Player)e).getAbilities().flying) {
                playerFlying = true;
            }
        }

        this.crouching = e.isCrouching();
        this.young = e.isBaby();
        this.riding = e.isPassenger();
    }

    public VertexConsumer vertex(RenderType t) {
        return this.bufferSource.getBuffer(t);
    }

    public @NotNull Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg);
    }

    public Iterable<ModelPart> hatsOver() {
        return ImmutableList.of(this.humanoidModel.hat, this.humanoidModel.head);
    }

    public Iterable<ModelPart> bodyPartsOver() {
        return ImmutableList.of(this.humanoidModel.body, this.humanoidModel.rightArm, this.humanoidModel.leftArm, this.humanoidModel.rightLeg, this.humanoidModel.leftLeg);
    }

    private void copyPartAngles(ModelPart from, ModelPart to) {
        to.xRot = from.xRot;
        to.yRot = from.yRot;
        to.zRot = from.zRot;
        to.x = from.x;
        to.y = from.y;
        to.z = from.z;
    }

    private void copyBipedAngles(HumanoidModel<Player> from, HumanoidModel<Player> to) {
        this.copyPartAngles(from.head, to.head);
        this.copyPartAngles(from.hat, to.hat);
        this.copyPartAngles(from.body, to.body);
        this.copyPartAngles(from.leftArm, to.leftArm);
        this.copyPartAngles(from.leftLeg, to.leftLeg);
        this.copyPartAngles(from.rightArm, to.rightArm);
        this.copyPartAngles(from.rightLeg, to.rightLeg);
    }

    public static class PlayerRender extends RenderLayer<Player, PlayerModel<Player>> {
        public PlayerRender(RenderLayerParent<Player, PlayerModel<Player>> t) {
            super(t);
        }

        public Iterable<ModelPart> playerParts() {
            return ImmutableList.of(this.getParentModel().head, this.getParentModel().hat, this.getParentModel().body, this.getParentModel().leftArm, (this.getParentModel()).rightArm, (this.getParentModel()).leftLeg, (this.getParentModel()).rightLeg);
        }

        public void render(@NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, @NotNull Player l, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            if (OculusCompat.isShaderPackActive()){
                if (RenderFrameState.isShadowPass()) {
                    return;
                }

                if (!RenderFrameState.shouldDeferWorldEffect()) {
                    return;
                }
            }
            if (EventHandler.isInfinite(l)) {
                AvaritiaShaders.cosmicOpacity.set(4.0F);
                if (OculusCompat.isShaderPackActive())
                    CosmicArmorLateRenderQueue.enqueuePlayerLayer(pPoseStack, this.getParentModel(), pPackedLight);
                else this.playerParts().forEach((modelPart) -> modelPart.render(pPoseStack, InfinityArmorModel.material(InfinityArmorModel.MASK_INV).buffer(pBuffer, EndlessRenderTypes::mask2), pPackedLight, 1, 1.0F, 1.0F, 1.0F, 1.0F));
            }
        }
    }
}
