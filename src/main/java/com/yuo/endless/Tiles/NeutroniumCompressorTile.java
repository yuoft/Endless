package com.yuo.endless.Tiles;

import com.yuo.endless.Container.NeutroniumCompressorContainer;
import com.yuo.endless.Container.NiumCIntArray;
import com.yuo.endless.NetWork.NetWorkHandler;
import com.yuo.endless.NetWork.NmCPacket;
import com.yuo.endless.Recipe.CompressorManager;
import com.yuo.endless.Recipe.NeutroniumRecipe;
import com.yuo.endless.Recipe.RecipeTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Optional;

public class NeutroniumCompressorTile extends BaseContainerBlockEntity implements WorldlyContainer {
    //用于自动输入输出
    LazyOptional<? extends IItemHandler>[] handlerTop = SidedInvWrapper.create(this, Direction.UP, Direction.NORTH, Direction.EAST,
            Direction.SOUTH, Direction.WEST);
    LazyOptional<? extends IItemHandler>[] handlerDown = SidedInvWrapper.create(this, Direction.DOWN);

    // 0：输入，1：输出，2：正在参与合成的物品
    public NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY); //物品栏
    public NiumCIntArray data = new NiumCIntArray();
    private final RecipeType<NeutroniumRecipe> recipeType = RecipeTypeRegistry.NEUTRONIUM_RECIPE;
    private final int[] SLOT_IN = new int[]{0};
    private final int[] SLOT_OUT = new int[]{1};

    public NeutroniumCompressorTile(BlockPos pos, BlockState state) {
        super(EndlessTileTypes.NEUTRONIUM_COMPRESSOR_TILE.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, NeutroniumCompressorTile tile) {
        //保存坐标
        tile.data.set(2, pos.getX());
        tile.data.set(3, pos.getY());
        tile.data.set(4, pos.getZ());

        ItemStack input = tile.items.get(0);
        ItemStack stack1 = tile.items.get(1); //已有输出
        if (input.isEmpty()) return; //没有输入时 停止
        ItemStack stack;
        Optional<NeutroniumRecipe> optional = level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.NEUTRONIUM_RECIPE, new SimpleContainer(input), level);
        if (optional.isPresent()){
            stack = optional.get().getRecipeOutput();
        }else stack = CompressorManager.getOutput(input); //获取此输入的输出
        //判断输出 输出和已有输出不同 输出为空
        if ((!stack1.isEmpty() && !(stack1.getItem() == stack.getItem())) || stack.isEmpty()) return;
        //机器内有残留时
        if (stack1.isEmpty() && tile.data.get(0) > 0){ //输入与缓存输入不同 或无法替换
            if (!optional.map(recipe -> input.equals(tile.items.get(2), false)).orElseGet(() -> CompressorManager.isInput(input, tile.items.get(2))))
                return;
        }

        int count = optional.map(NeutroniumRecipe::getRecipeCount).orElseGet(() -> CompressorManager.getCost(input));
        tile.data.set(1,count );
        if (count > 0 && tile.data.get(0) < count){
            tile.items.set(2, new ItemStack(input.getItem()));  //缓存参与合成物品
            int num = optional.isPresent() ? 1 : CompressorManager.getInputCost(input);
            tile.data.set(0, tile.data.get(0) + num);
            if (input.getCount() >= 1)
                input.shrink(1);
            setChanged(level, pos, state);
        }

        if (tile.data.get(0) >= tile.data.get(1)){ //物品已满，设置输出
            if (stack1.isEmpty()){
                tile.items.set(1, stack);
            }else stack1.grow(1);
            tile.data.set(0, 0);
            tile.data.set(1, 0);
            setChanged(level, pos, state);
        }

        if (!tile.items.get(2).isEmpty()){
            NetWorkHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new NmCPacket(pos, tile.items.get(2)));
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        NbtRead(nbt);
    }

    private void NbtRead(CompoundTag nbt){
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.items);
        this.data.set(0, nbt.getInt("Number"));
        this.data.set(1, nbt.getInt("NumberTotal"));
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        NbtWrite(compound);
        super.saveAdditional(compound);
    }

    private void NbtWrite(CompoundTag compound){
        compound.putInt("Number", this.data.get(0));
        compound.putInt("NumberTotal", this.data.get(1));
        ContainerHelper.saveAllItems(compound, this.items);
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
        return new TranslatableComponent("gui.endless.neutron_compressor");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new NeutroniumCompressorContainer(id, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return !this.items.get(0).isEmpty() || !this.items.get(1).isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        return ContainerHelper.removeItem(this.items, i, i1);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(this.items, i);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.equals(itemstack, false) && ItemStack.isSame(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (!flag) {
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level != null && this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? SLOT_OUT : SLOT_IN;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @org.jetbrains.annotations.Nullable Direction direction) {
        if (index == 0 && direction != Direction.DOWN){
            if (this.items.get(2).isEmpty() || this.items.get(2).equals(stack, false))
                return !CompressorManager.getOutput(stack).isEmpty();
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack itemStack, Direction direction) {
        return direction == Direction.DOWN && index == 1;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.remove && side != null && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side != Direction.DOWN)
                return handlerTop[0].cast();
            else
                return handlerDown[0].cast();
        }
        return super.getCapability(cap, side);
    }

    public Container getInventory(){
        return new SimpleContainer(this.items.get(0), this.items.get(1));
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }
}
