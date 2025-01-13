package com.yuo.endless.Items.Tool;

import com.yuo.endless.Client.Sound.ModSounds;
import com.yuo.endless.Config;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.*;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class InfinityBow extends BowItem {
    public static final Predicate<ItemStack> ARROWS = (stack) -> stack.is(ItemTags.ARROWS) || stack.getItem() == EndlessItems.infinityArrow.get();

    public InfinityBow() {
        super(new Properties().tab(EndlessTab.endless).stacksTo(1).durability(9999).fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> components, TooltipFlag pIsAdvanced) {
        CompoundTag nbt = stack.getOrCreateTag();
        boolean bow = nbt.getBoolean("InfinityBow");
        if (bow){
            components.add(new TranslatableComponent("endless.text.itemInfo.infinity_bow"));
        }
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
        if (player.isCrouching()) {
            CompoundTag nbt = itemstack.getOrCreateTag();
            nbt.putBoolean("InfinityBow", !nbt.getBoolean("InfinityBow"));
            itemstack.setTag(nbt);
            return InteractionResultHolder.success(itemstack);
        }
        InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onArrowNock(itemstack, level, player, hand, true);
        if (ret != null) return ret;

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void onUsingTick(ItemStack bow, LivingEntity player, int count) {
        Level world = player.level;
        CompoundTag nbt = bow.getOrCreateTag();
        boolean flag = nbt.getBoolean("InfinityBow");
        if (flag && player instanceof Player) {
            int useTime = getUseTime(count);
            int circleNum = getCircleNumFormBowUseTime(useTime);

            for (int i = 1; i <= circleNum; i++) {
                double radius = i / 3.0d + Math.min(i / 2.0d, useTime / 20.0d);
                int particleNum = 36 + 36 * (i - 1) + 36 * Math.max(0, i - 2) + 36 * Math.max(0, i - 3);
                double dis = 4 * i;
                spawnCircleParticle((Player) player, world, radius, particleNum, dis, i);
            }

            if (!world.isClientSide) {
                if (useTime == 20) //蓄力
                    world.playSound(null, player.getOnPos(), ModSounds.INFINITY_BOW_STAR.get(), SoundSource.NEUTRAL, 6.0f, 1.0f);
                if (useTime == 200) //蓄力完成
                    world.playSound(null, player.getOnPos(), ModSounds.INFINITY_BOW_END.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
            }

        }
    }

    //使用时间
    public int getUseTime(int count){
        return getUseDuration(new ItemStack(this)) - count;
    }

    /**
     * 根据使用时间获取粒子圆数量 max 4
     * @param time 使用时间
     * @return 数量
     */
    public int getCircleNumFormBowUseTime(int time){
        if (time > 0 && time<= 50) return 1;
        else if (time > 50 && time <= 100) return 2;
        else if (time > 100 && time <= 150) return 3;
        else return 4;
    }

    /**
     * 生成垂直于玩家视线的粒子圆
     * @param player 玩家
     * @param world 世界
     * @param radius 粒子圆半径
     * @param num 粒子生成数量
     * @param dis 粒子生成与玩家距离
     */
    public void spawnCircleParticle(Player player, Level world, double radius, int num, double dis, int j){
        Vec3 lookDirection = player.getLookAngle().normalize();

        // 计算垂直于视线方向的向量
        Vec3 up = new Vec3(0, 1, 0);
        Vec3 right = lookDirection.cross(up).normalize();
        Vec3 upPerpendicular = lookDirection.cross(right).normalize();

        // 生成圆形粒子效果
        for (int i = 0; i < num; i++) {
            double angle = 2 * Math.PI / num * i; //角度
            double xOffset = Math.cos(angle) * radius;
            double zOffset = Math.sin(angle) * radius;

            Vec3 particlePosition = player.getEyePosition(1.0f).add(lookDirection.scale(dis)).add(right.scale(xOffset)).add(upPerpendicular.scale(zOffset));

            // 添加粒子
            if (j == 1)
                world.addParticle(ParticleTypes.FLAME, particlePosition.x, particlePosition.y, particlePosition.z, 0, 0, 0);
            else if (j == 2)
                world.addParticle(ParticleTypes.DRAGON_BREATH, particlePosition.x, particlePosition.y, particlePosition.z, 0, 0, 0);
            else if (j == 3)
                world.addParticle(ParticleTypes.HAPPY_VILLAGER, particlePosition.x, particlePosition.y, particlePosition.z, 0, 0, 0);
            else if (j == 4)
                world.addParticle(ParticleTypes.CRIT, particlePosition.x, particlePosition.y, particlePosition.z, 0, 0, 0);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeLeft) {
        if (living instanceof Player player) {
            int max = getUseDuration(stack); //总使用时间
            float velocity = BowItem.getPowerForTime(max - timeLeft); //速度
            velocity = Math.max(velocity, 1.0f);

            CompoundTag nbt = stack.getOrCreateTag();
            boolean flag = nbt.getBoolean("InfinityBow");
            int useTime = getUseTime(timeLeft);

            ItemStack itemStack = findArrow(player);
            if (!level.isClientSide) {
                AbstractArrow arrow;
                if (!itemStack.isEmpty()) {
                    if (itemStack.getItem() == EndlessItems.infinityArrow.get()) { //无尽箭矢
                        if (flag && useTime >= 200){
                            arrow = new InfinitySuperArrowEntity(EntityRegistry.INFINITY_ARROW.get(), player, level, useTime);
                        }else {
                            arrow = new InfinityArrowEntity(EntityRegistry.INFINITY_ARROW.get(), player, level, true);
                            arrow.setPierceLevel((byte) 3);
                        }
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
