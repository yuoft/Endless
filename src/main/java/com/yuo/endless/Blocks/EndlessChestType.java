package com.yuo.endless.Blocks;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IChestType;
import com.yuo.endless.Endless;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.Tiles.AbsEndlessChestTile;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public enum EndlessChestType implements IChestType {
    COMPRESSOR("compressed",256,108, 9, 256, 276, EndlessUtils.fa("textures/gui/compressor_chest.png"), 256, 276),
    INFINITY("infinity", Integer.MAX_VALUE, 256, 9, 500, 276, EndlessUtils.fa("textures/gui/infinity_chest.png"), 500, 500),
    NORMAL("normal",64, 27, 3, 176, 168, EndlessUtils.tryParse("textures/gui/container/shulker_box.png"), 256, 256);

    private final String name;
    public final int size;
    public final int stackLimit;
    public final int rowLength;
    public final int xSize;
    public final int ySize;
    public final ResourceLocation guiTexture;
    public final int textureXSize;
    public final int textureYSize;
    public final boolean isChest = ModList.get().isLoaded(Endless.MOD_ID);

    EndlessChestType(@Nullable String name, int limit, int size, int rowLength, int xSize, int ySize, ResourceLocation guiTexture, int textureXSize, int textureYSize) {
        this.name = name;
        this.size = size;
        this.stackLimit = limit;
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
        return switch (type) {
            case COMPRESSOR -> EndlessBlocks.compressedChest.get();
            case INFINITY -> EndlessBlocks.infinityBox.get();
            default -> Blocks.CHEST;
        };
    }

    @Override
    public boolean isChest(BlockEntity blockEntity) {
        return isChest && blockEntity instanceof AbsEndlessChestTile;
    }

    //是否可以被玩家打开
    @Override
    public boolean canOpenByPlayer(BlockEntity blockEntity, Player player) {
        if (isChest && blockEntity instanceof AbsEndlessChestTile chestTile) {
            return chestTile.canOpen(player);
        }else return false;
    }

    //打开箱子的玩家数量
    @Override
    public int getOpenCount(BlockGetter blockGetter, BlockPos blockPos, BlockEntity blockEntity) {
        return isChest && blockEntity instanceof AbsEndlessChestTile ? AbsEndlessChestTile.getOpenCount(blockGetter, blockPos) : Integer.MAX_VALUE;
    }
}
