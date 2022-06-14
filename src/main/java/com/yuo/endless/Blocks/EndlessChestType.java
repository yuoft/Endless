package com.yuo.endless.Blocks;

import com.yuo.endless.Endless;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public enum EndlessChestType {
    COMPRESSOR("compressor",108, 9, 256, 276, new ResourceLocation(Endless.MOD_ID, "textures/gui/compressor_chest.png"), 256, 276),
    INFINITY("infinity",243, 9, 500, 276, new ResourceLocation(Endless.MOD_ID, "textures/gui/infinity_chest.png"), 500, 500),
    NORMAL("normal",27, 3, 176, 168, new ResourceLocation("textures/gui/container/shulker_box.png"), 256, 256);

    private final String name;
    public final int size;
    public final int rowLength;
    public final int xSize;
    public final int ySize;
    public final ResourceLocation guiTexture;
    public final int textureXSize;
    public final int textureYSize;

    EndlessChestType(@Nullable String name, int size, int rowLength, int xSize, int ySize, ResourceLocation guiTexture, int textureXSize, int textureYSize) {
        this.name = name;
        this.size = size;
        this.rowLength = rowLength;
        this.xSize = xSize;
        this.ySize = ySize;
        this.guiTexture = guiTexture;
        this.textureXSize = textureXSize;
        this.textureYSize = textureYSize;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public static Block get(EndlessChestType type) {
        switch (type) {
            case COMPRESSOR:
                return BlockRegistry.compressorChest.get();
            case INFINITY:
                return BlockRegistry.infinityBox.get();
            default:
                return Blocks.CHEST;
        }
    }

}
