package com.yuo.endless.Items;

import com.yuo.endless.tab.ModGroup;
import net.minecraft.item.Food;
import net.minecraft.item.Item;

//食物
public class OrdinaryFood extends Item {
    public OrdinaryFood(Food food){
        super(new Properties().food(food).group(ModGroup.endless));
    }
}
