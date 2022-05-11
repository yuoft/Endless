package com.yuo.endless.Container;

import com.yuo.endless.Endless;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypeRegistry {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Endless.MOD_ID);

    public static final RegistryObject<ContainerType<ExtremeCraftContainer>> extremeCraftContainer = CONTAINERS.register("extreme_craft_container", () ->
            IForgeContainerType.create((int windowId, PlayerInventory inv, PacketBuffer data) ->
                    new ExtremeCraftContainer(windowId, inv)));
    public static final RegistryObject<ContainerType<NeutronCollectorContainer>> neutronCollectorContainer = CONTAINERS.register("neutron_collector_container", () ->
            IForgeContainerType.create((int windowId, PlayerInventory inv, PacketBuffer data) ->
                    new NeutronCollectorContainer(windowId, inv)));
    public static final RegistryObject<ContainerType<DenseNeutronCollectorContainer>> denseNeutronCollectorContainer = CONTAINERS.register("dense_neutron_collector_container", () ->
            IForgeContainerType.create((int windowId, PlayerInventory inv, PacketBuffer data) ->
                    new DenseNeutronCollectorContainer(windowId, inv)));
    public static final RegistryObject<ContainerType<DoubleNeutronCollectorContainer>> doubleNeutronCollectorContainer = CONTAINERS.register("double_neutron_collector_container", () ->
            IForgeContainerType.create((int windowId, PlayerInventory inv, PacketBuffer data) ->
                    new DoubleNeutronCollectorContainer(windowId, inv)));
    public static final RegistryObject<ContainerType<TripleNeutronCollectorContainer>> tripleNeutronCollectorContainer = CONTAINERS.register("triple_neutron_collector_container", () ->
            IForgeContainerType.create((int windowId, PlayerInventory inv, PacketBuffer data) ->
                    new TripleNeutronCollectorContainer(windowId, inv)));
    public static final RegistryObject<ContainerType<NeutroniumCompressorContainer>> neutroniumCompressorContainer = CONTAINERS.register("neutronium_compressor_container", () ->
            IForgeContainerType.create((int windowId, PlayerInventory inv, PacketBuffer data) ->
                    new NeutroniumCompressorContainer(windowId, inv)));
}
