package com.yuo.endless.Container;

import com.yuo.endless.Recipe.CompressorManager;
import com.yuo.endless.Recipe.RecipeTypeRegistry;
import com.yuo.endless.Tiles.NeutroniumCompressorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NeutroniumCompressorContainer extends Container {

    private final NeutroniumCompressorTile tile;
    private final NiumCIntArray data;
    private final World world;

    public NeutroniumCompressorContainer(int id, PlayerInventory playerInventory){
        this(id,playerInventory , new NeutroniumCompressorTile());
    }

    public NeutroniumCompressorContainer(int id, PlayerInventory playerInventory, NeutroniumCompressorTile inventory) {
        super(ContainerTypeRegistry.neutroniumCompressorContainer.get(), id);
        this.tile = inventory;
        this.data = inventory.data;
        this.world = playerInventory.player.world;
        trackIntArray(data);
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
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.tile.isUsableByPlayer(playerIn);
    }

    //玩家shift行为
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();
            if (index >= 2){
                if (CompressorManager.getOutput(itemStack1).isEmpty()){
                    if (!this.mergeItemStack(itemStack1, 0, 1, false)) return ItemStack.EMPTY;
                }
                if (index < 29) { //从物品栏到快捷栏
                    if (!this.mergeItemStack(itemStack1, 30, 38, false)) return ItemStack.EMPTY;
                } else if (index < 38) {
                    if (!this.mergeItemStack(itemStack1, 2, 29, false)) return ItemStack.EMPTY;
                }
            }else if (!this.mergeItemStack(itemStack1, 2, 38, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }
        return itemstack;
    }

    protected boolean hasRecipe(ItemStack stack) {
        return this.world.getRecipeManager().getRecipe(RecipeTypeRegistry.NEUTRONIUM_RECIPE, new Inventory(stack), this.world).isPresent();
    }

    /**
     * 获取正参与合成物品
     * @return 物品
     */
    public ItemStack getItem(){
//        Ingredient recipeInput = TileUtils.getRecipeInput(world, tile);
//        if (!recipeInput.isSimple()) return recipeInput;
        BlockPos pos = new BlockPos(data.get(2), data.get(3), data.get(4));
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof NeutroniumCompressorTile){
            return ((NeutroniumCompressorTile) tileEntity).getStackInSlot(2);
        }
        return this.tile.getStackInSlot(2);
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
