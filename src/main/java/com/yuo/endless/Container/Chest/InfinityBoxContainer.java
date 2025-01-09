package com.yuo.endless.Container.Chest;

import com.yuo.endless.Container.EndlessMenuTypes;
import com.yuo.endless.Tiles.InfinityBoxTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;

import java.util.Optional;

public class InfinityBoxContainer extends InfinityChestContainer {
    private final InfinityBoxCraftInventory craftInputInv;
    private final InfinityBoxCraftInventoryResult craftOutputInv;
    private final ContainerData burnData;
    private final Player player;
    private final Level world;

    public InfinityBoxContainer(int id, Inventory playerInventory, FriendlyByteBuf buf){
        this(id, playerInventory, (Container) playerInventory.player.level.getBlockEntity(buf.readBlockPos()));
    }

    public InfinityBoxContainer(int id, Inventory playerInventory, Container tile) {
        super(EndlessMenuTypes.infinityBoxContainer.get(), id);
        this.chestTile = (InfinityBoxTile) tile;
        this.craftInputInv = new InfinityBoxCraftInventory(this, (InfinityBoxTile) chestTile);
        this.craftOutputInv = new InfinityBoxCraftInventoryResult((InfinityBoxTile) chestTile);
        this.burnData = ((InfinityBoxTile) chestTile).burnData;
        this.addDataSlots(this.burnData);
        this.player = playerInventory.player;
        this.world = player.level;
        chestTile.startOpen(this.player);

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
        slotsChanged(craftInputInv);
    }

    @Override
    public void slotsChanged(Container pContainer) {
        if (world.isClientSide) return;
        ServerPlayer serverPlayer = (ServerPlayer)player;
        ItemStack itemStack = ItemStack.EMPTY;
        CraftingContainer craftingInv = getCraftingInv();
        Optional<CraftingRecipe> optional = world.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInv, world);
        if (optional.isPresent()) {
            CraftingRecipe recipe = optional.get();
            if (craftOutputInv.setRecipeUsed(world, serverPlayer, recipe)) {
                itemStack = recipe.getResultItem(); //获取配方输出
            }
        }
        craftOutputInv.setItem(252, itemStack);
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.getStateId(), 252, itemStack));
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();

            if (index < 243) { //取出
                if (!super.moveItemStackTo(itemStack1, 253, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }//放入
            } else if (index == 252){
                if (!super.moveItemStackTo(itemStack1, 253, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemStack1, itemstack);
            } else if (!this.moveItemStackTo(itemStack1, 0, 243, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }
            slot.onTake(player, itemStack1);
        }

        return itemstack;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.chestTile.clearContent();
        this.chestTile.stopOpen(pPlayer);
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
        return this.world.getRecipeManager().getRecipesFor(RecipeType.SMELTING, new SimpleContainer(stack), this.world).isEmpty();
    }

    public boolean isFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null) > 0;
    }

    /**
     * 创建一个工作台容器
     * @return 容器
     */
    public CraftingContainer getCraftingInv(){
        CraftingContainer inventory = new CraftingContainer(new AbstractContainerMenu(MenuType.CRAFTING, this.containerId) {
            @Override
            public boolean stillValid(Player player) {
                return true;
            }
        }, 3, 3);
        inventory.setItem(0, craftInputInv.getItem(243));
        inventory.setItem(1, craftInputInv.getItem(244));
        inventory.setItem(2, craftInputInv.getItem(245));
        inventory.setItem(3, craftInputInv.getItem(246));
        inventory.setItem(4, craftInputInv.getItem(247));
        inventory.setItem(5, craftInputInv.getItem(248));
        inventory.setItem(6, craftInputInv.getItem(249));
        inventory.setItem(7, craftInputInv.getItem(250));
        inventory.setItem(8, craftInputInv.getItem(251));
        return inventory;
    }

}
