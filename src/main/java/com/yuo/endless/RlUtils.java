package com.yuo.endless;

import net.minecraft.resources.ResourceLocation;

/**
 * ResourceLocation类调用
 */
public class RlUtils {
    public static ResourceLocation fa(String path){
        return ResourceLocation.fromNamespaceAndPath(Endless.MOD_ID, path);
    }

    public static ResourceLocation fa(String namespace, String path){
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation tryParse(String s){
        return ResourceLocation.tryParse(s);
    }

    public static ResourceLocation parse(String s){
        return ResourceLocation.parse(s);
    }
}
