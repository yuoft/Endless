package com.yuo.endless.Client.Lib;

import net.minecraftforge.client.model.pipeline.IVertexConsumer;

public interface ISmartVertexConsumer extends IVertexConsumer {
    void put(Quad paramQuad);
}
