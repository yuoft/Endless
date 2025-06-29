package com.yuo.endless.NetWork;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class TotemPacket {
    private final ItemStack stack;
    private final Entity entity;
    public TotemPacket(FriendlyByteBuf buffer) {
        Minecraft instance = Minecraft.getInstance();
        stack = buffer.readItem();
        if (instance.level != null) {
            entity = instance.level.getEntity(buffer.readInt());
        }else entity = null;
    }

    public TotemPacket(ItemStack stack, Entity entity) {
        this.stack = stack;
        this.entity = entity;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(stack);
        buf.writeInt(entity.getId());
    }

    public static void handler(TotemPacket msg, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> playTotem(msg.stack, msg.entity)); //处理服务端发送给客户端的消息
        });
        ctx.get().setPacketHandled(true);
    }

    //播放图腾动画，声音，粒子
    @OnlyIn(Dist.CLIENT)
    public static void playTotem(ItemStack stack, Entity entity) {
        Minecraft instance = Minecraft.getInstance();
        ClientLevel world = instance.level;
        if (world != null){
            instance.particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
            world.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);
            instance.gameRenderer.displayItemActivation(stack);
        }
    }

}
