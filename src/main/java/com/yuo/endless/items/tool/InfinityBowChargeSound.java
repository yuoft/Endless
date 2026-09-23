package com.yuo.endless.items.tool;

import com.yuo.endless.client.sound.EndlessSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class InfinityBowChargeSound extends AbstractTickableSoundInstance {
    private final Player player;

    public InfinityBowChargeSound(Player player) {
        super(EndlessSounds.INFINITY_BOW_STAR.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.looping = false;      // 按音效文件长度决定，若音效本身是长音就不用循环
        this.delay = 0;
        this.volume = 6.0f;
        this.pitch = 1.0f;
        this.attenuation = Attenuation.NONE;  // 全图能听到（你原来音量 6.0f 应该是想这样）
        this.relative = false;
        updatePosition();
    }

    private void updatePosition() {
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    @Override
    public void tick() {
        // 玩家死亡/移除/停止使用则自动停止
        if (player.isRemoved() || player.isDeadOrDying() || !player.isUsingItem()) {
            stop();
            return;
        }
        updatePosition();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }
}