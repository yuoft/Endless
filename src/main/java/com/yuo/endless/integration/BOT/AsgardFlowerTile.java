package com.yuo.endless.integration.BOT;

import com.yuo.endless.Tiles.EndlessTileTypes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntityGeneratingFlower;

public class AsgardFlowerTile extends TileEntityGeneratingFlower {
    private static final int RANGE = 8;

    public AsgardFlowerTile() {
        super(EndlessTileTypes.ASGARD_FLOWER_TILE.get());
    }

    @Override
    public void tickFlower() {
        super.tickFlower();
        if (world == null || world.isRemote) return;
        linkCollector();

        if (canGeneratePassively()) {
            int delay = getDelayBetweenPassiveGeneration();
            if (delay > 0 && ticksExisted % delay == 0) {
                addMana(getValueForPassiveGeneration());
            }
        }
        emptyManaIntoCollector();

        double particleChance = 1F - (double) getMana() / (double) getMaxMana() / 3.5F;
        int color = getColor();
        float red = (color >> 16 & 0xFF) / 255F;
        float green = (color >> 8 & 0xFF) / 255F;
        float blue = (color & 0xFF) / 255F;

        if (Math.random() > particleChance) {
            Vector3d offset = world.getBlockState(getPos()).getOffset(world, getPos());
            double x = getPos().getX() + offset.x;
            double y = getPos().getY() + offset.y;
            double z = getPos().getZ() + offset.z;
            BotaniaAPI.instance().sparkleFX(getWorld(), x + 0.3 + Math.random() * 0.5, y + 0.5 + Math.random() * 0.5, z + 0.3 + Math.random() * 0.5, red, green, blue, (float) Math.random(), 5);
        }

        //产能
        long gameTime = world.getGameTime();
        if (gameTime % 5 == 0){
            addMana(Integer.MAX_VALUE);
        }

        //阻止周围产能花枯萎
        for(int dx = -RANGE; dx <= RANGE; dx++)
            for(int dz = -RANGE; dz <= RANGE; dz++){
                BlockPos pos = getEffectivePos().add(dx, 0, dz);
                TileEntity tile = world.getTileEntity(pos);
                if(tile instanceof TileEntityGeneratingFlower){
                    TileEntityGeneratingFlower flower = (TileEntityGeneratingFlower) tile;
                    if(flower.isPassiveFlower()){
                        flower.passiveDecayTicks = 0;
                    }
                }
            }
    }

    @Override
    public int getColor() {
        return 0x5EF2FF;
    }

    //最大魔力
    @Override
    public int getMaxMana() {
        return Integer.MAX_VALUE;
    }

    //作用范围
    @Override
    public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(getEffectivePos(), RANGE);
    }

    @Override
    public void writeToPacketNBT(CompoundNBT cmp) {
        super.writeToPacketNBT(cmp);

    }

    @Override
    public void readFromPacketNBT(CompoundNBT cmp) {
        super.readFromPacketNBT(cmp);

    }

    //是否是被动花
    @Override
    public boolean isPassiveFlower() {
        return true;
    }
}
