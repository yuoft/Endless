package com.yuo.endless.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yuo.endless.client.AvaritiaShaders;
import com.yuo.endless.client.lib.PerspectiveModelState;
import com.yuo.endless.config.ModConfig;
import com.yuo.endless.items.EndlessItems;
import com.yuo.endless.items.MatterCluster;
import com.yuo.endless.EndlessUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CosmicBakedModel extends WrappedItemModel implements IItemRenderer{
    private final List<ResourceLocation> maskSprite;

    public CosmicBakedModel(BakedModel wrapped, List<ResourceLocation> maskSprite) {
        super(wrapped);
        this.maskSprite = maskSprite;
    }

    public void renderItem(ItemStack stack, ItemDisplayContext transformType, PoseStack pStack, MultiBufferSource source, int light, int overlay) {
        this.renderWrapped(stack, pStack, source, light, overlay, true);
        if (source instanceof MultiBufferSource.BufferSource bs) {
            bs.endBatch();
        }

        Minecraft mc = Minecraft.getInstance();
        float yaw = 0.0F;
        float pitch = 0.0F;
        float scale = 1.0F;
        if (AvaritiaShaders.inventoryRender || transformType == ItemDisplayContext.GUI) {
            scale = 100.0F;
        } else {
            yaw = (float)(mc.player.getYRot() * 2.0f * Math.PI / 360.0);
            pitch = -(float)(mc.player.getXRot() * 2.0f * Math.PI / 360.0);
        }

        AvaritiaShaders.cosmicTime.set((float)(System.currentTimeMillis() - (long) AvaritiaShaders.renderTime) / 2000.0F);
        AvaritiaShaders.cosmicYaw.set(yaw);
        AvaritiaShaders.cosmicPitch.set(pitch);
        AvaritiaShaders.cosmicExternalScale.set(scale);
        if (stack.getItem() == EndlessItems.matterCluster.get()) {
            AvaritiaShaders.cosmicOpacity.set(getMatterClusterOpacity(stack));
        } else {
            AvaritiaShaders.cosmicOpacity.set(1.0F);
        }

        for(int i = 0; i < 10; ++i) {
            TextureAtlasSprite sprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                    .apply(EndlessUtils.fa( "shader/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }

        if (AvaritiaShaders.cosmicUVs != null) {
            AvaritiaShaders.cosmicUVs.set(AvaritiaShaders.COSMIC_UVS);
        }

        VertexConsumer cons = source.getBuffer(AvaritiaShaders.COSMIC_RENDER_TYPE);
        BakedModel model = this.wrapped.getOverrides().resolve(this.wrapped, stack, this.world, this.entity, 0);
        if (model != null && model.isGui3d() && stack.getItem() instanceof BlockItem) { //是否是方块
//            for (BakedModel bakedModel : model.getRenderPasses(stack, true)) {  加上后渲染出错
//                for (RenderType rendertype : bakedModel.getRenderTypes(stack, true))
//                    itemRenderer.renderModelLists(bakedModel, stack, light, overlay, pStack, source.getBuffer(rendertype));
//            }
            List<BakedQuad> blockLayer = new ArrayList<>();
            RandomSource random = RandomSource.create();
            for (Direction direction : Direction.values()) //获取六面
                blockLayer.addAll(model.getQuads(null, direction, random));
            List<TextureAtlasSprite> maskSprites = new ArrayList<>();
            for (ResourceLocation res : this.maskSprite)
                maskSprites.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(res));
            List<BakedQuad> overlayQuads = new ArrayList<>();
            for (BakedQuad base : blockLayer) { //添加纹理
                for (TextureAtlasSprite sprite : maskSprites) {
                    BakedQuad masked = new BakedQuad(base.getVertices(), base.getTintIndex(), base.getDirection(), sprite, base.isShade());
                    overlayQuads.add(masked);
                }
            }
            mc.getItemRenderer().renderQuadList(pStack, cons, overlayQuads, stack, light, overlay);
        } else {
            List<TextureAtlasSprite> atlasSprite = new ArrayList<>();

            for (ResourceLocation res : this.maskSprite) {
                atlasSprite.add(mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(res));
            }

            mc.getItemRenderer().renderQuadList(pStack, cons, bakeItem(atlasSprite), stack, light, overlay);
        }
    }

    public float getMatterClusterOpacity(ItemStack itemStack){
        float i = MatterCluster.getItemTag(itemStack).size() / (ModConfig.SERVER.matterClusterMaxTerm.get() * 1.0f);
        return (float) (Math.floor(i * 100) / 100.f);
    }

    public @Nullable PerspectiveModelState getModelState() {
        return (PerspectiveModelState)this.parentState;
    }

    public boolean isCosmic() {
        return true;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.wrapped.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.wrapped.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.wrapped.usesBlockLight();
    }
}