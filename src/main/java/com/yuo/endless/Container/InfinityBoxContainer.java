package com.yuo.endless.Container;

import com.yuo.endless.Tiles.InfinityBoxTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class InfinityBoxContainer extends Container {
    private final InfinityBoxTile chestTile;

    public InfinityBoxContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory, new InfinityBoxTile());
    }

    public InfinityBoxContainer(int id, PlayerInventory playerInventory, InfinityBoxTile tile) {
        super(ContainerTypeRegistry.infinityBoxContainer.get(), id);
        this.chestTile = tile;
        chestTile.openInventory(playerInventory.player);

        for(int j = 0; j < 9; ++j) {
            for(int k = 0; k < 27; ++k) {
                this.addSlot(new Slot(chestTile, k + j * 27, 8 + k * 18, 18 + j * 18));
            }
        }

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 170 + j1 * 18, 194 + l * 18));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 170 + i1 * 18, 252));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.chestTile.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 243) { //取出
                if (!this.mergeItemStack(itemstack1, 243, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }//放入
            } else if (!this.mergeItemStack(itemstack1, 0, 243, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.chestTile.closeInventory(playerIn);
    }
}
