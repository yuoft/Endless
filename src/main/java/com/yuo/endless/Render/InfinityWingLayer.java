package com.yuo.endless.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.ItemRegistry;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InfinityWingLayer<T extends PlayerEntity> extends LayerRenderer<T, PlayerModel<T>> {
    private static final ResourceLocation WING = new ResourceLocation(Endless.MOD_ID, "textures/models/infinity_wing.png");
    private static final ResourceLocation WING_GLOW = new ResourceLocation(Endless.MOD_ID, "textures/models/infinity_wingglow.png");

    public InfinityWingLayer(IEntityRenderer<T, PlayerModel<T>> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, PlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (isInfinityChest(player) && Config.CLIENT.isRenderLayer.get()){
            if (!player.isSpectator() && player.abilities.isFlying){
                matrixStackIn.push();
                IVertexBuilder builder = bufferIn.getBuffer(RenderType.getEntityCutout(WING_GLOW));
//                IVertexBuilder buffer = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(partialTicks % 2 == 0 ? WING : WING_GLOW));
                InfinityWingModel model = new InfinityWingModel();
                model.render(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f,1.0f,1.0f,1.0f);
                matrixStackIn.pop();
            }
        }
    }

    private boolean isInfinityChest(PlayerEntity player){
        return player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == ItemRegistry.infinityChest.get();
    }
}
