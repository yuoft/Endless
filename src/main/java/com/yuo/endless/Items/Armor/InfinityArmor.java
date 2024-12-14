package com.yuo.endless.Items.Armor;

import com.yuo.endless.Client.Model.InfinityArmorModel;
import com.yuo.endless.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.EndlessTab;
import com.yuo.endless.Entity.EndlessItemEntity;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Tool.ColorText;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class InfinityArmor extends ArmorItem {

    public static AttributeModifier modifierWalk = new AttributeModifier(UUID.fromString("d164b605-3715-49ca-bea3-1e67080d3f63"), Endless.MOD_ID + ":movement_speed", 0.1 * Config.SERVER.infinityLegsWalk.get(), AttributeModifier.Operation.ADDITION);
    public static AttributeModifier modifierFly = new AttributeModifier(UUID.fromString("bf93174c-8a89-42ed-a702-e6fd99c28be2"), Endless.MOD_ID + ":flying_speed", 0.15, AttributeModifier.Operation.ADDITION);

    public InfinityArmor(EquipmentSlot slot) {
        super(EndlessArmorMaterials.INFINITY, slot, new Properties().stacksTo(1).tab(EndlessTab.endless).fireResistant());
    }

    //不会触发末影人仇恨
    @Override
    public boolean isEnderMask(ItemStack stack, Player player, EnderMan endermanEntity) {
        return true;
    }

    //猪灵中立
    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }

    //盔甲在身上时触发效果
    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        Item item = stack.getItem();
        if (item == EndlessItems.infinityHead.get()) {
            if (player.isEyeInFluid(FluidTags.WATER)) { //玩家视线在水中
                player.setAirSupply(300);
            }
            player.getFoodData().eat(20, 20f); //饱腹
            if (stack.getOrCreateTag().getBoolean("flag"))
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0)); //夜视
        }
        if (item == EndlessItems.infinityChest.get()) {
            //清除所有负面效果
            Collection<MobEffectInstance> effects = player.getActiveEffects();
            if (!effects.isEmpty()) {
                List<MobEffect> bad = new ArrayList<>();
                effects.forEach((e) -> {
                    if (!e.getEffect().isBeneficial())
                        bad.add(e.getEffect());
                });
                if (!bad.isEmpty()) {
                    //player.clearActivePotions();
                    bad.forEach(player::removeEffect);
                }
            }
        }
        if (item == EndlessItems.infinityLegs.get()) {
            if (player.isOnFire()) player.clearFire();//着火时熄灭
            player.fireImmune(); //免疫火伤
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        int damage = stack.getDamageValue();
        if (damage > 0){
            stack.getOrCreateTag().putInt("Damage", 0);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.isCrouching()) {
            CompoundTag tags = stack.getTag();
            if (tags == null) {
                tags = new CompoundTag();
                stack.setTag(tags);
            }
            tags.putBoolean("flag", !tags.getBoolean("flag"));
            player.swing(hand); //摆臂
            return InteractionResultHolder.success(stack);
        } else return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> components, TooltipFlag pIsAdvanced) {
        if (slot == EquipmentSlot.HEAD) {
            components.add(new TranslatableComponent("endless.text.itemInfo.infinity_helmet"));
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
                components.add(new TranslatableComponent("endless.text.itemInfo.infinity_helmet1"));
        }
        if (slot == EquipmentSlot.CHEST) {
            components.add(new TranslatableComponent("endless.text.itemInfo.infinity_chestplate"));
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
                components.add(new TextComponent(ColorText.makeSANIC("+" + Config.SERVER.infinityChestFly.get() + "00% FlySpeed")));
        }
        if (slot == EquipmentSlot.LEGS) {
            components.add(new TranslatableComponent("endless.text.itemInfo.infinity_leggings"));
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
                components.add(new TextComponent(ColorText.makeSANIC("+" + Config.SERVER.infinityLegsWalk.get() + "00% WalkSpeed")));
        }
        if (slot == EquipmentSlot.FEET) {
            components.add(new TranslatableComponent("endless.text.itemInfo.infinity_boots"));
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
                components.add(new TextComponent(ColorText.makeSANIC("+" + Config.SERVER.infinityFeetJump.get() + "00% JumpHeight")));
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
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> stacks) {
        if (this.allowdedIn(tab)){
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putBoolean("Unbreakable",true);
            stacks.add(stack);
        }
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "endless:textures/models/infinity_armor.png";
    }

//    @OnlyIn(Dist.CLIENT)
//    public HumanoidModel<?> getArmorModel(LivingEntity entity, ItemStack itemstack, EquipmentSlot armorSlot, HumanoidModel<?> _deafult) {
//        InfinityArmorModel model = (armorSlot == EquipmentSlot.LEGS) ? (new InfinityArmorModel(0.5F)).setLegs(true) : new InfinityArmorModel(1.0F);
//        model.update(entity, itemstack, armorSlot);
//        return model;
//    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public Font getFont(ItemStack stack) {
                return IItemRenderProperties.super.getFont(stack);
            }

            @NotNull
            @Override
            public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                InfinityArmorModel model = (armorSlot == EquipmentSlot.LEGS) ? (new InfinityArmorModel(0.5F)).setLegs(true) : new InfinityArmorModel(1.0F);
                model.update(entityLiving, itemStack, armorSlot);
                return model;
            }
        });
    }

    @Nullable
    @Override
    public Entity createEntity(Level world, Entity location, ItemStack itemstack) {
        return new EndlessItemEntity(world, location, itemstack);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

}
