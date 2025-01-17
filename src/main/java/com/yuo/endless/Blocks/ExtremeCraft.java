package com.yuo.endless.Blocks;

import com.yuo.endless.Tiles.AbsCraftTile;
import com.yuo.endless.Tiles.EnderCraftTile;
import com.yuo.endless.Tiles.ExtremeCraftTile;
import com.yuo.endless.Tiles.NetherCraftTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class ExtremeCraft extends Block {
    private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent("gui.endless.extreme_crafting_table");
    private final CraftType type;

    public ExtremeCraft(CraftType type) {
        super(type.properties);
        this.type = type;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        switch (type) {
            case EXTREME_CRAFT: return new ExtremeCraftTile();
            case ENDER_CRAFT: return new EnderCraftTile();
            case NETHER_CRAFT: return new NetherCraftTile();
            default: throw new RuntimeException("Unknown type: " + type);
        }
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof AbsCraftTile){
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tile, pos);
            }
//            player.openContainer(state.getContainer(worldIn, pos));
            player.addStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof AbsCraftTile){
            switch (type) {
                case EXTREME_CRAFT: return new ExtremeCraftTile();
                case ENDER_CRAFT: return new EnderCraftTile();
                case NETHER_CRAFT: return new NetherCraftTile();
                default: throw new RuntimeException("Unknown type: " + type);
            }
        }return null;
    }

    //被破坏时 里面所有物品掉落
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.matchesBlock(newState.getBlock())) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof AbsCraftTile) {
                ((AbsCraftTile) tileentity).dropItem(worldIn, pos);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
        if (!isMoving) {
            for(Direction direction : Direction.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(direction), this);
            }
        }
    }

    /**
     * 合成台属性
     */
    public enum CraftType{
        EXTREME_CRAFT("extreme_craft", 9,81,
                Properties.create(Material.ROCK).hardnessAndResistance(15, 100).harvestLevel(3)
                        .harvestTool(ToolType.PICKAXE).sound(SoundType.GLASS).setRequiresTool()),
        ENDER_CRAFT("ender_craft", 7,49,
                Properties.create(Material.ROCK).hardnessAndResistance(10, 50).harvestLevel(2)
                        .harvestTool(ToolType.PICKAXE).sound(SoundType.STONE).setRequiresTool()),
        NETHER_CRAFT("nether_craft", 5,25,
                Properties.create(Material.ROCK).hardnessAndResistance(5, 30).harvestLevel(1)
                        .harvestTool(ToolType.PICKAXE).sound(SoundType.NETHER_BRICK).setRequiresTool());

        private final String type;
        private final int craftNum;
        private final int craftTotal;
        private final Properties properties;
        CraftType(String type, int craftNum, int craftTotal, Properties properties){
            this.type = type;
            this.craftNum = craftNum;
            this.craftTotal = craftTotal;
            this.properties = properties;
        }

        /**
         * @return 合成栏总数
         */
        public int getCraftTotal() {
            return craftTotal;
        }

        public Properties getProperties() {
            return properties;
        }

        /**
         * @return 配方大小
         */
        public int getCraftNum() {
            return craftNum;
        }

        public String getType() {
            return type;
        }
    }
}
