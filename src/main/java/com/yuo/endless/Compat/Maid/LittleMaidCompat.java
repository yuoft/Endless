package com.yuo.endless.Compat.Maid;

import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.overlay.MaidTipsOverlay;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.inventory.chest.ChestManager;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.Blocks.EndlessChestType;
import com.yuo.endless.Client.AvaritiaShaders;
import com.yuo.endless.Client.Model.InfinityArmorModel;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public void addChestType(ChestManager manager) {
        manager.add(EndlessChestType.COMPRESSOR);
        manager.add(EndlessChestType.INFINITY);
    }

    @Override
    public void addAdditionMaidLayer(EntityMaidRenderer renderer, Context context) {
        ILittleMaid.super.addAdditionMaidLayer(renderer, context);
        EntityModelSet modelSet = context.getModelSet();
        ModelManager modelManager = context.getModelManager();
        renderer.addLayer(new MaidLayer(renderer, context));
    }

    @Override
    public void addAdditionGeckoMaidLayer(GeckoEntityMaidRenderer<? extends Mob> renderer, Context context) {
        ILittleMaid.super.addAdditionGeckoMaidLayer(renderer, context);
        renderer.addGeoLayerRenderer(new MaidGeoLayer<>(renderer));
    }

    public static class MaidRender extends RenderLayer<EntityMaid, BedrockModel<EntityMaid>> {
        public MaidRender(LivingEntityRenderer<EntityMaid, BedrockModel<EntityMaid>> renderer) {
            super(renderer);
        }

//        public Iterable<ModelPart> playerParts() {
//            return ImmutableList.of(this.getParentModel().getHead(), this.getParentModel().getLeftArm(), this.getParentModel().getRightArm());
//        }

        public void render(@NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, @NotNull EntityMaid l, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            if (EventHandler.isInfinite(l)) {
                AvaritiaShaders.cosmicOpacity.set(2.0F);
                this.getParentModel().getModelMap().forEach((s, t) -> t.render(pPoseStack, InfinityArmorModel.material(InfinityArmorModel.MASK_INV).buffer(pBuffer, InfinityArmorModel::mask2), pPackedLight, 1, 1.0F, 1.0F, 1.0F, 1.0F));
            }

        }
    }
}
