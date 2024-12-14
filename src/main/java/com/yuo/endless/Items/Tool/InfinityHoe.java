package com.yuo.endless.Items.Tool;

import com.yuo.endless.Config;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.EndlessItemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("removal")
public class InfinityHoe extends HoeItem {
    public InfinityHoe() {
        super(EndlessItemTiers.INFINITY_TOOL, -10, 17.0f, new Properties().tab(EndlessTab.endless).fireResistant());
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (stack.isCorrectToolForDrops(state)){
            return this.getTier().getSpeed();
        }
        return Math.max(super.getDestroySpeed(stack, state), 6.0f);
    }

    //右键催熟周围作物并且收获成熟作物

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player playerIn, InteractionHand hand) {
        ItemStack heldItem = playerIn.getItemInHand(hand);
        if (!level.isClientSide){
            playerIn.swing(InteractionHand.MAIN_HAND);
            BlockPos blockPos = playerIn.getOnPos();
            int rang = 7;
            int height = 2;
            BlockPos minPos = blockPos.offset(-rang, -height, -rang);
            BlockPos maxPos = blockPos.offset(rang, height, rang);
            Map<ItemStack, Integer> map = new HashMap<>();
            for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();
                //收获 可食用作物
                if (block instanceof CropBlock){ //普通作物
                    if (block instanceof BeetrootBlock ? state.getValue(BeetrootBlock.AGE) >= 3 : state.getValue(CropBlock.AGE) >= 7){
                        //对收获作物进行打包
                        ToolHelper.putMapDrops(level, pos, playerIn, new ItemStack(this), map);
                        level.setBlock(pos, state.setValue(block instanceof BeetrootBlock ? BeetrootBlock.AGE : CropBlock.AGE, 0), 11); //重新种植
                    }
                }
                if (block instanceof CocoaBlock){ //可可豆
                    if (state.getValue(CocoaBlock.AGE) >= 2){
                        ToolHelper.putMapDrops(level, pos, playerIn, new ItemStack(this), map);
                        level.setBlock(pos, state.setValue(CocoaBlock.AGE, 0), 11); //重新种植
                    }
                }
                if (block instanceof StemGrownBlock){ //南瓜
                    ToolHelper.putMapDrops(level, pos, playerIn, new ItemStack(this), map);
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                }
                if (block instanceof SweetBerryBushBlock){ //浆果
                    if (state.getValue(SweetBerryBushBlock.AGE) >= 3){
                        ToolHelper.putMapDrops(level, pos, playerIn, new ItemStack(this), map);
                        level.setBlock(pos, state.setValue(SweetBerryBushBlock.AGE, 0), 11); //重新种植
                    }
                }
                //催熟
                if (block instanceof BonemealableBlock && !(block instanceof GrassBlock) && ((BonemealableBlock) block).isValidBonemealTarget(level, pos, state, true)){
                    for (int i = 0; i < 3; i++)
                        ((BonemealableBlock) block).performBonemeal((ServerLevel) level, level.random, pos, state);
                }
            }
            ToolHelper.spawnMatterCluster(playerIn, level, map);
            playerIn.getCooldowns().addCooldown(heldItem.getItem(), 20); //冷却
        }
        return InteractionResultHolder.pass(heldItem);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Block block1 = world.getBlockState(blockpos).getBlock();
        int hook = ForgeEventFactory.onHoeUse(context);
        if (hook != 0) return hook > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        if (context.getClickedFace() != Direction.DOWN && world.getBlockState(blockpos.above()).isAir() &&(block1 instanceof GrassBlock
                || block1.equals(Blocks.DIRT) || block1.equals(Blocks.COARSE_DIRT))) { //对着草方块或泥土使用锄头功能
            BlockState blockstate = Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, 7); //湿润状态的耕地
            Player player = context.getPlayer();
            world.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!world.isClientSide && player != null) {
                int rang = 5;
                BlockPos minPos = blockpos.offset(-rang, 0, -rang);
                BlockPos maxPos = blockpos.offset(rang, 0, rang);
                if (player.isCrouching()){
                    Map<ItemStack, Integer> map = new HashMap<>();
                    Iterable<BlockPos> boxMutable = BlockPos.betweenClosed(minPos, maxPos);
                    for (BlockPos pos : boxMutable) {
                        BlockState state = world.getBlockState(pos);
                        Block block = state.getBlock();
                        if (!world.getBlockState(pos.above()).isAir()){
                            for (int i = 1; i <= 3; i++){
                                harvest(world, pos.above(i), player, map);
                            }
                        }

                        if (world.getBlockState(pos.above()).isAir() && (block instanceof GrassBlock || block.equals(Blocks.DIRT) || block.equals(
                                Blocks.COARSE_DIRT) || block instanceof FarmBlock)){ // 当前为草方块或泥土或耕地， 上方为空气
                            world.setBlock(pos, blockstate, 11);
                        }
                        if (world.getBlockState(pos).isAir() && !world.getBlockState(pos.below()).isAir()){ //当期为空气，下方有方块
                            world.setBlock(pos, blockstate, 11);
                        }
                        if (state.getMaterial().isLiquid() || state.getBlock() instanceof LiquidBlockContainer){ //液体或可替换变为耕地
                            world.setBlock(pos, blockstate, 11);
                        }
                    }
                    ToolHelper.spawnMatterCluster(player, world, map);

                    //填充外边液体
                    Iterable<BlockPos> inBoxMutable = BlockPos.betweenClosed(minPos, maxPos.offset(0, 3, 0));
                    Iterable<BlockPos> allInBoxMutable = BlockPos.betweenClosed(minPos.offset(-1, 0, -1), maxPos.offset(1, 4, 1));
                    for (BlockPos pos: allInBoxMutable){
                        if (!hasBox(pos, inBoxMutable)){ //外壳坐标
                            BlockState state = world.getBlockState(pos);
                            if (state.getMaterial().isLiquid() || state.getBlock() instanceof LiquidBlockContainer)//如果是液体，则将其填埋
                                world.setBlock(pos, Blocks.STONE.defaultBlockState(), 11);
                        }
                    }
                }else world.setBlock(blockpos, blockstate, 11); //未潜行耕种一个方块
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        return InteractionResult.PASS;
    }

    //判断给定坐标是否包含在坐标列表中
    private boolean hasBox(BlockPos pos, Iterable<BlockPos> box){
        for (BlockPos pos1 : box){
            if (pos1.getX() == pos.getX() && pos1.getY() == pos.getY() && pos1.getZ() == pos.getZ()) return true;
        }
        return false;
    }

    //清空上方3格高度方块
    private void harvest(Level world, BlockPos pos, Player player, Map<ItemStack, Integer> map){
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (world.getBlockState(pos).isAir() || block instanceof CropBlock || block instanceof StemBlock
                || block instanceof CocoaBlock || block instanceof SweetBerryBushBlock){
            return;
        }
        if (Config.hoeBlocks.contains(block)) return;
        //添加到map中
        if (block.equals(Blocks.BEDROCK)){
            ItemStack stack1 = new ItemStack(Blocks.BEDROCK);
            ItemStack itemStack = ToolHelper.mapEquals(stack1, map);
            if (!itemStack.isEmpty())
                map.put(itemStack, map.get(itemStack) + stack1.getCount());
            else map.put(stack1, stack1.getCount());
        }else ToolHelper.putMapDrops(world, pos, player, new ItemStack(this), map);
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 0;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        stack.getOrCreateTag().putInt("Damage", 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int pSlotId, boolean pIsSelected) {
        int damage = stack.getDamageValue();
        if (damage > 0){
            stack.getOrCreateTag().putInt("Damage", 0);
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(tab)){
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putBoolean("Unbreakable",true);
            stacks.add(stack);
        }
    }

    //创建一个能被快速捡起的物品实体
    @org.jetbrains.annotations.Nullable
    @Override
    public Entity createEntity(Level level, Entity location, ItemStack stack) {
        return new EndlessItemEntity(level, location, stack);
    }

    //允许创建自定义物品实体
    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }
}
