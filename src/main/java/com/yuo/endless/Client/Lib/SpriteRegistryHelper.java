package com.yuo.endless.Client.Lib;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.*;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class SpriteRegistryHelper {
    public static final ResourceLocation TEXTURES = TextureAtlas.LOCATION_BLOCKS;;
    private final Multimap<ResourceLocation, IIconRegister> iconRegisters;
    private final Map<ResourceLocation, AtlasRegistrarImpl> atlasRegistrars;

    public SpriteRegistryHelper() {
        this(FMLJavaModLoadingContext.get().getModEventBus());
    }

    SpriteRegistryHelper(IEventBus eventBus) {
        this.iconRegisters = HashMultimap.create();
        this.atlasRegistrars = new HashMap<>();
        eventBus.register(this);
    }

    public void addIIconRegister(ResourceLocation basePath, IIconRegister iconRegister) {
        this.iconRegisters.put(basePath, iconRegister);
    }

    public void addIIconRegister(IIconRegister iconRegister) {
        addIIconRegister(TEXTURES, iconRegister);
    }

    public AtlasRegistrarImpl getRegistrar(TextureAtlas atlas) {
        AtlasRegistrarImpl registrar = this.atlasRegistrars.get(atlas.location());
        if (registrar == null) {
            registrar = new AtlasRegistrarImpl();
            this.atlasRegistrars.put(atlas.location(), registrar);
        }
        return registrar;
    }

    @SubscribeEvent
    public void onTextureStitchPre(TextureStitchEvent.Pre event) {
        TextureAtlas atlas = event.getAtlas();
        AtlasRegistrarImpl registrar = getRegistrar(atlas);
        this.iconRegisters.get(atlas.location()).forEach(e -> e.registerIcons(registrar));
        registrar.postRegister( e-> event.getAtlas());
        registrar.processPre(event::addSprite);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTextureStitchPostFirst(TextureStitchEvent.Post event) {
        TextureAtlas atlas = event.getAtlas();
        AtlasRegistrarImpl registrar = getRegistrar(atlas);
        registrar.processPost(atlas);
    }

    @SubscribeEvent
    public void onTextureStitchPost(TextureStitchEvent.Post event) {
        TextureAtlas atlas = event.getAtlas();
        AtlasRegistrarImpl registrar = getRegistrar(atlas);
        registrar.processPostFirst(atlas);
    }

    public static final class AtlasRegistrarImpl implements AtlasRegistrar {
        private final Multimap<ResourceLocation, Consumer<TextureAtlasSprite>> sprites;
        private final List<Consumer<TextureAtlas>> postCallbacks;
        private final Map<ResourceLocation, Consumer<ProceduralTexture>> proceduralTextures;

        public AtlasRegistrarImpl() {
            this.sprites = HashMultimap.create();
            this.postCallbacks = new ArrayList<>();
            this.proceduralTextures = new HashMap<>();
        }

        public void registerSprite(ResourceLocation loc, Consumer<TextureAtlasSprite> onReady) {
            this.sprites.put(loc, onReady);
        }

        public void postRegister(Consumer<TextureAtlas> func) {
            this.postCallbacks.add(func);
        }

        public void processPre(Consumer<ResourceLocation> register) {
            this.sprites.keySet().forEach(register);
        }

        void processPostFirst(TextureAtlas atlas) {
            for (Map.Entry<ResourceLocation, Consumer<ProceduralTexture>> entry : this.proceduralTextures.entrySet()) {
                ResourceLocation name = entry.getKey();
                ProceduralTexture texture = new ProceduralTexture(atlas, atlas.getSprite(name), entry.getValue());
                atlas.animatedTextures.add(texture);
                atlas.texturesByName.put(name, texture);
            }
        }

        void processPost(TextureAtlas atlas) {
            for (Map.Entry<ResourceLocation, Collection<Consumer<TextureAtlasSprite>>> entry : this.sprites.asMap().entrySet()) {
                TextureAtlasSprite sprite = atlas.getSprite(entry.getKey());
                entry.getValue().forEach(e -> e.accept(sprite));
            }
            for (Consumer<TextureAtlas> callback : this.postCallbacks)
                callback.accept(atlas);
        }
    }
}