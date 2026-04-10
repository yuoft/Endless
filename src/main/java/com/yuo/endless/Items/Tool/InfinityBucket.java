package com.yuo.endless.Items.Tool;

import com.yuo.endless.Config.ModConfig;
import com.yuo.endless.Blocks.Fluid.EndlessFluidBucketWrapper;
import com.yuo.endless.EndlessUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 右键范围装取流体（水和岩浆），并记录数量用于消耗
 */
public class InfinityBucket extends Item {
    public final static String FLUID_NAME = "fluid_name";
    public final static String FLUID_NUMBER = "fluid_number";

    public InfinityBucket() {
        super(new Properties().stacksTo(1).fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> components, TooltipFlag pIsAdvanced) {
        CompoundTag tag = stack.getOrCreateTag();
        String fluidKey = tag.getString(FLUID_NAME);
        Fluid fluid = getFluidForKey(fluidKey);
        if (fluid != Fluids.EMPTY){
            String registryName = fluid.getFluidType().toString();
            components.add(Component.translatable("endless.text.itemInfo.bucket_fluid").append(Component.translatable(registryName.isEmpty() ? Component.translatable("minecraft:null").toString() : registryName)));
            int fluidNum = tag.getInt(FLUID_NUMBER);
            if (fluidNum > 0){
                components.add(Component.translatable("endless.text.itemInfo.bucket_num", fluidNum));
            }
        }
    }

    private static Fluid getFluidForKey(String str){
        if (str.isEmpty()) return Fluids.EMPTY;
        if ("empty".equals(str)) return Fluids.EMPTY;
        return getFluidForKey(EndlessUtils.tryParse(str));
    }

    private static Fluid getFluidForKey(ResourceLocation res){
        Optional<Reference<Fluid>> delegate = ForgeRegistries.FLUIDS.getDelegate(res);
        return delegate.map(Holder::get).orElse(null);
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
    public InteractionResult useOn(UseOnContext pContext) {
        return super.useOn(pContext);
    }

    /**
     * 清空流体数据
     */
    public static void removeFluid(ItemStack bucket, Player playerIn, Level worldIn, InteractionHand handIn){
        bucket.getOrCreateTag().remove(FLUID_NAME);
        bucket.getOrCreateTag().remove(FLUID_NUMBER);
        playerIn.swing(handIn);
        worldIn.playSound(playerIn, playerIn.getOnPos(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack bucket = playerIn.getItemInHand(handIn);
        Fluid fluid = getFluid(bucket);
        BlockHitResult rayTraceResult = getPlayerPOVHitResult(worldIn, playerIn, fluid == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        if (rayTraceResult.getType() == Type.MISS) { //右键空气
            if (playerIn.isCrouching() && bucket.getOrCreateTag().getInt(FLUID_NUMBER) >= 0){ //潜行右键清空流体
                removeFluid(bucket, playerIn, worldIn, handIn);
                return InteractionResultHolder.sidedSuccess(bucket, worldIn.isClientSide);
            }
            return InteractionResultHolder.pass(bucket);
        } else if (rayTraceResult.getType() != Type.BLOCK) {
            return InteractionResultHolder.pass(bucket);
        } else {
            BlockPos blockpos = rayTraceResult.getBlockPos();
            Direction direction = rayTraceResult.getDirection();
            BlockPos relative = blockpos.relative(direction);
            if (worldIn.mayInteract(playerIn, blockpos) && playerIn.mayUseItemAt(relative, direction, bucket)) {
                if (!playerIn.isCrouching()) { //非潜行 装
                    BlockEntity tile = worldIn.getBlockEntity(blockpos);
                    if (tile != null) {
                        LazyOptional<IFluidHandler> teCapability = tile.getCapability(ForgeCapabilities.FLUID_HANDLER, direction);
                        if (teCapability.isPresent()) {
                            fluidCap(teCapability, bucket, worldIn, blockpos, playerIn, true, handIn);
                        }else return fillFluid(bucket, playerIn, worldIn, blockpos, fluid, direction);
                    }else return fillFluid(bucket, playerIn, worldIn, blockpos, fluid, direction);
                }else { //潜行 放出
                    BlockState blockstate = worldIn.getBlockState(blockpos);
                    BlockEntity tile = worldIn.getBlockEntity(blockpos);
                    if (tile != null) {
                        LazyOptional<IFluidHandler> teCapability = tile.getCapability(ForgeCapabilities.FLUID_HANDLER, direction);
                        if (teCapability.isPresent()) {
                            fluidCap(teCapability, bucket, worldIn, blockpos, playerIn, false, handIn);
                        } else return placeFluid(bucket, worldIn, blockpos, relative, blockstate, fluid, playerIn, rayTraceResult);
                    }else return placeFluid(bucket, worldIn, blockpos, relative, blockstate, fluid, playerIn, rayTraceResult);
                }
            } else {
                return InteractionResultHolder.fail(bucket);
            }
        }
        return InteractionResultHolder.pass(bucket);
    }

    /**
     * 装取一定范围内流体
     * @param bucket 桶
     * @param fluid  当前流体
     */
    private InteractionResultHolder<ItemStack> fillFluid(ItemStack bucket, Player playerIn, Level worldIn, BlockPos blockpos, Fluid fluid, Direction direction){
        BlockState state = worldIn.getBlockState(blockpos);
        boolean directionLiquid = isDirectionLiquid(worldIn, blockpos, fluid, direction);
        if ((state.getBlock() instanceof BucketPickup && state.liquid()) || directionLiquid) {
            Fluid fluid0 = worldIn.getFluidState(directionLiquid ? blockpos.relative(direction) : blockpos).getType();
            if (fluid0 != Fluids.EMPTY && (fluid == Fluids.EMPTY || fluid == fluid0)) { //要装流体不为空 桶内为空或流体相同
                CompoundTag tag = bucket.getOrCreateTag();
                //范围装取流体
                int fluidNum = 0;
                int range = ModConfig.SERVER.infinityBucketRange.get();
                for (BlockPos pos : BlockPos.betweenClosed(blockpos.offset(-range, -range, -range), blockpos.offset(range, range, range))) {
                    if (pos == blockpos) continue;
                    FluidState fluidState = worldIn.getFluidState(pos);
                    if (fluidState.getType() == fluid0){
                        worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                        fluidNum++;
                    }
                }
                playerIn.awardStat(Stats.ITEM_USED.get(this)); //使用状态
                //播放对应声音
                Optional<SoundEvent> pickupSound = fluid.getPickupSound();
                SoundEvent soundevent = pickupSound.orElse(null);
                if (soundevent == null) soundevent = fluid0.is(FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL;
                playerIn.playSound(soundevent, 1.0F, 1.0F);
                //添加流体数据
                String registryName = fluid0.getFluidType().toString();
                if (!registryName.isEmpty()){
                    tag.putString(FLUID_NAME, registryName);
                }else tag.putString(FLUID_NAME, "minecraft:null");
                if (fluid == fluid0){
                    tag.putInt(FLUID_NUMBER, fluidNum + tag.getInt(FLUID_NUMBER));
                }else tag.putInt(FLUID_NUMBER, fluidNum);
                //填充桶？
                if (!worldIn.isClientSide) {
                    CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) playerIn, bucket);
                }
                return InteractionResultHolder.sidedSuccess(bucket, worldIn.isClientSide());
            }
        }
        return InteractionResultHolder.fail(bucket);
    }

    /**
     * 已有流体，装取时获取流体。解决无法点击到流体问题。
     */
    private boolean isDirectionLiquid(Level worldIn, BlockPos blockpos, Fluid fluid, Direction direction) {
        BlockPos relative = blockpos.relative(direction);
        BlockState state = worldIn.getBlockState(relative);
        if (state.getBlock() instanceof BucketPickup && state.liquid()) {
            Fluid fluid1 = worldIn.getFluidState(relative).getType();
            return fluid1 != Fluids.EMPTY && (fluid == Fluids.EMPTY || fluid == fluid1);
        }
        return false;
    }

    /**
     * 获取流体能力接口 存放流体 音效和文字提示
     * @param teCap 容器能力接口
     * @param flag 存 true 放 false
     */
    private void fluidCap(LazyOptional<IFluidHandler> teCap, ItemStack bucket, Level worldIn, BlockPos blockpos, Player playerIn, boolean flag, InteractionHand handIn){
        IFluidHandler teHandler = teCap.orElse(EmptyFluidHandler.INSTANCE);
        EndlessFluidBucketWrapper wrapper = new EndlessFluidBucketWrapper(bucket);
        String registryName = getFluid(bucket).getFluidType().toString();
        wrapper.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent((e) ->{
            FluidStack fluidStack;
            if (flag){
                fluidStack = tryTransfer(e, teHandler, Integer.MAX_VALUE);
            }else fluidStack = tryTransfer(teHandler, e, Integer.MAX_VALUE);
            if (!fluidStack.isEmpty()) {
                worldIn.playSound(null, blockpos, Objects.requireNonNull(fluidStack.getFluid().getPickupSound().orElse(null)), SoundSource.BLOCKS, 1.0F, 1.0F);
                String name = fluidStack.getTag().getString("FluidName");
                if (!name.isEmpty() && registryName != null && !registryName.isEmpty()){
                    String tex = flag ? I18n.get("endless.infinity_bucket.place") + I18n.get(registryName)
                            : I18n.get("endless.infinity_bucket.assume") + I18n.get(name);
                    playerIn.displayClientMessage(Component.translatable(tex + "  " + fluidStack.getAmount() + "mb"), true);
                }
                playerIn.swing(handIn);
            }
        });
    }

    /**
     * 从容器装取
     */
    private InteractionResultHolder<ItemStack> placeFluid(ItemStack bucket, Level worldIn, BlockPos blockpos, BlockPos blockpos1, BlockState blockstate, Fluid fluid, Player playerIn, BlockHitResult hitResult){
        BlockPos pos = blockpos;
        CompoundTag tag = bucket.getOrCreateTag();
        if (fluid == Fluids.WATER) {
            pos = canBlockContainFluid(worldIn, blockpos, blockstate, fluid) ? blockpos : blockpos1;
        }
        int fluidNum = tag.getInt(FLUID_NUMBER);
        if (fluidNum >= 1 && tryPlaceContainedLiquid(playerIn, worldIn, pos, fluid, hitResult)) {
            if (fluidNum == 1){
                tag.putString(FLUID_NAME, "empty");
            }else {
                tag.putInt(FLUID_NUMBER, Math.max(fluidNum - 1, 1));
            }
            if (playerIn instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) playerIn, pos, bucket);
            }

            playerIn.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(bucket, worldIn.isClientSide());
        } else {
            return InteractionResultHolder.fail(bucket);
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
                        System.out.println("Lost {"+ input.getFluidInTank(0).getFluid().getFluidType() + "} fluid during transfer" + (drainedFluid.getAmount() - actualFill));
                    }
                }
                return drainedFluid;
            }
        }
        return FluidStack.EMPTY;
    }

    //尝试放置流体
    public boolean tryPlaceContainedLiquid(@Nullable Player player, Level worldIn, BlockPos posIn, Fluid fluid, @Nullable BlockHitResult hitResult) {
        if (!(fluid instanceof FlowingFluid)) {
            return false;
        } else {
            BlockState blockstate = worldIn.getBlockState(posIn);
            Block block = blockstate.getBlock();
            boolean flag = blockstate.canBeReplaced(fluid); //是否可用被流体替换
            //放置坐标是空气 可替换 属于流体容器且能装入
            boolean flag1 = blockstate.isAir() || flag || block instanceof LiquidBlockContainer && ((LiquidBlockContainer)block).canPlaceLiquid(worldIn, posIn, blockstate, fluid);
            if (!flag1) { //不能放置，则朝玩家方向移动一格
                return hitResult != null && tryPlaceContainedLiquid(player, worldIn, hitResult.getBlockPos().relative(hitResult.getDirection()), fluid,null);
            } else if (worldIn.dimensionType().ultraWarm() && fluid.is(FluidTags.WATER)) { //下界无法放水
                int i = posIn.getX();
                int j = posIn.getY();
                int k = posIn.getZ();
                worldIn.playSound(player, posIn, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F);

                for(int l = 0; l < 8; ++l) {
                    worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                return true;
            } else if (block instanceof LiquidBlockContainer && ((LiquidBlockContainer)block).canPlaceLiquid(worldIn,posIn,blockstate,fluid)) {
                ((LiquidBlockContainer)block).placeLiquid(worldIn, posIn, blockstate, ((FlowingFluid)fluid).getSource(false));
                this.playEmptySound(player, worldIn, posIn, fluid);
                return true;
            } else {
                if (!worldIn.isClientSide && flag && !blockstate.liquid()) {
                    worldIn.destroyBlock(posIn, true);
                }

                if (!worldIn.setBlock(posIn, fluid.defaultFluidState().createLegacyBlock(), 11) && !blockstate.getFluidState().isSource()) {
                    return false;
                } else {
                    this.playEmptySound(player, worldIn, posIn, fluid);
                    return true;
                }
            }
        }
    }

    //播放放置音效
    protected void playEmptySound(@Nullable Player player, Level worldIn, BlockPos pos, Fluid fluid) {
        SoundEvent soundevent = fluid.getPickupSound().orElse(null);
        if(soundevent == null) soundevent = fluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        worldIn.playSound(player, pos, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

//    @Override
//    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
//        return this.getClass() == InfinityBucket.class ? new EndlessFluidBucketWrapper(stack) : super.initCapabilities(stack, nbt);
//    }

    /**
     * 方块是否是含流体方块
     * @return 是 true
     */
    private boolean canBlockContainFluid(Level worldIn, BlockPos posIn, BlockState blockstate, Fluid fluid) {
        return blockstate.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) blockstate.getBlock()).canPlaceLiquid(worldIn, posIn, blockstate, fluid);
    }
}
