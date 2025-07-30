package com.yuo.endless.Client.Lib;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.shaders.Program;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CCShaderInstance extends ShaderInstance {
    private final List<Runnable> applyCallbacks = new LinkedList();

    protected CCShaderInstance(ResourceProvider resourceProvider, ResourceLocation loc, VertexFormat format) throws IOException {
        super(resourceProvider, loc, format);
    }

    public static CCShaderInstance create(ResourceProvider resourceProvider, ResourceLocation loc, VertexFormat format) {
        try {
            return new CCShaderInstance(resourceProvider, loc, format);
        } catch (IOException var4) {
            IOException ex = var4;
            throw new RuntimeException("Failed to initialize shader.", ex);
        }
    }

    public void onApply(Runnable callback) {
        this.applyCallbacks.add(callback);
    }

    public void apply() {
        Iterator var1 = this.applyCallbacks.iterator();

        while(var1.hasNext()) {
            Runnable callback = (Runnable)var1.next();
            callback.run();
        }

        super.apply();
    }

    public @Nullable CCUniform getUniform(String name) {
        return (CCUniform)super.getUniform(name);
    }

    protected void parseUniformNode(JsonElement json) throws ChainedJsonException {
        JsonObject obj = GsonHelper.convertToJsonObject(json, "uniform");
        String name = GsonHelper.getAsString(obj, "name");
        String typeStr = GsonHelper.getAsString(obj, "type");
        UniformType type = UniformType.parse(typeStr);
        if (type == null) {
            throw new ChainedJsonException("Invalid type '%s'. See UniformType enum. All vanilla types supported.".formatted(typeStr));
        } else {
            int count;
            count = GsonHelper.getAsInt(obj, "count");
            label45:
            switch (type) {
                case FLOAT:
                    switch (count) {
                        case 2 -> {
                            type = UniformType.VEC2;
                            break label45;
                        }
                        case 3 -> {
                            type = UniformType.VEC3;
                            break label45;
                        }
                        case 4 -> type = UniformType.VEC4;
                        default -> { }
                    }
                case INT:
                    switch (count) {
                        case 2 -> {
                            type = UniformType.I_VEC2;
                            break label45;
                        }
                        case 3 -> {
                            type = UniformType.I_VEC3;
                            break label45;
                        }
                        case 4 -> type = UniformType.I_VEC4;
                        default -> { }
                    }
                case U_INT:
                    switch (count) {
                        case 2 -> type = UniformType.U_VEC2;
                        case 3 -> type = UniformType.U_VEC3;
                        case 4 -> type = UniformType.U_VEC4;
                    }
            }

            CCUniform uniform = CCUniform.makeUniform(name, type, count, this);
            JsonArray jsonValues = GsonHelper.getAsJsonArray(obj, "values");
            if (jsonValues.size() != count && jsonValues.size() > 1) {
                throw new ChainedJsonException("Invalid amount of values specified (expected " + count + ", found " + jsonValues.size() + ")");
            } else {
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
                }

                this.uniforms.add(uniform);
            }
        }
    }

    public Program compileProgram(ResourceProvider resourceProvider, Program.Type programType, ResourceLocation loc) throws IOException {
        String var10002 = loc.getNamespace();
        String var10003 = loc.getPath();
        ResourceLocation adjustedLoc = new ResourceLocation(var10002, "shaders/core/" + var10003 + programType.getExtension());
        String cacheString = "ccl__" + adjustedLoc;
        Program program = (Program)programType.getPrograms().get(cacheString);
        if (program != null) {
            return program;
        } else {
            GlslProcessor.ProcessedShader processedShader = (new GlslProcessor(resourceProvider, adjustedLoc)).process();
            int id = GL20.glCreateShader(programType.getGlType());
            GL20.glShaderSource(id, processedShader.processedSource());
            GL20.glCompileShader(id);
            if (GL20.glGetShaderi(id, 35713) == 0) {
                String s1 = GL20.glGetShaderInfoLog(id);
                var10002 = programType.getName();
                throw new IOException("Couldn't compile " + var10002 + " program (" + processedShader.sourceName() + ", " + adjustedLoc + ") : " + s1);
            } else {
                program = new Program(programType, id, cacheString);
                programType.getPrograms().put(cacheString, program);
                return program;
            }
        }
    }

    private static float[] parseFloats(int count, JsonArray jsonValues) throws ChainedJsonException {
        int i = 0;
        float[] values = new float[Math.max(count, 16)];
        Iterator var4 = jsonValues.iterator();

        while(var4.hasNext()) {
            JsonElement jsonValue = (JsonElement)var4.next();

            try {
                values[i++] = GsonHelper.convertToFloat(jsonValue, "value");
            } catch (Exception var8) {
                Exception ex = var8;
                ChainedJsonException chainedjsonexception = ChainedJsonException.forException(ex);
                chainedjsonexception.prependJsonKey("values[" + i + "]");
                throw chainedjsonexception;
            }
        }

        if (count > 1 && jsonValues.size() == 1) {
            Arrays.fill(values, 1, values.length, values[0]);
        }

        return Arrays.copyOfRange(values, 0, count);
    }

    private static int[] parseInts(int count, JsonArray jsonValues) throws ChainedJsonException {
        int i = 0;
        int[] values = new int[Math.max(count, 16)];
        Iterator var4 = jsonValues.iterator();

        while(var4.hasNext()) {
            JsonElement jsonValue = (JsonElement)var4.next();

            try {
                values[i++] = GsonHelper.convertToInt(jsonValue, "value");
            } catch (Exception var8) {
                Exception ex = var8;
                ChainedJsonException chainedjsonexception = ChainedJsonException.forException(ex);
                chainedjsonexception.prependJsonKey("values[" + i + "]");
                throw chainedjsonexception;
            }
        }

        if (count > 1 && jsonValues.size() == 1) {
            Arrays.fill(values, 1, values.length, values[0]);
        }

        return Arrays.copyOfRange(values, 0, count);
    }

    private static double[] parseDoubles(int count, JsonArray jsonValues) throws ChainedJsonException {
        int i = 0;
        double[] values = new double[Math.max(count, 16)];
        Iterator var4 = jsonValues.iterator();

        while(var4.hasNext()) {
            JsonElement jsonValue = (JsonElement)var4.next();

            try {
                values[i++] = GsonHelper.convertToDouble(jsonValue, "value");
            } catch (Exception var8) {
                Exception ex = var8;
                ChainedJsonException chainedjsonexception = ChainedJsonException.forException(ex);
                chainedjsonexception.prependJsonKey("values[" + i + "]");
                throw chainedjsonexception;
            }
        }

        if (count > 1 && jsonValues.size() == 1) {
            Arrays.fill(values, 1, values.length, values[0]);
        }

        return Arrays.copyOfRange(values, 0, count);
    }
}
