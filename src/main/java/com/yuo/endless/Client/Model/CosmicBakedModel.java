package com.yuo.endless.Client.Model;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Config;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.MatterCluster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;

public class CosmicBakedModel extends WrappedItemModel implements IItemRenderer {
    public CosmicBakedModel(BakedModel wrapped, TextureAtlasSprite maskSprite) {
        super(wrapped);
        this.maskQuad = bakeItem(maskSprite);
    }

    @Override
    public void renderItem(ItemStack stack, TransformType transformType, PoseStack mStack, MultiBufferSource source, int light, int overlay) {
        renderWrapped(stack, mStack, source, light, overlay, true);
        if (source instanceof MultiBufferSource.BufferSource bs)
            bs.endBatch();
        if (stack.getItem() == EndlessItems.matterCluster.get()) {
            AvaritiaShaders.cosmicOpacity.set(getMatterClusterOpacity(stack));
        } else {
            AvaritiaShaders.cosmicOpacity.set(1.0F);
        }
        Minecraft mc = Minecraft.getInstance();
        float yaw = 0.0F;
        float pitch = 0.0F;
        float scale = 25.0F;
        if (transformType != TransformType.GUI && !AvaritiaShaders.inventoryRender) {
            if (mc.player != null) {
                yaw = (float) ((mc.player.getYHeadRot() * 2.0F) * Math.PI / 360.0D);
                pitch = -((float) ((mc.player.getXRot() * 2.0F) * Math.PI / 360.0D));
                scale = 1.0F;
            }
        }

        AvaritiaShaders.cosmicYaw.set(yaw);
        AvaritiaShaders.cosmicPitch.set(pitch);
        AvaritiaShaders.cosmicExternalScale.set(scale);

        mc.getItemRenderer().renderQuadList(mStack, source.getBuffer(RenderType.create("cosmic", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 0, true, true,
                RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(() -> AvaritiaShaders.cosmicShader)).setTextureState(RenderType.BLOCK_SHEET).setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                        .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY).setWriteMaskState(RenderType.COLOR_WRITE).setLightmapState(RenderType.LIGHTMAP).createCompositeState(true))), this.maskQuad, stack, light, overlay);

    }


    public float getMatterClusterOpacity(ItemStack itemStack){
        float i = MatterCluster.getItemTag(itemStack).size() / (Config.SERVER.matterClusterMaxTerm.get() * 1.0f);
        return (float) (Math.floor(i * 100) / 100.f);
    }

    @Override
    boolean isCosmic() {
        return true;
    }
}