package com.yuo.endless.Items.Tool;

import com.yuo.endless.tab.ModGroup;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class InfinityHoe extends HoeItem {
    public InfinityHoe() {
        super(MyItemTier.INFINITY_TOOL, -10, 17.0f, new Properties().group(ModGroup.myGroup).isImmuneToFire());
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.getHarvestTool() == ToolType.HOE){
            return 999.0f;
        }
        return Math.min(super.getDestroySpeed(stack, state), 6.0f);
    }

    //右键催熟周围作物并且收获成熟作物
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote){
            playerIn.swingArm(Hand.MAIN_HAND);
            BlockPos blockPos = playerIn.getPosition();
            int rang = 7;
            int height = 2;
            BlockPos minPos = blockPos.add(-rang, -height, -rang);
            BlockPos maxPos = blockPos.add(rang, height, rang);
            Map<ItemStack, Integer> map = new HashMap<>();
            for (BlockPos pos : BlockPos.getAllInBoxMutable(minPos, maxPos)) {
                BlockState state = worldIn.getBlockState(pos);
                Block block = state.getBlock();
                //收获 可食用作物
                if (block instanceof CropsBlock){ //普通作物
                    if (block instanceof BeetrootBlock ? state.get(BeetrootBlock.BEETROOT_AGE) >= 3 : state.get(CropsBlock.AGE) >= 7){
                        //对收获作物进行打包
                        ItemHander.putMapDrops(worldIn, pos, playerIn, new ItemStack(this), map);
                        worldIn.setBlockState(pos, state.with(block instanceof BeetrootBlock ? BeetrootBlock.BEETROOT_AGE:CropsBlock.AGE, 0), 11); //重新种植
                    }
                }
                if (block instanceof CocoaBlock){ //可可豆
                    if (state.get(CocoaBlock.AGE) >= 2){
                        ItemHander.putMapDrops(worldIn, pos, playerIn, new ItemStack(this), map);
                        worldIn.setBlockState(pos, state.with(CocoaBlock.AGE, 0), 11); //重新种植
                    }
                }
                if (block instanceof StemGrownBlock){ //南瓜
                    ItemHander.putMapDrops(worldIn, pos, playerIn, new ItemStack(this), map);
                    worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
                }
                if (block instanceof SweetBerryBushBlock){ //浆果
                    if (state.get(SweetBerryBushBlock.AGE) >= 3){
                        ItemHander.putMapDrops(worldIn, pos, playerIn, new ItemStack(this), map);
                        worldIn.setBlockState(pos, state.with(SweetBerryBushBlock.AGE, 0), 11); //重新种植
                    }
                }
                //催熟
                if (block instanceof IGrowable && !(block instanceof GrassBlock) && ((IGrowable) block).canGrow(worldIn, pos, state, true)){
                    for (int i = 0; i < 3; i++)
                        ((IGrowable) block).grow((ServerWorld) worldIn, worldIn.rand, pos, state);
                }
            }
            ItemHander.spawnMatterCluster(playerIn, worldIn, map);
            playerIn.getCooldownTracker().setCooldown(heldItem.getItem(), 20); //冷却
        }
        return ActionResult.resultPass(heldItem);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockpos = context.getPos();
        Block block1 = world.getBlockState(blockpos).getBlock();
        int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(context);
        if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
        if (context.getFace() != Direction.DOWN && world.isAirBlock(blockpos.up()) &&(block1 instanceof GrassBlock
                || block1.equals(Blocks.DIRT) || block1.equals(Blocks.COARSE_DIRT))) { //对着草方块或泥土使用锄头功能
            BlockState blockstate = Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, 7); //湿润状态的耕地
            PlayerEntity playerentity = context.getPlayer();
            world.playSound(playerentity, blockpos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!world.isRemote && playerentity != null) {
                int rang = 5;
                BlockPos minPos = blockpos.add(-rang, 0, -rang);
                BlockPos maxPos = blockpos.add(rang, 0, rang);
                if (playerentity.isSneaking()){
                    Map<ItemStack, Integer> map = new HashMap<>();
                    Iterable<BlockPos> boxMutable = BlockPos.getAllInBoxMutable(minPos, maxPos);
                    for (BlockPos pos : boxMutable) {
                        BlockState state = world.getBlockState(pos);
                        Block block = state.getBlock();
                        if (!world.isAirBlock(pos.up())){
                            for (int i = 1; i <= 3; i++){
                                harvest(world, pos.up(i), playerentity, map);
                            }
                        }

                        if (world.isAirBlock(pos.up()) && (block instanceof GrassBlock || block.equals(Blocks.DIRT) || block.equals(
                                Blocks.COARSE_DIRT) || block instanceof FarmlandBlock)){ // 当前为草方块或泥土或耕地， 上方为空气
                            world.setBlockState(pos, blockstate, 11);
                        }
                        if (world.isAirBlock(pos) && !world.isAirBlock(pos.down())){ //当期为空气，下方有方块
                            world.setBlockState(pos, blockstate, 11);
                        }
                        if (state.getMaterial().isLiquid() || state.getBlock() instanceof ILiquidContainer){ //液体或可替换变为耕地
                            world.setBlockState(pos, blockstate, 11);
                        }
                    }
                    ItemHander.spawnMatterCluster(playerentity, world, map);

                    //填充外边液体
                    Iterable<BlockPos> inBoxMutable = BlockPos.getAllInBoxMutable(minPos, maxPos.add(0, 3, 0));
                    Iterable<BlockPos> allInBoxMutable = BlockPos.getAllInBoxMutable(minPos.add(-1, 0, -1), maxPos.add(1, 4, 1));
                    for (BlockPos pos: allInBoxMutable){
                        if (!hasBox(pos, inBoxMutable)){ //外壳坐标
                            BlockState state = world.getBlockState(pos);
                            if (state.getMaterial().isLiquid() || state.getBlock() instanceof ILiquidContainer)//如果是液体，则将其填埋
                            world.setBlockState(pos, Blocks.STONE.getDefaultState());
                        }
                    }
                }else world.setBlockState(blockpos, blockstate, 11); //未潜行耕种一个方块
            }
            return ActionResultType.func_233537_a_(world.isRemote);
        }
        return ActionResultType.PASS;
    }

    //判断给定坐标是否包含在坐标列表中
    private boolean hasBox(BlockPos pos, Iterable<BlockPos> box){
        for (BlockPos pos1 : box){
            if (pos1.getX() == pos.getX() && pos1.getY() == pos.getY() && pos1.getZ() == pos.getZ()) return true;
        }
        return false;
    }

    //清空上方3格高度方块
    private void harvest(World world, BlockPos pos, PlayerEntity player, Map<ItemStack, Integer> map){
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (world.isAirBlock(pos) || block instanceof CropsBlock || block instanceof StemBlock
                || block instanceof CocoaBlock || block instanceof SweetBerryBushBlock){
            return;
        }
        //添加到map中
        if (block.equals(Blocks.BEDROCK)){
            ItemStack stack1 = new ItemStack(Blocks.BEDROCK);
            ItemStack itemStack = ItemHander.mapEquals(stack1, map);
            if (!itemStack.isEmpty())
                map.put(itemStack, map.get(itemStack) + stack1.getCount());
            else map.put(stack1, stack1.getCount());
        }else ItemHander.putMapDrops(world, pos, player, new ItemStack(this), map);
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }
    @Override
    public boolean isDamageable() {
        return false;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EndlessItemEntity(world, location.getPosX(), location.getPosY(), location.getPosZ(), itemstack);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }
}
