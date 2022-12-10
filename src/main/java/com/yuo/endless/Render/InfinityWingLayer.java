package com.yuo.endless.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;

public class InfinityWingLayer extends LayerRenderer<PlayerEntity, PlayerModel<PlayerEntity>> {
    private static final ResourceLocation WING = new ResourceLocation(Endless.MOD_ID, "textures/models/infinity_wing.png");
    private static final ResourceLocation WING_GLOW = new ResourceLocation(Endless.MOD_ID, "textures/models/infinity_wingglow.png");

    public InfinityWingLayer(IEntityRenderer<PlayerEntity, PlayerModel<PlayerEntity>> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, PlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (isInfinityChest(player)){
            if (!player.isSpectator() && player.abilities.isFlying){
                matrixStackIn.push();
                IVertexBuilder builder;
                if (Config.CLIENT.isChangeWing.get()){
                    builder = bufferIn.getBuffer(RenderType.getEntityCutout(player.world.getDayTime() % 2 == 0 ? WING : WING_GLOW));
                } else builder = bufferIn.getBuffer(RenderType.getEntityCutout(WING_GLOW));
                InfinityWingModel model = new InfinityWingModel();
                model.render(matrixStackIn, builder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0f,1.0f,1.0f,1.0f);
                matrixStackIn.pop();
            }
        }
    }

    private boolean isInfinityChest(PlayerEntity player){
        return player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == EndlessItems.infinityChest.get();
    }
}
