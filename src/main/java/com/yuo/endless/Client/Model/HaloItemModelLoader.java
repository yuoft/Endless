package com.yuo.endless.Client.Model;

import com.google.gson.*;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.blaze3d.vertex.VertexFormatElement.Type;
import com.mojang.blaze3d.vertex.VertexFormatElement.Usage;
import com.yuo.endless.Client.Lib.CachedFormat;
import com.yuo.endless.Client.Lib.IVertexConsumer;
import com.yuo.endless.Client.Lib.Quad;
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
                Iterator var6 = layerColorsArr.iterator();

                while(var6.hasNext()) {
                    JsonElement jsonElement = (JsonElement)var6.next();
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
        private static final ConcurrentMap<org.apache.commons.lang3.tuple.Pair<VertexFormat, VertexFormat>, int[]> formatMaps = new ConcurrentHashMap();
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
                Map<Direction, List<BakedQuad>> faceQuads = new HashMap();
                Direction[] var3 = Direction.values();
                int var4 = var3.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    Direction face = var3[var5];
                    faceQuads.put(face, transformQuads(model.getQuads(null, face, RandomSource.create()), layerColors));
                }

                List<BakedQuad> unculled = transformQuads(model.getQuads(null, null, RandomSource.create()), layerColors);
                return new SimpleBakedModel(unculled, faceQuads, model.useAmbientOcclusion(), model.usesBlockLight(), model.isGui3d(), model.getParticleIcon(), model.getTransforms(), ItemOverrides.EMPTY, RenderTypeGroup.EMPTY);
            }
        }

        static List<BakedQuad> transformQuads(List<BakedQuad> quads, IntList layerColors) {
            ArrayList<BakedQuad> newQuads = new ArrayList(quads.size());
            Iterator var3 = quads.iterator();

            while(var3.hasNext()) {
                BakedQuad quad = (BakedQuad)var3.next();
                newQuads.add(transformQuad(quad, layerColors));
            }

            return newQuads;
        }

        public static int[] mapFormats(VertexFormat from, VertexFormat to) {
            return from.equals(DefaultVertexFormat.BLOCK) && to.equals(DefaultVertexFormat.BLOCK) ? DEFAULT_MAPPING : (int[])formatMaps.computeIfAbsent(Pair.of(from, to), (pair) -> {
                return generateMapping((VertexFormat)pair.getLeft(), (VertexFormat)pair.getRight());
            });
        }

        public static void unpack(int[] from, float[] to, VertexFormat formatFrom, int v, int e) {
            int length = Math.min(4, to.length);
            VertexFormatElement element = (VertexFormatElement)formatFrom.getElements().get(e);
            int vertexStart = v * formatFrom.getVertexSize() + formatFrom.getOffset(e);
            int count = element.getElementCount();
            VertexFormatElement.Type type = element.getType();
            VertexFormatElement.Usage usage = element.getUsage();
            int size = type.getSize();
            int mask = (256 << 8 * (size - 1)) - 1;

            for(int i = 0; i < length; ++i) {
                if (i < count) {
                    int pos = vertexStart + size * i;
                    int index = pos >> 2;
                    int offset = pos & 3;
                    int bits = from[index];
                    bits >>>= offset * 8;
                    if ((pos + size - 1) / 4 != index) {
                        bits |= from[index + 1] << (4 - offset) * 8;
                    }

                    bits &= mask;
                    if (type == Type.FLOAT) {
                        to[i] = Float.intBitsToFloat(bits);
                    } else if (type != Type.UBYTE && type != Type.USHORT) {
                        if (type == Type.UINT) {
                            to[i] = (float)((double)((long)bits & 4294967295L) / 4.294967295E9);
                        } else if (type == Type.BYTE) {
                            to[i] = (float)((byte)bits) / (float)(mask >> 1);
                        } else if (type == Type.SHORT) {
                            to[i] = (float)((short)bits) / (float)(mask >> 1);
                        } else if (type == Type.INT) {
                            to[i] = (float)((double)((long)bits & 4294967295L) / 2.147483647E9);
                        }
                    } else {
                        to[i] = (float)bits / (float)mask;
                    }
                } else {
                    to[i] = i == 3 && usage == Usage.POSITION ? 1.0F : 0.0F;
                }
            }

        }

        private static int[] generateMapping(VertexFormat from, VertexFormat to) {
            int fromCount = from.getElements().size();
            int toCount = to.getElements().size();
            int[] eMap = new int[fromCount];

            for(int e = 0; e < fromCount; ++e) {
                VertexFormatElement expected = (VertexFormatElement)from.getElements().get(e);

                int e2;
                for(e2 = 0; e2 < toCount; ++e2) {
                    VertexFormatElement current = (VertexFormatElement)to.getElements().get(e2);
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
                        consumer.put(e, new float[0]);
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
                    int var9 = var8.length;

                    for(int var10 = 0; var10 < var9; ++var10) {
                        Quad.Vertex v = var8[var10];
                        float[] var10000 = v.color;
                        var10000[0] *= r;
                        var10000 = v.color;
                        var10000[1] *= g;
                        var10000 = v.color;
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
            TextureAtlasSprite particle = (TextureAtlasSprite)spriteGetter.apply(particleLocation);
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