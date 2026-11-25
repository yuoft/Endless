package com.yuo.endless.mixin;

import com.yuo.endless.event.EventHandler;
import com.yuo.endless.items.EndlessItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class PlayerMixin {

    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
    private void endless$guardSetHealth(float health, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;

        // 只关心玩家
        if (!(self instanceof Player player)) return;

        // 1) 玩家还没完成初始化 —— 直接放行，否则 inventory 会 NPE
        if (player.getInventory() == null || player.level() == null) return;

        // 2) 允许"回血"方向：heal 内部会调 setHealth(更高值)
        //    只有降低血量才需要拦
        if (health >= self.getHealth()) return;

        if (player.getMainHandItem().getItem() == EndlessItems.infinitySword.get()) return;

        // 3) 允许我们自己的内部逻辑放行（图腾治疗、锁血回滚等）
//        if (HealthGuard.isInternal()) return;

        // 4) 现在是真正要拦的情况：无限玩家被外部扣血
        if (EventHandler.isInfinite(player)) {
            ci.cancel();
        }
    }
}
