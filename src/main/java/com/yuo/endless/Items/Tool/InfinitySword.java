package com.yuo.endless.Items.Tool;

import com.brandon3055.draconicevolution.entity.GuardianCrystalEntity;
import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class InfinitySword extends SwordItem{

    public InfinitySword() {
        super(MyItemTier.INFINITY_SWORD, 0, -2.4f, new Properties().group(ModGroup.endless).isImmuneToFire());
    }

    /**
     * 清除玩家debuff
     * @param player 玩家
     */
    public static void clearBuff(PlayerEntity player) {
        EffectInstance effect = player.getActivePotionEffect(Effects.MINING_FATIGUE);
        if (effect != null){
            int amplifier = effect.getAmplifier();
            if (amplifier >= 0){
                player.removePotionEffect(Effects.MINING_FATIGUE);
            }
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)){ //防止添加到其它物品页
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putBoolean("Unbreakable",true);
            items.add(stack);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World p_77624_2_, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new StringTextComponent(ColorText.makeFabulous(I18n.format("endless.text.itemInfo.infinity")) + I18n.format("attribute.name.generic.attack_damage")));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
//        if (player.world.isRemote) return false;
        if (entity instanceof LivingEntity){
            LivingEntity living = (LivingEntity) entity;
            boolean b = hitEntity(stack, living, player);
            float attackStrength = player.getCooledAttackStrength(1.6f); //攻击强度
            float walkSpeed = player.abilities.getWalkSpeed();
            boolean isCritical = attackStrength > 0.848f && walkSpeed <= 0.1f && !player.isOnGround() && player.fallDistance > 0.f
                    && !player.isOnLadder() && !player.isInWater() && !player.isPassenger();
            CriticalHitEvent criticalHit = ForgeHooks.getCriticalHit(player, living, isCritical, isCritical ? 1.5f : 1.0f);
            isCritical = criticalHit != null;
            if (attackStrength > 0.84f && player.isOnGround() && !player.isSprinting()) { //攻击强度大于84%，且在地面,未疾跑
                sweepAttack(living, player);
            } else if (isCritical){
                //攻击强度大于84.8%，正在下落，未在地面, 正常行走速度,未骑乘实体，不在水中
                criticalAttack(player, living);
            }else if (player.isSprinting())
                knockAttack(player, living, stack);
        }
        damageGuardian(entity, player);
        return false;
    }

    /**
     * 击退
     * @param player 玩家
     * @param living 被攻击生物
     * @param stack 玩家使用武器
     */
    private void knockAttack(PlayerEntity player, LivingEntity living, ItemStack stack){
        ModifiableAttributeInstance knockback = player.getAttribute(Attributes.ATTACK_KNOCKBACK); //击退
        if (knockback != null){
            int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, stack); //击退附魔
            if (player.isSprinting()) level++;
            player.world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1.0f, 1.0f);
            living.applyKnockback((float)knockback.getValue() + level, -player.getLookVec().x, -player.getLookVec().z); //击退
        }
    }

    /**
     * 暴击
     * @param player 玩家
     * @param living 玩家攻击生物
     */
    private void criticalAttack(PlayerEntity player, LivingEntity living){
        EffectInstance blindness = player.getActivePotionEffect(Effects.BLINDNESS); //失明
        EffectInstance slowFalling = player.getActivePotionEffect(Effects.SLOW_FALLING); //缓降
        World world = player.world;
        if (blindness == null && slowFalling == null){ //没有此2个效果
            world.playSound(player, player.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0f, 1.0f);
            float health = living.getHealth() > 300 ? 300 : living.getHealth();
            int particleCount = (int) (health > 2.0 ? health * 0.5 : health);
            if (world instanceof ServerWorld){ //暴击粒子
                ServerWorld serverWorld = (ServerWorld) world;
                serverWorld.spawnParticle(ParticleTypes.CRIT, living.getPosX(), living.getPosYHeight(0.5), living.getPosZ(), particleCount, 0.1D, 0.0D, 0.1D, 0.2D);
            }
        }
    }

    /**
     * 横扫攻击
     * @param targetEntity 被攻击生物
     * @param player 玩家
     */
    private void sweepAttack(LivingEntity targetEntity, PlayerEntity player){
        World world = player.world;

        for(LivingEntity livingentity : world.getEntitiesWithinAABB(LivingEntity.class, targetEntity.getBoundingBox().grow(1.0D, 0.25D, 1.0D))) {
            if (livingentity != player && livingentity != targetEntity && !player.isOnSameTeam(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity)livingentity).hasMarker()) && player.getDistanceSq(livingentity) < 9.0D) {
                livingentity.applyKnockback(0.4F, MathHelper.sin(player.rotationYaw * ((float)Math.PI / 180F)), -MathHelper.cos(player.rotationYaw * ((float)Math.PI / 180F)));
                livingentity.attackEntityFrom(DamageSource.causePlayerDamage(player), Float.MAX_VALUE);
            }
        }
        //横扫音效
        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
        player.spawnSweepParticles(); //生成横扫粒子
    }

    //攻击实体
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
        if (fireAspect > 0){
            target.setFire(fireAspect * 4);
        }
        if (target instanceof EnderDragonEntity && attacker instanceof PlayerEntity){
            EnderDragonEntity dragon = (EnderDragonEntity) target; //攻击末影龙
            dragon.attackEntityPartFrom(dragon.dragonPartHead, new InfinityDamageSource(attacker), Float.POSITIVE_INFINITY);
        }else if (target instanceof WitherEntity){
            WitherEntity wither = (WitherEntity) target;
            wither.setInvulTime(0);
            wither.attackEntityFrom(new InfinityDamageSource(attacker), Float.POSITIVE_INFINITY);
        } else if (target instanceof ArmorStandEntity){
            target.attackEntityFrom(DamageSource.GENERIC, 10);
            return true;
        }else {
            if (target instanceof PlayerEntity){
              PlayerEntity player = (PlayerEntity) target;
              if (EventHandler.isInfinite(player)){ //被攻击玩家有全套无尽 减免至10点
                  if (EventHandler.isInfinityItem(player)) //玩家在持有无尽剑或弓时 减免至4点
                    target.attackEntityFrom(new InfinityDamageSource(attacker), Config.SERVER.infinityBearDamage.get());
                  else target.attackEntityFrom(new InfinityDamageSource(attacker), Config.SERVER.infinityArmorBearDamage.get());
              } else target.attackEntityFrom(new InfinityDamageSource(attacker),  Float.POSITIVE_INFINITY);
            } else target.attackEntityFrom(new InfinityDamageSource(attacker), Float.POSITIVE_INFINITY);
        }
        if (target instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) target;
            if (EventHandler.isInfinite(player)){ //玩家穿戴全套无尽 则不执行死亡
                return true;
            }
        }
        target.setHealth(-1);
//        target.deathTime = 20;
        return true;
    }

    //范围伤害
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            attackAOE(playerIn, Config.SERVER.swordAttackRange.get(), Config.SERVER.swordRangeDamage.get(), playerIn.isSneaking() && Config.SERVER.isSwordAttackAnimal.get());
            playerIn.swingArm(handIn);
            playerIn.getCooldownTracker().setCooldown(heldItem.getItem(), 20);
        }
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP , SoundCategory.PLAYERS, 1.0f, 5.0f);
        return ActionResult.resultSuccess(heldItem);
    }

    //aoe伤害
    protected void attackAOE(PlayerEntity player,float range, float damage,boolean type) {
        if (player.world.isRemote) return;
        AxisAlignedBB aabb = player.getBoundingBox().grow(range);//范围
        List<Entity> toAttack = player.getEntityWorld().getEntitiesWithinAABBExcludingEntity(player, aabb);//生物列表
        DamageSource src = new InfinityDamageSource(player);//伤害类型
        for (Entity entity : toAttack) { //循环遍历
            if (entity instanceof LivingEntity){
                if(type) { //潜行攻击所有生物
                    attackEntity(entity, src, damage);
                } else {
                    if (entity instanceof IMob) {
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
    private void attackEntity(Entity entity, DamageSource src, float damage){
        if (entity instanceof EnderDragonEntity){
            EnderDragonEntity dragon = (EnderDragonEntity) entity;
            dragon.attackEntityPartFrom(dragon.dragonPartHead, src, damage);
        }else if (entity instanceof WitherEntity){
            WitherEntity wither = (WitherEntity) entity;
            wither.setInvulTime(0); //将凋零无敌时间设为0
            wither.attackEntityFrom(src, damage);
        }else entity.attackEntityFrom(src, damage);//给与实体伤害
    }

    /**
     * 攻击龙研中的实体 混沌水晶
     * @param entity 实体
     * @param player 玩家
     */
    private void damageGuardian(Entity entity, PlayerEntity player){
        if (Endless.isDE){
            if (entity instanceof DraconicGuardianEntity){
                DraconicGuardianEntity draconicGuardian = (DraconicGuardianEntity) entity;
                draconicGuardian.attackEntityPartFrom(draconicGuardian.dragonPartHead, new InfinityDamageSource(player), Float.POSITIVE_INFINITY);
                draconicGuardian.setHealth(-1);
//                draconicGuardian.func_70097_a(new InfinityDamageSource(player), 10000);
                draconicGuardian.onDeath(new InfinityDamageSource(player));
            }else if (entity instanceof GuardianCrystalEntity && Config.SERVER.isBreakDECrystal.get()){
                GuardianCrystalEntity crystal = (GuardianCrystalEntity) entity;
                crystal.func_174812_G();
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
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        int damage = stack.getDamage();
        if (damage > 0){
            stack.getOrCreateTag().putInt("Damage", 0);
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EndlessItemEntity(world, location, itemstack);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }
}
