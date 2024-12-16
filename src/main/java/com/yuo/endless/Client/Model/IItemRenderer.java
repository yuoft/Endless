package com.yuo.endless.Client.Model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public interface IItemRenderer extends BakedModel {
    void renderItem(ItemStack paramItemStack, TransformType paramTransformType, PoseStack paramMatrixStack, MultiBufferSource paramIRenderTypeBuffer, int paramInt1, int paramInt2);

    @Override
    @NotNull
    ItemTransforms getTransforms() ;

    default BakedModel handlePerspective(TransformType type, PoseStack mat) {
        return ForgeHooksClient.handlePerspective(this, type, mat);
    }

    default boolean doesHandlePerspectives() {
        return true;
    }

    @Override
    default List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, Random random){
        return Collections.emptyList();
    }

    default boolean isCustomRenderer() {
        return true;
    }

    default TextureAtlasSprite getParticleIcon() {
        return getMissingSprite();
    }

    static TextureAtlasSprite getMissingSprite() {
        return getTextureMap().getSprite(TextureAtlas.LOCATION_PARTICLES);
    }

    static TextureAtlas getTextureMap() {
        return Minecraft.getInstance().getModelManager().getAtlas(TextureAtlas.LOCATION_BLOCKS);
    }

    default ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}