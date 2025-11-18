package com.yuo.endless.Event;

import com.yuo.endless.Endless;
import com.yuo.endless.Recipe.ModDataRecipes;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Endless.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenEvent {

    @SubscribeEvent
    public static void addLoot(GatherDataEvent event){
        boolean b = event.includeServer();
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<Provider> lookupProvider = event.getLookupProvider();
        if (b){
            generator.addProvider(true, new ModDataRecipes(output));
        }
    }
}
