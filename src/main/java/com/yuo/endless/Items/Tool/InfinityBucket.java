package com.yuo.endless.Items.Tool;

import com.yuo.endless.Config;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Fluid.EndlessFluidBucketWrapper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 右键范围装取流体（水和岩浆），并记录数量用于消耗
 */
public class InfinityBucket extends Item {
    public final static String FLUID_NAME = "fluid_name";
    public final static String FLUID_NUMBER = "fluid_number";

    public InfinityBucket() {
        super(new Properties().group(EndlessTab.endless).maxStackSize(1).isImmuneToFire());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tag = stack.getOrCreateTag();
        String fluidKey = tag.getString(FLUID_NAME);
        Fluid fluid = getFluidForKey(fluidKey);
        if (fluid != Fluids.EMPTY){
            ResourceLocation registryName = fluid.getRegistryName();
            if (registryName != null)
                tooltip.add(new TranslationTextComponent("endless.text.itemInfo.bucket_fluid").appendSibling(
                        new TranslationTextComponent(registryName.toString())));
            else tooltip.add(new TranslationTextComponent("endless.text.itemInfo.bucket_fluid").appendSibling(
                    new TranslationTextComponent("minecraft:null")));
            int fluidNum = tag.getInt(FLUID_NUMBER);
            if (fluidNum > 0){
                tooltip.add(new TranslationTextComponent("endless.text.itemInfo.bucket_num", fluidNum));
            }
        }
    }

    private static Fluid getFluidForKey(String str){
        if (str.isEmpty()) return Fluids.EMPTY;
        if ("empty".equals(str)) return Fluids.EMPTY;
        return getFluidForKey(new ResourceLocation(str));
    }

    private static Fluid getFluidForKey(ResourceLocation res){
        return Registry.FLUID.getOrDefault(res);
    }

    /**
     * 获取无尽桶流体
     * @param stack 桶
     * @return 流体
     */
    public static Fluid getFluid(ItemStack stack){
        return getFluidForKey(stack.getOrCreateTag().getString(FLUID_NAME));
    }

    /**
     * 获取无尽桶流体数量
     * @param stack 桶
     * @return 数量
     */
    public static int getFluidNumber(ItemStack stack){
        return stack.getOrCreateTag().getInt(FLUID_NUMBER);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext p_195939_1_) {
        return super.onItemUse(p_195939_1_);
    }

    /**
     * 清空流体数据
     */
    public static void removeFluid(ItemStack bucket, PlayerEntity playerIn, World worldIn, Hand handIn){
        bucket.getOrCreateTag().remove(FLUID_NAME);
        bucket.getOrCreateTag().remove(FLUID_NUMBER);
        playerIn.swingArm(handIn);
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    /**
     * 装取一定范围内流体
     * @param bucket 桶
     * @param fluid  当前流体
     */
    private ActionResult<ItemStack> fillFluid(ItemStack bucket, PlayerEntity playerIn, World worldIn, BlockPos blockpos, Fluid fluid){
        BlockState state = worldIn.getBlockState(blockpos);
        if (state.getBlock() instanceof IBucketPickupHandler) {
            Fluid fluid0 = ((IBucketPickupHandler)state.getBlock()).pickupFluid(worldIn, blockpos, state);
            if (fluid0 != Fluids.EMPTY && (fluid == Fluids.EMPTY || fluid == fluid0)) { //要装流体不为空 桶内为空或流体相同
                CompoundNBT tag = bucket.getOrCreateTag();
                //范围装取流体
                int fluidNum = 0;
                int range = Config.SERVER.infinityBucketRange.get();
                for (BlockPos pos : BlockPos.getAllInBoxMutable(blockpos.add(-range, -range, -range), blockpos.add(range, range, range))) {
                    if (pos == blockpos) continue;
                    FluidState fluidState = worldIn.getFluidState(pos);
                    if (fluidState.getFluid() == fluid0){
                        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
                        fluidNum++;
                    }
                }
                playerIn.addStat(Stats.ITEM_USED.get(this)); //使用状态
                //播放对应声音
                SoundEvent soundevent = fluid.getAttributes().getFillSound();
                if (soundevent == null) soundevent = fluid0.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_FILL_LAVA : SoundEvents.ITEM_BUCKET_FILL;
                playerIn.playSound(soundevent, 1.0F, 1.0F);
                //添加流体数据
                ResourceLocation registryName = fluid0.getRegistryName();
                if (registryName != null){
                    tag.putString(FLUID_NAME, registryName.toString());
                }else tag.putString(FLUID_NAME, "minecraft:null");
                tag.putInt(FLUID_NUMBER, fluidNum + 1);
                //填充桶？
                if (!worldIn.isRemote) {
                    CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)playerIn, bucket);
                }
                return ActionResult.func_233538_a_(bucket, worldIn.isRemote());
            }
        }
        return ActionResult.resultFail(bucket);
    }

    /**
     * 获取流体能力接口 存放流体 音效和文字提示
     * @param teCap 容器能力接口
     * @param flag 存 true 放 false
     */
    private void fluidCap(LazyOptional<IFluidHandler> teCap, ItemStack bucket, World worldIn, BlockPos blockpos, PlayerEntity playerIn, boolean flag, Hand handIn){
        IFluidHandler teHandler = teCap.orElse(EmptyFluidHandler.INSTANCE);
        EndlessFluidBucketWrapper wrapper = new EndlessFluidBucketWrapper(bucket);
        ResourceLocation registryName = getFluid(bucket).getRegistryName();
        wrapper.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent((e) ->{
            FluidStack fluidStack;
            if (flag){
                fluidStack = tryTransfer(e, teHandler, Integer.MAX_VALUE);
            }else fluidStack = tryTransfer(teHandler, e, Integer.MAX_VALUE);
            if (!fluidStack.isEmpty()) {
                worldIn.playSound(null, blockpos, fluidStack.getFluid().getAttributes().getEmptySound(fluidStack), SoundCategory.BLOCKS, 1.0F, 1.0F);
                ResourceLocation name = fluidStack.getFluid().getRegistryName();
                if (name != null && registryName != null){
                    String tex = flag ? I18n.format("endless.infinity_bucket.place") + I18n.format(registryName.toString())
                            : I18n.format("endless.infinity_bucket.assume") + I18n.format(name.toString());
                    playerIn.sendStatusMessage(new StringTextComponent(tex + "  " + fluidStack.getAmount() + "mb"), true);
                }
                playerIn.swingArm(handIn);
            }
        });
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack bucket = playerIn.getHeldItem(handIn);
        Fluid fluid = getFluid(bucket);
        BlockRayTraceResult rayTraceResult = rayTrace(worldIn, playerIn, fluid == Fluids.EMPTY ? FluidMode.SOURCE_ONLY : FluidMode.NONE);
        if (rayTraceResult.getType() == Type.MISS) { //右键空气
            if (playerIn.isSneaking() && bucket.getOrCreateTag().getInt(FLUID_NUMBER) >= 0){ //潜行右键清空流体
                removeFluid(bucket, playerIn, worldIn, handIn);
                return ActionResult.func_233538_a_(bucket, worldIn.isRemote);
            }
            return ActionResult.resultPass(bucket);
        } else if (rayTraceResult.getType() != Type.BLOCK) {
            return ActionResult.resultPass(bucket);
        } else {
            BlockPos blockpos = rayTraceResult.getPos();
            Direction direction = rayTraceResult.getFace();
            BlockPos blockpos1 = blockpos.offset(direction);
            if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos1, direction, bucket)) {
                if (!playerIn.isSneaking()) { //非潜行 装
                    TileEntity tile = worldIn.getTileEntity(blockpos);
                    if (tile != null) {
                        LazyOptional<IFluidHandler> teCapability = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction);
                        if (teCapability.isPresent()) {
                            fluidCap(teCapability, bucket, worldIn, blockpos, playerIn, true, handIn);
                        }else return fillFluid(bucket, playerIn, worldIn, blockpos, fluid);
                    }else return fillFluid(bucket, playerIn, worldIn, blockpos, fluid);
                }else { //潜行 放出
                    BlockState blockstate = worldIn.getBlockState(blockpos);
                    TileEntity tile = worldIn.getTileEntity(blockpos);
                    if (tile != null) {
                        LazyOptional<IFluidHandler> teCapability = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction);
                        if (teCapability.isPresent()) {
                            fluidCap(teCapability, bucket, worldIn, blockpos, playerIn, false, handIn);
                        } else return placeFluid(bucket, worldIn, blockpos, blockpos1, blockstate, fluid, playerIn, rayTraceResult);
                    }else return placeFluid(bucket, worldIn, blockpos, blockpos1, blockstate, fluid, playerIn, rayTraceResult);

                }
            } else {
                return ActionResult.resultFail(bucket);
            }
        }
        return ActionResult.resultPass(bucket);
    }

    /**
     * 从容器装取
     */
    private ActionResult<ItemStack> placeFluid(ItemStack bucket, World worldIn, BlockPos blockpos, BlockPos blockpos1, BlockState blockstate, Fluid fluid, PlayerEntity playerIn, BlockRayTraceResult rayTraceResult){
        BlockPos pos = blockpos;
        CompoundNBT tag = bucket.getOrCreateTag();
        if (fluid == Fluids.WATER) {
            pos = canBlockContainFluid(worldIn, blockpos, blockstate, fluid) ? blockpos : blockpos1;
        }
        int fluidNum = tag.getInt(FLUID_NUMBER);
        if (fluidNum >= 1 && tryPlaceContainedLiquid(playerIn, worldIn, pos, fluid, rayTraceResult)) {
            if (fluidNum == 1){
                tag.putString(FLUID_NAME, "empty");
            }else {
                tag.putInt(FLUID_NUMBER, Math.max(fluidNum - 1, 1));
            }
            if (playerIn instanceof ServerPlayerEntity) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerIn, pos, bucket);
            }

            playerIn.addStat(Stats.ITEM_USED.get(this));
            return ActionResult.func_233538_a_(bucket, worldIn.isRemote());
        } else {
            return ActionResult.resultFail(bucket);
        }
    }

    public static FluidStack tryTransfer(IFluidHandler input, IFluidHandler output, int maxFill) {
        FluidStack simulated = input.drain(maxFill, FluidAction.SIMULATE);
        if (!simulated.isEmpty()) {
            int simulatedFill = output.fill(simulated, FluidAction.SIMULATE);
            if (simulatedFill > 0) {
                FluidStack drainedFluid = input.drain(simulatedFill, FluidAction.EXECUTE);
                if (!drainedFluid.isEmpty()) {
                    int actualFill = output.fill(drainedFluid.copy(), FluidAction.EXECUTE);
                    if (actualFill != drainedFluid.getAmount()) {
                        System.out.println("Lost {"+ input.getFluidInTank(0).getFluid().getRegistryName() + "} fluid during transfer" + (drainedFluid.getAmount() - actualFill));
                    }
                }
                return drainedFluid;
            }
        }
        return FluidStack.EMPTY;
    }

    //尝试放置流体
    public boolean tryPlaceContainedLiquid(@Nullable PlayerEntity player, World worldIn, BlockPos posIn, Fluid fluid, @Nullable BlockRayTraceResult rayTrace) {
        if (!(fluid instanceof FlowingFluid)) {
            return false;
        } else {
            BlockState blockstate = worldIn.getBlockState(posIn);
            Block block = blockstate.getBlock();
            Material material = blockstate.getMaterial();
            boolean flag = blockstate.isReplaceable(fluid); //是否可用被流体替换
            //放置坐标是空气 可替换 属于流体容器且能装入
            boolean flag1 = worldIn.isAirBlock(posIn) || flag || block instanceof ILiquidContainer && ((ILiquidContainer)block).canContainFluid(worldIn, posIn, blockstate, fluid);
            if (!flag1) { //不能放置，则朝玩家方向移动一格
                return rayTrace != null && tryPlaceContainedLiquid(player, worldIn, rayTrace.getPos().offset(rayTrace.getFace()), fluid,null);
            } else if (worldIn.getDimensionType().isUltrawarm() && fluid.isIn(FluidTags.WATER)) { //下界无法放水
                int i = posIn.getX();
                int j = posIn.getY();
                int k = posIn.getZ();
                worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                for(int l = 0; l < 8; ++l) {
                    worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                return true;
            } else if (block instanceof ILiquidContainer && ((ILiquidContainer)block).canContainFluid(worldIn,posIn,blockstate,fluid)) {
                ((ILiquidContainer)block).receiveFluid(worldIn, posIn, blockstate, ((FlowingFluid)fluid).getStillFluidState(false));
                this.playEmptySound(player, worldIn, posIn, fluid);
                return true;
            } else {
                if (!worldIn.isRemote && flag && !material.isLiquid()) {
                    worldIn.destroyBlock(posIn, true);
                }

                if (!worldIn.setBlockState(posIn, fluid.getDefaultState().getBlockState(), 11) && !blockstate.getFluidState().isSource()) {
                    return false;
                } else {
                    this.playEmptySound(player, worldIn, posIn, fluid);
                    return true;
                }
            }
        }
    }

    //播放放置音效
    protected void playEmptySound(@Nullable PlayerEntity player, IWorld worldIn, BlockPos pos, Fluid fluid) {
        SoundEvent soundevent = fluid.getAttributes().getEmptySound();
        if(soundevent == null) soundevent = fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
        worldIn.playSound(player, pos, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

//    @Override
//    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
//        return this.getClass() == InfinityBucket.class ? new EndlessFluidBucketWrapper(stack) : super.initCapabilities(stack, nbt);
//    }

    /**
     * 方块是否是含流体方块
     * @return 是 true
     */
    private boolean canBlockContainFluid(World worldIn, BlockPos posIn, BlockState blockstate, Fluid fluid) {
        return blockstate.getBlock() instanceof ILiquidContainer && ((ILiquidContainer) blockstate.getBlock()).canContainFluid(worldIn, posIn, blockstate, fluid);
    }
}
