package com.yuo.endless.Container;

import com.yuo.endless.Recipe.CompressorManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class NiumCSlot extends Slot {
    private final World world;
    public NiumCSlot(IInventory inventoryIn, World worldIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.world = worldIn;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return !CompressorManager.getOutput(stack).isEmpty();
//        return this.world.getRecipeManager().getRecipe(RecipeTypeRegistry.NEUTRONIUM_RECIPE, new Inventory(stack), this.world).isPresent();
    }
}
