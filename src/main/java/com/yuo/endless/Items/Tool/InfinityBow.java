package com.yuo.endless.Items.Tool;

import com.yuo.endless.Config;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Entity.InfinityArrowEntity;
import com.yuo.endless.Entity.InfinityArrowSubEntity;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.EndlessTab;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class InfinityBow extends BowItem {
    public static final Predicate<ItemStack> ARROWS = (stack) -> stack.getItem().isIn(ItemTags.ARROWS) || stack.getItem() == EndlessItems.infinityArrow.get();

    public InfinityBow() {
        super(new Properties().group(EndlessTab.endless).maxStackSize(1).maxDamage(9999).isImmuneToFire());
    }

    //使用时间
    @Override
    public int getUseDuration(ItemStack stack) {
        return 1200;
    }

    @Override
    public int getItemEnchantability() {
        return 99;
    }

    //使用时动作
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, true);
        if (ret != null) return ret;

        playerIn.setActiveHand(handIn);
        return ActionResult.resultConsume(itemstack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            int max = getUseDuration(stack); //总使用时间
            float velocity = BowItem.getArrowVelocity(max - timeLeft); //速度
            velocity = Math.max(velocity, 1.0f);
            ItemStack itemStack = findArrow(player);
            if (!worldIn.isRemote) {
                AbstractArrowEntity arrow;
                if (!itemStack.isEmpty()) {
                    if (itemStack.getItem() == EndlessItems.infinityArrow.get()) { //无尽箭矢
                        arrow = new InfinityArrowEntity(EntityRegistry.INFINITY_ARROW.get(), player, worldIn, true);
                        arrow.setPierceLevel((byte) 3);
                    } else {
                        arrow = new InfinityArrowSubEntity(EntityRegistry.INFINITY_ARROW_SUB.get(), player, worldIn, itemStack); //普通箭矢
                        arrow.setPierceLevel((byte) 1);
                    }
                } else { //无箭矢
                    ItemStack arrowStack = new ItemStack(Items.ARROW);
                    ArrowItem arrowitem = (ArrowItem)(arrowStack.getItem() instanceof ArrowItem ? arrowStack.getItem() : Items.ARROW);
                    arrow = arrowitem.createArrow(worldIn, stack, entityLiving);
                    arrow.setDamage(Config.SERVER.noArrowDamage.get());
                }
                arrow.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, velocity * 3.0F, 1.0F);
                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                    arrow.setFire(100);
                }
//                int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
//                if (k > 0) {
//                    arrow.setKnockbackStrength(k);
//                }
                arrow.setIsCritical(true); //暴击粒子
                arrow.setShooter(player);

                arrow.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                worldIn.addEntity(arrow);
            }
            worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (worldIn.rand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
            if (!player.isCreative() && itemStack.getItem() instanceof ArrowItem){
                itemStack.shrink(1);
            }
            player.addStat(Stats.ITEM_USED.get(this));
        }
    }

    /**
     * 寻找弹药
     *
     * @param living 使用无尽弓的生物
     * @return 弹药
     */
    private ItemStack findArrow(LivingEntity living) {
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) living;
            ItemStack heldAmmo = getHeldAmmo(player, ARROWS);
            if (!heldAmmo.isEmpty()) return heldAmmo; //主副手有箭矢
            else {
                PlayerInventory inventory = player.inventory;
                for (int i = 0; i < inventory.getSizeInventory(); i++) { //优先无尽箭矢
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (stack.getItem() == EndlessItems.infinityArrow.get()) return stack;
                }
                for (int i = 0; i < inventory.getSizeInventory(); i++) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (ItemTags.ARROWS.contains(stack.getItem())) return stack;
                }
                return player.isCreative() ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getHeldAmmo(LivingEntity living, Predicate<ItemStack> isAmmo) {
        if (isAmmo.test(living.getHeldItem(Hand.OFF_HAND))) {
            return living.getHeldItem(Hand.OFF_HAND);
        } else {
            return isAmmo.test(living.getHeldItem(Hand.MAIN_HAND)) ? living.getHeldItem(Hand.MAIN_HAND) : ItemStack.EMPTY;
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
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)){
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putBoolean("Unbreakable",true);
            items.add(stack);
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
