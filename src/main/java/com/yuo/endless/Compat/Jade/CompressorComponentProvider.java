package com.yuo.endless.Compat.Jade;

import com.yuo.endless.Tiles.NeutroniumCompressorTile;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.text.DecimalFormat;

public class CompressorComponentProvider implements IComponentProvider, IServerDataProvider {

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
                iTooltip.add(new TranslatableComponent("jade.endless.neutron_compressor_progress", progress));
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, Object o, boolean b) {
        if (o instanceof NeutroniumCompressorTile ncTile){
            compoundTag.putInt("Number", ncTile.data.get(0));
            compoundTag.putInt("NumberTotal", ncTile.data.get(1));
        }
    }
}
