package com.yuo.endless.Proxy;

import net.minecraftforge.eventbus.api.IEventBus;

public class CommonProxy implements IProxy {
    @Override
    public void registerHandlers(IEventBus modBus) {
        IProxy.super.registerHandlers(modBus);
    }
}
