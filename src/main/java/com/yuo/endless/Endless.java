package com.yuo.endless;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Blocks.Fluid.EndlessFluidTypes;
import com.yuo.endless.Client.Sound.ModSounds;
import com.yuo.endless.Config.ModConfig;
import com.yuo.endless.Container.EndlessMenuTypes;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Blocks.Fluid.EndlessFluids;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Proxy.ClientProxy;
import com.yuo.endless.Proxy.CommonProxy;
import com.yuo.endless.Proxy.IProxy;
import com.yuo.endless.Recipe.EndlessRecipes;
import com.yuo.endless.Tiles.EndlessTileTypes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
        EndlessBlocks.BLOCKS.register(modEventBus);
        EndlessTabs.TABS.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        EndlessFluidTypes.FLUID_TYPES.register(modEventBus);
        EndlessFluids.FLUIDS.register(modEventBus);
        EndlessTileTypes.TILE_ENTITIES.register(modEventBus);
        EndlessMenuTypes.CONTAINERS.register(modEventBus);
        EndlessRecipes.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        proxy.registerHandlers(modEventBus);
    }
}
