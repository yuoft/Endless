package com.yuo.endless.Client.Sound;

import com.yuo.endless.Endless;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Endless.MOD_ID);

//    public static final SoundEvent GAPING_VOID = new SoundEvent(new ResourceLocation("endless:gaping_void"));
    public static final RegistryObject<SoundEvent> GAPING_VOID = SOUNDS.register("gaping_void",
            () -> new SoundEvent(new ResourceLocation(Endless.MOD_ID, "gaping_void")));
}
