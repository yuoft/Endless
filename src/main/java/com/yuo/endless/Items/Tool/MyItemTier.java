package com.yuo.endless.Items.Tool;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public enum MyItemTier implements IItemTier {

    SKULL_FIRE(1561, 10.0f, 10, 2, 10, () -> {
        return Ingredient.fromItems(EndlessBlocks.crystalMatrixBlock.get());
    }),
    NEUTRON(3152, 15.0f, 97, 6, 21, () -> {
        return Ingredient.fromItems(EndlessItems.neutroniumIngot.get());
    }),
    CRYSTAL(2401, 10.0f, 48, 5, 17, () -> {
        return Ingredient.fromItems(EndlessItems.crystalMatrixIngot.get());
    }),
    //数值无穷表示：Double或Float的POSITIVE_INFINITY（正）或NEGATIVE_INFINITY（负）
    INFINITY_TOOL(9999, Float.MAX_VALUE, 10, 9999, 99, () -> {
        return Ingredient.fromItems(EndlessItems.infinityIngot.get());
    }),
    INFINITY_SWORD(9999, 9999.0f, Float.POSITIVE_INFINITY, 9999, 99, () -> {
        return Ingredient.fromItems(EndlessItems.infinityIngot.get());
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
        this.repairMaterial = new LazyValue<>(repairMaterial);
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

    @Nonnull
    @Override
    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }
    }

