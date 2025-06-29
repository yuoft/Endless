package com.yuo.endless;

import com.yuo.endless.Items.EndlessItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

//创造模式物品栏 实例化
public class EndlessTabs {
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Endless.MOD_ID);
	public static final RegistryObject<CreativeModeTab> ENDLESSS_TAB = TABS.register(Endless.MOD_ID + "_tab", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.tab.Endless"))
			.icon(() -> EndlessItems.infinityCatalyst.get().getDefaultInstance())
			.displayItems((parameters, output) -> {
				for (RegistryObject<Item> entry : EndlessItems.ITEMS.getEntries()) {
					output.accept(new ItemStack(entry.get()));
				}


			}).build());
}
