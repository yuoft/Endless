package com.yuo.endless.Items.Tool;

import com.simibubi.create.content.contraptions.fluids.FlowSource;
import com.yuo.endless.Config.Config;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * 右键范围装取流体（水和岩浆），并记录数量用于消耗
 */
public class InfinityBucket extends Item {
//    private java.util.function.Supplier<? extends Fluid> fluidSupplier; //桶内流体
//    private Fluid fluid;
    private final static String FLUID_NAME = "fluid_name";
    private final static String FLUID_NUMBER = "fluid_number";

    public InfinityBucket() {
        super(new Properties().group(ModGroup.endless).maxStackSize(1).isImmuneToFire());
//        this.fluid = Fluids.EMPTY;
//        this.fluidSupplier = supplier;
    }

//    public InfinityBucket() {
//        super(new Properties().group(ModGroup.endless).maxStackSize(1).isImmuneToFire());
//        this.fluid = Fluids.EMPTY;
//        this.fluidSupplier = fluid.delegate;
//    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tag = stack.getOrCreateTag();
        String fluidKey = tag.getString(FLUID_NAME);
        Fluid fluid = getFluidForKey(fluidKey);
        if (fluid != Fluids.EMPTY){
            tooltip.add(new TranslationTextComponent("endless.text.itemInfo.bucket_fluid").appendSibling(
                    new TranslationTextComponent(fluid.getRegistryName().toString())));
            int fluidNum = tag.getInt(FLUID_NUMBER);
            if (fluidNum > 0){
                tooltip.add(new TranslationTextComponent("endless.text.itemInfo.bucket_num", fluidNum));
            }
        }
    }

    private Fluid getFluidForKey(String str){
        return Registry.FLUID.getOrDefault(new ResourceLocation(str));
    }

    private Fluid getFluidForKey(ResourceLocation res){
        return Registry.FLUID.getOrDefault(res);
    }

    private Fluid getFluid(ItemStack stack){
        return getFluidForKey(stack.getOrCreateTag().getString(FLUID_NAME));
    }

    @Override
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        super.onUse(worldIn, livingEntityIn, stack, count);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack bucket = playerIn.getHeldItem(handIn);
        Fluid fluid = getFluid(bucket);
        BlockRayTraceResult rayTraceResult = rayTrace(worldIn, playerIn, fluid == Fluids.EMPTY ? RayTraceContext.FluidMode.SOURCE_ONLY : RayTraceContext.FluidMode.NONE);
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, bucket, rayTraceResult);
        if (ret != null) return ret;
        if (rayTraceResult.getType() == RayTraceResult.Type.MISS) {
            if (playerIn.isSneaking() && bucket.getOrCreateTag().getInt(FLUID_NUMBER) > 0){ //潜行右键清空流体
                bucket.getOrCreateTag().remove(FLUID_NAME);
                bucket.getOrCreateTag().remove(FLUID_NUMBER);
                playerIn.swingArm(handIn);
                worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.PLAYERS, 1.0F, 1.0F);
                return ActionResult.func_233538_a_(bucket, worldIn.isRemote);
            }
            return ActionResult.resultPass(bucket);
        } else if (rayTraceResult.getType() != RayTraceResult.Type.BLOCK) {
            return ActionResult.resultPass(bucket);
        } else {
            BlockPos blockpos = rayTraceResult.getPos();
            Direction direction = rayTraceResult.getFace();
            BlockPos blockpos1 = blockpos.offset(direction);
            if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos1, direction, bucket)) {
                CompoundNBT tag = bucket.getOrCreateTag();
                if (fluid == Fluids.EMPTY) { //装
                    BlockState state = worldIn.getBlockState(blockpos);
                    if (state.getBlock() instanceof IBucketPickupHandler) {
                        Fluid fluid0 = ((IBucketPickupHandler)state.getBlock()).pickupFluid(worldIn, blockpos, state);
                        if (fluid0 != Fluids.EMPTY) {
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
                            tag.putString(FLUID_NAME, fluid0.getRegistryName().toString());
                            tag.putInt(FLUID_NUMBER, fluidNum + 1);
                            //填充桶？
                            if (!worldIn.isRemote) {
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity)playerIn, bucket);
                            }

                            return ActionResult.func_233538_a_(bucket, worldIn.isRemote());
                        }
                    }

                    return ActionResult.resultFail(bucket);
                } else { //放
                    BlockState blockstate = worldIn.getBlockState(blockpos);
                    BlockPos pos = canBlockContainFluid(worldIn, blockpos, blockstate, fluid) ? blockpos : blockpos1;
                    int fluidNum = tag.getInt(FLUID_NUMBER);
                    if (fluidNum >= 1 && this.tryPlaceContainedLiquid(playerIn, worldIn, pos, fluid, rayTraceResult)) {
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
            } else {
                return ActionResult.resultFail(bucket);
            }
        }
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
            boolean flag1 = blockstate.isAir() || flag || block instanceof ILiquidContainer && ((ILiquidContainer)block).canContainFluid(worldIn, posIn, blockstate, fluid);
            if (!flag1) { //不能放置，则朝玩家方向移动一格
                return rayTrace != null && this.tryPlaceContainedLiquid(player, worldIn, rayTrace.getPos().offset(rayTrace.getFace()), fluid,null);
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

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundNBT nbt) {
        if (this.getClass() == InfinityBucket.class)
            return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
        else
            return super.initCapabilities(stack, nbt);
    }

    //是否是流体容器
    private boolean canBlockContainFluid(World worldIn, BlockPos posIn, BlockState blockstate, Fluid fluid) {
        return blockstate.getBlock() instanceof ILiquidContainer && ((ILiquidContainer) blockstate.getBlock()).canContainFluid(worldIn, posIn, blockstate, fluid);
    }
}
