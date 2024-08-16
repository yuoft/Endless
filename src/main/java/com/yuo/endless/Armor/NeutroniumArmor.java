package com.yuo.endless.Armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.endless.Items.EndlessItems;
import com.yuo.endless.Items.Tool.Modifiers;
import com.yuo.endless.EndlessTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import vazkii.botania.api.item.IPhantomInkable;
import vazkii.botania.api.mana.IManaDiscountArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.client.core.handler.TooltipHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class NeutroniumArmor extends ArmorItem implements IManaDiscountArmor, IManaUsingItem, IPhantomInkable {
    private static final String TAG_PHANTOM_INK = "phantomInk";

    public NeutroniumArmor(IArmorMaterial material, EquipmentSlotType slot) {
        super(material, slot, new Properties().maxStackSize(1).group(EndlessTab.endless));
    }

    @Override
    public float getDiscount(ItemStack stack, int slot, PlayerEntity player, @Nullable ItemStack tool) {
        return hasArmorSet(player) ? 0.25f : 0f;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = getItem().getAttributeModifiers(slot);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(multimap);
        Item item = stack.getItem();
        if (slot == EquipmentSlotType.HEAD && item == EndlessItems.neutroniumHead.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(0,3d));
                builder.put(ForgeMod.SWIM_SPEED.get(), Modifiers.getModifierSwim(0,-0.04d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(0,-0.04d));
            return builder.build();
        }else if (slot == EquipmentSlotType.CHEST && item == EndlessItems.neutroniumChest.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(1,8d));
                builder.put(ForgeMod.SWIM_SPEED.get(), Modifiers.getModifierSwim(1,-0.04d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(1,-0.04d));
            return builder.build();
        }else if (slot == EquipmentSlotType.LEGS && item == EndlessItems.neutroniumLegs.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(2,6d));
                builder.put(ForgeMod.SWIM_SPEED.get(), Modifiers.getModifierSwim(2,-0.04d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(2,-0.04d));
            return builder.build();
        }else if (slot == EquipmentSlotType.FEET && item == EndlessItems.neutroniumFeet.get()){
                builder.put(Attributes.MAX_HEALTH, Modifiers.getModifierHealth(3,3d));
                builder.put(ForgeMod.SWIM_SPEED.get(), Modifiers.getModifierSwim(3,-0.04d));
                builder.put(Attributes.MOVEMENT_SPEED, Modifiers.getModifierSpeed(3,-0.04d));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean usesMana(ItemStack itemStack) {
        return true;
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return ToolCommons.damageItemIfPossible(stack, amount, entity, 25);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
        TooltipHandler.addOnShift(list, () -> addInformationAfterShift(stack, world, list, flags));
    }

    private static final LazyValue<ItemStack[]> armorSet = new LazyValue<>(() -> new ItemStack[] {
            new ItemStack(EndlessItems.neutroniumHead.get()),
            new ItemStack(EndlessItems.neutroniumChest.get()),
            new ItemStack(EndlessItems.neutroniumLegs.get()),
            new ItemStack(EndlessItems.neutroniumFeet.get()),
    });

    public ItemStack[] getArmorSetStacks() {
        return armorSet.getValue();
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformationAfterShift(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
        PlayerEntity player = Minecraft.getInstance().player;
        list.add((new TranslationTextComponent("endless.armorset.neutronium.desc")).mergeStyle(TextFormatting.GRAY));
        list.add(getArmorSetTitle(player));
        ItemStack[] stacks = getArmorSetStacks();
        for (ItemStack armor : stacks) {
            IFormattableTextComponent cmp = new StringTextComponent(" - ").appendSibling(armor.getDisplayName());
            EquipmentSlotType slot = ((ArmorItem) armor.getItem()).getEquipmentSlot();
            cmp.mergeStyle(hasArmorSetItem(player, slot) ? TextFormatting.GREEN : TextFormatting.GRAY);
            list.add(cmp);
        }
        if (hasPhantomInk(stack)) {
            list.add(new TranslationTextComponent("botaniamisc.hasPhantomInk").mergeStyle(TextFormatting.GRAY));
        }
    }

    public boolean hasArmorSet(PlayerEntity player) {
        return hasArmorSetItem(player, EquipmentSlotType.HEAD) && hasArmorSetItem(player, EquipmentSlotType.CHEST) && hasArmorSetItem(player, EquipmentSlotType.LEGS) && hasArmorSetItem(player, EquipmentSlotType.FEET);
    }

    public boolean hasArmorSetItem(PlayerEntity player, EquipmentSlotType slot) {
        if (player == null || player.inventory == null || player.inventory.armorInventory == null) {
            return false;
        }

        ItemStack stack = player.getItemStackFromSlot(slot);
        if (stack.isEmpty()) {
            return false;
        }

        switch (slot) {
            case HEAD:
                return stack.getItem() == EndlessItems.neutroniumHead.get();
            case CHEST:
                return stack.getItem() == EndlessItems.neutroniumChest.get();
            case LEGS:
                return stack.getItem() == EndlessItems.neutroniumLegs.get();
            case FEET:
                return stack.getItem() == EndlessItems.neutroniumFeet.get();
        }

        return false;
    }

    private int getSetPiecesEquipped(PlayerEntity player) {
        int pieces = 0;
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (slot.getSlotType() == EquipmentSlotType.Group.ARMOR && hasArmorSetItem(player, slot)) {
                pieces++;
            }
        }

        return pieces;
    }

    public IFormattableTextComponent getArmorSetName() {
        return new TranslationTextComponent("endless.armorset.neutronium.name");
    }

    private ITextComponent getArmorSetTitle(PlayerEntity player) {
        ITextComponent end = getArmorSetName()
                .appendString(" (" + getSetPiecesEquipped(player) + "/" + getArmorSetStacks().length + ")")
                .mergeStyle(TextFormatting.GRAY);
        return new TranslationTextComponent("botaniamisc.armorset")
                .appendString(" ")
                .appendSibling(end);
    }

    @Override
    public boolean hasPhantomInk(ItemStack stack) {
        return ItemNBTHelper.getBoolean(stack, TAG_PHANTOM_INK, false);
    }

    @Override
    public void setPhantomInk(ItemStack stack, boolean ink) {
        ItemNBTHelper.setBoolean(stack, TAG_PHANTOM_INK, ink);
    }
}
