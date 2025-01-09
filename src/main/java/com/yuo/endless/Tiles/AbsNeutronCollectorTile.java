package com.yuo.endless.Tiles;

import com.yuo.endless.Container.NCIntArray;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
        int time = tile.getCraftTime(); //生产时间
        ItemStack outItem = tile.getCraftOutputItem(); //产出物品
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
    public int getCraftTime(){
        return 3600;
    }
    //产物
    protected ItemStack getCraftOutputItem(){
        return new ItemStack(EndlessItems.neutroniumPile.get());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        NbtRead(nbt);
    }

    private void NbtRead(CompoundTag nbt){
        this.output = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.output);
        this.timer = nbt.getInt("Timer");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        NbtWrite(compound);
        super.saveAdditional(compound);
    }

    private void NbtWrite(CompoundTag compound){
        compound.putInt("Timer", this.timer);
        ContainerHelper.saveAllItems(compound, this.output);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        NbtWrite(tag);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (level != null) {
            handleUpdateTag(pkt.getTag());
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        NbtRead(tag);
    }

    @Override
    protected Component getDefaultName() {
        return null;
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.output.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return this.output.get(0);
    }

    @Override
    public ItemStack removeItem(int i, int count) {
        return ContainerHelper.removeItem(this.output, i, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(this.output, i);
    }

    @Override
    public void setItem(int i, ItemStack stack) {
        ItemStack itemstack = this.output.get(i);
        boolean flag = !stack.isEmpty() && stack.equals(itemstack, false) && ItemStack.isSame(stack, itemstack);
        this.output.set(i, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (!flag) {
            this.setChanged();
        }
    }

    @Override
    public void clearContent() {
        this.output.clear();
    }

    //是否可以使用tile
    @Override
    public boolean stillValid(Player player) {
        if (this.level != null && this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    //能否放入物品
    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        return false;
    }

    //通过面获取slot
    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? new int[]{0} : new int[]{};
    }

    //自动输入
    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @org.jetbrains.annotations.Nullable Direction direction) {
        return false;
    }

    //自动输出
    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return direction == Direction.DOWN && i == 0;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && side != null && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.DOWN)
                return handlerOutPut[0].cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for (LazyOptional<? extends IItemHandler> handler : handlerOutPut)
            handler.invalidate(); //移除能力
    }
}
