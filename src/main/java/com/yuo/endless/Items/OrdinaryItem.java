package com.yuo.endless.Items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 通用普通物品
 */
public class OrdinaryItem extends Item {

	public OrdinaryItem() {
		super(new Properties()); //设置物品所在 创造模式物品栏
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> components, TooltipFlag pIsAdvanced) {
		Item item = stack.getItem();
		if (item == EndlessItems.crystalMatrixIngot.get()){
			components.add(Component.translatable("endless.text.itemInfo.crystal_matrix_ingot"));
		}
		if (item == EndlessItems.neutroniumPile.get()){
			components.add(Component.translatable("endless.text.itemInfo.neutronium_pile"));
		}
		if (item == EndlessItems.neutroniumNugget.get()){
			components.add(Component.translatable("endless.text.itemInfo.neutronium_nugget"));
		}
		if (item == EndlessItems.neutroniumIngot.get()){
			components.add(Component.translatable("endless.text.itemInfo.neutronium_ingot"));
		}
		if (item == EndlessItems.recordFragment.get()){
			components.add(Component.translatable("endless.text.itemInfo.record_fragment"));
		}
		if (item == EndlessItems.starFuel.get()){
			components.add(Component.translatable("endless.text.itemInfo.star_fuel"));
		}
	}

}
