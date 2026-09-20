package com.yuo.endless.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.client.model.HaloBakedModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HaloBakedModel.class, remap = false)
public abstract class EXEEndlessHaloBakedModelMixin {
    @Shadow
    @Final
    private BakedQuad haloQuad;

    @Inject(method = "renderItem", at = @At("HEAD"))
    private void exe_markHaloSpriteActive(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource source, int packedLight, int packedOverlay, CallbackInfo ci) {
        if (this.haloQuad == null) {
            return;
        }

        TextureAtlasSprite sprite = this.haloQuad.getSprite();
        Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(sprite.contents().name());
    }
}
