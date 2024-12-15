package com.yuo.endless.Container.Chest;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;

import java.util.Optional;

public class InfinityBoxCraftResultSlot extends ResultSlot {
    private final InfinityBoxCraftInventory craftMatrix;
    private final Player player;

    public InfinityBoxCraftResultSlot(Player player, InfinityBoxCraftInventory craftingInventory, Container inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
        this.craftMatrix = craftingInventory;
        this.player = player;
    }

    @Override
    public void onTake(Player thePlayer, ItemStack stack) {
        this.checkTakeAchievements(stack);
        ForgeHooks.setCraftingPlayer(thePlayer);
        NonNullList<ItemStack> nonnulllist = NonNullList.create();
        Level world = thePlayer.level;
        CraftingContainer craftingInv = this.craftMatrix.getContainer().getCraftingInv();
        Optional<CraftingRecipe> optional = world.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInv, world);
        if (optional.isPresent()){
            nonnulllist = optional.get().getRemainingItems(craftingInv);
        }
        ForgeHooks.setCraftingPlayer(null);
        for(int i = 243; i < nonnulllist.size() + 243; ++i) {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            ItemStack itemstack1 = nonnulllist.get(i - 243);
            if (!itemstack.isEmpty()) {
                this.craftMatrix.decrStackSize(i, 1);
                itemstack = this.craftMatrix.getStackInSlot(i);
            }

            if (!itemstack1.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.craftMatrix.setInventorySlotContents(i, itemstack1);
                } else if (ItemStack.isSame(itemstack, itemstack1) && ItemStack.isSameItemSameTags(itemstack, itemstack1)) {
                    itemstack1.grow(itemstack.getCount());
                    this.craftMatrix.setInventorySlotContents(i, itemstack1);
                } else if (!this.player.getInventory().add(itemstack1)) {
                    this.player.drop(itemstack1, false);
                }
            }
        }
    }
}
