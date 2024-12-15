package com.yuo.endless.Container.Chest;

import com.yuo.endless.Tiles.InfinityBoxTile;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;

public class InfinityBoxBurnResultSlot extends Slot {
    private final Player player;
    private int removeCount;
    public InfinityBoxBurnResultSlot(Player player, Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        return false;
    }

    @Override
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    protected void onQuickCraft(ItemStack pStack, int pAmount) {
        this.removeCount += pAmount;
        this.checkTakeAchievements(pStack);
    }

    @Override
    public void onSwapCraft(int pNumItemsCrafted) {
        this.removeCount += pNumItemsCrafted;
    }

    protected void checkTakeAchievements(ItemStack pStack) {
        if (this.removeCount > 0) {
            pStack.onCraftedBy(this.player.level, this.player, this.removeCount);
            ForgeEventFactory.firePlayerCraftingEvent(this.player, pStack, this.container);
        }

        if (this.container instanceof RecipeHolder) {
            ((RecipeHolder)this.container).awardUsedRecipes(this.player);
        }

        this.removeCount = 0;
    }

    @Override
    public void onTake(Player thePlayer, ItemStack stack) {
        this.safeInsert(stack);
        super.onTake(thePlayer, stack);
        this.setChanged();
    }

    @Override
    public ItemStack safeInsert(ItemStack stack) {
//        stack.onCrafting(this.player.level, this.player, this.removeCount);
        if (!this.player.level.isClientSide && this.container instanceof InfinityBoxTile) {
            ((InfinityBoxTile)this.container).unlockRecipes(this.player);
        }

        this.removeCount = 0;
        ForgeEventFactory.firePlayerSmeltedEvent(this.player, stack);
        return super.safeInsert(stack);
    }

    @Override
    public ItemStack safeInsert(ItemStack pStack, int pIncrement) {
        this.removeCount += pIncrement;
        return super.safeInsert(pStack, pIncrement);
    }
}
