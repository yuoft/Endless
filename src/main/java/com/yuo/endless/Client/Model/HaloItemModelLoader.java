package com.yuo.endless.Client.Model;

import com.google.gson.*;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.yuo.endless.Client.Lib.CachedFormat;
import com.yuo.endless.Client.Lib.IVertexConsumer;
import com.yuo.endless.Client.Lib.Quad;
import com.yuo.endless.Client.Lib.VertexUtils;
import com.yuo.endless.Client.Model.HaloItemModelLoader.HaloItemModelGeometry;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class HaloItemModelLoader implements IGeometryLoader<HaloItemModelGeometry> {
    public static final HaloItemModelLoader INSTANCE = new HaloItemModelLoader();

    public HaloItemModelLoader() {
    }

    public HaloItemModelLoader.HaloItemModelGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) throws JsonParseException {
        JsonObject halo = modelContents.getAsJsonObject("halo");
        if (halo == null) {
            throw new IllegalStateException("Missing 'halo' object.");
        } else {
            IntArrayList layerColors = new IntArrayList();
            JsonArray layerColorsArr = modelContents.getAsJsonArray("layerColors");
            if (layerColorsArr != null) {

                for (JsonElement jsonElement : layerColorsArr) {
                    layerColors.add(jsonElement.getAsInt());
                }
            }

            String texture = GsonHelper.getAsString(halo, "texture");
            int color = GsonHelper.getAsInt(halo, "color");
            int size = GsonHelper.getAsInt(halo, "size");
            boolean pulse = GsonHelper.getAsBoolean(halo, "pulse");
            JsonObject clean = modelContents.getAsJsonObject();
            clean.remove("halo");
            clean.remove("loader");
            BlockModel baseModel = deserializationContext.deserialize(clean, BlockModel.class);
            return new HaloItemModelLoader.HaloItemModelGeometry(baseModel, layerColors, texture, color, size, pulse);
        }
    }

    public static class HaloItemModelGeometry implements IUnbakedGeometry<HaloItemModelLoader.HaloItemModelGeometry> {
        private static final ConcurrentMap<org.apache.commons.lang3.tuple.Pair<VertexFormat, VertexFormat>, int[]> formatMaps = new ConcurrentHashMap<>();
        private static final int[] DEFAULT_MAPPING;
        private final BlockModel baseModel;
        private final IntList layerColors;
        private final String texture;
        private final int color;
        private final int size;
        private final boolean pulse;

        public HaloItemModelGeometry(BlockModel baseModel, IntList layerColors, String texture, int color, int size, boolean pulse) {
            this.baseModel = baseModel;
            this.layerColors = layerColors;
            this.texture = texture;
            this.color = color;
            this.size = size;
            this.pulse = pulse;
        }

        private static BakedModel tintLayers(BakedModel model, IntList layerColors) {
            if (layerColors.isEmpty()) {
                return model;
            } else {
                Map<Direction, List<BakedQuad>> faceQuads = new HashMap<>();
                Direction[] var3 = Direction.values();

                for (Direction face : var3) {
                    faceQuads.put(face, transformQuads(model.getQuads(null, face, RandomSource.create()), layerColors));
                }

                List<BakedQuad> unculled = transformQuads(model.getQuads(null, null, RandomSource.create()), layerColors);
                return new SimpleBakedModel(unculled, faceQuads, model.useAmbientOcclusion(), model.usesBlockLight(), model.isGui3d(), model.getParticleIcon(), model.getTransforms(), ItemOverrides.EMPTY, RenderTypeGroup.EMPTY, RenderTypeGroup.EMPTY);
            }
        }

        static List<BakedQuad> transformQuads(List<BakedQuad> quads, IntList layerColors) {
            ArrayList<BakedQuad> newQuads = new ArrayList<>(quads.size());

            for (BakedQuad quad : quads) {
                newQuads.add(transformQuad(quad, layerColors));
            }

            return newQuads;
        }

        public static int[] mapFormats(VertexFormat from, VertexFormat to) {
            return from.equals(DefaultVertexFormat.BLOCK) && to.equals(DefaultVertexFormat.BLOCK) ? DEFAULT_MAPPING : formatMaps.computeIfAbsent(Pair.of(from, to), (pair) -> generateMapping(pair.getLeft(), pair.getRight()));
        }

        public static void unpack(int[] from, float[] to, VertexFormat formatFrom, int v, int e) {
            VertexUtils.unpack(from, to, formatFrom, v, e);

        }

        private static int[] generateMapping(VertexFormat from, VertexFormat to) {
            int fromCount = from.getElements().size();
            int toCount = to.getElements().size();
            int[] eMap = new int[fromCount];

            for(int e = 0; e < fromCount; ++e) {
                VertexFormatElement expected = from.getElements().get(e);

                int e2;
                for(e2 = 0; e2 < toCount; ++e2) {
                    VertexFormatElement current = to.getElements().get(e2);
                    if (expected.getUsage() == current.getUsage() && expected.getIndex() == current.getIndex()) {
                        break;
                    }
                }

                eMap[e] = e2;
            }

            return eMap;
        }

        public static void putBakedQuad(IVertexConsumer consumer, BakedQuad quad) {
            consumer.setTexture(quad.getSprite());
            consumer.setQuadOrientation(quad.getDirection());
            if (quad.isTinted()) {
                consumer.setQuadTint(quad.getTintIndex());
            }

            consumer.setApplyDiffuseLighting(quad.isShade());
            float[] data = new float[4];
            VertexFormat formatFrom = consumer.getVertexFormat();
            VertexFormat formatTo = DefaultVertexFormat.BLOCK;
            int countFrom = formatFrom.getElements().size();
            int countTo = formatTo.getElements().size();
            int[] eMap = mapFormats(formatFrom, formatTo);

            for(int v = 0; v < 4; ++v) {
                for(int e = 0; e < countFrom; ++e) {
                    if (eMap[e] != countTo) {
                        unpack(quad.getVertices(), data, formatTo, v, eMap[e]);
                        consumer.put(e, data);
                    } else {
                        consumer.put(e);
                    }
                }
            }

        }

        static BakedQuad transformQuad(BakedQuad quad, IntList layerColors) {
            int tintIndex = quad.getTintIndex();
            if (tintIndex != -1 && tintIndex < layerColors.size()) {
                int tint = layerColors.getInt(tintIndex);
                if (tint == -1) {
                    return quad;
                } else {
                    Quad newQuad = new Quad();
                    newQuad.reset(CachedFormat.BLOCK);
                    putBakedQuad(newQuad, quad);
                    float r = (float)(tint >> 16 & 255) / 255.0F;
                    float g = (float)(tint >> 8 & 255) / 255.0F;
                    float b = (float)(tint & 255) / 255.0F;
                    Quad.Vertex[] var8 = newQuad.vertices;

                    for (Quad.Vertex v : var8) {
                        float[] var10000 = v.color;
                        var10000[0] *= r;
                        var10000[1] *= g;
                        var10000[2] *= b;
                    }

                    newQuad.tintIndex = -1;
                    return newQuad.bake();
                }
            } else {
                return quad;
            }
        }

        public BakedModel bake(IGeometryBakingContext owner, ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
            BakedModel bakedBaseModel = this.baseModel.bake(bakery, this.baseModel, spriteGetter, modelTransform, modelLocation, false);
            Material particleLocation = this.baseModel.getMaterial(this.texture);
            TextureAtlasSprite particle = spriteGetter.apply(particleLocation);
            return new HaloBakedModel(tintLayers(bakedBaseModel, this.layerColors), particle, this.color, this.size, this.pulse);
        }

        public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
            this.baseModel.resolveParents(modelGetter);
        }

        static {
            DEFAULT_MAPPING = generateMapping(DefaultVertexFormat.BLOCK, DefaultVertexFormat.BLOCK);
        }
    }
}