package com.yuo.endless.Items.Tool;

import com.google.common.collect.Lists;
import com.yuo.endless.Config.Config;
import com.yuo.endless.Entity.*;
import com.yuo.endless.Items.ItemRegistry;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class InfinityCrossBow extends CrossbowItem {
    public static final Predicate<ItemStack> ARROWS = (stack) ->
            stack.getItem().isIn(ItemTags.ARROWS) || stack.getItem() == ItemRegistry.infinityArrow.get() || stack.getItem() == Items.FIREWORK_ROCKET;

    private boolean isLoadingStart = false;

    private boolean isLoadingMiddle = false;

    public InfinityCrossBow() {
        super(new Properties().group(ModGroup.endless).maxStackSize(1).isImmuneToFire());
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (isCharged(itemstack)) { //弹药以装填
            fireProjectiles(worldIn, playerIn, itemstack, getSpeed(itemstack), 1.0F);
            setCharged(itemstack, false);
            return ActionResult.resultConsume(itemstack);
        } else if (!findArrow(playerIn).isEmpty()) { //玩家有弹药
            if (!isCharged(itemstack)) {
                this.isLoadingStart = false;
                this.isLoadingMiddle = false;
                playerIn.setActiveHand(handIn);
            }

            return ActionResult.resultConsume(itemstack);
        }else if (findArrow(playerIn).isEmpty() && !isCharged(itemstack)){ //无弹药依然触发装填
            this.isLoadingStart = false;
            this.isLoadingMiddle = false;
            playerIn.setActiveHand(handIn);
            return ActionResult.resultConsume(itemstack);
        } else {
            return ActionResult.resultFail(itemstack);
        }
    }

    /**
     * 寻找弹药
     *
     * @param living 使用无尽弓的生物
     * @return 弹药 无尽矢 普通箭矢 烟花火箭
     */
    private static ItemStack findArrow(LivingEntity living) {
        if (living instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) living;
            ItemStack heldAmmo = InfinityBow.getHeldAmmo(player, ARROWS);
            if (!heldAmmo.isEmpty()) return heldAmmo;
            else {
                PlayerInventory inventory = player.inventory;
                for (int i = 0; i < inventory.getSizeInventory(); i++) { //优先无尽箭矢
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (stack.getItem() == ItemRegistry.infinityArrow.get()) return stack;
                }
                for (int i = 0; i < inventory.getSizeInventory(); i++) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (ItemTags.ARROWS.contains(stack.getItem())) return stack;
                }
                for (int i = 0; i < inventory.getSizeInventory(); i++) {
                    ItemStack stack = inventory.getStackInSlot(i);
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
        return stack.getItem() == Items.CROSSBOW && hasChargedProjectile(stack, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
    }

    public static void fireProjectiles(World worldIn, LivingEntity shooter, ItemStack stack, float velocityIn, float inaccuracyIn) {
        List<ItemStack> list = getChargedProjectiles(stack);
        float[] afloat = getRandomSoundPitches(shooter.getRNG()); //声音大小

        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            boolean flag = shooter instanceof PlayerEntity && ((PlayerEntity)shooter).abilities.isCreativeMode;
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
                        fireProjectile(worldIn, shooter, stack, new ItemStack(ItemRegistry.infinityArrow.get()), afloat[0], flag, velocityIn, inaccuracyIn, 0);
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
        return new float[]{1.0F, getRandomSoundPitch(flag), getRandomSoundPitch(!flag)};
    }

    private static float getRandomSoundPitch(boolean flagIn) {
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
        CompoundNBT compoundnbt = stack.getTag();
        if (compoundnbt != null && compoundnbt.contains("ChargedProjectiles", 9)) {
            ListNBT listnbt = compoundnbt.getList("ChargedProjectiles", 10);
            for(int i = 0; i < listnbt.size(); ++i) {
                CompoundNBT compoundnbt1 = listnbt.getCompound(i);
                list.add(ItemStack.read(compoundnbt1));
            }
        }

        return list;
    }

    //修改玩家状态
    private static void fireProjectilesAfter(World worldIn, LivingEntity shooter, ItemStack stack) {
        if (shooter instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)shooter;
            if (!worldIn.isRemote) {
                CriteriaTriggers.SHOT_CROSSBOW.test(serverplayerentity, stack);
            }

            serverplayerentity.addStat(Stats.ITEM_USED.get(stack.getItem()));
        }

        clearProjectiles(stack);
    }

    //清除弩上的弹药
    private static void clearProjectiles(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTag();
        if (compoundnbt != null) {
            ListNBT listnbt = compoundnbt.getList("ChargedProjectiles", 9);
            listnbt.clear();
            compoundnbt.put("ChargedProjectiles", listnbt);
        }

    }

    //射出一发
    private static void fireProjectile(World worldIn, LivingEntity shooter, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle) {
        if (!worldIn.isRemote) {
            boolean flag = projectile.getItem() == Items.FIREWORK_ROCKET;
            ProjectileEntity projectileentity;
            if (flag) {
                projectileentity = new InfinityFireWorkEntity(worldIn, projectile, shooter, Config.SERVER.infinityFireworkDamage.get(), shooter.getPosX(), shooter.getPosYEye() - (double)0.15F, shooter.getPosZ(), true);
            } else {
                projectileentity = createArrow(worldIn, shooter, crossbow, projectile);
                ((AbstractArrowEntity)projectileentity).pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
            }

            if (shooter instanceof ICrossbowUser) {
                ICrossbowUser icrossbowuser = (ICrossbowUser)shooter;
                icrossbowuser.fireProjectile(icrossbowuser.getAttackTarget(), crossbow, projectileentity, projectileAngle);
            } else {
                Vector3d vector3d1 = shooter.getUpVector(1.0F);
                Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), projectileAngle, true);
                Vector3d vector3d = shooter.getLook(1.0F);
                Vector3f vector3f = new Vector3f(vector3d);
                vector3f.transform(quaternion);
                projectileentity.shoot(vector3f.getX(), vector3f.getY(), vector3f.getZ(), velocity, inaccuracy);
            }

            worldIn.addEntity(projectileentity);
            worldIn.playSound(null, shooter.getPosX(), shooter.getPosY(), shooter.getPosZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
        }
    }

    //创建投掷物实体
    private static AbstractArrowEntity createArrow(World worldIn, LivingEntity shooter, ItemStack crossbow, ItemStack ammo) {
        AbstractArrowEntity arrow;
        if (ammo.isEmpty()){ //弹药为空发射普通箭矢
            ItemStack stack = new ItemStack(Items.ARROW);
            ArrowItem arrowitem = (ArrowItem)(stack.getItem() instanceof ArrowItem ? stack.getItem() : Items.ARROW);
            arrow = arrowitem.createArrow(worldIn, ammo, shooter);
            arrow.setDamage(Config.SERVER.noArrowDamage.get());
        }else {
            if (ammo.getItem() == ItemRegistry.infinityArrow.get()){
                arrow = new InfinityArrowEntity(EntityRegistry.INFINITY_ARROW.get(), shooter, worldIn, false);
                arrow.setPierceLevel((byte) 5);
            }else {
                arrow = new InfinityArrowSubEntity(EntityRegistry.INFINITY_ARROW_SUB.get(), shooter, worldIn, ammo);
                arrow.setPierceLevel((byte) 3);
            }
        }

        arrow.setIsCritical(true); //暴击粒子
        arrow.setHitSound(SoundEvents.ITEM_CROSSBOW_HIT);
        arrow.setShotFromCrossbow(true);
        arrow.setPierceLevel((byte)5); //5级穿透效果

        return arrow;
    }

    //弹药装填
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        float f = getCharge(i, stack);
        if (f >= 1.0F && !isCharged(stack) && hasAmmo(entityLiving, stack)) {
            setCharged(stack, true);
            SoundCategory soundcategory = entityLiving instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            worldIn.playSound(null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, soundcategory, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        List<ItemStack> list = getChargedProjectiles(stack);
        if (isCharged(stack) && !list.isEmpty()) {
            ItemStack itemstack = list.get(0);
            tooltip.add((new TranslationTextComponent("item.minecraft.crossbow.projectile")).appendString(" ").appendSibling(itemstack.getTextComponent()));
            if (flagIn.isAdvanced() && itemstack.getItem() == Items.FIREWORK_ROCKET) {
                List<ITextComponent> list1 = Lists.newArrayList();
                Items.FIREWORK_ROCKET.addInformation(itemstack, worldIn, list1, flagIn);
                if (!list1.isEmpty()) {
                    for(int i = 0; i < list1.size(); ++i) {
                        list1.set(i, (new StringTextComponent("  ")).appendSibling(list1.get(i)).mergeStyle(TextFormatting.GRAY));
                    }

                    tooltip.addAll(list1);
                }
            }

        }
    }

    @Override
    public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if (!worldIn.isRemote) {
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
            SoundEvent soundevent = this.getSoundEvent(i);
            SoundEvent soundevent1 = i == 0 ? SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE : null;
            float f = (float)(stack.getUseDuration() - count) / (float)getChargeTime(stack);
            if (f < 0.2F) {
                this.isLoadingStart = false;
                this.isLoadingMiddle = false;
            }

            if (f >= 0.2F && !this.isLoadingStart) {
                this.isLoadingStart = true;
                worldIn.playSound(null, livingEntityIn.getPosX(), livingEntityIn.getPosY(), livingEntityIn.getPosZ(), soundevent, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 0.5F && soundevent1 != null && !this.isLoadingMiddle) {
                this.isLoadingMiddle = true;
                worldIn.playSound(null, livingEntityIn.getPosX(), livingEntityIn.getPosY(), livingEntityIn.getPosZ(), soundevent1, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }
        }

    }

    private SoundEvent getSoundEvent(int enchantmentLevel) {
        switch(enchantmentLevel) {
            case 1:
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1;
            case 2:
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2;
            case 3:
                return SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3;
            default:
                return SoundEvents.ITEM_CROSSBOW_LOADING_START;
        }
    }

    //使用程度
    private static float getCharge(int useTime, ItemStack stack) {
        float f = (float)useTime / (float)getChargeTime(stack);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    private static boolean hasAmmo(LivingEntity entityIn, ItemStack stack) {
        boolean flag = entityIn instanceof PlayerEntity && ((PlayerEntity)entityIn).abilities.isCreativeMode;
        ItemStack itemstack = findArrow(entityIn); //无弹药 发射一发普通箭
        int j = itemstack.isEmpty() ? 1 :
                (ItemTags.ARROWS.contains(itemstack.getItem()) || itemstack.getItem() == Items.FIREWORK_ROCKET ? 3 : 21);
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
     * @return
     */
    private static boolean deleteStack(LivingEntity living, ItemStack stack, ItemStack itemStack, boolean flag0, boolean flag1) {
        boolean flag = flag1 && itemStack.getItem() instanceof ArrowItem;  //普通弹药被消耗
        ItemStack itemstack;
        if (itemStack.getItem() == ItemRegistry.infinityArrow.get()){
            itemstack = itemStack.copy();
        }else if (!flag && !flag1 && !flag0) {
            itemstack = itemStack.split(1);
            if (itemStack.isEmpty() && living instanceof PlayerEntity) {
                ((PlayerEntity)living).inventory.deleteStack(itemStack);
            }
        } else {
            itemstack = itemStack.copy();
        }

        addChargedProjectile(stack, itemstack);
        return true;
    }

    //为弩添加弹药数据
    private static void addChargedProjectile(ItemStack crossbow, ItemStack projectile) {
        CompoundNBT compoundnbt = crossbow.getOrCreateTag();
        ListNBT listnbt;
        if (compoundnbt.contains("ChargedProjectiles", 9)) {
            listnbt = compoundnbt.getList("ChargedProjectiles", 10);
        } else {
            listnbt = new ListNBT();
        }

        CompoundNBT compoundnbt1 = new CompoundNBT();
        projectile.write(compoundnbt1);
        listnbt.add(compoundnbt1);
        compoundnbt.put("ChargedProjectiles", listnbt);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return getChargeTime(stack) + 3; //使用时间
    }

    public static int getChargeTime(ItemStack stack) {
        return 25 - 5 * 3;//快速装填3
    }

    @Override
    public boolean isDamageable() {
        return false;
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
