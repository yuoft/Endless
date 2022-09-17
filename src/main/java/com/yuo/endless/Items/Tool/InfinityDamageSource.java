package com.yuo.endless.Items.Tool;

import com.yuo.endless.Items.ItemRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.openzen.zenscript.codemodel.expression.ThisExpression;

public class InfinityDamageSource extends EntityDamageSource {

    private static final String type = "infinity";

    public InfinityDamageSource(LivingEntity living) {
        super(type, living);
        setDamageBypassesArmor(); //不受盔甲护甲影响
        setDamageAllowedInCreativeMode(); //对创造模式造成伤害
        setDamageIsAbsolute(); //不受附魔，药水效果影响
    }

    @Override
    public ITextComponent getDeathMessage(LivingEntity entityLivingBaseIn) {
        ItemStack itemstack = ItemStack.EMPTY;
        if (damageSourceEntity instanceof LivingEntity){
            LivingEntity living = (LivingEntity) damageSourceEntity;
            itemstack = getInfinityWeapon(living);
        }
        String s = "death.attack.infinity";
        String s0 = "death.attack.infinity_weapon";
        int rand = entityLivingBaseIn.getEntityWorld().rand.nextInt(5);
        if (!itemstack.isEmpty() && damageSourceEntity != null)
            return new TranslationTextComponent(s0 + "." + rand,
                    entityLivingBaseIn.getDisplayName(), damageSourceEntity.getName(), itemstack.getDisplayName());

        return new TranslationTextComponent(s + "." + rand, entityLivingBaseIn.getDisplayName());
    }

    //是否根据难度缩放伤害值
    @Override
    public boolean isDifficultyScaled() {
        return false;
    }

    @Override
    public String toString() {
        return "InfinityDamageSource (" + this.damageSourceEntity + ")";
    }

    public static boolean isInfinity(DamageSource source){
        if (source instanceof InfinityDamageSource) return true;
        return source.damageType.equals(type);
    }

    /**
     * 获取击杀者使用的无尽武器
     * @param living 击杀者
     * @return 无尽武器/空
     */
    public ItemStack getInfinityWeapon(LivingEntity living){
        ItemStack mainItem = living.getHeldItem(Hand.MAIN_HAND);
        ItemStack offItem = living.getHeldItem(Hand.OFF_HAND);
        return isInfinityWeapon(mainItem) ? mainItem : isInfinityWeapon(offItem) ? offItem : ItemStack.EMPTY;
    }

    /**
     * 判断物品是否是无尽武器
     * @param stack 要判断的物品
     * @return 是 true
     */
    public boolean isInfinityWeapon(ItemStack stack){
        Item item = stack.getItem();
        return item == ItemRegistry.infinitySword.get() || item == ItemRegistry.infinityBow.get() || item == ItemRegistry.infinityCrossBow.get();
    }
}
