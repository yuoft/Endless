package com.yuo.endless.Client.Model;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Client.Lib.PerspectiveModelState;
import com.yuo.endless.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.MatterCluster;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
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
//        if (stack.getItem() == EndlessItems.infinitySword.get()) {
//            this.parentState = TransformUtils.DEFAULT_TOOL;
//        } else if (stack.getItem() != EndlessItems.infinityBow.get() && stack.getItem() != EndlessItems.infinityCrossBow.get()) {
//            this.parentState = TransformUtils.DEFAULT_ITEM;
//        } else {
//            this.parentState = TransformUtils.DEFAULT_BOW;
//        }

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
                    .apply(ResourceLocation.fromNamespaceAndPath(Endless.MOD_ID, "shader/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }

        if (AvaritiaShaders.cosmicUVs != null) {
            AvaritiaShaders.cosmicUVs.set(AvaritiaShaders.COSMIC_UVS);
        }

        VertexConsumer cons = source.getBuffer(AvaritiaShaders.COSMIC_RENDER_TYPE);
        List<TextureAtlasSprite> atlasSprite = new ArrayList<>();

        for (ResourceLocation res : this.maskSprite) {
            atlasSprite.add(mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(res));
        }

        mc.getItemRenderer().renderQuadList(pStack, cons, bakeItem(atlasSprite), stack, light, overlay);
    }
    private static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();
    private static final FaceBakery FACE_BAKERY = new FaceBakery();
    public static List<BakedQuad> bakeItem(final List<TextureAtlasSprite> sprites) {
        final LinkedList<BakedQuad> quads = new LinkedList<>();
        for (final TextureAtlasSprite sprite : sprites) {
            final List<BlockElement> unbaked = ITEM_MODEL_GENERATOR.processFrames(sprites.indexOf(sprite), "layer" + sprites.indexOf(sprite), sprite.contents());
            for (final BlockElement element : unbaked) {
                for (final Map.Entry<Direction, BlockElementFace> entry : element.faces.entrySet()) {
                    quads.add(FACE_BAKERY.bakeQuad(element.from, element.to, entry.getValue(), sprite, entry.getKey(), new PerspectiveModelState(ImmutableMap.of()), element.rotation, element.shade, ResourceLocation.fromNamespaceAndPath(Endless.MOD_ID,"dynamic")));
                }
            }
        }
        return quads;
    }

    public float getMatterClusterOpacity(ItemStack itemStack){
        float i = MatterCluster.getItemTag(itemStack).size() / (Config.SERVER.matterClusterMaxTerm.get() * 1.0f);
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