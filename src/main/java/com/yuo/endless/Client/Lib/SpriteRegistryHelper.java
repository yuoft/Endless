package com.yuo.endless.Client.Lib;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.Endless;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.*;
import java.util.function.Consumer;

//@OnlyIn(Dist.CLIENT)
public class SpriteRegistryHelper {
//    ResourceLocation TEXTURES = AtlasTexture.LOCATION_BLOCKS_TEXTURE;

    public static final ResourceLocation TEXTURES = PlayerContainer.LOCATION_BLOCKS_TEXTURE;;
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

    public AtlasRegistrarImpl getRegistrar(AtlasTexture atlas) {
        AtlasRegistrarImpl registrar = this.atlasRegistrars.get(atlas.getTextureLocation());
        if (registrar == null) {
            registrar = new AtlasRegistrarImpl();
            this.atlasRegistrars.put(atlas.getTextureLocation(), registrar);
        }
        return registrar;
    }

    @SubscribeEvent
    public void onTextureStitchPre(TextureStitchEvent.Pre event) {
        AtlasTexture atlas = event.getMap();
        AtlasRegistrarImpl registrar = getRegistrar(atlas);
        this.iconRegisters.get(atlas.getTextureLocation()).forEach(e -> e.registerIcons(registrar));
        registrar.postRegister( e-> event.getMap());
        registrar.processPre(event::addSprite);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTextureStitchPostFirst(TextureStitchEvent.Post event) {
        AtlasTexture atlas = event.getMap();
        AtlasRegistrarImpl registrar = getRegistrar(atlas);
        registrar.processPost(atlas);
    }

    @SubscribeEvent
    public void onTextureStitchPost(TextureStitchEvent.Post event) {
        AtlasTexture atlas = event.getMap();
        AtlasRegistrarImpl registrar = getRegistrar(atlas);
        registrar.processPostFirst(atlas);
    }

    public static void main(String[] args) {
        SpriteRegistryHelper helper = new SpriteRegistryHelper();
        helper.addIIconRegister(registrar -> registrar.registerSprite(shader("cosmic_0"), e -> {}));
        helper.addIIconRegister(registrar -> registrar.registerSprite(shader("cosmic_2"), e -> {}));
        helper.addIIconRegister(new ResourceLocation(Endless.MOD_ID, "aaa") ,registrar -> registrar.registerSprite(shader("cosmic_2"), e -> {}));
        int i = 0;
    }
    static ResourceLocation shader(String path) {
        return new ResourceLocation(Endless.MOD_ID, "shader/" + path);
    }

    public static final class AtlasRegistrarImpl implements AtlasRegistrar {
        private final Multimap<ResourceLocation, Consumer<TextureAtlasSprite>> sprites;
        private final List<Consumer<AtlasTexture>> postCallbacks;
        private final Map<ResourceLocation, Consumer<ProceduralTexture>> proceduralTextures;

        public static void main(String[] args) {
            AtlasRegistrarImpl registrar = new AtlasRegistrarImpl();
            registrar.registerSprite(shader("cosmic_0"), e -> {});
            registrar.registerSprite(shader("cosmic_1"), null);
            registrar.registerSprite(shader("cosmic_2"), null);
            registrar.registerSprite(shader("cosmic_3"), null);
            registrar.registerSprite(shader("cosmic_4"), null);
            registrar.registerSprite(shader("cosmic_5"), null);
            registrar.registerSprite(shader("cosmic_6"), null);
            registrar.registerSprite(shader("cosmic_7"), null);
            registrar.registerSprite(shader("cosmic_8"), null);
            registrar.registerSprite(shader("cosmic_9"), null);
        }

        public AtlasRegistrarImpl() {
            this.sprites = HashMultimap.create();
            this.postCallbacks = new ArrayList<>();
            this.proceduralTextures = new HashMap<>();
        }

        public void registerSprite(ResourceLocation loc, Consumer<TextureAtlasSprite> onReady) {
            this.sprites.put(loc, onReady);
        }

        public void registerProceduralSprite(ResourceLocation loc, Consumer<ProceduralTexture> cycleFunc, Consumer<TextureAtlasSprite> onReady) {
            this.registerSprite(loc, onReady);
            this.proceduralTextures.put(loc, cycleFunc);
        }

        public void postRegister(Consumer<AtlasTexture> func) {
            this.postCallbacks.add(func);
        }

        public void processPre(Consumer<ResourceLocation> register) {
            this.sprites.keySet().forEach(register);
        }

        void processPostFirst(AtlasTexture atlas) {
            for (Map.Entry<ResourceLocation, Consumer<ProceduralTexture>> entry : this.proceduralTextures.entrySet()) {
                ResourceLocation name = entry.getKey();
                ProceduralTexture texture = new ProceduralTexture(atlas, atlas.getSprite(name), entry.getValue());
                atlas.listAnimatedSprites.add(texture);
                atlas.mapUploadedSprites.put(name, texture);
            }
        }

        void processPost(AtlasTexture atlas) {
            for (Map.Entry<ResourceLocation, Collection<Consumer<TextureAtlasSprite>>> entry : this.sprites.asMap().entrySet()) {
                TextureAtlasSprite sprite = atlas.getSprite(entry.getKey());
                entry.getValue().forEach(e -> e.accept(sprite));
            }
            for (Consumer<AtlasTexture> callback : this.postCallbacks)
                callback.accept(atlas);
        }
    }
}