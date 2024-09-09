package com.yuo.endless.Client.Model;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.PerspectiveMapWrapper;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public interface IItemRenderer extends IBakedModel {
    void renderItem(ItemStack paramItemStack, ItemCameraTransforms.TransformType paramTransformType, MatrixStack paramMatrixStack, IRenderTypeBuffer paramIRenderTypeBuffer, int paramInt1, int paramInt2);

    IModelTransform getModelTransform();


    default IBakedModel handlePerspective(ItemCameraTransforms.TransformType type, MatrixStack mat) {
        return PerspectiveMapWrapper.handlePerspective(this, getModelTransform(), type, mat);
    }

    default boolean doesHandlePerspectives() {
        return true;
    }

    default List<BakedQuad> getQuads(BlockState b, Direction d, Random r) {
        return Collections.emptyList();
    }

    default boolean isBuiltInRenderer() {
        return true;
    }

    default TextureAtlasSprite getParticleTexture() {
        return getMissingSprite();
    }

    static TextureAtlasSprite getMissingSprite() {
        return getTextureMap().getSprite(MissingTextureSprite.getLocation());
    }

    static AtlasTexture getTextureMap() {
        return Minecraft.getInstance().getModelManager().getAtlasTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
    }

    default ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }
}