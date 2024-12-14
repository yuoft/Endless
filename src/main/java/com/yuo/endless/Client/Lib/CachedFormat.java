package com.yuo.endless.Client.Lib;


import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CachedFormat {
    public static Map<VertexFormat, CachedFormat> formatCache = new ConcurrentHashMap<>();

    public static CachedFormat BLOCK = new CachedFormat(DefaultVertexFormats.BLOCK);

    public VertexFormat format;

    public boolean hasPosition;

    public boolean hasNormal;

    public boolean hasColor;

    public boolean hasUV;

    public boolean hasOverlay;

    public boolean hasLightMap;

    public static CachedFormat lookup(VertexFormat f) {
        if (f == DefaultVertexFormats.BLOCK)
            return BLOCK;
        return formatCache.computeIfAbsent(f, CachedFormat::new);
    }

    public int positionIndex = -1;

    public int normalIndex = -1;

    public int colorIndex = -1;

    public int uvIndex = -1;

    public int overlayIndex = -1;

    public int lightMapIndex = -1;

    public int elementCount;

    public CachedFormat(VertexFormat format) {
        this.format = format;
        ImmutableList<VertexFormatElement> immutableList = format.getElements();
        this.elementCount = immutableList.size();
        for (int i = 0; i < this.elementCount; i++) {
            VertexFormatElement element = immutableList.get(i);
            switch (element.getUsage()) {
                case POSITION:
                    if (this.hasPosition)
                        throw new IllegalStateException();
                    this.hasPosition = true;
                    this.positionIndex = i;
                    break;
                case NORMAL:
                    if (this.hasNormal)
                        throw new IllegalStateException();
                    this.hasNormal = true;
                    this.normalIndex = i;
                    break;
                case COLOR:
                    if (this.hasColor)
                        throw new IllegalStateException();
                    this.hasColor = true;
                    this.colorIndex = i;
                    break;
                case UV:
                    switch (element.getIndex()) {
                        case 0:
                            if (this.hasUV)
                                throw new IllegalStateException();
                            this.hasUV = true;
                            this.uvIndex = i;
                            break;
                        case 1:
                            if (this.hasOverlay)
                                throw new IllegalStateException();
                            this.hasOverlay = true;
                            this.overlayIndex = i;
                            break;
                        case 2:
                            if (this.hasLightMap)
                                throw new IllegalStateException();
                            this.hasLightMap = true;
                            this.lightMapIndex = i;
                            break;
                    }
                    break;
            }
        }
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof CachedFormat))
            return false;
        CachedFormat other = (CachedFormat)obj;
        return (other.elementCount == this.elementCount && other.positionIndex == this.positionIndex && other.normalIndex == this.normalIndex && other.colorIndex == this.colorIndex && other.uvIndex == this.uvIndex && other.lightMapIndex == this.lightMapIndex);
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + this.elementCount;
        result = 31 * result + this.positionIndex;
        result = 31 * result + this.normalIndex;
        result = 31 * result + this.colorIndex;
        result = 31 * result + this.uvIndex;
        result = 31 * result + this.lightMapIndex;
        return result;
    }
}
