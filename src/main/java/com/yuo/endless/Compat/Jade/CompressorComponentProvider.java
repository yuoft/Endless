package com.yuo.endless.Compat.Jade;

import com.yuo.endless.Tiles.NeutroniumCompressorTile;
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

public class CompressorComponentProvider implements IComponentProvider, IServerDataProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        World world = accessor.getWorld();
        TileEntity tile = world.getTileEntity(accessor.getPosition());
        if (tile instanceof NeutroniumCompressorTile){
            int number = accessor.getServerData().getInt("Number");
            if (number != 0){
                String progress = getProgress(number, accessor.getServerData().getInt("NumberTotal"));
                tooltip.add(new TranslationTextComponent("jade.endless.neutron_compressor_progress", progress));
            }
        }
    }

    //数据同步
    @Override
    public void appendServerData(CompoundNBT compoundNBT, ServerPlayerEntity serverPlayerEntity, World world, Object o) {
        if (o instanceof NeutroniumCompressorTile){
            NeutroniumCompressorTile ncTile = (NeutroniumCompressorTile) o;
            compoundNBT.putInt("Number", ncTile.data.get(0));
            compoundNBT.putInt("NumberTotal", ncTile.data.get(1));
        }
    }

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
}
