package com.yuo.endless.Compat.Maid;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDeathEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.Tool.InfinityDamageTypes;
import com.yuo.endless.Items.Tool.InfinitySword;
import com.yuo.endless.NetWork.NetWorkHandler;
import com.yuo.endless.NetWork.TotemPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class InfinityTotemBauble implements IMaidBauble {
    // 在构造方法内手动注册，这样只有在女仆模组加载后事件才会生效。
    public InfinityTotemBauble() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    //女仆使用无尽图腾 全套无尽
    @SubscribeEvent
    public void onDeath(MaidDeathEvent event){
        EntityMaid maid = event.getMaid();
        if (EventHandler.isInfinite(maid) && !InfinityDamageTypes.isInfinity(event.getSource())) {
            maid.setHealth(maid.getMaxHealth());
            event.setCanceled(true);
        }
        int totemSlot = ItemsUtil.getBaubleSlotInMaid(maid, this);
        if (totemSlot >= 0){
            ItemStack totem = maid.getMaidBauble().getStackInSlot(totemSlot);
            maid.level().broadcastEntityEvent(maid, (byte)35);
            LivingEntity var5 = maid.getOwner();
            if (var5 instanceof ServerPlayer serverPlayer) {
                InitTrigger.MAID_EVENT.trigger(serverPlayer, "use_undead_bauble");
            }
            maid.removeAllEffects();
            int damage = totem.getDamageValue();
            if (damage == 9){ //最后一次
                maid.setHealth(maid.getMaxHealth());
                maid.addEffect(new MobEffectInstance(MobEffects.JUMP, 800, 1));
                maid.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 800, 1));
                LivingEntity living = maid.getOwner();
                if (living instanceof Player player){
                    InfinitySword.attackAOE(player, 8, 1000.0f, false);
                }
                maid.sendSystemMessage(Component.translatable("endless.text.msg.totem_break"));
            }else {
                maid.setHealth(10.0F);
            }
            maid.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 2600, 4));
            maid.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 1));
            maid.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 700, 2));
            maid.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1100, 0));
            totem.hurtAndBreak(1, maid, e -> e.broadcastBreakEvent(InteractionHand.MAIN_HAND));
            event.setCanceled(true);
        }
    }
}
