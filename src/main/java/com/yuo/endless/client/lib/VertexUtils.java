package com.yuo.endless.client.lib;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.blaze3d.vertex.VertexFormatElement.Type;
import com.mojang.blaze3d.vertex.VertexFormatElement.Usage;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VertexUtils {
    private static final ConcurrentMap<Pair<VertexFormat, VertexFormat>, int[]> formatMaps = new ConcurrentHashMap<>();
    private static final int[] DEFAULT_MAPPING;

    public VertexUtils() {
    }

    public static int[] mapFormats(VertexFormat from, VertexFormat to) {
        return from.equals(DefaultVertexFormat.BLOCK) && to.equals(DefaultVertexFormat.BLOCK) ? DEFAULT_MAPPING : formatMaps.computeIfAbsent(Pair.of(from, to), (pair) -> generateMapping(pair.getLeft(), pair.getRight()));
    }

    public static void putQuad(IVertexConsumer consumer, BakedQuad quad) {
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

    public static void unpack(int[] from, float[] to, VertexFormat formatFrom, int v, int e) {
        int length = Math.min(4, to.length);
        VertexFormatElement element = formatFrom.getElements().get(e);
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

    public static void pack(float[] from, int[] to, VertexFormat formatTo, int v, int e) {
        VertexFormatElement element = formatTo.getElements().get(e);
        int vertexStart = v * formatTo.getVertexSize() + formatTo.getOffset(e);
        int count = element.getElementCount();
        VertexFormatElement.Type type = element.getType();
        int size = type.getSize();
        int mask = (256 << 8 * (size - 1)) - 1;

        for(int i = 0; i < 4; ++i) {
            if (i < count) {
                int pos = vertexStart + size * i;
                int index = pos >> 2;
                int offset = pos & 3;
                float f = i < from.length ? from[i] : 0.0F;
                int bits;
                if (type == Type.FLOAT) {
                    bits = Float.floatToRawIntBits(f);
                } else if (type != Type.UBYTE && type != Type.USHORT && type != Type.UINT) {
                    bits = Math.round(f * (float)(mask >> 1));
                } else {
                    bits = Math.round(f * (float)mask);
                }

                to[index] &= ~(mask << offset * 8);
                to[index] |= (bits & mask) << offset * 8;
            }
        }

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

    static {
        DEFAULT_MAPPING = generateMapping(DefaultVertexFormat.BLOCK, DefaultVertexFormat.BLOCK);
    }
}
