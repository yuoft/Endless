package com.yuo.endless.Items.Tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class InfinityArrow extends Item  {
    private final Multimap<Attribute, AttributeModifier> tridentAttributes;

    public InfinityArrow() {
        super(new Properties().group(ModGroup.endless).maxStackSize(1).maxDamage(-1));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 48.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -2.0F, AttributeModifier.Operation.ADDITION));
        this.tridentAttributes = builder.build();
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.tridentAttributes : super.getAttributeModifiers(equipmentSlot);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.setMotion(playerIn.getLookVec().scale(2.5));
        playerIn.swingArm(handIn);
        playerIn.getCooldownTracker().setCooldown(playerIn.getHeldItem(handIn).getItem(), 10);
        BlockPos pos = playerIn.getPosition();
        for (int i = 0; i < 50; i++){
            worldIn.addOptionalParticle(ParticleTypes.PORTAL, pos.getX() + worldIn.rand.nextGaussian() / 2, pos.getY() + worldIn.rand.nextGaussian() / 2,
                    pos.getZ() + worldIn.rand.nextGaussian() / 2, worldIn.rand.nextGaussian(), worldIn.rand.nextGaussian(), worldIn.rand.nextGaussian());
        }
        worldIn.playSound(playerIn, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = attacker.getEntityWorld();
        if (!world.isRemote && world.isThundering()){ //雷雨天 召雷
            BlockPos pos = target.getPosition();
            LightningBoltEntity lightningboltentity = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
            lightningboltentity.moveForced(Vector3d.copyCenteredHorizontally(pos));
            lightningboltentity.setCaster(target instanceof ServerPlayerEntity ? (ServerPlayerEntity)target : null);
            world.addEntity(lightningboltentity);
        }
        return true;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EndlessItemEntity(world, location, itemstack);
    }
}
