package com.yuo.endless.items.tool;

import com.yuo.endless.config.ModConfig;
import com.yuo.endless.items.EndlessItems;
import com.yuo.endless.items.MatterCluster;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.*;

/**
 * 范围挖掘工具类
 */
public class ToolHelper {
    private final Map<ItemStack, Integer> map = new HashMap<>();
    private static final Set<BlockPos> set = new HashSet<>();

    /**
     * 根据玩家朝向来破坏方块
     * @param stack 工具
     * @param world 世界
     * @param pos 坐标
     * @param player 玩家
     * @param lv 范围挖掘等级 1:3*3*3；2:5*5*5
     */
	public void onBlockStartBreak(ItemStack stack, Level world, BlockPos pos, Player player, Integer lv) {
        Vec3 vec = player.getLookAngle();
        Direction facing = Direction.getNearest(vec.x, vec.y, vec.z);
        switch (facing){
            case UP:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY(); y <= pos.getY() + (2 * lv); y ++){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            destroyBlock(x, y, z, world, stack, player);
                        }
                    }
                }
                break;
            case DOWN:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY(); y >= pos.getY() - (2 * lv); y --){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            destroyBlock(x, y, z, world, stack, player);
                        }
                    }
                }
                break;
            case EAST:
                for (int x = pos.getX(); x <= pos.getX() + (2 * lv); x ++){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            destroyBlock(x, y, z, world, stack, player);
                        }
                    }
                }
                break;
            case WEST:
                for (int x = pos.getX(); x >= pos.getX() - (2 * lv); x --){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            destroyBlock(x, y, z, world, stack, player);
                        }
                    }
                }
                break;
            case NORTH:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ(); z >= pos.getZ() - (2 * lv); z --){
                            destroyBlock(x, y, z, world, stack, player);
                        }
                    }
                }
                break;
            case SOUTH:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ(); z <= pos.getZ() + (2 * lv); z ++){
                            destroyBlock(x, y, z, world, stack, player);
                        }
                    }
                }
                break;
        }
        spawnMatterCluster(player, world, map);
        map.clear(); //清空map
    }

    /**
     * 无尽工具范围破坏方块
     * @param x 要破坏的方块坐标
     * @param y 坐标
     * @param z 坐标
     * @param world 世界
     * @param stack 工具
     */
    private void destroyBlock(int x, int y, int z, Level world, ItemStack stack, Player player){
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = world.getBlockState(pos);
        Block goalBlock = state.getBlock();
        //排除空气方块和不能用镐挖掘方块 挖掘等级不够
        if (state.isAir()){
            return;
        }
        if (stack.getItem() == EndlessItems.infinityPickaxe.get()){
            //无法采集或在黑名单，就跳过此方块
            if (!state.canHarvestBlock(world, pos, player) || ModConfig.pickaxeBlocks.contains(goalBlock)) return;
        }
        if (stack.getItem() == EndlessItems.infinityShovel.get()){
            if (!state.canHarvestBlock(world, pos, player) || ModConfig.shovelBlocks.contains(goalBlock)) return;
        }
        if (stack.getItem() == EndlessItems.infinityAxe.get()){
            if (ModConfig.SERVER.isAxeChangeGrassBlock.get() && goalBlock instanceof GrassBlock)
                world.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
            //破坏植物和树叶 不添加到物资团
            if (goalBlock instanceof BushBlock || goalBlock instanceof LeavesBlock){
                world.destroyBlock(pos, false);
                return;
            }
            if (!state.canHarvestBlock(world, pos, player) || ModConfig.axeBlocks.contains(goalBlock)) return;
        }
        //是否破坏-1硬度方块
        if (!ModConfig.SERVER.isBreakBedrock.get() && state.canHarvestBlock(world, pos, player)) return;

        //添加到map中，进行掉落收集
        if (state.canHarvestBlock(world, pos, player)){
            Item block = Item.BY_BLOCK.getOrDefault(goalBlock, Items.AIR);
            if (block != null && block != Items.AIR){
                putMapItem(new ItemStack(block), map);
            }
        }else putMapDrops(world, pos, player, stack, map);
        world.removeBlock(pos, false); //移除方块
    }

    /**
     * 添加方块掉落物到map中
     * @param world 世界
     * @param pos 方块坐标
     * @param player 收获玩家
     * @param stack 使用工具
     * @param map 物品map
     */
    public static void putMapDrops(Level world, BlockPos pos, Player player, ItemStack stack, Map<ItemStack, Integer> map){
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!ModConfig.SERVER.isKeepStone.get() && (state.is(Tags.Blocks.STONE) || block == Blocks.DIRT || block == Blocks.COARSE_DIRT
            || block == Blocks.ROOTED_DIRT || block == Blocks.DIRT_PATH)) return; //不保留石头和泥土
        for (ItemStack drop : Block.getDrops(state, (ServerLevel) world, pos, world.getBlockEntity(pos), player, stack)) {
            putMapItem(drop, map);
        }
    }

    /**
     * 添加物品到map
     * @param drop 要添加的物品
     * @param map 物品map
     */
    private static void putMapItem(ItemStack drop, Map<ItemStack, Integer> map){
        ItemStack itemStack = mapEquals(drop, map);
        if (itemStack.isEmpty()){
            map.put(drop, drop.getCount());
        }else {
            map.computeIfPresent(itemStack, (k, integer) -> integer + drop.getCount());
        }
    }

    /**
     * 生成物质团到世界
     * @param player 玩家
     * @param world 世界
     * @param map 物品
     */
    public static void spawnMatterCluster(Player player, Level world, Map<ItemStack, Integer> map){
        List<ItemStack> stacks = MatterCluster.createMatterCluster(map);
        for (ItemStack stack : stacks) {
            if (!player.getAbilities().instabuild){ //非生存模式不生成物质团
                if (ModConfig.SERVER.isMergeMatterCluster.get()){
                    if (!MatterCluster.mergeMatterCluster(stack, player)) //合并
                        world.addFreshEntity(new ItemEntity(world, player.getX(), player.getY(), player.getZ(), stack));
                    else world.playSound(player, player.getOnPos(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0f, 3.0f);
                }else world.addFreshEntity(new ItemEntity(world, player.getX(), player.getY(), player.getZ(), stack));
            }
        }

    }

    /**
     * 判断map中是否有相同键，并且返回键（物品相同）
     * @param stack 要添加的物品
     * @param map 物品map
     * @return 相同的物品
     */
    public static ItemStack mapEquals(ItemStack stack, Map<ItemStack, Integer> map){
        for (ItemStack itemStack : map.keySet()) {
            if (itemStack.equals(stack, false)){
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * 无尽斧 连锁砍树
     * @param world 世界
     * @param origin 树木方块
     * @param player 玩家
     * @param steps 连锁距离
     * @param axe 工具
     */
    public void aoeBlocks(Level world, BlockPos origin, Player player, int steps, ItemStack axe){
        BlockState state = world.getBlockState(origin);
        if (state.isAir()) return;
        if (steps == 0) { //达到最大连锁距离
            return;
        }
        world.destroyBlock(origin, !player.isCreative(), player);

        for (BlockPos pos : getAroundPos(origin)) {
            if (set.contains(pos) || pos.equals(origin)) continue;
            if (isLogAndLeaf(world, pos)){
                steps--;
                set.add(pos); //添加当前坐标
                aoeBlocks(world, pos, player, steps, axe); //递归
            }
        }
    }

    /**
     * 获取当前木头周围一圈的坐标（26个） 去除空气坐标
     * @param pos 中心坐标
     * @return 周围坐标集合
     */
    public static Iterable<BlockPos> getAroundPos(BlockPos pos){
        return BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1));
    }

    /**
     * 判断当前坐标的方块是不是原木或树叶
     * @param world 世界
     * @param pos 要判断的坐标
     * @return true 是原木或树叶
     */
    public static boolean isLogAndLeaf(Level world, BlockPos pos){
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) return false;
        return state.is(BlockTags.LOGS) || state.is(BlockTags.LEAVES);
    }
}
