package com.yuo.endless.tab;

import com.yuo.endless.Items.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

//创造模式物品栏 实例化
public class ModGroup extends ItemGroup{
	public static ItemGroup endless = new ModGroup();

	public ModGroup() {
		super(ItemGroup.GROUPS.length, "Endless"); //页码11开始，名称
	}
	//图标
	@Override
	public ItemStack createIcon() {
		return new ItemStack(ItemRegistry.meatballs.get());
	}
}
