package com.yuo.endless.Client.Model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Items.EndlessItems;
import committee.nova.mods.avaritia.Static;
import committee.nova.mods.avaritia.api.client.model.PerspectiveModelState;
import committee.nova.mods.avaritia.api.client.model.bakedmodels.WrappedItemModel;
import committee.nova.mods.avaritia.api.client.util.TransformUtils;
import committee.nova.mods.avaritia.common.item.resources.MatterClusterItem;
import committee.nova.mods.avaritia.init.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CosmicBakedModel extends WrappedItemModel {
    private final List<ResourceLocation> maskSprite;

    public CosmicBakedModel(BakedModel wrapped, List<ResourceLocation> maskSprite) {
        super(wrapped);
        this.maskSprite = maskSprite;
    }

    public void renderItem(ItemStack stack, ItemDisplayContext transformType, PoseStack pStack, MultiBufferSource source, int light, int overlay) {
        if (stack.getItem() == EndlessItems.infinitySword.get()) {
            this.parentState = TransformUtils.DEFAULT_TOOL;
        } else if (stack.getItem() != EndlessItems.infinityBow.get() && stack.getItem() != EndlessItems.infinityCrossBow.get()) {
            this.parentState = TransformUtils.DEFAULT_ITEM;
        } else {
            this.parentState = TransformUtils.DEFAULT_BOW;
        }

        this.renderWrapped(stack, pStack, source, light, overlay, true);
        if (source instanceof MultiBufferSource.BufferSource bs) {
            bs.endBatch();
        }

        Minecraft mc = Minecraft.getInstance();
        float yaw = 0.0F;
        float pitch = 0.0F;
        float scale = 1.0F;
        if (!AvaritiaShaders.inventoryRender && transformType != ItemDisplayContext.GUI) {
            yaw = (float)((double)(mc.player.getYRot() * 2.0F) * Math.PI / 360.0);
            pitch = -((float)((double)(mc.player.getXRot() * 2.0F) * Math.PI / 360.0));
        } else {
            scale = 100.0F;
        }

        AvaritiaShaders.cosmicTime.set((float)(System.currentTimeMillis() - (long) AvaritiaShaders.renderTime) / 2000.0F);
        AvaritiaShaders.cosmicYaw.set(yaw);
        AvaritiaShaders.cosmicPitch.set(pitch);
        AvaritiaShaders.cosmicExternalScale.set(scale);
        if (stack.getItem() == ModItems.matter_cluster.get()) {
            AvaritiaShaders.cosmicOpacity.set((float) MatterClusterItem.getClusterSize(stack) / (float)MatterClusterItem.CAPACITY);
        } else {
            AvaritiaShaders.cosmicOpacity.set(1.0F);
        }

        for(int i = 0; i < 10; ++i) {
            TextureAtlasSprite sprite = (TextureAtlasSprite)Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(Static.rl("misc/cosmic_" + i));
            AvaritiaShaders.COSMIC_UVS[i * 4] = sprite.getU0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 1] = sprite.getV0();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 2] = sprite.getU1();
            AvaritiaShaders.COSMIC_UVS[i * 4 + 3] = sprite.getV1();
        }

        if (AvaritiaShaders.cosmicUVs != null) {
            AvaritiaShaders.cosmicUVs.set(AvaritiaShaders.COSMIC_UVS);
        }

        VertexConsumer cons = source.getBuffer(AvaritiaShaders.COSMIC_RENDER_TYPE);
        List<TextureAtlasSprite> atlasSprite = new ArrayList();
        Iterator var13 = this.maskSprite.iterator();

        while(var13.hasNext()) {
            ResourceLocation res = (ResourceLocation)var13.next();
            atlasSprite.add((TextureAtlasSprite)Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(res));
        }

        mc.getItemRenderer().renderQuadList(pStack, cons, bakeItem(atlasSprite), stack, light, overlay);
    }

    public @Nullable PerspectiveModelState getModelState() {
        return (PerspectiveModelState)this.parentState;
    }

    public boolean isCosmic() {
        return true;
    }
}