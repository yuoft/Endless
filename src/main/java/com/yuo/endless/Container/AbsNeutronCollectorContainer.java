package com.yuo.endless.Container;

import com.yuo.endless.Tiles.AbsNeutronCollectorTile;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AbsNeutronCollectorContainer extends AbstractContainerMenu {

    private final AbsNeutronCollectorTile collectorTile;
    private final NCIntArray data;

    public AbsNeutronCollectorContainer(int id, Inventory playerInventory, MenuType<?> containerType, Container tile) {
        super(containerType, id);
        this.collectorTile = (AbsNeutronCollectorTile) tile;
        this.data = collectorTile.data;
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

        this.addDataSlots(data);
    }

    //玩家shift行为
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            if (index != 0){
               if (index < 28) { //从物品栏到快捷栏
                    if (!this.moveItemStackTo(itemStack1, 28, 37, false)) return ItemStack.EMPTY;
                } else if (index < 37) {
                    if (!this.moveItemStackTo(itemStack1, 1, 28, false)) return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack1, 1, 37, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }

    //获取运行时间
    @OnlyIn(Dist.CLIENT)
    public int getTimer(){
        return (int) Math.ceil(this.data.get(0) / 3600.0 * 24);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.collectorTile.stillValid(player);
    }
}
