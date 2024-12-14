package com.yuo.endless.Container.Chest;

import com.yuo.endless.Container.ContainerTypeRegistry;
import com.yuo.endless.Tiles.InfinityBoxTile;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Iterator;
import java.util.Optional;

public class InfinityBoxContainer extends InfinityChestContainer {
    private final InfinityBoxCraftInventory craftInputInv;
    private final InfinityBoxCraftInventoryResult craftOutputInv;
    private final IIntArray burnData;
    private final PlayerEntity player;
    private final World world;

    public InfinityBoxContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory, new InfinityBoxTile(), new IntArray(4));
    }

    public InfinityBoxContainer(int id, PlayerInventory playerInventory, InfinityBoxTile tile, IIntArray intArray) {
        super(ContainerTypeRegistry.infinityBoxContainer.get(), id);
        this.chestTile = tile;
        this.craftInputInv = new InfinityBoxCraftInventory(this, (InfinityBoxTile) chestTile);
        this.craftOutputInv = new InfinityBoxCraftInventoryResult((InfinityBoxTile) chestTile);
        this.burnData = intArray;
        trackIntArray(this.burnData);
        this.player = playerInventory.player;
        this.world = player.world;
        chestTile.openInventory(this.player);

        for(int j = 0; j < 9; ++j) {
            for(int k = 0; k < 27; ++k) {
                this.addSlot(new InfinityBoxSlot(chestTile, k + j * 27, 8 + k * 18, 18 + j * 18));
            }
        }

        //合成
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                this.addSlot(new Slot(craftInputInv, j + i * 3 + 243,368 + j * 18, 194 + i * 18));
            }
        }
        this.addSlot(new InfinityBoxCraftResultSlot(player, craftInputInv, craftOutputInv, 252, 462, 212));

        //烧炼
        this.addSlot(new Slot(chestTile, 253, 62, 194));
        this.addSlot(new InfinityBoxBurnFuelSlot(this, chestTile, 254, 62, 230));
        this.addSlot(new InfinityBoxBurnResultSlot(player, chestTile, 255, 122, 212));

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 170 + j1 * 18, 194 + l * 18));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 170 + i1 * 18, 252));
        }
        onCraftMatrixChanged(craftInputInv);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        if (world.isRemote) return;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
        ItemStack itemStack = ItemStack.EMPTY;
        CraftingInventory craftingInv = getCraftingInv();
        Optional<ICraftingRecipe> optional = world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftingInv, world);
        if (optional.isPresent()) {
            ICraftingRecipe recipe = optional.get();
            if (craftOutputInv.canUseRecipe(world, serverPlayer, recipe)) {
                itemStack = recipe.getCraftingResult(craftingInv); //获取配方输出
            }
        }
        craftOutputInv.setInventorySlotContents(252, itemStack);
        serverPlayer.connection.sendPacket(new SSetSlotPacket(windowId, 252, itemStack));
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();

            if (index < 243) { //取出
                if (!super.mergeItemStack(itemStack1, 253, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }//放入
            } else if (index == 252){
                if (!super.mergeItemStack(itemStack1, 253, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemStack1, itemstack);
            } else if (!this.mergeItemStack(itemStack1, 0, 243, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
            slot.onTake(player, itemStack1);
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.chestTile.closeInventory(playerIn);
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgress() {
        int i = this.burnData.get(2);
        return i != 0 ? (int) Math.ceil(i / 160.0 * 22): 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnProgress() {
        int i = this.burnData.get(1);
        if (i == 0) {
            i = 200;
        }

        return (int) Math.ceil(this.burnData.get(0) / (i * 1.0) * 14);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isBurning() {
        return this.burnData.get(0) > 0;
    }

    public boolean hasRecipe(ItemStack stack) {
        return this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(stack), this.world).isPresent();
    }

    public boolean isFuel(ItemStack stack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0;
    }

    /**
     * 创建一个工作台容器
     * @return 容器
     */
    public CraftingInventory getCraftingInv(){
        CraftingInventory inventory = new CraftingInventory(new WorkbenchContainer(windowId, player.inventory), 3,3);
        inventory.setInventorySlotContents(0, craftInputInv.getStackInSlot(243));
        inventory.setInventorySlotContents(1, craftInputInv.getStackInSlot(244));
        inventory.setInventorySlotContents(2, craftInputInv.getStackInSlot(245));
        inventory.setInventorySlotContents(3, craftInputInv.getStackInSlot(246));
        inventory.setInventorySlotContents(4, craftInputInv.getStackInSlot(247));
        inventory.setInventorySlotContents(5, craftInputInv.getStackInSlot(248));
        inventory.setInventorySlotContents(6, craftInputInv.getStackInSlot(249));
        inventory.setInventorySlotContents(7, craftInputInv.getStackInSlot(250));
        inventory.setInventorySlotContents(8, craftInputInv.getStackInSlot(251));
        return inventory;
    }

}
