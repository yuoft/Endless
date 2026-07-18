package com.yuo.endless.compat.maid;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.MaidTipsOverlay;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import com.yuo.endless.items.EndlessItems;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

@LittleMaidExtension
public class LittleMaidCompat implements ILittleMaid {

    public LittleMaidCompat() {
        MinecraftForge.EVENT_BUS.register(new MaidEvents());
    }

    // 注册女仆工作任务的方法
    @Override
    public void addMaidTask(TaskManager manager) {
        // 添加自定义任务
        manager.add(new InfinitySwordTask());
        manager.add(new InfinityBowTask());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addMaidTips(MaidTipsOverlay maidTipsOverlay) {
        // 第一个参数是语言文件的 key，第二个参数是物品
        maidTipsOverlay.addTips("overlay.endless.cosmic_meat_balls.tips", EndlessItems.cosmicMeatBalls.get());
        maidTipsOverlay.addTips("overlay.endless.ultimate_stew.tips", EndlessItems.ultimateStew.get());
    }

    // 绑定女仆饰品，将自定义饰品与物品关联
    @Override
    public void bindMaidBauble(BaubleManager manager) {
        // 将无尽图腾e与自定义饰品绑定
        manager.bind(EndlessItems.infinityTotem.get(), new InfinityTotemBauble());
    }

//    @Override
//    public void addChestType(ChestManager manager) {
//        manager.add(EndlessChestType.COMPRESSOR);
//        manager.add(EndlessChestType.INFINITY);
//    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addAdditionMaidLayer(EntityMaidRenderer renderer, Context context) {
        ILittleMaid.super.addAdditionMaidLayer(renderer, context);
        renderer.addLayer(new MaidLayer(renderer));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addAdditionGeckoMaidLayer(GeckoEntityMaidRenderer<? extends Mob> renderer, Context context) {
        ILittleMaid.super.addAdditionGeckoMaidLayer(renderer, context);
        renderer.addGeoLayerRenderer(new MaidGeoLayer<>(renderer));
    }

}
