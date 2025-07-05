package com.yuo.endless.Compat.Jade;

import com.yuo.endless.Endless;
import com.yuo.endless.Tiles.NeutroniumCompressorTile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

import java.text.DecimalFormat;

public class CompressorComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    /**
     * 获取压缩进度
     * @return 进度
     */
    public String getProgress(int num, int total){
        if (num == 0) return "0";
        DecimalFormat df = new DecimalFormat("#.##");
        double v = num / (total * 1.0d);
        return df.format(v * 100);
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        Level world = blockAccessor.getLevel();
        BlockEntity tile = world.getBlockEntity(blockAccessor.getPosition());
        if (tile instanceof NeutroniumCompressorTile){
            int number = blockAccessor.getServerData().getInt("Number");
            if (number != 0){
                String progress = getProgress(number, blockAccessor.getServerData().getInt("NumberTotal"));
                iTooltip.add(Component.translatable("jade.endless.neutron_compressor_progress", progress));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
        if (accessor.getTarget() instanceof NeutroniumCompressorTile ncTile){
            compoundTag.putInt("Number", ncTile.data.get(0));
            compoundTag.putInt("NumberTotal", ncTile.data.get(1));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ResourceLocation.fromNamespaceAndPath(Endless.MOD_ID, "compressor");
    }
}
