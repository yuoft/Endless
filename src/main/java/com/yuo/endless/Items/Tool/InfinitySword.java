package com.yuo.endless.Items.Tool;

import com.yuo.endless.Event.EventHandler;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfinitySword extends SwordItem{

    public InfinitySword() {
        super(MyItemTier.INFINITY_ARMS, 0, -2.4f, new Properties().group(ModGroup.myGroup).isImmuneToFire());
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
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    //攻击实体
    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.world.isRemote) return true;
        if (target instanceof EnderDragonEntity && attacker instanceof PlayerEntity){
            EnderDragonEntity drageon = (EnderDragonEntity) target; //攻击末影龙
            drageon.attackEntityPartFrom(drageon.dragonPartHead, new InfinityDamageSource(attacker), Float.POSITIVE_INFINITY);
        }else if (target instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) target;
            if (EventHandler.isInfinite(player)){ //如果玩家穿戴全套无尽装备，则只造成10点伤害
                player.attackEntityFrom(new InfinityDamageSource(attacker), 10.0f);
            }else player.attackEntityFrom(new InfinityDamageSource(attacker), Float.POSITIVE_INFINITY);
        }
        else target.attackEntityFrom(new InfinityDamageSource(attacker), Float.POSITIVE_INFINITY);
        target.setHealth(0);
        target.onDeath(new InfinityDamageSource(attacker));
        return true;
    }

    //范围伤害
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            attackAOE(playerIn, 32, 10000, playerIn.isSneaking());
            playerIn.getCooldownTracker().setCooldown(heldItem.getItem(), 20);
        }
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP , SoundCategory.PLAYERS, 1.0f, 5.0f);
        return ActionResult.resultSuccess(heldItem);
    }
    //aoe伤害
    protected void attackAOE(PlayerEntity player,float range, float damage,boolean type)
    {
        if (player.world.isRemote) return;
        AxisAlignedBB aabb = player.getBoundingBox().grow(range);//范围
        List<Entity> toAttack = player.getEntityWorld().getEntitiesWithinAABBExcludingEntity(player, aabb);//生物列表
        DamageSource src = new InfinityDamageSource(player);//伤害类型
        for (Entity entity : toAttack) { //循环遍历
            if(type) {
                if(entity instanceof LivingEntity) {
                    entity.attackEntityFrom(src, damage);//给与实体伤害
                }
            }
            else {
                if (entity instanceof IMob) {
                    if (entity instanceof EnderDragonEntity){
                        EnderDragonEntity drageon = (EnderDragonEntity) entity;
                        drageon.attackEntityPartFrom(drageon.dragonPartHead, src, Float.POSITIVE_INFINITY);
                    }else if (entity instanceof WitherEntity){
                        WitherEntity wither = (WitherEntity) entity;
                        wither.setInvulTime(0); //将凋零无敌时间设为0
                        wither.attackEntityFrom(src, damage);
                    }
                    else entity.attackEntityFrom(src, damage);
                }
            }
        }
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EndlessItemEntity(world, location.getPosX(), location.getPosY(), location.getPosZ(), itemstack);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }
}
