package com.yuo.endless.Container;

import com.yuo.endless.Container.Chest.CompressorChestContainer;
import com.yuo.endless.Container.Chest.InfinityBoxContainer;
import com.yuo.endless.Endless;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EndlessMenuTypes {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Endless.MOD_ID);

    public static final RegistryObject<MenuType<ExtremeCraftContainer>> extremeCraftContainer = CONTAINERS.register("extreme_craft_container", () ->
            new MenuType<>(ExtremeCraftContainer::new));
    public static final RegistryObject<MenuType<NeutronCollectorContainer>> neutronCollectorContainer = CONTAINERS.register("neutron_collector_container", () ->
            new MenuType<>(NeutronCollectorContainer::new));
    public static final RegistryObject<MenuType<DenseNeutronCollectorContainer>> denseNeutronCollectorContainer = CONTAINERS.register("dense_neutron_collector_container", () ->
            new MenuType<>(DenseNeutronCollectorContainer::new));
    public static final RegistryObject<MenuType<DoubleNeutronCollectorContainer>> doubleNeutronCollectorContainer = CONTAINERS.register("double_neutron_collector_container", () ->
            new MenuType<>(DoubleNeutronCollectorContainer::new));
    public static final RegistryObject<MenuType<TripleNeutronCollectorContainer>> tripleNeutronCollectorContainer = CONTAINERS.register("triple_neutron_collector_container", () ->
            new MenuType<>(TripleNeutronCollectorContainer::new));
    public static final RegistryObject<MenuType<NeutroniumCompressorContainer>> neutroniumCompressorContainer = CONTAINERS.register("neutronium_compressor_container", () ->
            new MenuType<>(NeutroniumCompressorContainer::new));
    public static final RegistryObject<MenuType<CompressorChestContainer>> CompressorChestContainer = CONTAINERS.register("compressor_chest_container", () ->
            new MenuType<>(CompressorChestContainer::new));
    public static final RegistryObject<MenuType<InfinityBoxContainer>> infinityBoxContainer = CONTAINERS.register("infinity_box_container", () ->
            new MenuType<>(InfinityBoxContainer::new));
}
