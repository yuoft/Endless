package com.yuo.endless.Client.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.yuo.endless.Client.Lib.CachedFormat;
import com.yuo.endless.Client.Lib.Quad;
import com.yuo.endless.Client.Model.HaloItemModelLoader.HaloItemModelGeometry;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class HaloItemModelLoader implements IModelLoader<HaloItemModelGeometry> {
    public void onResourceManagerReload(@NotNull ResourceManager r) {}

    @NotNull
    public HaloItemModelGeometry read(@NotNull JsonDeserializationContext json, JsonObject object) {
        JsonObject halo = object.getAsJsonObject("halo");
        if (halo == null)
            throw new IllegalStateException();
        IntArrayList intArrayList = new IntArrayList();
        JsonArray layerColorsArr = object.getAsJsonArray("layerColors");
        if (layerColorsArr != null) {
            for (JsonElement jsonElement : layerColorsArr) {
                intArrayList.add(jsonElement.getAsInt());
            }
        }
        String texture = GsonHelper.getAsString(halo, "texture");
        int color = GsonHelper.getAsInt(halo, "color");
        int size = GsonHelper.getAsInt(halo, "size");
        boolean pulse = GsonHelper.getAsBoolean(halo, "pulse");
        JsonObject clean = object.getAsJsonObject();
        clean.remove("halo");
        clean.remove("loader");
        BlockModel baseModel = json.deserialize(clean, BlockModel.class);
        return new HaloItemModelGeometry(baseModel, intArrayList, texture, color, size, pulse);
    }

    public static class HaloItemModelGeometry implements IModelGeometry<HaloItemModelGeometry> {
        BlockModel baseModel;

        IntList layerColors;

        String texture;

        int color;

        int size;

        boolean pulse;

        Material haloMaterial;

        public HaloItemModelGeometry(BlockModel baseModel, IntList layerColors, String texture, int color, int size, boolean pulse) {
            this.baseModel = baseModel;
            this.layerColors = layerColors;
            this.texture = texture;
            this.color = color;
            this.size = size;
            this.pulse = pulse;
        }

        public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> sprite, ModelState model, ItemOverrides overrides, ResourceLocation id) {
            BakedModel bakedBaseModel = this.baseModel.bake(bakery, this.baseModel, sprite, model, id, false);
            return new HaloBakedModel(tintLayers(bakedBaseModel, this.layerColors), sprite.apply(this.haloMaterial), this.color, this.size, this.pulse);
        }

        public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> model, Set<Pair<String, String>> string) {
            Set<Material> materials = new HashSet<>();
            this.haloMaterial = owner.resolveTexture(this.texture);
            if (Objects.equals(this.haloMaterial.atlasLocation(), MissingTextureAtlasSprite.getLocation()))
                string.add(Pair.of(this.texture, owner.getModelName()));
            materials.add(this.haloMaterial);
            materials.addAll(this.baseModel.getMaterials(model, string));
            return materials;
        }

        static BakedModel tintLayers(BakedModel model, IntList layerColors) {
            if (layerColors.isEmpty())
                return model;
            Map<Direction, List<BakedQuad>> faceQuads = new HashMap<>();
            Direction[] var3 = Direction.values();
            for (Direction face : var3) {
                faceQuads.put(face, transformQuads(model.getQuads(null, face, new Random(), EmptyModelData.INSTANCE), layerColors));
            }
            List<BakedQuad> unculled = transformQuads(model.getQuads(null, null, new Random(), EmptyModelData.INSTANCE), layerColors);
            return new SimpleBakedModel(unculled, faceQuads, model.isGui3d(), model.isCustomRenderer(), model.useAmbientOcclusion(), model.getParticleIcon(), model.getTransforms(), ItemOverrides.EMPTY);
        }

        static List<BakedQuad> transformQuads(List<BakedQuad> quads, IntList layerColors) {
            List<BakedQuad> newQuads = new ArrayList<>(quads.size());
            for (BakedQuad quad : quads) {
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
                quad.pipe(newQuad);
                float r = (tint >> 16 & 0xFF) / 255.0F;
                float g = (tint >> 8 & 0xFF) / 255.0F;
                float b = (tint & 0xFF) / 255.0F;
                Quad.Vertex[] var8 = newQuad.vertices;
                for (Quad.Vertex v : var8) {
                    float[] var10000 = v.color;
                    var10000[0] = var10000[0] * r;
                    var10000[1] = var10000[1] * g;
                    var10000[2] = var10000[2] * b;
                }
                newQuad.tintIndex = -1;
                return newQuad.bake();
            }
            return quad;
        }
    }
}