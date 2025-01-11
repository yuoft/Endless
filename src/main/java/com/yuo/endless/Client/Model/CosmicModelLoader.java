package com.yuo.endless.Client.Model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.yuo.endless.Client.Model.CosmicModelLoader.CosmicModelGeometry;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class CosmicModelLoader implements IModelLoader<CosmicModelGeometry> {
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {}

    @NotNull
    public CosmicModelGeometry read(@NotNull JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        JsonObject cosmicObj = modelContents.getAsJsonObject("cosmic");
        if (cosmicObj == null)
            throw new IllegalStateException();
        else {
            String maskTexture = GsonHelper.getAsString(cosmicObj, "mask");
            JsonObject clean = modelContents.getAsJsonObject();
            clean.remove("cosmic");
            clean.remove("loader");
            BlockModel baseModel = deserializationContext.deserialize(clean, BlockModel.class);
            return new CosmicModelGeometry(baseModel, maskTexture);
        }
    }

    public static class CosmicModelGeometry implements IModelGeometry<CosmicModelGeometry> {
        BlockModel baseModel;

        String maskTexture;

        Material maskMaterial;

        public CosmicModelGeometry(BlockModel baseModel, String maskTexture) {
            this.baseModel = baseModel;
            this.maskTexture = maskTexture;
        }

        public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
            BakedModel baseBakedModel = this.baseModel.bake(bakery, this.baseModel, spriteGetter, modelTransform, modelLocation, true);
            return new CosmicBakedModel(baseBakedModel, spriteGetter.apply(this.maskMaterial));
        }

        public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
            Set<Material> materials = new HashSet<>();
            this.maskMaterial = owner.resolveTexture(this.maskTexture);
            if (Objects.equals(this.maskMaterial.atlasLocation(), MissingTextureAtlasSprite.getLocation()))
                missingTextureErrors.add(Pair.of(this.maskTexture, owner.getModelName()));
            materials.add(this.maskMaterial);
            materials.addAll(this.baseModel.getMaterials(modelGetter, missingTextureErrors));
            return materials;
        }
    }
}
