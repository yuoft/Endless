package com.yuo.endless.Compat.Jade;

import com.yuo.endless.Endless;
import com.yuo.endless.RlUtils;
import com.yuo.endless.Tiles.AbsNeutronCollectorTile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;

import java.text.DecimalFormat;

public class CollectorComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        Level world = blockAccessor.getLevel();
        BlockEntity tile = world.getBlockEntity(blockAccessor.getPosition());
        if (tile instanceof AbsNeutronCollectorTile ncTile){
            String progress = getProgress(blockAccessor.getServerData().getInt("Timer"), ncTile.getCraftTime());
            iTooltip.add(Component.translatable("jade.endless.neutron_collector_progress", progress));
        }
    }
    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor accessor) {
        if (accessor.getTarget() instanceof AbsNeutronCollectorTile ncTile){
            compoundTag.putInt("Timer", ncTile.data.get(0));
        }
    }

    @Override
    public @Nullable IElement getIcon(BlockAccessor accessor, IPluginConfig config, IElement currentIcon) {
        return IBlockComponentProvider.super.getIcon(accessor, config, currentIcon);
    }

    /**
     * 获取收集进度
     * @return 进度
     */
    public String getProgress(int time, int maxTime){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format((time / (maxTime * 1.0d)) * 100);
    }

    @Override
    public ResourceLocation getUid() {
        return RlUtils.fa("collector");
    }
}
