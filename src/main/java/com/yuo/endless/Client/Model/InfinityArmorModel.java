package com.yuo.endless.Client.Model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class InfinityArmorModel extends HumanoidModel {
    String s = "endless:textures/models/";

    ResourceLocation eyeTex = new ResourceLocation(this.s + "infinity_armor_eyes.png");

    ResourceLocation wingTex = new ResourceLocation(this.s + "infinity_armor_wing.png");

    ResourceLocation wingGlowTex = new ResourceLocation(this.s + "infinity_armor_wingglow.png");

    static boolean invulnRender;

    public static boolean useMenu = false;

    static boolean playerFlying;

    static boolean player;

    static boolean legs = true;

    Minecraft mc = Minecraft.getInstance();

    MultiBufferSource buf = this.mc.renderBuffers().bufferSource();

    Random randy = new Random();

    HumanoidModel invuln = new HumanoidModel(createMesh(new CubeDeformation(0.0F), 0.0F).getRoot().bake(64, 64));

    ModelPart bipedLeftWing;

    ModelPart bipedRightWing;

    RenderType glow(ResourceLocation tex) {
        return RenderType.create("", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 0, RenderType.CompositeState.builder().setShaderState(RenderType.POSITION_COLOR_TEX_LIGHTMAP_SHADER).setTextureState(new RenderStateShard.TextureStateShard(tex, false, false)).setTransparencyState(RenderType.LIGHTNING_TRANSPARENCY).setCullState(RenderType.NO_CULL).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
    }

    RenderType mask(ResourceLocation tex) {
        return RenderType.create("", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 0, RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicShader)).setTextureState(new RenderStateShard.TextureStateShard(tex, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setLightmapState(RenderType.LIGHTMAP).setWriteMaskState(RenderStateShard.COLOR_WRITE).setCullState(RenderType.NO_CULL).setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
    }

    static RenderType mask2(ResourceLocation tex) {
        return RenderType.create("", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 0, RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicShader2)).setTextureState(new RenderStateShard.TextureStateShard(tex, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setLightmapState(RenderType.LIGHTMAP).setWriteMaskState(RenderStateShard.COLOR_WRITE).setCullState(RenderType.NO_CULL).createCompositeState(true));
    }

    RenderType mask3(ResourceLocation tex) {
        return RenderType.create("", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 0, RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicShader)).setTextureState(new RenderStateShard.TextureStateShard(tex, false, false)).setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setLightmapState(RenderType.LIGHTMAP).setWriteMaskState(RenderStateShard.COLOR_WRITE).setCullState(RenderType.NO_CULL).createCompositeState(true));
    }

    public InfinityArmorModel(ModelPart r, int x) {
        super(createMesh(new CubeDeformation(1.0F), 0.0F).getRoot().bake(64, 64));
    }

    public InfinityArmorModel(ModelPart r) {
        super(r);
    }

    public static MeshDefinition createMeshes(CubeDeformation cube, float f, boolean islegs) {
        legs = islegs;
        int heightoffset = 0;
        int legoffset = islegs ? 32 : 0;
        MeshDefinition m = new MeshDefinition();
        PartDefinition p = m.getRoot();
        p.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cube), PartPose.offset(0.0F, 0.0F + f, 0.0F));
        p.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cube.extend(-0.5f)), PartPose.offset(0.0F, 0.0F + f, 0.0F));
        p.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, cube), PartPose.offset(0.0F, 0.0F + f, 0.0F));
        p.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cube), PartPose.offset(-5.0F, 2.0F + f, 0.0F));
        p.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cube), PartPose.offset(5.0F, 2.0F + f, 0.0F));
        p.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cube), PartPose.offset(-1.9F, 12.0F + f, 0.0F));
        p.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cube), PartPose.offset(1.9F, 12.0F + f, 0.0F));
        if (islegs) {
            p.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16 + legoffset).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, (0 + heightoffset), 0.0F));
            p.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16 + legoffset).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-1.9F, (12 + heightoffset), 0.0F));
            p.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16 + legoffset).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(1.9F, (12 + heightoffset), 0.0F));
        }
        return m;
    }

    public LayerDefinition rebuildWings() {
        MeshDefinition m = new MeshDefinition();
        PartDefinition p = m.getRoot();
        p.addOrReplaceChild("bipedRightWing", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 0.0F, 2.0F, 0.0F, 1.2566371F, 0.0F));
        p.addOrReplaceChild("bipedLeftWing", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 0.0F, 2.0F, 0.0F, -1.2566371F, 0.0F));
        return LayerDefinition.create(m, 64, 64);
    }

    void renderToBufferWing(PoseStack m, VertexConsumer v, int l, int o, float r, float g, float b, float a) {
        ModelPart h = rebuildWings().bakeRoot();
        this.bipedRightWing = h.getChild("bipedRightWing");
        this.bipedLeftWing = h.getChild("bipedLeftWing");
        this.bipedRightWing.render(m, v, l, o, r, g, b, a);
        this.bipedLeftWing.render(m, v, l, o, r, g, b, a);
    }

    public void renderToBuffer(PoseStack mStack, VertexConsumer vertexBuilder, int i1, int i2, float i3, float i4, float i5, float i6) {
        float f, f1, f2;
        InfinityArmorModel model = new InfinityArmorModel(rebuildWings().bakeRoot(), 0);
        copyBipedAngles(this, this.invuln);
        super.renderToBuffer(mStack, vertexBuilder, i1, i2, i3, i4, i5, i6);
        long time = 0;
        if (this.mc.player != null) {
            time = this.mc.player.level.getGameTime();
        }
        double pulse = Math.sin(time / 10.0D) * 0.5D + 0.5D;
        double pulse_mag_sqr = pulse * pulse * pulse * pulse * pulse * pulse;
        if (this.young) {
            f = 1.5F / this.babyHeadScale;
            f1 = 1.0F / this.babyBodyScale;
            f2 = 1.0F;
        } else {
            f = 1.0F;
            f1 = 1.0F;
            f2 = 0.0F;
        }
        AvaritiaShaders.cosmicOpacity.set(1.0F);
        AvaritiaShaders.cosmicOpacity2.set(0.2F);
        if (AvaritiaShaders.inventoryRender) {
            AvaritiaShaders.cosmicExternalScale.set(25.0F);
            AvaritiaShaders.cosmicExternalScale2.set(25.0F);
        } else {
            AvaritiaShaders.cosmicExternalScale.set(1.0F);
            AvaritiaShaders.cosmicExternalScale2.set(1.0F);
            AvaritiaShaders.cosmicYaw.set((float) ((this.mc.player.getYRot() * 2.0F) * Math.PI / 360.0D));
            AvaritiaShaders.cosmicPitch.set(-((float) ((this.mc.player.getXRot() * 2.0F) * Math.PI / 360.0D)));
            AvaritiaShaders.cosmicYaw2.set((float) ((this.mc.player.getYRot() * 2.0F) * Math.PI / 360.0D));
            AvaritiaShaders.cosmicPitch2.set(-((float) ((this.mc.player.getXRot() * 2.0F) * Math.PI / 360.0D)));
        }
        mStack.pushPose();
        mStack.scale(f, f, f);
        mStack.translate(0.0D, (this.babyYHeadOffset / 16.0F * f2), 0.0D);
        this.head.render(mStack, mat(AvaritiaShaders.MASK_SPRITES[0]).buffer(this.buf, this::mask), i1, i2, i3, i4, i5, i6);
        if (invulnRender && !player) {
            hatsOver().forEach(t -> t.render(mStack, mat(AvaritiaShaders.MASK_SPRITES_INV[0]).buffer(this.buf, InfinityArmorModel::mask2), i1, i2, i3, i4, i5, i6));
        }
        mStack.popPose();
        mStack.pushPose();
        mStack.scale(f1, f1, f1);
        mStack.translate(0.0D, (this.bodyYOffset / 16.0F * f2), 0.0D);
        if (invulnRender && !player) {
            bodyPartsOver().forEach(t -> t.render(mStack, mat(AvaritiaShaders.MASK_SPRITES_INV[0]).buffer(this.buf, InfinityArmorModel::mask2), i1, i2, i3, i4, i5, i6));
        }
        bodyParts().forEach(t -> t.render(mStack, mat(AvaritiaShaders.MASK_SPRITES[0]).buffer(this.buf, this::mask), i1, i2, i3, i4, i5, i6));
        bodyParts().forEach(t -> t.render(mStack, ver(glow(this.eyeTex)), i1, i2, 0.84F, 1.0F, 0.95F, (float)(pulse_mag_sqr * 0.5D)));
        mStack.popPose();
        mStack.pushPose();
        this.randy.setSeed(time / 3L * 1723609L);
        float[] col = HSVtoRGB(this.randy.nextFloat() * 6.0F, 1.0F, 1.0F);
        mStack.scale(f, f, f);
        mStack.translate(0.0D, (this.babyYHeadOffset / 16.0F * f2), -0.029999999329447746D);
        if (invulnRender) {
            this.hat.render(mStack, ver(RenderType.create("eyes", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 0,
                    RenderType.CompositeState.builder().setShaderState(RenderType.POSITION_COLOR_TEX_LIGHTMAP_SHADER).setTextureState(new RenderStateShard.TextureStateShard(this.eyeTex, false, false)).setCullState(RenderType.NO_CULL).createCompositeState(true))), i1, i2, col[0], col[1], col[2], 1.0F);
        } else {
//            float scale = 0.824625f;
//            mStack.scale(scale, scale, scale);
            this.hat.render(mStack, mat(AvaritiaShaders.MASK_SPRITES[0]).buffer(this.buf, this::mask3), i1, i2, i3, i4, i5, i6);
        }
        mStack.popPose();
        if (playerFlying && !AvaritiaShaders.inventoryRender) {
            mStack.pushPose();
            rebuildWings();
            mStack.scale(f1, f1, f1);
            mStack.translate(0.0D, (this.bodyYOffset / 16.0F * f2), 0.0D);
            model.renderToBufferWing(mStack, this.mc.renderBuffers().bufferSource().getBuffer(RenderType.armorCutoutNoCull(this.wingTex)), i1, i2, i3, i4, i5, i6);
            model.renderToBufferWing(mStack, mat(AvaritiaShaders.WING_SPRITES[0]).buffer(this.buf, this::mask), i1, i2, i3, i4, i5, i6);
            model.renderToBufferWing(mStack, this.mc.renderBuffers().bufferSource().getBuffer(glow(this.wingGlowTex)), i1, i2, 0.84F, 1.0F, 0.95F, (float)(pulse_mag_sqr * 0.5D));
            mStack.popPose();
        }
    }

    public void update(LivingEntity e, ItemStack i, EquipmentSlot a) {
        invulnRender = false;
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
        if (hasHat && hasChest && hasLeg && hasFoot)
            invulnRender = true;
        if (e instanceof Player) {
            player = true;
            if (hasChest && (((Player)e).getAbilities()).flying)
                playerFlying = true;
        }
        this.crouching = e.isCrouching();
        this.young = e.isBaby();
        this.riding = e.isPassenger();
    }

    public VertexConsumer ver(RenderType t) {
        return this.buf.getBuffer(t);
    }

    public static Material mat(TextureAtlasSprite t) {
        return new Material(TextureAtlas.LOCATION_BLOCKS, t.getName());
    }

    public Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg);
    }

    public Iterable<ModelPart> hatsOver() {
        return ImmutableList.of(this.invuln.hat, this.invuln.head);
    }

    public Iterable<ModelPart> bodyPartsOver() {
        return ImmutableList.of(this.invuln.body, this.invuln.rightArm, this.invuln.leftArm, this.invuln.rightLeg, this.invuln.leftLeg);
    }

    void copyPartAngles(ModelPart s, ModelPart d) {
        d.xRot = s.xRot;
        d.yRot = s.yRot;
        d.zRot = s.zRot;
        d.x = s.x;
        d.y = s.y;
        d.z = s.z;
    }

    void copyBipedAngles(HumanoidModel s, HumanoidModel d) {
        copyPartAngles(s.head, d.head);
        copyPartAngles(s.hat, d.hat);
        copyPartAngles(s.body, d.body);
        copyPartAngles(s.leftArm, d.leftArm);
        copyPartAngles(s.leftLeg, d.leftLeg);
        copyPartAngles(s.rightArm, d.rightArm);
        copyPartAngles(s.rightLeg, d.rightLeg);
    }

    public static class PlayerRender extends RenderLayer<Player, PlayerModel<Player>> {
        public Iterable<ModelPart> b() {
            return ImmutableList.of(getParentModel().head, (getParentModel()).hat, (getParentModel()).body, (getParentModel()).leftArm, (getParentModel()).rightArm, (getParentModel()).leftLeg, (getParentModel()).rightLeg);
        }

        public PlayerRender(RenderLayerParent<Player, PlayerModel<Player>> t) {
            super(t);
        }

        public void render(PoseStack m, MultiBufferSource b, int i1, Player l, float ff1, float ff2, float ff3, float ff4, float ff5, float ff6) {
            if (EventHandler.isInfinite(l)) {
                AvaritiaShaders.cosmicOpacity2.set(0.8F);
                b().forEach(t -> t.render(m, InfinityArmorModel.mat(AvaritiaShaders.MASK_SPRITES_INV[0]).buffer(b, InfinityArmorModel::mask2), i1, 1, 1.0F, 1.0F, 1.0F, 1.0F));
            }
        }
    }

    public static float[] HSVtoRGB(float h, float s, float v) {
        float[] hsv = new float[3];
        float[] rgb = new float[3];
        hsv[0] = h;
        hsv[1] = s;
        hsv[2] = v;
        if (hsv[0] == -1.0F) {
            rgb[2] = hsv[2];
            rgb[1] = hsv[2];
            rgb[0] = hsv[2];
            return rgb;
        }
        int i = (int)Math.floor(hsv[0]);
        float f = hsv[0] - i;
        if (i % 2 == 0)
            f = 1.0F - f;
        float m = hsv[2] * (1.0F - hsv[1]);
        float n = hsv[2] * (1.0F - hsv[1] * f);
        switch (i) {
            case 0:
            case 6:
                rgb[0] = hsv[2];
                rgb[1] = n;
                rgb[2] = m;
                break;
            case 1:
                rgb[0] = n;
                rgb[1] = hsv[2];
                rgb[2] = m;
                break;
            case 2:
                rgb[0] = m;
                rgb[1] = hsv[2];
                rgb[2] = n;
                break;
            case 3:
                rgb[0] = m;
                rgb[1] = n;
                rgb[2] = hsv[2];
                break;
            case 4:
                rgb[0] = n;
                rgb[1] = m;
                rgb[2] = hsv[2];
                break;
            case 5:
                rgb[0] = hsv[2];
                rgb[1] = m;
                rgb[2] = n;
                break;
        }
        return rgb;
    }
}
