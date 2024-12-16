package com.yuo.endless.Client.Model;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Client.Lib.WrappedItemModel;
import com.yuo.endless.Config;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.MatterCluster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
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
        if (source instanceof IRenderTypeBuffer.Impl)
            ((IRenderTypeBuffer.Impl)source).finish();
        if (stack.getItem() == EndlessItems.matterCluster.get()) {
            AvaritiaShaders.cosmicOpacity = getaa(stack);
        } else {
            AvaritiaShaders.cosmicOpacity = 1.0F;
        }
        Minecraft mc = Minecraft.getInstance();
        if (transformType == TransformType.GUI) {
            AvaritiaShaders.scale = 25.0F;
            AvaritiaShaders.yaw = 0.0F;
            AvaritiaShaders.pitch = 0.0F;
        } else if (!AvaritiaShaders.inventoryRender) {
            AvaritiaShaders.scale = 1.0F;
            if (mc.player != null) {
                AvaritiaShaders.yaw = (float)((mc.player.yRotO * 2.0F) * Math.PI / 360.0D);
                AvaritiaShaders.pitch = -((float)((mc.player.yHeadRot * 2.0F) * Math.PI / 360.0D));
            }
        }
        AvaritiaShaders.useShader();
        mc.getItemRenderer().render(mStack, source.getBuffer(RenderType.create("", DefaultVertexFormat.NEW_ENTITY, 7, 256, true, true,
                RenderType.State.getBuilder().texture(RenderType.BLOCK_SHEET).transparency(RenderType.TRANSLUCENT_TRANSPARENCY).writeMask(RenderType.COLOR_WRITE)
                .diffuseLighting(RenderType.DIFFUSE_LIGHTING_ENABLED).lightmap(RenderType.LIGHTNING).overlay(RenderType.OVERLAY_ENABLED).build(true))), this.maskQuad, stack, light, overlay);
        AvaritiaShaders.releaseShader(source);
    }

    public float getaa(ItemStack itemStack){
        float i = MatterCluster.getItemTag(itemStack).size() / (Config.SERVER.matterClusterMaxTerm.get() * 1.0f);
        return (float) (Math.floor(i * 100) / 100.f);
    }
}