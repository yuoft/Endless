package com.yuo.endless.Items.Tool;

import com.mojang.datafixers.util.Either;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.RlUtils;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class InfinityDamageTypes {

    public static ResourceKey<DamageType> INFINITY = ResourceKey.create(Registries.DAMAGE_TYPE, RlUtils.fa("infinity"));

    public static final RegistrySetBuilder DAMAGE_BUILDER = new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, InfinityDamageTypes::bootstrap);

    public static HolderLookup.Provider append(HolderLookup.Provider original) {
        return DAMAGE_BUILDER.buildPatch(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original);
    }

    // 注册
    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(INFINITY, new DamageType(type, Float.MAX_VALUE));
    }

    private static final String type = "infinity";
    static DamageType damageType = new DamageType(type, Float.POSITIVE_INFINITY);

    public static InfinityDamageSource infinity(LivingEntity living){
        return new InfinityDamageSource(living);
    }

    public static boolean isInfinity(DamageSource source){
        if (source instanceof InfinityDamageSource) return true;
        return source.getMsgId().equals(type);
    }

    private static Direct direct(DamageType t) {
        return new Direct(t);
    }

    private static class InfinityDamageSource extends DamageSource{
        public InfinityDamageSource(LivingEntity living) {
            super(direct(damageType), living);
        }

        @Override
        public @NotNull Holder<DamageType> typeHolder() {
            return direct(damageType);
        }

        @Override
        public @NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity livingEntity) {
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
        public @NotNull String toString() {
            return "InfinityDamageSource (" + this.getEntity() + ")";
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

    public static class Direct implements Holder<DamageType> {
        @Nullable
        private DamageType value;
        public Direct(DamageType value) {
            this.value = value;
        }

        public boolean isBound() {
            return true;
        }

        public boolean is(ResourceLocation location) {
            return false;
        }

        public boolean is(ResourceKey<DamageType> key) {
            return false;
        }

        public boolean is(TagKey<DamageType> tagKey) {
            return false;
        }

        public boolean is(Predicate<ResourceKey<DamageType>> keyPredicate) {
            return false;
        }

        public Either<ResourceKey<DamageType>, DamageType> unwrap() {
            return Either.right(this.value);
        }

        public Optional<ResourceKey<DamageType>> unwrapKey() {
            return Optional.of(InfinityDamageTypes.INFINITY);
        }

        public Kind kind() {
            return Holder.Kind.DIRECT;
        }

        public String toString() {
            return "Direct{" + this.value + "}";
        }

        public boolean canSerializeIn(HolderOwner<DamageType> holderOwner) {
            return true;
        }

        public Stream<TagKey<DamageType>> tags() {
            return Stream.of();
        }

        public DamageType value() {
            return this.value;
        }
    }
}
