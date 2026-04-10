package com.yuo.endless;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Blocks.Fluid.EndlessFluidTypes;
import com.yuo.endless.Client.Sound.ModSounds;
import com.yuo.endless.Compat.Curios.CuriosCompat;
import com.yuo.endless.Compat.PE.EndlessEmc;
import com.yuo.endless.Config.MenuCompat;
import com.yuo.endless.Config.ModConfig;
import com.yuo.endless.Container.EndlessMenuTypes;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Blocks.Fluid.EndlessFluids;
import com.yuo.endless.Event.DataGenEvent;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.NetWork.NetWorkHandler;
import com.yuo.endless.Proxy.ClientProxy;
import com.yuo.endless.Proxy.CommonProxy;
import com.yuo.endless.Proxy.IProxy;
import com.yuo.endless.Recipe.EndlessRecipes;
import com.yuo.endless.Tiles.EndlessTileTypes;
import net.minecraft.core.*;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod("endless")
public class Endless {
	public static final String MOD_ID = "endless";
    public static final IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    @SuppressWarnings("removal")  //禁用过期警告
    public Endless() {
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.SERVER_CONFIG); //配置文件
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EndlessUtils.checkMods();
        //注册物品至mod总线
        EndlessItems.ITEMS.register(modEventBus);
        EndlessUtils.registerModCompat();
        modEventBus.addListener(this::commonSetup);
        EndlessBlocks.BLOCKS.register(modEventBus);
        EndlessTabs.TABS.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        EndlessFluidTypes.FLUID_TYPES.register(modEventBus);
        EndlessFluids.FLUIDS.register(modEventBus);
        EndlessTileTypes.TILE_ENTITIES.register(modEventBus);
        EndlessMenuTypes.CONTAINERS.register(modEventBus);
        EndlessRecipes.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        if (EndlessUtils.isccApi)
            MenuCompat.registerModsPage();
        proxy.registerHandlers(modEventBus);
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
