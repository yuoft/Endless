package com.yuo.endless.Tiles;

import com.yuo.endless.Container.NeutroniumCompressorContainer;
import com.yuo.endless.Container.NiumCIntArray;
import com.yuo.endless.NetWork.NetWorkHandler;
import com.yuo.endless.NetWork.NmCPacket;
import com.yuo.endless.Recipe.CompressorManager;
import com.yuo.endless.Recipe.NeutroniumRecipe;
import com.yuo.endless.Recipe.RecipeTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public class NeutroniumCompressorTile extends LockableTileEntity implements ITickableTileEntity, ISidedInventory {
    //用于自动输入输出
    LazyOptional<? extends IItemHandler>[] handlerTop = SidedInvWrapper.create(this, Direction.UP, Direction.NORTH, Direction.EAST,
            Direction.SOUTH, Direction.WEST);
    LazyOptional<? extends IItemHandler>[] handlerDown = SidedInvWrapper.create(this, Direction.DOWN);

    // 0：输入，1：输出，2：正在参与合成的物品
    public NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY); //物品栏
    public NiumCIntArray data = new NiumCIntArray();
    private final IRecipeType<NeutroniumRecipe> recipeType = RecipeTypeRegistry.NEUTRONIUM_RECIPE;
    private final int[] SLOT_IN = new int[]{0};
    private final int[] SLOT_OUT = new int[]{1};

    public NeutroniumCompressorTile() {
        super(TileTypeRegistry.NEUTRONIUM_COOMPRESSOR_TILE.get());
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;
        //保存坐标
        this.data.set(2, pos.getX());
        this.data.set(3, pos.getY());
        this.data.set(4, pos.getZ());

        ItemStack input = this.items.get(0);
        ItemStack stack1 = this.items.get(1); //已有输出
        if (input.isEmpty()) return; //没有输入时 停止
        ItemStack stack = CompressorManager.getOutput(input); //获取此输入的输出
        //判断输出
        if (!stack1.isEmpty() && !(stack1.getItem() == stack.getItem()) || stack.isEmpty()) return;
        //机器内有残留时，输入方块与残留的参与合成方块不同时  停止
        if (stack1.isEmpty() && this.data.get(0) > 0 && !(CompressorManager.isInput(input, items.get(2)))) return;

        int count = CompressorManager.getCost(input);
        this.data.set(1,count );
        if (count > 0 && this.data.get(0) < count){
            this.items.set(2, new ItemStack(input.getItem()));  //缓存参与合成物品
            this.data.set(0, this.data.get(0) + CompressorManager.getInputCost(input));
            if (input.getCount() >= 1)
                input.shrink(1);
            markDirty();
        }

        if (this.data.get(0) >= this.data.get(1)){ //物品已满，设置输出
            if (stack1.isEmpty()){
                this.items.set(1, CompressorManager.getOutput(input));
            }else stack1.grow(1);
            this.data.set(0, 0);
            this.data.set(1, 0);
            markDirty();
        }

        if (!items.get(2).isEmpty()){
            NetWorkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new NmCPacket(pos, items.get(2)));
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        NbtRead(nbt);
    }

    private void NbtRead(CompoundNBT nbt){
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
        this.data.set(0, nbt.getInt("Number"));
        this.data.set(1, nbt.getInt("NumberTotal"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        NbtWrite(compound);
        return super.write(compound);
    }

    private void NbtWrite(CompoundNBT compound){
        compound.putInt("Number", this.data.get(0));
        compound.putInt("NumberTotal", this.data.get(1));
        ItemStackHelper.saveAllItems(compound, this.items);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
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
        return new TranslationTextComponent("gui.endless.neutronium_compressor");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new NeutroniumCompressorContainer(id, player, this);
    }

    @Override
    public int getSizeInventory() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return !this.items.get(0).isEmpty() || !this.items.get(1).isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (!flag) {
            this.markDirty();
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? SLOT_OUT : SLOT_IN;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        if (index == 0 && direction != Direction.DOWN){
            if (this.items.get(2).isEmpty() || this.items.get(2).isItemEqual(itemStackIn))
                return !CompressorManager.getOutput(itemStackIn).isEmpty();
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && index == 1;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.removed && side != null && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.DOWN)
                return handlerTop[0].cast();
            else
                return handlerDown[0].cast();
        }
        return super.getCapability(cap, side);
    }

    public Inventory getInventory(){
        return new Inventory(this.items.get(0), this.items.get(1));
    }

}
