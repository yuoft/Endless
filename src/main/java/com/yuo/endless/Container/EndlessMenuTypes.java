package com.yuo.endless.Container;

import com.yuo.endless.Container.Chest.CompressorChestContainer;
import com.yuo.endless.Container.Chest.InfinityBoxContainer;
import com.yuo.endless.Endless;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EndlessMenuTypes {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Endless.MOD_ID);

    public static final RegistryObject<MenuType<ExtremeCraftContainer>> extremeCraftContainer = CONTAINERS.register("extreme_craft_container", () ->
            IForgeMenuType.create(ExtremeCraftContainer::new));
    public static final RegistryObject<MenuType<NeutronCollectorContainer>> neutronCollectorContainer = CONTAINERS.register("neutron_collector_container", () ->
            getMenu(NeutronCollectorContainer::new));
    public static final RegistryObject<MenuType<DenseNeutronCollectorContainer>> denseNeutronCollectorContainer = CONTAINERS.register("dense_neutron_collector_container", () ->
            IForgeMenuType.create(DenseNeutronCollectorContainer::new));
    public static final RegistryObject<MenuType<DoubleNeutronCollectorContainer>> doubleNeutronCollectorContainer = CONTAINERS.register("double_neutron_collector_container", () ->
            IForgeMenuType.create(DoubleNeutronCollectorContainer::new));
    public static final RegistryObject<MenuType<TripleNeutronCollectorContainer>> tripleNeutronCollectorContainer = CONTAINERS.register("triple_neutron_collector_container", () ->
            IForgeMenuType.create(TripleNeutronCollectorContainer::new));
    public static final RegistryObject<MenuType<NeutroniumCompressorContainer>> neutroniumCompressorContainer = CONTAINERS.register("neutronium_compressor_container", () ->
            IForgeMenuType.create(NeutroniumCompressorContainer::new));
    public static final RegistryObject<MenuType<CompressorChestContainer>> CompressorChestContainer = CONTAINERS.register("compressor_chest_container", () ->
            IForgeMenuType.create(CompressorChestContainer::new));
    public static final RegistryObject<MenuType<InfinityBoxContainer>> infinityBoxContainer = CONTAINERS.register("infinity_box_container", () ->
            IForgeMenuType.create(InfinityBoxContainer::new));

    private static <T extends AbstractContainerMenu> MenuType<T> getMenu(IContainerFactory<T> containerFactory) {
        return new MenuType<T>(containerFactory);
    }
}
