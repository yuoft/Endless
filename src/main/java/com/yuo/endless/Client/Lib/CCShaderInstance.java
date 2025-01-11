package com.yuo.endless.Client.Lib;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.shaders.Shader;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CCShaderInstance extends ShaderInstance {
    final List<Runnable> applyCallbacks = new LinkedList<>();

    public CCShaderInstance(ResourceProvider resourceProvider, ResourceLocation loc, VertexFormat format) throws IOException {
        super(resourceProvider, loc, format);
    }

    public void onApply(Runnable callback) {
        this.applyCallbacks.add(callback);
    }

    public void apply() {
        for (Runnable callback : this.applyCallbacks)
            callback.run();
        super.apply();
    }

    public void parseUniformNode(@NotNull JsonElement json) throws ChainedJsonException {
        JsonObject obj = GsonHelper.convertToJsonObject(json, "uniform");
        String name = GsonHelper.getAsString(obj, "name");
        String typeStr = GsonHelper.getAsString(obj, "type");
        UniformType type = UniformType.parse(typeStr);
        if (type == null)
            throw new ChainedJsonException("");
        int count = GsonHelper.getAsInt(obj, "count");
        type = switch (type) {
            case INT -> switch (count) {
                case 2 -> UniformType.VEC2;
                case 3 -> UniformType.VEC3;
                case 4 -> UniformType.VEC4;
                default -> type;
            };
            case U_INT -> switch (count) {
                case 2 -> UniformType.I_VEC2;
                case 3 -> UniformType.I_VEC3;
                case 4 -> UniformType.I_VEC4;
                default -> type;
            };
            case FLOAT -> switch (count) {
                case 2 -> UniformType.U_VEC2;
                case 3 -> UniformType.U_VEC3;
                case 4 -> UniformType.U_VEC4;
                default -> type;
            };
            default -> type;
        };
        CCUniform uniform = CCUniform.makeUniform(name, type, count, (Shader)this);
        JsonArray jsonValues = GsonHelper.getAsJsonArray(obj, "values");
        if (jsonValues.size() != count && jsonValues.size() > 1)
            throw new ChainedJsonException("");
        switch (type.getCarrier()) {
            case INT:
            case U_INT:
                uniform.glUniformI(parseInts(count, jsonValues));
                break;
            case FLOAT:
            case MATRIX:
                uniform.glUniformF(false, parseFloats(count, jsonValues));
                break;
            case DOUBLE:
            case D_MATRIX:
                uniform.glUniformD(false, parseDoubles(count, jsonValues));
                break;
        }
        this.uniforms.add(uniform);
    }

    static float[] parseFloats(int count, JsonArray jsonValues) throws ChainedJsonException {
        int i = 0;
        float[] values = new float[Math.max(count, 16)];
        for (JsonElement jsonValue : jsonValues) {
            try {
                values[i++] = GsonHelper.convertToFloat(jsonValue, "value");
            } catch (Exception exception) {
                throw new ChainedJsonException("endless shader error");
            }
        }
        if (count > 1 && jsonValues.size() == 1)
            Arrays.fill(values, 1, values.length, values[0]);
        return Arrays.copyOfRange(values, 0, count);
    }

    static int[] parseInts(int count, JsonArray jsonValues) throws ChainedJsonException {
        int i = 0;
        int[] values = new int[Math.max(count, 16)];
        for (JsonElement jsonValue : jsonValues) {
            try {
                values[i++] = GsonHelper.convertToInt(jsonValue, "value");
            } catch (Exception exception) {
                throw new ChainedJsonException("endless shader error");
            }
        }
        if (count > 1 && jsonValues.size() == 1)
            Arrays.fill(values, 1, values.length, values[0]);
        return Arrays.copyOfRange(values, 0, count);
    }

    static double[] parseDoubles(int count, JsonArray jsonValues) throws ChainedJsonException {
        int i = 0;
        double[] values = new double[Math.max(count, 16)];
        for (JsonElement jsonValue : jsonValues) {
            try {
                values[i++] = GsonHelper.convertToDouble(jsonValue, "value");
            } catch (Exception exception) {
                throw new ChainedJsonException("endless shader error");
            }
        }
        if (count > 1 && jsonValues.size() == 1)
            Arrays.fill(values, 1, values.length, values[0]);
        return Arrays.copyOfRange(values, 0, count);
    }
}
