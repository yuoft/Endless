package com.yuo.endless.Client.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OBJParser {
    static Pattern vertPattern = Pattern.compile("v(?: ([\\d\\.+-]+))+");

    static Pattern uvwPattern = Pattern.compile("vt(?: ([\\d\\.+-]+))+");

    static Pattern normalPattern = Pattern.compile("vn(?: ([\\d\\.+-]+))+");

    static Pattern polyPattern = Pattern.compile("f(?: ((?:\\d*)(?:/\\d*)?(?:/\\d*)?))+");

    public static ThreadLocal<Matcher> vertMatcher = ThreadLocal.withInitial(() -> vertPattern.matcher(""));

    public static ThreadLocal<Matcher> uvwMatcher = ThreadLocal.withInitial(() -> uvwPattern.matcher(""));

    public static ThreadLocal<Matcher> normalMatcher = ThreadLocal.withInitial(() -> normalPattern.matcher(""));

    public static ThreadLocal<Matcher> polyMatcher = ThreadLocal.withInitial(() -> polyPattern.matcher(""));

    public static Map<String, CCModel> parseModels(ResourceLocation res) {
        return parseModels(res, null);
    }

    public static Map<String, CCModel> parseModels(ResourceLocation res, Transformation coordSystem) {
        return parseModels(res, 4, coordSystem);
    }

    public static Map<String, CCModel> parseModels(ResourceLocation res, int vertexMode, Transformation coordSystem) {
        try (IResource resource = Minecraft.getInstance().getResourceManager().getResource(res)) {
            return parseModels(resource.getInputStream(), vertexMode, coordSystem);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static Map<String, CCModel> parseModels(InputStream input, int vertexMode, Transformation coordSystem) throws IOException {
        if (coordSystem == null)
            coordSystem = new RedundantTransformation();
        int vp = (vertexMode == 7) ? 4 : 3;
        HashMap<String, CCModel> modelMap = new HashMap<>();
        ArrayList<Vector3> verts = new ArrayList<>();
        ArrayList<Vector3> uvs = new ArrayList<>();
        ArrayList<Vector3> normals = new ArrayList<>();
        ArrayList<int[]> polys = (ArrayList)new ArrayList<>();
        String modelName = "unnamed";
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("\\s+", " ").trim();
            if (line.startsWith("#") || line.length() == 0)
                continue;
            if (line.startsWith("v ")) {
                assertMatch(vertMatcher.get(), line);
                double[] values = parseDoubles(line.substring(2), " ");
                Vector3 vert = new Vector3(values[0], values[1], values[2]);
                coordSystem.apply(vert);
                verts.add(vert);
                continue;
            }
            if (line.startsWith("vt ")) {
                assertMatch(uvwMatcher.get(), line);
                double[] values = parseDoubles(line.substring(3), " ");
                uvs.add(new Vector3(values[0], 1.0D - values[1], 0.0D));
                continue;
            }
            if (line.startsWith("vn ")) {
                assertMatch(normalMatcher.get(), line);
                double[] values = parseDoubles(line.substring(3), " ");
                Vector3 norm = (new Vector3(values[0], values[1], values[2])).normalize();
                coordSystem.applyN(norm);
                normals.add(norm);
                continue;
            }
            if (line.startsWith("f ")) {
                assertMatch(polyMatcher.get(), line);
                String[] av = line.substring(2).split(" ");
                int[][] polyVerts = new int[av.length][3];
                for (int i = 0; i < av.length; i++) {
                    String[] as = av[i].split("/");
                    for (int p = 0; p < as.length; p++) {
                        if (as[p].length() > 0)
                            polyVerts[i][p] = Integer.parseInt(as[p]);
                    }
                }
                if (vp == 3) {
                    triangulate((List<int[]>)polys, polyVerts);
                } else {
                    quadulate((List<int[]>)polys, polyVerts);
                }
            }
            if (line.startsWith("g ")) {
                if (!polys.isEmpty()) {
                    modelMap.put(modelName, CCModel.createModel(verts, uvs, normals, vertexMode, (List<int[]>)polys));
                    polys.clear();
                }
                modelName = line.substring(2);
            }
        }
        if (!polys.isEmpty())
            modelMap.put(modelName, CCModel.createModel(verts, uvs, normals, vertexMode, (List<int[]>)polys));
        return modelMap;
    }

    static void assertMatch(Matcher m, String s) {
        m.reset(s);
    }

    static double[] parseDoubles(String s, String token) {
        String[] as = s.split(token);
        double[] values = new double[as.length];
        for (int i = 0; i < as.length; i++)
            values[i] = Double.parseDouble(as[i]);
        return values;
    }

    static void triangulate(List<int[]> polys, int[][] polyVerts) {
        for (int i = 2; i < polyVerts.length; i++) {
            polys.add(polyVerts[0]);
            polys.add(polyVerts[i]);
            polys.add(polyVerts[i - 1]);
        }
    }

    static void quadulate(List<int[]> polys, int[][] polyVerts) {
        if (polyVerts.length == 4) {
            polys.add(polyVerts[0]);
            polys.add(polyVerts[3]);
            polys.add(polyVerts[2]);
            polys.add(polyVerts[1]);
        } else {
            for (int i = 2; i < polyVerts.length; i++) {
                polys.add(polyVerts[0]);
                polys.add(polyVerts[i]);
                polys.add(polyVerts[i - 1]);
                polys.add(polyVerts[i - 1]);
            }
        }
    }
}
