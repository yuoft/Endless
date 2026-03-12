package com.yuo.endless.NetWork;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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
    private final int entityId; // 改为存储实体ID而不是实体本身

    public TotemPacket(FriendlyByteBuf buffer) {
        stack = buffer.readItem();
        entityId = buffer.readInt();
    }

    public TotemPacket(ItemStack stack, Entity entity) {
        this.stack = stack;
        this.entityId = entity.getId();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(stack);
        buf.writeInt(entityId);
    }

    public static void handler(TotemPacket msg, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                // 在客户端执行时再获取实体
                Minecraft mc = Minecraft.getInstance();
                if (mc.level != null) {
                    Entity entity = mc.level.getEntity(msg.entityId);
                    if (entity != null) {
                        playTotem(msg.stack, entity);
                    }
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }

    //播放图腾动画，声音，粒子
    @OnlyIn(Dist.CLIENT)
    public static void playTotem(ItemStack stack, Entity entity) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel world = mc.level;
        if (world != null){
            mc.particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
            world.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);
            if (mc.player == entity)
                mc.gameRenderer.displayItemActivation(stack);
        }
    }
}
