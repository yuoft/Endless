package com.yuo.endless;

import com.yuo.endless.event.EventHandler;
import com.yuo.endless.items.EndlessItems;
import com.yuo.endless.items.Singularity;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

//创造模式物品栏 实例化
public class EndlessTabs {
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Endless.MOD_ID);
	public static final RegistryObject<CreativeModeTab> ENDLESSS_TAB = TABS.register(Endless.MOD_ID + "_tab", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.tab.Endless"))
			.icon(() -> EndlessItems.infinityCatalyst.get().getDefaultInstance())
			.displayItems((parameters, output) -> {
				for (RegistryObject<Item> entry : EndlessItems.ITEMS.getEntries()) {
					if (entry.get() instanceof Singularity){
						for (String s : Singularity.TYPE) {
							output.accept(Singularity.getSingularity(s));
						}

					}else if (EventHandler.isInfinityItem(entry.get())){
						Item item = entry.get();
						ItemStack stack = new ItemStack(item);
						if (item == EndlessItems.infinitySword.get()){
							Map<Enchantment, Integer> map = new HashMap<>();
							map.put(Enchantments.MOB_LOOTING, 10);
							EnchantmentHelper.setEnchantments( map, stack);
						}
						if (item == EndlessItems.infinityPickaxe.get()){
							Map<Enchantment, Integer> map = new HashMap<>();
							map.put(Enchantments.BLOCK_FORTUNE, 10);
							EnchantmentHelper.setEnchantments( map, stack);
						}
						stack.getOrCreateTag().putBoolean("Unbreakable",true);
						output.accept(stack);
					}else output.accept(new ItemStack(entry.get()));
				}


			}).build());
}
