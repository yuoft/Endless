package com.yuo.endless.Blocks.Fluid;

import com.yuo.endless.Items.Tool.InfinityBucket;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yuo.endless.Items.Tool.InfinityBucket.FLUID_NAME;
import static com.yuo.endless.Items.Tool.InfinityBucket.FLUID_NUMBER;

public class EndlessFluidBucketWrapper extends FluidBucketWrapper {
    private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);

    public EndlessFluidBucketWrapper(@Nonnull ItemStack container) {
        super(container);
        this.container = container;
    }

    @Nonnull
    public ItemStack getContainer() {
        return this.container;
    }

    //可填充流体类型
    public boolean canFillFluidType(FluidStack fluid) {
        Fluid fluid1 = InfinityBucket.getFluid(this.container);
        if (fluid1 == Fluids.EMPTY) return true; //空
        return fluid1 == fluid.getFluid(); //类型相同
    }

    @Nonnull
    public FluidStack getFluid() {
        Item item = this.container.getItem();
        if (item instanceof InfinityBucket) {
            return new FluidStack(InfinityBucket.getFluid(this.container), 1000 * InfinityBucket.getFluidNumber(this.container));
        } else {
            return super.getFluid();
        }
    }

    protected void setFluid(@Nonnull FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            CompoundTag nbt = this.container.getOrCreateTag();
            nbt.putString(FLUID_NAME, "empty");
            nbt.putInt(FLUID_NUMBER, 0);
            this.container.setTag(nbt);
        } else {
            //添加流体数据
            ResourceLocation registryName = fluidStack.getFluid().getRegistryName();
            CompoundTag tag = this.container.getOrCreateTag();
            if (registryName != null){
                tag.putString(FLUID_NAME, registryName.toString());
            }else tag.putString(FLUID_NAME, "minecraft:null");
            tag.putInt(FLUID_NUMBER, (int) Math.floor(fluidStack.getAmount() / 1000.0));
            this.container.setTag(tag);
        }

    }

    public int getTanks() {
        return 1;
    }

    @Nonnull
    public FluidStack getFluidInTank(int tank) {
        return this.getFluid();
    }

    public int getTankCapacity(int tank) {
        return 1000;
    }

    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return true;
    }

    /**
     * 填充桶 从容器装到桶里
     * @param resource 流体容器
     * @param action 动作
     * @return 装取数量
     */
    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        //容器内大于一桶
        int amount = resource.getAmount();
        FluidStack fluid = this.getFluid();
        if (this.container.getCount() == 1 && amount >= 1000 &&(fluid.isEmpty() || fluid.isFluidEqual(resource) )){
            int num = (int) Math.floor(amount / 1000d);
            if (action.execute()) {
                int amount1 = fluid.getAmount();
                FluidStack copy = resource.copy();
                copy.setAmount(1000 * num + amount1);
                this.setFluid(copy);
            }

            return 1000 * num;
        }
        return 0;
    }

    @Nonnull
    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        int amount = resource.getAmount();
        if (this.container.getCount() == 1 && amount >= 1000) {
            FluidStack fluidStack = this.getFluid();
            if (!fluidStack.isEmpty() && fluidStack.isFluidEqual(resource)) {
                if (action.execute()) {
                    FluidStack fluid = fluidStack.copy();
                    int stackAmount = fluidStack.getAmount();
                    int count = Math.max(0, stackAmount > amount ? stackAmount - amount : amount - stackAmount);
                    fluid.setAmount(count);
                    this.setFluid(fluid);
                    FluidStack copy = fluid.copy();
                    copy.setAmount(amount);
                    return copy;
                }

                return fluidStack;
            } else {
                return FluidStack.EMPTY;
            }
        } else {
            return FluidStack.EMPTY;
        }
    }

    //消耗
    @Nonnull
    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        if (this.container.getCount() == 1 && maxDrain >= 1000) {
            FluidStack fluidStack = this.getFluid();
            if (!fluidStack.isEmpty()) {
                if (action.execute()) {
                    FluidStack fluid = fluidStack.copy();
                    fluid.setAmount(Math.max(0, fluid.getAmount() - maxDrain));
                    this.setFluid(fluid);
                    FluidStack copy = fluidStack.copy();
                    copy.setAmount(maxDrain);
                    return copy;
                }

                return fluidStack;
            } else {
                return FluidStack.EMPTY;
            }
        } else {
            return FluidStack.EMPTY;
        }
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.orEmpty(capability, this.holder);
    }
}
