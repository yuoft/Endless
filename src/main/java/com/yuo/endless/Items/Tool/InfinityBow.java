package com.yuo.endless.Items.Tool;

import com.yuo.endless.Client.Sound.ModSounds;
import com.yuo.endless.Config;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.*;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class InfinityBow extends BowItem {
    public static final Predicate<ItemStack> ARROWS = (stack) -> stack.getItem().isIn(ItemTags.ARROWS) || stack.getItem() == EndlessItems.infinityArrow.get();

    public InfinityBow() {
        super(new Properties().group(EndlessTab.endless).maxStackSize(1).maxDamage(9999).isImmuneToFire());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> components, ITooltipFlag flag) {
        CompoundNBT nbt = stack.getOrCreateTag();
        boolean bow = nbt.getBoolean("InfinityBow");
        if (bow){
            components.add(new TranslationTextComponent("endless.text.itemInfo.infinity_bow"));
        }
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
        if (playerIn.isSneaking()) {
            CompoundNBT nbt = itemstack.getOrCreateTag();
            nbt.putBoolean("InfinityBow", !nbt.getBoolean("InfinityBow"));
            itemstack.setTag(nbt);
            return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
        }
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, true);
        if (ret != null) return ret;
        playerIn.setActiveHand(handIn);
        return ActionResult.resultConsume(itemstack);
    }

    @Override
    public void onUsingTick(ItemStack bow, LivingEntity player, int count) {
        World world = player.world;
        CompoundNBT nbt = bow.getOrCreateTag();
        boolean flag = nbt.getBoolean("InfinityBow");
        if (flag && player instanceof PlayerEntity) {
            int useTime = getUseTime(count);
            int circleNum = getCircleNumFormBowUseTime(useTime);

            for (int i = 1; i <= circleNum; i++) {
                double radius = i / 3.0d + Math.min(i / 2.0d, useTime / 20.0d);
                int particleNum = 36 + 36 * (i - 1) + 36 * Math.max(0, i - 2) + 36 * Math.max(0, i - 3);
                double dis = 4 * i;
                spawnCircleParticle((PlayerEntity) player, world, radius, particleNum, dis, i);
            }

            if (!world.isRemote) {
                if (useTime == 20) //蓄力
                    world.playSound(null, player.getPosition(), ModSounds.INFINITY_BOW_STAR.get(), SoundCategory.NEUTRAL, 6.0f, 1.0f);
                if (useTime == 200) //蓄力完成
                    world.playSound(null, player.getPosition(), ModSounds.INFINITY_BOW_END.get(), SoundCategory.NEUTRAL, 1.0f, 1.0f);
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
    public void spawnCircleParticle(PlayerEntity player, World world, double radius, int num, double dis, int j){
        Vector3d lookDirection = player.getLook(1.0F).normalize();

        // 计算垂直于视线方向的向量
        Vector3d up = new Vector3d(0, 1, 0);
        Vector3d right = lookDirection.crossProduct(up).normalize();
        Vector3d upPerpendicular = lookDirection.crossProduct(right).normalize();

        // 生成圆形粒子效果
        for (int i = 0; i < num; i++) {
            double angle = 2 * Math.PI / num * i; //角度
            double xOffset = Math.cos(angle) * radius;
            double zOffset = Math.sin(angle) * radius;

            Vector3d particlePosition = player.getEyePosition(1.0f).add(lookDirection.scale(dis)).add(right.scale(xOffset)).add(upPerpendicular.scale(zOffset));

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
    public void onPlayerStoppedUsing(ItemStack bow, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            int max = getUseDuration(bow); //总使用时间
            float velocity = BowItem.getArrowVelocity(max - timeLeft); //速度
            velocity = Math.max(velocity, 1.0f);
            ItemStack itemStack = findArrow(player);

            CompoundNBT nbt = bow.getOrCreateTag();
            boolean flag = nbt.getBoolean("InfinityBow");
            int useTime = getUseTime(timeLeft);

            if (!worldIn.isRemote) {
                AbstractArrowEntity arrow;
                if (!itemStack.isEmpty()) {
                    if (itemStack.getItem() == EndlessItems.infinityArrow.get()) { //无尽箭矢
                        if (flag && useTime >= 200){
                            arrow = new InfinitySuperArrowEntity(EntityRegistry.INFINITY_ARROW.get(), player, worldIn, useTime);
                        }else {
                            arrow = new InfinityArrowEntity(EntityRegistry.INFINITY_ARROW.get(), player, worldIn, true);
                            arrow.setPierceLevel((byte) 3);
                        }
                    } else {
                        arrow = new InfinityArrowSubEntity(EntityRegistry.INFINITY_ARROW_SUB.get(), player, worldIn, itemStack); //普通箭矢
                        arrow.setPierceLevel((byte) 1);
                    }
                } else { //无箭矢
                    ItemStack arrowStack = new ItemStack(Items.ARROW);
                    ArrowItem arrowitem = (ArrowItem)(arrowStack.getItem() instanceof ArrowItem ? arrowStack.getItem() : Items.ARROW);
                    arrow = arrowitem.createArrow(worldIn, bow, entityLiving);
                    arrow.setDamage(Config.SERVER.noArrowDamage.get());
                }
                arrow.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, velocity * 3.0F, 1.0F);
                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bow) > 0) {
                    arrow.setFire(100);
                }
                arrow.setIsCritical(true); //暴击粒子
                arrow.setShooter(player);

                arrow.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                worldIn.addEntity(arrow);
            }

            if (flag && useTime >= 200)
                worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.INFINITY_BOW_SHOOT.get(), SoundCategory.NEUTRAL, 2.0F, 3.0F + (float) Math.ceil((useTime - 200) / 100.0f)  * 0.5F);
            else worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (worldIn.rand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
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
