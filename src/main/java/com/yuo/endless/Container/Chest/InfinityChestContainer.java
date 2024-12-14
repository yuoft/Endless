package com.yuo.endless.Container.Chest;

import com.yuo.endless.Tiles.AbsEndlessChestTile;
import com.yuo.endless.Tiles.InfinityBoxTile;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.Iterator;

public class InfinityChestContainer extends Container {
    protected AbsEndlessChestTile chestTile;

    protected InfinityChestContainer(@Nullable ContainerType<?> type, int id) {
        super(type, id);
        chestTile = null;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerEntity) {
        return this.chestTile.isUsableByPlayer(playerEntity);
    }


    /**
     * shift 移动物品
     * @param stackIn 物品
     * @param index 开始查找序号
     * @param index0 结束序号
     * @param flagIn 查找顺序
     * @return 结果
     */
    public boolean mergeItemStack(ItemStack stackIn, int index, int index0, boolean flagIn) {
        boolean flag = false;
        int i = index;
        if (flagIn) { //从后往前
            i = index0 - 1;
        }

        Slot slot1;
        ItemStack itemstack; //空物品
        if (stackIn.isStackable()) { //可堆叠的
            while(!stackIn.isEmpty()) {
                if (flagIn) {
                    if (i < index) {
                        break;
                    }
                } else if (i >= index0) {
                    break;
                }

                slot1 = (Slot)this.inventorySlots.get(i);
                itemstack = slot1.getStack(); //当前格物品
                if (!itemstack.isEmpty() && areItemsAndTagsEqual(stackIn, itemstack)) { //相同
                    int j = itemstack.getCount() + stackIn.getCount(); //总数量
                    int maxSize = Math.max(slot1.getSlotStackLimit(), stackIn.getMaxStackSize()); //允许的最大堆叠数量
                    if (j <= maxSize) {
                        stackIn.setCount(0);
                        itemstack.setCount(j);
                        slot1.onSlotChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stackIn.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot1.onSlotChanged();
                        flag = true;
                    }
                }

                if (flagIn) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!stackIn.isEmpty()) { //不为空
            if (flagIn) {
                i = index0 - 1;
            } else {
                i = index;
            }

            while(true) {
                if (flagIn) {
                    if (i < index) {
                        break;
                    }
                } else if (i >= index0) {
                    break;
                }

                slot1 = (Slot)this.inventorySlots.get(i);
                itemstack = slot1.getStack();
                if (itemstack.isEmpty() && slot1.isItemValid(stackIn)) {
                    if (stackIn.getCount() > slot1.getSlotStackLimit()) {
                        slot1.putStack(stackIn.split(slot1.getSlotStackLimit()));
                    } else {
                        slot1.putStack(stackIn.split(stackIn.getCount()));
                    }

                    slot1.onSlotChanged();
                    flag = true;
                    break;
                }

                if (flagIn) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

    @Override
    public ItemStack slotClick(int slot, int button, ClickType clickType, PlayerEntity player) {
        try {
            return this.clickSlot(slot, button, clickType, player);
        } catch (Exception var8) {
            CrashReport crashreport = CrashReport.makeCrashReport(var8, "Container click");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Click info");
            crashreportcategory.addDetail("Menu Type", () -> Registry.MENU.getKey(this.getType()).toString());
            crashreportcategory.addDetail("Menu Class", () -> this.getClass().getCanonicalName());
            crashreportcategory.addDetail("Slot Count", this.inventorySlots.size());
            crashreportcategory.addDetail("Slot", slot);
            crashreportcategory.addDetail("Button", button);
            crashreportcategory.addDetail("Type", clickType);
            throw new ReportedException(crashreport);
        }
    }

    /**
     * 玩家点击一个格子
     * @param slot 点击的容器格子
     * @param button 鼠标按键0左键1右键
     * @param type 点击类型
     * @param player 玩家
     * @return 鼠标上的物品
     */
    private ItemStack clickSlot(int slot, int button, ClickType type, PlayerEntity player) {
        ItemStack itemstack = ItemStack.EMPTY;
        PlayerInventory playerinventory = player.inventory;
        ItemStack itemstack9; //玩家操作的物品
        ItemStack itemstack11;
        int k3;
        int k1;
        if (type == ClickType.QUICK_CRAFT) { //快速合成  移动均分
            int i1 = this.dragEvent;
            this.dragEvent = getDragEvent(button);
            if ((i1 != 1 || this.dragEvent != 2) && i1 != this.dragEvent) {
                this.resetDrag();
            } else if (playerinventory.getItemStack().isEmpty()) {
                this.resetDrag();
            } else if (this.dragEvent == 0) {
                this.dragMode = extractDragMode(button);
                if (isValidDragMode(this.dragMode, player)) {
                    this.dragEvent = 1;
                    this.dragSlots.clear();
                } else {
                    this.resetDrag();
                }
            } else if (this.dragEvent == 1) {
                Slot slot7 = this.inventorySlots.get(slot);
                itemstack11 = playerinventory.getItemStack();
                if (slot7 != null && canAddItemToSlot(slot7, itemstack11, true) && slot7.isItemValid(itemstack11) && (this.dragMode == 2 || itemstack11.getCount() > this.dragSlots.size()) && this.canDragIntoSlot(slot7)) {
                    this.dragSlots.add(slot7);
                }
            } else if (this.dragEvent == 2) {
                if (!this.dragSlots.isEmpty()) {
                    itemstack9 = playerinventory.getItemStack().copy();
                    k1 = playerinventory.getItemStack().getCount();
                    Iterator<Slot> var23 = this.dragSlots.iterator();

                    label334:
                    while(true) {
                        Slot slot8;
                        ItemStack itemstack13;
                        do {
                            do {
                                do {
                                    do {
                                        if (!var23.hasNext()) {
                                            itemstack9.setCount(k1);
                                            playerinventory.setItemStack(itemstack9);
                                            break label334;
                                        }

                                        slot8 = (Slot)var23.next();
                                        itemstack13 = playerinventory.getItemStack();
                                    } while(slot8 == null);
                                } while(!canAddItemToSlot(slot8, itemstack13, true));
                            } while(!slot8.isItemValid(itemstack13));
                        } while(this.dragMode != 2 && itemstack13.getCount() < this.dragSlots.size());

                        if (this.canDragIntoSlot(slot8)) {
                            ItemStack itemstack14 = itemstack9.copy();
                            int j3 = slot8.getHasStack() ? slot8.getStack().getCount() : 0;
                            computeStackSize(this.dragSlots, this.dragMode, itemstack14, j3);
                            k3 = Math.min(itemstack14.getMaxStackSize(), slot8.getItemStackLimit(itemstack14));
                            if (itemstack14.getCount() > k3) {
                                itemstack14.setCount(k3);
                            }

                            k1 -= itemstack14.getCount() - j3;
                            slot8.putStack(itemstack14);
                        }
                    }
                }

                this.resetDrag();
            } else {
                this.resetDrag();
            }
        } else if (this.dragEvent != 0) {
            this.resetDrag();
        } else {
            Slot slot6;
            int l2;
            if (type != ClickType.PICKUP && type != ClickType.QUICK_MOVE || button != 0 && button != 1) {
                if (type == ClickType.SWAP) { //交换物品
                    slot6 = this.inventorySlots.get(slot);
                    itemstack9 = playerinventory.getStackInSlot(button);
                    itemstack11 = slot6.getStack();
                    if (!itemstack9.isEmpty() || !itemstack11.isEmpty()) {
                        if (itemstack9.isEmpty()) {
                            if (slot6.canTakeStack(player)) {
                                playerinventory.setInventorySlotContents(button, itemstack11);
                                slot6.onSwapCraft(itemstack11.getCount());
                                slot6.putStack(ItemStack.EMPTY);
                                slot6.onTake(player, itemstack11);
                            }
                        } else if (itemstack11.isEmpty()) {
                            if (slot6.isItemValid(itemstack9)) {
                                l2 = slot6.getItemStackLimit(itemstack9);
                                if (itemstack9.getCount() > l2) {
                                    slot6.putStack(itemstack9.split(l2));
                                } else {
                                    slot6.putStack(itemstack9);
                                    playerinventory.setInventorySlotContents(button, ItemStack.EMPTY);
                                }
                            }
                        } else if (slot6.canTakeStack(player) && slot6.isItemValid(itemstack9)) {
                            l2 = slot6.getItemStackLimit(itemstack9);
                            if (itemstack9.getCount() > l2) {
                                slot6.putStack(itemstack9.split(l2));
                                slot6.onTake(player, itemstack11);
                                if (!playerinventory.addItemStackToInventory(itemstack11)) {
                                    player.dropItem(itemstack11, true);
                                }
                            } else {
                                slot6.putStack(itemstack9);
                                playerinventory.setInventorySlotContents(button, itemstack11);
                                slot6.onTake(player, itemstack11);
                            }
                        }
                    }
                } else if (type == ClickType.CLONE && player.abilities.isCreativeMode && playerinventory.getItemStack().isEmpty() && slot >= 0) {
                    slot6 = this.inventorySlots.get(slot); //创造模式复制物品
                    if (slot6 != null && slot6.getHasStack()) {
                        itemstack9 = slot6.getStack().copy();
                        itemstack9.setCount(itemstack9.getMaxStackSize());
                        playerinventory.setItemStack(itemstack9);
                    }
                } else if (type == ClickType.THROW && playerinventory.getItemStack().isEmpty() && slot >= 0) {
                    slot6 = (Slot)this.inventorySlots.get(slot); //丢出物品
                    if (slot6 != null && slot6.getHasStack() && slot6.canTakeStack(player)) {
                        itemstack9 = slot6.decrStackSize(button == 0 ? 1 : slot6.getStack().getCount());
                        slot6.onTake(player, itemstack9);
                        player.dropItem(itemstack9, true);
                    }
                } else if (type == ClickType.PICKUP_ALL && slot >= 0) {
                    slot6 = (Slot)this.inventorySlots.get(slot); //合并所有相同物品
                    itemstack9 = playerinventory.getItemStack(); //玩家物品
                    if (!itemstack9.isEmpty() && (slot6 == null || !slot6.getHasStack() || !slot6.canTakeStack(player))) {
                        k1 = button == 0 ? 0 : this.inventorySlots.size() - 1;
                        l2 = button == 0 ? 1 : -1;

                        for(int j = 0; j < 2; ++j) {
                            for(int k = k1; k >= 0 && k < this.inventorySlots.size() && itemstack9.getCount() < itemstack9.getMaxStackSize(); k += l2) {
                                Slot slot1 = (Slot)this.inventorySlots.get(k);
                                if (slot1.getHasStack() && canAddItemToSlot(slot1, itemstack9, true) && slot1.canTakeStack(player) && this.canMergeSlot(itemstack9, slot1)) {
                                    ItemStack itemstack3 = slot1.getStack();
                                    if (j != 0 || itemstack3.getCount() != itemstack3.getMaxStackSize()) {
                                        k3 = Math.min(itemstack9.getMaxStackSize() - itemstack9.getCount(), itemstack3.getCount());
                                        ItemStack itemstack4 = slot1.decrStackSize(k3);
                                        itemstack9.grow(k3);
                                        if (itemstack4.isEmpty()) {
                                            slot1.putStack(ItemStack.EMPTY);
                                        }

                                        slot1.onTake(player, itemstack4);
                                    }
                                }
                            }
                        }
                    }

                    this.detectAndSendChanges();
                }
            } else if (slot == -999)//错误格
            {
                if (!playerinventory.getItemStack().isEmpty()) {
                    if (button == 0) {
                        player.dropItem(playerinventory.getItemStack(), true);
                        playerinventory.setItemStack(ItemStack.EMPTY);
                    }

                    if (button == 1) {
                        player.dropItem(playerinventory.getItemStack().split(1), true);
                    }
                }
            } else if (type == ClickType.QUICK_MOVE) //快速移动物品
            {
                if (slot < 0) {
                    return ItemStack.EMPTY;
                }

                slot6 = this.inventorySlots.get(slot);
                if (slot6 == null || !slot6.canTakeStack(player)) {
                    return ItemStack.EMPTY;
                }

                for(itemstack9 = this.transferStackInSlot(player, slot); !itemstack9.isEmpty() && ItemStack.areItemsEqual(slot6.getStack(), itemstack9); itemstack9 = this.transferStackInSlot(player, slot)) {
                    itemstack = itemstack9.copy();
                }
            } else  //从容器拿放
            {
                if (slot < 0) {
                    return ItemStack.EMPTY;  //放入物品容器
                }

                slot6 = this.inventorySlots.get(slot);
                if (slot6 != null) {
                    itemstack9 = slot6.getStack(); //物品格
                    itemstack11 = playerinventory.getItemStack();
                    if (!itemstack9.isEmpty()) {
                        itemstack = itemstack9.copy();
                    }

                    if (itemstack9.isEmpty()) { //物品格为空
                        if (!itemstack11.isEmpty() && slot6.isItemValid(itemstack11)) {
                            l2 = button == 0 ? itemstack11.getMaxStackSize() : 1; //左键 还是右键  左键放完 右键放一个
                            if (l2 > slot6.getItemStackLimit(itemstack11)) {
                                l2 = slot6.getItemStackLimit(itemstack11); //超过堆叠数
                            }

                            slot6.putStack(itemstack11.split(l2)); //只放64 有剩余
                        }
                    } else if (slot6.canTakeStack(player)) { //容器物品格不为空 拿取
                        if (itemstack11.isEmpty()) { //空物品
                            if (itemstack9.isEmpty()) {
                                slot6.putStack(ItemStack.EMPTY);
                                playerinventory.setItemStack(ItemStack.EMPTY);
                            } else {
                                int i = (int) Math.floor((itemstack9.getCount() + 1) / 2.0); //右键取一半物品
                                l2 = button == 0 ? itemstack9.getMaxStackSize() : Math.min(i, itemstack9.getMaxStackSize());
                                playerinventory.setItemStack(slot6.decrStackSize(l2));
                                if (itemstack9.isEmpty()) {
                                    slot6.putStack(ItemStack.EMPTY);
                                }

                                slot6.onTake(player, playerinventory.getItemStack());
                            }
                        } else if (slot6.isItemValid(itemstack11)) { //能否放进此物品
                            if (areItemsAndTagsEqual(itemstack9, itemstack11)) { //物品相同 超过则放部分
                                l2 = button == 0 ? itemstack11.getCount() : 1;
                                if (l2 > slot6.getItemStackLimit(itemstack11) - itemstack9.getCount()) {
                                    l2 = slot6.getItemStackLimit(itemstack11) - itemstack9.getCount();
                                }

                                itemstack11.shrink(l2);
                                itemstack9.grow(l2);
                            } else if (itemstack11.getCount() <= slot6.getItemStackLimit(itemstack11)) { //否则全放
                                slot6.putStack(itemstack11);
                                playerinventory.setItemStack(itemstack9);
                            }//不能放入但是容器有物品 则合并到玩家鼠标物品
                        } else if (itemstack11.getMaxStackSize() > 1 && areItemsAndTagsEqual(itemstack9, itemstack11) && !itemstack9.isEmpty()) {
                            l2 = itemstack9.getCount(); //容器物品数量
                            if (l2 + itemstack11.getCount() <= itemstack11.getMaxStackSize()) {
                                itemstack11.grow(l2);
                                itemstack9 = slot6.decrStackSize(l2);
                                if (itemstack9.isEmpty()) {
                                    slot6.putStack(ItemStack.EMPTY); //取完置为空
                                }

                                slot6.onTake(player, playerinventory.getItemStack());
                            }
                        }
                    }

                    slot6.onSlotChanged();
                }
            }
        }

        return itemstack;
    }
}
