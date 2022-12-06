package com.yuo.endless.Armor;

import com.yuo.endless.Config.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Tool.ColorText;
import com.yuo.endless.Items.Tool.EndlessItemEntity;
import com.yuo.endless.tab.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class InfinityArmor extends ArmorItem {

    public static AttributeModifier modifierWalk = new AttributeModifier(UUID.fromString("d164b605-3715-49ca-bea3-1e67080d3f63"), Endless.MOD_ID + ":movement_speed", 0.1 * Config.SERVER.infinityLegsWalk.get(), AttributeModifier.Operation.ADDITION);
    public static AttributeModifier modifierFly = new AttributeModifier(UUID.fromString("bf93174c-8a89-42ed-a702-e6fd99c28be2"), Endless.MOD_ID + ":flying_speed", 0.15, AttributeModifier.Operation.ADDITION);

    public InfinityArmor(EquipmentSlotType slot) {
        super(MyArmorMaterial.INFINITY, slot, new Properties().maxStackSize(1).group(ModGroup.endless).isImmuneToFire());
    }

    //不会触发末影人仇恨
    @Override
    public boolean isEnderMask(ItemStack stack, PlayerEntity player, EndermanEntity endermanEntity) {
        return true;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return false;
    }

    //猪灵中立
    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }

    //盔甲在身上时触发效果
    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        Item item = stack.getItem();
        if (item == EndlessItems.infinityHead.get()) {
            if (player.areEyesInFluid(FluidTags.WATER)) { //玩家视线在水中
                player.setAir(300);
            }
            player.getFoodStats().addStats(20, 20f); //饱腹
            if (stack.getOrCreateTag().getBoolean("flag"))
                player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 300, 0)); //夜视
        }
        if (item == EndlessItems.infinityChest.get()) {
            //清除所有负面效果
            Collection<EffectInstance> effects = player.getActivePotionEffects();
            if (effects.size() > 0) {
                List<Effect> bad = new ArrayList<>();
                effects.forEach((e) -> {
                    if (!e.getPotion().isBeneficial())
                        bad.add(e.getPotion());
                });
                if (bad.size() > 0) {
                    //player.clearActivePotions();
                    bad.forEach(player::removePotionEffect);
                }
            }
        }
        if (item == EndlessItems.infinityLegs.get()) {
            if (player.isBurning()) player.extinguish();//着火时熄灭
            player.isImmuneToFire(); //免疫火伤
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        int damage = stack.getDamage();
        if (damage > 0){
            stack.getOrCreateTag().putInt("Damage", 0);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) { //切换无尽装备模式
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (playerIn.isSneaking()) {
            CompoundNBT tags = stack.getTag();
            if (tags == null) {
                tags = new CompoundNBT();
                stack.setTag(tags);
            }
            tags.putBoolean("flag", !tags.getBoolean("flag"));
            playerIn.swingArm(handIn); //摆臂
            return ActionResult.resultSuccess(stack);
        } else return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (slot == EquipmentSlotType.HEAD) {
            tooltip.add(new TranslationTextComponent("endless.text.itemInfo.infinity_helmet"));
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
                tooltip.add(new TranslationTextComponent("endless.text.itemInfo.infinity_helmet1"));
        }
        if (slot == EquipmentSlotType.CHEST) {
            tooltip.add(new TranslationTextComponent("endless.text.itemInfo.infinity_chestplate"));
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
                tooltip.add(new StringTextComponent(ColorText.makeSANIC("+" + Config.SERVER.infinityChestFly.get() + "00% FlySpeed")));
        }
        if (slot == EquipmentSlotType.LEGS) {
            tooltip.add(new TranslationTextComponent("endless.text.itemInfo.infinity_leggings"));
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
                tooltip.add(new StringTextComponent(ColorText.makeSANIC("+" + Config.SERVER.infinityLegsWalk.get() + "00% WalkSpeed")));
        }
        if (slot == EquipmentSlotType.FEET) {
            tooltip.add(new TranslationTextComponent("endless.text.itemInfo.infinity_boots"));
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
                tooltip.add(new StringTextComponent(ColorText.makeSANIC("+" + Config.SERVER.infinityFeetJump.get() + "00% JumpHeight")));
//				tooltip.add(new StringTextComponent(TextFormatting.BLUE + "+" + TextFormatting.ITALIC + "400" +
//						TextFormatting.RESET + "" + TextFormatting.BLUE + "% JumpHeight"));
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
