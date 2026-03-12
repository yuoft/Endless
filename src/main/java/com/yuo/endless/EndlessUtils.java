package com.yuo.endless;

import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.Items.Tool.InfinityDamageTypes;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;

/**
 * ResourceLocation类调用
 */
@SuppressWarnings("removal")
public class EndlessUtils {
    public static ResourceLocation fa(String path){
        return new ResourceLocation(Endless.MOD_ID, path);
    }

    public static ResourceLocation fa(String namespace, String path){
        return new ResourceLocation(namespace, path);
    }

    public static ResourceLocation tryParse(String s){
        try {
            return new ResourceLocation(s);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }

    public static ResourceLocation parse(String s){
        return new ResourceLocation(s);
    }

    /**
     * 无尽攻击
     * @param target 目标
     * @param attacker 攻击者
     */
    public static void atkInfinity(LivingEntity target, LivingEntity attacker){
        if (target instanceof EnderDragon dragon && attacker instanceof Player){
            dragon.hurt(dragon.head, InfinityDamageTypes.infinity(attacker), Float.MAX_VALUE);
        } else if (target instanceof ArmorStand){
            target.hurt(attacker.damageSources().generic(), 10);
        } else if (Endless.isDE && target instanceof DraconicGuardianEntity draconicGuardian){
            draconicGuardian.attackEntityPartFrom(draconicGuardian.dragonPartHead, InfinityDamageTypes.infinity(attacker), Float.MAX_VALUE);
            draconicGuardian.setHealth(-1);
            draconicGuardian.die(InfinityDamageTypes.infinity(attacker));
        } else {
            if (target instanceof Player player){
                if (EventHandler.isInfinite(player)){ //被攻击玩家有全套无尽 减免至10点
                    if (EventHandler.isInfinityItem(player)) //玩家在持有无尽剑或弓时 减免至4点
                        target.hurt(InfinityDamageTypes.infinity(attacker), Config.SERVER.infinityBearDamage.get());
                    else target.hurt(InfinityDamageTypes.infinity(attacker), Config.SERVER.infinityArmorBearDamage.get());
                } else target.hurt(InfinityDamageTypes.infinity(attacker),  Float.MAX_VALUE);
            } else target.hurt(InfinityDamageTypes.infinity(attacker), Float.MAX_VALUE);
        }
    }
}
