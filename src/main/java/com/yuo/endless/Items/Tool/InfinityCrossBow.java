package com.yuo.endless.Items.Tool;

import com.google.common.collect.Lists;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.yuo.endless.Config;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.*;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class InfinityCrossBow extends CrossbowItem {
    public static final Predicate<ItemStack> ARROWS = (stack) ->
            stack.is(ItemTags.ARROWS) || stack.getItem() == EndlessItems.infinityArrow.get() || stack.getItem() == Items.FIREWORK_ROCKET;

    private boolean isLoadingStart = false;

    private boolean isLoadingMiddle = false;

    public InfinityCrossBow() {
        super(new Properties().tab(EndlessTab.endless).stacksTo(1).durability(9999).fireResistant());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (isCharged(itemstack)) { //弹药以装填
            fireProjectiles(level, player, itemstack, getSpeed(itemstack), 1.0F);
            setCharged(itemstack, false);
            return InteractionResultHolder.consume(itemstack);
        } else if (!findArrow(player).isEmpty()) { //玩家有弹药
            if (!isCharged(itemstack)) {
                this.isLoadingStart = false;
                this.isLoadingMiddle = false;
                player.startUsingItem(hand);
            }

            return InteractionResultHolder.consume(itemstack);
        }else if (findArrow(player).isEmpty() && !isCharged(itemstack)){ //无弹药依然触发装填
            this.isLoadingStart = false;
            this.isLoadingMiddle = false;
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 99;
    }

    /**
     * 寻找弹药
     *
     * @param living 使用无尽弓的生物
     * @return 弹药 无尽矢 普通箭矢 烟花火箭
     */
    private static ItemStack findArrow(LivingEntity living) {
        if (living instanceof Player player) {
            ItemStack heldAmmo = InfinityBow.getHeldAmmo(player, ARROWS);
            if (!heldAmmo.isEmpty()) return heldAmmo;
            else {
                Inventory inventory = player.getInventory();
                for (int i = 0; i < inventory.getContainerSize(); i++) { //优先无尽箭矢
                    ItemStack stack = inventory.getItem(i);
                    if (stack.getItem() == EndlessItems.infinityArrow.get()) return stack;
                }
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack stack = inventory.getItem(i);
                    if (stack.is(ItemTags.ARROWS)) return stack;
                }
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack stack = inventory.getItem(i);
                    if (stack.getItem() == Items.FIREWORK_ROCKET) return stack;
                }
                return player.isCreative() ? new ItemStack(Items.ARROW) : ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * 获取弹药速度
     * @param stack 弩
     * @return 速度
     */
    private static float getSpeed(ItemStack stack) {
        return stack.getItem() == Items.CROSSBOW && containsChargedProjectile(stack, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
    }

    public static void fireProjectiles(Level worldIn, LivingEntity shooter, ItemStack stack, float velocityIn, float inaccuracyIn) {
        List<ItemStack> list = getChargedProjectiles(stack);
        float[] afloat = getRandomSoundPitches(shooter.getRandom()); //声音大小

        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            boolean flag = shooter instanceof Player && ((Player)shooter).isCreative();
            if (!itemstack.isEmpty()) {
                if (list.size() <= 3){
                    if (i == 0) {
                        fireProjectile(worldIn, shooter, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 0.0F);
                    } else if (i == 1) {
                        fireProjectile(worldIn, shooter, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, -10.0F);
                    } else {
                        fireProjectile(worldIn, shooter, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 10.0F);
                    }
                }else { // 无尽箭矢 扇形射出大量箭矢 中间为无尽箭
                    if (i == 10){
                        fireProjectile(worldIn, shooter, stack, new ItemStack(EndlessItems.infinityArrow.get()), afloat[0], flag, velocityIn, inaccuracyIn, 0);
                    }else {
                        fireProjectile(worldIn, shooter, stack, new ItemStack(Items.ARROW), afloat[i < 10 ? 1 : 2], flag, velocityIn, inaccuracyIn, getArrowAngle(i, i < 10));
                    }
                }
            }else {
                fireProjectile(worldIn, shooter, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 0.0F);
            }
        }

        fireProjectilesAfter(worldIn, shooter, stack);
    }

    /**
     * 获取箭矢散射角度
     * @param i 箭矢序数
     * @param flag 是否偏向左边
     * @return 角度
     */
    private static float getArrowAngle(int i, boolean flag){
        return flag ?  -(45f - i * 4.5f): (i - 10) * 4.5f;
    }

    private static float[] getRandomSoundPitches(Random rand) {
        boolean flag = rand.nextBoolean();
        return new float[]{1.0F, getRandomSoundPitch(flag, rand), getRandomSoundPitch(!flag, rand)};
    }

    private static float getRandomSoundPitch(boolean flagIn, Random random) {
        float f = flagIn ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * 0.5F + 1.8F) + f;
    }


    /**
     * 获取装填的弹药
     * @param stack 弩
     * @return 弹药列表
     */
    private static List<ItemStack> getChargedProjectiles(ItemStack stack) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("ChargedProjectiles", 9)) {
            ListTag listnbt = tag.getList("ChargedProjectiles", 10);
            for(int i = 0; i < listnbt.size(); ++i) {
                CompoundTag nbt = listnbt.getCompound(i);
                list.add(ItemStack.of(nbt));
            }
        }

        return list;
    }

    //修改玩家状态
    private static void fireProjectilesAfter(Level worldIn, LivingEntity shooter, ItemStack stack) {
        if (shooter instanceof ServerPlayer serverPlayer) {
            if (!worldIn.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger(serverPlayer, stack);
            }

            serverPlayer.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        }

        clearProjectiles(stack);
    }

    //清除弩上的弹药
    private static void clearProjectiles(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTag();
        if (compoundnbt != null) {
            ListTag listnbt = compoundnbt.getList("ChargedProjectiles", 9);
            listnbt.clear();
            compoundnbt.put("ChargedProjectiles", listnbt);
        }

    }

    //射出一发
    private static void fireProjectile(Level worldIn, LivingEntity shooter, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle) {
        if (!worldIn.isClientSide) {
            boolean flag = projectile.getItem() == Items.FIREWORK_ROCKET;
            Projectile projectile1;
            if (flag) {
                projectile1 = new InfinityFireWorkEntity(worldIn, projectile, shooter, Config.SERVER.infinityFireworkDamage.get(), shooter.getX(), shooter.getEyeY() - (double)0.15F, shooter.getZ(), true);
            } else {
                projectile1 = createArrow(worldIn, shooter, crossbow, projectile);
                ((AbstractArrow)projectile1).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            if (shooter instanceof CrossbowAttackMob crossbowmen) {
                crossbowmen.shootCrossbowProjectile(Objects.requireNonNull(crossbowmen.getTarget()), crossbow, projectile1, projectileAngle);
            } else {
                Vec3 vector3d1 = shooter.getUpVector(1.0F);
                Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), projectileAngle, true);
                Vec3 vector3d = shooter.getViewVector(1.0F);
                Vector3f vector3f = new Vector3f(vector3d);
                vector3f.transform(quaternion);
                projectile1.shoot(vector3f.x(), vector3f.y(), vector3f.z(), velocity, inaccuracy);
            }

            worldIn.addFreshEntity(projectile1);
            worldIn.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, soundPitch);
        }
    }

    //创建投掷物实体
    private static AbstractArrow createArrow(Level worldIn, LivingEntity shooter, ItemStack crossbow, ItemStack ammo) {
        AbstractArrow arrow;
        if (ammo.isEmpty()){ //弹药为空发射普通箭矢
            ItemStack stack = new ItemStack(Items.ARROW);
            ArrowItem arrowitem = (ArrowItem)(stack.getItem() instanceof ArrowItem ? stack.getItem() : Items.ARROW);
            arrow = arrowitem.createArrow(worldIn, ammo, shooter);
            arrow.setBaseDamage(Config.SERVER.noArrowDamage.get());
            arrow.setPierceLevel((byte) 1);
        }else {
            if (ammo.getItem() == EndlessItems.infinityArrow.get()){
                arrow = new InfinityArrowEntity(EntityRegistry.INFINITY_ARROW.get(), shooter, worldIn, false);
                arrow.setPierceLevel((byte) 5);//5级穿透效果
            }else {
                arrow = new InfinityArrowSubEntity(EntityRegistry.INFINITY_ARROW_SUB.get(), shooter, worldIn, ammo);
                arrow.setPierceLevel((byte) 3);
            }
        }

        arrow.setCritArrow(true); //暴击粒子
        arrow.setSoundEvent(SoundEvents.CROSSBOW_SHOOT);
        arrow.setShotFromCrossbow(true);

        return arrow;
    }

    //弹药装填

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        float f = getCharge(i, stack);
        if (f >= 1.0F && !isCharged(stack) && hasAmmo(living, stack)) {
            setCharged(stack, true);
            SoundSource subcategory = living instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            level.playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.CROSSBOW_LOADING_END, subcategory, 1.0F, 1.0F / (level.random.nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> components, TooltipFlag pFlag) {
        List<ItemStack> list = getChargedProjectiles(stack);
        if (isCharged(stack) && !list.isEmpty()) {
            ItemStack itemstack = list.get(0);
            components.add((new TranslatableComponent("item.minecraft.crossbow.projectile")).append(" ").append(itemstack.getDisplayName()));
            if (pFlag.isAdvanced() && itemstack.getItem() == Items.FIREWORK_ROCKET) {
                List<Component> list1 = Lists.newArrayList();
                Items.FIREWORK_ROCKET.appendHoverText(itemstack, level, list1, pFlag);
                if (!list1.isEmpty()) {
                    list1.replaceAll(pSibling -> (new TextComponent("  ")).append(pSibling).withStyle(ChatFormatting.GRAY));

                    components.addAll(list1);
                }
            }

        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int count) {
        if (!level.isClientSide) {
            int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
            SoundEvent soundevent = this.getSoundEvent(i);
            SoundEvent soundevent1 = i == 0 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
            float f = (float)(stack.getUseDuration() - count) / (float)getChargeTime();
            if (f < 0.2F) {
                this.isLoadingStart = false;
                this.isLoadingMiddle = false;
            }

            if (f >= 0.2F && !this.isLoadingStart) {
                this.isLoadingStart = true;
                level.playSound(null, living.getX(), living.getY(), living.getZ(), soundevent, SoundSource.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 0.5F && soundevent1 != null && !this.isLoadingMiddle) {
                this.isLoadingMiddle = true;
                level.playSound(null, living.getX(), living.getY(), living.getZ(), soundevent1, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }
    }

    private SoundEvent getSoundEvent(int enchantmentLevel) {
        return switch (enchantmentLevel) {
            case 1 -> SoundEvents.CROSSBOW_QUICK_CHARGE_1;
            case 2 -> SoundEvents.CROSSBOW_QUICK_CHARGE_2;
            case 3 -> SoundEvents.CROSSBOW_QUICK_CHARGE_3;
            default -> SoundEvents.CROSSBOW_LOADING_START;
        };
    }

    //使用程度
    private static float getCharge(int useTime, ItemStack stack) {
        float f = (float)useTime / (float)getChargeTime();
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    private static boolean hasAmmo(LivingEntity entityIn, ItemStack stack) {
        boolean flag = entityIn instanceof Player && ((Player)entityIn).isCreative();
        ItemStack itemstack = findArrow(entityIn); //无弹药 发射一发普通箭
        int j = itemstack.isEmpty() ? 1 : (itemstack.is(ItemTags.ARROWS) || itemstack.getItem() == Items.FIREWORK_ROCKET ? 3 : 21);
        ItemStack itemstack1 = itemstack.copy();

        for(int k = 0; k < j; ++k) {
            if (k > 0) {
                itemstack = itemstack1.copy();
            }

            if (itemstack.isEmpty() && flag) {
                itemstack = new ItemStack(Items.ARROW);
                itemstack1 = itemstack.copy();
            }

            if (!deleteStack(entityIn, stack, itemstack, k > 0, flag)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 消耗库存弹药
     * @param living 实体
     * @param stack 弩
     * @param itemStack 弹药
     * @param flag0 弹药数量是否超过一
     * @param flag1 是否创造模式
     * @return true
     */
    private static boolean deleteStack(LivingEntity living, ItemStack stack, ItemStack itemStack, boolean flag0, boolean flag1) {
        boolean flag = flag1 && itemStack.getItem() instanceof ArrowItem;  //普通弹药被消耗
        ItemStack itemstack;
        if (itemStack.getItem() == EndlessItems.infinityArrow.get()){
            itemstack = itemStack.copy();
        }else if (!flag && !flag1 && !flag0) {
            itemstack = itemStack.split(1);
            if (itemStack.isEmpty() && living instanceof Player) {
                ((Player)living).getInventory().removeItem(itemStack);
            }
        } else {
            itemstack = itemStack.copy();
        }

        addChargedProjectile(stack, itemstack);
        return true;
    }

    //为弩添加弹药数据
    private static void addChargedProjectile(ItemStack crossbow, ItemStack projectile) {
        CompoundTag compoundnbt = crossbow.getOrCreateTag();
        ListTag listnbt;
        if (compoundnbt.contains("ChargedProjectiles", 9)) {
            listnbt = compoundnbt.getList("ChargedProjectiles", 10);
        } else {
            listnbt = new ListTag();
        }

        CompoundTag compoundnbt1 = new CompoundTag();
        projectile.deserializeNBT(compoundnbt1);
        listnbt.add(compoundnbt1);
        compoundnbt.put("ChargedProjectiles", listnbt);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return getChargeTime() + 3; //使用时间
    }

    public static int getChargeTime() {
        return 25 - 5 * 3;//快速装填3
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
