package com.yuo.endless.Items.Armor;

import com.yuo.endless.Endless;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

//盔甲材料类
public enum EndlessArmorMaterials implements ArmorMaterial {
	//---------材质---耐久值----------护甲值-------附魔能力--------音效----------------------盔甲韧性- 击退抗性-修复材料
	INFINITY(Endless.MOD_ID + ":" + "infinity", 999, new int[] { 7, 13, 15, 9 }, 99, SoundEvents.ARMOR_EQUIP_DIAMOND, 6, 1,
			() -> Ingredient.of(EndlessItems.infinityIngot.get())),
	NEUTRON(Endless.MOD_ID + ":" + "neutron", 199, new int[] { 19, 25, 29, 18 }, 21, SoundEvents.ARMOR_EQUIP_DIAMOND, 5, 0.2f,
			() -> Ingredient.of(EndlessItems.neutroniumIngot.get())),
	CRYSTAL(Endless.MOD_ID + ":" + "crystal", 99, new int[] { 11, 16, 19, 10 }, 17, SoundEvents.ARMOR_EQUIP_DIAMOND, 3, 0.1f,
			() -> Ingredient.of(EndlessItems.crystalMatrixIngot.get()));

	private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
	private final String name;
	private final int durabilityMultiplier;
	private final int[] slotProtections;
	private final int enchantmentValue;
	private final SoundEvent sound;
	private final float toughness;
	private final float knockbackResistance;
	private final LazyLoadedValue<Ingredient> repairIngredient;

	EndlessArmorMaterials(String pName, int pDurabilityMultiplier, int[] pSlotProtections, int pEnchantmentValue, SoundEvent pSound, float pToughness, float pKnockbackResistance, Supplier pRepairIngredient) {
		this.name = pName;
		this.durabilityMultiplier = pDurabilityMultiplier;
		this.slotProtections = pSlotProtections;
		this.enchantmentValue = pEnchantmentValue;
		this.sound = pSound;
		this.toughness = pToughness;
		this.knockbackResistance = pKnockbackResistance;
		this.repairIngredient = new LazyLoadedValue(pRepairIngredient);
	}

	public int getDurabilityForSlot(EquipmentSlot pSlot) {
		return HEALTH_PER_SLOT[pSlot.getIndex()] * this.durabilityMultiplier;
	}

	public int getDefenseForSlot(EquipmentSlot pSlot) {
		return this.slotProtections[pSlot.getIndex()];
	}

	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	public SoundEvent getEquipSound() {
		return this.sound;
	}

	public Ingredient getRepairIngredient() {
		return (Ingredient)this.repairIngredient.get();
	}

	public String getName() {
		return this.name;
	}

	public float getToughness() {
		return this.toughness;
	}

	public float getKnockbackResistance() {
		return this.knockbackResistance;
	}

}
