package com.yuo.endless.Items.Tool;

import com.google.common.collect.Sets;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Items.MatterCluster;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 范围挖掘工具类
 */
public class ItemHander 
{
    private final Map<ItemStack, Integer> map = new HashMap<>();
    private static final Set<BlockPos> set = new HashSet<>();
    public static final Set<Material> MATERIAL_PICKAXE = Sets.newHashSet(Material.ROCK, Material.ANVIL, Material.IRON, Material.ICE,
            Material.GLASS, Material.TNT, Material.REDSTONE_LIGHT, Material.PACKED_ICE, Material.SPONGE, Material.SHULKER, Material.WOOL,
            Material.PISTON, Material.CORAL, Material.GOURD);
    public static final Set<Material> MATERIAL_AXE = Sets.newHashSet(Material.WOOD, Material.PORTAL, Material.WEB, Material.PLANTS, Material.OCEAN_PLANT,
            Material.NETHER_PLANTS, Material.TALL_PLANTS, Material.SEA_GRASS, Material.NETHER_WOOD, Material.BAMBOO, Material.BAMBOO_SAPLING,
            Material.LEAVES, Material.CACTUS);
    public static final Set<Material> MATERIAL_SHOVEL = Sets.newHashSet(Material.SAND, Material.EARTH, Material.SNOW, Material.CLAY, Material.ORGANIC,
            Material.SNOW_BLOCK);

    /**
     * 根据玩家朝向来破坏方块
     * @param stack 工具
     * @param world 世界
     * @param pos 坐标
     * @param player 玩家
     * @param lv 范围挖掘等级 1:3*3*3；2:5*5*5
     * @param type 工具类型
     */
	public void onBlockStartBreak(ItemStack stack, World world, BlockPos pos, PlayerEntity player, Integer lv, ToolType type) {
        Vector3d vec = player.getLookVec();
        Direction facing = Direction.getFacingFromVector(vec.x, vec.y, vec.z);
        switch (facing){
            case UP:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY(); y <= pos.getY() + (2 * lv); y ++){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            destroyBlock(x, y, z, world, stack, player, type);
                        }
                    }
                }
                break;
            case DOWN:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY(); y >= pos.getY() - (2 * lv); y --){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            destroyBlock(x, y, z, world, stack, player, type);
                        }
                    }
                }
                break;
            case EAST:
                for (int x = pos.getX(); x <= pos.getX() + (2 * lv); x ++){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            destroyBlock(x, y, z, world, stack, player, type);
                        }
                    }
                }
                break;
            case WEST:
                for (int x = pos.getX(); x >= pos.getX() - (2 * lv); x --){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            destroyBlock(x, y, z, world, stack, player, type);
                        }
                    }
                }
                break;
            case NORTH:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ(); z >= pos.getZ() - (2 * lv); z --){
                            destroyBlock(x, y, z, world, stack, player, type);
                        }
                    }
                }
                break;
            case SOUTH:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ(); z <= pos.getZ() + (2 * lv); z ++){
                            destroyBlock(x, y, z, world, stack, player, type);
                        }
                    }
                }
                break;
        }
        spawnMatterCluster(player, world, map);
        map.clear(); //清空map
    }

    /**
     * 无尽斧砍树
     * @param pos 坐标
     * @param world 世界
     * @param stack 工具
     * @param player 玩家
     */
    private void destroyBlock(BlockPos pos, World world, ItemStack stack, PlayerEntity player){
        BlockState state = world.getBlockState(pos);
        if (world.isAirBlock(pos) || state.getHarvestLevel() > stack.getHarvestLevel(ToolType.AXE, player, state)
                || !MATERIAL_AXE.contains(state.getMaterial())){
            return;
        }
        world.destroyBlock(pos, true, player); //破坏方块
    }

    /**
     * 破坏方块
     * @param x 要破坏的方块坐标
     * @param y 坐标
     * @param z 坐标
     * @param world 世界
     * @param stack 工具
     */
    private void destroyBlock(int x, int y, int z, World world, ItemStack stack, PlayerEntity player, ToolType type){
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = world.getBlockState(pos);
        if (type == ToolType.AXE && state.getBlock() instanceof GrassBlock){ //将草方块转变为泥土
            world.setBlockState(pos, Blocks.DIRT.getDefaultState(),2);
        }
        //排除空气方块和不能用镐挖掘方块 挖掘等级不够
        if (world.isAirBlock(pos) || state.getHarvestLevel() > stack.getHarvestLevel(type, player, state)){
            return;
        }
        if (type == ToolType.PICKAXE && !MATERIAL_PICKAXE.contains(state.getMaterial())) return;
        if (type == ToolType.SHOVEL && !MATERIAL_SHOVEL.contains(state.getMaterial())) return;
        if (type == ToolType.AXE && !MATERIAL_AXE.contains(state.getMaterial())) return;
        //黑名单
        if (type == ToolType.PICKAXE && Config.pickaxeBlocks.contains(state.getBlock())) return;
        if (type == ToolType.SHOVEL && Config.shovelBlocks.contains(state.getBlock())) return;
        if (type == ToolType.AXE && Config.axeBlocks.contains(state.getBlock())) return;

        //物品超过64组则不继续添加
        if (map.size() > 64){
            world.destroyBlock(pos, false, player); //破坏方块
            return;
        }
        if (!Config.SERVER.isBreakBedrock.get() && state.getBlock().equals(Blocks.BEDROCK)) return;
        //添加到map中
        if (state.getBlock().equals(Blocks.BEDROCK)){
            ItemStack stack1 = new ItemStack(Blocks.BEDROCK);
            putMapItem(stack1, map);
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
    public static void putMapDrops(World world, BlockPos pos, PlayerEntity player, ItemStack stack, Map<ItemStack, Integer> map){
        BlockState state = world.getBlockState(pos);
        if (!Config.SERVER.isKeepStone.get() && (state.isIn(Tags.Blocks.STONE) || state.isIn(Tags.Blocks.DIRT))) return; //不保留石头和泥土
        for (ItemStack drop : Block.getDrops(state, (ServerWorld) world, pos, world.getTileEntity(pos), player, stack)) {
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
        int dropCount = drop.getCount();
        Integer maxCount = Config.SERVER.matterClusterMaxCount.get();
        if (!itemStack.isEmpty()) {
            Integer mapCount = map.get(itemStack);
            if (mapCount + dropCount > maxCount) { //数量超过限制
                map.put(itemStack, maxCount);
//                putMapItem(new ItemStack(drop.getItem(), mapCount + dropCount - mapCount), map);
            } else map.put(itemStack, mapCount + dropCount);
        } else {
            if (dropCount > maxCount){
                map.put(drop, maxCount);
//                putMapItem(new ItemStack(drop.getItem(), dropCount - maxCount), map);
            }else map.put(drop, dropCount);
        }
    }

    /**
     * 生成物质团到世界
     * @param player 玩家
     * @param world 世界
     * @param map 物品
     */
    public static void spawnMatterCluster(PlayerEntity player, World world, Map<ItemStack, Integer> map){
        ItemStack stack = MatterCluster.setMap(map);
        if (!player.isCreative() && map.size() > 0){ //创造模式不生成物质团
            if (Config.SERVER.isMergeMatterCluster.get()){
                if (!MatterCluster.mergeMatterCluster(stack, player)) //合并
                    world.addEntity(new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), stack));
                else world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1.0f, 3.0f);
            }else world.addEntity(new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), stack));
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
            if (itemStack.isItemEqual(stack)){
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
     * @param steps 连锁数量
     * @param stack 工具
     */
    public void aoeBlocks(World world, BlockPos origin, PlayerEntity player, int steps, ItemStack stack){
        if (world.isAirBlock(origin)) {
            return;
        }
        destroyBlock(origin, world, stack, player);
        if (steps == 0) { //达到最大连锁数量
            return;
        }
        for (Direction dir : Direction.values()) {
            BlockPos stepPos = origin.offset(dir);
            if (set.contains(stepPos)) { //防止操作重复坐标
                continue;
            }
            BlockState stepState = world.getBlockState(stepPos);
            Block stepBlock = stepState.getBlock();
            boolean log = stepState.getMaterial() == Material.WOOD;
            boolean leaf = stepBlock instanceof LeavesBlock;
            if (log || leaf) {
                int step = steps - 1; //剩余数量-1
                aoeBlocks(world, stepPos, player, step, stack); //递归
                set.add(stepPos); //添加当前坐标
            }
        }
    }
}
