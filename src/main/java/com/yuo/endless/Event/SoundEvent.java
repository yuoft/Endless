package com.yuo.endless.Event;

import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Client.Gui.InfinityBoxScreen;
import net.minecraft.client.gui.screens.Screen;
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
    public static boolean IS_INFINITY_CHEST; //无尽箱子物品数量显示控制，防止RS冲突

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void screenPre(ScreenEvent.DrawScreenEvent.Pre e) {
        AvaritiaShaders.inventoryRender = true;
        Screen screen = e.getScreen();
        if (screen instanceof InfinityBoxScreen){
            IS_INFINITY_CHEST = true;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void screenPost(ScreenEvent.DrawScreenEvent.Post e) {
        AvaritiaShaders.inventoryRender = false;
        Screen screen = e.getScreen();
        if (screen instanceof InfinityBoxScreen){
            IS_INFINITY_CHEST = false;
        }
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
