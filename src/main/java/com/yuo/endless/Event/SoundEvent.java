package com.yuo.endless.Event;

import com.yuo.endless.Client.AvaritiaShaders;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Date;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class SoundEvent {
    public static Date lastplayedlog = null;
    public static Date lastplayedleaf = null;

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void screenPre(ScreenEvent.DrawScreenEvent.Pre e) {
        AvaritiaShaders.inventoryRender = true;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void screenPost(ScreenEvent.DrawScreenEvent.Post e) {
        AvaritiaShaders.inventoryRender = false;
    }

    //减少声音播放 间隔10ms
    @SubscribeEvent
    public static void sound(PlaySoundEvent event){
        String name = event.getName().trim();
        //在连锁破坏时 以下声音设置最小播放间隔
        if (name.equals("block.grass.break") || name.equals("block.wood.break")) {
            Date now = new Date();
            Date then;

            if (name.equals("block.grass.break")) {
                then = lastplayedleaf;
                lastplayedleaf = now;
            }
            else {
                then = lastplayedlog;
                lastplayedlog = now;
            }

            if (then != null) {
                long ms = (now.getTime()-then.getTime());
                if (ms < 10) {
                    event.setSound(null);
                }
            }
        }
    }
}
