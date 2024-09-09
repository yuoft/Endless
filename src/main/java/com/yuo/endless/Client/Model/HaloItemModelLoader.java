package com.yuo.endless.Client.Model;

import codechicken.lib.model.CachedFormat;
import codechicken.lib.model.Quad;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.yuo.endless.Client.Model.HaloItemModelLoader.HaloItemModelGeometry;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;

import java.util.*;
import java.util.function.Function;

public class HaloItemModelLoader implements IModelLoader<HaloItemModelGeometry> {
    public void onResourceManagerReload(IResourceManager r) {}

    public HaloItemModelGeometry read(JsonDeserializationContext json, JsonObject object) {
        JsonObject halo = object.getAsJsonObject("halo");
        if (halo == null)
            throw new IllegalStateException();
        IntArrayList intArrayList = new IntArrayList();
        JsonArray layerColorsArr = object.getAsJsonArray("layerColors");
        if (layerColorsArr != null) {
            Iterator<JsonElement> var6 = layerColorsArr.iterator();
            while (var6.hasNext()) {
                JsonElement jsonElement = var6.next();
                intArrayList.add(jsonElement.getAsInt());
            }
        }
        String texture = JSONUtils.getString(halo, "texture");
        int color = JSONUtils.getInt(halo, "color");
        int size = JSONUtils.getInt(halo, "size");
        boolean pulse = JSONUtils.getBoolean(halo, "pulse");
        JsonObject clean = object.getAsJsonObject();
        clean.remove("halo");
        clean.remove("loader");
        BlockModel baseModel = (BlockModel)json.deserialize((JsonElement)clean, BlockModel.class);
        return new HaloItemModelGeometry(baseModel, (IntList)intArrayList, texture, color, size, pulse);
    }

    public static class HaloItemModelGeometry implements IModelGeometry<HaloItemModelGeometry> {
        BlockModel baseModel;

        IntList layerColors;

        String texture;

        int color;

        int size;

        boolean pulse;

        RenderMaterial haloMaterial;

        public HaloItemModelGeometry(BlockModel baseModel, IntList layerColors, String texture, int color, int size, boolean pulse) {
            this.baseModel = baseModel;
            this.layerColors = layerColors;
            this.texture = texture;
            this.color = color;
            this.size = size;
            this.pulse = pulse;
        }

        public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> sprite, IModelTransform model, ItemOverrideList overrides, ResourceLocation id) {
            IBakedModel bakedBaseModel = this.baseModel.bakeModel(bakery, this.baseModel, sprite, model, id, false);
            return new HaloBakedModel(tintLayers(bakedBaseModel, this.layerColors), sprite.apply(this.haloMaterial), this.color, this.size, this.pulse);
        }

        public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> model, Set<Pair<String, String>> string) {
            Set<RenderMaterial> materials = new HashSet<>();
            this.haloMaterial = owner.resolveTexture(this.texture);
            if (Objects.equals(this.haloMaterial.getTextureLocation(), MissingTextureSprite.getDynamicTexture()))
                string.add(Pair.of(this.texture, owner.getModelName()));
            materials.add(this.haloMaterial);
            materials.addAll(this.baseModel.getTextures(model, string));
            return materials;
        }

        static IBakedModel tintLayers(IBakedModel model, IntList layerColors) {
            if (layerColors.isEmpty())
                return model;
            Map<Direction, List<BakedQuad>> faceQuads = new HashMap<>();
            Direction[] var3 = Direction.values();
            int var4 = var3.length;
            for (int var5 = 0; var5 < var4; var5++) {
                Direction face = var3[var5];
                faceQuads.put(face, transformQuads(model.getQuads(null, face, new Random(), (IModelData) EmptyModelData.INSTANCE), layerColors));
            }
            List<BakedQuad> unculled = transformQuads(model.getQuads(null, null, new Random(), (IModelData)EmptyModelData.INSTANCE), layerColors);
            return new SimpleBakedModel(unculled, faceQuads, model.isGui3d(), model.isBuiltInRenderer(), model.isAmbientOcclusion(), model.getParticleTexture(), model.getItemCameraTransforms(), ItemOverrideList.EMPTY);
        }

        static List<BakedQuad> transformQuads(List<BakedQuad> quads, IntList layerColors) {
            List<BakedQuad> newQuads = new ArrayList<>(quads.size());
            Iterator<BakedQuad> var3 = quads.iterator();
            while (var3.hasNext()) {
                BakedQuad quad = var3.next();
                newQuads.add(transformQuad(quad, layerColors));
            }
            return newQuads;
        }

        static BakedQuad transformQuad(BakedQuad quad, IntList layerColors) {
            int tintIndex = quad.getTintIndex();
            if (tintIndex != -1 && tintIndex < layerColors.size()) {
                int tint = layerColors.getInt(tintIndex);
                if (tint == -1)
                    return quad;
                Quad newQuad = new Quad();
                newQuad.reset(CachedFormat.BLOCK);
                quad.pipe((IVertexConsumer)newQuad);
                float r = (tint >> 16 & 0xFF) / 255.0F;
                float g = (tint >> 8 & 0xFF) / 255.0F;
                float b = (tint & 0xFF) / 255.0F;
                Quad.Vertex[] var8 = newQuad.vertices;
                int var9 = var8.length;
                for (int var10 = 0; var10 < var9; var10++) {
                    Quad.Vertex v = var8[var10];
                    float[] var10000 = v.color;
                    var10000[0] = var10000[0] * r;
                    var10000 = v.color;
                    var10000[1] = var10000[1] * g;
                    var10000 = v.color;
                    var10000[2] = var10000[2] * b;
                }
                newQuad.tintIndex = -1;
                return newQuad.bake();
            }
            return quad;
        }
    }
}