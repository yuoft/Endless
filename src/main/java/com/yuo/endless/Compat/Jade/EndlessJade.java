package com.yuo.endless.Compat.Jade;

import com.yuo.endless.Blocks.AbsNeutronCollector;
import com.yuo.endless.Blocks.NeutroniumCompressor;
import com.yuo.endless.Tiles.AbsNeutronCollectorTile;
import com.yuo.endless.Tiles.NeutroniumCompressorTile;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class EndlessJade implements IWailaPlugin {
    @Override
    public void register(IRegistrar iRegistrar) {
        iRegistrar.registerComponentProvider(new CollectorComponentProvider(), TooltipPosition.BODY, AbsNeutronCollector.class);
        iRegistrar.registerBlockDataProvider(new CollectorComponentProvider(), AbsNeutronCollectorTile.class);
        iRegistrar.registerComponentProvider(new CompressorComponentProvider(), TooltipPosition.BODY, NeutroniumCompressor.class);
        iRegistrar.registerBlockDataProvider(new CompressorComponentProvider(), NeutroniumCompressorTile.class);
    }

}
