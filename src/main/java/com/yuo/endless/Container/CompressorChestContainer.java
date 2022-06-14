package com.yuo.endless.Container;

import com.yuo.endless.Tiles.CompressorChestTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class CompressorChestContainer extends Container {
    private final CompressorChestTile chestTile;

    public CompressorChestContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory, new CompressorChestTile());
    }

    public CompressorChestContainer(int id, PlayerInventory playerInventory, CompressorChestTile tile) {
        super(ContainerTypeRegistry.CompressorChestContainer.get(), id);
        this.chestTile = tile;
        chestTile.openInventory(playerInventory.player);

        for(int j = 0; j < 9; ++j) {
            for(int k = 0; k < 12; ++k) {
                this.addSlot(new Slot(chestTile, k + j * 12, 12 + k * 18, 18 + j * 18));
            }
        }

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 40 + j1 * 18, 194 + l * 18));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 39 + i1 * 18, 252));
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

            if (index < 54) { //取出
                if (!this.mergeItemStack(itemstack1, 54, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }//放入
            } else if (!this.mergeItemStack(itemstack1, 0, 54, false)) {
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
