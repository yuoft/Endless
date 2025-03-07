package com.yuo.endless.Items.Tool;

import com.google.common.collect.Sets;
import com.yuo.endless.Config;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.MatterCluster;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.*;

/**
 * 范围挖掘工具类
 */
public class ToolHelper {
    private final Map<ItemStack, Integer> map = new HashMap<>();
    private static final Set<BlockPos> set = new HashSet<>();
    public static final Set<Material> MATERIAL_PICKAXE = Sets.newHashSet(Material.STONE, Material.HEAVY_METAL, Material.ICE,
            Material.GLASS, Material.EXPLOSIVE, Material.BUILDABLE_GLASS, Material.ICE_SOLID, Material.SPONGE, Material.SHULKER_SHELL, Material.WOOL,
            Material.PISTON, Material.BARRIER);
    public static final Set<Material> MATERIAL_AXE = Sets.newHashSet(Material.WOOD, Material.PORTAL, Material.WEB, Material.PLANT, Material.WATER_PLANT, Material.REPLACEABLE_PLANT,
            Material.REPLACEABLE_FIREPROOF_PLANT, Material.REPLACEABLE_WATER_PLANT, Material.NETHER_WOOD, Material.BAMBOO, Material.BAMBOO_SAPLING,
            Material.LEAVES, Material.CACTUS);
    public static final Set<Material> MATERIAL_SHOVEL = Sets.newHashSet(Material.DIRT, Material.SAND, Material.SNOW, Material.GRASS, Material.CLAY, Material.CAKE,
            Material.SNOW, Material.POWDER_SNOW, Material.TOP_SNOW);

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
     * 破坏方块
     * @param x 要破坏的方块坐标
     * @param y 坐标
     * @param z 坐标
     * @param world 世界
     * @param stack 工具
     */
    private void destroyBlock(int x, int y, int z, Level world, ItemStack stack, Player player){
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = world.getBlockState(pos);
        if (stack.getItem() == EndlessItems.infinityAxe.get() && Config.SERVER.isAxeChangeGrassBlock.get() && state.getBlock() instanceof GrassBlock){ //将草方块转变为泥土
            world.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
        }
        //排除空气方块和不能用镐挖掘方块 挖掘等级不够
        if (state.isAir()){
            return;
        }

        if (!stack.isCorrectToolForDrops(state)  //采集树叶
                && !(stack.getItem() == EndlessItems.infinityAxe.get() && state.getBlock() instanceof LeavesBlock)) return;

        if (stack.getItem() == EndlessItems.infinityPickaxe.get() && !MATERIAL_PICKAXE.contains(state.getMaterial())) return;
        if (stack.getItem() == EndlessItems.infinityShovel.get() && !MATERIAL_SHOVEL.contains(state.getMaterial())) return;
        if (stack.getItem() == EndlessItems.infinityAxe.get() && !MATERIAL_AXE.contains(state.getMaterial())) return;
        //黑名单
        if (stack.getItem() == EndlessItems.infinityPickaxe.get() && Config.pickaxeBlocks.contains(state.getBlock())) return;
        if (stack.getItem() == EndlessItems.infinityShovel.get() && Config.shovelBlocks.contains(state.getBlock())) return;
        if (stack.getItem() == EndlessItems.infinityAxe.get() && Config.axeBlocks.contains(state.getBlock())) return;

        if (!Config.SERVER.isBreakBedrock.get() && state.canHarvestBlock(world, pos, player)) return;
//        world.destroyBlock(pos, false, player); //破坏方块

        //添加到map中
        if (state.canHarvestBlock(world, pos, player)){
            Item block = Item.BY_BLOCK.getOrDefault(state.getBlock(), Items.AIR);
            if (block != null && block != Items.AIR){
                putMapItem(new ItemStack(block), map);
            }
        }else putMapDrops(world, pos, player, stack, map);
        world.removeBlock(pos, false); //破坏方块
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
        if (!Config.SERVER.isKeepStone.get() && (state.is(Tags.Blocks.STONE) || state.getMaterial() == Material.DIRT)) return; //不保留石头和泥土
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
            if (!player.isCreative()){ //创造模式不生成物质团
                if (Config.SERVER.isMergeMatterCluster.get()){
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
