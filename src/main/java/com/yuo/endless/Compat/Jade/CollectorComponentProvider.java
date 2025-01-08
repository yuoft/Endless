package com.yuo.endless.Compat.Jade;

import com.yuo.endless.Tiles.AbsNeutronCollectorTile;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import mcp.mobius.waila.api.ui.IElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public class CollectorComponentProvider implements IComponentProvider, IServerDataProvider {

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        Level world = blockAccessor.getLevel();
        BlockEntity tile = world.getBlockEntity(blockAccessor.getPosition());
        if (tile instanceof AbsNeutronCollectorTile ncTile){
            String progress = getProgress(blockAccessor.getServerData().getInt("Timer"), ncTile.getCraftTime());
            iTooltip.add(new TranslatableComponent("jade.endless.neutron_collector_progress", progress));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, Object o, boolean b) {
        if (o instanceof AbsNeutronCollectorTile ncTile){
            compoundTag.putInt("Timer", ncTile.data.get(0));
        }
    }

    @Override
    public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
        return IComponentProvider.super.getIcon(accessor, config, currentIcon);
    }

    /**
     * 获取收集进度
     * @return 进度
     */
    public String getProgress(int time, int maxTime){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format((time / (maxTime * 1.0d)) * 100);
    }
}
