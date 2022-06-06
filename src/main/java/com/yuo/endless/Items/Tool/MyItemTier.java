package com.yuo.endless.Items.Tool;

import com.yuo.endless.Items.ItemRegistry;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum MyItemTier implements IItemTier {

    SKULLFIRE(444, 10.0f, 10, 2, 10, () -> {
        return Ingredient.fromItems(ItemRegistry.infinityIngot.get());
    }),
    NEUTRON(3152, 15.0f, 10, 6, 21, () -> {
        return Ingredient.fromItems(ItemRegistry.neutroniumIngot.get());
    }),
    CRYSTAL(2401, 10.0f, 8, 5, 17, () -> {
        return Ingredient.fromItems(ItemRegistry.crystalMatrixIngot.get());
    }),
    //数值无穷表示：Double或Float的POSITIVE_INFINITY（正）或NEGATIVE_INFINITY（负）
    INFINITY_TOOL(9999, 9999.0f, 10, 9999, 0, () -> {
        return Ingredient.fromItems(ItemRegistry.infinityIngot.get());
    }),
    INFINITY_SWORD(9999, 9999.0f, Float.POSITIVE_INFINITY, 9999, 0, () -> {
        return Ingredient.fromItems(ItemRegistry.infinityIngot.get());
    });

    private final int maxUses;//耐久
    private final float efficiency;//使用效率
    private final float attackDamage;//工具伤害
    private final int harvestLevel;//工具等级
    private final int enchantability;//附魔等级
    private final LazyValue<Ingredient> repairMaterial;//修复材料

    MyItemTier(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability, Supplier<Ingredient> repairMaterial) {
        this.maxUses = maxUses;
        this.efficiency = efficiency;
        this.attackDamage = attackDamage;
        this.harvestLevel = harvestLevel;
        this.enchantability = enchantability;
        this.repairMaterial = new LazyValue<Ingredient>(repairMaterial);
    }

    @Override
    public int getMaxUses() {
        return this.maxUses;
    }

    @Override
    public float getEfficiency() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }
    }
