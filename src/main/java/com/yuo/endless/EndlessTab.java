package com.yuo.endless;

import com.yuo.endless.Items.EndlessItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

//创造模式物品栏 实例化
public class EndlessTab extends ItemGroup{
	public static ItemGroup endless = new EndlessTab();

	public EndlessTab() {
		super(ItemGroup.GROUPS.length, "Endless"); //页码11开始，名称
	}
	//图标
	@Override
	public ItemStack createIcon() {
		return new ItemStack(EndlessItems.infinityCatalyst.get());
	}
}
