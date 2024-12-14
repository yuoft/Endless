package com.yuo.endless;

import com.yuo.endless.Items.EndlessItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

//创造模式物品栏 实例化
public class EndlessTab extends CreativeModeTab {
	public static CreativeModeTab endless = new EndlessTab();

	public EndlessTab() {
		super(CreativeModeTab.getGroupCountSafe(), "Endless"); //页码11开始，名称
	}

	//图标
	@Override
	public ItemStack makeIcon() {
		return new ItemStack(EndlessItems.infinityCatalyst.get());
	}
}
