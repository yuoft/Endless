package com.yuo.endless.Items.Tool;

import com.yuo.endless.Blocks.EndlessBlocks;
import com.yuo.endless.Items.EndlessItems;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags.Blocks;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum EndlessItemTiers implements Tier {

    SKULL_FIRE(1561, 10.0f, 10, 2, 10,
            () -> Ingredient.of(EndlessBlocks.crystalMatrixBlock.get())),
    NEUTRON(3152, 15.0f, 97, 6, 21,
            () -> Ingredient.of(EndlessItems.neutroniumIngot.get())),
    CRYSTAL(2401, 10.0f, 48, 5, 17,
            () -> Ingredient.of(EndlessItems.crystalMatrixIngot.get())),
    //数值无穷表示：Double或Float的POSITIVE_INFINITY（正）或NEGATIVE_INFINITY（负）
    INFINITY_TOOL(9999, Float.MAX_VALUE, 10, 9999, 99,
            () -> Ingredient.of(EndlessItems.infinityIngot.get())),
    INFINITY_SWORD(9999, 9999.0f, Float.POSITIVE_INFINITY, 9999, 99,
            () -> Ingredient.of(EndlessItems.infinityIngot.get()));

    private final int uses;//耐久
    private final float speed;//使用效率
    private final float damage;//工具伤害
    private final int level;//工具等级
    private final int enchantmentValue;//附魔等级
    private final LazyLoadedValue<Ingredient> repairIngredient;//修复材料

    EndlessItemTiers(int maxUses, float efficiency, float attackDamage, int harvestLevel, int enchantability, Supplier<Ingredient> repairMaterial) {
        this.uses = maxUses;
        this.speed = efficiency;
        this.damage = attackDamage;
        this.level = harvestLevel;
        this.enchantmentValue = enchantability;
        this.repairIngredient = new LazyLoadedValue<>(repairMaterial);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Nullable
    public TagKey<Block> getTag() {
        return Blocks.NEEDS_NETHERITE_TOOL;
    }
}

