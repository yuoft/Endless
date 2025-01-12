package com.yuo.endless.Items.Tool;

import com.brandon3055.draconicevolution.entity.GuardianCrystalEntity;
import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity;
import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianPartEntity;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.EndlessItemEntity;
import com.yuo.endless.Event.EventHandler;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

import java.util.List;
import java.util.function.Consumer;

public class InfinitySword extends SwordItem {

    public InfinitySword() {
        super(EndlessItemTiers.INFINITY_SWORD, 0, -2.4f, new Properties().tab(EndlessTab.endless).fireResistant());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = getDefaultAttributeModifiers(slot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        if (slot == EquipmentSlot.MAINHAND){
            builder.put(ForgeMod.REACH_DISTANCE.get(), Modifiers.getModifierHandRang(0,5.0d));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    /**
     * 清除玩家debuff
     * @param player 玩家
     */
    public static void clearBuff(Player player) {
        MobEffectInstance effect = player.getEffect(MobEffects.DIG_SPEED);
        if (effect != null){
            int amplifier = effect.getAmplifier();
            if (amplifier >= 0){
                player.removeEffect(MobEffects.DIG_SPEED);
            }
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(tab)){
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putBoolean("Unbreakable",true);
            stacks.add(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> components, TooltipFlag pIsAdvanced) {
        components.add(new TextComponent(ColorText.makeFabulous(I18n.get("endless.text.itemInfo.infinity")) + I18n.get("attribute.name.generic.attack_damage")));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof LivingEntity living){
            hurtEnemy(stack, living, player);

            //模拟原版剑攻击
            float attackStrength = player.getAttackStrengthScale(1.6f); //攻击强度 0.5F ??
            boolean isCritical = attackStrength > 0.9f && player.fallDistance > 0.f && !player.isOnGround() && !player.onClimbable()
                    && !player.isInWater() && !player.isPassenger() && entity instanceof LivingEntity;
            CriticalHitEvent criticalHit = ForgeHooks.getCriticalHit(player, living, isCritical, isCritical ? 1.5f : 1.0f);
            isCritical = criticalHit != null;
            if (attackStrength > 0.9f && player.isOnGround() && !player.isSprinting()) { //攻击强度大于90%，且在地面,未疾跑
                sweepAttack(living, player);
            } else if (isCritical){
                //攻击强度大于90%，正在下落，未在地面, 未攀爬，不在水中，未骑乘实体，攻击目标是生物
                criticalAttack(player, living);
            }else if (player.isSprinting())
                knockAttack(player, living, stack);
        }
        damageGuardian(entity, player);
        return true;
    }

    /**
     * 击退
     * @param player 玩家
     * @param living 被攻击生物
     * @param stack 玩家使用武器
     */
    private void knockAttack(Player player, LivingEntity living, ItemStack stack){
        AttributeInstance knockBack = player.getAttribute(Attributes.ATTACK_KNOCKBACK); //击退
        if (knockBack != null){
            int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.KNOCKBACK, stack); //击退附魔
            if (player.isSprinting()) level++;
            player.level.playSound(player, player.getOnPos(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.PLAYERS, 1.0f, 1.0f);
            living.knockback((float)knockBack.getValue() + level, -player.getLookAngle().x, -player.getLookAngle().z); //击退
        }
    }

    /**
     * 暴击
     * @param player 玩家
     * @param living 玩家攻击生物
     */
    private void criticalAttack(Player player, LivingEntity living){
        MobEffectInstance blindness = player.getEffect(MobEffects.BLINDNESS); //失明
        MobEffectInstance slowFalling = player.getEffect(MobEffects.SLOW_FALLING); //缓降
        Level world = player.level;
        if (blindness == null && slowFalling == null){ //没有此2个效果
            world.playSound(player, player.getOnPos(), SoundEvents.PLAYER_ATTACK_CRIT, player.getSoundSource(), 1.0f, 1.0f);
            float health = living.getHealth() > 300 ? 300 : living.getHealth();
            int particleCount = (int) (health > 2.0 ? health * 0.5 : health);
            if (world instanceof ServerLevel serverWorld){ //暴击粒子
                serverWorld.sendParticles(ParticleTypes.CRIT, living.getX(), living.getY(0.5), living.getZ(), particleCount, 0.1D, 0.0D, 0.1D, 0.2D);
            }
        }
    }

    /**
     * 横扫攻击
     * @param targetEntity 被攻击生物
     * @param player 玩家
     */
    private void sweepAttack(LivingEntity targetEntity, Player player){
        Level world = player.level;

        for(LivingEntity livingentity : world.getEntitiesOfClass(LivingEntity.class, player.getItemInHand(InteractionHand.MAIN_HAND).getSweepHitBox(player, targetEntity))) {
            if (livingentity != player && livingentity != targetEntity && !player.isAlliedTo(livingentity) &&
                    (!(livingentity instanceof ArmorStand) || !((ArmorStand)livingentity).isMarker()) &&
                    player.distanceTo(livingentity) < 9.0D) {
                livingentity.knockback(0.4F, Math.sin(player.yRotO * ((float)Math.PI / 180F)), -Math.cos(player.yRotO * ((float)Math.PI / 180F)));
                livingentity.hurt(new InfinityDamageSource(player), Float.MAX_VALUE);
            }
        }
        //横扫音效
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
        player.sweepAttack(); //生成横扫粒子
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        if (toolAction == ToolActions.SWORD_SWEEP) return true;
        return super.canPerformAction(stack, toolAction);
    }

    //攻击实体
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int fireAspect = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
        if (fireAspect > 0){
            target.setSecondsOnFire(fireAspect * 4);
        }
        if (target instanceof EnderDragon dragon && attacker instanceof Player){ //攻击末影龙
            dragon.hurt(dragon.head, new InfinityDamageSource(attacker), Float.MAX_VALUE);
        }else if (target instanceof WitherBoss wither){
            wither.setInvulnerableTicks(0);
            wither.hurt(new InfinityDamageSource(attacker), Float.MAX_VALUE);
        } else if (target instanceof ArmorStand){
            target.hurt(DamageSource.GENERIC, 10);
            return true;
        }else {
            if (target instanceof Player player){
                if (EventHandler.isInfinite(player)){ //被攻击玩家有全套无尽 减免至10点
                    if (EventHandler.isInfinityItem(player)) //玩家在持有无尽剑或弓时 减免至4点
                        target.hurt(new InfinityDamageSource(attacker), Config.SERVER.infinityBearDamage.get());
                    else target.hurt(new InfinityDamageSource(attacker), Config.SERVER.infinityArmorBearDamage.get());
                } else target.hurt(new InfinityDamageSource(attacker),  Float.MAX_VALUE);
            } else target.hurt(new InfinityDamageSource(attacker), Float.MAX_VALUE);
        }
        if (target instanceof Player player){
            if (EventHandler.isInfinite(player)){ //玩家穿戴全套无尽 则不执行死亡
                return true;
            }
        }

        spawnHealthParticle(target);

        if (target.isAlive() || target.getHealth() > 0){
            target.setHealth(-1);
            if (!target.level.isClientSide)
                target.die(new InfinityDamageSource(attacker));
            if (Config.SERVER.swordKill.get()){
                target.kill();
                target.deathTime = 20;
                target.remove(RemovalReason.KILLED);
            }
        }
        return true;
    }

    /**
     * 生成被攻击粒子  根据血量生成相应数量粒子
     * @param target 被攻击实体
     */
    private static void spawnHealthParticle(Entity target){
        if (target instanceof LivingEntity living){
            float maxHealth = living.getMaxHealth();
            if (target.level instanceof ServerLevel) {
                int k = (int)((double)maxHealth * 0.5);
                ((ServerLevel)target.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getY(0.5), target.getZ(), k, 0.1, 0.0, 0.1, 0.2);
            }
        }
    }

    //范围伤害
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (!level.isClientSide) {
            attackAOE(player, Config.SERVER.swordAttackRange.get(), Config.SERVER.swordRangeDamage.get(), player.isCrouching() && Config.SERVER.isSwordAttackAnimal.get());
            player.swing(hand);
            player.getCooldowns().addCooldown(heldItem.getItem(), 20);
        }
        level.playSound(player, player.getOnPos(), SoundEvents.PLAYER_LEVELUP , SoundSource.PLAYERS, 1.0f, 5.0f);
        return InteractionResultHolder.success(heldItem);
    }

    //aoe伤害
    private void attackAOE(Player player,float range, float damage,boolean type) {
        if (player.level.isClientSide) return;
        AABB aabb = player.getBoundingBox().deflate(range);//范围
        List<Entity> toAttack = player.level.getEntities(player, aabb);//生物列表
        DamageSource src = new InfinityDamageSource(player);//伤害类型
        for (Entity entity : toAttack) { //循环遍历
            if (entity instanceof LivingEntity){
                if(type) { //潜行攻击所有生物
                    attackEntity(entity, src, damage);
                } else {
                    if (entity instanceof Mob) {
                        attackEntity(entity, src, damage);
                    }
                }
            }
        }
    }

    /**
     * 攻击原版生物
     * @param entity 生物
     * @param src 伤害类型
     * @param damage 伤害值
     */
    public static void attackEntity(Entity entity, DamageSource src, float damage){
        if (entity instanceof EnderDragon dragon){
            dragon.hurt(dragon.head, src, damage);
        }else if (entity instanceof WitherBoss wither){
            wither.setInvulnerableTicks(0); //将凋零无敌时间设为0
            wither.hurt(src, damage);
        }else entity.hurt(src, damage);//给与实体伤害
    }

    /**
     * 攻击龙研中的实体 混沌水晶
     * @param entity 实体
     * @param player 玩家
     */
    public static void damageGuardian(Entity entity, Player player){
        if (Endless.isDE && Config.SERVER.isBreakDECrystal.get()){
            if (entity instanceof DraconicGuardianEntity draconicGuardian){
                draconicGuardian.attackEntityPartFrom(draconicGuardian.getDragonParts()[2], new InfinityDamageSource(player), Float.MAX_VALUE);
                draconicGuardian.setHealth(-1);
                draconicGuardian.die(new InfinityDamageSource(player));
            }else if (entity instanceof GuardianCrystalEntity crystal){
                crystal.kill();
            }else if (entity instanceof DraconicGuardianPartEntity draconicGuardian) {
                DraconicGuardianEntity dragon = draconicGuardian.dragon;
                    dragon.hurt(DamageSource.thorns(player), Float.MAX_VALUE);
                    dragon.attackEntityPartFrom(dragon.dragonPartHead, new InfinityDamageSource(player), Float.MAX_VALUE);
                    GuardianCrystalEntity crystal = dragon.closestGuardianCrystal;
                    if (crystal != null) {
                        crystal.kill();
                    }
                    if (dragon.isAlive() || dragon.getHealth() > 0) {
                        dragon.setHealth(-1);
                        if (!player.level.isClientSide) {
                            dragon.die(new InfinityDamageSource(player));
                        }
                    }
                    dragon.kill();
                }
        }
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 0;
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        stack.getOrCreateTag().putInt("Damage", 0);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int pSlotId, boolean pIsSelected) {
        int damage = stack.getDamageValue();
        if (damage > 0){
            stack.getOrCreateTag().putInt("Damage", 0);
        }
    }

    //创建一个能被快速捡起的物品实体
    @org.jetbrains.annotations.Nullable
    @Override
    public Entity createEntity(Level level, Entity location, ItemStack stack) {
        return new EndlessItemEntity(level, location, stack);
    }

    //允许创建自定义物品实体
    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }
}
