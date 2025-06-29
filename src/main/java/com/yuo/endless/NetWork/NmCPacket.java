package com.yuo.endless.NetWork;

import com.yuo.endless.Tiles.NeutroniumCompressorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class NmCPacket {
    private static BlockPos pos;
    private static ItemStack stack;
    public NmCPacket(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
        stack = buffer.readItem();
    }

    public NmCPacket(BlockPos pos, ItemStack stack) {
        NmCPacket.pos = pos;
        NmCPacket.stack = stack;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeItem(stack);
    }

    public static void handler(NmCPacket msg, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(msg, ctx)); //处理服务端发送给客户端的消息
        });
        ctx.get().setPacketHandled(true);
    }

    public BlockPos getPos() {
        return pos;
    }

    public ItemStack getStack() {
        return stack;
    }

    //客户端控制参与合成物品显示
    public static void handlePacket(NmCPacket msg, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() ->{
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null){
                BlockEntity tileEntity = world.getBlockEntity(msg.getPos());
                if (tileEntity instanceof NeutroniumCompressorTile && !msg.getStack().isEmpty()){
                    ((NeutroniumCompressorTile) tileEntity).setItem(2, msg.getStack());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
