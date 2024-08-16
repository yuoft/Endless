package com.yuo.endless.Items;

import com.yuo.endless.EndlessTab;
import net.minecraft.item.Food;
import net.minecraft.item.Item;

//食物
public class OrdinaryFood extends Item {
    public OrdinaryFood(Food food){
        super(new Properties().food(food).group(EndlessTab.endless));
    }
}
