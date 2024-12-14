package com.yuo.endless.Items;

import com.yuo.endless.EndlessTab;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

//食物
public class OrdinaryFood extends Item {
    public OrdinaryFood(FoodProperties food){
        super(new Properties().food(food).tab(EndlessTab.endless));
    }
}
