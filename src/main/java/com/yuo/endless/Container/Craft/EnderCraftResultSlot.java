package com.yuo.endless.Container.Craft;

import com.yuo.endless.Recipe.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.Optional;

public class EnderCraftResultSlot extends CraftingResultSlot {
    private final CraftingInventory craftMatrix;
    private final PlayerEntity player;

    public EnderCraftResultSlot(PlayerEntity player, CraftingInventory craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
        this.craftMatrix = craftingInventory;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        this.onCrafting(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
        NonNullList<ItemStack> nonnulllist;
        World world = thePlayer.world;
        Optional<EnderCraftRecipe> recipeOptional = world.getRecipeManager().getRecipe(RecipeTypeRegistry.ENDER_CRAFT_RECIPE, this.craftMatrix, world);
        Optional<EnderCraftShapeRecipe> recipeOptionalIn = world.getRecipeManager().getRecipe(RecipeTypeRegistry.ENDER_CRAFT_SHAPE_RECIPE, this.craftMatrix, world);
        if (recipeOptional.isPresent()){ //有序配方
            nonnulllist = world.getRecipeManager().getRecipeNonNull(RecipeTypeRegistry.ENDER_CRAFT_RECIPE, this.craftMatrix, world);
        }else if (recipeOptionalIn .isPresent()){ //无序配方
            nonnulllist = world.getRecipeManager().getRecipeNonNull(RecipeTypeRegistry.ENDER_CRAFT_SHAPE_RECIPE, this.craftMatrix, world);
        }else {
            nonnulllist = NonNullList.create();
        }
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            ItemStack itemstack1 = nonnulllist.get(i);
            if (!itemstack.isEmpty()) {
                this.craftMatrix.decrStackSize(i, 1);
                itemstack = this.craftMatrix.getStackInSlot(i);
            }

            if (!itemstack1.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.craftMatrix.setInventorySlotContents(i, itemstack1);
                } else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
                    itemstack1.grow(itemstack.getCount());
                    this.craftMatrix.setInventorySlotContents(i, itemstack1);
                } else if (!this.player.inventory.addItemStackToInventory(itemstack1)) {
                    this.player.dropItem(itemstack1, false);
                }
            }
        }

        return stack;
    }
}
