package com.yuo.endless.Client.Model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Client.Lib.ColorUtils;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.EndlessUtils;
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
import org.apache.logging.log4j.core.appender.rolling.action.IfAccumulatedFileCount;
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

    public static RenderType mask2(ResourceLocation tex) {
        return RenderType.create("", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 0, CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicShader)).setTextureState(new RenderStateShard.TextureStateShard(tex, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setLightmapState(RenderType.LIGHTMAP).setWriteMaskState(RenderStateShard.COLOR_WRITE).setCullState(RenderType.NO_CULL).createCompositeState(true));
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

    public void renderToBuffer(@NotNull PoseStack pPoseStack, @NotNull VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        InfinityArmorModel model = new InfinityArmorModel(this.rebuildWings().bakeRoot(), 0);
        this.copyBipedAngles(this, this.humanoidModel);
        super.renderToBuffer(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
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

        AvaritiaShaders.cosmicOpacity.set(1.0F);
        if (AvaritiaShaders.inventoryRender) {
            AvaritiaShaders.cosmicExternalScale.set(100.0F);
        } else {
            AvaritiaShaders.cosmicExternalScale.set(1.0F);
            AvaritiaShaders.cosmicYaw.set((float)((double)(this.mc.player.getYRot() * 2.0F) * Math.PI / 360.0));
            AvaritiaShaders.cosmicPitch.set(-((float)((double)(this.mc.player.getXRot() * 2.0F) * Math.PI / 360.0)));
        }

        pPoseStack.pushPose();
        pPoseStack.scale(f, f, f);
        pPoseStack.translate(0.0, this.babyYHeadOffset / 16.0F * f3, -0.029999999329447746);
        this.head.render(pPoseStack, material(MASK).buffer(this.bufferSource, this::mask), pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        if (modelRender && !player) {
            this.hatsOver().forEach((t) -> {
                t.render(pPoseStack, material(MASK_INV).buffer(this.bufferSource, InfinityArmorModel::mask2), pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
            });
        }

        pPoseStack.popPose();
        pPoseStack.pushPose();
        pPoseStack.scale(f2, f2, f2);
        pPoseStack.translate(0.0, this.bodyYOffset / 16.0F * f3, 0.0);
        this.bodyParts().forEach((t) -> t.render(pPoseStack, material(MASK).buffer(this.bufferSource, this::mask), pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha));
        if (modelRender && !player) {
            this.bodyPartsOver().forEach((t) -> t.render(pPoseStack, material(MASK_INV).buffer(this.bufferSource, InfinityArmorModel::mask2), pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha));
        }

        this.bodyParts().forEach((t) -> t.render(pPoseStack, this.vertex(this.glow(this.eyeTex)), pPackedLight, pPackedOverlay, 0.84F, 1.0F, 0.95F, (float) (pulse_mag_sqr * 0.5)));
        pPoseStack.popPose();
        pPoseStack.pushPose();
        this.random.setSeed(time / 3L * 1723609L);
        float[] col = ColorUtils.HSVtoRGB(this.random.nextFloat() * 6.0F, 1.0F, 1.0F);
        pPoseStack.scale(f, f, f);
        pPoseStack.translate(0.0, this.babyYHeadOffset / 16.0F * f3, -0.029999999329447746);
        this.hat.render(pPoseStack, material(MASK).buffer(this.bufferSource, this::mask), pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
        if (modelRender) {
            this.hat.render(pPoseStack, this.vertex(RenderType.create("", DefaultVertexFormat.NEW_ENTITY, Mode.QUADS, 0,
                    CompositeState.builder().setShaderState(RenderType.POSITION_COLOR_TEX_SHADER)
                            .setTextureState(new RenderStateShard.TextureStateShard(this.eyeTex, false, false))
                            .setCullState(RenderType.NO_CULL)
                            .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                            .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)  // 深度测试：小于等于
//                            .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(false, true)) // 不写入深度
                            .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
                            .createCompositeState(true))), pPackedLight, pPackedOverlay, col[0], col[1], col[2], 1.0F);
        }

        pPoseStack.popPose();
        if (playerFlying && !AvaritiaShaders.inventoryRender) {
            pPoseStack.pushPose();
            this.rebuildWings();
            pPoseStack.scale(f2, f2, f2);
            pPoseStack.translate(0.0, this.bodyYOffset / 16.0F * f3, 0.0);
            model.renderToBufferWing(pPoseStack, this.mc.renderBuffers().bufferSource().getBuffer(RenderType.armorCutoutNoCull(this.wingTex)), pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
            model.renderToBufferWing(pPoseStack, material(WING).buffer(this.bufferSource, this::mask), pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
            model.renderToBufferWing(pPoseStack, this.mc.renderBuffers().bufferSource().getBuffer(this.glow(this.wingGlowTex)), pPackedLight, pPackedOverlay, 0.84F, 1.0F, 0.95F, (float)(pulse_mag_sqr * 0.5));
            pPoseStack.popPose();
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
            if (EventHandler.isInfinite(l)) {
                AvaritiaShaders.cosmicOpacity.set(2.0F);
                this.playerParts().forEach((t) -> t.render(pPoseStack, InfinityArmorModel.material(InfinityArmorModel.MASK_INV).buffer(pBuffer, InfinityArmorModel::mask2), pPackedLight, 1, 1.0F, 1.0F, 1.0F, 1.0F));
            }

        }
    }
}
