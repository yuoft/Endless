package com.yuo.endless.Event;

import com.mojang.blaze3d.platform.InputConstants;
import com.yuo.endless.Client.Model.InfinityArmorModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Date;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class SoundEvent {
    public static Date lastplayedlog = null;
    public static Date lastplayedleaf = null;

    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.KeyInputEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.level.isClientSide){
            int key = event.getKey();
//            boolean keyE = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_E);
//            boolean keyEsc = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_ESCAPE);
            if (key == InputConstants.KEY_E)
                InfinityArmorModel.useMenu = !InfinityArmorModel.useMenu;
            if (key == InputConstants.KEY_ESCAPE && InfinityArmorModel.useMenu)
                InfinityArmorModel.useMenu = false;
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
