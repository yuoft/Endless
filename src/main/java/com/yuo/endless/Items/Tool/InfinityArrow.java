package com.yuo.endless.Items.Tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.Config;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.EndlessItemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class InfinityArrow extends Item {
    private final Multimap<Attribute, AttributeModifier> tridentAttributes;

    public InfinityArrow() {
        super(new Properties().tab(EndlessTab.endless).stacksTo(1).defaultDurability(9999).fireResistant());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 48.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.0F, AttributeModifier.Operation.ADDITION));
        this.tridentAttributes = builder.build();
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (this.allowdedIn(tab)){
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putBoolean("Unbreakable",true);
            items.add(stack);
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pSlot) {
        return super.getDefaultAttributeModifiers(pSlot);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.tridentAttributes : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 99;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.setDeltaMovement(player.getLookAngle().scale(2.5));
        player.swing(hand);
        player.getCooldowns().addCooldown(player.getItemInHand(hand).getItem(), 10);
        BlockPos pos = player.getOnPos();
        for (int i = 0; i < 50; i++){
            level.addParticle(ParticleTypes.PORTAL, pos.getX() + level.random.nextGaussian() / 2, pos.getY() + level.random.nextGaussian() / 2,
                    pos.getZ() + level.random.nextGaussian() / 2, level.random.nextGaussian(), level.random.nextGaussian(), level.random.nextGaussian());
        }
        level.playSound(player, pos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
        return super.use(level, player, hand);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Level world = attacker.getLevel();
        if (!world.isClientSide && world.isThundering() && Config.SERVER.isArrowLightning.get()){ //雷雨天 召雷
            BlockPos pos = target.getOnPos();
            LightningBolt lightningboltentity = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
            lightningboltentity.moveTo(Vec3.atBottomCenterOf(pos));
            lightningboltentity.setCause(target instanceof ServerPlayer ? (ServerPlayer)target : null);
            world.addFreshEntity(lightningboltentity);
        }
        if (target instanceof WitherBoss wither){
            wither.setInvulnerableTicks(0);
            wither.hurt(new InfinityDamageSource(attacker), 49);
        }else if (target instanceof EnderDragon dragon){
            dragon.hurt(dragon.head, new InfinityDamageSource(attacker), 49);
        }else if (target instanceof ArmorStand){
            target.hurt(DamageSource.GENERIC, 10);
        }else target.hurt(new InfinityDamageSource(attacker), 49);
        return super.hurtEnemy(stack, target, attacker);
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
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        int damage = pStack.getDamageValue();
        if (damage > 0){
            pStack.getOrCreateTag().putInt("Damage", 0);
        }
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(Level world, Entity location, ItemStack itemstack) {
        return new EndlessItemEntity(world, location, itemstack);
    }
}
