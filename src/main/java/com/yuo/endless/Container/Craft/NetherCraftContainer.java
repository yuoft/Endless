package com.yuo.endless.Container.Craft;

import com.yuo.endless.Container.ContainerTypeRegistry;
import com.yuo.endless.Recipe.NetherCraftRecipe;
import com.yuo.endless.Recipe.NetherCraftShapeRecipe;
import com.yuo.endless.Recipe.RecipeTypeRegistry;
import com.yuo.endless.Tiles.NetherCraftTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.World;

import java.util.Optional;

public class NetherCraftContainer extends RecipeBookContainer<CraftingInventory> {

    private final ExtremeCraftInventory inputInventory;
    private final ExtremeCraftInventoryResult outputInventory;
    private final PlayerEntity player;
    private final World world;


    public NetherCraftContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory, new NetherCraftTile());
    }

    public NetherCraftContainer(int id, PlayerInventory playerInventory, NetherCraftTile tile) {
        super(ContainerTypeRegistry.netherCraftContainer.get(), id);
        this.inputInventory = new ExtremeCraftInventory(this, tile);
        this.outputInventory = new ExtremeCraftInventoryResult(tile);
        this.player = playerInventory.player;
        this.world = playerInventory.player.world;
        //5*5
        for (int m = 0; m < 5; m++){
            for (int n = 0; n < 5; n++){
                this.addSlot(new Slot(inputInventory, n + m * 5, 14 + n * 18, 18 + m * 18));
            }
        }
        //添加输出栏
        this.addSlot(new NetherCraftResultSlot(playerInventory.player, inputInventory, outputInventory, 25, 142 , 53));
        //添加玩家物品栏
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 124 + i * 18));
            }
        }
        //添加玩家快捷栏
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 182));
        }
        onCraftMatrixChanged(inputInventory);
    }

    //输入改变时设置输出
    @Override
    public void onCraftMatrixChanged(IInventory matrix) {
        if (world.isRemote) return;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
        ItemStack itemStack = ItemStack.EMPTY;
        //获取配方 先检查无尽配方
        Optional<NetherCraftRecipe> recipeOptional = world.getRecipeManager().getRecipe(RecipeTypeRegistry.NETHER_CRAFT_RECIPE, inputInventory, world);
        Optional<NetherCraftShapeRecipe> recipeOptionalIn = world.getRecipeManager().getRecipe(RecipeTypeRegistry.NETHER_CRAFT_SHAPE_RECIPE, inputInventory, world);
        if (recipeOptional.isPresent()){ //json配方 有序
            NetherCraftRecipe recipe = recipeOptional.get();
            if (outputInventory.canUseRecipe(world, serverPlayer, recipe)){
                itemStack = recipe.getCraftingResult(inputInventory);
            }
        }else if (recipeOptionalIn.isPresent()){ //无序配方
            NetherCraftShapeRecipe recipe = recipeOptionalIn.get();
            if (outputInventory.canUseRecipe(world, serverPlayer, recipe)){
                itemStack = recipe.getCraftingResult(inputInventory);
            }
        }
        outputInventory.setInventorySlotContents(25, itemStack);
        serverPlayer.connection.sendPacket(new SSetSlotPacket(windowId, 25, itemStack));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.inputInventory.isUsableByPlayer(playerIn);
    }

    //玩家shift行为
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();
            if (index == 25){
                if (!this.mergeItemStack(itemStack1, 26, 62, true)) return ItemStack.EMPTY;
                slot.onSlotChange(itemStack1, itemstack);
            } else if (index >= 26){
                if (index < 53){//从物品栏到工作台
                    if (!this.mergeItemStack(itemStack1, 0, 25, false)) return ItemStack.EMPTY;
                } else if (index < 62) {  //快捷栏到物品栏
                    if (!this.mergeItemStack(itemStack1, 26, 53, false)) return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemStack1, 26, 62, false)) return ItemStack.EMPTY; //从合成台取出来

            if (itemStack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }

    @Override
    public void fillStackedContents(RecipeItemHelper itemHelperIn) {
        if (this.inputInventory != null) {
            ((IRecipeHelperPopulator)this.inputInventory).fillStackedContents(itemHelperIn);
        }
    }

    @Override
    public void clear() {
//        this.inputInventory.clear();
        this.outputInventory.clear();
    }

    @Override
    public boolean matches(IRecipe<? super CraftingInventory> recipeIn) {
        return recipeIn.matches(this.inputInventory, this.player.world);
    }

    @Override
    public int getOutputSlot() {
        return 25;
    }

    @Override
    public int getWidth() {
        return 5;
    }

    @Override
    public int getHeight() {
        return 5;
    }

    @Override
    public int getSize() {
        return 26;
    }

    @Override
    public RecipeBookCategory func_241850_m() {
        return null;
    }
}
