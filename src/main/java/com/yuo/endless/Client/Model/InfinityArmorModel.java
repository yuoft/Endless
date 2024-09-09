package com.yuo.endless.Client.Model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Random;

public class InfinityArmorModel extends BipedModel {
    String s = "endless:textures/models/";

    ResourceLocation eyeTex = new ResourceLocation(this.s + "infinity_armor_eyes.png");

    ResourceLocation wingTex = new ResourceLocation(this.s + "infinity_armor_wing.png");

    ResourceLocation wingGlowTex = new ResourceLocation(this.s + "infinity_armor_wingglow.png");

    boolean invulnRender;

    boolean playerFlying;

    boolean player;

    boolean legs = true;

    Minecraft mc = Minecraft.getInstance();

    IRenderTypeBuffer buf = (IRenderTypeBuffer)this.mc.getRenderTypeBuffers().getBufferSource();

    Random randy = new Random();

    BipedModel invuln;

    float expand;

    ModelRenderer bipedLeftWing;

    ModelRenderer bipedRightWing;

    RenderType glow(ResourceLocation t) {
        return (RenderType)RenderType.makeType("", DefaultVertexFormats.ENTITY, 7, 256, true,
                true, RenderType.State.getBuilder().cull(RenderType.CULL_DISABLED).texture(new RenderState.TextureState(t, false, false)).transparency(RenderType.LIGHTNING_TRANSPARENCY)
                .alpha(RenderType.DEFAULT_ALPHA).overlay(RenderType.OVERLAY_ENABLED).layer(RenderType.VIEW_OFFSET_Z_LAYERING).build(true));
    }

    static RenderType mask(ResourceLocation t) {
        return (RenderType)RenderType.makeType("", DefaultVertexFormats.BLOCK, 7, 256, true,
                true, RenderType.State.getBuilder().cull(RenderType.CULL_DISABLED).texture(new RenderState.TextureState(t, false, false)).transparency(RenderType.TRANSLUCENT_TRANSPARENCY)
                .writeMask(RenderType.COLOR_WRITE).overlay(RenderType.OVERLAY_ENABLED).layer(RenderType.VIEW_OFFSET_Z_LAYERING).build(true));
    }

    static RenderType mask2(ResourceLocation t) {
        return (RenderType)RenderType.makeType("", DefaultVertexFormats.ENTITY, 7, 256, true,
                true, RenderType.State.getBuilder().cull(RenderType.CULL_DISABLED).texture(new RenderState.TextureState(t, false, false)).transparency(RenderType.TRANSLUCENT_TRANSPARENCY)
                .writeMask(RenderType.COLOR_WRITE).overlay(RenderType.OVERLAY_ENABLED).build(true));
    }

    public InfinityArmorModel(float expand) {
        super(expand, 0.0F, 64, 64);
        this.expand = expand;
        this.invuln = new BipedModel(0.0F);
        this.bipedHeadwear = new ModelRenderer((Model)this, 32, 0);
        this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, expand * 0.5F);
        this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public InfinityArmorModel setLegs(boolean islegs) {
        this.legs = islegs;
        int heightoffset = 0;
        int legoffset = islegs ? 32 : 0;
        this.bipedBody = new ModelRenderer((Model)this, 16, 16 + legoffset);
        this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, this.expand);
        this.bipedBody.setRotationPoint(0.0F, (0 + heightoffset), 0.0F);
        this.bipedRightLeg = new ModelRenderer((Model)this, 0, 16 + legoffset);
        this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, this.expand);
        this.bipedRightLeg.setRotationPoint(-1.9F, (12 + heightoffset), 0.0F);
        this.bipedLeftLeg = new ModelRenderer((Model)this, 0, 16 + legoffset);
        this.bipedLeftLeg.mirror = true;
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, this.expand);
        this.bipedLeftLeg.setRotationPoint(1.9F, (12 + heightoffset), 0.0F);
        return this;
    }

    public void render(MatrixStack mStack, IVertexBuilder vertexBuilder, int i1, int i2, float i3, float i4, float i5, float i6) {
        float f, f1, f2;
        copyBipedAngles(this, this.invuln);
        super.render(mStack, vertexBuilder, i1, i2, i3, i4, i5, i6);
        AvaritiaShaders.shading(this.buf);
        long time = this.mc.player.world.getGameTime();
        double pulse = Math.sin(time / 10.0D) * 0.5D + 0.5D;
        double pulse_mag_sqr = Math.pow(pulse, 6.0D);
        if (this.isChild) {
            f = 1.5F / this.childHeadScale;
            f1 = 1.0F / this.childBodyScale;
            f2 = 1.0F;
        } else {
            f = 1.0F;
            f1 = 1.0F;
            f2 = 0.0F;
        }
        AvaritiaShaders.cosmicOpacity = 1.0F;
        AvaritiaShaders.cosmicOpacity2 = 0.2F;
        if (AvaritiaShaders.inventoryRender) {
            AvaritiaShaders.scale = 25.0F;
        } else {
            AvaritiaShaders.scale = 1.0F;
            AvaritiaShaders.yaw = (float)((this.mc.player.rotationYaw * 2.0F) * Math.PI / 360.0D);
            AvaritiaShaders.pitch = -((float)((this.mc.player.rotationPitch * 2.0F) * Math.PI / 360.0D));
        }
        if (this.invulnRender) {
            mStack.push();
            AvaritiaShaders.useShader2();
            if (!this.player)
                this.invuln.render(mStack, mat(AvaritiaShaders.MASK_INV).getBuffer(this.buf, InfinityArmorModel::mask), i1, i2, i3, i4, i5, i6);
            AvaritiaShaders.releaseShader(this.buf);
            mStack.pop();
        }
        AvaritiaShaders.useShader();
        mStack.push();
        mStack.scale(f, f, f);
        mStack.translate(0.0D, (this.childHeadOffsetY / 16.0F * f2), -0.029999999329447746D);
        this.bipedHeadwear.render(mStack, mat(AvaritiaShaders.MASK).getBuffer(this.buf, InfinityArmorModel::mask2), i1, i2, 1.0F, 1.0F, 1.0F, 1.0F);
        mStack.pop();
        mStack.push();
        mStack.scale(f, f, f);
        mStack.translate(0.0D, (this.childHeadOffsetY / 16.0F * f2), (this.childHeadOffsetZ / 16.0F * f2));
        this.bipedHead.render(mStack, mat(AvaritiaShaders.MASK).getBuffer(this.buf, InfinityArmorModel::mask), i1, i2, 1.0F, 1.0F, 1.0F, 1.0F);
        mStack.pop();
        mStack.push();
        mStack.scale(f1, f1, f1);
        mStack.translate(0.0D, (this.childBodyOffsetY / 16.0F * f2), 0.0D);
        func_225600_b_().forEach(t -> t.render(mStack, mat(AvaritiaShaders.MASK).getBuffer(this.buf, InfinityArmorModel::mask), i1, i2, 1.0F, 1.0F, 1.0F, 1.0F));
        mStack.pop();
        AvaritiaShaders.releaseShader(this.buf);
        mStack.push();
        mStack.scale(f1, f1, f1);
        mStack.translate(0.0D, (this.childBodyOffsetY / 16.0F * f2), 0.0D);
        func_225600_b_().forEach(t -> t.render(mStack, ver(glow(this.eyeTex)), i1, i2, 0.84F, 1.0F, 0.95F, (float)(pulse_mag_sqr * 0.5D)));
        mStack.pop();
        if (this.invulnRender) {
            mStack.push();
            this.randy.setSeed(time / 3L * 1723609L);
            float[] col = HSVtoRGB(this.randy.nextFloat() * 6.0F, 1.0F, 1.0F);
            mStack.scale(f, f, f);
            mStack.translate(0.0D, (this.childHeadOffsetY / 16.0F * f2), -0.029999999329447746D);
            this.bipedHeadwear.render(mStack, ver((RenderType)RenderType.makeType("", DefaultVertexFormats.ENTITY, 7, 0,
                    RenderType.State.getBuilder().texture(new RenderState.TextureState(this.eyeTex, false, false))
                    .alpha(RenderType.DEFAULT_ALPHA).overlay(RenderType.OVERLAY_ENABLED).cull(RenderType.CULL_DISABLED).build(true))), i1, i2, col[0], col[1], col[2], 1.0F);
            mStack.pop();
        }
        if (this.playerFlying && !AvaritiaShaders.inventoryRender) {
            mStack.push();
            mStack.scale(f1, f1, f1);
            mStack.translate(0.0D, (this.childBodyOffsetY / 16.0F * f2), 0.0D);
            renderToBufferWing(mStack, ver(RenderType.getArmorCutoutNoCull(this.wingTex)), i1, i2, i3, i4, i5, i6);
            AvaritiaShaders.shading(this.buf);
            AvaritiaShaders.useShader();
            renderToBufferWing(mStack, mat(AvaritiaShaders.WING).getBuffer(this.buf, InfinityArmorModel::mask), i1, i2, i3, i4, i5, i6);
            AvaritiaShaders.releaseShader(this.buf);
            renderToBufferWing(mStack, ver(glow(this.wingGlowTex)), i1, i2, 0.84F, 1.0F, 0.95F, (float)(pulse_mag_sqr * 0.5D));
            mStack.pop();
        }
    }

    public void update(LivingEntity e, ItemStack i, EquipmentSlotType a) {
        this.invulnRender = false;
        this.playerFlying = false;
        this.player = false;
        ItemStack hats = e.getItemStackFromSlot(EquipmentSlotType.HEAD);
        ItemStack chest = e.getItemStackFromSlot(EquipmentSlotType.CHEST);
        ItemStack leg = e.getItemStackFromSlot(EquipmentSlotType.LEGS);
        ItemStack foot = e.getItemStackFromSlot(EquipmentSlotType.FEET);
        boolean hasHat = hats.getItem() == EndlessItems.infinityHead.get();
        boolean hasChest = chest.getItem() == EndlessItems.infinityChest.get();
        boolean hasLeg = leg.getItem() == EndlessItems.infinityLegs.get();
        boolean hasFoot = foot.getItem() == EndlessItems.infinityFeet.get();
        if (hasHat && hasChest && hasLeg && hasFoot)
            this.invulnRender = true;
        if (e instanceof PlayerEntity) {
            this.player = true;
            if (hasChest && ((PlayerEntity)e).abilities.isFlying)
                this.playerFlying = true;
        }
        this.isSneak = e.isCrouching();
        this.isChild = e.isChild();
        this.isSitting = e.isPassenger();
        this.invuln.isSneak = this.isSneak;
        this.invuln.isSitting = this.isSitting;
        this.invuln.isChild = this.isChild;
        this.invuln.swimAnimation = this.swimAnimation;
    }

    void renderToBufferWing(MatrixStack m, IVertexBuilder v, int i1, int i2, float i3, float i4, float i5, float i6) {
        this.bipedLeftWing = new ModelRenderer((Model)this, 0, 0);
        this.bipedLeftWing.mirror = true;
        this.bipedLeftWing.addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F);
        this.bipedLeftWing.setRotationPoint(-1.5F, 0.0F, 2.0F);
        this.bipedLeftWing.rotateAngleY = 1.2566371F;
        this.bipedRightWing = new ModelRenderer((Model)this, 0, 0);
        this.bipedRightWing.addBox(0.0F, -11.6F, 0.0F, 0.0F, 32.0F, 32.0F);
        this.bipedRightWing.setRotationPoint(1.5F, 0.0F, 2.0F);
        this.bipedRightWing.rotateAngleY = -1.2566371F;
        this.bipedLeftWing.render(m, v, i1, i2, i3, i4, i5, i6);
        this.bipedRightWing.render(m, v, i1, i2, i3, i4, i5, i6);
    }

    public IVertexBuilder ver(RenderType t) {
        return this.buf.getBuffer(t);
    }

    public static RenderMaterial mat(TextureAtlasSprite t) {
        return new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, t.getName());
    }

    public Iterable<ModelRenderer> func_225600_b_() {
        return (Iterable<ModelRenderer>)ImmutableList.of(this.bipedBody, this.bipedRightArm, this.bipedLeftArm,
                this.bipedRightLeg, this.bipedLeftLeg);
    }

    void copyPartAngles(ModelRenderer s, ModelRenderer d) {
        d.rotateAngleX = s.rotateAngleX;
        d.rotateAngleY = s.rotateAngleY;
        d.rotateAngleZ = s.rotateAngleZ;
        d.rotationPointX = s.rotationPointX;
        d.rotationPointY = s.rotationPointY;
        d.rotationPointZ = s.rotationPointZ;
    }

    void copyBipedAngles(BipedModel s, BipedModel d) {
        copyPartAngles(s.bipedHead, d.bipedHead);
        copyPartAngles(s.bipedHeadwear, d.bipedHeadwear);
        copyPartAngles(s.bipedBody, d.bipedBody);
        copyPartAngles(s.bipedLeftArm, d.bipedLeftArm);
        copyPartAngles(s.bipedRightArm, d.bipedRightArm);
        copyPartAngles(s.bipedLeftLeg, d.bipedLeftLeg);
        copyPartAngles(s.bipedRightLeg, d.bipedRightLeg);
    }

    public static class PlayerRender extends LayerRenderer<LivingEntity, BipedModel<LivingEntity>> {
        public PlayerRender(IEntityRenderer<LivingEntity, BipedModel<LivingEntity>> t) {
            super(t);
        }

        public void render(MatrixStack m, IRenderTypeBuffer b, int i1, LivingEntity l, float ff1, float ff2, float ff3, float ff4, float ff5, float ff6) {
            if (EventHandler.isInfinite((PlayerEntity) l)) {
                AvaritiaShaders.shading(b);
                m.push();
                AvaritiaShaders.useShader2();
                ImmutableList.of(((BipedModel)getEntityModel()).bipedHead, ((BipedModel)getEntityModel()).bipedHeadwear,
                        ((BipedModel)getEntityModel()).bipedBody, ((BipedModel)getEntityModel()).bipedLeftArm,
                        ((BipedModel)getEntityModel()).bipedRightArm, ((BipedModel)getEntityModel()).bipedLeftLeg,
                        ((BipedModel)getEntityModel()).bipedRightLeg).forEach(t -> t.render(m,
                        InfinityArmorModel.mat(AvaritiaShaders.MASK_INV).getBuffer(b, InfinityArmorModel::mask),
                        i1, 1, 1.0F, 1.0F, 1.0F, 1.0F));
                AvaritiaShaders.releaseShader(b);
                m.pop();
            }
        }
    }

    public static float[] HSVtoRGB(float h, float s, float v) {
        float[] q = new float[3];
        float[] w = new float[3];
        q[0] = h;
        q[1] = s;
        q[2] = v;
        if (q[0] == -1.0F) {
            w[2] = q[2];
            w[1] = q[2];
            w[0] = q[2];
            return w;
        }
        int i = (int)Math.floor(q[0]);
        float f = q[0] - i;
        if (i % 2 == 0)
            f = 1.0F - f;
        float m = q[2] * (1.0F - q[1]);
        float n = q[2] * (1.0F - q[1] * f);
        switch (i) {
            case 0:
            case 6:
                w[0] = q[2];
                w[1] = n;
                w[2] = m;
                break;
            case 1:
                w[0] = n;
                w[1] = q[2];
                w[2] = m;
                break;
            case 2:
                w[0] = m;
                w[1] = q[2];
                w[2] = n;
                break;
            case 3:
                w[0] = m;
                w[1] = n;
                w[2] = q[2];
                break;
            case 4:
                w[0] = n;
                w[1] = m;
                w[2] = q[2];
                break;
            case 5:
                w[0] = q[2];
                w[1] = m;
                w[2] = n;
                break;
        }
        return w;
    }
}
