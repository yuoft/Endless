package com.yuo.endless.Container.Chest;

import com.yuo.endless.Tiles.AbsEndlessChestTile;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.core.Registry;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Iterator;

public class InfinityChestContainer extends AbstractContainerMenu {
    protected AbsEndlessChestTile chestTile;

    protected InfinityChestContainer(@Nullable MenuType<?> type, int id) {
        super(type, id);
        chestTile = null;
    }


    @Override
    public boolean stillValid(Player player) {
        return this.chestTile.stillValid(player);
    }


    /**
     * shift 移动物品
     * @param stackIn 物品
     * @param index 开始查找序号
     * @param index0 结束序号
     * @param flagIn 查找顺序
     * @return 结果
     */
    @Override
    public boolean moveItemStackTo(ItemStack stackIn, int index, int index0, boolean flagIn) {
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

                slot1 = (Slot)this.slots.get(i);
                itemstack = slot1.getItem(); //当前格物品
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(stackIn, itemstack)) { //相同
                    int j = itemstack.getCount() + stackIn.getCount(); //总数量
                    int maxSize = Math.max(slot1.getMaxStackSize(), stackIn.getMaxStackSize()); //允许的最大堆叠数量
                    if (j <= maxSize) {
                        stackIn.setCount(0);
                        itemstack.setCount(j);
                        slot1.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stackIn.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot1.setChanged();
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

                slot1 = (Slot)this.slots.get(i);
                itemstack = slot1.getItem();
                if (itemstack.isEmpty() && slot1.mayPlace(stackIn)) {
                    if (stackIn.getCount() > slot1.getMaxStackSize()) {
                        slot1.set(stackIn.split(slot1.getMaxStackSize()));
                    } else {
                        slot1.set(stackIn.split(stackIn.getCount()));
                    }

                    slot1.setChanged();
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
    public void clicked(int slot, int button, ClickType clickType, Player player) {
        try {
            this.doClick(slot, button, clickType, player);
        } catch (Exception var8) {
            CrashReport crashreport = CrashReport.forThrowable(var8, "Container click");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Click info");
            crashreportcategory.setDetail("Menu Type", () -> Registry.MENU.getKey(this.getType()).toString());
            crashreportcategory.setDetail("Menu Class", () -> this.getClass().getCanonicalName());
            crashreportcategory.setDetail("Slot Count", this.slots.size());
            crashreportcategory.setDetail("Slot", slot);
            crashreportcategory.setDetail("Button", button);
            crashreportcategory.setDetail("Type", clickType);
            throw new ReportedException(crashreport);
        }
    }

    /**
     * 玩家点击一个格子
     * @param slot 点击的容器格子
     * @param button 鼠标按键0左键1右键
     * @param type 点击类型
     * @param player 玩家
     */
    private void doClick(int slot, int button, ClickType type, Player player) {
        ItemStack itemstack = ItemStack.EMPTY;
        Inventory playerinventory = player.getInventory();
        ItemStack itemstack9; //玩家操作的物品
        ItemStack itemstack11;
        int k3;
        int k1;
        if (type == ClickType.QUICK_CRAFT) { //快速合成  移动均分
            int i1 = this.quickcraftStatus;
            this.quickcraftStatus = getQuickcraftHeader(button);
            if ((i1 != 1 || this.quickcraftStatus != 2) && i1 != this.quickcraftStatus) {
                this.resetQuickCraft();
            } else if (this.getCarried().isEmpty()) {
                this.resetQuickCraft();
            } else if (this.quickcraftStatus == 0) {
                this.quickcraftType = getQuickcraftType(button);
                if (isValidQuickcraftType(this.quickcraftType, player)) {
                    this.quickcraftStatus = 1;
                    this.quickcraftSlots.clear();
                } else {
                    this.resetQuickCraft();
                }
            } else if (this.quickcraftStatus == 1) {
                Slot slot7 = this.slots.get(slot);
                itemstack11 = this.getCarried();
                if (canItemQuickReplace(slot7, itemstack11, true) && slot7.mayPlace(itemstack11) && (this.quickcraftType == 2 || itemstack11.getCount() > this.quickcraftSlots.size()) && this.canDragTo(slot7)) {
                    this.quickcraftSlots.add(slot7);
                }
            } else if (this.quickcraftStatus == 2) {
                if (!this.quickcraftSlots.isEmpty()) {
                    itemstack9 = this.getCarried().copy();
                    k1 = this.getCarried().getCount();
                    Iterator<Slot> var23 = this.quickcraftSlots.iterator();

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
                                            this.setCarried(itemstack9);
                                            break label334;
                                        }

                                        slot8 = (Slot)var23.next();
                                        itemstack13 = this.getCarried();
                                    } while(slot8 == null);
                                } while(!canItemQuickReplace(slot8, itemstack13, true));
                            } while(!slot8.mayPlace(itemstack13));
                        } while(this.quickcraftType != 2 && itemstack13.getCount() < this.quickcraftSlots.size());

                        if (this.canDragTo(slot8)) {
                            ItemStack itemstack14 = itemstack9.copy();
                            int j3 = slot8.hasItem() ? slot8.getItem().getCount() : 0;
                            getQuickCraftSlotCount(this.quickcraftSlots, this.quickcraftType, itemstack14, j3);
                            k3 = Math.min(itemstack14.getMaxStackSize(), slot8.getMaxStackSize(itemstack14));
                            if (itemstack14.getCount() > k3) {
                                itemstack14.setCount(k3);
                            }

                            k1 -= itemstack14.getCount() - j3;
                            slot8.set(itemstack14);
                        }
                    }
                }

                this.resetQuickCraft();
            } else {
                this.resetQuickCraft();
            }
        } else if (this.quickcraftStatus != 0) {
            this.resetQuickCraft();
        } else {
            Slot slot6;
            int l2;
            if (type != ClickType.PICKUP && type != ClickType.QUICK_MOVE || button != 0 && button != 1) {
                if (type == ClickType.SWAP) { //交换物品
                    slot6 = this.slots.get(slot);
                    itemstack9 = playerinventory.getItem(button);
                    itemstack11 = slot6.getItem();
                    if (!itemstack9.isEmpty() || !itemstack11.isEmpty()) {
                        if (itemstack9.isEmpty()) {
                            if (slot6.mayPickup(player)) {
                                playerinventory.setItem(button, itemstack11);
                                slot6.onSwapCraft(itemstack11.getCount());
                                slot6.set(ItemStack.EMPTY);
                                slot6.onTake(player, itemstack11);
                            }
                        } else if (itemstack11.isEmpty()) {
                            if (slot6.mayPlace(itemstack9)) {
                                l2 = slot6.getMaxStackSize(itemstack9);
                                if (itemstack9.getCount() > l2) {
                                    slot6.set(itemstack9.split(l2));
                                } else {
                                    slot6.set(itemstack9);
                                    playerinventory.setItem(button, ItemStack.EMPTY);
                                }
                            }
                        } else if (slot6.mayPickup(player) && slot6.mayPlace(itemstack9)) {
                            l2 = slot6.getMaxStackSize(itemstack9);
                            if (itemstack9.getCount() > l2) {
                                slot6.set(itemstack9.split(l2));
                                slot6.onTake(player, itemstack11);
                                if (!playerinventory.add(itemstack11)) {
                                    player.drop(itemstack11, true);
                                }
                            } else {
                                slot6.set(itemstack9);
                                playerinventory.setItem(button, itemstack11);
                                slot6.onTake(player, itemstack11);
                            }
                        }
                    }
                } else if (type == ClickType.CLONE && player.getAbilities().instabuild && this.getCarried().isEmpty() && slot >= 0) {
                    slot6 = this.slots.get(slot); //创造模式复制物品
                    if (slot6.hasItem()) {
                        itemstack9 = slot6.getItem().copy();
                        itemstack9.setCount(itemstack9.getMaxStackSize());
                        this.setCarried(itemstack9);
                    }
                } else if (type == ClickType.THROW && this.getCarried().isEmpty() && slot >= 0) {
                    slot6 = (Slot)this.slots.get(slot); //丢出物品
                    k3 = button == 0 ? 1 : slot6.getItem().getCount();
                    itemstack9 = slot6.safeTake(k3, Integer.MAX_VALUE, player);
                    player.drop(itemstack9, true);
                } else if (type == ClickType.PICKUP_ALL && slot >= 0) {
                    slot6 = (Slot)this.slots.get(slot); //合并所有相同物品
                    itemstack9 = this.getCarried(); //玩家物品
                    if (!itemstack9.isEmpty() && (!slot6.hasItem() || !slot6.mayPickup(player))) {
                        k1 = button == 0 ? 0 : this.slots.size() - 1;
                        l2 = button == 0 ? 1 : -1;

                        for(int j = 0; j < 2; ++j) {
                            for(int k = k1; k >= 0 && k < this.slots.size() && itemstack9.getCount() < itemstack9.getMaxStackSize(); k += l2) {
                                Slot slot1 = (Slot)this.slots.get(k);
                                if (slot1.hasItem() && canItemQuickReplace(slot1, itemstack9, true) && slot1.mayPickup(player) && this.canTakeItemForPickAll(itemstack9, slot1)) {
                                    ItemStack itemstack3 = slot1.getItem();
                                    if (j != 0 || itemstack3.getCount() != itemstack3.getMaxStackSize()) {
                                        ItemStack itemStack = slot1.safeTake(itemstack3.getCount(), itemstack9.getMaxStackSize() - itemstack9.getCount(), player);
                                        itemstack9.grow(itemStack.getCount());
                                    }
                                }
                            }
                        }
                    }

                    this.broadcastChanges();
                }
            } else if (slot == -999)//错误格
            {
                if (!this.getCarried().isEmpty()) {
                    if (button == 0) {
                        player.drop(this.getCarried(), true);
                        this.setCarried(ItemStack.EMPTY);
                    } else  {
                        player.drop(this.getCarried().split(1), true);
                    }
                }
            } else if (type == ClickType.QUICK_MOVE) //快速移动物品
            {
                if (slot < 0) {
                    return;
                }

                slot6 = this.slots.get(slot);
                if (!slot6.mayPickup(player)) {
                    return;
                }

                for(itemstack9 = this.quickMoveStack(player, slot); !itemstack9.isEmpty() && ItemStack.isSame(slot6.getItem(), itemstack9); itemstack9 = this.quickMoveStack(player, slot)) {
                    itemstack = itemstack9.copy();
                }
            } else  //从容器拿放
            {
                if (slot < 0) {
                    return;  //放入物品容器
                }

                slot6 = this.slots.get(slot);
                itemstack9 = slot6.getItem(); //物品格
                itemstack11 = this.getCarried();
                if (!itemstack9.isEmpty()) {
                    itemstack = itemstack9.copy();
                }

                if (itemstack9.isEmpty()) { //物品格为空
                    if (!itemstack11.isEmpty() && slot6.mayPlace(itemstack11)) {
                        l2 = button == 0 ? itemstack11.getMaxStackSize() : 1; //左键 还是右键  左键放完 右键放一个
                        if (l2 > slot6.getMaxStackSize(itemstack11)) {
                            l2 = slot6.getMaxStackSize(itemstack11); //超过堆叠数
                        }

                        slot6.set(itemstack11.split(l2)); //只放64 有剩余
                    }
                } else if (slot6.mayPickup(player)) { //容器物品格不为空 拿取
                    if (itemstack11.isEmpty()) { //空物品
                        if (itemstack9.isEmpty()) {
                            slot6.set(ItemStack.EMPTY);
                            this.setCarried(ItemStack.EMPTY);
                        } else {
                            int i = (int) Math.floor((itemstack9.getCount() + 1) / 2.0); //右键取一半物品
                            l2 = button == 0 ? itemstack9.getMaxStackSize() : Math.min(i, itemstack9.getMaxStackSize());
                            this.setCarried(slot6.remove(l2));
                            if (itemstack9.isEmpty()) {
                                slot6.set(ItemStack.EMPTY);
                            }

                            slot6.onTake(player, this.getCarried());
                        }
                    } else if (slot6.mayPlace(itemstack11)) { //能否放进此物品
                        if (ItemStack.isSameItemSameTags(itemstack9, itemstack11)) { //物品相同 超过则放部分
                            l2 = button == 0 ? itemstack11.getCount() : 1;
                            if (l2 > slot6.getMaxStackSize(itemstack11) - itemstack9.getCount()) {
                                l2 = slot6.getMaxStackSize(itemstack11) - itemstack9.getCount();
                            }

                            itemstack11.shrink(l2);
                            itemstack9.grow(l2);
                        } else if (itemstack11.getCount() <= slot6.getMaxStackSize(itemstack11)) { //否则全放
                            slot6.set(itemstack11);
                            this.setCarried(itemstack9);
                        }//不能放入但是容器有物品 则合并到玩家鼠标物品
                    } else if (itemstack11.getMaxStackSize() > 1 && ItemStack.isSameItemSameTags(itemstack9, itemstack11) && !itemstack9.isEmpty()) {
                        l2 = itemstack9.getCount(); //容器物品数量
                        if (l2 + itemstack11.getCount() <= itemstack11.getMaxStackSize()) {
                            itemstack11.grow(l2);
                            itemstack9 = slot6.remove(l2);
                            if (itemstack9.isEmpty()) {
                                slot6.set(ItemStack.EMPTY); //取完置为空
                            }

                            slot6.onTake(player, this.getCarried());
                        }
                    }
                }

                slot6.setChanged();
            }
        }

    }

    public Container getContainer() {
        return this.chestTile;
    }
}
