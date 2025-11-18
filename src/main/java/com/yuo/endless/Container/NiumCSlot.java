package com.yuo.endless.Container;

import com.yuo.endless.Recipe.CompressorManager;
import com.yuo.endless.Recipe.EndlessRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NiumCSlot extends Slot {
    private final Level world;
    public NiumCSlot(Container inventoryIn, Level worldIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.world = worldIn;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !CompressorManager.getOutput(stack).isEmpty() ||
                this.world.getRecipeManager().getRecipesFor(EndlessRecipes.NEUTRONIUM_RECIPE.get(), new SimpleContainer(stack), this.world).isEmpty();
    }
}
