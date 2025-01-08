package com.yuo.endless.Compat.Jade;

import com.yuo.endless.Blocks.AbsNeutronCollector;
import com.yuo.endless.Blocks.NeutroniumCompressor;
import com.yuo.endless.Tiles.AbsNeutronCollectorTile;
import com.yuo.endless.Tiles.NeutroniumCompressorTile;
import mcp.mobius.waila.api.*;

@WailaPlugin
public class EndlessJade implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        IWailaPlugin.super.register(registration);

        registration.registerBlockDataProvider(new CollectorComponentProvider(), AbsNeutronCollectorTile.class);
        registration.registerBlockDataProvider(new CompressorComponentProvider(), NeutroniumCompressorTile.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        IWailaPlugin.super.registerClient(registration);

        registration.registerComponentProvider(new CollectorComponentProvider(), TooltipPosition.BODY, AbsNeutronCollector.class);
        registration.registerComponentProvider(new CompressorComponentProvider(), TooltipPosition.BODY, NeutroniumCompressor.class);
    }
}
