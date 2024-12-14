package com.yuo.endless.Items.Tool;

import com.yuo.endless.Items.EndlessItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class InfinityDamageSource extends EntityDamageSource {

    private static final String type = "infinity";

    public InfinityDamageSource(LivingEntity living) {
        super(type, living);
        bypassArmor();//不受盔甲护甲影响
        bypassInvul();//对创造模式造成伤害
        bypassMagic();//不受附魔，药水效果影响
        setIsFall();
        setMagic();
        setProjectile();
        setExplosion();
        setThorns();
        setNoAggro();
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity livingEntity) {
        ItemStack itemstack = ItemStack.EMPTY;
        if (entity instanceof LivingEntity living){
            itemstack = getInfinityWeapon(living);
        }
        String s = "death.attack.infinity";
        String s0 = "death.attack.infinity_weapon";
        Random rand = livingEntity.level.random;
        Component displayName = livingEntity.getDisplayName();
        Component name = entity.getName();
        if (!itemstack.isEmpty()){//有击杀者和武器
            if (itemstack.getItem() instanceof InfinitySword){ //剑0-4
                return new TranslatableComponent(s0 + "." + rand.nextInt(5),
                        displayName, name, itemstack.getDisplayName());
            } //弓弩1-5
            else {
                int i = rand.nextInt(5) + 1;
                return new TranslatableComponent(s0 + "." + i,
                        displayName, name, itemstack.getDisplayName());
            }
        }
        else return new TranslatableComponent(s + "." + rand.nextInt(4), displayName, name);
    }

    //是否根据难度缩放伤害值
    @Override
    public boolean scalesWithDifficulty() {
        return false;
    }

    @Override
    public String toString() {
        return "InfinityDamageSource (" + this.entity + ")";
    }

    public static boolean isInfinity(DamageSource source){
        if (source instanceof InfinityDamageSource) return true;
        return source.getMsgId().equals(type);
    }

    /**
     * 获取击杀者使用的无尽武器
     * @param living 击杀者
     * @return 无尽武器/空
     */
    public ItemStack getInfinityWeapon(LivingEntity living){
        ItemStack mainItem = living.getMainHandItem();
        ItemStack offItem = living.getOffhandItem();
        return isInfinityWeapon(mainItem) ? mainItem : isInfinityWeapon(offItem) ? offItem : ItemStack.EMPTY;
    }

    /**
     * 判断物品是否是无尽武器
     * @param stack 要判断的物品
     * @return 是 true
     */
    public boolean isInfinityWeapon(ItemStack stack){
        Item item = stack.getItem();
        return item == EndlessItems.infinitySword.get() || item == EndlessItems.infinityBow.get() || item == EndlessItems.infinityCrossBow.get();
    }
}
