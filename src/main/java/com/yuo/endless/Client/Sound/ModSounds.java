package com.yuo.endless.Client.Sound;

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
    public static final RegistryObject<SoundEvent> INFINITY_BOW_STAR = SOUNDS.register("infinity_bow_star",
            () -> new SoundEvent(new ResourceLocation(Endless.MOD_ID, "infinity_bow_star")));
    public static final RegistryObject<SoundEvent> INFINITY_BOW_SHOOT = SOUNDS.register("infinity_bow_shoot",
            () -> new SoundEvent(new ResourceLocation(Endless.MOD_ID, "infinity_bow_shoot")));
    public static final RegistryObject<SoundEvent> INFINITY_BOW_END = SOUNDS.register("infinity_bow_end",
            () -> new SoundEvent(new ResourceLocation(Endless.MOD_ID, "infinity_bow_end")));
}
