package com.yuo.endless.Container;

import com.yuo.endless.Config;
import com.yuo.endless.Recipe.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ExtremeCraftContainer extends RecipeBookMenu<CraftingContainer> {

    private final ExtremeCraftInventory inputInventory;
    private final ExtremeCraftInventoryResult outputInventory;
    private final Player player;
    private final Level world;


    public ExtremeCraftContainer(int id, Inventory playerInventory, FriendlyByteBuf buf){
        this(id, playerInventory, (Container) playerInventory.player.level.getBlockEntity(buf.readBlockPos()));
    }

    public ExtremeCraftContainer(int id, Inventory playerInventory, Container tile) {
        super(EndlessMenuTypes.extremeCraftContainer.get(), id);
        this.inputInventory = new ExtremeCraftInventory(this, tile);
        this.outputInventory = new ExtremeCraftInventoryResult(tile);
        this.player = playerInventory.player;
        this.world = playerInventory.player.level;
        //添加9*9合成栏
        for (int m = 0; m < 9; m++){
            for (int n = 0; n < 9; n++){
                this.addSlot(new Slot(inputInventory, n + m * 9, 12 + n * 18, 8 + m * 18));
            }
        }
        //添加输出栏
        this.addSlot(new ExtremeCraftResultSlot(playerInventory.player, inputInventory, outputInventory, 81, 210 , 80));
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
        slotsChanged(inputInventory);
    }

    //输入改变时设置输出
    @Override
    public void slotsChanged(Container matrix) {
        if (world.isClientSide) return;
        ServerPlayer serverPlayer = (ServerPlayer)player;
        ItemStack itemStack = ItemStack.EMPTY;
        //获取配方 先检查无尽配方
        Optional<ExtremeCraftRecipe> recipeOptional = world.getRecipeManager().getRecipeFor(RecipeTypeRegistry.EXTREME_CRAFT_RECIPE, inputInventory, world);
        Optional<ExtremeCraftShapeRecipe> recipeOptionalIn = world.getRecipeManager().getRecipeFor(RecipeTypeRegistry.EXTREME_CRAFT_SHAPE_RECIPE, inputInventory, world);
        ExtremeCraftShapeRecipe shapeRecipe = ModRecipeManager.matchesRecipe(inputInventory, world);
        if (recipeOptional.isPresent()){ //json配方 有序
            ExtremeCraftRecipe recipe = recipeOptional.get();
            if (outputInventory.setRecipeUsed(world, serverPlayer, recipe)){
                itemStack = recipe.getResultItem();
            }
        }else if (recipeOptionalIn.isPresent()){ //无序配方
            ExtremeCraftShapeRecipe recipe = recipeOptionalIn.get();
            if (outputInventory.setRecipeUsed(world, serverPlayer, recipe)){
                itemStack = recipe.getResultItem();
            }
        }else if (shapeRecipe != null){ //硬编码配方
            if (outputInventory.setRecipeUsed(world, serverPlayer, shapeRecipe)){
                itemStack = shapeRecipe.getResultItem();
            }
        }else {
            ItemStack recipeOutPut = ExtremeCraftingManager.getInstance().getRecipeOutPut(inputInventory, world);
            ItemStack recipeOutPut1 = ExtremeCraftShpaelessManager.getInstance().getRecipeOutPut(inputInventory, world);
            if (Config.SERVER.isCraftTable.get()){
                CraftingContainer craftingInv = getCraftingInv();
                Optional<CraftingRecipe> optional = world.getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftingInv, world);
                if (optional.isPresent() && isCraft()) {
                    CraftingRecipe recipe = optional.get();
                    if (outputInventory.setRecipeUsed(world, serverPlayer, recipe)) {
                        itemStack = recipe.assemble(craftingInv); //获取配方输出
                    }
                }else {
                    itemStack = recipeOutPut.isEmpty() ? recipeOutPut1 : recipeOutPut;
                }
            }else itemStack = recipeOutPut.isEmpty() ? recipeOutPut1 : recipeOutPut;
        }
        outputInventory.setItem(81, itemStack);
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.getStateId(), 81, itemStack));
    }

    /**
     * 判断是否适用工作台配方
     * @return 适用 true
     */
    private boolean isCraft() {
        List<Integer> list = Arrays.asList(0, 1, 2, 9, 10, 11, 18, 19, 20);
        for (int i = 0; i < inputInventory.getContainerSize(); i++){
            ItemStack stack = inputInventory.getItem(i);
            if (list.contains(i)) continue;
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    /**
     * 创建一个工作台容器
     * @return 容器
     */
    private CraftingContainer getCraftingInv(){
        CraftingContainer inventory = new CraftingContainer(new AbstractContainerMenu(MenuType.CRAFTING, containerId) {
            @Override
            public boolean stillValid(Player pPlayer) {
                return true;
            }
        }, 3, 3);
        inventory.setItem(0, inputInventory.getItem(0));
        inventory.setItem(1, inputInventory.getItem(1));
        inventory.setItem(2, inputInventory.getItem(2));
        inventory.setItem(3, inputInventory.getItem(9));
        inventory.setItem(4, inputInventory.getItem(10));
        inventory.setItem(5, inputInventory.getItem(11));
        inventory.setItem(6, inputInventory.getItem(18));
        inventory.setItem(7, inputInventory.getItem(19));
        inventory.setItem(8, inputInventory.getItem(20));
        return inventory;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inputInventory.stillValid(player);
    }

    //玩家shift行为
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            if (index == 81){
                if (!this.moveItemStackTo(itemStack1, 82, 118, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(itemStack1, itemstack);
            } else if (index >= 82){
                if (index < 109){//从物品栏到工作台
                    if (!this.moveItemStackTo(itemStack1, 0, 81, false)) return ItemStack.EMPTY;
                } else if (index < 118) {  //快捷栏到物品栏
                    if (!this.moveItemStackTo(itemStack1, 82, 109, false)) return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack1, 82, 118, false)) return ItemStack.EMPTY; //从合成台取出来

            if (itemStack1.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents stackedContents) {
        if (this.inputInventory != null) {
           this.inputInventory.fillStackedContents(stackedContents);
        }
    }

    @Override
    public void clearCraftingContent() {
        this.inputInventory.clearContent();
        this.outputInventory.clearContent();
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> recipe) {
        return recipe.matches(this.inputInventory, this.player.level);
    }

    @Override
    public int getResultSlotIndex() {
        return 81;
    }

    @Override
    public int getGridWidth() {
        return 9;
    }

    @Override
    public int getGridHeight() {
        return 9;
    }

    @Override
    public int getSize() {
        return 82;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return null;
    }

    @Override
    public boolean shouldMoveToInventory(int i) {
        return false;
    }
}
