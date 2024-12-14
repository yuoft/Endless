package com.yuo.endless.Items.Tool;

import com.yuo.endless.Config;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.EndlessItemEntity;
import com.yuo.endless.Entity.EntityRegistry;
import com.yuo.endless.Entity.InfinityArrowEntity;
import com.yuo.endless.Entity.InfinityArrowSubEntity;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class InfinityBow extends BowItem {
    public static final Predicate<ItemStack> ARROWS = (stack) -> stack.is(ItemTags.ARROWS) || stack.getItem() == EndlessItems.infinityArrow.get();

    public InfinityBow() {
        super(new Properties().tab(EndlessTab.endless).stacksTo(1).durability(9999).fireResistant());
    }

    //使用时间
    @Override
    public int getUseDuration(ItemStack stack) {
        return 1200;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 99;
    }

    //使用时动作
    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onArrowNock(itemstack, level, player, hand, true);
        if (ret != null) return ret;

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeLeft) {
        if (living instanceof Player player) {
            int max = getUseDuration(stack); //总使用时间
            float velocity = BowItem.getPowerForTime(max - timeLeft); //速度
            velocity = Math.max(velocity, 1.0f);
            ItemStack itemStack = findArrow(player);
            if (!level.isClientSide) {
                AbstractArrow arrow;
                if (!itemStack.isEmpty()) {
                    if (itemStack.getItem() == EndlessItems.infinityArrow.get()) { //无尽箭矢
                        arrow = new InfinityArrowEntity(EntityRegistry.INFINITY_ARROW.get(), player, level, true);
                        arrow.setPierceLevel((byte) 3);
                    } else {
                        arrow = new InfinityArrowSubEntity(EntityRegistry.INFINITY_ARROW_SUB.get(), player, level, itemStack); //普通箭矢
                        arrow.setPierceLevel((byte) 1);
                    }
                } else { //无箭矢
                    ItemStack arrowStack = new ItemStack(Items.ARROW);
                    ArrowItem arrowitem = (ArrowItem)(arrowStack.getItem() instanceof ArrowItem ? arrowStack.getItem() : Items.ARROW);
                    arrow = arrowitem.createArrow(level, stack, player);
                    arrow.setBaseDamage(Config.SERVER.noArrowDamage.get());
                }
                arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, velocity * 3.0F, 1.0F);
                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
                    arrow.setRemainingFireTicks(100);
                }
                arrow.setCritArrow(true); //暴击粒子
                arrow.setOwner(player);

                arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                level.addFreshEntity(arrow);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 1.0F, 1.0F / (level.random.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
            if (!player.isCreative() && itemStack.getItem() instanceof ArrowItem){
                itemStack.shrink(1);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    /**
     * 寻找弹药
     *
     * @param living 使用无尽弓的生物
     * @return 弹药
     */
    private ItemStack findArrow(LivingEntity living) {
        if (living instanceof Player player) {
            ItemStack heldAmmo = getHeldAmmo(player, ARROWS);
            if (!heldAmmo.isEmpty()) return heldAmmo; //主副手有箭矢
            else {
                Inventory inventory = player.getInventory();
                for (int i = 0; i < inventory.getContainerSize(); i++) { //优先无尽箭矢
                    ItemStack stack = inventory.getItem(i);
                    if (stack.getItem() == EndlessItems.infinityArrow.get()) return stack;
                }
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack stack = inventory.getItem(i);
                    if ((stack.is(ItemTags.ARROWS))) return stack;
                }
                return player.isCreative() ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack getHeldAmmo(LivingEntity living, Predicate<ItemStack> isAmmo) {
        if (isAmmo.test(living.getItemInHand(InteractionHand.OFF_HAND))) {
            return living.getItemInHand(InteractionHand.OFF_HAND);
        } else {
            return isAmmo.test(living.getItemInHand(InteractionHand.MAIN_HAND)) ? living.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
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

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(tab)){
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putBoolean("Unbreakable",true);
            stacks.add(stack);
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
