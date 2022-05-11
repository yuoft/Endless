package com.yuo.endless.NetWork;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class NmCPacket {
    private static BlockPos pos;
    private static ItemStack stack;
    public NmCPacket(PacketBuffer buffer) {
        pos = buffer.readBlockPos();
        stack = buffer.readItemStack();
    }

    public NmCPacket(BlockPos pos, ItemStack stack) {
        NmCPacket.pos = pos;
        NmCPacket.stack = stack;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeItemStack(stack);
    }

    public static void handler(NmCPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> NmCClientPacket.handlePacket(msg, ctx)); //处理服务端发送给客户端的消息
        });
        ctx.get().setPacketHandled(true);
    }

    public BlockPos getPos() {
        return pos;
    }

    public ItemStack getStack() {
        return stack;
    }
}
