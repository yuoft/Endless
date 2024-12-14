package com.yuo.endless.Tiles;

import com.yuo.endless.Container.NCIntArray;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public class AbsNeutronCollectorTile extends BaseContainerBlockEntity implements WorldlyContainer {
    private int timer; //计时器
    private NonNullList<ItemStack> output = NonNullList.withSize(1, ItemStack.EMPTY); //输出栏
    public NCIntArray data = new NCIntArray();
    LazyOptional<? extends IItemHandler>[] handlerOutPut = SidedInvWrapper.create(this, Direction.DOWN);

    public AbsNeutronCollectorTile(BlockEntityType<?> tileEntityType, BlockPos pos, BlockState state) {
        super(tileEntityType, pos, state);
    }

    /**
     * 服务端tick
     */
    public static void serverTick(Level level, BlockPos pos, BlockState state, AbsNeutronCollectorTile tile) {
        ItemStack output = tile.output.get(0);
        if (!output.isEmpty() && output.getCount() == output.getMaxStackSize()) return; //产物已满，停止计时
        tile.timer++;
        tile.data.set(0, tile.timer);
        int time = getCraftTime(); //生产时间
        ItemStack outItem = getCraftOutputItem(); //产出物品
        if (tile.timer >= time){
            tile.timer = 0;
            tile.data.set(0, tile.timer);
            //产物为空 设置产物 否则数量加1
            if(output.isEmpty()) tile.output.set(0, outItem);
            else output.grow(1);
            setChanged(level, pos, state);//标记变化
        }
    }

    //生产时间
    public static int getCraftTime(){
        return 3600;
    }
    //产物
    protected static ItemStack getCraftOutputItem(){
        return new ItemStack(EndlessItems.neutroniumPile.get());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        NbtRead(nbt);
    }

    private void NbtRead(CompoundNBT nbt){
        this.output = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.output);
        this.timer = nbt.getInt("Timer");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        NbtWrite(compound);
        return super.write(compound);
    }

    private void NbtWrite(CompoundNBT compound){
        compound.putInt("Timer", this.timer);
        ItemStackHelper.saveAllItems(compound, this.output);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (world != null) {
            handleUpdateTag(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT compound = super.getUpdateTag();
        NbtWrite(compound);
        return compound;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        NbtRead(tag);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return null;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.output.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.output.get(0);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.output, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.output, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.output.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.output.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (!flag) {
            this.markDirty();
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world != null && this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        this.output.clear();
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    //通过面获取slot
    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? new int[]{0} : new int[]{};
    }

    //自动输入
    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return false;
    }

    //自动输出
    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && index == 0;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.removed && side != null && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.DOWN)
                return handlerOutPut[0].cast();
        }
        return super.getCapability(cap, side);
    }
}
