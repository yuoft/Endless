package com.yuo.endless.Items.Tool;

import com.mojang.datafixers.util.Either;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class InfinityDamageSource extends DamageSource {

    private static final String type = "infinity";

    public InfinityDamageSource(LivingEntity living) {
        super(new Holder<DamageType>() {
            @Override
            public DamageType value() {
                return new DamageType(type, Float.POSITIVE_INFINITY);
            }

            @Override
            public boolean isBound() {
                return false;
            }

            @Override
            public boolean is(ResourceLocation resourceLocation) {
                return false;
            }

            @Override
            public boolean is(ResourceKey<DamageType> resourceKey) {
                return false;
            }

            @Override
            public boolean is(Predicate<ResourceKey<DamageType>> predicate) {
                return false;
            }

            @Override
            public boolean is(TagKey<DamageType> tagKey) {
                return false;
            }

            @Override
            public Stream<TagKey<DamageType>> tags() {
                return Stream.empty();
            }

            @Override
            public Either<ResourceKey<DamageType>, DamageType> unwrap() {
                return null;
            }

            @Override
            public Optional<ResourceKey<DamageType>> unwrapKey() {
                return Optional.empty();
            }

            @Override
            public Kind kind() {
                return null;
            }

            @Override
            public boolean canSerializeIn(HolderOwner<DamageType> holderOwner) {
                return false;
            }
        }, living);
//        bypassArmor();//不受盔甲护甲影响
//        bypassInvul();//对创造模式造成伤害
//        bypassMagic();//不受附魔，药水效果影响
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity livingEntity) {
        ItemStack itemstack = ItemStack.EMPTY;
        if (getEntity() instanceof LivingEntity living){
            itemstack = getInfinityWeapon(living);
        }
        String s = "death.attack.infinity";
        String s0 = "death.attack.infinity_weapon";
        RandomSource rand = livingEntity.getRandom();
        Component displayName = livingEntity.getDisplayName();
        Component name = getEntity().getName();
        if (!itemstack.isEmpty()){//有击杀者和武器
            if (itemstack.getItem() instanceof InfinitySword){ //剑0-4
                return Component.translatable(s0 + "." + rand.nextInt(5),
                        displayName, name, itemstack.getDisplayName());
            } //弓弩1-5
            else {
                int i = rand.nextInt(5) + 1;
                return Component.translatable(s0 + "." + i,
                        displayName, name, itemstack.getDisplayName());
            }
        }
        else return Component.translatable(s + "." + rand.nextInt(4), displayName, name);
    }

    //是否根据难度缩放伤害值
    @Override
    public boolean scalesWithDifficulty() {
        return false;
    }

    @Override
    public String toString() {
        return "InfinityDamageSource (" + this.getEntity() + ")";
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
