package com.yuo.endless.Container.Chest;

import com.yuo.endless.Container.EndlessMenuTypes;
import com.yuo.endless.Tiles.CompressorChestTile;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CompressorChestContainer extends InfinityChestContainer {

    public CompressorChestContainer(int id, Inventory playerInventory){
        this(id, playerInventory, new CompressorChestTile(null, null));
    }

    public CompressorChestContainer(int id, Inventory playerInventory, CompressorChestTile tile) {
        super(EndlessMenuTypes.CompressorChestContainer.get(), id);
        this.chestTile = tile;
        chestTile.startOpen(playerInventory.player);

        for(int j = 0; j < 9; ++j) {
            for(int k = 0; k < 12; ++k) {
                this.addSlot(new InfinityBoxSlot(chestTile, k + j * 12, 12 + k * 18, 18 + j * 18));
            }
        }

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 39 + j1 * 18, 194 + l * 18));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 39 + i1 * 18, 252));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 54) { //取出
                if (!super.moveItemStackTo(itemstack1, 54, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }//放入
            } else if (!this.moveItemStackTo(itemstack1, 0, 54, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.chestTile.clearContent();
    }
}
