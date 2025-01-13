package com.yuo.endless.World;

import com.yuo.endless.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Entity.EntityRegistry;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Endless.MOD_ID)
public class ModWorldEvent {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        if (Config.SERVER.mobSpawn.get()) //默认不生成
            ModEntityGen.addEntityToAllBiomes(event, EntityRegistry.INFINITY_MOB.get(), Config.SERVER.mobWeigh.get(), 1,1);
    }
}
