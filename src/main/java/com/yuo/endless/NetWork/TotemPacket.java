package com.yuo.endless.NetWork;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class TotemPacket {
    private final ItemStack stack;
    private final Entity entity;
    public TotemPacket(PacketBuffer buffer) {
        Minecraft instance = Minecraft.getInstance();
        stack = buffer.readItemStack();
        entity = instance.world.getEntityByID(buffer.readInt());
    }

    public TotemPacket(ItemStack stack, Entity entity) {
        this.stack = stack;
        this.entity = entity;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeItemStack(stack);
        buf.writeInt(entity.getEntityId());
    }

    public static void handler(TotemPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> playTotem(msg.stack, msg.entity)); //处理服务端发送给客户端的消息
        });
        ctx.get().setPacketHandled(true);
    }

    //播放图腾动画，声音，粒子
    @OnlyIn(Dist.CLIENT)
    public static void playTotem(ItemStack stack, Entity entity) {
        Minecraft instance = Minecraft.getInstance();
        ClientWorld world = instance.world;
        if (world != null){
            instance.particles.emitParticleAtEntity(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
            world.playSound(entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);
            instance.gameRenderer.displayItemActivation(stack);
        }
    }

}
