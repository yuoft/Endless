package com.yuo.endless.Items.Tool;

import com.google.common.collect.Sets;
import com.yuo.endless.Items.MatterCluster;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
    private Map<ItemStack, Integer> map = new HashMap<>();
    private static Set<BlockPos> set = new HashSet<>();
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
     * @param stack
     * @param world
     * @param pos
     * @param player
     * @param lv 范围挖掘等级 1:3*3*3；2:5*5*5
     * @param type
     */
	public void onBlockStartBreak(ItemStack stack, World world, BlockPos pos, PlayerEntity player, Integer lv, ToolType type) {
        Vector3d vec = player.getLookVec();
        Direction facing = Direction.getFacingFromVector(vec.x, vec.y, vec.z);
        switch (facing){
            case UP:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY(); y <= pos.getY() + (2 * lv); y ++){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            destroyBlocks(x, y, z, world, stack, player, type);
                        }
                    }
                }
                break;
            case DOWN:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY(); y >= pos.getY() - (2 * lv); y --){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            destroyBlocks(x, y, z, world, stack, player, type);
                        }
                    }
                }
                break;
            case EAST:
                for (int x = pos.getX(); x <= pos.getX() + (2 * lv); x ++){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            destroyBlocks(x, y, z, world, stack, player, type);
                        }
                    }
                }
                break;
            case WEST:
                for (int x = pos.getX(); x >= pos.getX() - (2 * lv); x --){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ() - lv; z <= pos.getZ() + lv; z ++){
                            destroyBlocks(x, y, z, world, stack, player, type);
                        }
                    }
                }
                break;
            case NORTH:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ(); z >= pos.getZ() - (2 * lv); z --){
                            destroyBlocks(x, y, z, world, stack, player, type);
                        }
                    }
                }
                break;
            case SOUTH:
                for (int x = pos.getX() - lv; x <= pos.getX() + lv; x ++){
                    for (int y = pos.getY() - lv; y <= pos.getY() + lv; y ++){
                        for (int z = pos.getZ(); z <= pos.getZ() + (2 * lv); z ++){
                            destroyBlocks(x, y, z, world, stack, player, type);
                        }
                    }
                }
                break;
        }
        spawnMatterCluster(player, world, map);
        map.clear(); //清空map
    }

    private void destroyBlocks(BlockPos pos, World world, ItemStack stack, PlayerEntity player, ToolType type){
        BlockState state = world.getBlockState(pos);
        if (world.isAirBlock(pos) || state.getHarvestLevel() > stack.getHarvestLevel(type, player, state)
                || !MATERIAL_AXE.contains(state.getMaterial())){
            return;
        }
        world.destroyBlock(pos, true, player); //破坏方块
    }

    /**
     * 破坏方块
     * @param x 要破坏的方块坐标
     * @param y
     * @param z
     * @param world 世界
     * @param stack 工具
     */
    private void destroyBlocks(int x, int y, int z, World world, ItemStack stack, PlayerEntity player, ToolType type){
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof GrassBlock){
            world.setBlockState(pos, Blocks.DIRT.getDefaultState(),2);
        }
        //排除空气方块和不能用镐挖掘方块 挖掘等级不够
        if (world.isAirBlock(pos) || state.getHarvestLevel() > stack.getHarvestLevel(type, player, state)){
            return;
        }
        if (type == ToolType.PICKAXE && !MATERIAL_PICKAXE.contains(state.getMaterial())) return;
        if (type == ToolType.SHOVEL && !MATERIAL_SHOVEL.contains(state.getMaterial())) return;
        if (type == ToolType.AXE && !MATERIAL_AXE.contains(state.getMaterial())) return;
        //添加到map中
        if (state.getBlock().equals(Blocks.BEDROCK)){
            ItemStack stack1 = new ItemStack(Blocks.BEDROCK);
            putMapItem(stack1, map);
        }else putMapDrops(world, pos, player, stack, map);
        world.destroyBlock(pos, false, player); //破坏方块
    }

    /**
     * 添加方块掉落物到map中
     * @param world
     * @param pos
     * @param player
     * @param stack
     * @param map
     */
    public static void putMapDrops(World world, BlockPos pos, PlayerEntity player, ItemStack stack, Map<ItemStack, Integer> map){
        for (ItemStack drop : Block.getDrops(world.getBlockState(pos), (ServerWorld) world, pos, world.getTileEntity(pos), player, stack)) {
            putMapItem(drop, map);
        }
    }

    /**
     * 添加物品到map
     * @param drop
     * @param map
     */
    private static void putMapItem(ItemStack drop, Map<ItemStack, Integer> map){
        ItemStack itemStack = mapEquals(drop, map);
        if (!itemStack.isEmpty())
            map.put(itemStack, map.get(itemStack) + drop.getCount());
        else map.put(drop, drop.getCount());
    }

    /**
     * 生成物质团到世界
     * @param player
     * @param world
     * @param map
     */
    public static void spawnMatterCluster(PlayerEntity player, World world, Map<ItemStack, Integer> map){
        if (!player.isCreative() && map.size() > 0)
            world.addEntity(new ItemEntity(world, player.getPosX(), player.getPosY(), player.getPosZ(), MatterCluster.setMap(map)));
    }

    /**
     * 判断map中是否有相同键，并且返回键（物品相同）
     * @param stack
     * @param map
     * @return
     */
    public static ItemStack mapEquals(ItemStack stack, Map<ItemStack, Integer> map){
        for (ItemStack itemStack : map.keySet()) {
            if (itemStack.getItem() == stack.getItem()){
                return itemStack;
            }
        }
        return ItemStack.EMPTY;
    }

    public void aoeBlocks(World world, BlockPos origin, PlayerEntity player, int steps, ItemStack stack, boolean leaves, boolean force){
        BlockState originState = world.getBlockState(origin);
        Block originBlock = originState.getBlock();
        if (!force && originBlock.isAir(originState, world, origin)) {
            return;
        }
        destroyBlocks(origin, world, stack, player, ToolType.AXE);
        if (steps == 0) {
            return;
        }
        for (Direction dir : Direction.values()) {
            BlockPos stepPos = origin.offset(dir);
            if (set.contains(stepPos)) {
                continue;
            }
            BlockState stepState = world.getBlockState(stepPos);
            Block stepBlock = stepState.getBlock();
            boolean log = stepState.getMaterial() == Material.WOOD;
            boolean leaf = stepBlock instanceof LeavesBlock;
            if (log || leaf) {
                int step = steps - 1;
                steps = leaf ? leaves ? steps : 3 : steps;
                aoeBlocks(world, stepPos, player, step, stack, leaf, false);
                set.add(stepPos);
            }
        }
    }
}
