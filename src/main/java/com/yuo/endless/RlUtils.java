package com.yuo.endless;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;

/**
 * ResourceLocation类调用
 */
public class RlUtils {
    public static ResourceLocation fa(String path){
        return new ResourceLocation(Endless.MOD_ID, path);
    }

    public static ResourceLocation fa(String namespace, String path){
        return new ResourceLocation(namespace, path);
    }

    public static ResourceLocation tryParse(String s){
        try {
            return new ResourceLocation(s);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }

    public static ResourceLocation parse(String s){
        return new ResourceLocation(s);
    }
}
