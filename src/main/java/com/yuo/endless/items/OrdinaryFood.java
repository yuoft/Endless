package com.yuo.endless.items;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

//食物
public class OrdinaryFood extends Item {
    public OrdinaryFood(FoodProperties food){
        super(new Properties().food(food));
    }
}
