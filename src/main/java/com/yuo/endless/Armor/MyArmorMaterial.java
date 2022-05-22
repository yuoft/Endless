package com.yuo.endless.Armor;

import com.yuo.endless.Endless;
import com.yuo.endless.Items.ItemRegistry;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

//盔甲材料类
public enum MyArmorMaterial implements IArmorMaterial {
	//---------材质---耐久值----------护甲值-------附魔能力--------音效----------------------盔甲韧性- 击退抗性-修复材料
	INFINITY(Endless.MOD_ID + ":" + "infinity", 999, new int[] { 6, 10, 12, 6 }, 99, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 6, 1,() -> Ingredient.EMPTY),
	NEUTRON(Endless.MOD_ID + ":" + "neutron", 53, new int[] { 6, 10, 12, 6 }, 21, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 5, 0.2f,
			() -> Ingredient.fromItems(ItemRegistry.neutroniumIngot.get())),
	CRYSTAL(Endless.MOD_ID + ":" + "crystal", 41, new int[] { 4, 8, 10, 4 }, 17, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3, 0.1f,
			() -> Ingredient.fromItems(ItemRegistry.crystalMatrixIngot.get()));

	private static final int[] MAX_DAMAGE_ARRAY = new int[] { 13, 15, 16, 11 };
	private final String name;
	private final int maxDamageFactor;
	private final int[] damageReductionAmountArray;
	private final int enchantability;
	private final SoundEvent soundEvent;
	private final float toughness;
	private final LazyValue<Ingredient> repairMaterial;
	private final float knockbackResistance;

	MyArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial) {
		this.name = name;
		this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmountArray = damageReductionAmountArray;
		this.enchantability = enchantability;
		this.soundEvent = soundEvent;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairMaterial = new LazyValue<>(repairMaterial);
	   }

	public int getDurability(EquipmentSlotType slotIn) {
		return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
	}

	public int getDamageReductionAmount(EquipmentSlotType slotIn) {
		return this.damageReductionAmountArray[slotIn.getIndex()];
	}

	public int getEnchantability() {
		return this.enchantability;
	}

	@Nonnull
	public SoundEvent getSoundEvent() {
		return this.soundEvent;
	}

	@Nonnull
	public Ingredient getRepairMaterial() {
		return this.repairMaterial.getValue();
	}

	@OnlyIn(Dist.CLIENT)
	@Nonnull
	public String getName() {
		return this.name;
	}

	public float getToughness() {
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}

}
