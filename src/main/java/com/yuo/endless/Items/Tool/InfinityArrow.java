package com.yuo.endless.Items.Tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.Config;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.EndlessItemEntity;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Entity.GapingVoidEntity;
import com.yuo.endless.Entity.SwordVoidEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class InfinityArrow extends Item  {
    private final Multimap<Attribute, AttributeModifier> tridentAttributes;

    public InfinityArrow() {
        super(new Properties().group(EndlessTab.endless).maxStackSize(1).maxDamage(9999).isImmuneToFire());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 48.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -2.0F, AttributeModifier.Operation.ADDITION));
        this.tridentAttributes = builder.build();
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)){
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putBoolean("Unbreakable",true);
            items.add(stack);
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.tridentAttributes : super.getAttributeModifiers(equipmentSlot);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 99;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (playerIn.isSneaking()){
            hitDisEntity(playerIn, playerIn.getHeldItem(handIn));
        }else {
            playerIn.setMotion(playerIn.getLookVec().scale(3.0));
        }
        playerIn.swingArm(handIn);
        playerIn.getCooldownTracker().setCooldown(playerIn.getHeldItem(handIn).getItem(), playerIn.isSneaking() ? 120 : 10);
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
        if (!world.isRemote && world.isThundering() && Config.SERVER.isArrowLightning.get()){ //雷雨天 召雷
            BlockPos pos = target.getPosition();
            LightningBoltEntity lightningboltentity = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
            lightningboltentity.moveForced(Vector3d.copyCenteredHorizontally(pos));
            lightningboltentity.setCaster(target instanceof ServerPlayerEntity ? (ServerPlayerEntity)target : null);
            world.addEntity(lightningboltentity);
        }
        if (!world.isRemote){
            if (target instanceof WitherEntity){
                WitherEntity wither = (WitherEntity) target;
                wither.setInvulTime(0);
                wither.attackEntityFrom(new InfinityDamageSource(attacker), 49);
            }else {
                target.attackEntityFrom(new InfinityDamageSource(attacker), 49);
                if (world.rand.nextDouble() > 0.998){
                    SwordVoidEntity swordVoid = new SwordVoidEntity(EntityRegistry.INFINITY_SWORD.get(), world);
                    swordVoid.setPositionAndUpdate(target.getPosition().getX(), target.getPosition().getY() + 5, target.getPosition().getZ());
                    world.addEntity(swordVoid);
                }
            }
        }
        return super.hitEntity(stack, target, attacker);
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
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        return new EndlessItemEntity(world, location, itemstack);
    }

    /**
     * 模拟远距离攻击
     * @param playerIn 玩家
     */
    private static void hitDisEntity(PlayerEntity playerIn, ItemStack sword) {
        Attribute attribute = ForgeMod.REACH_DISTANCE.get();
        double dis = attribute.getDefaultValue() * 10;
        rayTrace(playerIn, dis, 1.0f);
    }

    public static void rayTrace(PlayerEntity entity, double playerReach, float partialTicks) {
        Vector3d eyePosition = entity.getEyePosition(partialTicks);
        Minecraft mc = Minecraft.getInstance();
        Vector3d traceEnd;
        if (mc.objectMouseOver != null && mc.objectMouseOver.getType() == Type.BLOCK) {
            traceEnd = mc.objectMouseOver.getHitVec();
            traceEnd = eyePosition.add(traceEnd.subtract(eyePosition).scale(1.01));
        } else {
            Vector3d lookVector = entity.getLook(partialTicks);
            traceEnd = eyePosition.add(lookVector.x * playerReach, lookVector.y * playerReach, lookVector.z * playerReach);
        }

        World world = entity.getEntityWorld();
        RayTraceResult raytraceresult = world.rayTraceBlocks(new RayTraceContext(eyePosition, traceEnd, BlockMode.COLLIDER, FluidMode.NONE, entity));
        Vector3d hitVec = raytraceresult.getHitVec();
        //向目标位置冲刺
        double sq = Math.sqrt(entity.getDistanceSq(hitVec));
//        entity.setMotion(hitVec.normalize().scale(sq));
        GapingVoidEntity.setEntityMotionFromVector(entity, new BlockPos(hitVec), 0.5);
        //击杀路线上实体
        AxisAlignedBB bound = new AxisAlignedBB(eyePosition, traceEnd);
        Predicate<Entity> predicate = (e) -> e instanceof LivingEntity;
        rayTraceEntities(world, entity, bound, predicate);
    }

    public static void rayTraceEntities(World worldIn, PlayerEntity player, AxisAlignedBB boundingBox, Predicate<Entity> filter) {
        Iterator<Entity> var9 = worldIn.getEntitiesInAABBexcluding(player, boundingBox, filter).iterator();
        var9.forEachRemaining(entity -> {
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.attackEntityFrom(new InfinityDamageSource(player), Float.MAX_VALUE);
            }
        });
    }
}
