package com.yuo.endless.Proxy;

import com.yuo.endless.Compat.Curios.CuriosCompat;
import com.yuo.endless.Compat.PE.EndlessEmc;
import com.yuo.endless.Config.ModConfig;
import com.yuo.endless.EndlessUtils;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.NetWork.NetWorkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

public class CommonProxy implements IProxy {
    @Override
    public void registerHandlers(IEventBus modBus) {
        IProxy.super.registerHandlers(modBus);
        modBus.addListener(this::commonSetup);
    }

    @SuppressWarnings("removal")
    private void commonSetup(final FMLCommonSetupEvent event) {
        if (EndlessUtils.isCurios){
            FMLJavaModLoadingContext.get().getModEventBus().addListener(CuriosCompat::sendImc);
        }
        if (EndlessUtils.isPE){
            EndlessEmc.registerEmc();
        }

        ModConfig.loadConfig(); //加载工具黑名单
        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
        //添加发射器
        DefaultDispenseItemBehavior itemBehavior = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultBehaviour = new DefaultDispenseItemBehavior();

            /**
             * 分配指定的堆栈，播放分配声音并生成粒子。
             */
            @NotNull
            @Override
            public ItemStack execute(BlockSource source, ItemStack stack) {
                BucketItem bucketitem = (BucketItem)stack.getItem();
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                Position position = DispenserBlock.getDispensePosition(source);
                Level world = source.getLevel();
                BlockPos blockPos = source.getPos().relative(direction);
                DefaultDispenseItemBehavior.spawnItem(world, stack.split(1), 6, direction, position);
                if (bucketitem.emptyContents(null, world, blockPos, null)) {
                    bucketitem.checkExtraContent(null, world, stack, blockPos);
                    return new ItemStack(Items.BUCKET);
                } else {
                    return this.defaultBehaviour.dispense(source, stack);
                }
            }
        };
        DispenserBlock.registerBehavior(EndlessItems.infinityFluidBucket.get(), itemBehavior);
    }
}
