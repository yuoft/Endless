package com.yuo.endless.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.endless.compat.oculus.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

/**
 * @author xiaoyang
 */
@Mixin(value = GameRenderer.class, priority = 500)
public abstract class OculusAfterLevelMixin {

    @Shadow @Final private Map<String, ShaderInstance> shaders;

    @Unique
    private static void endless_20$finishAllFrames() {
        List<Runnable> cleanupTasks = List.of(
                CosmicArmorLateRenderQueue::endFrame,
                CosmicItemLateRenderQueue::endFrame,
                CosmicBlockLateRenderQueue::endFrame,
                GapingVoidLateRenderQueue::endFrame,
                MobLateRenderQueue::endFrame,
                BowCircleLateRenderQueue::endFrame,
                RenderFrameState::endFrame
        );

        for (Runnable task : cleanupTasks) {
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void beginWorldRender(float partialTick, long finishTimeNano, PoseStack poseStack, CallbackInfo ci) {
        RenderFrameState.Snapshot snap = RenderFrameState.beginFrame();
        CosmicArmorLateRenderQueue.beginFrame(snap);
        CosmicItemLateRenderQueue.beginFrame(snap);
        CosmicBlockLateRenderQueue.beginFrame(snap);
        GapingVoidLateRenderQueue.beginFrame(snap);
        MobLateRenderQueue.beginFrame(snap);
        BowCircleLateRenderQueue.beginFrame(snap);
    }

    @Inject(method = "renderLevel", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/GameRenderer;renderHand:Z", ordinal = 0))
    private void renderLateWorldPasses(float partialTick, long finishTimeNano, PoseStack poseStack, CallbackInfo ci) {
        if (!RenderFrameState.current().shaderPackActive()) {
            return;
        }
        CosmicArmorLateRenderQueue.renderAfterLevel();
        CosmicItemLateRenderQueue.renderAfterLevel();
        CosmicBlockLateRenderQueue.renderAfterLevel();
        GapingVoidLateRenderQueue.renderAfterLevel();
        MobLateRenderQueue.renderAfterLevel();
        BowCircleLateRenderQueue.renderAfterLevel();
    }

    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void endWorldRender(float partialTick, long finishTimeNano, PoseStack poseStack, CallbackInfo ci) {
        try {
            // 根据活动管道的不同，自定义渲染器可能会在世界传递后运行。
            if (RenderFrameState.current().shaderPackActive()) {
                CosmicArmorLateRenderQueue.renderAfterLevel();
                CosmicItemLateRenderQueue.renderAfterLevel();
                CosmicBlockLateRenderQueue.renderAfterLevel();
                GapingVoidLateRenderQueue.renderAfterLevel();
                MobLateRenderQueue.renderAfterLevel();
                BowCircleLateRenderQueue.renderAfterLevel();
            }
        } finally {
            endless_20$finishAllFrames();
        }
    }
}
