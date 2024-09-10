package com.yuo.endless.Client.Model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.yuo.endless.Client.Model.CosmicModelLoader.CosmicModelGeometry;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class CosmicModelLoader implements IModelLoader<CosmicModelGeometry> {
    public void onResourceManagerReload(IResourceManager resourceManager) {}

    public CosmicModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        JsonObject cosmicObj = modelContents.getAsJsonObject("cosmic");
        if (cosmicObj == null)
            throw new IllegalStateException();
        else {
//            List<String> maskTexture = new ArrayList<>();
//            if (cosmicObj.has("mask") && cosmicObj.get("mask").isJsonArray()){
//                JsonArray masks = cosmicObj.getAsJsonArray("mask");
//                for (int i = 0; i < masks.size(); i++){
//                    maskTexture.add(masks.get(i).getAsString());
//                }
//            } else {
//                maskTexture.add(JSONUtils.getString(cosmicObj, "mask"));
//            }
            String maskTexture = JSONUtils.getString(cosmicObj, "mask");
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

        RenderMaterial maskMaterial;

        public CosmicModelGeometry(BlockModel baseModel, String maskTexture) {
            this.baseModel = baseModel;
            this.maskTexture = maskTexture;
        }

        public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
            IBakedModel baseBakedModel = this.baseModel.bakeModel(bakery, this.baseModel, spriteGetter, modelTransform, modelLocation, true);
            return new CosmicBakedModel(baseBakedModel, spriteGetter.apply(this.maskMaterial));
        }

        public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
            Set<RenderMaterial> materials = new HashSet<>();
            this.maskMaterial = owner.resolveTexture(this.maskTexture);
            if (Objects.equals(this.maskMaterial.getTextureLocation(), MissingTextureSprite.getDynamicTexture()))
                missingTextureErrors.add(Pair.of(this.maskTexture, owner.getModelName()));
            materials.add(this.maskMaterial);
            materials.addAll(this.baseModel.getTextures(modelGetter, missingTextureErrors));
            return materials;
        }
    }
}
