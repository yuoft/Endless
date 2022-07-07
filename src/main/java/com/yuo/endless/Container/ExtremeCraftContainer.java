package com.yuo.endless.Container;

import com.yuo.endless.Config.Config;
import com.yuo.endless.Recipe.ExtremeCraftRecipe;
import com.yuo.endless.Recipe.ExtremeCraftingManager;
import com.yuo.endless.Recipe.RecipeTypeRegistry;
import com.yuo.endless.Tiles.ExtremeCraftTile;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.World;

import java.util.Optional;

public class ExtremeCraftContainer extends RecipeBookContainer<CraftingInventory> {

    private final ExtremeCraftInventory inputInventory;
    private final ExtremeCraftInventoryResult outputInventory;
    private final PlayerEntity player;
    private final World world;


    public ExtremeCraftContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory, new ExtremeCraftTile());
    }

    public ExtremeCraftContainer(int id, PlayerInventory playerInventory, ExtremeCraftTile tile) {
        super(ContainerTypeRegistry.extremeCraftContainer.get(), id);
        this.inputInventory = new ExtremeCraftInventory(this, tile);
        this.outputInventory = new ExtremeCraftInventoryResult(tile);
        this.player = playerInventory.player;
        this.world = playerInventory.player.world;
        //添加9*9合成栏
        for (int m = 0; m < 9; m++){
            for (int n = 0; n < 9; n++){
                this.addSlot(new Slot(inputInventory, n + m * 9, 12 + n * 18, 8 + m * 18));
            }
        }
        //添加输出栏
        this.addSlot(new ExtremeCraftReslutSlot(playerInventory.player, inputInventory, outputInventory, 81, 210 , 80));
        //添加玩家物品栏
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 39 + j * 18, 174 + i * 18));
            }
        }
        //添加玩家快捷栏
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 39 + k * 18, 232));
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
        Optional<ExtremeCraftRecipe> recipeOptional = world.getRecipeManager().getRecipe(RecipeTypeRegistry.EXTREME_CRAFT_RECIPE, inputInventory, world);
        if (recipeOptional.isPresent()){
            ExtremeCraftRecipe recipe = recipeOptional.get();
            if (outputInventory.canUseRecipe(world, serverPlayer, recipe)){
                itemStack = recipe.getCraftingResult(inputInventory);
            }
        }else {
            CraftingInventory craftingInv = getCraftingInv();
            Optional<ICraftingRecipe> optional = world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftingInv, world);
            if (optional.isPresent()) {
                ICraftingRecipe recipe = optional.get();
                if (outputInventory.canUseRecipe(world, serverPlayer, recipe)) {
                    itemStack = recipe.getCraftingResult(craftingInv); //获取配方输出
                }
            }else {
                itemStack = ExtremeCraftingManager.getInstance().getRecipeOutPut(inputInventory, world);
            }
        }
        outputInventory.setInventorySlotContents(81, itemStack);
        serverPlayer.connection.sendPacket(new SSetSlotPacket(windowId, 81, itemStack));
    }

    /**
     * 创建一个工作台容器
     * @return 容器
     */
    private CraftingInventory getCraftingInv(){
        CraftingInventory inventory = new CraftingInventory(new WorkbenchContainer(windowId, player.inventory), 3,3);
        inventory.setInventorySlotContents(0, inputInventory.getStackInSlot(0));
        inventory.setInventorySlotContents(1, inputInventory.getStackInSlot(1));
        inventory.setInventorySlotContents(2, inputInventory.getStackInSlot(2));
        inventory.setInventorySlotContents(3, inputInventory.getStackInSlot(9));
        inventory.setInventorySlotContents(4, inputInventory.getStackInSlot(10));
        inventory.setInventorySlotContents(5, inputInventory.getStackInSlot(11));
        inventory.setInventorySlotContents(6, inputInventory.getStackInSlot(18));
        inventory.setInventorySlotContents(7, inputInventory.getStackInSlot(19));
        inventory.setInventorySlotContents(8, inputInventory.getStackInSlot(20));
        return inventory;
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
            if (index == 81){
                if (!this.mergeItemStack(itemStack1, 82, 118, true)) return ItemStack.EMPTY;
                slot.onSlotChange(itemStack1, itemstack);
            } else if (index >= 82){
                if (ExtremeCraftingManager.getInstance().isRecipeInput(itemStack1)){
                    if (!this.mergeItemStack(itemStack1, 0, 81, false)) return ItemStack.EMPTY;
                }
                if (index < 109) { //从物品栏到快捷栏
                    if (!this.mergeItemStack(itemStack1, 109, 118, false)) return ItemStack.EMPTY;
                } else if (index < 118) {
                    if (!this.mergeItemStack(itemStack1, 82, 109, false)) return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemStack1, 82, 118, false)) return ItemStack.EMPTY; //从合成台取出来

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
        this.inputInventory.clear();
        this.outputInventory.clear();
    }

    @Override
    public boolean matches(IRecipe<? super CraftingInventory> recipeIn) {
        return recipeIn.matches(this.inputInventory, this.player.world);
    }

    @Override
    public int getOutputSlot() {
        return 81;
    }

    @Override
    public int getWidth() {
        return 9;
    }

    @Override
    public int getHeight() {
        return 9;
    }

    @Override
    public int getSize() {
        return 82;
    }

    @Override
    public RecipeBookCategory func_241850_m() {
        return null;
    }

//    //获取配方输出
//    @Nullable
//    public ItemStack getRecipeOut(ExtremeCraftInventory inventory) {
//        Set<IRecipe<?>> recipes = findRecipesByType(EndlessRecipeType.EXTREME_CRAFT, this.player.world);
//        for (IRecipe<?> iRecipe : recipes) {
//            ExtremeCraftRecipe recipe = (ExtremeCraftRecipe) iRecipe;
//            if (recipe.matches(inventory, this.player.world)) {
//                return recipe.getRecipeOutput();
//            }
//        }
//
//        return ItemStack.EMPTY;
//    }

//    public static Set<IRecipe<?>> findRecipesByType(IRecipeType<?> typeIn, World world) {
//        return world != null ? world.getRecipeManager().getRecipes().stream()
//                .filter(recipe -> recipe.getType() == typeIn).collect(Collectors.toSet()) : Collections.emptySet();
//    }

}
