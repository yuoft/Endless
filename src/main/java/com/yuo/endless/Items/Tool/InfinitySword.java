package com.yuo.endless.Items.Tool;

import com.brandon3055.draconicevolution.entity.GuardianCrystalEntity;
import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Map<Enchantment, Integer> map = new HashMap<Enchantment, Integer>();
            map.put(Enchantments.LOOTING, 10);
            ItemStack stack = new ItemStack(this);
            EnchantmentHelper.setEnchantments(map, stack);
            items.add(stack);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World p_77624_2_, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new StringTextComponent(ColorText.makeFabulous(I18n.format("endless.text.itemInfo.infinity")) + I18n.format("attribute.name.generic.attack_damage")));
    }

//    @Override
//    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
//        return false;
//    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        if (entity instanceof LivingEntity && !player.world.isRemote){
            LivingEntity living = (LivingEntity) entity;
            hitEntity(stack, living, player);
            sweepAttack(living, player);
        }
        damageGuardian(entity, player);
        return false;
    }

    /**
     * 模拟左键横扫攻击
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
        if (attacker.world.isRemote) return true;
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
        if (Endless.isDraconicEvolution){
            if (entity instanceof DraconicGuardianEntity){
                DraconicGuardianEntity draconicGuardian = (DraconicGuardianEntity) entity;
                draconicGuardian.attackEntityPartFrom(draconicGuardian.dragonPartHead, new InfinityDamageSource(player), Float.MAX_VALUE);
                draconicGuardian.setHealth(-1);
            }else if (entity instanceof GuardianCrystalEntity && Config.SERVER.isBreakDECrystal.get()){
                GuardianCrystalEntity crystal = (GuardianCrystalEntity) entity;
                crystal.func_174812_G();
            }
        }
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
