package com.yuo.endless.Container;

import com.yuo.endless.Config;
import com.yuo.endless.Recipe.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.Optional;

public class ExtremeCraftResultSlot extends CraftingResultSlot {
    private final CraftingInventory craftMatrix;
    private final PlayerEntity player;

    public ExtremeCraftResultSlot(PlayerEntity player, CraftingInventory craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
        this.craftMatrix = craftingInventory;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        this.onCrafting(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
        NonNullList<ItemStack> nonnulllist; //优先匹配工作台配方，没有则配方无尽配方
        World world = thePlayer.world;
        Optional<ExtremeCraftRecipe> recipeOptional = world.getRecipeManager().getRecipe(RecipeTypeRegistry.EXTREME_CRAFT_RECIPE, this.craftMatrix, world);
        Optional<ExtremeCraftShapeRecipe> recipeOptionalIn = world.getRecipeManager().getRecipe(RecipeTypeRegistry.EXTREME_CRAFT_SHAPE_RECIPE, this.craftMatrix, world);
        ExtremeCraftShapeRecipe shapeRecipe = ModRecipeManager.matchesRecipe(this.craftMatrix, world);
        if (recipeOptional.isPresent()){ //有序配方
            nonnulllist = world.getRecipeManager().getRecipeNonNull(RecipeTypeRegistry.EXTREME_CRAFT_RECIPE, this.craftMatrix, world);
        }else if (recipeOptionalIn .isPresent()){ //无序配方  需单独匹配容器
            nonnulllist = world.getRecipeManager().getRecipeNonNull(RecipeTypeRegistry.EXTREME_CRAFT_SHAPE_RECIPE, this.craftMatrix, world);
        }else if (shapeRecipe != null){
            nonnulllist = shapeRecipe.getRemainingItems(this.craftMatrix);
        }else {
            if (Config.SERVER.isCraftTable.get()){
                Optional<ICraftingRecipe> optional = world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, this.craftMatrix, world);
                if (optional.isPresent()){ // 原版配方
                    nonnulllist = world.getRecipeManager().getRecipeNonNull(IRecipeType.CRAFTING, this.craftMatrix, world);
                }else nonnulllist = ExtremeCraftingManager.getInstance().getRecipeShirkItem((ExtremeCraftInventory) this.craftMatrix, world);
            }else nonnulllist = ExtremeCraftingManager.getInstance().getRecipeShirkItem((ExtremeCraftInventory) this.craftMatrix, world);
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
