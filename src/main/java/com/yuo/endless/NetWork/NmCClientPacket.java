package com.yuo.endless.NetWork;

import com.yuo.endless.Tiles.NeutroniumCompressorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

//客户端控制参与合成物品显示
public class NmCClientPacket {
    public static void handlePacket(NmCPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->{
            ClientWorld world = Minecraft.getInstance().world;
            if (world != null){
                TileEntity tileEntity = world.getTileEntity(msg.getPos());
                if (tileEntity instanceof NeutroniumCompressorTile && !msg.getStack().isEmpty()){
                    ((NeutroniumCompressorTile) tileEntity).setInventorySlotContents(2, msg.getStack());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
