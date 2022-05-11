package com.yuo.endless.Container;

import com.yuo.endless.Tiles.AbsNeutronCollectorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class AbsNeutronCollectorContainer extends Container {

    private final AbsNeutronCollectorTile collectorTile;
    private final NCIntArray data;

    public AbsNeutronCollectorContainer(int id, PlayerInventory playerInventory, ContainerType<?> containerType, AbsNeutronCollectorTile inventory) {
        super(containerType, id);
        this.collectorTile = inventory;
        this.data = inventory.data;
        trackIntArray(data);
        //中子素生成槽
        this.addSlot(new NCOutputSlot(collectorTile, 0, 80,35));
        //添加玩家物品栏
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        //添加玩家快捷栏
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.collectorTile.isUsableByPlayer(playerIn);
    }

    //玩家shift行为
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();
            if (index != 0){
               if (index < 28) { //从物品栏到快捷栏
                    if (!this.mergeItemStack(itemStack1, 28, 37, false)) return ItemStack.EMPTY;
                } else if (index < 37) {
                    if (!this.mergeItemStack(itemStack1, 1, 28, false)) return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemStack1, 1, 37, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }

    //获取运行时间
    public int getTimer(){
        return (int) Math.ceil(this.data.get(0) / 3600.0 * 24);
    }

}
