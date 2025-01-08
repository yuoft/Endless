package com.yuo.endless.Container;

import com.yuo.endless.Config;
import com.yuo.endless.Recipe.*;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class ExtremeCraftResultSlot extends ResultSlot {
    private final CraftingContainer craftMatrix;
    private final Player player;

    public ExtremeCraftResultSlot(Player player, CraftingContainer craftingInventory, Container inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
        this.craftMatrix = craftingInventory;
    }

    @Override
    public void onTake(Player thePlayer, ItemStack stack) {
        this.onTake(player, stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
        NonNullList<ItemStack> nonnulllist; //优先匹配工作台配方，没有则配方无尽配方
        Level world = thePlayer.level;
        Optional<ExtremeCraftRecipe> recipeOptional = world.getRecipeManager().getRecipeFor(RecipeTypeRegistry.EXTREME_CRAFT_RECIPE, this.craftMatrix, world);
        Optional<ExtremeCraftShapeRecipe> recipeOptionalIn = world.getRecipeManager().getRecipeFor(RecipeTypeRegistry.EXTREME_CRAFT_SHAPE_RECIPE, this.craftMatrix, world);
        ExtremeCraftShapeRecipe shapeRecipe = ModRecipeManager.matchesRecipe(this.craftMatrix, world);
        if (recipeOptional.isPresent()){ //有序配方
            nonnulllist = world.getRecipeManager().getRemainingItemsFor(RecipeTypeRegistry.EXTREME_CRAFT_RECIPE, this.craftMatrix, world);
        }else if (recipeOptionalIn .isPresent()){ //无序配方  需单独匹配容器
            nonnulllist = world.getRecipeManager().getRemainingItemsFor(RecipeTypeRegistry.EXTREME_CRAFT_SHAPE_RECIPE, this.craftMatrix, world);
        }else if (shapeRecipe != null){
            nonnulllist = shapeRecipe.getRemainingItems(this.craftMatrix);
        }else {
            if (Config.SERVER.isCraftTable.get()){
                Optional<CraftingRecipe> optional = world.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, this.craftMatrix, world);
                if (optional.isPresent()){ // 原版配方
                    nonnulllist = world.getRecipeManager().getRemainingItemsFor(RecipeType.CRAFTING, this.craftMatrix, world);
                }else nonnulllist = ExtremeCraftingManager.getInstance().getRecipeShirkItem((ExtremeCraftInventory) this.craftMatrix, world);
            }else nonnulllist = ExtremeCraftingManager.getInstance().getRecipeShirkItem((ExtremeCraftInventory) this.craftMatrix, world);
        }
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = this.craftMatrix.getItem(i);
            ItemStack itemstack1 = nonnulllist.get(i);
            if (!itemstack.isEmpty()) {
                this.craftMatrix.removeItem(i, 1);
                itemstack = this.craftMatrix.getItem(i);
            }

            if (!itemstack1.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.craftMatrix.setItem(i, itemstack1);
                } else if (ItemStack.isSame(itemstack, itemstack1) && ItemStack.isSame(itemstack, itemstack1)) {
                    itemstack1.grow(itemstack.getCount());
                    this.craftMatrix.setItem(i, itemstack1);
                } else if (!this.player.getInventory().add(itemstack1)) {
                    this.player.drop(itemstack1, false);
                }
            }
        }
    }
}
