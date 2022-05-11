package com.yuo.endless.Sound;

import com.yuo.endless.Endless;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Endless.MOD_ID);

//    public static final SoundEvent GAPING_VOID = new SoundEvent(new ResourceLocation("endless:gaping_void"));
    public static final RegistryObject<SoundEvent> GAPING_VOID = SOUNDS.register("gaping_void",
            () -> new SoundEvent(new ResourceLocation(Endless.MOD_ID, "gaping_void")));
}
