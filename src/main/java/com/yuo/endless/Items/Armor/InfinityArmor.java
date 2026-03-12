package com.yuo.endless.Items.Armor;

import com.yuo.endless.Client.Model.InfinityArmorModel;
import com.yuo.endless.Config;
import com.yuo.endless.Endless;
import com.yuo.endless.Entity.EndlessItemEntity;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Client.ColorText;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class InfinityArmor extends ArmorItem {

    public static AttributeModifier modifierFly = new AttributeModifier(UUID.fromString("bf93174c-8a89-42ed-a702-e6fd99c28be2"), Endless.MOD_ID + ":flying_speed", 0.15, AttributeModifier.Operation.ADDITION);

    public InfinityArmor(Type slot) {
        super(EndlessArmorMaterials.INFINITY, slot, new Properties().stacksTo(1).fireResistant());
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
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        Item item = stack.getItem();
        if (item == EndlessItems.infinityHead.get() && !player.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            if (player.isEyeInFluid(FluidTags.WATER)) { //玩家视线在水中
                player.setAirSupply(300);
            }
            player.getFoodData().eat(20, 20f); //饱腹
            if (stack.getOrCreateTag().getBoolean("flag"))
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0)); //夜视
        }
        if (item == EndlessItems.infinityChest.get() && !player.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
           clearBadEffects(player);
        }
        if (item == EndlessItems.infinityLegs.get() && !player.getItemBySlot(EquipmentSlot.LEGS).isEmpty()) {
            if (player.isOnFire()) player.clearFire();//着火时熄灭
            player.fireImmune(); //免疫火伤
        }
    }

    /**
     *  清除所有负面效果
     */
    public static void clearBadEffects(LivingEntity living) {
        Collection<MobEffectInstance> effects = living.getActiveEffects();
        if (!effects.isEmpty()) {
            List<MobEffect> bad = new ArrayList<>();
            effects.forEach((e) -> {
                if (!e.getEffect().isBeneficial())
                    bad.add(e.getEffect());
            });
            if (!bad.isEmpty()) {
                bad.forEach(living::removeEffect);
            }
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
        EquipmentSlot slot = this.getEquipmentSlot();
        if (type == Type.HELMET) {
            components.add(Component.translatable("endless.text.itemInfo.infinity_helmet"));
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
                components.add(Component.translatable("endless.text.itemInfo.infinity_helmet1"));
        }
        if (type == Type.CHESTPLATE) {
            components.add(Component.translatable("endless.text.itemInfo.infinity_chestplate"));
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
                components.add(Component.keybind(ColorText.makeSANIC("+" + Config.SERVER.infinityChestFly.get() + "00% FlySpeed")));
        }
        if (type == Type.LEGGINGS) {
            components.add(Component.translatable("endless.text.itemInfo.infinity_leggings"));
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
                components.add(Component.keybind(ColorText.makeSANIC("+" + Config.SERVER.infinityLegsWalk.get() + "00% WalkSpeed")));
        }
        if (type == Type.BOOTS) {
            components.add(Component.translatable("endless.text.itemInfo.infinity_boots"));
            if (stack.hasTag() && stack.getOrCreateTag().getBoolean("flag"))
                components.add(Component.keybind(ColorText.makeSANIC("+" + Config.SERVER.infinityFeetJump.get() + "00% JumpHeight")));
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

    @org.jetbrains.annotations.Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return "endless:textures/models/infinity_armor.png";
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull HumanoidModel<Player> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemstack, EquipmentSlot armorSlot, HumanoidModel _deafult) {
                InfinityArmorModel model =
                        armorSlot == EquipmentSlot.LEGS
                                ? new InfinityArmorModel(InfinityArmorModel.createMesh(new CubeDeformation(1.0F), 0.0F, true).getRoot().bake(64, 64))
                                : new InfinityArmorModel(InfinityArmorModel.createMesh(new CubeDeformation(1.0F), 0.0F, false).getRoot().bake(64, 64));
                model.update(entityLiving);
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
