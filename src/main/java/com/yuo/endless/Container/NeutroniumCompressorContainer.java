package com.yuo.endless.Container;

import com.yuo.endless.Recipe.CompressorManager;
import com.yuo.endless.Recipe.RecipeTypeRegistry;
import com.yuo.endless.Tiles.NeutroniumCompressorTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class NeutroniumCompressorContainer extends AbstractContainerMenu {

    private final NeutroniumCompressorTile tile;
    private final NiumCIntArray data;
    private final Level world;

    public NeutroniumCompressorContainer(int id, Inventory playerInventory){
        this(id,playerInventory , new NeutroniumCompressorTile(null, null));
    }

    public NeutroniumCompressorContainer(int id, Inventory playerInventory, NeutroniumCompressorTile inventory) {
        super(EndlessMenuTypes.neutroniumCompressorContainer.get(), id);
        this.tile = inventory;
        this.data = inventory.data;
        this.world = playerInventory.player.level;
        //矿物输入槽
        this.addSlot(new NiumCSlot(tile, world, 0, 39,35));
        //奇点槽
        this.addSlot(new NCOutputSlot(tile, 1, 116,35));
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

    @Override
    public boolean stillValid(Player player) {
        return this.tile.stillValid(player);
    }

    //玩家shift行为
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            if (index >= 2){
                if (!CompressorManager.getOutput(itemStack1).isEmpty() || hasRecipe(itemStack1)){
                    if (!this.moveItemStackTo(itemStack1, 0, 1, false)) return ItemStack.EMPTY;
                }
                if (index < 29) { //从物品栏到快捷栏
                    if (!this.moveItemStackTo(itemStack1, 30, 38, false)) return ItemStack.EMPTY;
                } else if (index < 38) {
                    if (!this.moveItemStackTo(itemStack1, 2, 29, false)) return ItemStack.EMPTY;
                }
            }else if (!this.moveItemStackTo(itemStack1, 2, 38, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }
        return itemstack;
    }

    protected boolean hasRecipe(ItemStack stack) {
        return this.world.getRecipeManager().getRecipeFor(RecipeTypeRegistry.NEUTRONIUM_RECIPE, new SimpleContainer(stack), this.world).isPresent();
    }

    /**
     * 获取正参与合成物品
     * @return 物品
     */
    public ItemStack getItem(){
        BlockPos pos = new BlockPos(data.get(2), data.get(3), data.get(4));
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof NeutroniumCompressorTile){
            return ((NeutroniumCompressorTile) tileEntity).getItem(2);
        }
        return this.tile.getItem(2);
    }

    //获取物品数量
    public int getNumber(){
        return this.data.get(0);
    }
    //总数
    public int getCount(){ return this.data.get(1);}
    //获取压缩进度 2种显示
    public int getProgress(){ return (int) Math.ceil(this.data.get(0) / (this.data.get(1) * 1.0) * 22);}
    public int getProgress1(){ return (int) Math.ceil(this.data.get(0) / (this.data.get(1) * 1.0) * 16);}
}
