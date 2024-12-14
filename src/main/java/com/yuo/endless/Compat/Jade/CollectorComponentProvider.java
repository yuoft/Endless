package com.yuo.endless.Compat.Jade;

import com.yuo.endless.Tiles.AbsNeutronCollectorTile;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.List;

public class CollectorComponentProvider implements IComponentProvider, IServerDataProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        World world = accessor.getWorld();
        TileEntity tile = world.getTileEntity(accessor.getPosition());
        if (tile instanceof AbsNeutronCollectorTile){
            AbsNeutronCollectorTile ncTile = (AbsNeutronCollectorTile) tile;
            String progress = getProgress(accessor.getServerData().getInt("Timer"), ncTile.getCraftTime());
            tooltip.add(new TranslationTextComponent("jade.endless.neutron_collector_progress", progress));
        }
    }

    @Override
    public void appendServerData(CompoundNBT compoundNBT, ServerPlayerEntity serverPlayerEntity, World world, Object o) {
        if (o instanceof AbsNeutronCollectorTile){
            AbsNeutronCollectorTile ncTile = (AbsNeutronCollectorTile) o;
            compoundNBT.putInt("Timer", ncTile.data.get(0));
        }
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
