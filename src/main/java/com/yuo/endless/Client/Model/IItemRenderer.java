package com.yuo.endless.Client.Model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public interface IItemRenderer extends BakedModel {
    void renderItem(ItemStack paramItemStack, ItemDisplayContext paramTransformType, PoseStack paramMatrixStack, MultiBufferSource paramIRenderTypeBuffer, int paramInt1, int paramInt2);

    @NotNull
    ModelState getModelTransform() ;

//    default BakedModel applyTransform(ItemTransform type, PoseStack mat) {
//        return PerspectiveMapWrapper.handlePerspective(this, getModelTransform(), type, mat);
//    }

    @Override
    default boolean useAmbientOcclusion(BlockState state) {
        return true;
    }

    default boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    default List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource){
        return Collections.emptyList();
    }

    default boolean isCustomRenderer() {
        return true;
    }

    default @NotNull TextureAtlasSprite getParticleIcon() {
        return getMissingSprite();
    }

    static TextureAtlasSprite getMissingSprite() {
        return getTextureMap().getSprite(TextureAtlas.LOCATION_PARTICLES);
    }

    static TextureAtlas getTextureMap() {
        return Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
    }

    default @NotNull ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}